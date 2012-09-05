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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;

import de.drv.dsrv.extrastandard.namespace.components.RootElementType;
import de.extra.client.core.builder.IExtraRequestBuilder;
import de.extra.client.core.locator.IPluginsLocatorManager;
import de.extrastandard.api.exception.ExtraConfigRuntimeException;
import de.extrastandard.api.exception.ExtraCoreRuntimeException;
import de.extrastandard.api.exception.ExtraRuntimeException;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;
import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.plugin.IConfigPlugin;
import de.extrastandard.api.plugin.IDataPlugin;
import de.extrastandard.api.plugin.IOutputPlugin;
import de.extrastandard.api.plugin.IResponseProcessPlugin;

@Named("clientCore")
public class ClientCore implements ApplicationContextAware {

	private static final Logger LOG = LoggerFactory.getLogger(ClientCore.class);

	public static final int STATUS_CODE_OK = 0;

	public static final int STATUS_CODE_ERROR = 9;

	ApplicationContext applicationContext;

	@Inject
	@Named("pluginsLocatorManager")
	private IPluginsLocatorManager pluginsLocatorManager;

	@Inject
	@Named("extraRequestBuilder")
	IExtraRequestBuilder extraMessageBuilder;

	@Inject
	@Named("eXTrajaxb2Marshaller")
	private Marshaller marshaller;

	private IDataPlugin dataPlugin;

	private IConfigPlugin configPlugin;

	private IOutputPlugin outputPlugin;

	private IResponseProcessPlugin responsePlugin;

	@PostConstruct
	public void init() {
		dataPlugin = pluginsLocatorManager.getConfiguredDataPlugin();

		configPlugin = pluginsLocatorManager.getConfiguredConfigPlugin();

		outputPlugin = pluginsLocatorManager.getConfiguredOutputPlugin();

		responsePlugin = pluginsLocatorManager.getConfiguredResponsePlugin();

	}

	/**
	 * <pre>
	 *  1. Holt über den DataPlugin die zu verarbeitenden Daten.
	 *  2. Verpackt die Daten in eine Extra Message Anhand der Meassagekonfiguration
	 *  3. Versendet die Daten via Outputplugin
	 *  4. Verarbeitet die eXTra Response via Reponse-Process Plugins
	 * </pre>
	 */
	public ClientProcessResult process() {

		final IExtraProfileConfiguration configFile = configPlugin.getConfigFile();

		final Iterator<IInputDataContainer> versandDatenIterator = dataPlugin.getData();

		final ClientProcessResult clientProcessResult = applicationContext.getBean("clientProcessResult",
				ClientProcessResult.class);
		while (versandDatenIterator.hasNext()) {
			final IInputDataContainer dataContainer = versandDatenIterator.next();
			try {
				final List<IResponseData> responses = processInputData(dataContainer, configFile);
				clientProcessResult.addResult(dataContainer, responses);
			} catch (final ExtraConfigRuntimeException extraConfigException) {
				LOG.error("Exception in der Extra-Processing", extraConfigException);
				clientProcessResult.addException(dataContainer, extraConfigException);
			} catch (final ExtraRuntimeException extraRuntimeException) {
				LOG.error("Exception in der Extra-Processing", extraRuntimeException);
				clientProcessResult.addException(dataContainer, extraRuntimeException);
			} catch (final Exception exception) {
				LOG.error("Exception in der Extra-Processing", exception);
				clientProcessResult.addException(dataContainer, exception);
			}
		}
		return clientProcessResult;

	}

	private List<IResponseData> processInputData(final IInputDataContainer inputDataContainer,
			final IExtraProfileConfiguration configFile) {
		try {

			final RootElementType request = extraMessageBuilder.buildXmlMessage(inputDataContainer, configFile);

			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			final StreamResult streamResult = new StreamResult(outputStream);

			marshaller.marshal(request, streamResult);

			LOG.debug("Ausgabe: " + outputStream.toString());
			LOG.debug("Übergabe an OutputPlugin");

			final InputStream responseAsStream = outputPlugin.outputData(new ByteArrayInputStream(outputStream
					.toByteArray()));

			final List<IResponseData> responseDataList = responsePlugin.processResponse(responseAsStream);

			return responseDataList;

		} catch (final XmlMappingException xmlMappingException) {
			throw new ExtraCoreRuntimeException(xmlMappingException);
		} catch (final IOException ioException) {
			throw new ExtraCoreRuntimeException(ioException);
		}

	}

	//
	// /**
	// * Funktion in der der Request aufgebaut wird.
	// *
	// * @return StatusCode nach der Verarbeitung
	// */
	// public int buildRequest() {
	// int statusCode = STATUS_CODE_ERROR;
	//
	// final IDataPlugin dataPlugin =
	// pluginsLocatorManager.getConfiguratedDataPlugin();
	//
	// final Iterator<IInputDataContainer> versandDatenIterator =
	// dataPlugin.getData();
	//
	// while (versandDatenIterator.hasNext()) {
	// // Nothing TODO Klient refactorn
	// statusCode = STATUS_CODE_OK;
	// logger.info("Keine Nachrichten gefunden. Warte auf Nachrichten.");
	// return statusCode;
	// }
	//
	// final IConfigPlugin configPlugin =
	// pluginsLocatorManager.getConfiguratedConfigPlugin();
	//
	// final IExtraProfileConfiguration configFile =
	// configPlugin.getConfigFile();
	//
	// try {
	// IInputDataContainer versanddatenBean = null;
	//
	// // Überprüfen ob ein PackageLayer benötigt wird
	// if (!configFile.isPackageLayer()) {
	// for (final Iterator<IInputDataContainer> iter =
	// versandDatenListe.iterator(); iter.hasNext();) {
	// versanddatenBean = iter.next();
	//
	// // XMLTransport request = requestHelper.buildRequest(
	// // versanddatenBean, configFile);
	// final RootElementType request =
	// extraMessageBuilder.buildXmlMessage(versanddatenBean, configFile);
	//
	// final ByteArrayOutputStream outpuStream = new ByteArrayOutputStream();
	// final StreamResult streamResult = new StreamResult(outpuStream);
	//
	// marshaller.marshal(request, streamResult);
	// logger.debug("Ausgabe: " + outpuStream.toString());
	// logger.debug("Übergabe an OutputPlugin");
	//
	// final IOutputPlugin outputPlugin =
	// pluginsLocatorManager.getConfiguratedOutputPlugin();
	//
	// final InputStream responseAsStream = outputPlugin.outputData(new
	// ByteArrayInputStream(outpuStream
	// .toByteArray()));
	//
	// // Source responseSource = new
	// // InputSource(responseAsStream);
	//
	// final IResponseProcessPlugin responsePlugin =
	// pluginsLocatorManager.getConfiguratedResponsePlugin();
	//
	// final boolean isReportSuccesfull =
	// responsePlugin.processResponse(responseAsStream);
	//
	// if (isReportSuccesfull) {
	// statusCode = STATUS_CODE_OK;
	// } else {
	// statusCode = STATUS_CODE_ERROR;
	// }
	// }
	// } else {
	// // TODO Aufbau der Logik zum Versand von mehreren Nachrichten
	// // als Package oder Message
	//
	// }
	// } catch (final XmlMappingException e) {
	// logger.error("Fehler beim Erstellen des Requests", e);
	// } catch (final IOException e) {
	// logger.error("Fehler beim Erstellen des Requests", e);
	// }
	//
	// return statusCode;
	// }

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

}
