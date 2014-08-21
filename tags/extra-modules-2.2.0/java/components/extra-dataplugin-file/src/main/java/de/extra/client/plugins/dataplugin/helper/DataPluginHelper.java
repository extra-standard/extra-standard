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
package de.extra.client.plugins.dataplugin.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import de.extra.client.core.model.impl.CompressionPluginDescription;
import de.extra.client.core.model.impl.DataSourcePluginDescription;
import de.extra.client.core.model.impl.EncryptionPluginDescription;
import de.extra.client.plugins.dataplugin.auftragssatz.AuftragssatzType;
import de.extra.client.plugins.dataplugin.auftragssatz.CompressionInfoType;
import de.extra.client.plugins.dataplugin.auftragssatz.DataSourceInfoType;
import de.extra.client.plugins.dataplugin.auftragssatz.EncryptionInfoType;
import de.extrastandard.api.model.content.IInputDataPluginDescription;

/**
 * @deprecated
 * 
 *             Wird als SpringBean aktuell nicht verwendet
 */
@Deprecated
public class DataPluginHelper {

	private static final Logger LOG = LoggerFactory
			.getLogger(DataPluginHelper.class);

	@Value("${plugins.dataplugin.fileDataPlugin.inputVerzeichnis}")
	private File inputDirectory;

	/**
	 * Input-Verzeichnis.
	 * 
	 * @return Input-Verzeichnis
	 */
	public File getInputDirectory() {
		return inputDirectory;
	}

