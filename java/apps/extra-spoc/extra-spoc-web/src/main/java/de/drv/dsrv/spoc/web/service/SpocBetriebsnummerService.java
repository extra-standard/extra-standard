package de.drv.dsrv.spoc.web.service;

import java.net.URI;

/**
 * Definiert Methoden zum Lesen und Weitergeben der Betriebsnummer.
 */
public interface SpocBetriebsnummerService {

	/**
	 * Extrahiert die Betriebsnummer aus dem aktuellen Request und f&uuml;gt sie
	 * der &uuml;bergebenen <code>uri</code> als Parameter hinzu.
	 * 
	 * @param fachverfahrenUrl
	 *            die URL, der die <code>betriebsnummer</code> hinzugef&uuml;gt
	 *            werden soll; darf nicht <code>null</code> sein
	 * 
	 * @return die aus dem Request extrahierte Betriebsnummer, oder
	 *         <code>null</code>, wenn keine Betriebsnummer extrahiert werden
	 *         konnte
	 */
	URI addBetriebsnummerFromRequestToUrl(final URI fachverfahrenUrl);
}
