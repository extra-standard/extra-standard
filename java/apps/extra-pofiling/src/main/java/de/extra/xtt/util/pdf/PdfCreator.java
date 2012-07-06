package de.extra.xtt.util.pdf;

import com.sun.xml.xsom.XSSchemaSet;

/**
 * Schnittstelle für das Generieren einer PDF-Dokumentation
 * 
 * @author Beier
 * 
 */
public interface PdfCreator {

	/**
	 * Für das angegebene Schema wird eine Dokumentation im PDF-Format erzeugt.
	 * 
	 * @param filePathPdf
	 *            Dateipfad zum Speichern der PDF-Datei
	 * @param xmlSchema
	 *            SchemaSet, für das die Dokumenttaion erzeugt werden soll
	 * @param bezVerfahren
	 *            Bezeichnung des Verfahrens
	 * @throws PdfCreatorException
	 *             Ausnahme wird erzeugt, falls beim Ausführen der Methode ein beliebiger Fehler auftritt
	 */
	public void erzeugePdfDoku(String filePathPdf, XSSchemaSet xmlSchema, String bezVerfahren)
			throws PdfCreatorException;

}
