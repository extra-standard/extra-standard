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
package de.extra.client.core.process.impl;

import java.util.List;

import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import de.extra.client.core.process.IRequestIdAcquisitionStrategy;
import de.extrastandard.api.exception.ExtraCoreRuntimeException;
import de.extrastandard.api.model.content.ICriteriaQueryInputData;
import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.model.content.ISingleContentInputData;
import de.extrastandard.api.model.content.ISingleInputData;
import de.extrastandard.api.model.content.IDbQueryInputData;
import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.ICommunicationProtocol;

/**
 * Initialle implementation for the calculation of the RequestId
 * 
 * @author Leonid Potap
 * @version $Id$
 */
@Named("simpleRequestIdAcquisitionStrategy")
public class SimpleRequestIdAcquisitionStrategy implements
		IRequestIdAcquisitionStrategy {

	@Override
	public void setRequestId(ICommunicationProtocol dbInputData,
			ISingleInputData singleInputData) {
		Assert.notNull(dbInputData, "Inputdata is null");
		Assert.notNull(singleInputData, "inputDataContainer is null");

		if (ISingleContentInputData.class.isAssignableFrom(singleInputData
				.getClass())) {
			ISingleContentInputData singleContentInputData = ISingleContentInputData.class
					.cast(singleInputData);
			setRequestIdForContentInputData(dbInputData, singleContentInputData);
		} else if (IDbQueryInputData.class.isAssignableFrom(singleInputData
				.getClass())) {
			IDbQueryInputData iSingleQueryInputData = IDbQueryInputData.class
					.cast(singleInputData);
			setRequestIdForQueryInpuData(dbInputData, iSingleQueryInputData);
			// TODO MAXRESP (06.11.12)
		} else if (ICriteriaQueryInputData.class
				.isAssignableFrom(singleInputData.getClass())) {
			ICriteriaQueryInputData singleQueryInputData = ICriteriaQueryInputData.class
					.cast(singleInputData);
			setRequestIdForSingleQueryInputData(dbInputData,
					singleQueryInputData);
		} else {
			throw new ExtraCoreRuntimeException("Unexpected Data Type"
					+ singleInputData.getClass());
		}

	}

	/**
	 * <pre>
	 * A distinction is made between 3 Strategies
	 * 1. RequestId the input from outside. Master of RequestId {@link IInputDataContainer}
	 * 2. RequestId is internally calculated from the execution. Master {@link ICommunicationProtocol}
	 * 3. Request Id is already calculated in an early execution phase
	 * </pre>
	 * 
	 * @see de.extra.client.core.process.IRequestIdAcquisitionStrategy#setRequestId(de.extrastandard.api.model.execution.ICommunicationProtocol,
	 *      de.extrastandard.api.model.content.IInputDataContainer)
	 */
	private void setRequestIdForContentInputData(final ICommunicationProtocol inputData,
			final ISingleContentInputData singleContentInputData) {
		Assert.notNull(inputData, "Inputdata is null");
		Assert.notNull(singleContentInputData, "inputDataContainer is null");
		if (StringUtils.isNotEmpty(singleContentInputData.getRequestId())
				&& StringUtils.isNotEmpty(inputData.getRequestId())) {
			// RequestId is alredy calculated. No start phase of the Scenario
			if (!singleContentInputData.getRequestId().equals(
					inputData.getRequestId())) {
				// Expected RequestId is the same
				throw new ExtraCoreRuntimeException(
						"RequestId between different InputData and InputDataContainer. Inputdata RequestId: "
								+ inputData.getRequestId()
								+ " inputDataContainer RequestId: "
								+ singleContentInputData.getRequestId());
			}
		}
		if (StringUtils.isNotEmpty(singleContentInputData.getRequestId())) {
			// Strategy 1
			inputData.setRequestId(singleContentInputData.getRequestId());
		} else if (inputData.getRequestId() == null) {
			// Strategy 2
			final String requestId = inputData.calculateRequestId();
			inputData.setRequestId(requestId);
			singleContentInputData.setRequestId(requestId);
		} else if (inputData.getRequestId() != null) {
			// Strategy 3
			singleContentInputData.setRequestId(inputData.getRequestId());
		}

	}

	private void setRequestIdForQueryInpuData(final ICommunicationProtocol inputData,
			final IDbQueryInputData singleQueryInputData) {
		Assert.notNull(inputData, "Inputdata is null");
		Assert.notNull(singleQueryInputData, "inputDataContainer is null");
		// Einzelne Value, die an Server übertragen wird und zur Identifizierung
		// der Nachrichten dienen kann ist der Ursprung-ResponseId

		final String requestId = singleQueryInputData.getSourceResponceId();
		inputData.setRequestId(requestId);
		singleQueryInputData.setRequestId(requestId);
	}

	// TODO MAXRESP (06.11.12)
	private void setRequestIdForSingleQueryInputData(
			final ICommunicationProtocol inputData,
			final ICriteriaQueryInputData singleQueryInputData) {
		Assert.notNull(inputData, "Inputdata is null");
		Assert.notNull(singleQueryInputData, "inputDataContainer is null");
		String inputIdentifier = singleQueryInputData.getInputIdentifier();

		// ??
//		singleQueryInputData.setRequestId(inputIdentifier);
//		inputData.setRequestId(inputIdentifier);
		String requestIdKand = String.valueOf(inputData.getId());
		singleQueryInputData.setRequestId(requestIdKand);
		inputData.setRequestId(requestIdKand);
	}

	@Override
	public void setRequestId(final IInputDataContainer iInputDataContainer,
			final IExecution execution) {
		Assert.notNull(iInputDataContainer, "InputDataContainer is null");
		Assert.notNull(execution, "Execution is null");
		Assert.isTrue(!iInputDataContainer.isContentEmpty(),
				"Content is Empty!!");
		if (iInputDataContainer.getContentSize() == 1) {
			final List<ISingleInputData> inputDataList = iInputDataContainer
					.getContent();
			final ISingleInputData singleInputData = inputDataList.get(0);
			iInputDataContainer.setRequestId(singleInputData.getRequestId());
		} else {
			final Long executionId = execution.getId();
			iInputDataContainer.setRequestId(String.valueOf(executionId));
		}
	}

}