	/**
	 * Laedt die Nutzdatenfiles aus dem Input-Verzeichnis.
	 * 
	 * @return Liste mit den Pfaden der Nutzdateien
	 */
	public List<String> getNutzfiles() {
		final List<String> worklist = new ArrayList<String>();
		final File[] files = inputDirectory.listFiles();

		// Prüfe auf gueltige Dateien
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {

				// Nur Nutzdaten hinzufügen
				if (!files[i].getName().endsWith(".auf")) {
					LOG.debug("Nutzdatensatz gefunden");

					worklist.add(files[i].getAbsolutePath());
				} else {
					LOG.debug("Auftragssatz gefunden");
				}
				LOG.debug("Datei " + files[i].getAbsolutePath() + " ("
						+ files[i].length()
						+ "Bytes) zur Arbeitsliste hinzugef�gt.");
			}
		}
		LOG.info("Arbeitsliste mit " + worklist.size() + " Elementen angelegt.");
		return worklist;
	}

	/**
	 * Wandelt die Nutzdatei in ein Byte-Array für den Aufbau des Requests um.
	 * 
	 * @param filename
	 *            Name der Datei
	 * @return Byte-Array mit den Nutzdaten
	 */
	public byte[] getNutzdaten(final String filename) {
		byte[] nutzdaten = null;

		final File nutzdatei = new File(filename);
		FileInputStream fis = null;

		if (nutzdatei.exists() && !nutzdatei.isDirectory()) {
			try {
				LOG.debug("Einlesen der Nutzdaten");

				fis = new FileInputStream(nutzdatei);
				nutzdaten = new byte[(int) nutzdatei.length()];
				fis.read(nutzdaten);

				if (LOG.isTraceEnabled()) {
					LOG.trace("Nutzdaten: " + new String(nutzdaten));
				}
			} catch (final FileNotFoundException e) {
				LOG.error("Datei konnte nicht gefunden werden", e);
			} catch (final IOException e) {
				LOG.error("Fehler beim Lesen der Datei", e);
			} finally {
				try {
					fis.close();
				} catch (final IOException e) {
					LOG.error("Fehler beim Schlie�en des Streams", e);
				}
			}
		} else {
			LOG.info("Datei nicht vorhanden oder Verzeichnis");
		}

		return nutzdaten;
	}

	/**
	 * Hilfsklasse zum unmarshalling des Auftragssatzes.
	 * 
	 * @param auftragssatzName
	 * @return JaxB-Element vom Typ AuftragssatzType
	 */
	public AuftragssatzType unmarshalAuftragssatz(final String auftragssatzName) {
		JAXBContext jc;
		JAXBElement<?> element = null;
		try {
			// Initialisieren des JaxB-Contextes
			jc = JAXBContext
					.newInstance("de.extra.client.plugins.dataPlugin.auftragssatz");

			// Aufruf des Unmarshallers
			final Unmarshaller u = jc.createUnmarshaller();
			final File auftragsFile = new File(auftragssatzName);
			element = (JAXBElement<?>) u.unmarshal(auftragsFile);
		} catch (final JAXBException e) {
			LOG.error("Fehler beim Verarbeiten des XML", e);
		} catch (final Exception e) {
			LOG.error("Fehler beim Verarbeiten des XML", e);
		}

		return (AuftragssatzType) element.getValue();
	}

	/**
	 * Fuellt die Liste der VersanddatenBean.
	 * 
	 * @param vdb
	 *            VersanddatenBean
	 * @param auftragssatz
	 *            Auftragssatz
	 * @return VersanddatenBean
	 */
	public List<IInputDataPluginDescription> extractPluginListe(
			final AuftragssatzType auftragssatz) {
		CompressionPluginDescription compressionPlugin = new CompressionPluginDescription();
		EncryptionPluginDescription encryptionPlugin = new EncryptionPluginDescription();
		DataSourcePluginDescription dataSourcePlugin = new DataSourcePluginDescription();

		final List<IInputDataPluginDescription> pluginListe = new ArrayList<IInputDataPluginDescription>();
		if (auftragssatz.getCompressionInfo() != null) {

			compressionPlugin = fuelleCompression(auftragssatz
					.getCompressionInfo());
			pluginListe.add(compressionPlugin);
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Keine Informationen zur Kompression gefunden");
			}
		}

		if (auftragssatz.getEncryptionInfo() != null) {
			encryptionPlugin = fuelleEncryption(auftragssatz
					.getEncryptionInfo());
			pluginListe.add(encryptionPlugin);
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Keine Informationen zur Verschluesselung gefunden");
			}
		}
		if (auftragssatz.getDataSourceInfo() != null) {
			dataSourcePlugin = fuelleDataSource(auftragssatz
					.getDataSourceInfo());
			pluginListe.add(dataSourcePlugin);
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Keine Informationen zur DataSource gefunden");
			}
		}
		return pluginListe;
	}

	/**
	 * Hilfsklasse zum Befüllen der CompressionPluginBean.
	 * 
	 * @param compressionInfo
	 * @return CompressionPluginBean
	 */
	private static CompressionPluginDescription fuelleCompression(
			final CompressionInfoType compressionInfo) {
		final CompressionPluginDescription compressionPlugin = new CompressionPluginDescription();

		compressionPlugin.setOrder(compressionInfo.getOrder().intValue());
		compressionPlugin.setCompAlgoId(compressionInfo.getAlgoId());
		compressionPlugin.setCompAlgoVers(compressionInfo.getAlgoVersion());
		compressionPlugin.setCompAlgoName(compressionInfo.getAlgoName());
		compressionPlugin.setCompSpecUrl(compressionInfo.getSpecUrl());
		compressionPlugin.setCompSpecName(compressionInfo.getSpecName());
		compressionPlugin.setCompSpecVers(compressionInfo.getSpecVers());
		compressionPlugin.setCompInput(compressionInfo.getInputSize()
				.intValue());
		compressionPlugin.setCompOutput(compressionInfo.getOutputSize()
				.intValue());

		return compressionPlugin;
	}

	/**
	 * Hilfsklasse zum Befüllen der EncryprionPluginBean.
	 * 
	 * @param encryptionInfo
	 * @return EncryptionPluginBean
	 */
	private static EncryptionPluginDescription fuelleEncryption(
			final EncryptionInfoType encryptionInfo) {
		final EncryptionPluginDescription encryptionPlugin = new EncryptionPluginDescription();

		encryptionPlugin.setOrder(encryptionInfo.getOrder().intValue());
		encryptionPlugin.setEncAlgoId(encryptionInfo.getAlgoId());
		encryptionPlugin.setEncAlgoVers(encryptionInfo.getAlgoVersion());
		encryptionPlugin.setEncAlgoName(encryptionInfo.getAlgoName());
		encryptionPlugin.setEncSpecUrl(encryptionInfo.getSpecUrl());
		encryptionPlugin.setEncSpecName(encryptionInfo.getSpecName());
		encryptionPlugin.setEncSpecVers(encryptionInfo.getSpecVers());
		encryptionPlugin.setEncInput(encryptionInfo.getInputSize().intValue());
		encryptionPlugin
				.setEncOutput(encryptionInfo.getOutputSize().intValue());

		return encryptionPlugin;
	}

	/**
	 * Hilfsklasse zum Befüllen der DataSourcePlugin.
	 * 
	 * @param dataSource
	 * @return
	 */
	private static DataSourcePluginDescription fuelleDataSource(
			final DataSourceInfoType dataSource) {
		final DataSourcePluginDescription dataSourcePlugin = new DataSourcePluginDescription();

		dataSourcePlugin.setType(dataSource.getDsType());
		dataSourcePlugin.setName(dataSource.getDsName());
		dataSourcePlugin.setCreated(dataSource.getDsCreateDate()
				.toGregorianCalendar().getTime());
		dataSourcePlugin.setEncoding(dataSource.getDsEncoding());

		return dataSourcePlugin;
	}
}
