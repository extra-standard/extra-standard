package de.extrastandard.api.model.content;

/**
 * Liefert Ergebnisse Übertragung eines Datensatzes einer Anfrage/Übertragung
 * zurück.
 * 
 * @author Leonid Potap
 * @version $Id$
 */
public interface ISingleResponseData {

	/**
	 * requestId der Ursprung Anfrage
	 */
	String getRequestId();

	/**
	 * responseId der Antwort
	 */
	String getResponseId();

	/**
	 * @return Liefert Return-Code der Nachricht.
	 */
	String getReturnCode();

	/**
	 * Liefert Return-Text der Nachricht, wenn vorhanden.
	 * 
	 * @return
	 */
	String getReturnText();

}
