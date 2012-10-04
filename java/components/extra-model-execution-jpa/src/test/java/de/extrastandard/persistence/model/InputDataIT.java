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
import javax.persistence.Transient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.persistence.repository.InputDataRepository;
import de.extrastandard.persistence.repository.ProcedureRepository;
import de.extrastandard.persistence.repository.StatusRepository;

/**
 * @author Leonid Potap
 * @version $Id$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-persistence-jpa.xml", "/spring-ittest-hsqldb-propertyplaceholder.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
// @ContextConfiguration(locations = { "/spring-persistence-jpa.xml",
// "/spring-ittest-oracle-propertyplaceholder.xml" })
public class InputDataIT {

	@Inject
	@Named("persistenceTestSetup")
	private PersistenceTestSetup persistenceTestSetup;

	@Transient
	@Inject
	@Named("inputDataRepository")
	private transient InputDataRepository inputDataRepository;

	@Inject
	@Named("procedureRepository")
	private ProcedureRepository procedureRepository;

	@Inject
	@Named("statusRepository")
	private StatusRepository statusRepository;

	private Procedure procedureSendFetch;

	private Status statusResultexpected;

	@Before
	public void before() throws Exception {

		persistenceTestSetup.setUpTestDatenForProcedureSendFetchPhase2();
		procedureSendFetch = procedureRepository.findByName(PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME);
		statusResultexpected = statusRepository.findByName(PersistentStatus.RESULTS_EXPECTED.name());
	}

	@Test
	public void testInputDataSucessAnotherTransaction() throws Exception {
		final List<IInputData> listInputData = inputDataRepository.findByProcedureAndStatus(procedureSendFetch,
				statusResultexpected);
		Assert.assertTrue("InputData is empty", !listInputData.isEmpty());
		for (final IInputData inputData : listInputData) {
			final String testResponseId = "10";
			inputData.success(testResponseId, PhaseQualifier.PHASE2);
			Assert.assertEquals(testResponseId, inputData.getResponseId());
			Assert.assertEquals(PersistentStatus.RESULTS_PROCESSED.name(), inputData.getLastTransition()
					.getCurrentStatus().getName());
			Assert.assertNull(inputData.getErrorCode());
			Assert.assertNull(inputData.getErrorMessage());
		}

	}
}
