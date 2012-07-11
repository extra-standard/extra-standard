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

/**
 * Diese Klasse ist ein Container für einen Eintrag im PDF-Dokument, der im
 * Inhaltsverzeichnis inkl. Seitennummer und Referenz erscheinen soll.
 * 
 * @author Beier
 */
public class ContPdfEntry {

	private final ContPdfEntry parentEntry;
	private final String bezeichnung;
	private final String destination;
	private int pageNumber;

	/**
	 * Konstruktor zu Initialisierung des Dateninhalts.
	 * 
	 * @param parentEntry
	 *            �bergeordneter Eintrag (optional)
	 * @param bezeichnung
	 *            Bezeichnung des Eintrags
	 * @param destination
	 *            Bezeichnung der Referenz
	 * @param pageNumber
	 *            Seitennummer des Eintrags
	 */
	public ContPdfEntry(ContPdfEntry parentEntry, String bezeichnung,
			String destination, int pageNumber) {
		this.parentEntry = parentEntry;
		this.bezeichnung = bezeichnung;
		this.destination = destination;
		this.pageNumber = pageNumber;
	}

	/**
	 * Gibt den �bergeordneten Eintrag des aktuellen Eintrags zur�ck
	 * 
	 * @return �bergeordneter Eintrag, falls vorhanden; sonst <code>null</code>
	 */
	public ContPdfEntry getParentEntry() {
		return parentEntry;
	}

	/**
	 * Gibt die Bezeichnung des Eintrags zur�ck
	 * 
	 * @return Bezeichnung
	 */
	public String getBezeichnung() {
		return bezeichnung;
	}

	/**
	 * Gibt diie Referenz des Eintrags zur�ck
	 * 
	 * @return Referenz im PDF-Dokument
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * Setzt die Seitenzahl f�r den Eintrag
	 * 
	 * @param pageNumber
	 *            Seitenzahl des Eintrags im PDF-Dokument
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * Gitb die Seitenzahl des Eintrags zur�ck
	 * 
	 * @return Seitenzahl im PDF-Dokument
	 */
	public int getPageNumber() {
		return pageNumber;
	}
}
