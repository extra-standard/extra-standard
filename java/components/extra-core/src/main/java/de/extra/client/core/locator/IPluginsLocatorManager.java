package de.extra.client.core.locator;

import de.extra.client.core.plugin.IConfigPlugin;
import de.extra.client.core.plugin.IDataPlugin;
import de.extra.client.core.plugin.IOutputPlugin;
import de.extra.client.core.plugin.IResponseProcessPlugin;

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