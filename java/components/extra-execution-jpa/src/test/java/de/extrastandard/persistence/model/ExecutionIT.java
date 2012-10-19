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

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Transient;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import de.extra.client.core.model.inputdata.impl.DBQueryInputData;
import de.extra.client.core.model.inputdata.impl.InDBQueryInputData;
import de.extra.client.core.responce.impl.ResponseData;
import de.extra.client.core.responce.impl.SingleResponseData;
import de.extrastandard.api.model.content.IDbQueryInputData;
import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.model.content.ISingleQueryInputData;
import de.extrastandard.api.model.content.ISingleResponseData;
import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.IExecutionPersistence;
import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.IPhaseConnection;
import de.extrastandard.api.model.execution.IProcessTransition;
import de.extrastandard.api.model.execution.IStatus;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.persistence.repository.InputDataRepository;

/**
 * @author Leonid Potap
 * @version $Id$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-persistence-jpa.xml",
		"/spring-ittest.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class ExecutionIT {

	@Inject
	@Named("persistenceTestSetup")
	private PersistenceTestSetup persistenceTestSetup;

	@Transient
	@Inject
	@Named("inputDataRepository")
	private transient InputDataRepository inputDataRepository;

	@Inject
	@Named("executionPersistenceJpa")
	private IExecutionPersistence executionPersistence;

	@Before
	public void before() throws Exception {

		persistenceTestSetup.setUpTestDatenForProcedureSendFetchPhase2();
	}

	@Test
	public void testFailedPhase2() {
		final List<IInputData> sourceInputDataList = executionPersistence
				.findInputDataForExecution(
						PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME,
						PhaseQualifier.PHASE2);
		Assert.assertTrue("InputData is empty", !sourceInputDataList.isEmpty());
		final IExecution execution = executionPersistence.startExecution(
				PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME, "Test",
				PhaseQualifier.PHASE2);
		final IResponseData responseData = new ResponseData();
		final IDbQueryInputData dbQueryInputData = new DBQueryInputData();

		final String testRequestId = "TEST_1_REQUEST_ID";
		final String returnCode = "return code phase 2";
		final String returnText = "return text phase2";
		final String responseId = "response id phase 2";
		// TODO Refactor. Testvorbereitung auslagern
		for (final IInputData iSourcenputData : sourceInputDataList) {
			final Long sourceInputDataId = iSourcenputData.getId();
			final String inputDataRequestId = testRequestId + sourceInputDataId;
			final String inputDataReturnCode = returnCode + sourceInputDataId;
			final String inputDataReturnText = returnText + sourceInputDataId;
			final String inputDataResponseId = responseId + sourceInputDataId;
			final ISingleResponseData singleResponseData = new SingleResponseData(
					inputDataRequestId, inputDataReturnCode,
					inputDataReturnText, inputDataResponseId);
			responseData.addSingleResponse(singleResponseData);
			final ISingleQueryInputData singleQueryInputData = new InDBQueryInputData(
					sourceInputDataId, iSourcenputData.getRequestId(),
					iSourcenputData.getResponseId());
			dbQueryInputData.addSingleDBQueryInputData(singleQueryInputData);
			final IInputData newDbQueryInputData = execution
					.startDbQueryInputData(singleQueryInputData);

			newDbQueryInputData.setRequestId(singleResponseData.getRequestId());
		}

		final String errorCode = "ERROR_CODE";
		final String errorText = "ERROR_TEXT";
		execution.failed(errorCode, errorText);

		assertEquals("Unexpected ErrorCode", errorCode,
				execution.getErrorCode());
		assertEquals("Unexpected ErrorMessage", errorText,
				execution.getErrorMessage());
		final IProcessTransition lastTransition = execution.getLastTransition();
		assertNotNull(lastTransition);
		final IStatus currentStatus = lastTransition.getCurrentStatus();
		assertNotNull(currentStatus);
		assertEquals("Unexpected Status", PersistentStatus.FAIL.name(),
				currentStatus.getName());
		final Set<IInputData> inputDataSet = execution.getInputDataSet();
		for (final IInputData iInputData : inputDataSet) {
			final IPhaseConnection currentPhaseConnection = iInputData
					.getCurrentPhaseConnection();
			final IStatus status = currentPhaseConnection.getStatus();
			assertEquals("Unexpected Status", PersistentStatus.FAIL.name(),
					status.getName());
		}
		for (final IInputData iquelleInputData : sourceInputDataList) {
			// refresh
			final InputData quelleInputData = inputDataRepository
					.findOne(iquelleInputData.getId());
			// prüfen PhaseConnection
			final IPhaseConnection quellePhaseConnection = quelleInputData
					.getNextPhaseConnection();
			final IInputData targetInputData = quellePhaseConnection
					.getTargetInputData();
			assertNotNull(targetInputData);
			final IStatus quellePhaseConnectionStatus = quellePhaseConnection
					.getStatus();
			assertNotNull(quellePhaseConnectionStatus);
			assertEquals(PersistentStatus.FAIL.name(),
					quellePhaseConnectionStatus.getName());
		}
	}

	@Test
	public void testInputDataSucessPhase2AnotherTransaction() {
		final List<IInputData> sourceInputDataList = executionPersistence
				.findInputDataForExecution(
						PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME,
						PhaseQualifier.PHASE2);
		Assert.assertTrue("InputData is empty", !sourceInputDataList.isEmpty());
		final IExecution execution = executionPersistence.startExecution(
				PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME, "Test",
				PhaseQualifier.PHASE2);
		final IResponseData responseData = new ResponseData();
		final IDbQueryInputData dbQueryInputData = new DBQueryInputData();

		final String testRequestId = "TEST_REQUEST_ID";
		final String returnCode = "return code phase 2";
		final String returnText = "return text phase2";
		final String responseId = "response id phase 2";

		for (final IInputData iSourceInputData : sourceInputDataList) {
			final Long sourceInputDataId = iSourceInputData.getId();
			final String inputDataRequestId = testRequestId + sourceInputDataId;
			final String inputDataReturnCode = returnCode + sourceInputDataId;
			final String inputDataReturnText = returnText + sourceInputDataId;
			final String inputDataResponseId = responseId + sourceInputDataId;
			final ISingleResponseData singleResponseData = new SingleResponseData(
					inputDataRequestId, inputDataReturnCode,
					inputDataReturnText, inputDataResponseId);
			responseData.addSingleResponse(singleResponseData);
			final ISingleQueryInputData singleQueryInputData = new InDBQueryInputData(
					sourceInputDataId, iSourceInputData.getRequestId(),
					iSourceInputData.getResponseId());
			dbQueryInputData.addSingleDBQueryInputData(singleQueryInputData);
			final IInputData newDbQueryInputData = execution
					.startDbQueryInputData(singleQueryInputData);

			newDbQueryInputData.setRequestId(singleResponseData.getRequestId());
		}

		execution.endExecution(responseData);

		final IProcessTransition lastTransition = execution.getLastTransition();
		assertNotNull(lastTransition);
		final IStatus currentStatus = lastTransition.getCurrentStatus();
		assertNotNull(currentStatus);
		assertEquals(PersistentStatus.DONE.name(), currentStatus.getName());
		final Set<IInputData> inputDataSet = execution.getInputDataSet();
		for (final IInputData iInputData : inputDataSet) {
			final String requestId = iInputData.getRequestId();
			final ISingleResponseData response = responseData
					.getResponse(requestId);
			assertEquals(response.getResponseId(), iInputData.getResponseId());
			assertEquals(response.getReturnCode(), iInputData.getReturnCode());
			assertEquals(response.getReturnText(), iInputData.getReturnText());
		}
		for (final IInputData iquelleInputData : sourceInputDataList) {
			// refresh
			final InputData quelleInputData = inputDataRepository
					.findOne(iquelleInputData.getId());
			// prüfen PhaseConnection
			final IPhaseConnection quellePhaseConnection = quelleInputData
					.getNextPhaseConnection();
			final IInputData targetInputData = quellePhaseConnection
					.getTargetInputData();
			assertNotNull(targetInputData);
			final IStatus quellePhaseConnectionStatus = quellePhaseConnection
					.getStatus();
			assertNotNull(quellePhaseConnectionStatus);
			assertEquals(PersistentStatus.DONE.name(),
					quellePhaseConnectionStatus.getName());
		}
	}
}
