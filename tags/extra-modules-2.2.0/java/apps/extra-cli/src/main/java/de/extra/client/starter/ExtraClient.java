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
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.extra.client.core.ClientCore;
import de.extra.client.core.ClientProcessResult;
import de.extra.client.core.ProcessResult;
import de.extra.client.core.model.inputdata.impl.SingleFileInputData;
import de.extrastandard.api.exception.ExceptionCode;
import de.extrastandard.api.exception.ExtraConfigRuntimeException;
import de.extrastandard.api.model.content.ISingleInputData;

/**
 * ExtraClient main program.
 * 
 * @author Leonid Potap
 * @author Thorsten Vogel
 * @version $Id: ExtraClient.java 538 2012-09-05 09:48:23Z
 *          thorstenvogel@gmail.com $
 */
public class ExtraClient {

	private static final Logger LOG = LoggerFactory
			.getLogger(ExtraClient.class);

	private static final Logger opperation_logger = LoggerFactory
			.getLogger("de.extra.client.operation");

	/**
	 * Name of the basic configuration.
	 */
	public static final String PROPERTIES_BASIC_FILENAME = "extra-properties-basic.properties";

	/**
	 * Name of Basic-Properties.
	 */
	public static final String BEAN_NAME_EXTRA_PROPERTIES_BASIC = "_extern_extra-properties-basic";

	/**
	 * Filename user configuration.
	 */
	public static final String PROPERTIES_USER_FILENAME = "extra-properties-user.properties";

	/**
	 * Name of User-Properties.
	 */
	public static final String BEAN_NAME_EXTRA_PROPERTIES_USER = "_extern_extra-properties-user";

	/**
	 * Path and filename for Spring configuration.
	 */
	private static final String SPRING_XML_FILE_PATH = "spring-cli.xml";

	/**
	 * Command line parameters.
	 */
	private final ExtraClientParameters parameters;

	/**
	 * Create an ExtraClient. Configuration will be read from.
	 * {@link #PROPERTIES_BASIC_FILENAME} and {@link #PROPERTIES_USER_FILENAME}
	 * 
	 * @param parameters
	 *            parameters for extra-client
	 */
	public ExtraClient(final ExtraClientParameters parameters) {
		this.parameters = parameters;
	}

	/**
	 * Start method.
	 * 
	 * @return Statuscode
	 */
	public ClientProcessResult execute() {
		opperation_logger.info("Start Of Processing.");
		LOG.debug("Load ApplicationContext");
		try {
			final ApplicationContext applicationContext = createApplicationContext();
			final ClientCore clientCore = applicationContext.getBean(
					"clientCore", ClientCore.class);
			final ClientProcessResult clientProcessResult = clientCore
					.process(parameters.getConfigurationDirectory()
							.getAbsolutePath());
			opperation_logger.info("ExecutionsResults: {}",
					clientProcessResult.printResults());
			postProcess(clientProcessResult);
			return clientProcessResult;
		} catch (final Exception e) {
			LOG.error("Fehler beim Start", e);
			throw new ExtraConfigRuntimeException(e);
		}
	}

	/**
	 * Post process all files.
	 * 
	 * @param clientProcessResult
	 */
	private void postProcess(final ClientProcessResult clientProcessResult) {
		final List<ProcessResult> responses = clientProcessResult
				.getResponses();
		for (final ProcessResult processResult : responses) {
			final List<ISingleInputData> content = processResult
					.getDataContainer().getContent();
			for (final ISingleInputData singleInputData : content) {
				final String inputDataType = singleInputData.getInputDataType();
				if (!SingleFileInputData.INPUT_DATA_TYPE.equals(inputDataType)) {
					continue;
				}
				handleInputFile(singleInputData);
			}
		}
	}

	/**
	 * Handle the input files based on parameters. - Create Backup and/or -
	 * Delete file
	 * 
	 * @param singleInputData
	 *            file
	 */
	void handleInputFile(final ISingleInputData singleInputData) {
		final String inputIdentifier = singleInputData.getInputIdentifier();
		final File file = new File(inputIdentifier);
		if (parameters.shouldCreateBackup()) {
			final File backupDirectory = parameters.getBackupDirectory();
			LOG.debug("copying {} to {}", new Object[] {
					file.getAbsolutePath(), backupDirectory.getAbsolutePath() });
			try {
				FileUtils.copyFileToDirectory(file, backupDirectory);
			} catch (final IOException e) {
				LOG.error(
						"Konnte Datei {} nicht nach {} kopieren ({}).",
						new Object[] { file.getAbsolutePath(),
								backupDirectory.getAbsolutePath(),
								e.getMessage() });
			}
		}

		if (parameters.getDeleteInputFiles()) {
			LOG.debug("deleting {}", file.getAbsolutePath());
			try {
				if (!file.delete()) {
					LOG.error("Datei {} konnte nicht gelöscht werden.",
							file.getAbsolutePath());
				}
			} catch (final Exception e) {
				LOG.error("Fehler beim Löschen der Datei {} ({}).",
						file.getAbsolutePath(), e.getMessage());
			}
		}
	}

