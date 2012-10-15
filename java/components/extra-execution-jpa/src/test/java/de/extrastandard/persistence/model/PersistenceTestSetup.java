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

import de.extra.client.core.responce.impl.ResponseData;
import de.extra.client.core.responce.impl.SingleResponseData;
import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.model.content.ISingleResponseData;
import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.persistence.repository.MandatorRepository;
import de.extrastandard.persistence.repository.StatusRepository;

/**
 * @author Leonid Potap
 * @version $Id$
 */
@Named("persistenceTestSetup")
public class PersistenceTestSetup {

	private static final String MANDATOR_TEST = "TEST";

	public static final String PROCEDURE_DATA_MATCH_NAME = "Datenabgleich";

	@Inject
	@Named("statusRepository")
	private transient StatusRepository statusRepository;

	@Inject
	@Named("mandatorRepository")
	private transient MandatorRepository mandatorRepository;

	@Inject
	@Named("executionPersistenceJPA")
	private ExecutionPersistence executionPersistence;

	public void setupInitialDaten() {

		new Status(PersistentStatus.INITIAL);

		new Status(PersistentStatus.ENVELOPED);

		new Status(PersistentStatus.TRANSMITTED);

		new Status(PersistentStatus.RESULTS_EXPECTED);

		new Status(PersistentStatus.RESULTS_PROCESSED);

		new Status(PersistentStatus.RECEIPT_CONFIRMED);

		new Status(PersistentStatus.FAIL);

		new Status(PersistentStatus.DONE);

		new Mandator(MANDATOR_TEST);

	}

	public void setupProcedureSendFeths() {

		final ProcedureType procedureSendFetch = new ProcedureType("SCENARIO_SEND_FETCH", PhaseQualifier.PHASE1.name(),
				PhaseQualifier.PHASE3.name());

		final ProcedurePhaseConfiguration procedurePhaseConfigurationPhase3 = new ProcedurePhaseConfiguration(
				procedureSendFetch, PhaseQualifier.PHASE3);

		final ProcedurePhaseConfiguration procedurePhaseConfigurationPhase2 = new ProcedurePhaseConfiguration(
				procedureSendFetch, PhaseQualifier.PHASE2, procedurePhaseConfigurationPhase3);

		new ProcedurePhaseConfiguration(procedureSendFetch, PhaseQualifier.PHASE1, procedurePhaseConfigurationPhase2);

		final Mandator mandatorTEST = mandatorRepository.findByName(MANDATOR_TEST);

		new Procedure(mandatorTEST, procedureSendFetch, "Datenabgleich", "SEND_FETH");

	}

	public IExecution setUpTestDatenForProcedureSendFetchPhase2() {
		final IExecution executionForTestPhase2 = executionPersistence.startExecution(
				PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME, "-c d:/extras/configdir", PhaseQualifier.PHASE1);

		final IInputData inputData = executionForTestPhase2.startContentInputData("inputIdentifier", "hashCode");
		final String calculatedRequestId = inputData.calculateRequestId();
		inputData.setRequestId(calculatedRequestId);

		final IResponseData responseData = new ResponseData();
		final ISingleResponseData singleResponseData = new SingleResponseData(calculatedRequestId, "ReturnCode",
				"ReturnText", "RESPONSE_ID");
		responseData.addSingleResponse(singleResponseData);
		executionForTestPhase2.endExecution(responseData);
		return executionForTestPhase2;
	}

	public IExecution setUpTestDatenForProcedureSendFetchPhase3() {
		final IExecution executionForTestPhase3 = executionPersistence.startExecution(
				PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME, "-c d:/extras/configdir2", PhaseQualifier.PHASE2);

		final IInputData inputDataForPhase3 = executionForTestPhase3.startContentInputData("inputIdentifierPhase2",
				"hashCodePhase2");
		final String calculatedRequestIdForPhase3 = inputDataForPhase3.calculateRequestId();
		inputDataForPhase3.setRequestId(calculatedRequestIdForPhase3);

		final IResponseData responseDataForPhase3 = new ResponseData();
		final ISingleResponseData singleResponseDataForPhase3 = new SingleResponseData(calculatedRequestIdForPhase3,
				"ReturnCodePhase2", "ReturnTextPhase2", "RESPONSE_ID_PHASE2");
		responseDataForPhase3.addSingleResponse(singleResponseDataForPhase3);
		executionForTestPhase3.endExecution(responseDataForPhase3);

		return executionForTestPhase3;
	}
}
