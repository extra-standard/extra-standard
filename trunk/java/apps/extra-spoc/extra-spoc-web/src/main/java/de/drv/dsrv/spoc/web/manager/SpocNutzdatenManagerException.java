package de.drv.dsrv.spoc.web.manager;

/**
 * Ausnahme f&uuml;r Fehler bei der Verarbeitung der eXTra-Nutzdaten.
 */
public class SpocNutzdatenManagerException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 *            Fehlernachricht
	 */
	public SpocNutzdatenManagerException(final String message) {
		super(message);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 *            Fehlernachricht
	 * @param cause
	 *            Fehlergrund
	 */
	public SpocNutzdatenManagerException(final String message, final Throwable cause) {
		super(message, cause);
	}
}