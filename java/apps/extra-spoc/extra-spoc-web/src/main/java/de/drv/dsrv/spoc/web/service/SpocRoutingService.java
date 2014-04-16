package de.drv.dsrv.spoc.web.service;

import java.net.URI;

/**
 * Stellt Funktionen zum Routing eines eingehenden Requests auf das
 * entsprechende Fachverfahren zur Verf&uuml;gung.
 */
public interface SpocRoutingService {

	/**
	 * Ermittelt das Ziel-Fachverfahren f&uuml;r einen eXTra-Request durch die
	 * Auswertung bestimmter Parameter des Requests.
	 *
	 * @param version
	 *            der Wert des <code>version</code> Attributs des
	 *            <code>Transport</code> Elements des eXTra Requests
	 * @param profile
	 *            der Wert des <code>profile</code> Attributs des
	 *            <code>Transport</code> Elements des eXTra Requests
	 * @param procedure
	 *            der Wert des <code>Procedure</code> Elements aus dem
	 *            <code>TransportHeader</code> des eXTra Requests
	 * @param dataType
	 *            der Wert des <code>DataType</code> Elements aus dem
	 *            <code>TransportHeader</code> des eXTra Requests
	 *
	 * @return die <code>URI</code> des Fachverfahrens, das f&uuml;r die
	 *         &uuml;bergebenen Parameter konfiguriert ist
	 */
	URI getFachverfahrenUrl(String version, String profile, String procedure, String dataType);
}
