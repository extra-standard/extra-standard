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
package de.drv.dsrv.spoc.web.webservice.jax;

import java.net.URI;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.BindingType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.RequestDetailsType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.request.TransportRequestType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.response.TransportResponseType;
import de.drv.dsrv.spoc.web.service.FachverfahrenRequestService;
import de.drv.dsrv.spoc.web.service.SpocBetriebsnummerService;
import de.drv.dsrv.spoc.web.service.SpocRoutingService;
import de.drv.dsrv.spoc.web.webservice.UnidentifiedFachverfahrenException;

/**
 * SPoC-Endpoint, der die Nutzdaten des Requests in einer Datenbank speichert
 * und die dazugehoerige ID an das Fachverfahren weiter gibt. Falls die Response
 * eine solche ID enthaelt, werden die dazugehorigen Nutzdaten aus der Datenbank
 * gelesen und an die Response gehaengt.
 */
@Service
@WebService(name = "Extra", targetNamespace = "http://www.extra-standard.de/namespace/webservice", serviceName = "ExtraService", portName = "extraSOAP")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_MTOM_BINDING)
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({ de.drv.dsrv.spoc.extra.v1_3.jaxb.components.ObjectFactory.class,
		de.drv.dsrv.spoc.extra.v1_3.jaxb.logging.ObjectFactory.class,
		de.drv.dsrv.spoc.extra.v1_3.jaxb.plugins.ObjectFactory.class,
		de.drv.dsrv.spoc.extra.v1_3.jaxb.request.ObjectFactory.class,
		de.drv.dsrv.spoc.extra.v1_3.jaxb.response.ObjectFactory.class,
		de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ObjectFactory.class, org.w3._2000._09.xmldsig.ObjectFactory.class,
		org.w3._2001._04.xmlenc.ObjectFactory.class })
public class SpocMtomEndpoint extends SpringBeanAutowiringSupport {

	private static final Log LOG = LogFactory.getLog(SpocMtomEndpoint.class);

	@Autowired
	private SpocRoutingService spocRoutingService;

	@Autowired
	private FachverfahrenRequestService fachverfahrenRequestService;

	@Autowired
	private SpocBetriebsnummerService betriebsnummerService;

	@Autowired
	private JaxWsExtraExceptionHelper extraExceptionHelper;

	/**
	 * Standard-Konstruktor.
	 */
	public SpocMtomEndpoint() {
		super();
	}

	/**
	 * Bearbeitet einen Request, leitet die Anfrage an das Fachverfahren weiter
	 * und gibt die Response an den Aufrufer zurueck. Dabei werden evtl.
	 * vorhandene Nutzdaten in der Datenbank gespeichert und die ID an das
	 * Fachverfahren weiter gegeben. Falls die Response eine solche ID enthaelt,
	 * werden die dazugehorigen Nutzdaten aus der Datenbank gelesen und an die
	 * Response gehaengt.
	 * 
	 * @param transportRequest
	 *            zu verarbeitende Anfrage
	 * @return Response des Fachverfahrens
	 * @throws ExtraError
	 *             falls ein Fehler auftritt oder vom Fachverfahren ein
	 *             ExtraError zurueck gegeben wird
	 */
	@WebMethod(action = "http://www.extra-standard.de/namespace/webservice/execute")
	@WebResult(name = "Transport", targetNamespace = "http://www.extra-standard.de/namespace/response/1", partName = "TransportResponse")
	public TransportResponseType execute(
			@WebParam(name = "Transport", targetNamespace = "http://www.extra-standard.de/namespace/request/1", partName = "TransportRequest") final TransportRequestType transportRequest)
			throws ExtraError {

		try {
			// Ermittle Fachverfahren-URL
			URI fachverfahrenUrl = getFachverfahrenUrl(transportRequest);

			// Versucht, die Betriebsnummer aus dem Request zu lesen und sie als
			// Parameter an den weiter geleiteten Request anzuhaengen.
			fachverfahrenUrl = this.betriebsnummerService.addBetriebsnummerFromRequestToUrl(fachverfahrenUrl);

			// Leitet den Request an das Fachverfahren weiter.
			if (LOG.isInfoEnabled()) {
				LOG.info("Weiterleiten des Requests an Fachverfahren >" + fachverfahrenUrl + "<.");
			}

			final TransportResponseType transportResponseType = this.fachverfahrenRequestService
					.executeFachverfahrenRequest(fachverfahrenUrl, transportRequest);

			if (LOG.isInfoEnabled()) {
				LOG.info("Antwort erhalten von Fachverfahren >" + fachverfahrenUrl + "<.");
			}
			return transportResponseType;

		} catch (final Exception e) {
			throw this.extraExceptionHelper.handleExceptionJaxWs(e);
		}
	}

	private URI getFachverfahrenUrl(final TransportRequestType transportRequest)
			throws UnidentifiedFachverfahrenException {

		String extraVersion = null;
		String extraProfile = null;
		String extraProcedure = null;
		String extraDataType = null;

		if (transportRequest != null) {

			// Profile
			extraProfile = transportRequest.getProfile();

			if (transportRequest.getVersion() != null) {

				// Version
				extraVersion = transportRequest.getVersion().value();
			}

			if ((transportRequest.getTransportHeader() != null)
					&& (transportRequest.getTransportHeader().getRequestDetails() != null)) {

				final RequestDetailsType requestDetails = transportRequest.getTransportHeader().getRequestDetails();

				// Procedure
				extraProcedure = requestDetails.getProcedure();

				// DataType
				extraDataType = requestDetails.getDataType();
			}
		}

		final URI fachverfahrenUrl = this.spocRoutingService.getFachverfahrenUrl(extraVersion, extraProfile,
				extraProcedure, extraDataType);

		if (fachverfahrenUrl == null) {
			throw new UnidentifiedFachverfahrenException(extraProfile, extraVersion, extraProcedure, extraDataType);
		} else {
			return fachverfahrenUrl;
		}
	}

}
