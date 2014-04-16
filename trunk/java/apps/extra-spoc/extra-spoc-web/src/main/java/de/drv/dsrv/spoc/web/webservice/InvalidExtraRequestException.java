package de.drv.dsrv.spoc.web.webservice;

/**
 * Wird geworfen, wenn der Request kein g&uuml;ltiger eXTra-Request ist.
 */
public class InvalidExtraRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 *            detaillierte Beschreibung, warum der Request ung&uuml;ltig ist
	 */
	public InvalidExtraRequestException(final String message) {
		super(message);
	}
}
