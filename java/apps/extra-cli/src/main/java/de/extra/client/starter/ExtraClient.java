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

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.extra.client.core.ClientCore;
import de.extra.client.core.ClientProcessResult;
import de.extrastandard.api.exception.ExceptionCode;
import de.extrastandard.api.exception.ExtraConfigRuntimeException;

/**
 *
 * @author Leonid Potab
 * @author Thorsten Vogel
 * @version $Id$
 */
public class ExtraClient {

	private static final Logger LOG = LoggerFactory
			.getLogger(ExtraClient.class);

	/**
	 * Name der grundlegenden Konfiguration
	 */
	public static final String PROPERTIES_BASIC_FILENAME = "extra-properties-basic.properties";

	/**
	 * Dateiname der Benutzerkonfiguration
	 */
	public static final String PROPERTIES_USER_FILENAME = "extra-properties-user.properties";

	/**
	 * Pfad und Dateiname der Spring Konfiguration
	 */
	private static String SPRING_XML_FILE_PATH = "spring-cli.xml";

	private final File configurationDirectory;

	/**
	 * Erzeugt einen ExtraClient. Die Konfiguration wird aus den Dateien
	 * {@link #PROPERTIES_BASIC_FILENAME} und {@link #PROPERTIES_USER_FILENAME}
	 * ausgelesen.
	 *
	 * @param configurationDirectory
	 *            Konfigurationsverzeichnis
	 */
	public ExtraClient(final File configurationDirectory) {
		this.configurationDirectory = configurationDirectory;
	}

	/**
	 * Startmethode zum Aufruf aus dem startenden Programm.
	 *
	 * @return Statuscode
	 */
	public ClientProcessResult execute() {
		LOG.debug("SpringBeans laden");
		ApplicationContext applicationContext = null;
		File basicPropsFile = new File(configurationDirectory,
				PROPERTIES_BASIC_FILENAME);
		if (!basicPropsFile.exists() || !basicPropsFile.canRead()) {
			throw new ExtraConfigRuntimeException(
					ExceptionCode.EXTRA_CONFIGURATION_EXCEPTION, String.format(
							"Konfiguration nicht gefunden: %s",
							PROPERTIES_BASIC_FILENAME));
		}
		File userPropsFile = new File(configurationDirectory,
				PROPERTIES_USER_FILENAME);
		if (!userPropsFile.exists() || !userPropsFile.canRead()) {
			throw new ExtraConfigRuntimeException(
					ExceptionCode.EXTRA_CONFIGURATION_EXCEPTION, String.format(
							"Konfiguration nicht gefunden: %s",
							PROPERTIES_USER_FILENAME));
		}
		try {
			Properties basicProperties = new Properties();
			FileInputStream basicPropsStream = new FileInputStream(basicPropsFile);
			basicProperties.load(basicPropsStream);
			IOUtils.closeQuietly(basicPropsStream);

			Properties userProperties = new Properties();
			FileInputStream userPropsStream = new FileInputStream(userPropsFile);
			basicProperties.load(userPropsStream);
			IOUtils.closeQuietly(userPropsStream);

			Map<String, Object> env = new HashMap<String, Object>();
			env.put("_extern_extra-properties-basic", basicProperties);
			env.put("_extern_extra-properties-user", userProperties);
			applicationContext = new ApplicationContextStarter<AbstractApplicationContext>() {
				@Override
				protected AbstractApplicationContext createUninitializedContext() {
					return new ClassPathXmlApplicationContext(
							new String[] { SPRING_XML_FILE_PATH }, false);
				}
			}.createApplicationContext(env);

			LOG.info("Beginn der Verarbeitung");

			final ClientCore clientCore = applicationContext.getBean("clientCore", ClientCore.class);

			final ClientProcessResult processResult = clientCore.process();

			// TODO Auswerten Ergebnisse

			return processResult;

		} catch (final Exception e) {
			LOG.error("Fehler beim Start", e);
			throw new ExtraConfigRuntimeException(e);
		}
	}
}
