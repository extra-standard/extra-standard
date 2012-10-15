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
package de.extra.client.core.process;

import de.extrastandard.api.model.content.IDbQueryInputData;
import de.extrastandard.api.model.content.IFileInputData;
import de.extrastandard.api.model.content.ISingleContentInputData;
import de.extrastandard.api.model.content.ISingleQueryInputData;
import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.IInputData;

/**
 * @author Leonid Potap
 * @version $Id$
 */
public interface IRequestIdAcquisitionStrategy {

	/**
	 * @param inputData
	 * @param singleContentInputData
	 */
	void setRequestId(IInputData inputData, ISingleContentInputData singleContentInputData);

	/**
	 * If input data include only one element, the element.requestId is taken as
	 * InputData.requestId.
	 * 
	 * Otherwise execution.id set as RequestId the Input Data
	 * 
	 * @param fileInputData
	 * @param execution
	 */
	void setRequestId(IFileInputData fileInputData, IExecution execution);

	/**
	 * @param dbQueryInputData
	 * @param execution
	 */
	void setRequestId(IDbQueryInputData dbQueryInputData, IExecution execution);

	/**
	 * @param dbInputData
	 * @param singleQueryInputData
	 */
	void setRequestId(IInputData dbInputData, ISingleQueryInputData singleQueryInputData);

}
