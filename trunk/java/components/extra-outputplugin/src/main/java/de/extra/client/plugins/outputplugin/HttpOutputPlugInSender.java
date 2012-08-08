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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;

import de.drv.dsrv.extrastandard.namespace.components.FlagCodeType;
import de.drv.dsrv.extrastandard.namespace.components.FlagType;
import de.drv.dsrv.extrastandard.namespace.response.XMLTransport;
import de.extra.client.plugins.outputplugin.config.HttpOutputPluginConnectConfiguration;
import de.extra.client.plugins.outputplugin.config.HttpOutputPluginSenderDataConfiguration;
import de.extra.client.plugins.outputplugin.transport.ExtraTransportException;
import de.extra.client.plugins.outputplugin.transport.ExtraTransportFactory;
import de.extra.client.plugins.outputplugin.transport.IExtraTransport;
import de.extra.client.plugins.outputplugin.utils.IResponseSaver;

@Named("httpOutputPlugInSender")
public class HttpOutputPlugInSender {

	private static Logger logger = Logger.getLogger(HttpOutputPlugInSender.class);

	private IExtraTransport client;

	XMLTransport extraResponse = null;
	
	@Inject
	ExtraTransportFactory extraTransportFactory;
	
	@Inject
	@Named("fileSystemHelper")
	private IResponseSaver fileSystemHelper;
	

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
	public boolean processOutput(String request) {

		boolean returnCode = true;

		try {

			try {
				// Initialisiere Transport-Client
				client = extraTransportFactory.loadTransportImpl();
				client.initTransport(extraConnectData);
			} catch (ExtraTransportException e) {
				logger.error("Fehler beim initialisieren des Transports", e);
			}

			JAXBElement<?> element = client.senden(request);
			extraResponse = (XMLTransport) element.getValue();
		} catch (ExtraTransportException e) {
			logger.error("Fehler beim Versand der eXTra-Nachricht", e);
			returnCode = false;
		}

		if (returnCode) {
			boolean speichernErfolgreich = fileSystemHelper
					.processResponse(extraResponse);

			List<FlagType> flagList = new ArrayList<FlagType>();

			flagList = extraResponse.getTransportHeader().getResponseDetails()
					.getReport().getFlag();

			FlagType flag = flagList.get(0);

			FlagCodeType flagCode = flag.getCode();

			if (flagCode.getValue().equalsIgnoreCase("C00")
					|| flagCode.getValue().equalsIgnoreCase("I000")
					|| flagCode.getValue().equalsIgnoreCase("E98")) {

				logger.debug("Verarbeitung erfolgreich");

				returnCode = true;
			} else {

				logger.debug("Verarbeitung nicht erfolgreich");

				returnCode = false;
			}
		}

		return returnCode;
	}
}
