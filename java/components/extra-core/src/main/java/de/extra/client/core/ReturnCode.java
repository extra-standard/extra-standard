/**
 *
 */
package de.extra.client.core;

/**
 * Returncodes für Batchanwendungen.
 * 
 * @author Thorsten Vogel
 * @version $Id: ReturnCode.java 538 2012-09-05 09:48:23Z
 *          thorstenvogel@gmail.com $
 */
public enum ReturnCode {

	/**
	 * Programm ist ohne Fehler oder Unregelmaessigkeiten gelaufen.
	 */
	SUCCESS(0),

	/**
	 * Unregelmaessigkeiten, die aber das Ziel nicht beeinflussen.
	 */
	WARNING(16),

	/**
	 * Fehler oder Stoerung auf technischer Ebene.
	 */
	TECHNICAL(32),

	/**
	 * Fachlicher Fehler.
	 */
	BUSINESS(64);

	private final int code;

	private ReturnCode(final int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static ReturnCode resolve(final int status) {
		ReturnCode[] values = ReturnCode.values();
		for (int i = 0; i < values.length; i++) {
			if (status == values[i].getCode()) {
				return values[i];
			}
		}
		throw new IllegalArgumentException("Zu Status " + status
				+ " ist kein ReturnCode zugeordnet.");
	}

}
