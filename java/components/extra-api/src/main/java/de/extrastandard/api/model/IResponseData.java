package de.extrastandard.api.model;

/**
 * Liefert Ergebnisse einer Anfrage/Übertragung zurück.
 * 
 * @author Leonid Potap
 * @version $Id$
 */
public interface IResponseData {

	/**
	 * requestId der Ursprung Anfrage
	 */
	String getRequestId();

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
