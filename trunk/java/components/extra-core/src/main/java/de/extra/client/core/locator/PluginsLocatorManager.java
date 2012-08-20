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
package de.extra.client.core.locator;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import de.extra.client.core.plugin.IConfigPlugin;
import de.extra.client.core.plugin.IDataPlugin;
import de.extra.client.core.plugin.IOutputPlugin;
import de.extra.client.core.plugin.IResponseProcessPlugin;

/**
 * Sucht nach der in der Konfiguration definierten Plugins.
 * 
 * @author evpqq5
 */
@Named("pluginsLocatorManager")
public class PluginsLocatorManager implements IPluginsLocatorManager {

	private static final Logger logger = Logger
			.getLogger(PluginsLocatorManager.class);

	@Inject
	Map<String, IDataPlugin> dataPluginMap;

	@Value("${plugins.dataplugin}")
	String dataPlugBeanName;

	@Inject
	Map<String, IConfigPlugin> configPluginMap;

	@Value("${plugins.configplugin}")
	String configPluginBeanName;

	@Inject
	Map<String, IOutputPlugin> outputPluginMap;

	@Value("${plugins.outputplugin}")
	String outputPluginBeanName;

	@Inject
	Map<String, IResponseProcessPlugin> responsePluginMap;

	@Value("${plugins.responseprocessplugin}")
	String responsePluginBeanName;

	public void setDataPluginMap(Map<String, IDataPlugin> dataPluginMap) {
		this.dataPluginMap = dataPluginMap;
	}

	/* (non-Javadoc)
	 * @see de.extra.client.core.locator.PluginsLocatorManagerInterface#getConfiguratedDataPlugin()
	 */
	@Override
	public IDataPlugin getConfiguratedDataPlugin() {
		logger.debug(dataPlugBeanName);
		IDataPlugin idataPlugin = dataPluginMap.get(dataPlugBeanName);
		logger.debug("DataPlugInClass: " + idataPlugin.getClass());
		return idataPlugin;
	}

	/* (non-Javadoc)
	 * @see de.extra.client.core.locator.PluginsLocatorManagerInterface#getConfiguratedOutputPlugin()
	 */
	@Override
	public IOutputPlugin getConfiguratedOutputPlugin() {
		logger.debug(outputPluginBeanName);
		IOutputPlugin ioutputPlugin = outputPluginMap.get(outputPluginBeanName);
		logger.debug("OutpuPlugInClass: " + ioutputPlugin.getClass());
		return ioutputPlugin;
	}

	/* (non-Javadoc)
	 * @see de.extra.client.core.locator.PluginsLocatorManagerInterface#getConfiguratedConfigPlugin()
	 */
	@Override
	public IConfigPlugin getConfiguratedConfigPlugin() {
		logger.debug(configPluginBeanName);
		IConfigPlugin iConfigPlugin = configPluginMap.get(configPluginBeanName);
		logger.debug("ConfiPlugInClasse: " + iConfigPlugin.getClass());
		return iConfigPlugin;
	}

	/* (non-Javadoc)
	 * @see de.extra.client.core.locator.PluginsLocatorManagerInterface#getConfiguratedResponsePlugin()
	 */
	@Override
	public IResponseProcessPlugin getConfiguratedResponsePlugin() {
		logger.debug(configPluginBeanName);
		IResponseProcessPlugin iResponsePlugin = responsePluginMap
				.get(responsePluginBeanName);
		logger.debug("responsePluginClass: " + iResponsePlugin.getClass());
		return iResponsePlugin;
	}

}
