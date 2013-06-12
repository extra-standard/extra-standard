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
package de.extra.client.plugins.outputplugin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.XmlMappingException;

import de.drv.dsrv.extra.marshaller.IExtraMarschaller;
import de.drv.dsrv.extra.marshaller.IExtraUnmarschaller;
import de.drv.dsrv.extrastandard.namespace.request.RequestTransport;
import de.drv.dsrv.extrastandard.namespace.response.ResponseTransport;
import de.extrastandard.api.exception.ExtraOutputPluginRuntimeException;
import de.extrastandard.api.plugin.IOutputPlugin;

@Named("httpOutputPlugin")
public class HttpOutputPlugin implements IOutputPlugin {

	private static final Logger LOG = LoggerFactory
			.getLogger(HttpOutputPlugin.class);

	@Inject
	@Named("httpOutputPluginSender")
	private HttpOutputPluginSender httpSender;

	@Inject
	@Named("extraUnmarschaller")
	private IExtraUnmarschaller extraUnmarschaller;

	@Inject
	@Named("extraMarschaller")
	private IExtraMarschaller marshaller;

	@Value("${core.outgoing.validation}")
	private boolean outgoingXmlValidation;

	@Override
	public ResponseTransport outputData(final RequestTransport requestTransport) {
		try {
			LOG.info("Start des Versands...");
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			final StreamResult streamResult = new StreamResult(outputStream);
			marshaller.marshal(requestTransport, streamResult,
					outgoingXmlValidation);
			final ByteArrayInputStream requestAsStream = new ByteArrayInputStream(
					outputStream.toByteArray());
			final InputStream responseStreams = httpSender
					.processOutput(requestAsStream);
			// ResponseTransport aus Stream erzeugen
			final ResponseTransport transportResponse = extraUnmarschaller
					.unmarshal(responseStreams, ResponseTransport.class);

			return transportResponse;
		} catch (final XmlMappingException xmlMappingException) {
			final ExtraOutputPluginRuntimeException extraOutputPluginRuntimeException = new ExtraOutputPluginRuntimeException(
					xmlMappingException);
			throw (extraOutputPluginRuntimeException);
		} catch (final IOException ioException) {
			final ExtraOutputPluginRuntimeException extraOutputPluginRuntimeException = new ExtraOutputPluginRuntimeException(
					ioException);
			throw (extraOutputPluginRuntimeException);
		}
	}
}
