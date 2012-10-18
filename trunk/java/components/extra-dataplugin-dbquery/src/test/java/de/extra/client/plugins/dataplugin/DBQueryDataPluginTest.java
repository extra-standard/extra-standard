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
package de.extra.client.plugins.dataplugin;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.extra.client.core.model.inputdata.impl.DBQueryInputData;
import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.model.content.ISingleQueryInputData;
import de.extrastandard.api.model.execution.IExecutionPersistence;
import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.persistence.model.InputData;

/**
 * Test for ExtraRequestBuilder.
 * 
 * @author Leonid Potap
 * @since 1.0.0
 * @version 1.0.0
 */
@RunWith(MockitoJUnitRunner.class)
public class DBQueryDataPluginTest {

	private static final String RESPONSE_23 = "RESPONSE_23";

	private static final String REQUEST_23 = "REQUEST_23";

	private static final long ID_23 = 23L;

	@InjectMocks
	private DBQueryDataPlugin dbQueryDataPlugin;

	@Mock
	IExecutionPersistence executionPersistence;

	@Before
	public void setUp() throws Exception {
		final ArrayList<IInputData> testDatenList = new ArrayList<IInputData>();
		dbQueryDataPlugin.setExecutionPhase(PhaseQualifier.PHASE2.getName());
		final IInputData inputData = mock(InputData.class);
		when(inputData.getId()).thenReturn(ID_23);
		when(inputData.getRequestId()).thenReturn(REQUEST_23);
		when(inputData.getResponseId()).thenReturn(RESPONSE_23);
		when(inputData.getId()).thenReturn(23L);
		testDatenList.add(inputData);
		when(executionPersistence.findInputDataForExecution(anyString(), eq(PhaseQualifier.PHASE2))).thenReturn(
				testDatenList);
	}

	@Test
	public final void testGetData() {
		while (dbQueryDataPlugin.hasMoreData()) {
			final IInputDataContainer inputDataContainer = dbQueryDataPlugin.getData();
			Assert.assertTrue(inputDataContainer.isImplementationOf(DBQueryInputData.class));
			final DBQueryInputData dbQueryInputData = inputDataContainer.cast(DBQueryInputData.class);
			final List<ISingleQueryInputData> singleQueryInputDatas = dbQueryInputData.getInputData();
			Assert.assertEquals(1, singleQueryInputDatas.size());
			final ISingleQueryInputData singleQueryInputData = singleQueryInputDatas.get(0);
			Assert.assertEquals(RESPONSE_23, singleQueryInputData.getSourceResponceId());
			Assert.assertEquals(REQUEST_23, singleQueryInputData.getSourceRequestId());
		}

	}

}
