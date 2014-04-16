package de.drv.dsrv.spoc.commons.mtomdata;

/**
 * Repraesentiert eine Ausnahme beim Zugriff auf die Datenbanktabelle DFUEMAIN.
 */
public class MtomDataDaoException extends Exception {

	private static final long serialVersionUID = -3187225437848628567L;

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 *            die Nachricht fuer diese Ausnahme
	 */
	public MtomDataDaoException(final String message) {
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
	public MtomDataDaoException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
