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
 * Definiert Methoden zum Lesen und Weitergeben der Betriebsnummer.
 */
public interface SpocBetriebsnummerService {

	/**
	 * Extrahiert die Betriebsnummer aus dem aktuellen Request und f&uuml;gt sie
	 * der &uuml;bergebenen <code>uri</code> als Parameter hinzu.
	 * 
	 * @param fachverfahrenUrl
	 *            die URL, der die <code>betriebsnummer</code> hinzugef&uuml;gt
	 *            werden soll; darf nicht <code>null</code> sein
	 * 
	 * @return die aus dem Request extrahierte Betriebsnummer, oder
	 *         <code>null</code>, wenn keine Betriebsnummer extrahiert werden
	 *         konnte
	 */
	URI addBetriebsnummerFromRequestToUrl(final URI fachverfahrenUrl);
}
