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

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import de.extrastandard.api.exception.ExtraRuntimeException;
import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.model.content.ISingleInputData;
import de.extrastandard.api.model.content.ISingleResponseData;
import de.extrastandard.api.model.execution.ICommunicationProtocol;
import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.IProcedure;
import de.extrastandard.api.model.execution.IProcessTransition;
import de.extrastandard.api.model.execution.IStatus;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.persistence.repository.ExecutionRepository;
import de.extrastandard.persistence.repository.PhaseConnectionRepository;
import de.extrastandard.persistence.repository.ProcedureRepository;
import de.extrastandard.persistence.repository.StatusRepository;

/**
 * JPA Implementierung von {@link IExecution}.
 * 
 * @author Thorsten Vogel
 * @version $Id: Execution.java 508 2012-09-04 09:35:41Z thorstenvogel@gmail.com
 *          $
 */
@Configurable(preConstruction = true)
@Entity
@Table(name = "EXECUTION")
@org.hibernate.annotations.Table(appliesTo = "EXECUTION", indexes = { @Index(name = "execution_idx_proc", columnNames = { "procedure_id" }) })
public class Execution extends AbstractEntity implements IExecution {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(CommunicationProtocol.class);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "execution_entity_seq_gen")
	@SequenceGenerator(name = "execution_entity_seq_gen", sequenceName = "seq_execution_id")
	private Long id;

	@Column(name = "start_time")
	private Date startTime;

	@Column(name = "end_time")
	private Date endTime;

	@Column(name = "parameters")
	private String parameters;

	@Column(name = "phase")
	private String phase;

	@Column(name = "error_code")
	private String errorCode;

	@Column(name = "error_message", length = 10000)
	private String errorMessage;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "procedure_id")
	private Procedure procedure;

	@ManyToOne
	@JoinColumn(name = "last_transition_id")
	private ProcessTransition lastTransition;

	@OneToMany(mappedBy = "execution", fetch = FetchType.LAZY)
	private final Set<CommunicationProtocol> communicationProtocols = new HashSet<CommunicationProtocol>();

	@Inject
	@Named("executionRepository")
	@Transient
	private transient ExecutionRepository repository;

	@Inject
	@Named("statusRepository")
	@Transient
	private transient StatusRepository statusRepository;

	@Inject
	@Named("procedureRepository")
	@Transient
	private transient ProcedureRepository procedureRepository;

	@Inject
	@Named("phaseConnectionRepository")
	@Transient
	private transient PhaseConnectionRepository phaseConnectionRepository;

	/**
	 * Empty constructor.
	 */
	public Execution() {
	}

	/**
	 * Erzeugt eine neue Execution, die sogleich persistiert wird.
	 * 
	 * @param parameters
	 *            Parameter, mit denen die Execution gestartet wurde.
	 */
	public Execution(final IProcedure procedure, final String parameters,
			final PhaseQualifier phaseQualifier) {
		Assert.notNull(procedure, "Procedure is null");
		Assert.notNull(phaseQualifier, "PhaseQualifier is null");
		this.parameters = parameters;
		this.phase = phaseQualifier.getName();
		this.startTime = new Date();
		this.procedure = procedureRepository.findByName(procedure.getName());
		repository.save(this);
		final ProcessTransition transition = new ProcessTransition(this);
		this.lastTransition = transition;
		repository.save(this);
	}

	/**
	 * @see de.extrastandard.api.model.execution.ICommunicationProtocol#updateProgress(de.extrastandard.api.model.execution.IStatus,
	 *      java.lang.String)
	 */
	@Override
	@Transactional
	public void updateProgress(final PersistentStatus newPersistentStatusEnum) {
		Assert.notNull(newPersistentStatusEnum, "newStatus must be specified");
		final Status newPersistentStatus = statusRepository
				.findOne(newPersistentStatusEnum.getId());

		final IProcessTransition lastTransition = this.getLastTransition();
		final IStatus currentStatus = lastTransition.getCurrentStatus();

		final Date lastTransitionDate = this.lastTransition.getTransitionDate();
		final ProcessTransition transition = new ProcessTransition(this,
				currentStatus, newPersistentStatus, lastTransitionDate);
		this.lastTransition = transition;
		repository.save(this);
	}

	/**
	 * @see de.extrastandard.api.model.execution.ICommunicationProtocol#failed(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void failed(final String errorCode, final String errorMessage) {
		try {
			this.errorCode = errorCode;
			String truncatedErrorMessage = errorMessage;
			if (org.apache.commons.lang.StringUtils.isNotEmpty(errorMessage)
					&& errorMessage.length() > 255) {
				truncatedErrorMessage = errorMessage.substring(0, 254);
			}
			this.errorMessage = truncatedErrorMessage;
			this.errorMessage = errorMessage;
			updateProgress(PersistentStatus.FAIL);
			// InputData bzw. PhasenConnection updaten
			for (final CommunicationProtocol inputData : this.communicationProtocols) {
				final PhaseConnection currentPhaseConnection = inputData
						.getCurrentPhaseConnection();
				if (currentPhaseConnection != null) {
					currentPhaseConnection.setFailed();
				}
			}
		} catch (final Exception exception) {
			logger.error("Exception beim inputData.failed", exception);
		}
	}

	/**
	 * @see de.extrastandard.api.model.execution.ICommunicationProtocol#failed(de.extrastandard.api.exception.ExtraRuntimeException)
	 */
	@Override
	public void failed(final ExtraRuntimeException exception) {
		try {
			this.errorCode = exception.getCode().name();
			this.errorMessage = exception.getMessage();
			failed(errorCode, errorMessage);

		} catch (final Exception exception2) {
			logger.error("Exception beim inputData.failed", exception);
		}
	}

	/**
	 * @see de.extrastandard.api.model.execution.IExecution#endExecution(de.extrastandard.api.model.execution.IStatus)
	 */
	@Override
	@Transactional
	public void endExecution(final IResponseData responseData) {
		Assert.notNull(responseData, "ResponseData is null");
		this.endTime = new Date();
		updateProgress(PersistentStatus.DONE);
		repository.save(this);
		for (final CommunicationProtocol inputData : communicationProtocols) {
			processResponseData(inputData, responseData);
		}
	}

	/**
	 * Verarbeitet das Server-Ergebnis (ResponseData) für eine Criteria-Query
	 * (z.B. 'alle Dok. mit ID > 7'). Jedem Ergebnissatz (ISingleResponseData)
	 * wird ein CommunicationProtocol-Objekt zugeordnet.
	 * 
	 * @since 1.0.0-M2
	 * @param comProt
	 * @param responseData
	 */
	private void processResponseData(final CommunicationProtocol comProt,
			final IResponseData responseData) {
		final String requestId = comProt.getRequestId();
		int nummerResponse = 1;
		// für jede Response muss ein InputData Objekt angelegt werden
		// (21.11.12) kein Ergebnis muss auch moeglich sein!
		final Collection<ISingleResponseData> responseDataCollection = responseData
				.getResponse(requestId);
		// Liegen fuer diesen Request Ergebnisse vor?
		if (responseDataCollection != null) {
			for (final ISingleResponseData singleResponseData : responseDataCollection) {
				if (nummerResponse == 1) {
					// vorhandenes CommunicationProtocol Objekt nehmen und
					// Kommunikationsdaten
					// aktualisieren
					comProt.transmitted(singleResponseData);
					processPhaseConnectionForInputData(comProt);
				} else {
					// neues InputData Objekt erzeugen
					final CommunicationProtocol comProtForResponse = new CommunicationProtocol(
							comProt, singleResponseData);
					processPhaseConnectionForInputData(comProtForResponse);
				}
				nummerResponse++;
			}
		} else {
			// Keine Ergebnisse!
			comProt.transmitWithoutResponse();
		}
	}

	/**
	 * Für das übergebene CommunicationProtocol Objekt wird die zugeordnete
	 * PhaseConection geschlossen und eine Nachfolge-PhaseConnection vorbereitet
	 * (falls die aktuelle Phase keine Endphase ist)
	 * 
	 * @param communicationProtocol
	 */
	private void processPhaseConnectionForInputData(
			final CommunicationProtocol communicationProtocol) {
		// (14.11.12) Nur bei erfolgreicher Verarbeitung darf die nächste Phase
		// vorbereitet werden!
		// (18.12.12) Auch im Zustand 'WAIT' wird die naechste Phase
		// vorbereitet!

		if (communicationProtocol.isSuccessfulOrWait()
				&& !this.procedure.isProcedureEndPhase(this.phase)) {
			final String nextPhasenQualifier = this.procedure
					.getNextPhase(this.phase);
			new PhaseConnection(communicationProtocol, nextPhasenQualifier);
		}

		// Abgearbeitete PhaseConnection schliessen
		final List<PhaseConnection> quellePhaseConnections = phaseConnectionRepository
				.findByTargetCommunicationProtocol(communicationProtocol);
		for (final PhaseConnection quellePhaseConnection : quellePhaseConnections) {
			// Erfolgreiche Verarbeitung?
			if (communicationProtocol.isSuccessful()) {
				quellePhaseConnection.success();
			} else {
				quellePhaseConnection.setFailed();
			}
		}
	}

	// /**
	// * @see
	// de.extrastandard.api.model.execution.IExecution#startInputData(de.extrastandard.persistence.api.IInputType,
	// * java.lang.String, java.lang.String)
	// */
	// @Override
	// @Transactional
	// public IInputData startContentInputData(
	// final ISingleContentInputData singleContentInputData) {
	// final InputData inputData = new InputData(singleContentInputData, this);
	// this.inputDataSet.add(inputData);
	// repository.save(this);
	// return inputData;
	// }
	//
	// @Override
	// public IInputData startDbQueryInputData(
	// final ISingleQueryInputData singleQueryInputData) {
	// final InputData inputData = new InputData(singleQueryInputData, this);
	// this.inputDataSet.add(inputData);
	// repository.save(this);
	// return inputData;
	// }

	/**
	 * @param singleInputData
	 * @return
	 */
	@Override
	public ICommunicationProtocol startInputData(
			final ISingleInputData singleInputData) {
		final CommunicationProtocol inputData = new CommunicationProtocol(
				singleInputData, this);
		this.communicationProtocols.add(inputData);
		repository.save(this);
		return inputData;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IExecution#getParameters()
	 */
	@Override
	public String getParameters() {
		return parameters;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IExecution#getStartTime()
	 */
	@Override
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IExecution#getProcedure()
	 */
	@Override
	public IProcedure getProcedure() {
		return procedure;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IExecution#getEndTime()
	 */
	@Override
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IExecution#getCommunicationProtocols()
	 */
	// @Override
	// public Set<InputData> getInputDataSet() {
	// return inputDataSet;
	// }

	public void setStartTime(final Date startTime) {
		this.startTime = startTime;
	}

	public void setParameters(final String parameters) {
		this.parameters = parameters;
	}

	public void setProcedure(final Procedure procedure) {
		this.procedure = procedure;
	}

	public void setEndTime(final Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the inputDataSet
	 */
	@Override
	public Set<ICommunicationProtocol> getCommunicationProtocols() {
		return new HashSet<ICommunicationProtocol>(this.communicationProtocols);
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Execution [parameters=");
		builder.append(parameters);
		builder.append(", procedure=");
		builder.append(procedure);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", lastTransition=");
		builder.append(lastTransition);
		builder.append(", errorCode=");
		builder.append(errorCode);
		builder.append(", errorMessage=");
		builder.append(errorMessage);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * @return the phase
	 */
	@Override
	public String getPhase() {
		return phase;
	}

	/**
	 * @param phase
	 *            the phase to set
	 */
	public void setPhase(final String phase) {
		this.phase = phase;
	}

	/**
	 * @return the lastTransition
	 */
	@Override
	public ProcessTransition getLastTransition() {
		return lastTransition;
	}

	/**
	 * @return the errorCode
	 */
	@Override
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @see de.extrastandard.api.model.execution.ICommunicationProtocol#hasError()
	 */
	@Override
	public boolean hasError() {
		return StringUtils.hasText(errorCode)
				|| StringUtils.hasText(errorMessage);
	}

}
