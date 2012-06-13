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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.extra.client.core.model.VersanddatenBean;
import de.extra.client.plugins.dataplugin.auftragssatz.AuftragssatzType;
import de.extra.client.plugins.dataplugin.helper.DataPluginHelper;
import de.extra.client.plugins.dataplugin.interfaces.IDataPluginController;

public class DataPluginController implements IDataPluginController {

	private final DataPluginHelper dataPluginHelper;

	public DataPluginController(final DataPluginHelper dataPluginHelper) {
		super();
		this.dataPluginHelper = dataPluginHelper;
	}

	/**
	 * Verarbeitungs-Controller fuer das DataPlugin
	 */
	@Override
	public List<VersanddatenBean> processData() {
		List<VersanddatenBean> versanddatenBeanList = new ArrayList<VersanddatenBean>();

		List<String> nutzfileList = new ArrayList<String>();

		// Ermitteln der Nutzdaten
		nutzfileList = dataPluginHelper.getNutzfiles();

		// Befuellen der Versanddaten-Liste
		for (Iterator<String> iter = nutzfileList.iterator(); iter.hasNext();) {
			String filename = iter.next();

			VersanddatenBean versanddatenBean = new VersanddatenBean();

			versanddatenBean.setNutzdaten(dataPluginHelper
					.getNutzdaten(filename));

			AuftragssatzType auftragssatz = new AuftragssatzType();

			String auftragssatzName = filename + ".auf";

			auftragssatz = dataPluginHelper
					.unmarshalAuftragssatz(auftragssatzName);

			// Setzen der RequestId
			versanddatenBean.setRequestId(auftragssatz.getRequestId());

			versanddatenBean = dataPluginHelper.fuelleVersandatenBean(
					versanddatenBean, auftragssatz);

			versanddatenBeanList.add(versanddatenBean);
		}

		return versanddatenBeanList;
	}
}
