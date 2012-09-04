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

	public static boolean dbInit = false;

	@Before
	public void before() throws Exception {
		if (!dbInit) {
			// insert status
			new Status(PersistentStatus.INITIAL.toString()).saveOrUpdate();
			new Status(PersistentStatus.ENVELOPED.toString()).saveOrUpdate();
			new Status(PersistentStatus.TRANSMITTED.toString()).saveOrUpdate();
			new Status(PersistentStatus.RESULTS_EXPECTED.toString()).saveOrUpdate();
			new Status(PersistentStatus.RESULTS_PROCESSED.toString()).saveOrUpdate();
			new Status(PersistentStatus.RECEIPT_CONFIRMED.toString()).saveOrUpdate();
			new Status(PersistentStatus.DONE.toString()).saveOrUpdate();
			dbInit = true;
		}
	}

	@Resource(type=ExecutionPersistence.class)
	private ExecutionPersistence executionPersistence;

	@Test
	public void testExecutionConstruction() {
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

		IInputDataTransition transition = inputData.getLastTransition();
		assertNotNull(transition);
		assertNotNull(transition.getId());
		assertNull(transition.getPreviousStatus());
		assertNotNull(transition.getCurrentStatus());
		assertEquals(PersistentStatus.INITIAL.toString(), transition.getCurrentStatus().toString());
		assertNotNull(transition.getInputData());
		assertEquals(inputData.getId(), transition.getInputData().getId());
	}


}
