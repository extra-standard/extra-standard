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
import java.util.List;

import de.extra.client.core.model.VersanddatenBean;
import de.extra.client.plugins.queryplugin.helper.QueryHelper;
import de.extra.client.plugins.queryplugin.interfaces.IQueryPluginController;

public class QueryPluginController implements IQueryPluginController {

	private final QueryHelper queryHelper;

	private final String startId;

	private final String packageLimit;

	public QueryPluginController(final QueryHelper queryHelper,
			final String startId, final String packageLimit) {
		super();
		this.queryHelper = queryHelper;
		this.startId = startId;
		this.packageLimit = packageLimit;
	}

	/**
	 * Controller-Klasse zum Aufbau der Query
	 */
	@Override
	public List<VersanddatenBean> processQuery() {

		List<VersanddatenBean> versanddatenBeanList = new ArrayList<VersanddatenBean>();

		VersanddatenBean versanddatenBean = new VersanddatenBean();

		// Erzeugen der Query

		versanddatenBean.setDataRequest(queryHelper.createQuery(startId,
				packageLimit));

		// Hinzuf�gen der Bean mit der Query

		versanddatenBeanList.add(versanddatenBean);

		return versanddatenBeanList;
	}
}
