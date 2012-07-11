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
package de.extra.xtt.util.tools;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import de.extra.xtt.gui.model.ProfilingTreeNode;
import de.extra.xtt.util.schema.SchemaElement;

/**
 * Klasse mit verschiedenen Einstellungen, Laden und Zugriff auf die Properties
 * und das ResourceBundle.
 * 
 * @author Beier
 */
public class Configurator {

	private static Logger logger = Logger.getLogger(Configurator.class);

	/**
	 * Dateiname der Propertiesdatei mit den Benutzereinstellungen
	 */
	public static final String PATH_PROPERTIES_USER = "xtt_user.properties";
	/**
	 * Dateiname der Propertiesdatei mit den Anmerkungen
	 */
	public static final String PATH_PROPERTIES_ANMERKUNGEN = "xtt_anmerkungen.properties";
	/**
	 * Dateiname der Propertiesdatei mit den Systemeinstellungen
	 */
	public static final String PATH_PROPERTIES_SYSTEM = "/xtt_system.properties";
	/**
	 * Dateiname der Propertiesdatei mit den Namespace-Angaben
	 */
	public static final String PATH_PROPERTIES_NAMESPACE = "/xtt_namespace.properties";
	/**
	 * Dateiname der Propertiesdatei mit den Infos zur Version
	 */
	public static final String PATH_PROPERTIES_VERSION = "/de/extra/xtt/version.properties";
	/**
	 * Name der Resource-Datei mit den Strings f�r die Oberfl�che
	 */
	public static final String NAME_RESBUNDLE_STRINGS = "strings";
	/**
	 * Dateiname des Schemas f�r die Profilkonfiguration
	 */
	public static final String PATH_TAILORING_SCHEMA = "/resource/TailoringSchema.xsd";

	private final ResourceBundle resStrings;
	private final Properties propertiesUser;
	private final Properties propertiesSystem;
	private final Properties propertiesNamespace;
	private final Properties propertiesVersion;
	private Properties propertiesAnmerkungen;
	private Properties propertiesAnmerkungenDefault;
	private final String pathTailoringSchemaCurrent;

	/**
	 * 
	 * Die beiden Haupt-Schematypen
	 * 
	 * @author Beier
	 * 
	 */
	public static enum SchemaType {
		REQUEST, RESPONSE
	}

	/**
	 * Bezeichnungen in der xtt_user.properties
	 * 
	 * @author Beier
	 * 
	 */
	public static enum PropBezeichnungUser {
		VERZEICHNIS_BASIS_SCHEMATA, DATEINAME_XSD_REQUEST, DATEINAME_XSD_RESPONSE, BAUM_HAUPTELEMENTE_EXPANDIERT, BAUM_REFRENZIERTE_ELEMENTE_EXPANDIERT
	}

	/**
	 * Bezeichnungen der xtt_system.properties
	 * 
	 * @author Beier
	 * 
	 */
	public static enum PropBezeichnungSystem {
		PATH_PROF_XML, PATH_PROF_SCHEMA, SCHEMA_ROOT_ELEMENT
	}

