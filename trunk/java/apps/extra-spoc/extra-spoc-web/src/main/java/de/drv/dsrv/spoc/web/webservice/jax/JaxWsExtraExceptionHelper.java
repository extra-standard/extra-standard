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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorType;
import de.drv.dsrv.spoc.web.webservice.ExtraExceptionHelper;

/**
 * Erweitert den {@link ExtraExceptionHelper} um die Behandlung von Ausnahmen im
 * JAX-WS-Umfeld.
 */
public class JaxWsExtraExceptionHelper extends ExtraExceptionHelper {

	private static final Log LOG = LogFactory.getLog(JaxWsExtraExceptionHelper.class);

	private final String soapFaultString;

	/**
	 * Konstruktor.
	 * 
	 * @param messages
	 *            Quelle mit den Fehlertexten
	 * @param soapFaultString
	 *            SOAP-Fehler
	 * @param extraErrorCode
	 *            eXTRa-Fehlercode
	 */
	public JaxWsExtraExceptionHelper(final MessageSource messages, final String soapFaultString,
			final String extraErrorCode) {
		super(messages, extraErrorCode);
		this.soapFaultString = soapFaultString;
	}

	/**
	 * Behandelt verschiedene Ausnahmen und erstellt dazu passende
	 * ExtraError-Objekte.
	 * 
	 * @param exception
	 *            zu behandelnde Ausnahme
	 * @return ExtraError erstellter ExtraError
	 */
	public ExtraError handleExceptionJaxWs(final Exception exception) {

		ExtraError extraError = null;

		LOG.error("Behandlung von Exception: ", exception);

		if (exception instanceof ExtraError) {
			extraError = (ExtraError) exception;
		} else {

			try {
				final ExtraErrorType extraErrorType = generateExtraErrorFromException(exception);
				extraError = new ExtraError(this.soapFaultString, extraErrorType);
			} catch (final Exception e) {
				LOG.error("Exception beim Marshalling von ExtraError.", e);
			}
		}
		return extraError;
	}

}
