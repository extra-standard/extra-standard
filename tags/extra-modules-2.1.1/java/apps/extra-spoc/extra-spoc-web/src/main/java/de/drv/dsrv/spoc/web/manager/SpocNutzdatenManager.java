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
package de.drv.dsrv.spoc.web.manager;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.request.TransportRequestType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.response.TransportResponseType;

/**
 * Definiert Methoden zum Speichern und Lesen der Nutzdaten, die als Anhang im
 * eXTra-Request oder eXTra-Response transportiert werden.
 */
public interface SpocNutzdatenManager {

	/**
	 * Liest die - falls vorhanden - Nutzdaten aus dem eXTra-Request und
	 * speichert diese in die SPoC-MTOM-Datenbank. Im Request werden die
	 * Nutzdaten vom Typ
	 * {@link de.drv.dsrv.spoc.extra.v1_3.jaxb.components.Base64CharSequenceType}
	 * durch die ID des Datensatzes in Form von
	 * {@link de.drv.dsrv.spoc.extra.v1_3.jaxb.components.AnyXMLType} ersetzt.
	 * 
	 * @param transportRequestType
	 *            eXTra-Request
	 * @throws SpocNutzdatenManagerException
	 *             falls bei der Verarbeitung ein Fehler auftritt
	 */
	void processRequestNutzdaten(final TransportRequestType transportRequestType) throws SpocNutzdatenManagerException;

	/**
	 * Falls in der eXTra-Response im Element
	 * {@link de.drv.dsrv.spoc.extra.v1_3.jaxb.components.AnyXMLType} eine
	 * MTOM-Data-ID vorhanden ist, werden die entsprechenden Nutzdaten aus der
	 * SPoC-MTOM-Datenbank gelesen und als
	 * {@link de.drv.dsrv.spoc.extra.v1_3.jaxb.components.Base64CharSequenceType}
	 * in die eXTra-Response eingefuegt. Dabei wird das Element
	 * {@link de.drv.dsrv.spoc.extra.v1_3.jaxb.components.AnyXMLType} entfernt.
	 * 
	 * @param transportResponseType
	 *            eXTra-Response
	 * @throws SpocNutzdatenManagerException
	 *             falls bei der Verarbeitung ein Fehler auftritt
	 */
	void processResponseNutzdaten(final TransportResponseType transportResponseType)
			throws SpocNutzdatenManagerException;

}
