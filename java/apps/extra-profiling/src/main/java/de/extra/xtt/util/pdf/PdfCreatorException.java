package de.extra.xtt.util.pdf;

/**
 * Exception für die Verwendung im PdfCreator-Objekt.
 * 
 * @author Beier
 *
 */
public class PdfCreatorException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public PdfCreatorException(String message, Throwable cause) {
		super(message, cause);
	}
}
