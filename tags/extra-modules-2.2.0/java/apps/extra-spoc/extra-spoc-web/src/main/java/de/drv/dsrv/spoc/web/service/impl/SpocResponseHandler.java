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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import de.drv.dsrv.spoc.web.service.FachverfahrenResponseException;

/**
 * <code>ResponseHandler</code> f&uuml;r die Antworten der Fachverfahren.
 * <p>
 * Liefert eine <code>Source</code> zur&uuml;ck, da evtl. in der Response ein
 * Charset definiert wurde, mit dem die <code>Source</code> initialisiert werden
 * muss (au&szlig;erhalb des <code>ResponseHandler</code>s kann nicht mehr auf
 * den <code>Content-Type</code> Header, der das Charset definiert, zugegriffen
 * werden).
 */
class SpocResponseHandler implements ResponseHandler<StreamSource> {

	private static final Log LOG = LogFactory.getLog(SpocResponseHandler.class);

	@Override
	public StreamSource handleResponse(final HttpResponse httpResponse) throws IOException {

		final HttpEntity httpResponseEntity = getAndCheckHttpEntity(httpResponse);

		// Kopiert den InputStream der Response, damit die Connection
		// geschlossen werden kann.
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		IOUtils.copy(httpResponseEntity.getContent(), outputStream);
		EntityUtils.consume(httpResponseEntity);
		final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

		// Initialisiert die zurueck gegebene Source.
		StreamSource responseSource;
		final Charset charset = getCharsetFromResponse(httpResponseEntity);
		if (charset != null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("F\u00fcr die Antwort des Fachverfahrens wird das Charset >" + charset.name()
						+ "< aus dem Content-Type Header verwendet.");
			}
			responseSource = new StreamSource(new InputStreamReader(inputStream, charset));
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Die Antwort des Fachverfahren hat kein Charset im Content-Type Header spezifiziert."
						+ " Der Payload wird dem XML-Parser ohne Charset \u00fcbergeben,"
						+ " dieses sollte aber im XML-Prolog spezifiziert sein.");
			}
			responseSource = new StreamSource(inputStream);
		}

		return responseSource;
	}

	/**
	 * Bestimmt {@link HttpEntity} aus uebergebener {@link HttpResponse} und
	 * prueft den Status-Code. Bei einem Wert ueber 300 wird eine Ausnahme
	 * erzeugt.
	 * 
	 * @param httpResponse
	 *            zu verarbeitende HTTP-Antwort
	 * @return {@link HttpEntity} aus Response-Objekt
	 * @throws IOException
	 *             falls beim Lesen der Antwort ein Fehler auftritt
	 */
	public HttpEntity getAndCheckHttpEntity(final HttpResponse httpResponse) throws IOException {

		final HttpEntity httpResponseEntity = httpResponse.getEntity();

		// Alle HTTP Status Codes ueber 300 werden als Fehler betrachtet.
		final StatusLine statusLine = httpResponse.getStatusLine();
		if ((statusLine != null) && (statusLine.getStatusCode() >= HttpStatus.SC_MULTIPLE_CHOICES)) {
			if (LOG.isInfoEnabled()) {
				LOG.info("Antwort des Fachverfahrens mit HTTP Status Code >" + statusLine.getStatusCode()
						+ "< und Payload: " + IOUtils.toString(httpResponseEntity.getContent()));
			}
			EntityUtils.consume(httpResponseEntity);
			throw new FachverfahrenResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}

		return httpResponseEntity;
	}

	/**
	 * Versucht, das Charset des Payloads aus dem HTTP-Header
	 * <code>Content-Type</code> zu bestimmen.
	 * 
	 * @param httpResponseEntity
	 *            die Response des Fachverfahrens
	 * 
	 * @return das in der Response verwendete Charset, oder <code>null</code>,
	 *         wenn kein Charset bestimmt werden konnte
	 */
	private Charset getCharsetFromResponse(final HttpEntity httpResponseEntity) {
		ContentType contentType;
		try {
			contentType = ContentType.get(httpResponseEntity);
		} catch (final RuntimeException exc) {
			LOG.warn("Fehler beim Auslesen des Content-Types aus der Response.", exc);
			return null;
		}

		if (contentType == null) {
			if (LOG.isInfoEnabled()) {
				LOG.info("Es konnte kein Content-Type und somit auch kein Charset aus der Response gelesen werden.");
			}
			return null;
		}

		return contentType.getCharset();
	}
}