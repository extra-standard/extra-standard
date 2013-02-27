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
package de.extra.client.plugins.responseprocessplugin.filesystem;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.XmlMappingException;
import org.springframework.util.Assert;

import de.drv.dsrv.extra.marshaller.IExtraMarschaller;
import de.drv.dsrv.extrastandard.namespace.components.FlagCodeType;
import de.drv.dsrv.extrastandard.namespace.components.FlagType;
import de.drv.dsrv.extrastandard.namespace.components.ReportType;
import de.drv.dsrv.extrastandard.namespace.components.TextType;
import de.drv.dsrv.extrastandard.namespace.response.Transport;
import de.extra.client.core.responce.impl.SingleReportData;

@Named("extraMessageReturnDataExtractor")
public class ExtraMessageReturnDataExtractor {

	private static final Logger LOG = LoggerFactory
			.getLogger(ExtraMessageReturnDataExtractor.class);

	// TODO Systemunabhängigen Formatter
	static final String NEW_LINE = "\r\n";
	// TODO globaler Logger: auslagern
	static final Logger operation_logger = LoggerFactory
			.getLogger("de.extra.client.operation");
	static final Logger message_response_logger = LoggerFactory
			.getLogger("de.extra.client.message.response");

	/**
	 * Liefert ReportDaten. Vorraussetzung ist gibt genau ein FlagType in dem
	 * FlagList
	 * 
	 * Eine Default Implementirung für die vorab Version
	 * 
	 * @param report
	 * @return
	 */
	public SingleReportData extractReportData(final ReportType report) {
		Assert.notNull(report, "Report in der Acknowledge ist leer");
		final List<FlagType> flagList = report.getFlag();
		Assert.notNull(flagList, "Report Flag ist null");
		Assert.isTrue(flagList.size() == 1, "FlagType is empty or has ");
		final FlagType flagType = flagList.get(0);
		Assert.notNull(flagType, "FlagType ist null");

		final FlagCodeType flagCodeType = flagType.getCode();
		Assert.notNull(flagCodeType, "FlagCode ist null");
		final String returnCode = flagCodeType.getValue();
		final TextType text = flagType.getText();
		String returnText = null;
		if (text != null) {
			returnText = text.getValue();
		}
		return new SingleReportData(returnText, returnCode);
	}

	/**
	 * Liefert FlagCode aus der Nachricht
	 * 
	 * @param extraResponse
	 * @return
	 */
	public FlagCodeType getReturnCode(final Transport extraResponse) {
		// TODO Allgemeine Lösung
		List<FlagType> flagList = new ArrayList<FlagType>();

		flagList = extraResponse.getTransportHeader().getResponseDetails()
				.getReport().getFlag();

		final FlagType flag = flagList.get(0);

		final FlagCodeType flagCode = flag.getCode();

		if (flagCode.getValue().equalsIgnoreCase("C00")
				|| flagCode.getValue().equalsIgnoreCase("I000")
				|| flagCode.getValue().equalsIgnoreCase("E98")) {

			LOG.debug("Verarbeitung erfolgreich");

		} else {

			LOG.debug("Verarbeitung nicht erfolgreich");

		}
		return flagCode;
	}

	/**
	 * Die eXTra-Nachricht wird im Log ausgegeben, wenn der messageLogger auf
	 * Debug gesetzt ist.
	 * 
	 * @param marshaller
	 * @param extraResponse
	 */
	static void printResult(IExtraMarschaller marshaller,
			final Transport extraResponse) {
		operation_logger.info("Nachricht vom eXTra-Server erhalten");
		message_response_logger.info("Nachricht vom eXTra-Server erhalten:");
		Logger messageLogger = ExtraMessageReturnDataExtractor.message_response_logger;
		if (messageLogger.isDebugEnabled()) {
			try {
				final Writer writer = new StringWriter();
				final StreamResult streamResult = new StreamResult(writer);

				marshaller.marshal(extraResponse, streamResult);
				messageLogger.debug(writer.toString());
			} catch (final XmlMappingException xmlException) {
				messageLogger.error(
						"XmlMappingException beim Lesen des Results ",
						xmlException);
			} catch (final IOException ioException) {
				messageLogger.debug("IOException beim Lesen des Results ",
						ioException);
			}
		}
	}

}
