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

import de.extrastandard.api.model.content.ISingleResponseData;

/**
 * Die Kommunikation zwischen Client und Server wird im CommunicationProtocol festgehalten.
 * 
 * 
 * @author Thorsten Vogel
 * @version $Id: ICommunicationProtocol.java 562 2012-09-06 14:12:43Z
 *          thorstenvogel@gmail.com $
 */
public interface ICommunicationProtocol extends PersistentEntity {

	/**
	 * Markiert CommunicationProtocol als Übertragen. Die ResponseDaten werden in den
	 * CommunicationProtocol festgehalten
	 * 
	 * @param singleResponseData
	 */
	void transmitted(ISingleResponseData singleResponseData);

	/**
	 * @return requestId, unique identification of this message
	 */
	String getRequestId();

	/**
	 * Identifikation dieser Daten.
	 * 
	 * @return Identifier
	 */
	String getInputIdentifier();

	/**
	 * Hashcode dieser Daten. Sollte kein HashCode verwendet werden können, so
	 * ist der Rückgabewert <code>null</code>.
	 * 
	 * @return Hashcode oder <code>null</code>.
	 */
	String getHashcode();

	/**
	 * Response ID oder <code>null</code>.
	 * 
	 * @return Response ID oder <code>null</code>.
	 */
	String getResponseId();

	/**
	 * Liefert die zugehörige Execution.
	 * 
	 * @return Execution
	 */
	IExecution getExecution();

	/**
	 * @return berechnete RequestId
	 */
	String calculateRequestId();

	/**
	 * Setzt RequestId
	 * 
	 * @param requestId
	 */
	void setRequestId(String requestId);

	/**
	 * @return
	 */
	String getReturnText();

	/**
	 * @return
	 */
	String getReturnCode();

	/**
	 * @return nextPhase {@link IPhaseConnection)
	 */
	IPhaseConnection getNextPhaseConnection();

	/**
	 * @return currentPhase {@link IPhaseConnection)
	 */
	IPhaseConnection getCurrentPhaseConnection();

}
