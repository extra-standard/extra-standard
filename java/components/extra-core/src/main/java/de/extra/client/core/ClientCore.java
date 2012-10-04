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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.util.Assert;

import de.drv.dsrv.extrastandard.namespace.components.RootElementType;
import de.extra.client.core.builder.IExtraRequestBuilder;
import de.extra.client.core.locator.IPluginsLocatorManager;
import de.extra.client.core.process.IRequestIdAcquisitionStrategy;
import de.extrastandard.api.exception.ExceptionCode;
import de.extrastandard.api.exception.ExtraConfigRuntimeException;
import de.extrastandard.api.exception.ExtraCoreRuntimeException;
import de.extrastandard.api.exception.ExtraRuntimeException;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;
import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.model.content.ISingleResponseData;
import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.IExecutionPersistence;
import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.api.plugin.IConfigPlugin;
import de.extrastandard.api.plugin.IDataPlugin;
import de.extrastandard.api.plugin.IOutputPlugin;
import de.extrastandard.api.plugin.IResponseProcessPlugin;

@Named("clientCore")
public class ClientCore implements ApplicationContextAware {

	private static final Logger LOG = LoggerFactory.getLogger(ClientCore.class);

	ApplicationContext applicationContext;

	@Value("${core.execution.procedure}")
	private String executionProcedure;

	@Value("${core.execution.phase}")
	private String executionPhase;

	@Inject
	@Named("pluginsLocatorManager")
	private IPluginsLocatorManager pluginsLocatorManager;

	@Inject
	@Named("extraRequestBuilder")
	IExtraRequestBuilder extraMessageBuilder;

	@Inject
	@Named("eXTrajaxb2Marshaller")
	private Marshaller marshaller;

	@Inject
	@Named("simpleRequestIdAcquisitionStrategy")
	private IRequestIdAcquisitionStrategy requestIdAcquisitionStrategy;

	private IDataPlugin dataPlugin;

	private IConfigPlugin configPlugin;

	private IOutputPlugin outputPlugin;

	private IResponseProcessPlugin responsePlugin;

	private IExecutionPersistence executionPersistence;

	@PostConstruct
	public void init() {
		dataPlugin = pluginsLocatorManager.getConfiguredDataPlugin();

		configPlugin = pluginsLocatorManager.getConfiguredConfigPlugin();

		outputPlugin = pluginsLocatorManager.getConfiguredOutputPlugin();

		responsePlugin = pluginsLocatorManager.getConfiguredResponsePlugin();

		executionPersistence = pluginsLocatorManager.getConfiguredExecutionPesistence();

	}

	/**
	 * <pre>
	 *  1. Holt über den DataPlugin die zu verarbeitenden Daten.
	 *  2. Verpackt die Daten in eine Extra Message Anhand der Meassagekonfiguration
	 *  3. Versendet die Daten via Outputplugin
	 *  4. Verarbeitet die eXTra Response via Reponse-Process Plugins
	 * </pre>
	 */
	public ClientProcessResult process(final String processParameters) {

		final PhaseQualifier phaseQualifier = PhaseQualifier.resolveByName(executionPhase);

		final boolean isProcedureStartPhase = executionPersistence.isProcedureStartPhase(executionProcedure,
				executionPhase);

		IExecution execution = null;
		if (isProcedureStartPhase) {
			execution = executionPersistence.startExecution(executionProcedure, processParameters);
		}

		final IExtraProfileConfiguration configFile = configPlugin.getConfigFile();

		final Iterator<IInputDataContainer> versandDatenIterator = dataPlugin.getData();

		final ClientProcessResult clientProcessResult = applicationContext.getBean("clientProcessResult",
				ClientProcessResult.class);

		while (versandDatenIterator.hasNext()) {
			final IInputDataContainer inputDataContainer = versandDatenIterator.next();
			final String inputIdentification = inputDataContainer.getInputIdentification();
			IInputData inputData = null;
			try {
				if (isProcedureStartPhase) {
					inputData = execution.startInputData(inputIdentification, null);
					requestIdAcquisitionStrategy.setRequestId(inputData, inputDataContainer);
				} else {
					inputData = executionPersistence.findInputDataByRequestId(inputDataContainer.getRequestId());
					Assert.notNull(inputData, "Keine Daten sind zu dem Request gefunden. RequestId: "
							+ inputDataContainer.getRequestId());
				}

				final IResponseData responseData = processInputData(inputDataContainer, configFile, inputData);

				/**
				 * TODO Hier ist die Enschränkung. Momentan wird pro Lauf nur
				 * eine Datei versendent. TODO API nach der ersten Pilotierung
				 * sollten angepasst werden
				 * 
				 */
				final String requestId = inputDataContainer.getRequestId();
				final ISingleResponseData singleResponseData = responseData.getResponse(requestId);
				String responseId = null;
				if (isProcedureStartPhase && singleResponseData != null) {
					responseId = singleResponseData.getResponseId();
				}
				/**
				 * TODO ENDE
				 * */
				inputData.success(responseId, phaseQualifier);

				clientProcessResult.addResult(inputDataContainer, responseData);
			} catch (final ExtraConfigRuntimeException extraConfigException) {
				LOG.error("Exception in der Extra-Processing", extraConfigException);
				clientProcessResult.addException(inputDataContainer, extraConfigException);
				failed(inputData, extraConfigException);
			} catch (final ExtraRuntimeException extraRuntimeException) {
				LOG.error("Exception in der Extra-Processing", extraRuntimeException);
				clientProcessResult.addException(inputDataContainer, extraRuntimeException);
				failed(inputData, extraRuntimeException);
			} catch (final Exception exception) {
				LOG.error("Exception in der Extra-Processing", exception);
				clientProcessResult.addException(inputDataContainer, exception);
				failed(inputData, exception);
			}
		}
		return clientProcessResult;

	}

	private void failed(final IInputData inputData, final ExtraRuntimeException extraRuntimeException) {
		if (inputData != null) {
			inputData.failed(extraRuntimeException);
		}

	}

	private void failed(final IInputData inputData, final Exception exception) {
		if (inputData != null) {
			inputData.failed(ExceptionCode.UNEXPECTED_INTERNAL_EXCEPTION.name(), exception.getMessage());
		}
	}

	private IResponseData processInputData(final IInputDataContainer inputDataContainer,
			final IExtraProfileConfiguration configFile, final IInputData inputData) {
		try {
			final RootElementType request = extraMessageBuilder.buildXmlMessage(inputDataContainer, configFile);

			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			final StreamResult streamResult = new StreamResult(outputStream);

			marshaller.marshal(request, streamResult);

			LOG.debug("Ausgabe: " + outputStream.toString());
			LOG.debug("Übergabe an OutputPlugin");
			inputData.updateProgress(PersistentStatus.ENVELOPED);
			final InputStream responseAsStream = outputPlugin.outputData(new ByteArrayInputStream(outputStream
					.toByteArray()));
			inputData.updateProgress(PersistentStatus.TRANSMITTED);
			final IResponseData responseData = responsePlugin.processResponse(responseAsStream);

			return responseData;

		} catch (final XmlMappingException xmlMappingException) {
			throw new ExtraCoreRuntimeException(xmlMappingException);
		} catch (final IOException ioException) {
			throw new ExtraCoreRuntimeException(ioException);
		}

	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

}
