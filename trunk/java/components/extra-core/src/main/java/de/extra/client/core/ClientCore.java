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
package de.extra.client.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;

import de.drv.dsrv.extrastandard.namespace.components.RootElementType;
import de.extra.client.core.builder.IExtraRequestBuilder;
import de.extra.client.core.locator.IPluginsLocatorManager;
import de.extrastandard.api.model.IExtraProfileConfiguration;
import de.extrastandard.api.model.IInputDataContainer;
import de.extrastandard.api.plugin.IConfigPlugin;
import de.extrastandard.api.plugin.IDataPlugin;
import de.extrastandard.api.plugin.IOutputPlugin;
import de.extrastandard.api.plugin.IResponseProcessPlugin;

@Named("clientCore")
public class ClientCore {

	private static final Logger logger = Logger.getLogger(ClientCore.class);

	public static final int STATUS_CODE_OK = 0;

	public static final int STATUS_CODE_ERROR = 9;

	@Inject
	@Named("pluginsLocatorManager")
	private IPluginsLocatorManager pluginsLocatorManager;

	@Inject
	@Named("extraRequestBuilder")
	IExtraRequestBuilder extraMessageBuilder;

	@Inject
	@Named("eXTrajaxb2Marshaller")
	private Marshaller marshaller;

	/**
	 * Funktion in der der Request aufgebaut wird.
	 * 
	 * @return StatusCode nach der Verarbeitung
	 */
	public int buildRequest() {
		int statusCode = STATUS_CODE_ERROR;

		IDataPlugin dataPlugin = pluginsLocatorManager
				.getConfiguratedDataPlugin();

		List<IInputDataContainer> versandDatenListe = dataPlugin
				.getSenderData();

		if (versandDatenListe == null || versandDatenListe.isEmpty()) {
			// Nothing TODO Klient refactorn
			statusCode = STATUS_CODE_OK;
			logger.info("Keine Nachrichten gefunden. Warte auf Nachrichten.");
			return statusCode;
		}

		IConfigPlugin configPlugin = pluginsLocatorManager
				.getConfiguratedConfigPlugin();

		IExtraProfileConfiguration configFile = configPlugin.getConfigFile();

		try {
			IInputDataContainer versanddatenBean = null;

			// Überprüfen ob ein PackageLayer benötigt wird
			if (!configFile.isPackageLayer()) {
				for (Iterator<IInputDataContainer> iter = versandDatenListe
						.iterator(); iter.hasNext();) {
					versanddatenBean = iter.next();

					// XMLTransport request = requestHelper.buildRequest(
					// versanddatenBean, configFile);
					RootElementType request = extraMessageBuilder
							.buildXmlMessage(versanddatenBean, configFile);

					ByteArrayOutputStream outpuStream = new ByteArrayOutputStream();
					StreamResult streamResult = new StreamResult(outpuStream);

					marshaller.marshal(request, streamResult);
					logger.debug("Ausgabe: " + outpuStream.toString());
					logger.debug("Übergabe an OutputPlugin");

					IOutputPlugin outputPlugin = pluginsLocatorManager
							.getConfiguratedOutputPlugin();

					InputStream responseAsStream = outputPlugin
							.outputData(new ByteArrayInputStream(outpuStream
									.toByteArray()));

					// Source responseSource = new
					// InputSource(responseAsStream);

					IResponseProcessPlugin responsePlugin = pluginsLocatorManager
							.getConfiguratedResponsePlugin();

					boolean isReportSuccesfull = responsePlugin
							.processResponse(responseAsStream);

					if (isReportSuccesfull) {
						statusCode = STATUS_CODE_OK;
					} else {
						statusCode = STATUS_CODE_ERROR;
					}
				}
			} else {
				// TODO Aufbau der Logik zum Versand von mehreren Nachrichten
				// als Package oder Message

			}
		} catch (XmlMappingException e) {
			logger.error("Fehler beim Erstellen des Requests", e);
		} catch (IOException e) {
			logger.error("Fehler beim Erstellen des Requests", e);
		}

		return statusCode;
	}
}
