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

import de.extrastandard.api.plugin.IConfigPlugin;
import de.extrastandard.api.plugin.IDataPlugin;
import de.extrastandard.api.plugin.IOutputPlugin;
import de.extrastandard.api.plugin.IResponseProcessPlugin;

/**
 * @author Leonid Potab
 * @version $Id$
 */
public interface IPluginsLocatorManager {

	/**
	 * Liefert der in der Konfiguration unter dem Schlüssel plugins.dataplugin
	 * definierten Bean
	 *
	 * @return
	 */
	IDataPlugin getConfiguredDataPlugin();

	/**
	 * Liefert der in der Konfiguration unter dem Schlüssel plugins.dataplugin
	 * definierten Bean
	 *
	 * @return
	 */
	IOutputPlugin getConfiguredOutputPlugin();

	/**
	 * Liefert der in der Konfiguration unter dem Schlüssel plugins.dataplugin
	 * definierten Bean
	 *
	 * @return
	 */
	IConfigPlugin getConfiguredConfigPlugin();

	/**
	 * Liefert der in der Konfiguration unter dem Schlüssel plugins.dataplugin
	 * definierten Bean
	 *
	 * @return
	 */
	IResponseProcessPlugin getConfiguredResponsePlugin();

}