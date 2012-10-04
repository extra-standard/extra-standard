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
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;
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

import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.IInputDataTransition;
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

	@Resource(type = ExecutionPersistence.class)
	private ExecutionPersistence executionPersistence;

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

		final IExecution execution = executionPersistence.startExecution(
				PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME, "-c d:/extras/configdir");

		final IInputData inputData = execution.startInputData("inputIdentifier", "hashCode");
		final IInputData inputData2 = execution.startInputData("inputIdentifier2", "hashCode2");
		final IInputData inputData3 = execution.startInputData("inputIdentifier3", "hashCode3");
		inputData.success("responseId", PhaseQualifier.PHASE1);
		inputData2.success("responseId2", PhaseQualifier.PHASE1);
		inputData3.success("responseId3", PhaseQualifier.PHASE1);
		inputData3.success("responseId4", PhaseQualifier.PHASE2);
		LOGGER.debug("InputData3 Status: " + inputData3.getLastTransition().getCurrentStatus());
	}

	@Test
	public void testGetInputDataForExecutionByScenarionDataMatchAndPhase2() throws Exception {
		// success Phase 2
		final List<IInputData> inputDataForExecutionPhase2 = executionPersistence.findInputDataForExecution(
				PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME, PhaseQualifier.PHASE2);
		assertFalse("Keine Daten für die Phase2 gefunden", inputDataForExecutionPhase2.isEmpty());
		for (final IInputData inputData : inputDataForExecutionPhase2) {
			final IInputDataTransition resultExpectedTransition = inputData.getLastTransition();
			assertNotNull(resultExpectedTransition);
			assertEquals(statusResultsExpected, resultExpectedTransition.getCurrentStatus());
		}
	}

	@Test
	public void testGetInputDataForExecutionByScenarionDataMatchAndPhase3() throws Exception {
		// success Phase 2
		final List<IInputData> inputDataForExecutionPhase3 = executionPersistence.findInputDataForExecution(
				PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME, PhaseQualifier.PHASE3);
		assertFalse("Keine Daten für die Phase3 gefunden", inputDataForExecutionPhase3.isEmpty());
		for (final IInputData inputData : inputDataForExecutionPhase3) {
			final IInputDataTransition resultProcessedTransition = inputData.getLastTransition();
			assertNotNull(resultProcessedTransition);
			assertEquals(statusResultsProcessed, resultProcessedTransition.getCurrentStatus());
		}
	}

}
