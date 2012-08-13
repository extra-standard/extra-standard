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

import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import de.extra.client.plugins.outputplugin.config.HttpOutputPluginConnectConfiguration;
import de.extra.client.plugins.outputplugin.transport.ExtraTransportFactory;
import de.extra.client.plugins.outputplugin.transport.IExtraTransport;

@Named("httpOutputPluginSender")
public class HttpOutputPluginSender {

	private static final Logger logger = Logger
			.getLogger(HttpOutputPluginSender.class);

	private IExtraTransport client;

	@Inject
	ExtraTransportFactory extraTransportFactory;

	@Inject
	@Named("httpOutputPluginConnectConfiguration")
	private HttpOutputPluginConnectConfiguration extraConnectData;

	/**
	 * Verarbeiten des Requests.
	 * 
	 * @param request
	 *            eXTra-Request aus der CoreLib
	 * @return
	 */
	public InputStream processOutput(String request) {

		// Initialisiere Transport-Client
		client = extraTransportFactory.loadTransportImpl();
		client.initTransport(extraConnectData);

		InputStream response = client.senden(request);

		return response;
	}
}
