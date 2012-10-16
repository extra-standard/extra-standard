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
import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.IProcedure;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.persistence.repository.InputDataRepository;
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
	@Named("inputDataRepository")
	private transient InputDataRepository inputDataRepository;

	@Inject
	@Named("statusRepository")
	private transient StatusRepository statusRepository;

	private static final Integer MAX_RESULT_SIZE = 5;

	/**
	 * @see de.extrastandard.api.model.execution.IExecutionPersistence#startExecution(java.lang.String)
	 */
	@Override
	@Transactional
	public IExecution startExecution(final String procedureName, final String parameters,
			final PhaseQualifier phaseQualifier) {
		Assert.notNull(procedureName, "ProcedureName is null");
		final IProcedure procedure = procedureRepository.findByName(procedureName);
		Assert.notNull(procedure, "Procedure not found. Name : " + procedureName);
		return new Execution(procedure, parameters, phaseQualifier);
	}

	@Override
	public IInputData findInputDataByRequestId(final String requestId) {
		return inputDataRepository.findByRequestId(requestId);
	}

	/**
	 * Seeks InputData for further Procesierung depending on the ExecutePhase
	 * 
	 * @param phaseQualifier
	 * @return
	 */
	@Override
	@Transactional
	public List<IInputData> findInputDataForExecution(final String procedureName, final PhaseQualifier phaseQualifier,
			final Integer inputDataLimit) {
		Assert.notNull(procedureName, "ProcedureName is null");
		Assert.notNull(phaseQualifier, "Phase is null");
		final IProcedure procedure = procedureRepository.findByName(procedureName);
		return findInputDataForExecution(procedure, phaseQualifier, inputDataLimit);
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
	public List<IInputData> findInputDataForExecution(final IProcedure procedure, final PhaseQualifier phaseQualifier,
			final Integer inputDataLimit) {
		Assert.notNull(procedure, "Procedure is null");
		Assert.notNull(phaseQualifier, "Phase is null");
		final Status statusInitial = statusRepository.findOne(PersistentStatus.INITIAL.getId());

		final Pageable pageRequest = new PageRequest(0, inputDataLimit);
		final List<IInputData> inputDateList = inputDataRepository.findByProcedureAndPhaseQualifierAndStatus(procedure,
				phaseQualifier.getName(), statusInitial, pageRequest);
		return inputDateList;
	}

	@Override
	public List<IInputData> findInputDataForExecution(final String executionProcedure,
			final PhaseQualifier phaseQualifier) {
		return findInputDataForExecution(executionProcedure, phaseQualifier, MAX_RESULT_SIZE);
	}

	@Override
	public Long countInputDataForExecution(final String executionProcedure, final PhaseQualifier phaseQualifier) {
		final Status statusInitial = statusRepository.findOne(PersistentStatus.INITIAL.getId());
		final IProcedure procedure = procedureRepository.findByName(executionProcedure);
		return inputDataRepository.count(procedure, phaseQualifier.getName(), statusInitial);
	}

}
