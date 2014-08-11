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
package de.drv.dsrv.spoc.commons.zertifikat;

import javax.servlet.http.HttpServletRequest;

/**
 * Definiert die Methoden fuer die Pruefung der Betriebsnummer.
 */
public interface BetriebsnummerHelper {

	/**
	 * Ermittelt die Betriebsnummer, welche im SPoC ermittelt und ueber einen
	 * Request Parameter ans Fachverfahren weitergeleitet wird aus dem
	 * uebergebenen HTTP-Servlet-Request.
	 * 
	 * @param request
	 *            das HTTP-Servlet-Request zum Zugriff auf die Request Parameter
	 * @return die aus dem HTTP-Servlet-Request aus einem Request Parameter
	 *         ermittelte Betriebsnummer, sofern diese vorhanden ist vorhanden;
	 *         andernfalls <code>null</code>
	 */
	String ermittleBetriebsnummer(final HttpServletRequest request);
}
