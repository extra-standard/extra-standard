package de.extra.xtt.util;

/**
 * Exception für die Verwendung im ExtraTailoring-Objekt
 * 
 * @author Beier
 * 
 */
public class ExtraTailoringException extends Exception {

	private static final long serialVersionUID = 9206166494265472743L;

	public ExtraTailoringException(String message) {
		super(message);
	}

	public ExtraTailoringException(String message, Throwable cause) {
		super(message, cause);
	}

}
