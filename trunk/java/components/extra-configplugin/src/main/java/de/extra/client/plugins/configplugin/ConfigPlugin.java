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

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.extra.client.core.model.ConfigFileBean;
import de.extra.client.core.plugin.IConfigPlugin;
import de.extra.client.plugins.configplugin.controller.ConfigPluginController;

public class ConfigPlugin implements IConfigPlugin {

	/**
	 * Pfad und Dateiname in der die SpringConfig.xml liegt
	 */
	private static String SPRING_XML_FILE_PATH = "spring-configpluginconfig.xml";

	/**
	 * Dateipfad der log4jProperties
	 */
	private static final String LOG_4_J_FILE = "log4j.properties";

	private static Logger logger = Logger.getLogger(ConfigPlugin.class);

	/**
	 * Basismethode mit der die Verarbeitung der Konfigurationsdatei aufgerufen
	 * wird.
	 */
	@Override
	public ConfigFileBean getConfigFile() {
		// Laden des Log4Files
		PropertyConfigurator.configureAndWatch(LOG_4_J_FILE);
		logger.info("Start des Auslesens der Config-Datei");

		// Spring Beans laden.
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				SPRING_XML_FILE_PATH);

		// Instanziieren der Bean
		ConfigPluginController controller = (ConfigPluginController) applicationContext
				.getBean("configController");

		// Aufruf der Verarbeitung
		return controller.processConfigFile();
	}
}
