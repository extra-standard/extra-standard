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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import de.extra.client.core.model.FileInputData;
import de.extra.client.plugins.dataplugin.auftragssatz.AuftragssatzType;
import de.extra.client.plugins.dataplugin.helper.DataPluginHelper;
import de.extra.client.plugins.dataplugin.interfaces.IDataPluginController;
import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.model.content.IInputDataPluginDescription;
import de.extrastandard.api.observer.ITransportObserver;

@Named("dataPluginController")
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
	public List<IInputDataContainer> processData() {
		final List<IInputDataContainer> versanddatenBeanList = new ArrayList<IInputDataContainer>();
		List<String> nutzfileList = new ArrayList<String>();

		// Ermitteln der Nutzdaten
		nutzfileList = dataPluginHelper.getNutzfiles();

		// Befüllen der Versanddaten-Liste
		for (final Iterator<String> iter = nutzfileList.iterator(); iter.hasNext();) {
			final String filename = iter.next();

			final File inputFile = new File(filename);
			FileInputStream inputData;
			try {
				inputData = new FileInputStream(inputFile);
			} catch (final FileNotFoundException e) {
				// TODO exception reinbauen
				throw new IllegalStateException(e);
			}

			final AuftragssatzType auftragssatz = new AuftragssatzType();
			final List<IInputDataPluginDescription> pluginListe = dataPluginHelper.extractPluginListe(auftragssatz);
			// String auftragssatzName = filename + ".auf";
			// auftragssatz = dataPluginHelper
			// .unmarshalAuftragssatz(auftragssatzName);

			// Setzen der RequestId
			final FileInputData versanddatenBean = new FileInputData(inputFile.getName(), inputData, null);

			versanddatenBean.setRequestId(auftragssatz.getRequestId());
			versanddatenBean.setPlugins(pluginListe);
			versanddatenBeanList.add(versanddatenBean);

			transportObserver.requestDataReceived(filename, inputFile.length());
		}

		return versanddatenBeanList;
	}
}
