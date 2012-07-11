package de.extra.xtt.util.tools;

/**
 * Exception für die Verwendung in der Configurator-Klasse.
 * 
 * @author Beier
 *
 */
public class ConfiguratorException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConfiguratorException(String message) {
		super(message);
	}

	public ConfiguratorException(String message, Throwable cause) {
		super(message, cause);
	}
}
