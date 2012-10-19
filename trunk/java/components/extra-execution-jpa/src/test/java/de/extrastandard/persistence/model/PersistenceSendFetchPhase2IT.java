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

/**
 * Integrationtest for PersistenceSendFetchPhase2IT.
 * 
 * @author Leonid Potap
 * @version $Id$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-persistence-jpa.xml",
		"/spring-ittest-hsqldb-propertyplaceholder.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class PersistenceSendFetchPhase2IT {

	@Inject
	@Named("executionPersistenceJpa")
	private IExecutionPersistence executionPersistence;

	@Inject
	@Named("persistenceTestSetup")
	private PersistenceTestSetup persistenceTestSetup;

	@Before
	public void before() throws Exception {
		persistenceTestSetup.setUpTestDatenForProcedureSendFetchPhase2();
		persistenceTestSetup.setUpTestDatenForProcedureSendFetchPhase3();
	}

	@Test
	public void testGetInputDataForExecutionByScenarionDataMatchAndPhase2()
			throws Exception {
		// Success Phase 1
		final List<IInputData> inputDataForExecutionPhase2 = executionPersistence
				.findInputDataForExecution(
						PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME,
						PhaseQualifier.PHASE2);
		assertFalse("Keine Daten für die Phase2 gefunden",
				inputDataForExecutionPhase2.isEmpty());
		for (final IInputData inputData : inputDataForExecutionPhase2) {
			final IPhaseConnection nextPhasenConnection = inputData
					.getNextPhaseConnection();
			final IStatus status = nextPhasenConnection.getStatus();
			assertEquals(PersistentStatus.INITIAL.getId(), status.getId());
			final String nextPhasequalifier = nextPhasenConnection
					.getNextPhasequalifier();
			assertEquals(PhaseQualifier.PHASE2.getName(), nextPhasequalifier);
		}
	}

	@Test
	public void testGetInputDataForExecutionByScenarionDataMatchAndPhase3()
			throws Exception {
		// Success Phase 2
		final List<IInputData> inputDataForExecutionPhase3 = executionPersistence
				.findInputDataForExecution(
						PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME,
						PhaseQualifier.PHASE3);
		assertFalse("Keine Daten für die Phase2 gefunden",
				inputDataForExecutionPhase3.isEmpty());
		for (final IInputData inputData : inputDataForExecutionPhase3) {
			final IPhaseConnection nextPhasenConnection = inputData
					.getNextPhaseConnection();
			final IStatus status = nextPhasenConnection.getStatus();
			assertEquals(PersistentStatus.INITIAL.getId(), status.getId());
			final String nextPhasequalifier = nextPhasenConnection
					.getNextPhasequalifier();
			assertEquals(PhaseQualifier.PHASE3.getName(), nextPhasequalifier);
		}
	}
}
