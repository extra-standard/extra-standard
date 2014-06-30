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
package de.extra.client.plugins.outputplugin.ws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.xml.transform.StringResult;

import de.drv.dsrv.extra.marshaller.IExtraMarschaller;
import de.drv.dsrv.extra.marshaller.IExtraUnmarschaller;
import de.drv.dsrv.extrastandard.namespace.request.RequestTransport;
import de.drv.dsrv.extrastandard.namespace.response.ResponseTransport;
import de.extra.client.core.annotation.PluginConfigType;
import de.extra.client.core.annotation.PluginConfiguration;
import de.extra.client.core.annotation.PluginValue;
import de.extrastandard.api.exception.ExceptionCode;
import de.extrastandard.api.exception.ExtraCoreRuntimeException;
import de.extrastandard.api.exception.ExtraOutputPluginRuntimeException;
import de.extrastandard.api.plugin.IOutputPlugin;

/**
 * @author Thorsten Vogel
 * @version $Id: WsOutputPlugin.java 1449 2013-01-11 10:45:54Z
 *          potap.rentenservice@gmail.com $
 */
@Named("wsOutputPlugin")
@PluginConfiguration(pluginBeanName = "wsOutputPlugin", pluginType = PluginConfigType.OutputPlugins)
public class WsOutputPlugin implements IOutputPlugin {

	private static final Logger logger = LoggerFactory
			.getLogger(WsOutputPlugin.class);
	// TODO globaler Logger: auslagern
	private static final Logger operation_logger = LoggerFactory
			.getLogger("de.extra.client.operation");

	@Inject
	@Named("webServiceTemplate")
	private WebServiceTemplate webServiceTemplate;

	@PluginValue(key = "endpoint.url")
	private String endpointUrl;

	@Inject
	@Named("eXTraNoValidationjaxb2Marshaller")
	private Jaxb2Marshaller noValidationJaxb2Marshaller;

	@Inject
	@Named("extraUnmarschaller")
	private IExtraUnmarschaller extraUnmarschaller;

	@Inject
	@Named("extraMarschaller")
	private IExtraMarschaller marshaller;

	@Value("${core.outgoing.validation}")
	private boolean outgoingXmlValidation;

	/**
	 * @see de.extrastandard.api.plugin.IOutputPlugin#outputData(java.io.InputStream)
	 */
	@Override
	public ResponseTransport outputData(final RequestTransport requestTransport) {
		// 1. RequestTransport as Stream umwandeln. Prüfen, ob es auch einfacher
		// geht.
		try {

			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			final StreamResult streamResult = new StreamResult(outputStream);
			marshaller.marshal(requestTransport, streamResult,
					outgoingXmlValidation);
			final ByteArrayInputStream requestAsStream = new ByteArrayInputStream(
					outputStream.toByteArray());

			logger.debug("sending request");
			operation_logger.info("Webservice Aufruf von: {}", endpointUrl);

			final ByteArrayOutputStream temp = new ByteArrayOutputStream();
			final StreamSource source = new StreamSource(requestAsStream);
			final StreamResult result = new StreamResult(temp);
			webServiceTemplate.sendSourceAndReceiveToResult(endpointUrl,
					source, result);
			// ResponseTransport aus Stream erzeugen
			final ResponseTransport extraResponse = extraUnmarschaller
					.unmarshal(new ByteArrayInputStream(temp.toByteArray()),
							ResponseTransport.class);
			return extraResponse;
		} catch (final SoapFaultClientException soapFaultClientException) {
			// Faengt eine vom Server gemeldete SoapFaultClientException ab
			operation_logger.error("Server meldet SOAP-Fehler: {}",
					soapFaultClientException.getFaultStringOrReason());
			final ExtraOutputPluginRuntimeException extraOutputPluginRuntimeException = extractSoapFaultClientException(soapFaultClientException);
			throw (extraOutputPluginRuntimeException);
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

	private ExtraOutputPluginRuntimeException extractSoapFaultClientException(
			final SoapFaultClientException soapFaultClientException) {
		try {
			final StringBuilder message = new StringBuilder();
			final Result resultFault = new StringResult();
			TransformerFactory
					.newInstance()
					.newTransformer()
					.transform(
							soapFaultClientException.getSoapFault().getSource(),
							resultFault);
			final String resultFaultAsString = resultFault.toString();
			message.append(resultFaultAsString);
			// operation_logger.error("SoapFault Detail:\n {}",
			// resultFaultAsString);
			return new ExtraOutputPluginRuntimeException(
					ExceptionCode.EXTRA_TRANSFER_EXCEPTION, message.toString());
		} catch (final Exception e) {
			operation_logger
					.error("Schwerwiegender Fehler beim Extrahieren des SOAP-Fehlers!",
							e.getMessage());
			throw new ExtraCoreRuntimeException(
					ExceptionCode.UNEXPECTED_INTERNAL_EXCEPTION,
					"Schwerwiegender Fehler beim Extrahieren des SOAP-Fehlers!"
							+ e.getMessage());
		}
	}

	/**
	 * @param endpointUrl
	 *            the endpointUrl to set
	 */
	public void setEndpointUrl(final String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}
}
