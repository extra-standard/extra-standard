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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.extrastandard.api.model.execution.IExecutionPersistence;
import de.extrastandard.api.model.execution.ICommunicationProtocol;
import de.extrastandard.api.model.execution.PhaseQualifier;

/**
 * Integration Test for InputData.
 * 
 * @author Leonid Potap
 * @version $Id$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-persistence-jpa.xml",
		"/spring-ittest.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class InputDataIT {

	@Inject
	@Named("persistenceTestSetup")
	private PersistenceTestSetup persistenceTestSetup;

	@Inject
	@Named("executionPersistenceJpa")
	private IExecutionPersistence executionPersistence;

	@Before
	public void before() throws Exception {
		persistenceTestSetup.setUpTestDatenForProcedureSendFetchPhase2();
	}

	@Test
	@Transactional
	public void testFindInputDataForExecutionWithLimit() {
		final Integer testDataSize = 6;
		for (Integer counter = 0; counter < testDataSize; counter++) {
			persistenceTestSetup.setUpTestDatenForProcedureSendFetchPhase2();
		}
		final Integer inputDataLimit = 5;
		final List<ICommunicationProtocol> findInputDataForExecution = executionPersistence
				.findInputDataForExecution(
						PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME,
						PhaseQualifier.PHASE2, inputDataLimit);
		Assert.assertTrue("Zu viele Daten ausgewählt",
				findInputDataForExecution.size() <= inputDataLimit);
	}

	@Test
	@Transactional
	public void testCountInputDataForExecution() {
		final Long countInputDataForExecutionBeforeInsert = executionPersistence
				.countInputDataForExecution(
						PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME,
						PhaseQualifier.PHASE2);
		final Integer testDataSize = 12;
		for (Integer counter = 0; counter < testDataSize; counter++) {
			persistenceTestSetup.setUpTestDatenForProcedureSendFetchPhase2();
		}
		final Long countInputDataForExecution = executionPersistence
				.countInputDataForExecution(
						PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME,
						PhaseQualifier.PHASE2);
		final Long expectedCount = countInputDataForExecutionBeforeInsert
				+ testDataSize;
		Assert.assertEquals("Count stimmt nicht", expectedCount,
				countInputDataForExecution);
	}

}
