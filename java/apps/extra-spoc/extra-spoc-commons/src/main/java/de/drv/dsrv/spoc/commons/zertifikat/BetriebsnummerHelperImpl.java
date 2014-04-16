package de.drv.dsrv.spoc.commons.zertifikat;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementiert die Methoden fuer die Pruefung der Betriebsnummer.
 */
public class BetriebsnummerHelperImpl implements BetriebsnummerHelper {

	/**
	 * Parameter-Name der Betriebsnummer.
	 */
	protected static final String BETRIEBSNUMMER_PARAM = "bbnrZertifikat"; // NOPMD

	@Override
	public String ermittleBetriebsnummer(final HttpServletRequest request) {
		return request.getParameter(BETRIEBSNUMMER_PARAM);
	}
}
