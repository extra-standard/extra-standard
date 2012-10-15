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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.extrastandard.api.model.execution.IExecutionPersistence;
import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.IPhaseConnection;
import de.extrastandard.api.model.execution.IStatus;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.persistence.repository.StatusRepository;

/**
 * @author Leonid Potap
 * @version $Id$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-persistence-jpa.xml", "/spring-ittest-hsqldb-propertyplaceholder.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class PersistenceSendFetchPhase2IT {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceSendFetchPhase2IT.class);

	@Inject
	@Named("executionPersistenceJPA")
	private IExecutionPersistence executionPersistence;

	@Inject
	@Named("persistenceTestSetup")
	private PersistenceTestSetup persistenceTestSetup;

	@Inject
	@Named("statusRepository")
	private transient StatusRepository statusRepository;

	private Status statusResultsExpected;

	private Status statusResultsProcessed;

	private Status statusReceiptConfirmed;

	private Status statusDone;

	@Before
	public void before() throws Exception {

		statusResultsExpected = statusRepository.findByName(PersistentStatus.RESULTS_EXPECTED.name());
		statusResultsProcessed = statusRepository.findByName(PersistentStatus.RESULTS_PROCESSED.name());
		statusReceiptConfirmed = statusRepository.findByName(PersistentStatus.RECEIPT_CONFIRMED.name());
		statusDone = statusRepository.findByName(PersistentStatus.DONE.name());

		persistenceTestSetup.setUpTestDatenForProcedureSendFetchPhase2();

		persistenceTestSetup.setUpTestDatenForProcedureSendFetchPhase3();

		// final IExecution executionForTestPhase2 =
		// executionPersistence.startExecution(
		// PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME,
		// "-c d:/extras/configdir", PhaseQualifier.PHASE1);
		//
		// final IInputData inputData =
		// executionForTestPhase2.startContentInputData("inputIdentifier",
		// "hashCode");
		// final String calculatedRequestId = inputData.calculateRequestId();
		// inputData.setRequestId(calculatedRequestId);
		//
		// final IResponseData responseData = new ResponseData();
		// final ISingleResponseData singleResponseData = new
		// SingleResponseData(calculatedRequestId, "ReturnCode",
		// "ReturnText", "RESPONSE_ID");
		// responseData.addSingleResponse(singleResponseData);
		// executionForTestPhase2.endExecution(responseData);
		//
		// final IExecution executionForTestPhase3 =
		// executionPersistence.startExecution(
		// PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME,
		// "-c d:/extras/configdir2", PhaseQualifier.PHASE2);
		//
		// final IInputData inputDataForPhase3 =
		// executionForTestPhase3.startContentInputData("inputIdentifierPhase2",
		// "hashCodePhase2");
		// final String calculatedRequestIdForPhase3 =
		// inputDataForPhase3.calculateRequestId();
		// inputDataForPhase3.setRequestId(calculatedRequestIdForPhase3);
		//
		// final IResponseData responseDataForPhase3 = new ResponseData();
		// final ISingleResponseData singleResponseDataForPhase3 = new
		// SingleResponseData(calculatedRequestIdForPhase3,
		// "ReturnCodePhase2", "ReturnTextPhase2", "RESPONSE_ID_PHASE2");
		// responseDataForPhase3.addSingleResponse(singleResponseDataForPhase3);
		// executionForTestPhase3.endExecution(responseDataForPhase3);

	}

	@Test
	public void testGetInputDataForExecutionByScenarionDataMatchAndPhase2() throws Exception {
		// success Phase 1
		final List<IInputData> inputDataForExecutionPhase2 = executionPersistence.findInputDataForExecution(
				PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME, PhaseQualifier.PHASE2);
		assertFalse("Keine Daten für die Phase2 gefunden", inputDataForExecutionPhase2.isEmpty());
		for (final IInputData inputData : inputDataForExecutionPhase2) {
			final IPhaseConnection nextPhasenConnection = inputData.getNextPhaseConnection();
			final IStatus status = nextPhasenConnection.getStatus();
			assertEquals(PersistentStatus.INITIAL.getId(), status.getId());
			final String nextPhasequalifier = nextPhasenConnection.getNextPhasequalifier();
			assertEquals(PhaseQualifier.PHASE2.getName(), nextPhasequalifier);

		}
	}

	@Test
	public void testGetInputDataForExecutionByScenarionDataMatchAndPhase3() throws Exception {
		// success Phase 2
		final List<IInputData> inputDataForExecutionPhase3 = executionPersistence.findInputDataForExecution(
				PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME, PhaseQualifier.PHASE3);
		assertFalse("Keine Daten für die Phase2 gefunden", inputDataForExecutionPhase3.isEmpty());
		for (final IInputData inputData : inputDataForExecutionPhase3) {
			final IPhaseConnection nextPhasenConnection = inputData.getNextPhaseConnection();
			final IStatus status = nextPhasenConnection.getStatus();
			assertEquals(PersistentStatus.INITIAL.getId(), status.getId());
			final String nextPhasequalifier = nextPhasenConnection.getNextPhasequalifier();
			assertEquals(PhaseQualifier.PHASE3.getName(), nextPhasequalifier);

		}
	}

}
