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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.request.TransportRequestType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.response.TransportResponseType;
import de.drv.dsrv.spoc.web.manager.SpocNutzdatenManagerException;
import de.drv.dsrv.spoc.web.webservice.jax.ExtraError;

/**
 * Stellt Funktionen zum Weiterleiten des eXTra-Requests an das Fachverfahren
 * zur Verf&uuml;gung.
 */
public interface FachverfahrenRequestService {

	/**
	 * Leitet den Payload des eXTra-Requests an das Fachverfahren weiter.
	 * 
	 * @param fachverfahrenUri
	 *            die URI des Fachverfahrens
	 * @param payloadInputStream
	 *            der Payload des eXTra-Requests
	 * @return der Payload der Antwort des Fachverfahrens als XML
	 *         <code>Source</code>
	 * @throws IOException
	 *             wenn beim Request an das Fachverfahren ein Fehler auftritt
	 */
	Source executeFachverfahrenRequest(URI fachverfahrenUri, InputStream payloadInputStream) throws IOException;

	/**
	 * Leitet den Payload des eXTra-Requests an das Fachverfahren weiter.
	 * 
	 * @param fachverfahrenUri
	 *            die URI des Fachverfahrens
	 * @param transportRequestType
	 *            eXTra-Request
	 * @return eXTra-Response
	 * @throws IOException
	 *             wenn beim Request an das Fachverfahren ein Fehler auftritt
	 * @throws ExtraError
	 *             falls vom Fachverfahren ein
	 *             {@link de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorType}
	 *             -Objekt zurueckgegeben wird
	 * @throws JAXBException
	 *             falls beim Marshalling oder Unmarshalling ein Fehler auftritt
	 * @throws SpocNutzdatenManagerException
	 *             falls beim Verarbeiten der Nutzdaten ein Fehler auftritt
	 */
	TransportResponseType executeFachverfahrenRequest(URI fachverfahrenUri, TransportRequestType transportRequestType)
			throws IOException, ExtraError, JAXBException, SpocNutzdatenManagerException;
}
