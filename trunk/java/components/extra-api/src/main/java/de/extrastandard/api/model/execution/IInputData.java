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

import de.extrastandard.api.exception.ExtraRuntimeException;

/**
 * Inputdaten.
 * 
 * @author Thorsten Vogel
 * @version $Id: IInputData.java 562 2012-09-06 14:12:43Z
 *          thorstenvogel@gmail.com $
 */
public interface IInputData extends PersistentEntity {

	/**
	 * Aktualisiert diese Inputdaten mit dem angegebenen Status.
	 * 
	 * @param newStatus
	 *            Der zu setzende Status.
	 */
	void updateProgress(IStatus newStatus);

	/**
	 * Markiert diese Instanz mit den angegebenen Fehlerdaten.
	 * 
	 * @param errorCode
	 *            Code des Fehlers.
	 * @param errorMessage
	 *            Fehlermeldung.
	 */
	void failed(String errorCode, String errorMessage);

	/**
	 * Markiert diese Instanz mit der angegebenen Exception.
	 * 
	 * @param exception
	 *            die den Fehler auslösende Exception.
	 */
	void failed(ExtraRuntimeException exception);

	/**
	 * Markiert diese Instanz als erfolgreich übertragen.
	 * 
	 * @param responseId
	 *            ID der Response.
	 * @param phaseQualifier
	 */
	void success(String responseId, PhaseQualifier phaseQualifier);

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
	 * @return
	 */
	IInputDataTransition getLastTransition();

}
