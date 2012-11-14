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

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.extra.client.core.model.inputdata.impl.SingleStringInputData;
import de.extra.client.core.responce.impl.ResponseData;
import de.extra.client.core.responce.impl.SingleResponseData;
import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.model.content.ISingleContentInputData;
import de.extrastandard.api.model.content.ISingleResponseData;
import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.IProcessTransition;
import de.extrastandard.api.model.execution.IStatus;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.api.model.execution.PhaseQualifier;

/**
 * Integration Test for Persistence.
 * 
 * @author Thorsten Vogel
 * @version $Id: PersistenceTest.java 562 2012-09-06 14:12:43Z
 *          thorstenvogel@gmail.com $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-persistence-jpa.xml",
		"/spring-ittest.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class PersistenceIT {

	@Inject
	@Named("executionPersistenceJpa")
	private ExecutionPersistenceJpa executionPersistence;

	@Before
	public void before() throws Exception {
		// persistenceTestSetup.setupInitialDaten();
		// persistenceTestSetup.setupProcedureSendFeths();
	}

	@Test
	public void testExecutionConstruction() throws Exception {
		assertNotNull(executionPersistence);

		final String parameter = "-c d:/extras/configdir";
		final IExecution execution = executionPersistence.startExecution(
				PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME, parameter,
				PhaseQualifier.PHASE1);

		assertNotNull(execution.getParameters());
		assertNotNull(execution.getId());
		assertNotNull(execution.getStartTime());
		assertEquals(parameter, execution.getParameters());
		assertEquals(PhaseQualifier.PHASE1.getName(), execution.getPhase());

		final IProcessTransition lastTransitionInitial = execution
				.getLastTransition();
		assertNotNull(lastTransitionInitial);
		final IStatus currentStatusInitial = lastTransitionInitial
				.getCurrentStatus();
		assertNotNull(currentStatusInitial);
		assertEquals(PersistentStatus.INITIAL.getId(),
				currentStatusInitial.getId());
		final IStatus previousStatus = lastTransitionInitial
				.getPreviousStatus();
		assertNull(previousStatus);

		execution.updateProgress(PersistentStatus.ENVELOPED);
		final IProcessTransition lastTransitionEnveloped = execution
				.getLastTransition();
		assertNotNull(lastTransitionEnveloped);
		final IStatus currentStatusEnveloped = lastTransitionEnveloped
				.getCurrentStatus();
		assertNotNull(currentStatusEnveloped);
		assertEquals(PersistentStatus.ENVELOPED.getId(),
				currentStatusEnveloped.getId());
		final IStatus previousStatusInitial = lastTransitionEnveloped
				.getPreviousStatus();
		assertNotNull(previousStatusInitial);
		assertEquals(PersistentStatus.INITIAL.getId(),
				previousStatusInitial.getId());

		execution.updateProgress(PersistentStatus.TRANSMITTED);
		final IProcessTransition lastTransitionTransmitted = execution
				.getLastTransition();
		assertNotNull(lastTransitionTransmitted);
		final IStatus currentStatusTransmitted = lastTransitionTransmitted
				.getCurrentStatus();
		assertNotNull(currentStatusTransmitted);
		assertEquals(PersistentStatus.TRANSMITTED.getId(),
				currentStatusTransmitted.getId());
		final IStatus previousStatusEnveloped = lastTransitionTransmitted
				.getPreviousStatus();
		assertNotNull(previousStatusEnveloped);
		assertEquals(PersistentStatus.ENVELOPED.getId(),
				previousStatusEnveloped.getId());

		final ISingleContentInputData singleContentInputData = new SingleStringInputData(
				"test data");
		final IInputData inputData = execution
				.startInputData(singleContentInputData);
		assertNotNull(inputData);
		assertEquals(singleContentInputData.getHashCode(),
				inputData.getHashcode());
		assertEquals(singleContentInputData.getInputIdentifier(),
				inputData.getInputIdentifier());

		final String requestId = inputData.calculateRequestId();
		singleContentInputData.setRequestId(requestId);
		inputData.setRequestId(requestId);
		assertEquals(singleContentInputData.getRequestId(),
				inputData.getRequestId());

		final IResponseData responceData = new ResponseData();
		final Boolean successful = true;
		final ISingleResponseData singleResponseData = new SingleResponseData(
				requestId, "ReturnCode", "ReturnText", "RESPONSE_ID",
				successful);
		responceData.addSingleResponse(singleResponseData);
		execution.endExecution(responceData);
		final IProcessTransition lastTransitionDone = execution
				.getLastTransition();
		assertNotNull(lastTransitionDone);
		final IStatus currentStatusDone = lastTransitionDone.getCurrentStatus();
		assertNotNull(currentStatusDone);
		assertEquals(PersistentStatus.DONE.getId(), currentStatusDone.getId());
		final IStatus previousStatusTransmitted = lastTransitionDone
				.getPreviousStatus();
		assertNotNull(previousStatusTransmitted);
		assertEquals(PersistentStatus.TRANSMITTED.getId(),
				previousStatusTransmitted.getId());

		// TODO IInputData testen

		Thread.sleep(500);

	}

}
