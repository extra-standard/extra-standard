package de.extra.client.core.locator;

import de.extrastandard.api.plugin.IConfigPlugin;
import de.extrastandard.api.plugin.IDataPlugin;
import de.extrastandard.api.plugin.IOutputPlugin;
import de.extrastandard.api.plugin.IResponseProcessPlugin;

public interface IPluginsLocatorManager {

	/**
	 * Liefert der in der Konfiguration unter dem Schlüssel plugins.dataplugin
	 * definierten Bean
	 * 
	 * @return
	 */
	IDataPlugin getConfiguratedDataPlugin();

	/**
	 * Liefert der in der Konfiguration unter dem Schlüssel plugins.dataplugin
	 * definierten Bean
	 * 
	 * @return
	 */
	IOutputPlugin getConfiguratedOutputPlugin();

	/**
	 * Liefert der in der Konfiguration unter dem Schlüssel plugins.dataplugin
	 * definierten Bean
	 * 
	 * @return
	 */
	IConfigPlugin getConfiguratedConfigPlugin();

	/**
	 * Liefert der in der Konfiguration unter dem Schlüssel plugins.dataplugin
	 * definierten Bean
	 * 
	 * @return
	 */
	IResponseProcessPlugin getConfiguratedResponsePlugin();

}