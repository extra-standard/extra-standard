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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.IInputDataTransition;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.api.model.execution.PhaseQualifier;

/**
 * @author Thorsten Vogel
 * @version $Id: PersistenceTest.java 562 2012-09-06 14:12:43Z
 *          thorstenvogel@gmail.com $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-persistence-jpa.xml", "/spring-ittest-hsqldb-propertyplaceholder.xml" })
// @ContextConfiguration(locations = { "/spring-persistence-jpa.xml",
// "/spring-ittest-oracle-propertyplaceholder.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class PersistenceIT {

	@Resource(type = ExecutionPersistence.class)
	private ExecutionPersistence executionPersistence;

	public static boolean dbInit = false;

	private Status statusInital;

	private Status statusEnveloped;

	private Status statusTransmitted;

	private Status statusResultsExpected;

	private Status statusResultsProcessed;

	private Status statusReceiptConfirmed;

	private Status statusDone;

	private Procedure procedureDataMatch;

	@Before
	public void before() throws Exception {
		// if (!dbInit) {

		statusInital = new Status(PersistentStatus.INITIAL);
		statusInital.saveOrUpdate();

		statusEnveloped = new Status(PersistentStatus.ENVELOPED);
		statusEnveloped.saveOrUpdate();

		statusTransmitted = new Status(PersistentStatus.TRANSMITTED);
		statusTransmitted.saveOrUpdate();

		statusResultsExpected = new Status(PersistentStatus.RESULTS_EXPECTED);
		statusResultsExpected.saveOrUpdate();

		statusResultsProcessed = new Status(PersistentStatus.RESULTS_PROCESSED);
		statusResultsProcessed.saveOrUpdate();

		statusReceiptConfirmed = new Status(PersistentStatus.RECEIPT_CONFIRMED);
		statusReceiptConfirmed.saveOrUpdate();

		statusDone = new Status(PersistentStatus.DONE);
		statusDone.saveOrUpdate();

		final Mandator mandatorTEST = new Mandator("TEST");
		mandatorTEST.saveOrUpdate();

		final ProcedureType procedureSendFetch = new ProcedureType("SCENARIO_SEND_FETCH", statusReceiptConfirmed);
		// procedureSendFetch.saveOrUpdate();

		final ProcedurePhaseConfiguration procedurePhase1Configuration = new ProcedurePhaseConfiguration(
				procedureSendFetch, PhaseQualifier.PHASE1, PersistentStatus.RESULTS_EXPECTED);
		procedurePhase1Configuration.saveOrUpdate();

		final ProcedurePhaseConfiguration procedurePhase2Configuration = new ProcedurePhaseConfiguration(
				procedureSendFetch, PhaseQualifier.PHASE2, PersistentStatus.RESULTS_PROCESSED);
		procedurePhase2Configuration.saveOrUpdate();

		final ProcedurePhaseConfiguration procedurePhase3Configuration = new ProcedurePhaseConfiguration(
				procedureSendFetch, PhaseQualifier.PHASE3, PersistentStatus.RECEIPT_CONFIRMED);
		procedurePhase3Configuration.saveOrUpdate();

		procedureDataMatch = new Procedure(mandatorTEST, procedureSendFetch, "Datenabgleich");
		procedureDataMatch.saveOrUpdate();

		// dbInit = true;
		// }
	}

	@Test
	public void testExecutionConstruction() throws Exception {
		assertNotNull(executionPersistence);

		final IExecution execution = executionPersistence.startExecution(procedureDataMatch, "-c d:/extras/configdir");

		assertNotNull(execution.getParameters());
		assertNotNull(execution.getId());
		assertNotNull(execution.getStartTime());

		// start input data
		final IInputData inputData = execution.startInputData("inputIdentifier", "hashCode");
		assertNotNull(inputData.getId());
		assertEquals("inputIdentifier", inputData.getInputIdentifier());
		assertEquals("hashCode", inputData.getHashcode());
		assertEquals(execution.getId(), inputData.getExecution().getId());
		assertEquals(statusInital, inputData.getLastTransition().getCurrentStatus());

		// check if transition is created and pertinent
		final IInputDataTransition firstTransition = inputData.getLastTransition();
		assertNotNull(firstTransition);
		assertNotNull(firstTransition.getId());
		assertNull(firstTransition.getPreviousStatus());
		assertNotNull(firstTransition.getCurrentStatus());
		assertEquals(PersistentStatus.INITIAL.toString(), firstTransition.getCurrentStatus().getName());
		assertNotNull(firstTransition.getInputData());
		assertEquals(inputData.getId(), firstTransition.getInputData().getId());

		Thread.sleep(500);

		// update progress Enveloped
		inputData.updateProgress(statusEnveloped);

		final IInputDataTransition envelopedTransition = inputData.getLastTransition();

		assertNotNull(envelopedTransition);
		assertTrue(!firstTransition.getId().equals(envelopedTransition.getId()));
		assertEquals(statusInital, envelopedTransition.getPreviousStatus());
		assertEquals(statusEnveloped, envelopedTransition.getCurrentStatus());
		assertEquals(statusEnveloped, inputData.getLastTransition().getCurrentStatus());

		// update progress Transmitted
		inputData.updateProgress(statusTransmitted);

		final IInputDataTransition transmittedTransition = inputData.getLastTransition();

		assertNotNull(transmittedTransition);
		assertTrue(!envelopedTransition.getId().equals(transmittedTransition.getId()));
		assertEquals(statusEnveloped, transmittedTransition.getPreviousStatus());
		assertEquals(statusTransmitted, transmittedTransition.getCurrentStatus());
		assertEquals(statusTransmitted, inputData.getLastTransition().getCurrentStatus());

	}

	@Test
	public void testInputDataSuccess() throws Exception {
		assertNotNull(executionPersistence);

		final IExecution execution = executionPersistence.startExecution(procedureDataMatch, "-c d:/extras/configdir");
		final IInputData inputData = execution.startInputData("inputIdentifier", "hashCode");
		// success Phase 1
		inputData.success("responseId", PhaseQualifier.PHASE1);
		final IInputDataTransition resultExcectedTransition = inputData.getLastTransition();
		assertNotNull(resultExcectedTransition);
		assertEquals(statusInital, resultExcectedTransition.getPreviousStatus());
		assertEquals(statusResultsExpected, resultExcectedTransition.getCurrentStatus());
		assertEquals(statusResultsExpected, inputData.getLastTransition().getCurrentStatus());
		assertEquals("responseId", inputData.getResponseId());

		// success Phase 2
		inputData.success("responseId", PhaseQualifier.PHASE2);
		final IInputDataTransition resultProcessedTransition = inputData.getLastTransition();
		assertNotNull(resultProcessedTransition);
		assertEquals(statusResultsExpected, resultProcessedTransition.getPreviousStatus());
		assertEquals(statusResultsProcessed, resultProcessedTransition.getCurrentStatus());
		assertEquals(statusResultsProcessed, inputData.getLastTransition().getCurrentStatus());
		assertEquals("responseId", inputData.getResponseId());

		// success Phase 3
		inputData.success("responseId", PhaseQualifier.PHASE3);
		final IInputDataTransition inputDataTransitionDone = inputData.getLastTransition();
		assertNotNull(inputDataTransitionDone);
		assertEquals(statusReceiptConfirmed, inputDataTransitionDone.getPreviousStatus());
		assertEquals(statusDone, inputDataTransitionDone.getCurrentStatus());
		assertEquals(statusDone, inputData.getLastTransition().getCurrentStatus());
		assertEquals("responseId", inputData.getResponseId());

	}

}
