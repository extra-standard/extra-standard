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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import de.extra.client.core.model.inputdata.impl.SingleStringInputData;
import de.extra.client.core.responce.impl.ResponseData;
import de.extra.client.core.responce.impl.SingleResponseData;
import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.model.content.ISingleResponseData;
import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.persistence.repository.MandatorRepository;

/**
 * @author Leonid Potap
 * @version $Id$
 */
@Named("persistenceTestSetup")
public class PersistenceTestSetup {

	private static final Logger logger = LoggerFactory.getLogger(PersistenceTestSetup.class);

	private static final String MANDATOR_TEST = "TEST";

	public static final String PROCEDURE_DATA_MATCH_NAME = "Datenabgleich";

	@Inject
	@Named("mandatorRepository")
	private transient MandatorRepository mandatorRepository;

	@Inject
	@Named("executionPersistenceJpa")
	private ExecutionPersistenceJpa executionPersistence;

	@Transactional
	public void setupInitialDaten() {

		new Status(PersistentStatus.INITIAL);

		new Status(PersistentStatus.ENVELOPED);

		new Status(PersistentStatus.TRANSMITTED);

		new Status(PersistentStatus.FAIL);

		new Status(PersistentStatus.DONE);

		new Mandator(MANDATOR_TEST);

		logger.info("SetupInitialDaten finished");

	}

	@Transactional
	public void setupprocedureSendFetch() {

		final ProcedureType procedureSendFetch = new ProcedureType("SCENARIO_SEND_FETCH");

		final ProcedurePhaseConfiguration procedurePhaseConfigurationPhase3 = new ProcedurePhaseConfiguration(
				procedureSendFetch, PhaseQualifier.PHASE3);

		final ProcedurePhaseConfiguration procedurePhaseConfigurationPhase2 = new ProcedurePhaseConfiguration(
				procedureSendFetch, PhaseQualifier.PHASE2, procedurePhaseConfigurationPhase3);

		new ProcedurePhaseConfiguration(procedureSendFetch, PhaseQualifier.PHASE1, procedurePhaseConfigurationPhase2);

		final Mandator mandatorTEST = mandatorRepository.findByName(MANDATOR_TEST);

		new Procedure(mandatorTEST, procedureSendFetch, "Datenabgleich", "SEND_FETH");

		logger.info("setupProcedureSendFeth finished");
	}

	@Transactional
	public IExecution setUpTestDatenForProcedureSendFetchPhase2() {
		final IExecution executionForTestPhase2 = executionPersistence.startExecution(
				PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME, "-c d:/extras/configdir", PhaseQualifier.PHASE1);

		final IInputData inputData = executionForTestPhase2.startContentInputData(new SingleStringInputData(
				"TestStringInoutData"));
		final String calculatedRequestId = inputData.calculateRequestId();
		inputData.setRequestId(calculatedRequestId);

		final IResponseData responseData = new ResponseData();
		final ISingleResponseData singleResponseData = new SingleResponseData(calculatedRequestId, "ReturnCode",
				"ReturnText", "RESPONSE_ID_Phase_1" + calculatedRequestId);
		responseData.addSingleResponse(singleResponseData);
		executionForTestPhase2.endExecution(responseData);
		logger.info("SetupTestDatenForProcedureSendFetchPhase2  finished");
		return executionForTestPhase2;
	}

	@Transactional
	public IExecution setUpTestDatenForProcedureSendFetchPhase3() {
		final IExecution executionForTestPhase3 = executionPersistence.startExecution(
				PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME, "-c d:/extras/configdir2", PhaseQualifier.PHASE2);

		final IInputData inputDataForPhase3 = executionForTestPhase3.startContentInputData(new SingleStringInputData(
				"TestStringInoutData"));
		final String calculatedRequestIdForPhase3 = inputDataForPhase3.calculateRequestId();
		inputDataForPhase3.setRequestId(calculatedRequestIdForPhase3);

		final IResponseData responseDataForPhase3 = new ResponseData();
		final ISingleResponseData singleResponseDataForPhase3 = new SingleResponseData(calculatedRequestIdForPhase3,
				"ReturnCodePhase2", "ReturnTextPhase2", "RESPONSE_ID_Phase_1" + calculatedRequestIdForPhase3);
		responseDataForPhase3.addSingleResponse(singleResponseDataForPhase3);
		executionForTestPhase3.endExecution(responseDataForPhase3);
		logger.info("SetupTestDatenForProcedureSendFetchPhase3  finished");

		return executionForTestPhase3;
	}
}
