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
package de.drv.dsrv.spoc.web.service.impl;

import java.net.URI;
import java.security.cert.X509Certificate;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;

import de.drv.dsrv.spoc.util.zertifikat.ZertifikatHelper;
import de.drv.dsrv.spoc.web.service.SpocBetriebsnummerService;

/**
 * Implementiert Methoden zum Lesen und Weitergeben der Betriebsnummer.
 */
public class SpocBetriebsnummerServiceImpl implements SpocBetriebsnummerService {

	private static final Log LOG = LogFactory.getLog(SpocBetriebsnummerServiceImpl.class);

	private static final String PARAM_NAME_BBNR = "bbnrZertifikat";

	private final ZertifikatHelper zertifikatHelper;

	/**
	 * Konstruktor.
	 * 
	 * @param zertifikatHelper
	 *            Hilfsklasse zum Zugriff auf die Zertifikate
	 */
	public SpocBetriebsnummerServiceImpl(final ZertifikatHelper zertifikatHelper) {
		this.zertifikatHelper = zertifikatHelper;
	}

	@Override
	public URI addBetriebsnummerFromRequestToUrl(final URI fachverfahrenUrl) {

		URI uriWithBetriebsnummer = fachverfahrenUrl;

		// Bestimme Request-Objekt
		HttpServletRequest request = null;
		final TransportContext ctx = TransportContextHolder.getTransportContext();
		if (ctx != null) {
			if (ctx.getConnection() instanceof HttpServletConnection) {
				request = ((HttpServletConnection) ctx.getConnection()).getHttpServletRequest();
			}
		}

		// Ermittle Betriebsnummer
		String betriebsnummer = null;
		if (request != null) {
			// Ermittle Zertifikate aus dem HTTP-Request
			final X509Certificate[] zertifikate = (X509Certificate[]) request
					.getAttribute("javax.net.ssl.peer_certificates");
			betriebsnummer = this.zertifikatHelper.ermittleBetriebsnummer(zertifikate);
		} else {
			LOG.warn("HttpServletRequest konnte nicht ueber den TransportContext bezogen werden.");
		}

		if (betriebsnummer != null) {
			try {
				uriWithBetriebsnummer = URI.create(fachverfahrenUrl.toString() + "?" + PARAM_NAME_BBNR + "="
						+ betriebsnummer);
			} catch (final IllegalArgumentException exc) {
				LOG.error("An die URI >" + fachverfahrenUrl + "< konnte kein Parameter angeh\u00e4ngt werden,"
						+ "der Request wird ohne Betriebsnummer an das Fachverfahren weiter geleitet.", exc);
			}

		} else {
			LOG.warn("Betriebsnummer konnte nicht aus dem Request f\u00fcr Fachverfahren >" + fachverfahrenUrl
					+ "< gelesen werden.");
		}
		return uriWithBetriebsnummer;
	}
}
