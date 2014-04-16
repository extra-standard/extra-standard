package de.drv.dsrv.spoc.commons.compression;

/**
 * Fehler, der im Zusammenhang mit der Komprimierung und Dekomprimierung von
 * Daten auftritt.
 */
public class CompressException extends Exception {

	private static final long serialVersionUID = -6478047043992665915L;

	/**
	 * Konstruktor.
	 * 
	 * @param cause
	 *            Fehlergrund
	 */
	public CompressException(final Throwable cause) {
		super(cause);
	}
}
