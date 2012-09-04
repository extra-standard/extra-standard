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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.extra.client.core.locator.impl.PluginsLocatorManager;
import de.extrastandard.api.plugin.IDataPlugin;

/**
 * Test for PluginsLocatorManager.
 * 
 * @author Lofi Dewanto
 * @since 1.0.0
 * @version 1.0.0
 */
@RunWith(MockitoJUnitRunner.class)
public class PluginsLocatorManagerTest {

	@InjectMocks
	private IPluginsLocatorManager pluginsLocatorManager = new PluginsLocatorManager();

	@Mock
	private Map<String, IDataPlugin> dataPluginMap;

	@Mock
	IDataPlugin dataPlugin;

	@Test
	public void testGetConfiguratedDataPlugin() {
		when(dataPluginMap.get(anyString())).thenReturn(dataPlugin);

		IDataPlugin resultDataPlugin = pluginsLocatorManager
				.getConfiguratedDataPlugin();
		assertNotNull(resultDataPlugin);
		assertEquals(dataPlugin, resultDataPlugin);
		verify(dataPluginMap, atLeastOnce()).get(anyString());
	}

}
