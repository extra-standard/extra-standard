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
package de.extra.client.plugins.queryplugin;

import java.util.List;

import javax.inject.Named;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.extra.client.core.model.SenderDataBean;
import de.extra.client.core.plugin.IDataPlugin;
import de.extra.client.plugins.queryplugin.interfaces.IQueryPluginController;

@Named("dataPlugin")
public class QueryPlugin implements IDataPlugin {

	/**
	 * Pfad und Dateiname in der die SpringConfig.xml liegt
	 */
	private static String SPRING_XML_FILE_PATH = "spring-queryplugin.xml";

	/**
	 * Dateipfad der log4jProperties
	 */
	private static final String LOG_4_J_FILE = "log4j.properties";

	private static Logger logger = Logger.getLogger(QueryPlugin.class);

	@Override
	public List<SenderDataBean> getSenderData() {

		PropertyConfigurator.configureAndWatch(LOG_4_J_FILE);
		logger.info("Start des Versands");

		// Spring Beans laden.
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				SPRING_XML_FILE_PATH);

		// Laden des Controlers
		IQueryPluginController controller = (IQueryPluginController) applicationContext
				.getBean("queryController");

		if (logger.isDebugEnabled()) {
			logger.debug("Erstelle Query");
		}

		return controller.processQuery();
	}
}
