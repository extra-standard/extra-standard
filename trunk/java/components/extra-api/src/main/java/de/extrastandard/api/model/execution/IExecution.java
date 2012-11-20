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

import java.util.Date;
import java.util.Set;

import de.extrastandard.api.exception.ExtraRuntimeException;
import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.model.content.ISingleInputData;

/**
 * Ein Execution bietet Möglichkeiten zur Protokollierung der zu verarbeitenden
 * Eingangsdaten.
 * 
 * @author Thorsten Vogel
 * @version $Id: IExecution.java 487 2012-09-03 13:07:52Z
 *          thorstenvogel@gmail.com $
 */
public interface IExecution extends PersistentEntity {

	/**
	 * * Persists a new @ link} IInputData instance for a
	 * {@link ISingleInputData}.
	 * 
	 * @param singleInputData
	 * @return
	 */
	ICommunicationProtocol startInputData(final ISingleInputData singleInputData);

	/**
	 * Updates this execution with the specified status.
	 * 
	 * @param newPersistentStatus
	 */
	void updateProgress(PersistentStatus newPersistentStatus);

	/**
	 * Ends this execution.
	 * 
	 * @param responseData
	 */
	void endExecution(IResponseData responseData);

	/**
	 * Marks this instance with the specified error data. This method do not
	 * throw any exception.
	 * 
	 * @param errorCode
	 *            Code des Fehlers.
	 * @param errorMessage
	 *            Fehlermeldung.
	 */
	void failed(String errorCode, String errorMessage);

	/**
	 * Marks this instance with the specified error data. This method do not
	 * throw any exception.
	 * 
	 * @param exception
	 *            die den Fehler auslösende Exception.
	 */
	void failed(ExtraRuntimeException exception);

	/**
	 * Parameter für diese Execution.
	 * 
	 * @return Parameter dieser Execution
	 */
	String getParameters();

	/**
	 * Zeitpunkt, an dem diese Execution gestartet wurde.
	 * 
	 * @return Startzeitpunkt.
	 */
	Date getStartTime();

	/**
	 * Name des Verfahrens.
	 * 
	 * @return Verfahren
	 */
	IProcedure getProcedure();

	/**
	 * Zeitpunkt, an dem diese Execution beendet wurde.
	 * 
	 * @return Endzeitpunkt, oder <code>null</code> falls noch nicht beendet.
	 */
	Date getEndTime();

	/**
	 * Liefert <code>true</code>, falls ein Fehler vorliegt.
	 * 
	 * @return <code>true</code> bei Fehler, ansonsten <code>false</code>.
	 */
	boolean hasError();

	/**
	 * Falls vorhanden, ein Fehlercode.
	 * 
	 * @return Fehlercode oder <code>null</code>.
	 */
	String getErrorCode();

	/**
	 * Falls vorhanden, eine Fehlermeldung.
	 * 
	 * @return Fehlermeldung oder <code>null</code>.
	 */
	String getErrorMessage();

	/**
	 * @return lastTransition
	 */
	IProcessTransition getLastTransition();

	/**
	 * @return phase of the Execution
	 */
	String getPhase();

	/**
	 * @return InputDaten der Execution
	 */
	Set<ICommunicationProtocol> getInputDataSet();

}
