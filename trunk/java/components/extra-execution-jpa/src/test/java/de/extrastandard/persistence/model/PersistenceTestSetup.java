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

		final Status statusReceiptConfirmed = statusRepository.findByName(PersistentStatus.RECEIPT_CONFIRMED.name());

		final ProcedureType procedureSendFetch = new ProcedureType("SCENARIO_SEND_FETCH", statusReceiptConfirmed,
				PhaseQualifier.PHASE1.name());

		new ProcedurePhaseConfiguration(procedureSendFetch, PhaseQualifier.PHASE1, PersistentStatus.RESULTS_EXPECTED);

		new ProcedurePhaseConfiguration(procedureSendFetch, PhaseQualifier.PHASE2, PersistentStatus.RESULTS_PROCESSED,
				PersistentStatus.RESULTS_EXPECTED);

		new ProcedurePhaseConfiguration(procedureSendFetch, PhaseQualifier.PHASE3, PersistentStatus.RECEIPT_CONFIRMED,
				PersistentStatus.RESULTS_PROCESSED);

		final Mandator mandatorTEST = mandatorRepository.findByName(MANDATOR_TEST);

		new Procedure(mandatorTEST, procedureSendFetch, "Datenabgleich", "SEND_FETH");

	}

	public IInputData setUpTestDatenForProcedureSendFetchPhase2() {
		final IExecution execution = executionPersistence.startExecution(
				PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME, "-c /threephaseszenario/phase1");

		final IInputData inputData = execution.startInputData("TestDatenScenarioSendFetchPhase1", "hashCodePhase1");
		final String requestId = inputData.calculateRequestId();
		inputData.setRequestId(requestId);
		inputData.saveOrUpdate();
		inputData.success("ResponseIdScenarioSendFethcPhase1", PhaseQualifier.PHASE1);
		return inputData;
	}

	public IInputData setUpTestDatenForProcedureSendFetchPhase3() {
		final IExecution execution = executionPersistence.startExecution(
				PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME, "-c /threephaseszenario/phase2");

		final IInputData inputData = execution.startInputData("TestDatenScenarioSendFetchPhase2", "hashCodePhase1");
		final String requestId = inputData.calculateRequestId();
		inputData.setRequestId(requestId);
		inputData.saveOrUpdate();
		inputData.success("ResponseIdScenarioSendFethcPhase1", PhaseQualifier.PHASE1);

		inputData.success("ResponseIdScenarioSendFethcPhase2", PhaseQualifier.PHASE2);
		return inputData;
	}
}
