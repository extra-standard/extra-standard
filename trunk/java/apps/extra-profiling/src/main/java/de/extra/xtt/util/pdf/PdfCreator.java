package de.extra.xtt.util.pdf;

import com.sun.xml.xsom.XSSchemaSet;

/**
 * Schnittstelle f�r das Generieren einer PDF-Dokumentation
 * 
 * @author Beier
 * 
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
	 *             Ausnahme wird erzeugt, falls beim Ausf�hren der Methode ein beliebiger Fehler auftritt
	 */
	public void erzeugePdfDoku(String filePathPdf, XSSchemaSet xmlSchema, String bezVerfahren)
			throws PdfCreatorException;

}
