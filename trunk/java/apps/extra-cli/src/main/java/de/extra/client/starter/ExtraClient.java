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
package de.extra.client.starter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import de.extra.client.core.ClientCore;

public class ExtraClient {

	/**
	 * Pfad und Dateiname in der die springconfig.xml liegt.
	 */
	private static String SPRING_XML_FILE_PATH = "spring-cli.xml";

	/**
	 * Dateipfad der log4jProperties.
	 */
	private static final String LOG_4_J_FILE = "log4j.properties";

	private static Logger logger = Logger.getLogger(ExtraClient.class);

	/**
	 * Startmethode zum Aufruf aus dem startenden Programm.
	 * 
	 * @return Statuscode
	 */
	public int execute() throws Exception {
		PropertyConfigurator.configureAndWatch(LOG_4_J_FILE);

		logger.debug("SpringBeans laden");
		ApplicationContext applicationContext = null;

		try {
			// Spring Beans laden.
			applicationContext = new ClassPathXmlApplicationContext(
					SPRING_XML_FILE_PATH);
			// TODO: Pfad als Property
			Resource logBoilerplate = applicationContext.getResource("dependency-tree.txt"); 
			if (logBoilerplate.exists()) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(logBoilerplate.getInputStream()));
				String line = reader.readLine();
				while (line != null) {
					ExtraClientConstants.opLogger.info(line);
					line = reader.readLine();
				}
			}

		} catch (Exception e) {
			logger.error("Laden der Beans fehlgeschlagen", e);
		}

		// Steuerung an Controller übergeben
		logger.info("Beginn der Verarbeitung");

		ClientCore clientCore = applicationContext.getBean("clientCore",
				ClientCore.class);

		return clientCore.buildRequest();
	}
}
