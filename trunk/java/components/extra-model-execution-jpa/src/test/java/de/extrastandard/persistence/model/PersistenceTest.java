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

import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.IInputDataTransition;
import de.extrastandard.api.model.execution.PersistentStatus;

/**
 * @author Thorsten Vogel
 * @version $Id$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-persistence-jpa.xml")
public class PersistenceTest {

	@Resource(type=ExecutionPersistence.class)
	private ExecutionPersistence executionPersistence;

	public static boolean dbInit = false;

	private Status statusInital;

	private Status statusEnveloped;

	private Status statusTransmitted;

	private Status statusResultsExpected;

	private Status statusResultsProcessed;

	private Status statusReceiptConfirmed;

	private Status statusDone;

	@Before
	public void before() throws Exception {
		if (!dbInit) {

			statusInital = new Status(PersistentStatus.INITIAL.toString());
			statusInital.saveOrUpdate();

			statusEnveloped = new Status(PersistentStatus.ENVELOPED.toString());
			statusEnveloped.saveOrUpdate();

			statusTransmitted = new Status(PersistentStatus.TRANSMITTED.toString());
			statusTransmitted.saveOrUpdate();

			statusResultsExpected = new Status(PersistentStatus.RESULTS_EXPECTED.toString());
			statusResultsExpected.saveOrUpdate();

			statusResultsProcessed = new Status(PersistentStatus.RESULTS_PROCESSED.toString());
			statusResultsProcessed.saveOrUpdate();

			statusReceiptConfirmed = new Status(PersistentStatus.RECEIPT_CONFIRMED.toString());
			statusReceiptConfirmed.saveOrUpdate();

			statusDone = new Status(PersistentStatus.DONE.toString());
			statusDone.saveOrUpdate();

			dbInit = true;
		}
	}

	@Test
	public void testExecutionConstruction() throws Exception {
		assertNotNull(executionPersistence);

		IExecution execution = executionPersistence.startExecution("-c d:/extras/configdir");

		assertNotNull(execution.getParameters());
		assertNotNull(execution.getId());
		assertNotNull(execution.getStartTime());

		// start input data
		IInputData inputData = execution.startInputData("inputIdentifier", "hashCode");
		assertNotNull(inputData.getId());
		assertEquals("inputIdentifier", inputData.getInputIdentifier());
		assertEquals("hashCode", inputData.getHashcode());
		assertEquals(execution.getId(), inputData.getId());
		assertEquals(statusInital, inputData.getStatus());

		// check if transition is created and pertinent
		IInputDataTransition firstTransition = inputData.getLastTransition();
		assertNotNull(firstTransition);
		assertNotNull(firstTransition.getId());
		assertNull(firstTransition.getPreviousStatus());
		assertNotNull(firstTransition.getCurrentStatus());
		assertEquals(PersistentStatus.INITIAL.toString(), firstTransition.getCurrentStatus().toString());
		assertNotNull(firstTransition.getInputData());
		assertEquals(inputData.getId(), firstTransition.getInputData().getId());

		Thread.sleep(500);

		// update progress
		inputData.updateProgress(statusEnveloped, "qualifier");

		IInputDataTransition envelopedTransition = inputData.getLastTransition();

		assertNotNull(envelopedTransition);
		assertTrue(!firstTransition.getId().equals(envelopedTransition.getId()));
		assertEquals(statusInital, envelopedTransition.getPreviousStatus());
		assertEquals(statusEnveloped, envelopedTransition.getCurrentStatus());
		assertEquals(statusEnveloped, inputData.getStatus());

		// success
		inputData.success("responseId");

		assertEquals("responseId", inputData.getResponseId());

	}


}
