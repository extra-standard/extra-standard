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
package de.extra.client.core.plugin.dummies;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Named;

import de.extra.client.core.model.CompressionPluginBean;
import de.extra.client.core.model.DataSourcePluginBean;
import de.extra.client.core.model.EncryptionPluginBean;
import de.extra.client.core.model.PlugindatenBean;
import de.extra.client.core.model.SenderDataBean;
import de.extra.client.core.plugin.IDataPlugin;
@Named("dummyDataPlugin")
public class DummyDataPlugin implements IDataPlugin {

	@Override
	public List<SenderDataBean> getSenderData() {
		List<SenderDataBean> versanddatenList = new ArrayList<SenderDataBean>();
		versanddatenList.add(loadVersanddaten());
		return versanddatenList;
	}

	/**
	 * Dummy-Klasse mit statischen Werten.
	 * 
	 * @return Versanddatenbean
	 */
	private SenderDataBean loadVersanddaten() {

		SenderDataBean vb = new SenderDataBean();
		CompressionPluginBean compressionPlugin = new CompressionPluginBean();
		EncryptionPluginBean encryptionPlugin = new EncryptionPluginBean();
		DataSourcePluginBean dataSourcePlugin = new DataSourcePluginBean();

		String nutzdaten = "Testdaten";

		// Nutzdaten setzen
		vb.setNutzdaten(nutzdaten.getBytes());

		// Compression-Infos setzen
		compressionPlugin.setOrder(1);
		compressionPlugin
				.setCompAlgoId("http://www.extra-standard.de/transforms/compression/NONE");
		compressionPlugin.setCompAlgoVers("1.0");
		compressionPlugin.setCompAlgoName("KKS");

		compressionPlugin.setCompSpecUrl("http://www.datentausch.de");
		compressionPlugin.setCompSpecName("KKS");
		compressionPlugin.setCompSpecVers("1.0");

		compressionPlugin.setCompInput(200);
		compressionPlugin.setCompOutput(100);

		// Encryption-Infos setzen
		encryptionPlugin.setOrder(2);
		encryptionPlugin
				.setEncAlgoId("http://www.extra-standard.de/transforms/encryption/PKCS7");
		encryptionPlugin.setEncAlgoVers("1.3");
		encryptionPlugin.setEncAlgoName("KKS");

		encryptionPlugin.setEncSpecUrl("http://www.datentausch.de");
		encryptionPlugin.setEncSpecName("KKS");
		encryptionPlugin.setEncSpecVers("1.5.1");

		encryptionPlugin.setEncInput(100);
		encryptionPlugin.setEncOutput(200);

		// DataSource-Infos setzen
		dataSourcePlugin.setDsType("http://extra-standard.de/container/FILE");
		dataSourcePlugin.setDsName("EDUA0000003");

		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, 2012);
		cal.set(Calendar.MONTH, 12);
		cal.set(Calendar.DATE, 01);
		dataSourcePlugin.setDsCreated(cal.getTime());
		dataSourcePlugin.setDsEncoding("I8");

		List<PlugindatenBean> pluginList = new ArrayList<PlugindatenBean>();

		pluginList.add(compressionPlugin);
		pluginList.add(encryptionPlugin);
		pluginList.add(dataSourcePlugin);

		vb.setPlugins(pluginList);

		return vb;
	}
}
