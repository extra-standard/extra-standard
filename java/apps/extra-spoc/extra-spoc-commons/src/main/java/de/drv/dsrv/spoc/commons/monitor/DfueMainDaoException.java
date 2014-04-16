package de.drv.dsrv.spoc.commons.monitor;

/**
 * Repraesentiert eine Ausnahme beim Zugriff auf die Datenbanktabelle DFUEMAIN.
 */
public class DfueMainDaoException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 *            die Nachricht fuer diese Ausnahme
	 */
	public DfueMainDaoException(final String message) {
		super(message);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 *            die Nachricht fuer diese Ausnahme
	 * @param cause
	 *            die Ursache fuer das Auftreten der Ausnahme
	 */
	public DfueMainDaoException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
