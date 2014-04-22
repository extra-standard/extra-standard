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
package de.drv.dsrv.spoc.web.service;

import java.net.URI;

/**
 * Stellt Funktionen zum Routing eines eingehenden Requests auf das
 * entsprechende Fachverfahren zur Verf&uuml;gung.
 */
public interface SpocRoutingService {

	/**
	 * Ermittelt das Ziel-Fachverfahren f&uuml;r einen eXTra-Request durch die
	 * Auswertung bestimmter Parameter des Requests.
	 *
	 * @param version
	 *            der Wert des <code>version</code> Attributs des
	 *            <code>Transport</code> Elements des eXTra Requests
	 * @param profile
	 *            der Wert des <code>profile</code> Attributs des
	 *            <code>Transport</code> Elements des eXTra Requests
	 * @param procedure
	 *            der Wert des <code>Procedure</code> Elements aus dem
	 *            <code>TransportHeader</code> des eXTra Requests
	 * @param dataType
	 *            der Wert des <code>DataType</code> Elements aus dem
	 *            <code>TransportHeader</code> des eXTra Requests
	 *
	 * @return die <code>URI</code> des Fachverfahrens, das f&uuml;r die
	 *         &uuml;bergebenen Parameter konfiguriert ist
	 */
	URI getFachverfahrenUrl(String version, String profile, String procedure, String dataType);
}
