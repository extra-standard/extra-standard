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
package de.drv.dsrv.spoc.web.webservice.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.xml.sax.SAXParseException;

import de.drv.dsrv.spoc.web.webservice.InvalidExtraRequestException;

/**
 * Erweitert den von Spring-WS vorgegebenen
 * <code>PayloadValidatingInterceptor</code>, damit der zur&uuml;ck gegebene
 * Fehler im eXTra Format ist.
 */
public class SpocPayloadValidatingInterceptor extends PayloadValidatingInterceptor {

	private static final Log LOG = LogFactory.getLog(SpocPayloadValidatingInterceptor.class);

	public SpocPayloadValidatingInterceptor() {
		super();
	}

	@Override
	protected boolean handleRequestValidationErrors(
			final MessageContext messageContext, final SAXParseException[] errors) {

		final StringBuilder errorText = new StringBuilder();

        for (final SAXParseException error : errors) {
			errorText.append(error.getMessage()).append("   ");
			LOG.warn("Fehler bei der XML-Validierung: " + error.getMessage());
        }
		throw new InvalidExtraRequestException(errorText.toString());
	}
}
