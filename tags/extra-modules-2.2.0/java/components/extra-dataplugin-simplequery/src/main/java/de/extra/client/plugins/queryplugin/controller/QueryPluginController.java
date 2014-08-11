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
package de.extra.client.plugins.queryplugin.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.extra.client.core.annotation.PluginValue;
import de.extra.client.plugins.queryplugin.interfaces.IQueryPluginController;
import de.extrastandard.api.model.content.IInputDataContainer;

/**
 * @deprecated
 * 
 *             Wird nicht mehr verwendet.
 * 
 */
// @Named("queryPluginController")
// @PluginConfiguration(pluginBeanName = "queryDataPlugin", pluginType =
// PluginConfigType.DataPlugins)
public class QueryPluginController implements IQueryPluginController {

	@PluginValue(key = "startId")
	private String startId;

	private final List<IInputDataContainer> senderDataBeanList;

	private final Iterator<IInputDataContainer> iterator;

	public QueryPluginController() {
		super();
		senderDataBeanList = new ArrayList<IInputDataContainer>();
		iterator = senderDataBeanList.iterator();
	}

	/**
	 * @param startId
	 */
	public void setStartId(final String startId) {
		this.startId = startId;
	}

	/**
	 * Controller-Klasse zum Aufbau der Query.
	 */
	@Override
	public IInputDataContainer getData() {
		return iterator.next();
	}

	@Override
	public boolean hasMoreData() {
		return iterator.hasNext();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
}
