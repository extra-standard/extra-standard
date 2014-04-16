package de.drv.dsrv.spoc.commons.zertifikat;

import javax.servlet.http.HttpServletRequest;

/**
 * Definiert die Methoden fuer die Pruefung der Betriebsnummer.
 */
public interface BetriebsnummerHelper {

	/**
	 * Ermittelt die Betriebsnummer, welche im SPoC ermittelt und ueber einen
	 * Request Parameter ans Fachverfahren weitergeleitet wird aus dem
	 * uebergebenen HTTP-Servlet-Request.
	 * 
	 * @param request
	 *            das HTTP-Servlet-Request zum Zugriff auf die Request Parameter
	 * @return die aus dem HTTP-Servlet-Request aus einem Request Parameter
	 *         ermittelte Betriebsnummer, sofern diese vorhanden ist vorhanden;
	 *         andernfalls <code>null</code>
	 */
	String ermittleBetriebsnummer(final HttpServletRequest request);
}
