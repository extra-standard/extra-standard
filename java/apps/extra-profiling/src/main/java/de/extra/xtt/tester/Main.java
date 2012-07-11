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
package de.extra.xtt.tester;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import de.extra.xtt.gui.XmlXsdFileFilter;
import de.extra.xtt.util.XsdCreatorCtrl;
import de.extra.xtt.util.XsdCreatorCtrlImpl;
import de.extra.xtt.util.tools.Configurator;
import de.extra.xtt.util.tools.ConfiguratorException;
import de.extra.xtt.util.tools.XsdXmlHelper;

/**
 * Diese Klasse erstellt für alle XML-Konfigurationen im parametrierten Ordner
 * (siehe System-Properties) die entsprechenden Schemadateien. In der
 * Oberflächen-Anwendung wird diese Klasse nicht verwendet.
 * 
 * @author Beier
 */
public class Main {

	private static Logger logger = Logger.getLogger(Main.class);

	private static void behandleXmlProfil(XsdCreatorCtrl xsdCreatorCtrl,
			Configurator configurator, File fileConfig) {
		try {
			xsdCreatorCtrl.setXmlFileConfig(fileConfig);

			// Schema profilieren ...
			Map<String, Document> docZielSchema = xsdCreatorCtrl
					.createSchemaProf();

			for (Map.Entry<String, Document> currEntry : docZielSchema
					.entrySet()) {
				String dateiname = configurator.getDateinameFuerSchema(
						xsdCreatorCtrl.getBezeichnungKurzVerfahren(),
						currEntry.getKey());
				String currPath = configurator
						.getPropertySystem(Configurator.PropBezeichnungSystem.PATH_PROF_SCHEMA)
						+ dateiname;
				if (logger.isInfoEnabled()) {
					logger.info(String.format(
							"Schreibe profilierte Schemadatei ('%s') ...",
							currPath));
				}

				XsdXmlHelper.schreibeXsdXml(currPath, currEntry.getValue());
			}

			if (logger.isInfoEnabled()) {
				logger.info(String
						.format("Profilierung erfolgreich abgeschlossen."));
			}
		} catch (Exception e) {
			logger.error(
					"Fehler bei der Profilierung von '"
							+ xsdCreatorCtrl.getPathXmlConfig() + "' :", e);
		}
	}

	private static Configurator initConfigurator() throws ConfiguratorException {
		Properties propUser = Configurator
				.loadPropertiesFromFile(Configurator.PATH_PROPERTIES_USER);
		Properties propSystem = Configurator
				.loadPropertiesFromResource(Configurator.PATH_PROPERTIES_SYSTEM);
		Properties propNamespace = Configurator
				.loadPropertiesFromResource(Configurator.PATH_PROPERTIES_NAMESPACE);
		Properties propVersion = Configurator
				.loadPropertiesFromResource(Configurator.PATH_PROPERTIES_VERSION);
		Properties propAnmerkungen = Configurator
				.loadPropertiesFromFile(Configurator.PATH_PROPERTIES_ANMERKUNGEN);
		ResourceBundle resStrings = Configurator
				.loadResourceBundle(Configurator.NAME_RESBUNDLE_STRINGS);
		Configurator config = new Configurator(propUser, propSystem,
				propNamespace, propVersion, propAnmerkungen, resStrings,
				Configurator.PATH_TAILORING_SCHEMA);
		return config;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Configurator initalisieren
			Configurator config = initConfigurator();

			// Control-Klasse instanziieren
			XsdCreatorCtrl xsdCreatorCtrl = new XsdCreatorCtrlImpl(config);

			// F�r alle Dateien im XML-Verzeichnis Profilierungsschemata
			// erzeugen
			File dirXmlProfile = new File(
					config.getPropertySystem(Configurator.PropBezeichnungSystem.PATH_PROF_XML));
			for (File currFile : dirXmlProfile.listFiles(new XmlXsdFileFilter(
					XmlXsdFileFilter.SUFFIX.XML))) {
				if (currFile.isFile()) {
					behandleXmlProfil(xsdCreatorCtrl, config, currFile);
				}
			}
		} catch (ConfiguratorException configEx) {
			logger.error(configEx);
		}
	}
}
