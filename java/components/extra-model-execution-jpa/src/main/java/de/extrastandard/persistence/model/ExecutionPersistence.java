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

import javax.inject.Named;

import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.IExecutionPersistence;
import de.extrastandard.api.model.execution.IProcedure;

/**
 * Einstiegsklasse zum Management von Executions.
 * 
 * @author Thorsten Vogel
 * @version $Id: ExecutionPersistence.java 508 2012-09-04 09:35:41Z
 *          thorstenvogel@gmail.com $
 */
@Named("executionPersistence")
public class ExecutionPersistence implements IExecutionPersistence {

	/**
	 * @see de.extrastandard.api.model.execution.IExecutionPersistence#startExecution(java.lang.String)
	 */
	@Override
	public IExecution startExecution(final IProcedure procedure, final String parameters) {
		return new Execution(procedure, parameters);
	}

}
