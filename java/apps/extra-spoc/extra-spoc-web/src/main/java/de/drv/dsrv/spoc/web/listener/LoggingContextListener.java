package de.drv.dsrv.spoc.web.listener;

import java.net.URL;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

/**
 * Listener zum Initialisieren der Commons Logging Factory mit dem Logger von
 * log4j.
 */
public final class LoggingContextListener implements ServletContextListener {

	@Resource(name = "url/SPoC_log4j.propertiesRef")
	private URL log4jResource;

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(final ServletContextEvent event) {
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	@Override
	public void contextInitialized(final ServletContextEvent event) {

		PropertyConfigurator.configure(this.log4jResource);

		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.Log4JLogger");

	}
}
