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

/**
 * Inputdaten.
 * 
 * @author Leonid Potap
 * @version $Id: IPhaseConnection.java 754 2012-10-15 14:06:06Z
 *          potap.rentenservice@gmail.com $
 */
public interface IPhaseConnection {

	/**
	 * @return the sourceCommunicationProtocol
	 */
	ICommunicationProtocol getSourceCommunicationProtocol();

	/**
	 * @return the targetCommunicationProtocol
	 */
	ICommunicationProtocol getTargetCommunicationProtocol();

	/**
	 * @return the nextPhasequalifier
	 */
	String getNextPhasequalifier();

	/**
	 * @return the status
	 */
	IStatus getStatus();

	/**
	 * @param targetInputData
	 */
	void setTargetInputData(ICommunicationProtocol targetInputData);

	/**
	 * Markiert PhaseConnction as fehlgeschlagen. Status is Fail
	 */
	public void setFailed();

}