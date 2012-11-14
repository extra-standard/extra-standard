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
package de.extrastandard.api.model.content;

/**
 * Liefert Ergebnisse Übertragung eines Datensatzes einer Anfrage/Übertragung
 * zurück.
 * 
 * @author Leonid Potap
 * @version $Id: ISingleResponseData.java 756 2012-10-15 14:14:40Z
 *          potap.rentenservice@gmail.com $
 */
public interface ISingleResponseData {

	/**
	 * requestId der Ursprung Anfrage
	 */
	String getRequestId();

	/**
	 * responseId der Antwort
	 */
	String getResponseId();

	/**
	 * @return Liefert Return-Code der Nachricht.
	 */
	String getReturnCode();

	/**
	 * Liefert Return-Text der Nachricht, wenn vorhanden.
	 * 
	 * @return
	 */
	String getReturnText();

	/**
	 * Zeigt an, ob die Server-Verarbeitung erfolgreich war.
	 * 
	 * @return
	 */
	Boolean isSuccessful();
}
