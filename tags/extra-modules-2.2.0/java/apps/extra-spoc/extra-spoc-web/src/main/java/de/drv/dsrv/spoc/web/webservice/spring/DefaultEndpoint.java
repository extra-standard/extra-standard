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

import java.util.Locale;

import javax.xml.transform.Source;

import org.springframework.context.MessageSource;
import org.springframework.ws.server.endpoint.PayloadEndpoint;
import org.springframework.ws.server.endpoint.annotation.Endpoint;

import de.drv.dsrv.spoc.web.util.Messages;
import de.drv.dsrv.spoc.web.webservice.InvalidExtraRequestException;

/**
 * Web Service Endpoint fuer alle Nicht-eXTra-Requests. Es wird eine
 * {@link InvalidExtraRequestException} erzeugt, die dann im
 * {@link SpocEndpointExceptionResolver} behandelt wird.
 * <p>
 * Dieser Endpoint wird auch dann aufgerufen, wenn der Request nicht gegen das
 * eXTra-Schema validiert, da die Auswahl des Endpoints vor dem
 * Validierungs-Interceptor greift.
 */
@Endpoint
public class DefaultEndpoint implements PayloadEndpoint {

	private final MessageSource messages;

	public DefaultEndpoint(final MessageSource messages) {
		this.messages = messages;
	}

	@Override
	public Source invoke(final Source source) {

		throw new InvalidExtraRequestException(this.messages.getMessage(Messages.ERROR_REQUEST_NO_EXTRA, null,
				Locale.GERMAN));
	}
}
