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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

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
}
