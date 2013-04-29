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
package de.extra.xtt.util;

import java.io.File;
import java.util.Map;

import org.w3c.dom.Document;

import de.extra.xtt.gui.model.ProfilingTreeModel;

/**
 * Controller-Klasse für die Anwendung XSD-Creator. Stellt alle notwendigen
 * Funktionen für die Oberfl�che breit.
 * 
 * @author Beier
 */
public interface XsdCreatorCtrl {

	/**
	 * Erzeugt das TreeModel f�r das Request-Schema
	 * 
	 * @return TreeModel mit allen Elementen des Request-Schemas
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausf�hren der Methode ein
	 *             beliebiger Fehler auftritt
	 */
	public abstract ProfilingTreeModel createTreeModelForRequest()
			throws XsdCreatorCtrlException;

	/**
	 * Erzeugt das TreeModel f�r das Response-Schema
	 * 
	 * @return TreeModel mit alle Elementen des Response-Schemas
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausf�hren der Methode ein
	 *             beliebiger Fehler auftritt
	 */
	public abstract ProfilingTreeModel createTreeModelForResponse()
			throws XsdCreatorCtrlException;

	/**
	 * Erzeugt das TreeModel f�r die aktuell geladene Konfiguration
	 * 
	 * @return TreeModel f�r das aktuell geladene Schema
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausf�hren der Methode ein
	 *             beliebiger Fehler auftritt
	 */
	public abstract ProfilingTreeModel createTreeModelForCurrentConfig()
			throws XsdCreatorCtrlException;

	/**
	 * L�dt die Profilkonfiguration aus der �bergebenen Datei inkl. passendem
	 * Schema
	 * 
	 * @param fileConfig
	 *            Datei mit einer XML-Profilkonfiguration
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausf�hren der Methode ein
	 *             beliebiger Fehler auftritt
	 */
	public abstract void setXmlFileConfig(File fileConfig)
			throws XsdCreatorCtrlException;

	/**
	 * Erzeugt eine PDF-Dokumentation f�r das aktuelle profilierte Schema
	 * 
	 * @param pathFile
	 *            Pfad f�r die zu erstellende PDF-Datei
	 * @param filePathSchema
	 *            Pfad der Schemadatei
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausf�hren der Methode ein
	 *             beliebiger Fehler auftritt
	 */
	public abstract void createPdfDoku(String pathFile, String filePathSchema)
			throws XsdCreatorCtrlException;

	/**
	 * Liefert den aktuellen TargetNamespace aus der Profilkonfiguration
	 * 
	 * @return TargetNamespace
	 */
	public abstract String getTargetNamespace();

	/**
	 * Gibt an, ob aktuell eine Profilkonfiguration geladen ist
	 * 
	 * @return <code>true</code>, wenn Profilkonfiguration geladen; sonst
	 *         <code>false</code>
	 */
	public abstract boolean isDocXmlLoaded();

	/**
	 * F�r die �bergebenen TreeModels wird eine Profilkonfiguration inkl.
	 * TargetNamespace und und Kurzbezeichnung f�r das Verfahren erzeugt.
	 * 
	 * @param treeModelMain
	 *            TreeModel mit den Hauptelementen
	 * @param treeModelAdd
	 *            TreeModel mit dne referenzierten Elementen
	 * @param targetNamespace
	 *            TargetNamespace f�r das profilierte Schema
	 * @param bezVerfahrenKurz
	 *            Kurzbezeichnung des Verfahrens; wird f�r die Dateinamen
	 *            verwendet
	 * @param bezVerfahren
	 *            Bbezeichnung des Verfahrens
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausf�hren der Methode ein
	 *             beliebiger Fehler auftritt
	 */
	public abstract void createXmlProf(ProfilingTreeModel treeModelMain,
			ProfilingTreeModel treeModelAdd, String targetNamespace,
			String bezVerfahrenKurz, String bezVerfahren)
			throws XsdCreatorCtrlException;

	/**
	 * Erzeugt das profilierte Schema f�r die aktuelle Profilkonfiguration;
	 * Ergebnis k�nnen ein oder mehrere Schemadateien sein.
	 * 
	 * @return Map mit dem Namespace-Pr�fix als Key und dem dazugeh�rigen
	 *         Schemadokument als Value
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausf�hren der Methode ein
	 *             beliebiger Fehler auftritt
	 */
	public abstract Map<String, Document> createSchemaProf()
			throws XsdCreatorCtrlException;

	/**
	 * Liefert den Pfad zur Datei des Quellschemas f�r die aktuelle
	 * Konfiguration.
	 * 
	 * @return Dateipfad vom Quellschema
	 */
	public abstract String getPathQuellSchema();

	/**
	 * Liefert den Pfad zur Datei der aktuell geladenen Profilkonfiguration.
	 * 
	 * @return Pfad der Profilkonfiguration
	 */
	public abstract String getPathXmlConfig();

	/**
	 * Speichert die aktuelle Profilkonfiguration.
	 * 
	 * @param path
	 *            Pfad und Name f�r die neue Datei
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausf�hren der Methode ein
	 *             beliebiger Fehler auftritt
	 */
	public abstract void saveXmlConfig(String path)
			throws XsdCreatorCtrlException;

	/**
	 * Speichert die �bergebenen Schema-Dateien im angegebenen Verzeichnis. Die
	 * Dateinamen werden automatisch aus dem Namespace-Pr�fix und der
	 * Kurzbezeichnung des Verfahrens aus der Konfiguration gebildet.
	 * 
	 * @param path
	 *            Verzeichnis, in dem die Schemadateien gespeichert werden
	 * @param docXsd
	 *            Map mit dem Namespace-Pr�fix als Key und dem dazugeh�rigen
	 *            Schemadokument als Value
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausf�hren der Methode ein
	 *             beliebiger Fehler auftritt
	 */
	public abstract void saveXsdSchema(String path, Map<String, Document> docXsd)
			throws XsdCreatorCtrlException;

	/**
	 * Liefert die Kurzbezeichnung des Verfahrens aus der aktuellen
	 * Profilkonfiguration.
	 * 
	 * @return Kurzbezeichnung des Verfahrens
	 */
	public abstract String getBezeichnungKurzVerfahren();

	/**
	 * Liefert die Bezeichnung des Verfahrens aus der aktuellen
	 * Profilkonfiguration.
	 * 
	 * @return Bezeichnung des Verfahrens
	 */
	public abstract String getBezeichnungVerfahren();

	/**
	 * Pr�ft den �bergebenen String, ob es ein g�ltiger Dateiname ist (nur
	 * Buchstaben, Ziffern und '_')
	 * 
	 * @param bezKurzVerfahren
	 *            Zu pr�fender String
	 * @return <code>true</code>, falls Bezeichnung g�ltig ist, sonst
	 *         <code>false</code>
	 */
	public abstract boolean validateBezeichnungKurzVerfahren(
			String bezKurzVerfahren);

}