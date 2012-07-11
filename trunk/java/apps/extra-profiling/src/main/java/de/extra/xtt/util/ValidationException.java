package de.extra.xtt.util;

/**
 * Exception f�r die Verwendung bei der Profilierung vom XML-Dokumenten
 * 
 * @author Beier
 *
 */
public class ValidationException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ValidationException(String meldung) {
		super(meldung);
	}
}
