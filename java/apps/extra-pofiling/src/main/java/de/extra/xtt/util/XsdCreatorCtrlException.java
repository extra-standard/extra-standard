package de.extra.xtt.util;

/**
 * Exception für die Verwendung in der XsdCreatorCtrl-Klasse
 * 
 * @author Beier
 *
 */
public class XsdCreatorCtrlException extends Exception {

	private static final long serialVersionUID = 1L;


	public XsdCreatorCtrlException(String message) {
		super(message);
	}

	public XsdCreatorCtrlException(String message, Throwable cause) {
		super(message, cause);
	}

}
