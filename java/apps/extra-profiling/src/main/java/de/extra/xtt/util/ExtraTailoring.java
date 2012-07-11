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

import java.util.Map;

import org.w3c.dom.Document;

import com.sun.xml.xsom.XSSchemaSet;

import de.extra.xtt.gui.model.ProfilingTreeNode;
import de.extra.xtt.util.tools.Configurator;

/**
 * Schnittstelle für die Erzeugung eines verfahrensspezifischen Extra-Schemas.
 * 
 * @author Mueller
 */
public interface ExtraTailoring {

	/**
	 * Methode zum erzeugen eines profilierten Extra-Schemas inkl. Hauptschema
	 * und verkn�pfte Schemata
	 * 
	 * @param ssQuellSchema
	 *            Das vollst�ndige Quellschema, z.B. eXTra Request 1.1
	 * @param pofilXml
	 *            Die Profilkonfiguration im XML-Format mit der Beschreibung der
	 *            ben�tigten Element
	 * @param bezeichnungVerfahren
	 *            Kurzbezeichnung des zu profilierenden Verfahren (wird f�r die
	 *            Dateinamen der Schemadateien verwendet)
	 * @return Die profilierten Schemadateien im DOM-Format
	 * @throws ExtraTailoringException
	 *             Falls ein Fehler auftritt, wird diese Ausnahme erzeugt
	 */
	public Map<String, Document> erzeugeProfiliertesExtraSchema(
			XSSchemaSet ssQuellSchema, Document pofilXml,
			String bezeichnungVerfahren) throws ExtraTailoringException;

	/**
	 * Methode zum Erzeugen einer Profil-Konfiguration
	 * 
	 * @param schemaType
	 *            Angabe, welches Schema profiliert werden soll (Basisschema)
	 * @param rootNode
	 *            Wurzelelement aus dem Haupt-Profilbaum, der die selektierten
	 *            Knoten enth�lt
	 * @param rootNodeRef
	 *            Wurzelelement aus dem Profilbaum mit den referenzierten
	 *            Elementen
	 * @param targetNamespace
	 *            TargetNamespace f�r das Schema; wird in der Konfiguration
	 *            gespeichert
	 * @param bezVerfahrenKurz
	 *            Kurzbezeichnung des zu profilierenden Verfahrens; wird in der
	 *            Konfiguration gespeichert
	 * @param bezVerfahren
	 *            Bezeichnung des zu profilierenden Verfahrens; wird in der
	 *            Konfiguration gespeichert
	 * @return Profilkonfiguration im DOM-Format
	 * @throws ExtraTailoringException
	 *             Falls ein Fehler auftritt, wird diese Ausnahme erzeugt
	 */
	public Document erzeugeProfilkonfiguration(
			Configurator.SchemaType schemaType, ProfilingTreeNode rootNode,
			ProfilingTreeNode rootNodeRef, String targetNamespace,
			String bezVerfahrenKurz, String bezVerfahren)
			throws ExtraTailoringException;
}
