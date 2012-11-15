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
package de.extra.client.plugins.dataplugin.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import de.extra.client.core.model.inputdata.impl.FileInputData;
import de.extra.client.core.model.inputdata.impl.SingleFileInputData;
import de.extra.client.plugins.dataplugin.auftragssatz.AuftragssatzType;
import de.extra.client.plugins.dataplugin.helper.DataPluginHelper;
import de.extra.client.plugins.dataplugin.interfaces.IDataPluginController;
import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.model.content.IInputDataPluginDescription;
import de.extrastandard.api.observer.ITransportObserver;

/**
 * @deprecated
 * 
 *             Wird als SpringBean aktuell nicht verwendet
 */
// @Named("dataPluginController")
@Deprecated
public class DataPluginController implements IDataPluginController {

	@Inject
	@Named("dataPluginHelper")
	private DataPluginHelper dataPluginHelper;

	@Inject
	@Named("transportObserver")
	private ITransportObserver transportObserver;

	/**
	 * Verarbeitungs-Controller fuer das DataPlugin.
	 */
	@Override
	public IInputDataContainer processData() {
		final FileInputData versanddatenBeanList = new FileInputData();
		List<String> nutzfileList = new ArrayList<String>();

		// Ermitteln der Nutzdaten
		nutzfileList = dataPluginHelper.getNutzfiles();

		// Befüllen der Versanddaten-Liste
		for (final Iterator<String> iter = nutzfileList.iterator(); iter
				.hasNext();) {
			final String filename = iter.next();

			final File inputFile = new File(filename);
			final AuftragssatzType auftragssatz = new AuftragssatzType();
			final List<IInputDataPluginDescription> pluginListe = dataPluginHelper
					.extractPluginListe(auftragssatz);
			// String auftragssatzName = filename + ".auf";
			// auftragssatz = dataPluginHelper
			// .unmarshalAuftragssatz(auftragssatzName);

			// Setzen der RequestId
			final SingleFileInputData simpleFileInputData = new SingleFileInputData(
					inputFile, pluginListe);
			simpleFileInputData.setRequestId(auftragssatz.getRequestId());
			simpleFileInputData.setPlugins(pluginListe);
			versanddatenBeanList.addSingleInputData(simpleFileInputData);
			transportObserver.requestDataReceived(filename, inputFile.length());
		}
		return versanddatenBeanList;
	}
}
