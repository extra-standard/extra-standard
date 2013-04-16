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
package de.extra.xtt.util.pdf;

import com.sun.xml.xsom.XSSchemaSet;

/**
 * Schnittstelle für das Generieren einer PDF-Dokumentation
 * 
 * @author Beier
 */
public interface PdfCreator {

	/**
	 * F�r das angegebene Schema wird eine Dokumentation im PDF-Format erzeugt.
	 * 
	 * @param filePathPdf
	 *            Dateipfad zum Speichern der PDF-Datei
	 * @param xmlSchema
	 *            SchemaSet, f�r das die Dokumenttaion erzeugt werden soll
	 * @param bezVerfahren
	 *            Bezeichnung des Verfahrens
	 * @throws PdfCreatorException
	 *             Ausnahme wird erzeugt, falls beim Ausf�hren der Methode ein
	 *             beliebiger Fehler auftritt
	 */
	public void erzeugePdfDoku(String filePathPdf, XSSchemaSet xmlSchema,
			String bezVerfahren) throws PdfCreatorException;

}
