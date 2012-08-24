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
package de.extra.client.plugins.configplugin;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import de.extra.client.core.model.IExtraProfileConfiguration;
import de.extra.client.core.plugin.IConfigPlugin;
import de.extra.client.plugins.configplugin.controller.ConfigPluginController;

@Named("defaultConfigPlugin")
public class ConfigPlugin implements IConfigPlugin {

	@Inject
	@Named("configController")
	private ConfigPluginController configPluginController;

	private static Logger logger = Logger.getLogger(ConfigPlugin.class);

	/**
	 * Basismethode mit der die Verarbeitung der Konfigurationsdatei aufgerufen
	 * wird.
	 */
	@Override
	public IExtraProfileConfiguration getConfigFile() {
		logger.info("Aufruf der Verarbeitung");
		return configPluginController.processConfigFile();
	}
}