	// Zuordnung von Schematyp zu Namespace-URL
	private final Map<SchemaType, String> schemaNamespaceUrl = new HashMap<SchemaType, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(SchemaType.REQUEST,
					"http://www.extra-standard.de/namespace/request/1");
			put(SchemaType.RESPONSE,
					"http://www.extra-standard.de/namespace/response/1");
		}
	};

	private final List<String> schemaPrefixSorted = Arrays.asList("xreq",
			"xres", "xlog", "xcpt", "xplg", "xenc", "ds", "xcode");

	// Zuorndung von Namespace-Pr�fix zu Dateiname des Schemadatei
	private final Map<String, String> schemaNsBezeichnungDatei = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("xreq", "request");
			put("xres", "response");
			put("xcpt", "components");
			put("xlog", "logging");
			put("xplg", "plugins");
			put("xcode", "codelists");
			put("ds", "xmldsig");
			put("xenc", "xenc");
		}
	};

	/**
	 * Konstruktor mit Initialisierung der Properties und weiteren Einstellungen
	 * 
	 * @param propertiesUser
	 *            User-Properties
	 * @param propertiesSystem
	 *            System-Properties
	 * @param propertiesNamespace
	 *            Zuordnungen von Namespace-Pr�fix zu -URL und umgekehrt
	 * @param propertiesVersion
	 *            Angaben zur Version
	 * @param propertiesAnmerkungen
	 *            Properties mit den Anmerkungen zu den Elementen
	 * @param resBundleStrings
	 *            Oberfl�chentexte
	 * @param pathTailroingSchema
	 *            Pfad zum Schema f�r die Profilkonfiguration
	 */
	public Configurator(Properties propertiesUser, Properties propertiesSystem,
			Properties propertiesNamespace, Properties propertiesVersion,
			Properties propertiesAnmerkungen, ResourceBundle resBundleStrings,
			String pathTailroingSchema) {
		super();
		this.propertiesUser = propertiesUser;
		this.propertiesSystem = propertiesSystem;
		this.propertiesNamespace = propertiesNamespace;
		this.propertiesVersion = propertiesVersion;
		this.propertiesAnmerkungen = propertiesAnmerkungen;
		this.resStrings = resBundleStrings;
		this.pathTailoringSchemaCurrent = pathTailroingSchema;
		if (propertiesAnmerkungen != null) {
			this.propertiesAnmerkungenDefault = (Properties) propertiesAnmerkungen
					.clone();
		} else {
			this.propertiesAnmerkungenDefault = null;
		}
	}

	/**
	 * Statische Methode zum Laden einer Properties-Datei �ber das File-System
	 * 
	 * @param pathProperties
	 *            Pfad zur Properties-Datei
	 * @return Properties-Objekt mit den Werten aus der angegebenen Datei
	 * @throws ConfiguratorException
	 *             Falls beim Laden der Properties-Datei ein Fehler auftritt,
	 *             wird diese Ausnahme erzeugt
	 */
	public static Properties loadPropertiesFromFile(String pathProperties)
			throws ConfiguratorException {
		try {
			// File-Properties laden
			BufferedInputStream stream = new BufferedInputStream(
					new FileInputStream(pathProperties));
			Properties propertiesFile = new Properties();
			propertiesFile.load(stream);
			stream.close();
			return propertiesFile;
		} catch (Exception e) {
			throw new ConfiguratorException(
					"Fehler beim Laden der Property-Datei '" + pathProperties
							+ "'.", e);
		}
	}

	/**
	 * Statische Methode zum Laden von Properties aus einer Resourcen-Datei
	 * 
	 * @param pathProperties
	 *            Pfad zur Properties-Resource-Datei
	 * @return Properties-Objekt mit den Werten aus der angegebenen Datei
	 * @throws ConfiguratorException
	 *             Falls beim Laden der Properties-Datei ein Fehler auftritt,
	 *             wird diese Ausnahme erzeugt
	 */
	public static Properties loadPropertiesFromResource(String pathProperties)
			throws ConfiguratorException {
		try {
			// Resource-Properties laden
			InputStream is = Configurator.class
					.getResourceAsStream(pathProperties);
			Properties propertiesFile = new Properties();
			propertiesFile.load(is);
			is.close();
			return propertiesFile;
		} catch (Exception e) {
			throw new ConfiguratorException(
					"Fehler beim Laden der Property-Datei '" + pathProperties
							+ "'.", e);
		}
	}

	/**
	 * Statische Methoden zum Laden eines ResourceBundles
	 * 
	 * @param nameResBundle
	 *            Bezeichnung des ResourceBundles
	 * @return ResourceBundle-Objekt
	 * @throws ConfiguratorException
	 *             Falls beim Laden des ResourceBundles ein Fehler auftritt,
	 *             wird diese Ausnahme erzeugt
	 */
	public static ResourceBundle loadResourceBundle(String nameResBundle)
			throws ConfiguratorException {
		if (nameResBundle != null) {
			try {
				ResourceBundle resBundle = ResourceBundle.getBundle(
						nameResBundle, Locale.getDefault());
				return resBundle;
			} catch (MissingResourceException e) {
				throw new ConfiguratorException(
						"Fehler beim Laden des ResourceBundles '"
								+ nameResBundle + "'.", e);
			}
		} else {
			throw new ConfiguratorException(
					"Ung�ltiger Wert f�r ResourceBundle.");
		}

	}

	/**
	 * L�dt die Anmerkungen-Properties.
	 * 
	 * @throws ConfiguratorException
	 */
	public void reloadPropertiesAnmerkungen() throws ConfiguratorException {
		this.propertiesAnmerkungen = (Properties) propertiesAnmerkungenDefault
				.clone();
	}

	/**
	 * Erzeugt ein SchemaElement-Objekt aus einer Element-Bezeichnung (kann
	 * Namespace-Pr�fix enthalten)
	 * 
	 * @param elementBezeichnungMitNsPrefix
	 *            Element-Bezeichnung aus XML-Datei
	 * @return SchemaElement-Objekt mit dem Namen des Elements, dem
	 *         Namespace-Pr�fix und der -URL
	 */
	public SchemaElement getSchemaElement(String elementBezeichnungMitNsPrefix) {
		if (elementBezeichnungMitNsPrefix == null) {
			return null;
		} else {
			String nsPrefix = "";
			String elementName = elementBezeichnungMitNsPrefix;
			int indexTrenner = elementBezeichnungMitNsPrefix.indexOf(":");
			if (indexTrenner > 0) {
				nsPrefix = elementBezeichnungMitNsPrefix.substring(0,
						indexTrenner);
				elementName = elementBezeichnungMitNsPrefix
						.substring(indexTrenner + 1);
				return new SchemaElement(elementName,
						getPropertyNamespace(nsPrefix), nsPrefix);
			} else {
				// kein Pr�fix f�r Namespace angegeben
				return new SchemaElement(elementName, "", "");
			}
		}
	}

	/**
	 * Liefert die Liste aller Schema-Pr�fixe in sortierter Reihenfolge zur�ck
	 * 
	 * @return Liste mit Schema-Pr�fixen
	 */
	public List<String> getSchemaPrefixSorted() {
		return schemaPrefixSorted;
	}

	/**
	 * Erzeugt einen Dateinamen f�r das angegebene Verfahren und den
	 * Namespace-Pr�fix einer Schemadatei.
	 * 
	 * @param bezeichnungVerfahren
	 *            Bezeichnung des zu profilierenden Verfahrens
	 * @param nsPrefix
	 *            Namespace-Pr�fix
	 * @return Dateiname f�r eine Schemadatei (*.xsd)
	 */
	public String getDateinameFuerSchema(String bezeichnungVerfahren,
			String nsPrefix) {
		String dateiname = bezeichnungVerfahren + "_";
		if (schemaNsBezeichnungDatei.containsKey(nsPrefix)) {
			dateiname += schemaNsBezeichnungDatei.get(nsPrefix);
		} else {
			dateiname += nsPrefix;
		}
		dateiname += ".xsd";
		return dateiname;
	}

	/**
	 * Erzeugt einen Dateinamen f�r das angegebene Verfahren einer
	 * PDF-Dokumentation.
	 * 
	 * @param bezeichnungVerfahren
	 *            Bezeichnung des zu profilierenden Verfahrens
	 * @return Dateiname f�r eine PDF-Doku (*.pdf)
	 */
	public String getDateinameFuerDoku(String bezeichnungVerfahren) {
		String dateiname = "Spezifikation_" + bezeichnungVerfahren + ".pdf";
		return dateiname;
	}

	/**
	 * 
	 * Liefert einen Oberfl�chentext f�r den angegebenen Key zur�ck; falls nicht
	 * vorhanden, wird der Key selbst zur�ck gegeben.
	 * 
	 * @param key
	 *            Key f�r den Text
	 * @return der zum Key passende Text
	 */
	public String getResString(String key) {
		try {
			return resStrings.getString(key);
		} catch (Exception e) {
			logger.error("Fehler beim Zugriff auf den Resource-String '" + key
					+ "':", e);
			return key;
		}
	}

	/**
	 * Gibt den Pfad f�r das Schema f�r die Profilkonfiguration zur Validierung
	 * zur�ck.
	 * 
	 * @return Pfad zum Scheme
	 */
	public String getPathTailoringSchema() {
		return pathTailoringSchemaCurrent;
	}

	/**
	 * Liefert den Wert aus der User-Properties-Datei zum angegebenen Schl�ssel
	 * zur�ck.
	 * 
	 * @param bez
	 *            Schl�sselbezeichnung
	 * @return Wert aus der Properties-Datei
	 */
	public String getPropertyUser(PropBezeichnungUser bez) {
		return propertiesUser.getProperty(bez.toString());
	}

	/**
	 * Liefert den Wert aus der System-Properties-Datei zum angegebenen
	 * Schl�ssel zur�ck.
	 * 
	 * @param bez
	 *            Schl�sselbezeichnung
	 * @return Wert aus der Properties-Datei
	 */
	public String getPropertySystem(PropBezeichnungSystem bez) {
		return propertiesSystem.getProperty(bez.toString());
	}

	/**
	 * Liefert den Wert aus der Namespace-Properties-Datei zum angegebenen
	 * Schl�ssel zur�ck. Zu einer URL wird das passende Pr�fix, zu einem Pr�fix
	 * die passende URL zur�ckgegeben.
	 * 
	 * @param bez
	 *            Schl�sselbezeichnung
	 * @return Wert aus der Properties-Datei
	 */
	public String getPropertyNamespace(String bez) {
		return propertiesNamespace.getProperty(bez.toString());
	}

	/**
	 * Gibt den Anmerkungstext f�r die Verwendung des angegebenen Knoten zur�ck.
	 * 
	 * @param node
	 *            Knoten, f�r den der Anmerkungstext bestimmt werden soll
	 * @return Anmerkungstext zur Verwendung
	 */
	public String getAnmerkungVerwendung(ProfilingTreeNode node) {
		String anmerkung = "";
		if ((node != null) && (node.getParent() != null)
				&& (propertiesAnmerkungen != null)) {
			SchemaElement seParent = node.getParent().getSchemaElement();
			SchemaElement seCurrNode = node.getSchemaElement();
			String key = seParent.getNsPrefix() + "_" + seParent.getName()
					+ "_" + seCurrNode.getNsPrefix() + "_"
					+ seCurrNode.getName();
			anmerkung = propertiesAnmerkungen.getProperty(key, "");
		}
		return anmerkung;
	}

	/**
	 * Setzt einen neuen Anmerkungstext zur Verwendung des angegegebenen
	 * SchemaElements.
	 * 
	 * @param schemaElement
	 *            SchemaElement, f�r das der Anmerkungstext gesetzt werden soll
	 * @param schemaElementParent
	 *            SchemaElement vom Vaterknoten, in dem das aktuelle Element
	 *            verwendet wird
	 * @param text
	 *            Neuer Anmerkungstext
	 */
	public void setAnmerkungVerwendung(SchemaElement schemaElement,
			SchemaElement schemaElementParent, String text) {
		if ((schemaElement != null) && (propertiesAnmerkungen != null)) {
			String key = schemaElementParent.getNsPrefix() + "_"
					+ schemaElementParent.getName() + "_"
					+ schemaElement.getNsPrefix() + "_"
					+ schemaElement.getName();
			propertiesAnmerkungen.setProperty(key, text);
		}
	}

	/**
	 * Gibt den allgemeinen Anmerkungstext f�r den angegebenen Knoten zur�ck.
	 * 
	 * @param node
	 *            Knoten, f�r den der Anmerkungstext bestimmt werden soll
	 * @return Allg. Anmerkungstext
	 */
	public String getAnmerkungAllgemein(ProfilingTreeNode node) {
		String anmerkung = "";
		if ((node != null) && (propertiesAnmerkungen != null)) {
			String key = node.getSchemaElement().getNsPrefix() + "_"
					+ node.getSchemaElement().getName();
			anmerkung = propertiesAnmerkungen.getProperty(key, "");
		}
		return anmerkung;
	}

	/**
	 * Setzt einen neuen allgemeinen Anmerkungstext f�r das angegegebene
	 * SchemaElement.
	 * 
	 * @param schemaElement
	 *            SchemaElement, f�r das der Anmerkungstext gesetzt werden soll
	 * @param text
	 *            Neuer Anmerkungstext
	 */
	public void setAnmerkungAllgemein(SchemaElement schemaElement, String text) {
		if ((schemaElement != null) && (propertiesAnmerkungen != null)) {
			String key = schemaElement.getNsPrefix() + "_"
					+ schemaElement.getName();
			propertiesAnmerkungen.setProperty(key, text);
		}
	}

	/**
	 * Liefert den Versions-String zur�ck ([Major].[Minor] ([BuildDate]))
	 * 
	 * @return Versions-String
	 */
	public String getVersion() {
		return propertiesVersion.getProperty("build.number.major") + "."
				+ propertiesVersion.getProperty("build.number.minor") + " ("
				+ propertiesVersion.getProperty("build.date") + ")";
	}

	/**
	 * Liefert die Namespace-URL zum angegebenen Schema zur�ck.
	 * 
	 * @param schema
	 *            Schematyp
	 * @return Namespace-URL
	 */
	public String getSchemaTypeNsUrl(SchemaType schema) {
		if (schemaNamespaceUrl.containsKey(schema)) {
			return schemaNamespaceUrl.get(schema);
		} else {
			return "";
		}
	}
}
