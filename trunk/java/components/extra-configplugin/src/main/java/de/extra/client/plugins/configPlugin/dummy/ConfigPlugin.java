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
package de.extra.client.plugins.configPlugin.dummy;

import de.extra.client.core.model.ConfigFileBean;
import de.extra.client.core.plugin.IConfigPlugin;

public class ConfigPlugin implements IConfigPlugin {

	/**
	 * Dummy zum Testen der Anwendung
	 */
	@Override
	public ConfigFileBean getConfigFile() {
		return loadConfigFile();
	}

	private ConfigFileBean loadConfigFile() {
		ConfigFileBean config = new ConfigFileBean();

		config.setTestIndicator(true);
		config.setAbsClass("Betriebsnummer");
		config.setAbsBbnr("12345678");
		config.setAbsName("Softwarehaus");
		config.setEmpfClass("Betriebsnummer");
		config.setEmpfBbnr("66667777");
		config.setEmpfName("DRV Bund Wuerzburg");

		config.setProductName("ExtraClient");
		config.setProductManuf("DRV Bund");

		config.setProcedure("http://www.extra-standard.de/procedures/DEUEV");
		config.setDataType("http://www.extra-standard.de/datatypes/Sofortmeldung");
		config.setScenario("http://www.extra-standard.de/scenario/request-with-acknowledgement");

		config.setContentType("xcpt:Base64CharSequence");
		config.setPackageLayer(false);
		config.setMessageLayer(false);

		config.setRequestId("rqId12345");

		return config;
	}
}
