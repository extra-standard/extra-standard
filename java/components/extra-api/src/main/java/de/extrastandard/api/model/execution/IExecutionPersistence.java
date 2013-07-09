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
package de.extrastandard.api.model.execution;

import java.util.List;

/**
 * Stellt Funktionen zur persistenten Protokollierung von {@link IExecution}s
 * bereit.
 * 
 * @author Thorsten Vogel
 * @version $Id: IExecutionPersistence.java 487 2012-09-03 13:07:52Z
 *          thorstenvogel@gmail.com $
 */
public interface IExecutionPersistence {

	/**
	 * <pre>
	 * The method starts an execution. 
	 * If the procedure name is not found, an unchecked exception is thrown.
	 * </pre>
	 * 
	 * @param procedure
	 *            Execution Scenario
	 * @param parameters
	 *            Aufrufparameter
	 * @return eine {@link IExecution} Instanz.
	 */
	IExecution startExecution(String procedureName, String parameters,
			PhaseQualifier phaseQualifier);

	/**
	 * @param requestId
	 * @return
	 */
	ICommunicationProtocol findInputDataByRequestId(String requestId);

	/**
	 * Seeks InputData for further Procesierung depending on the ExecutePhase
	 * 
	 * @param executionProcedure
	 * @param phaseQualifier
	 * @param inputDataLimit
	 *            limits the result set
	 * @return
	 */
	List<ICommunicationProtocol> findInputDataForExecution(
			String executionProcedure, PhaseQualifier phaseQualifier,
			Integer inputDataLimit);

	/**
	 * Seeks InputData for further Procesierung depending on the ExecutePhase
	 * 
	 * @param executionProcedure
	 * @param phaseQualifier
	 * @return
	 */
	List<ICommunicationProtocol> findInputDataForExecution(
			String executionProcedure, PhaseQualifier phaseQualifier);

	/**
	 * Seeks InputData for further Procesierung depending on the ExecutePhase
	 * 
	 * @param executionProcedure
	 * @param phaseQualifier
	 * @return
	 */
	Long countInputDataForExecution(String executionProcedure,
			PhaseQualifier phaseQualifier);

	// Max Query (Sterbedatenabgleich)
	/**
	 * @param procedureName
	 * @param phaseQualifier
	 * @param subquery
	 * @return
	 */
	String maxResponseIdForExecution(String procedureName,
			PhaseQualifier phaseQualifier, String subquery);

	// Max String Query (Sterbedatenabgleich)
	/**
	 * Ermittelt MAX ResponseID auf Basis eines Strings. Gilt nur für ein feste
	 * ResponseID Format. Wenn keine Daten gefunden wird, wird Null
	 * zurückgeliefert.
	 * 
	 * @param procedureName
	 * @param phaseQualifier
	 * @param subquery
	 * @return
	 */
	String maxSpecialStringResponseIdForExecution(String procedureName,
			PhaseQualifier phaseQualifier, String subquery);

	/**
	 * Sucht für einen OutputIdentifier das zugeordnete CommunicationProtocol.
	 * Diese Methode wird verwendet, wenn eine externe Anwendung eine
	 * OutputDatei (= OutputIdentifier) bestätigen möchte.
	 * 
	 * @param outputIdentifier
	 * @return
	 */
	boolean changeCommunicationProtocolStatusByOutputIdentifier(
			final String outputIdentifier, PersistentStatus persistentStatus);

}
