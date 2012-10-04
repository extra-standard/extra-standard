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

import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import de.extra.client.core.process.IRequestIdAcquisitionStrategy;
import de.extrastandard.api.exception.ExtraCoreRuntimeException;
import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.model.execution.IInputData;

/**
 * Initialle implementation for the calculation of the RequestId
 * 
 * @author Leonid Potap
 * @version $Id$
 */
@Named("simpleRequestIdAcquisitionStrategy")
public class SimpleRequestIdAcquisitionStrategy implements IRequestIdAcquisitionStrategy {

	/**
	 * <pre>
	 * A distinction is made between 3 Strategies
	 * 1. RequestId the input from outside. Master of RequestId {@link IInputDataContainer}
	 * 2. RequestId is internally calculated from the execution. Master {@link IInputData}
	 * 3. Request Id is already calculated in an early execution phase
	 * </pre>
	 * 
	 * @see de.extra.client.core.process.IRequestIdAcquisitionStrategy#setRequestId(de.extrastandard.api.model.execution.IInputData,
	 *      de.extrastandard.api.model.content.IInputDataContainer)
	 */
	@Override
	public void setRequestId(final IInputData inputData, final IInputDataContainer inputDataContainer) {
		// Should still get more complexity, then distribute to multiple
		// classes.
		Assert.notNull(inputData, "Inputdata is null");
		Assert.notNull(inputDataContainer, "inputDataContainer is null");

		if (StringUtils.isNotEmpty(inputDataContainer.getRequestId())
				&& StringUtils.isNotEmpty(inputData.getRequestId())) {
			// RequestId is alredy calculated. No start phase of the Scenario
			if (!inputDataContainer.getRequestId().equals(inputData.getRequestId())) {
				// Expected RequestId is the same
				throw new ExtraCoreRuntimeException(
						"RequestId between different InputData and InputDataContainer. Inputdata RequestId: "
								+ inputDataContainer.getRequestId() + " inputDataContainer RequestId: "
								+ inputDataContainer.getRequestId());
			}
		}
		if (StringUtils.isNotEmpty(inputDataContainer.getRequestId())) {
			// Strategy 1
			inputData.setRequestId(inputDataContainer.getRequestId());
		} else if (inputData.getRequestId() == null) {
			// Strategy 2
			final String requestId = inputData.calculateRequestId();
			inputData.setRequestId(requestId);
			inputData.saveOrUpdate();
			inputDataContainer.setRequestId(requestId);

		} else if (inputData.getRequestId() != null) {
			// Strategy 3
			inputDataContainer.setRequestId(inputData.getRequestId());
		}

	}

}
