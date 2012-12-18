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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.IExecutionPersistence;
import de.extrastandard.api.model.execution.ICommunicationProtocol;
import de.extrastandard.api.model.execution.IProcedure;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.persistence.repository.CommunicationProtocolRepository;
import de.extrastandard.persistence.repository.ProcedureRepository;
import de.extrastandard.persistence.repository.StatusRepository;

/**
 * Einstiegsklasse zum Management von Executions.
 * 
 * @author Thorsten Vogel
 * @version $Id: ExecutionPersistenceJpa.java 508 2012-09-04 09:35:41Z
 *          thorstenvogel@gmail.com $
 */
@Named("executionPersistenceJpa")
public class ExecutionPersistenceJpa implements IExecutionPersistence {

	@Inject
	@Named("procedureRepository")
	private transient ProcedureRepository procedureRepository;

	@Inject
	@Named("communicationProtocolRepository")
	private transient CommunicationProtocolRepository communicationProtocolRepository;

	@Inject
	@Named("statusRepository")
	private transient StatusRepository statusRepository;

	private static final Integer MAX_RESULT_SIZE = 5;

	/**
	 * @see de.extrastandard.api.model.execution.IExecutionPersistence#startExecution(java.lang.String)
	 */
	@Override
	@Transactional
	public IExecution startExecution(final String procedureName,
			final String parameters, final PhaseQualifier phaseQualifier) {
		Assert.notNull(procedureName, "ProcedureName is null");
		final IProcedure procedure = procedureRepository
				.findByName(procedureName);
		Assert.notNull(procedure, "Procedure not found. Name : "
				+ procedureName);
		return new Execution(procedure, parameters, phaseQualifier);
	}

	@Override
	public ICommunicationProtocol findInputDataByRequestId(final String requestId) {
		return communicationProtocolRepository.findByRequestId(requestId);
	}

	/**
	 * Seeks InputData for further Procesierung depending on the ExecutePhase
	 * 
	 * @param phaseQualifier
	 * @return
	 */
	@Override
	@Transactional
	public List<ICommunicationProtocol> findInputDataForExecution(
			final String procedureName, final PhaseQualifier phaseQualifier,
			final Integer inputDataLimit) {
		Assert.notNull(procedureName, "ProcedureName is null");
		Assert.notNull(phaseQualifier, "Phase is null");
		final IProcedure procedure = procedureRepository
				.findByName(procedureName);
		return findInputDataForExecution(procedure, phaseQualifier,
				inputDataLimit);
	}

	/**
	 * Seeks InputData for further Procesierung depending on the ExecutePhase
	 * 
	 * @param procedure
	 *            the Procedure
	 * @param phaseQualifier
	 *            the PhaseQuelifier
	 * @param inputDataLimit
	 *            limits the result set
	 * @return
	 */
	public List<ICommunicationProtocol> findInputDataForExecution(
			final IProcedure procedure, final PhaseQualifier phaseQualifier,
			final Integer inputDataLimit) {
		Assert.notNull(procedure, "Procedure is null");
		Assert.notNull(phaseQualifier, "Phase is null");
		final Status statusInitial = statusRepository
				.findOne(PersistentStatus.INITIAL.getId());
		final Status statusDone = statusRepository
				.findOne(PersistentStatus.DONE.getId());

		final Pageable pageRequest = new PageRequest(0, inputDataLimit);

		// (18.12.12) es duerfen nur Erfolgreich verarbeitete Datensaetze weiter verarbeitet werden
//		final List<ICommunicationProtocol> inputDateList = communicationProtocolRepository
//				.findByProcedureAndPhaseQualifierAndStatus(procedure,
//						phaseQualifier.getName(), statusInitial, pageRequest);

		final List<ICommunicationProtocol> inputDateList = communicationProtocolRepository
				.findByProcedureAndPhaseQualifierAndStatusAndComProtStatus(procedure,
						phaseQualifier.getName(), statusInitial, statusDone, pageRequest);
		
		return inputDateList;
	}

	@Override
	public List<ICommunicationProtocol> findInputDataForExecution(
			final String executionProcedure, final PhaseQualifier phaseQualifier) {
		return findInputDataForExecution(executionProcedure, phaseQualifier,
				MAX_RESULT_SIZE);
	}

	@Override
	public Long countInputDataForExecution(final String executionProcedure,
			final PhaseQualifier phaseQualifier) {
		final Status statusInitial = statusRepository
				.findOne(PersistentStatus.INITIAL.getId());
		final IProcedure procedure = procedureRepository
				.findByName(executionProcedure);
		return communicationProtocolRepository.count(procedure, phaseQualifier.getName(),
				statusInitial);
	}

	@Override
	public String maxResponseIdForExecution(String procedureName,
			PhaseQualifier phaseQualifier, String subquery) {
		Assert.notNull(procedureName, "Procedure is null");
		Assert.notNull(phaseQualifier, "Phase is null");
		final IProcedure procedure = procedureRepository
				.findByName(procedureName);

		final Integer maxResponseId ;
		if (subquery != null && subquery.length() > 0) {
			maxResponseId = communicationProtocolRepository
					.maxResponseIdForProcedureAndPhaseAndSubquery(procedure,
							phaseQualifier.getName(), subquery);			
		}
		else {
			maxResponseId = communicationProtocolRepository
					.maxResponseIdForProcedureAndPhase(procedure,
							phaseQualifier.getName());			
		}

		if (maxResponseId == null) {
			return "0";
		}
		return String.valueOf(maxResponseId);
	}

	@Override
	public boolean changeCommunicationProtocolStatusByOutputIdentifier(final String outputIdentifier, PersistentStatus persistentStatus) {
		ICommunicationProtocol communicationProtocol = communicationProtocolRepository.findByOutputIdentifier(outputIdentifier);
		if (communicationProtocol == null) {
			// Fehler: zu dem OutputIdentifier gibt es kein CommunicationProtocol!
			return false;
		}
		communicationProtocol.changeStatus(persistentStatus);
		// Alles OK
		return true;
	}

}
