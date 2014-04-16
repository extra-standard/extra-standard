package de.drv.dsrv.utility.spring.common;

import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class StageAwarePropertyPlaceholderConfigurer extends
		PropertyPlaceholderConfigurer {

	private final transient String stage;

	/**
	 * Konstruktor.
	 */
	public StageAwarePropertyPlaceholderConfigurer(final String stage) {
		super();
		this.stage = stage;
	}

	/**
	 * Ausgelagerte Methode, um die Testbarkeit über Unit Tests sicher zu
	 * stellen.
	 * 
	 * @see PropertyPlaceholderConfigurer#resolvePlaceholder(String, Properties)
	 */
	protected String resolvePlaceholderFromSuper(final String placeholder,
			final Properties props) {
		return super.resolvePlaceholder(placeholder, props);
	}

	@Override
	protected String resolvePlaceholder(final String placeholder,
			final Properties props) {
		// Ermittle Wert speziell für die auf dem Server eingestellte Stage
		String wert = resolvePlaceholderFromSuper(placeholder + "."
				+ this.stage, props);

		// Prüfe, ob der Wert für die Stageun gültig ist
		if (wert == null) {
			// Ermittle den Wert ohne Stage
			wert = resolvePlaceholderFromSuper(placeholder, props);
		}

		// Geben Wert zurück else {
		return wert;
	}
}