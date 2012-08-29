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
package de.extrastandard.persistence.api;

import de.extrastandard.api.exception.ExtraRuntimeException;

/**
 * Inputdaten.
 *
 * @author DPRS
 * @version $Id$
 */
public interface IInputData {

	/**
	 * Aktualisiert diese Inputdaten mit dem angegebenen Status.
	 *
	 * @param status Der zu setzende Status.
	 */
	void updateProgress(IStatus status);

	/**
	 * Markiert diese Instanz mit den angegebenen Fehlerdaten.
	 *
	 * @param errorCode Code des Fehlers.
	 * @param errorMessage Fehlermeldung.
	 */
	void failed(String errorCode, String errorMessage);

	/**
	 * Markiert diese Instanz mit den angegebenen Fehlerdaten.
	 *
	 * @param exception die den Fehler auslösende Exception.
	 */
	void failed(ExtraRuntimeException exception);

	/**
	 * Markiert diese Instanz als erfolgreich übertragen.
	 *
	 * @param responseId ID der Response.
	 */
	void success(String responseId);

}
