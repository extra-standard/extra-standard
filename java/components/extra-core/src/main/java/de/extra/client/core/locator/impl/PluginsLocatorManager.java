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
package de.extra.client.core.locator.impl;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import de.extra.client.core.locator.IPluginsLocatorManager;
import de.extra.client.core.util.IExtraValidator;
import de.extrastandard.api.model.execution.IExecutionPersistence;
import de.extrastandard.api.plugin.IConfigPlugin;
import de.extrastandard.api.plugin.IDataPlugin;
import de.extrastandard.api.plugin.IOutputPlugin;
import de.extrastandard.api.plugin.IResponseProcessPlugin;

/**
 * Sucht nach der in der Konfiguration definierten Plugins.
 * 
 * @author DPRS
 * @version $Id: PluginsLocatorManager.java 512 2012-09-04 11:34:28Z
 *          potap.rentenservice@gmail.com $
 */
@Named("pluginsLocatorManager")
public class PluginsLocatorManager implements IPluginsLocatorManager {

	private static final Logger LOG = LoggerFactory
			.getLogger(PluginsLocatorManager.class);

	@Inject
	@Named("extraValidator")
	private IExtraValidator validator;

	@Inject
	private Map<String, IDataPlugin> dataPluginMap;

	@Value("${plugins.dataplugin}")
	private String dataPlugBeanName;

	@Inject
	private Map<String, IConfigPlugin> configPluginMap;

	@Value("${plugins.configplugin}")
	private String configPluginBeanName;

	@Inject
	private Map<String, IOutputPlugin> outputPluginMap;

	@Value("${plugins.outputplugin}")
	private String outputPluginBeanName;

	@Inject
	private Map<String, IResponseProcessPlugin> responsePluginMap;

	@Value("${plugins.responseprocessplugin}")
	private String responsePluginBeanName;

	@Inject
	private Map<String, IExecutionPersistence> executionPersistenceMap;

	@Value("${plugins.execution}")
	private String executionBeanName;

	public void setDataPluginMap(final Map<String, IDataPlugin> dataPluginMap) {
		this.dataPluginMap = dataPluginMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra.client.core.locator.PluginsLocatorManagerInterface#
	 * getConfiguratedDataPlugin()
	 */
	@Override
	public IDataPlugin getConfiguredDataPlugin() {
		LOG.debug(dataPlugBeanName);
		final IDataPlugin idataPlugin = dataPluginMap.get(dataPlugBeanName);
		Assert.notNull(idataPlugin, "DataPlugin not found for BeanName: "
				+ dataPlugBeanName);
		LOG.debug("DataPlugInClass: " + idataPlugin.getClass());
		validator.validate(idataPlugin);
		return idataPlugin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra.client.core.locator.PluginsLocatorManagerInterface#
	 * getConfiguratedOutputPlugin()
	 */
	@Override
	public IOutputPlugin getConfiguredOutputPlugin() {
		LOG.debug(outputPluginBeanName);
		final IOutputPlugin ioutputPlugin = outputPluginMap
				.get(outputPluginBeanName);
		Assert.notNull(ioutputPlugin, "OutputPlugin not found for BeanName: "
				+ outputPluginBeanName);
		LOG.debug("OutpuPlugInClass: " + ioutputPlugin.getClass());
		validator.validate(ioutputPlugin);
		return ioutputPlugin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra.client.core.locator.PluginsLocatorManagerInterface#
	 * getConfiguratedConfigPlugin()
	 */
	@Override
	public IConfigPlugin getConfiguredConfigPlugin() {
		LOG.debug(configPluginBeanName);
		final IConfigPlugin iConfigPlugin = configPluginMap
				.get(configPluginBeanName);
		Assert.notNull(iConfigPlugin, "ConfigPlugin not found for BeanName: "
				+ configPluginBeanName);
		LOG.debug("ConfiPlugInClasse: " + iConfigPlugin.getClass());
		validator.validate(iConfigPlugin);
		return iConfigPlugin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra.client.core.locator.PluginsLocatorManagerInterface#
	 * getConfiguratedResponsePlugin()
	 */
	@Override
	public IResponseProcessPlugin getConfiguredResponsePlugin() {
		LOG.debug(responsePluginBeanName);
		final IResponseProcessPlugin iResponsePlugin = responsePluginMap
				.get(responsePluginBeanName);
		Assert.notNull(iResponsePlugin,
				"ResponsePlugin not found for BeanName: "
						+ responsePluginBeanName);
		LOG.debug("responsePluginClass: " + iResponsePlugin.getClass());
		validator.validate(iResponsePlugin);
		return iResponsePlugin;
	}

	@Override
	public IExecutionPersistence getConfiguredExecutionPesistence() {
		LOG.debug(executionBeanName);
		final IExecutionPersistence iExecutionPersistence = executionPersistenceMap
				.get(executionBeanName);
		Assert.notNull(iExecutionPersistence,
				"ExecutionPersistence not found for BeanName: "
						+ executionBeanName);
		LOG.debug("ExecutionPersistenceClass: "
				+ iExecutionPersistence.getClass());
		validator.validate(iExecutionPersistence);
		return iExecutionPersistence;
	}

}
