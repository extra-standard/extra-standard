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
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.WebServiceTemplate;

import de.extra.client.core.annotation.PluginConfigType;
import de.extra.client.core.annotation.PluginConfiguration;
import de.extra.client.core.annotation.PluginValue;
import de.extrastandard.api.plugin.IOutputPlugin;

/**
 * @author Thorsten Vogel
 * @version $Id$
 */
@Named("wsOutputPlugin")
@PluginConfiguration(pluginBeanName = "wsOutputPlugin", pluginType = PluginConfigType.OutputPlugins)
public class WsOutputPlugin implements IOutputPlugin {

	private static final Logger logger = LoggerFactory
			.getLogger(WsOutputPlugin.class);
	private static final Logger operation_logger = LoggerFactory
			.getLogger("de.extra.client.operation");

	@Inject
	@Named("webServiceTemplate")
	private WebServiceTemplate webServiceTemplate;

	@PluginValue(key = "endpoint.url")
	private String endpointUrl;

	/**
	 * @see de.extrastandard.api.plugin.IOutputPlugin#outputData(java.io.InputStream)
	 */
	@Override
	public InputStream outputData(final InputStream requestAsStream) {
		final ByteArrayOutputStream temp = new ByteArrayOutputStream();

		logger.debug("sending request");
		operation_logger.info("Webservice Aufruf von: {}", endpointUrl);
		final StreamSource source = new StreamSource(requestAsStream);
		final StreamResult result = new StreamResult(temp);
		webServiceTemplate.sendSourceAndReceiveToResult(endpointUrl, source,
				result);
		return new ByteArrayInputStream(temp.toByteArray());
	}

	/**
	 * @param endpointUrl
	 *            the endpointUrl to set
	 */
	public void setEndpointUrl(final String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}
}
