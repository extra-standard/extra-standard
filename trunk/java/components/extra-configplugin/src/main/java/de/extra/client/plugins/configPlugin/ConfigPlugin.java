package de.extra.client.plugins.configPlugin;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.extra.client.core.ExtraClient;
import de.extra.client.core.model.ConfigFileBean;
import de.extra.client.core.plugin.IConfigPlugin;
import de.extra.client.plugins.configPlugin.controller.ConfigPluginController;

public class ConfigPlugin implements IConfigPlugin {

	/**
	 * Pfad und Dateiname in der die SpringConfig.xml liegt
	 */

	private static String SPRING_XML_FILE_PATH = "ConfigPluginConfig.xml";

	/**
	 * Dateipfad der log4jProperties
	 */

	private static final String LOG_4_J_FILE = "log4j.properties";

	private static Logger logger = Logger.getLogger(ExtraClient.class);

	/**
	 * 
	 * Basismethode mit der die Verarbeitung der Konfigurationsdatei aufgerufen
	 * wird.
	 * 
	 */

	public ConfigFileBean getConfigFile() {

		// Laden des Log4Files

		PropertyConfigurator.configureAndWatch(LOG_4_J_FILE);
		logger.info("Start des Auslesens der Config-Datei");

		// Spring Beans laden.
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				SPRING_XML_FILE_PATH);

		// Instanziieren der Bean

		ConfigPluginController controller = (ConfigPluginController) applicationContext
				.getBean("configController");

		// Aufruf der Verarbeitung

		return controller.processConfigFile();
	}

}
