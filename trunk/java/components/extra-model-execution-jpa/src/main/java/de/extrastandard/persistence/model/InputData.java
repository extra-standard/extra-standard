/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.extrastandard.persistence.model;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import de.extrastandard.api.exception.ExtraRuntimeException;
import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.IInputDataTransition;
import de.extrastandard.api.model.execution.IProcedure;
import de.extrastandard.api.model.execution.IStatus;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.persistence.repository.InputDataRepository;
import de.extrastandard.persistence.repository.StatusRepository;

/**
 * JPA Implementierung von {@link IInputData}.
 * 
 * @author Thorsten Vogel
 * @version $Id: InputData.java 562 2012-09-06 14:12:43Z thorstenvogel@gmail.com
 *          $
 */
@Configurable(preConstruction = true)
@Entity
@Table(name = "INPUT_DATA")
public class InputData extends AbstractEntity implements IInputData {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "input_data_entity_seq_gen")
	@SequenceGenerator(name = "input_data_entity_seq_gen", sequenceName = "seq_input_data_id")
	private Long id;

	@Column(name = "input_identifier")
	private String inputIdentifier;

	@Column(name = "hashcode")
	private String hashcode;

	@Column(name = "error_code")
	private String errorCode;

	@Column(name = "error_message")
	private String errorMessage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "execution_id")
	private Execution execution;

	@Column(name = "responseid")
	private String responseId;

	@ManyToOne
	@JoinColumn(name = "last_transition_id")
	private InputDataTransition lastTransition;

	@Transient
	@Inject
	@Named("inputDataRepository")
	private transient InputDataRepository repository;

	@Transient
	@Inject
	@Named("statusRepository")
	private transient StatusRepository statusRepository;

	/**
	 * Dieser Konstruktur wird ausschliesslich durch das ORM Tool genutzt.
	 * Andere Benutzer müssen immer den Konstruktor
	 * {@link #InputData(String, String, String)} benutzen.
	 */
	public InputData() {
	}

	/**
	 * Erzeugt eine neue IInputData-Instanz und legt gleichzeitig eine neue
	 * Transition an.
	 * 
	 * @param inputIdentifier
	 *            Identifier
	 * @param hashCode
	 *            Hashcode der Daten
	 * @param qualifier
	 *            Qualifizierung
	 */
	public InputData(final Execution execution, final String inputIdentifier, final String hashCode) {
		Assert.notNull(inputIdentifier, "inputIdentifier must be specified");
		this.inputIdentifier = inputIdentifier;
		this.hashcode = hashCode;
		this.execution = execution;
		saveOrUpdate();
		final InputDataTransition transition = new InputDataTransition(this);
		this.lastTransition = transition;
		saveOrUpdate();
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputData#updateProgress(de.extrastandard.api.model.execution.IStatus,
	 *      java.lang.String)
	 */
	@Override
	@Transactional
	public void updateProgress(final IStatus newStatus) {
		Assert.notNull(newStatus, "newStatus must be specified");

		final IInputDataTransition lastTransition = this.getLastTransition();
		final IStatus currentStatus = lastTransition.getCurrentStatus();

		final Date lastTransitionDate = this.lastTransition.getTransitionDate();
		final InputDataTransition transition = new InputDataTransition(this, currentStatus, newStatus,
				lastTransitionDate);
		this.lastTransition = transition;
		saveOrUpdate();
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputData#failed(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void failed(final String errorCode, final String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		// TODO transition / status setzen
		saveOrUpdate();
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputData#failed(de.extrastandard.api.exception.ExtraRuntimeException)
	 */
	@Override
	public void failed(final ExtraRuntimeException exception) {
		this.errorCode = exception.getCode().name();
		this.errorMessage = exception.getMessage();
		// TODO transition / status setzen
		saveOrUpdate();
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputData#success(java.lang.String)
	 */
	@Override
	@Transactional
	public void success(final String responseId, final PhaseQualifier phaseQualifier) {
		this.responseId = responseId;
		final IStatus currentStatus = this.lastTransition.getCurrentStatus();
		final IProcedure procedure = this.execution.getProcedure();
		final IStatus newStatus = procedure.getPhaseEndStatus(phaseQualifier);
		final Date lastTransitionDate = this.lastTransition.getTransitionDate();
		final InputDataTransition transition = new InputDataTransition(this, currentStatus, newStatus,
				lastTransitionDate);
		this.lastTransition = transition;
		saveOrUpdate();
		if (procedure.isProcedureEndStatus(newStatus)) {
			// wechseln zu dem Status Done
			final Status statusDone = statusRepository.findByName(PersistentStatus.DONE.toString());
			final InputDataTransition transitionDone = new InputDataTransition(this, newStatus, statusDone,
					lastTransitionDate);
			this.lastTransition = transitionDone;
			saveOrUpdate();
			// Auch Execution wird abgeschlossen
			this.execution.setEndTime(new Date());
			execution.saveOrUpdate();
		}
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputData#hasError()
	 */
	@Override
	public boolean hasError() {
		return StringUtils.hasText(errorCode) || StringUtils.hasText(errorMessage);
	}

	/**
	 * @see de.extrastandard.api.model.execution.PersistentEntity#saveOrUpdate()
	 */
	@Override
	public void saveOrUpdate() {
		repository.save(this);
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputData#getInputIdentifier()
	 */
	@Override
	public String getInputIdentifier() {
		return inputIdentifier;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputData#getHashcode()
	 */
	@Override
	public String getHashcode() {
		return hashcode;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputData#getErrorCode()
	 */
	@Override
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputData#getErrorMessage()
	 */
	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputData#getResponseId()
	 */
	@Override
	public String getResponseId() {
		return responseId;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputData#getExecution()
	 */
	@Override
	public IExecution getExecution() {
		return execution;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputData#getLastTransition()
	 */
	@Override
	public IInputDataTransition getLastTransition() {
		return lastTransition;
	}

	public void setInputIdentifier(final String inputIdentifier) {
		this.inputIdentifier = inputIdentifier;
	}

	public void setHashcode(final String hashcode) {
		this.hashcode = hashcode;
	}

	public void setErrorCode(final String errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setExecution(final IExecution execution) {
		this.execution = (Execution) execution;
	}

	public void setResponseId(final String responseId) {
		this.responseId = responseId;
	}

	public void setLastTransition(final IInputDataTransition lastTransition) {
		this.lastTransition = (InputDataTransition) lastTransition;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("InputData [inputIdentifier=");
		builder.append(inputIdentifier);
		builder.append(", hashcode=");
		builder.append(hashcode);
		builder.append(", errorCode=");
		builder.append(errorCode);
		builder.append(", errorMessage=");
		builder.append(errorMessage);
		builder.append(", execution=");
		builder.append(execution);
		builder.append(", responseId=");
		builder.append(responseId);
		builder.append(", lastTransition=");
		builder.append(lastTransition);
		builder.append("]");
		return builder.toString();
	}

}
