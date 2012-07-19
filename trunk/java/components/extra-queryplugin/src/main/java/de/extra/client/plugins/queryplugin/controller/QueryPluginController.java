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

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;

import de.extra.client.core.model.SenderDataBean;
import de.extra.client.plugins.queryplugin.helper.QueryHelper;
import de.extra.client.plugins.queryplugin.interfaces.IQueryPluginController;

@Named("queryController")
public class QueryPluginController implements IQueryPluginController {

	@Inject
	@Named("queryHelper")
	private QueryHelper queryHelper;

	@Value("${startId}")
	private String startId;

	@Value("${packageLimit}")
	private String packageLimit;

	/**
	 * Controller-Klasse zum Aufbau der Query.
	 */
	@Override
	public List<SenderDataBean> processQuery() {
		List<SenderDataBean> versanddatenBeanList = new ArrayList<SenderDataBean>();
		SenderDataBean versanddatenBean = new SenderDataBean();

		// Erzeugen der Query
		versanddatenBean.setDataRequest(queryHelper.createQuery(startId,
				packageLimit));
		// Hinzufügen der Bean mit der Query
		versanddatenBeanList.add(versanddatenBean);

		return versanddatenBeanList;
	}
}