	/**
	 * Create application context.
	 * 
	 * @return ApplicationContext
	 * @throws Exception
	 */
	ApplicationContext createApplicationContext() throws Exception {
		final Properties basicProperties = readBasicProperties();
		final Properties userProperties = readUserProperties();
		final Map<String, Object> env = new HashMap<String, Object>();
		env.put(BEAN_NAME_EXTRA_PROPERTIES_BASIC, basicProperties);
		env.put(BEAN_NAME_EXTRA_PROPERTIES_USER, userProperties);
		env.put("_configurationDirectory",
				parameters.getConfigurationDirectory());
		final ApplicationContext applicationContext = new ApplicationContextStarter<AbstractApplicationContext>() {
			@Override
			protected AbstractApplicationContext createUninitializedContext() {
				return new ClassPathXmlApplicationContext(
						new String[] { SPRING_XML_FILE_PATH }, false);
			}
		}.createApplicationContext(env);

		return applicationContext;
	}

	private Properties readBasicProperties() throws FileNotFoundException,
			IOException {
		final Properties basicProperties = new Properties();

		// read global configuration directory
		final File globalConfigurationDirectory = parameters
				.getGlobalConfigurationDirectory();
		readPropertiesFromDirectory(globalConfigurationDirectory,
				basicProperties);

		// store mandant as a property
		final String mandant = parameters.getMandant();
		basicProperties.put("extra.mandant", mandant);

		// determine mandant configuration directory
		final String mandantConfigurationDirectory = mandant;
		final File clientConfigurationDirectory = new File(
				globalConfigurationDirectory, mandantConfigurationDirectory);
		if (clientConfigurationDirectory.exists()) {
			LOG.info("reading client configuration directory {}",
					clientConfigurationDirectory.getAbsolutePath());
			readPropertiesFromDirectory(clientConfigurationDirectory,
					basicProperties);
		}

		final File basicPropertiesFile = new File(
				parameters.getConfigurationDirectory(),
				PROPERTIES_BASIC_FILENAME);
		readPropertiesFromFile(basicProperties, basicPropertiesFile);
		return basicProperties;
	}

	private Properties readUserProperties() throws FileNotFoundException,
			IOException {
		final Properties userProperties = new Properties();
		final File userPropsFile = new File(
				parameters.getConfigurationDirectory(),
				PROPERTIES_USER_FILENAME);
		readPropertiesFromFile(userProperties, userPropsFile);
		return userProperties;
	}

	private void readPropertiesFromDirectory(final File propertiesDirectory,
			final Properties properties) throws FileNotFoundException,
			IOException {
		checkDirectory(propertiesDirectory);
		final String[] propertyFiles = propertiesDirectory
				.list(new FilenameFilter() {
					@Override
					public boolean accept(final File dir, final String name) {
						return name != null && name.endsWith(".properties");
					}
				});
		// merge all found properties
		for (final String propertyFile : propertyFiles) {
			readPropertiesFromFile(properties, new File(propertiesDirectory,
					propertyFile));
		}
	}

	private void readPropertiesFromFile(final Properties properties,
			final File propertyFile) throws FileNotFoundException, IOException {
		checkFile(propertyFile);
		final FileInputStream stream = new FileInputStream(propertyFile);
		LOG.debug("loading properties from file {}", propertyFile);
		properties.load(stream);
		IOUtils.closeQuietly(stream);
	}

	private void checkDirectory(final File directory) {
		if (directory == null || !directory.exists() || !directory.canRead()
				|| !directory.isDirectory()) {
			throw new ExtraConfigRuntimeException(
					ExceptionCode.EXTRA_CONFIGURATION_EXCEPTION, String.format(
							"Verzeichnis nicht gefunden: %s",
							directory.getAbsolutePath()));
		}
	}

	private void checkFile(final File file) {
		if (file == null || !file.exists() || !file.canRead()) {
			throw new ExtraConfigRuntimeException(
					ExceptionCode.EXTRA_CONFIGURATION_EXCEPTION, String.format(
							"Datei nicht gefunden: %s", file.getAbsolutePath()));
		}
	}

}
