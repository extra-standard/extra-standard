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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import de.extrastandard.api.model.execution.IStatus;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.persistence.repository.InputDataRepository;
import de.extrastandard.persistence.repository.StatusRepository;

/**
 * JPA Implementierung von {@link IInputData}.
 *
 * @author Thorsten Vogel
 * @version $Id$
 */
@Configurable(preConstruction = true)
@Entity
@Table(name = "INPUT_DATA")
public class InputData extends AbstractEntity implements IInputData {

	private static final long serialVersionUID = 1L;

	@Column(name = "input_identifier")
	private String inputIdentifier;

	@Column(name = "hashcode")
	private String hashcode;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "status_id")
	private Status status;

	@Column(name = "errorcode")
	private String errorCode;

	@Column(name = "errormessage")
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
	 * Erzeugt eine neue IInputData-Instanz und legt gleichzeitig eine neue Transition an.
	 *
	 * @param inputIdentifier Identifier
	 * @param hashCode Hashcode der Daten
	 * @param qualifier Qualifizierung
	 */
	public InputData(final String inputIdentifier, final String hashCode,
			final String qualifier) {
		Assert.notNull(inputIdentifier, "inputIdentifier must be specified");
		Assert.notNull(qualifier, "qualifier must be specified");

		this.inputIdentifier = inputIdentifier;
		this.hashcode = hashCode;
		saveOrUpdate();

		// create a transition
		InputDataTransition transition = new InputDataTransition();
		transition.setPreviousStatus(null);
		transition.setCurrentStatus(statusRepository.findByName(PersistentStatus.INITIAL.toString()));
		transition.setTransitionDate(new Date());
		transition.setQualifier(qualifier);
		transition.setInputData(this);
		transition.saveOrUpdate();

		// update this with the newly created transition
		this.lastTransition = transition;
		saveOrUpdate();
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputData#updateProgress(de.extrastandard.api.model.execution.IStatus, java.lang.String)
	 */
	@Override
	@Transactional
	public void updateProgress(final IStatus newStatus, final String qualifier) {
		Assert.notNull(newStatus, "newStatus must be specified");
		Assert.notNull(qualifier, "qualifier must be specified");

		Date lastTransitionDate = this.lastTransition.getTransitionDate();

		InputDataTransition currentTransition = new InputDataTransition();
		currentTransition.setPreviousStatus(this.status);
		currentTransition.setCurrentStatus(statusRepository
				.findByName(newStatus.getName()));
		currentTransition.setTransitionDate(new Date());
		currentTransition.setQualifier(qualifier);
		currentTransition.setDuration(currentTransition.getTransitionDate()
				.getTime() - lastTransitionDate.getTime());
		currentTransition.setInputData(this);
		currentTransition.saveOrUpdate();

		this.lastTransition = currentTransition;
		this.status = (Status) newStatus;
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
		saveOrUpdate();
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputData#failed(de.extrastandard.api.exception.ExtraRuntimeException)
	 */
	@Override
	public void failed(final ExtraRuntimeException exception) {
		this.errorCode = exception.getCode().name();
		this.errorMessage = exception.getMessage();
		saveOrUpdate();
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputData#success(java.lang.String)
	 */
	@Override
	public void success(final String responseId) {
		this.responseId = responseId;
		saveOrUpdate();
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputData#hasError()
	 */
	@Override
	public boolean hasError() {
		return StringUtils.hasText(errorCode)
				|| StringUtils.hasText(errorMessage);
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

	public void setStatus(final IStatus status) {
		this.status = (Status) status;
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
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InputData [inputIdentifier=");
		builder.append(inputIdentifier);
		builder.append(", hashcode=");
		builder.append(hashcode);
		builder.append(", status=");
		builder.append(status);
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
