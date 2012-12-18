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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;

import de.extrastandard.api.exception.ExtraCoreRuntimeException;
import de.extrastandard.api.model.content.ICriteriaQueryInputData;
import de.extrastandard.api.model.content.ISingleContentInputData;
import de.extrastandard.api.model.content.ISingleInputData;
import de.extrastandard.api.model.content.IDbQueryInputData;
import de.extrastandard.api.model.content.ISingleResponseData;
import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.ICommunicationProtocol;
import de.extrastandard.api.model.execution.IProcedure;
import de.extrastandard.api.model.execution.InputDataQualifier;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.persistence.repository.CommunicationProtocolRepository;
import de.extrastandard.persistence.repository.StatusRepository;

/**
 * JPA Implementierung von {@link ICommunicationProtocol}.
 * 
 * @author Thorsten Vogel
 * @version $Id: InputData.java 562 2012-09-06 14:12:43Z thorstenvogel@gmail.com
 *          $
 */
@Configurable(preConstruction = true)
@Entity
@Table(name = "COMMUNICATION_PROTOCOL")
public class CommunicationProtocol extends AbstractEntity implements ICommunicationProtocol {

	private static final long serialVersionUID = 1L;

	// private static final Logger logger =
	// LoggerFactory.getLogger(InputData.class);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "communication_protocol_entity_seq_gen")
	@SequenceGenerator(name = "communication_protocol_entity_seq_gen", sequenceName = "seq_communication_protocol_id")
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

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "next_phase_connection_id")
	private PhaseConnection nextPhaseConnection;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "current_phase_connection_id")
	private PhaseConnection currentPhaseConnection;

	@Column(name = "response_id")
	private String responseId;

	@Column(name = "request_id")
	private String requestId;

	// 1.0.0-M2 (07.11.12)
	// TODO als Enum definieren?!
	/** Art der Anfrage (Query, ...) */
	@Column(name = "input_data_qualifier")
	private String inputDataQualifier;

	// 1.0.0-M2 (14.11.12)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "status_id")
	/** Anhand des Status erkennt man ob die letzte Serververarbeitung erfolgreich verlaufen ist */
	private Status status;

	// 1.0.0-M3 (12.12.12)
	@Column(name = "output_identifier")
	private String outputIdentifier;

	// 1.0.0-M3 (17.12.12)
	@Column(name = "subquery")
	private String subquery;

	@Transient
	@Inject
	@Named("communicationProtocolRepository")
	private transient CommunicationProtocolRepository repository;

	@Inject
	@Named("statusRepository")
	@Transient
	private transient StatusRepository statusRepository;

	/**
	 * Dieser Konstruktur wird ausschliesslich durch das ORM Tool genutzt.
	 * Andere Benutzer müssen immer den Konstruktor
	 * {@link #InputData(String, String, String)} benutzen.
	 */
	public CommunicationProtocol() {
	}

	// /**
	// * Erzeugt eine neue IInputData-Instanz und legt gleichzeitig eine neue
	// * Transition an.
	// *
	// * @param inputIdentifier
	// * Identifier
	// * @param hashCode
	// * Hashcode der Daten
	// * @param qualifier
	// * Qualifizierung
	// */
	// public InputData(final ISingleContentInputData singleContentInputData,
	// final Execution execution) {
	// Assert.notNull(singleContentInputData,
	// "SingleContentInputData must be specified");
	// final String hashCode = singleContentInputData.getHashCode();
	// final String inputIdentifier = singleContentInputData
	// .getInputIdentifier();
	// Assert.notNull(inputIdentifier, "inputIdentifier must be specified");
	// Assert.notNull(hashCode, "inputIdentifier must be specified");
	// this.inputIdentifier = inputIdentifier;
	// this.hashcode = hashCode;
	// this.execution = execution;
	// repository.save(this);
	// }
	//
	// /**
	// * @param serverResponseId
	// * @param sourceRequestId
	// * @param execution
	// */
	// public InputData(final ISingleQueryInputData singleQueryInputData,
	// final Execution execution) {
	// Assert.notNull(singleQueryInputData,
	// "ISingleQueryInputData must be specified");
	// Assert.notNull(execution, "Execution must be specified");
	//
	// this.execution = execution;
	// final Long sourceIdentificationId = singleQueryInputData
	// .getSourceIdentificationId();
	// Assert.notNull(sourceIdentificationId,
	// "SourceIdentification must be specified");
	// final InputData sourceInputData = repository
	// .findOne(sourceIdentificationId);
	// final PhaseConnection sourceInputNextPhaseConnection = sourceInputData
	// .getNextPhaseConnection();
	// this.currentPhaseConnection = sourceInputNextPhaseConnection;
	// repository.save(this);
	// sourceInputNextPhaseConnection.setTargetInputData(this);
	// final String requestId = this.calculateRequestId();
	// this.requestId = requestId;
	// this.inputIdentifier = singleQueryInputData.getInputIdentifier();
	// repository.save(this);
	// }

	/**
	 * Erzeugt eine neue IInputData-Instanz.
	 * 
	 * @param singleInputData
	 * @param execution
	 */
	public CommunicationProtocol(final ISingleInputData singleInputData,
			final Execution execution) {
		Assert.notNull(singleInputData, "ISingleInputData must be specified");
		Assert.notNull(execution, "Execution must be specified");
		this.execution = execution;
		final String inputIdentifier = singleInputData.getInputIdentifier();
		Assert.notNull(inputIdentifier, "inputIdentifier must be specified");
		this.inputIdentifier = inputIdentifier;
		if (IDbQueryInputData.class.isAssignableFrom(singleInputData
				.getClass())) {
			final IDbQueryInputData singleQueryInputData = IDbQueryInputData.class
					.cast(singleInputData);
			fillInputData(singleQueryInputData);
		} else if (ISingleContentInputData.class
				.isAssignableFrom(singleInputData.getClass())) {
			final ISingleContentInputData singleContentInputData = ISingleContentInputData.class
					.cast(singleInputData);
			fillInputData(singleContentInputData);
		}
		// TODO MAXRESP (06.11.12)
		else if (ICriteriaQueryInputData.class.isAssignableFrom(singleInputData
				.getClass())) {
			final ICriteriaQueryInputData maxResponseIdQueryInputData = ICriteriaQueryInputData.class
					.cast(singleInputData);
			fillInputData(maxResponseIdQueryInputData);
		} else {
			throw new ExtraCoreRuntimeException("Unexpected inputdata: "
					+ singleInputData.getClass());
		}
		// TODO MW Status InputData?!
		this.status = statusRepository
				.findOne(PersistentStatus.INITIAL.getId());
	}

	/**
	 * Dieser Konstruktor wird verwendet, um einer 1 zu n Abfrage (z.B. 'alle
	 * Dokumente mit ID > 7') mehr als ein Response-Ergebnis zuordnen zu können.
	 * Aus dem Ursprungs CommunicationProtocol-Objekt und dem Response-Objekt wird ein
	 * weiteres CommunicationProtocol Objekt erzeugt (ohne Status und nextPhaseConnection).
	 * 
	 * @param criteriaInputData
	 * @param singleResponseData
	 */
	public CommunicationProtocol(CommunicationProtocol criteriaInputData,
			ISingleResponseData singleResponseData) {
		// Datenübernahme aus CommunicationProtocol
		this.currentPhaseConnection = criteriaInputData.currentPhaseConnection;
		this.execution = criteriaInputData.execution;
		this.inputDataQualifier = criteriaInputData.inputDataQualifier;
		this.inputIdentifier = criteriaInputData.inputIdentifier;
		this.requestId = singleResponseData.getRequestId();

		this.subquery = criteriaInputData.subquery;
		
		// nextPhaseConnection darf nicht uebernommen werden!
		//this.nextPhaseConnection = criteriaInputData.nextPhaseConnection;

		// Datenübernahme aus ISingleResponseData
		transmitted(singleResponseData);

		// TODO hashCode Berechnung!?
		this.hashcode = String.valueOf(this.requestId.hashCode() * 31
				+ this.responseId.hashCode() * 33);
		repository.save(this);
	}

	/**
	 * 
	 * @param singleQueryInputData
	 * @param execution
	 */
	private void fillInputData(final IDbQueryInputData singleQueryInputData) {
		final Long sourceIdentificationId = singleQueryInputData
				.getSourceIdentificationId();
		Assert.notNull(sourceIdentificationId,
				"SourceIdentification must be specified");
		final CommunicationProtocol sourceInputData = repository
				.findOne(sourceIdentificationId);
		final PhaseConnection sourceInputNextPhaseConnection = sourceInputData
				.getNextPhaseConnection();
		this.currentPhaseConnection = sourceInputNextPhaseConnection;
		repository.save(this);
		sourceInputNextPhaseConnection.setTargetInputData(this);
		final String requestId = this.calculateRequestId();
		this.requestId = requestId;
		// (08.11.12) verschiedene Qualifizierungen (Query, Criteria, ...)
		this.inputDataQualifier = InputDataQualifier.QUERY_UNIQUE.getName();
		repository.save(this);
	}

	private void fillInputData(
			final ISingleContentInputData singleContentInputData) {

		final String hashCode = singleContentInputData.getHashCode();
		Assert.notNull(hashCode, "inputIdentifier must be specified");
		this.hashcode = hashCode;
		// (08.11.12) verschiedene Qualifizierungen (Query, Criteria, ...)
		this.inputDataQualifier = InputDataQualifier.CONTENT.getName();

		repository.save(this);

	}

	// TODO MAXRESP (06.11.12)
	/**
	 * Initialisiert die Daten für einen Query-Criteria-Request (z.B.
	 * 'GreaterThen 2')
	 * 
	 * @param singleQueryInputData
	 */
	private void fillInputData(
			final ICriteriaQueryInputData singleQueryInputData) {

		final String hashCode = String.valueOf(singleQueryInputData.hashCode());
		this.hashcode = hashCode;
		final String requestId = this.calculateRequestId();
		// (08.11.12) verschiedene Qualifizierungen (Query, Criteria, ...)
		this.inputDataQualifier = InputDataQualifier.QUERY_CRITERIA.getName();

		// (17.12.12) Suchanfrage erweitern (z.B. fuer gezielte Abfrage nach Laendern)
		this.subquery = singleQueryInputData.getSubquery();
		
		repository.save(this);
	}

	/**
	 * @see de.extrastandard.api.model.execution.ICommunicationProtocol#getInputIdentifier()
	 */
	@Override
	public String getInputIdentifier() {
		return inputIdentifier;
	}

	/**
	 * @see de.extrastandard.api.model.execution.ICommunicationProtocol#getHashcode()
	 */
	@Override
	public String getHashcode() {
		return hashcode;
	}

	/**
	 * @see de.extrastandard.api.model.execution.ICommunicationProtocol#getResponseId()
	 */
	@Override
	public String getResponseId() {
		return responseId;
	}

	/**
	 * @see de.extrastandard.api.model.execution.ICommunicationProtocol#getExecution()
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
		// TODO MW calculateRequestId
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
		repository.save(this);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CommunicationProtocol [inputIdentifier=");
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
	public PhaseConnection getNextPhaseConnection() {
		return nextPhaseConnection;
	}

	/**
	 * @return the currentPhaseConnectionSet
	 */
	@Override
	public PhaseConnection getCurrentPhaseConnection() {
		return currentPhaseConnection;
	}

	/**
	 * @param currentPhaseConnection
	 *            the currentPhaseConnection to set
	 */
	public void setCurrentPhaseConnection(
			final PhaseConnection currentPhaseConnection) {
		this.currentPhaseConnection = currentPhaseConnection;
	}

	/**
	 * @param nextPhasenConnection
	 *            the nextPhasenConnection to set
	 */
	public void setNextPhaseConnection(final PhaseConnection nextPhaseConnection) {
		this.nextPhaseConnection = nextPhaseConnection;
		repository.save(this);
	}

	/**
	 * @return the inputDataQualifier
	 */
	public String getInputDataQualifier() {
		return inputDataQualifier;
	}

	/**
	 * @param inputDataQualifier
	 *            the inputDataQualifier to set
	 */
	public void setInputDataQualifier(String inputDataQualifier) {
		this.inputDataQualifier = inputDataQualifier;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/** Zeigt an, ob die Kommunikation erfolgreich abgeschlossen ist */
	public boolean isSuccessful() {
		return (status != null && status.getId().equals(
				PersistentStatus.DONE.getId()));
	}

	/** Zeigt an, ob die Kommunikation erfolgreich ist oder auf externe Bestaetigung wartet */
	public boolean isSuccessfulOrWait() {
		return isSuccessful() || (status != null && PersistentStatus.WAIT.getId().equals(status.getId()));
	}

	public String getOutputIdentifier() {
		return outputIdentifier;
	}

	public void setOutputIdentifier(String outputIdentifier) {
		this.outputIdentifier = outputIdentifier;
	}

	@Override
	public void transmitted(final ISingleResponseData singleResponseData) {
		this.responseId = singleResponseData.getResponseId();
		this.returnCode = singleResponseData.getReturnCode();
		this.returnText = singleResponseData.getReturnText();
		this.outputIdentifier = singleResponseData.getOutputIdentifier();

		// (12.12.12) PersistentStatus wird bei der Response-Verarbeitung berechnet
		this.status = statusRepository.findOne(singleResponseData.getPersistentStatus().getId());

		repository.save(this);
	}
	
	/**
	 * Für externe Status-Änderungen (z.B. von 'WAIT' nach 'DONE')
	 * @param aPersistentStatus
	 */
	public void changeStatus(PersistentStatus aPersistentStatus) {
		this.status = statusRepository.findOne(aPersistentStatus.getId());
		repository.save(this);		
	}

	@Override
	public void transmitWithoutResponse() {
		// TODO MW mehr Informationen falls kein Ergebnis vorliegt
		this.returnText = "Kein Ergebnis!";
		this.status = statusRepository.findOne(PersistentStatus.DONE.getId());

		repository.save(this);		
	}

}
