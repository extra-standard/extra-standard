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
package de.extrastandard.procedures.sa.acceptance;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.extra.client.starter.ExtraClient;
import de.extra.client.starter.ExtraClientTestBasic;
import de.extrastandard.api.model.execution.ICommunicationProtocol;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.persistence.model.Execution;
import de.extrastandard.persistence.model.ExecutionPersistenceJpa;
import de.extrastandard.persistence.model.ProcessTransition;
import de.extrastandard.persistence.repository.ExecutionRepository;

/**
 * Ausfuehrung Sterbedaten Fachverfahren Phase 1
 * 
 * @author r52gma
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-persistence-jpa.xml",
		"/property-placeholder-test.xml",
		"/conf/phase1/spring-phase1-acceptance-flyway.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class Phase1Acceptance {

	private static final String TEST_CONFIG = "/conf/phase1";

	private static final String LOG_DIR = "/logs";

	private final ExtraClientTestBasic extraClientTestBasic = new ExtraClientTestBasic();

	private ExtraClient extraClient;

	@Inject
	@Named("executionPersistenceJpa")
	private ExecutionPersistenceJpa executionPersistenceJpa;

	@Inject
	@Named("executionRepository")
	private ExecutionRepository executionRepository;

	@Before
	public void setUp() throws Exception {
		extraClient = extraClientTestBasic.createExtraKlient(TEST_CONFIG,
				LOG_DIR);
	}

	@Test
	public void testExecute() throws Exception {
		extraClientTestBasic.testExecute(extraClient);
	}

	@After
	public void checkResult() {
		final int expectedExecutionSize = 3;
		final String expectedPhase = "PHASE1";
		final String expectedParametersSuffix = "\\conf\\phase1";
		final String expectedReturnCode = "C00";

		final List<Execution> allExecutions = executionRepository.findAll();
		Assert.assertEquals("Unexpected Execution Size", expectedExecutionSize,
				allExecutions.size());
		for (final Execution execution : allExecutions) {
			Assert.assertNull("ErrorCode is not null", execution.getErrorCode());
			Assert.assertNull("ErrorMessage is not null",
					execution.getErrorMessage());
			Assert.assertEquals("Unexpected Phase", expectedPhase,
					execution.getPhase());
			Assert.assertNotNull("Parameters ist null",
					execution.getParameters());
			Assert.assertTrue("Unexpected Parameters", execution
					.getParameters().endsWith(expectedParametersSuffix));
			final ProcessTransition lastTransition = execution
					.getLastTransition();
			Assert.assertNotNull("LastTransition ist null", lastTransition);
			final String statusName = lastTransition.getCurrentStatus()
					.getName();
			Assert.assertEquals("Unexpected Phase",
					PersistentStatus.DONE.name(), statusName);
			final Set<ICommunicationProtocol> communicationProtocols = execution
					.getCommunicationProtocols();
			Assert.assertEquals("Unexpected Count of CommunicationProtokols",
					1, communicationProtocols.size());
			for (final ICommunicationProtocol communicationProtocol : communicationProtocols) {
				Assert.assertEquals("Unexpected ReturnCode",
						expectedReturnCode,
						communicationProtocol.getReturnCode());
				Assert.assertNull("Unexpected nextPhaseConnection",
						communicationProtocol.getNextPhaseConnection());
			}

		}

	}
}
