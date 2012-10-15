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
import org.springframework.util.Assert;

import de.extrastandard.api.model.content.ISingleResponseData;
import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.IPhaseConnection;
import de.extrastandard.api.model.execution.IProcedure;
import de.extrastandard.persistence.repository.InputDataRepository;

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

	// private static final Logger logger =
	// LoggerFactory.getLogger(InputData.class);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "input_data_entity_seq_gen")
	@SequenceGenerator(name = "input_data_entity_seq_gen", sequenceName = "seq_input_data_id")
	private Long id;

	@Column(name = "input_identifier")
	private String inputIdentifier;

	@Column(name = "hashcode")
	private String hashcode;

	@Column(name = "return_text")
	private String returnText;

	@Column(name = "return_code")
	private String returnCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "execution_id")
	private Execution execution;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "next_phase_connection_id")
	private PhaseConnection nextPhaseConnection;

	@Column(name = "response_id")
	private String responseId;

	@Column(name = "request_id")
	private String requestId;

	@Transient
	@Inject
	@Named("inputDataRepository")
	private transient InputDataRepository repository;

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
	}

	public InputData(final Execution execution, final String serverResponseId) {
		Assert.notNull(serverResponseId, "ServerResponseId must be specified");
		// Annahme DBQuery InputData fordert die Daten mit dem vorher von dem
		// Server erhalteten ResponseId
		this.requestId = serverResponseId;
		this.execution = execution;
		saveOrUpdate();
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

	public void setInputIdentifier(final String inputIdentifier) {
		this.inputIdentifier = inputIdentifier;
	}

	public void setHashcode(final String hashcode) {
		this.hashcode = hashcode;
	}

	public void setExecution(final IExecution execution) {
		this.execution = (Execution) execution;
	}

	public void setResponseId(final String responseId) {
		this.responseId = responseId;
	}

	@Override
	public String calculateRequestId() {
		final IProcedure procedure = execution.getProcedure();
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(procedure.getShortKey()).append("_").append(id);
		return stringBuilder.toString();
	}

	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @return the requestId
	 */
	@Override
	public String getRequestId() {
		return requestId;
	}

	/**
	 * @param requestId
	 *            the requestId to set
	 */
	@Override
	public void setRequestId(final String requestId) {
		this.requestId = requestId;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("InputData [inputIdentifier=");
		builder.append(inputIdentifier);
		builder.append(", hashcode=");
		builder.append(hashcode);
		builder.append(", execution=");
		builder.append(execution);
		builder.append(", responseId=");
		builder.append(responseId);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * @return the returnText
	 */
	@Override
	public String getReturnText() {
		return returnText;
	}

	/**
	 * @return the returnCode
	 */
	@Override
	public String getReturnCode() {
		return returnCode;
	}

	/**
	 * @return the nextPhasenConnection
	 */
	@Override
	public IPhaseConnection getNextPhaseConnection() {
		return nextPhaseConnection;
	}

	/**
	 * @param nextPhasenConnection
	 *            the nextPhasenConnection to set
	 */
	public void setNextPhaseConnection(final PhaseConnection nextPhaseConnection) {
		this.nextPhaseConnection = nextPhaseConnection;
	}

	@Override
	public void transmitted(final ISingleResponseData singleResponseData) {
		this.responseId = singleResponseData.getResponseId();
		this.returnCode = singleResponseData.getReturnCode();
		this.returnText = singleResponseData.getReturnText();
		repository.save(this);

	}

}
