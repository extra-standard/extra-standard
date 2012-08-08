package de.extra.client.core.locator;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import de.extra.client.core.plugin.IConfigPlugin;
import de.extra.client.core.plugin.IDataPlugin;
import de.extra.client.core.plugin.IOutputPlugin;


/**
 * Sucht nach der in der Konfiguration definierten Plugins
 * @author evpqq5
 *
 */
@Named("plugInsLocatorManager")
public class PlugInsLocatorManager {
	
	
    private static final Logger LOG = Logger.getLogger(PlugInsLocatorManager.class);

	
	@Inject
	Map<String, IDataPlugin> dataPlugInMap;
	
	@Value("${plugins.dataplugin}")
	String dataPlugBeanName;
	
	@Inject
	Map<String, IConfigPlugin> configPlugInMap;
	
	@Value("${plugins.configplugin}")
	String configPlugInBeanName;
	
	@Inject
	Map<String, IOutputPlugin> outputPlugInMap;
	
	@Value("${plugins.outputplugIn}")
	String outputPlugInBeanName;
	
	
	
	/**
	 * Liefert der in der Konfiguration unter dem Schlüssel plugins.dataplugin definierten Bean
	 * @return
	 */
	public IDataPlugin getConfiguratedDataPlugIn(){
		LOG.debug(dataPlugBeanName);	
		IDataPlugin idataPlugIn = dataPlugInMap.get(dataPlugBeanName);
		LOG.debug("DataPlugInClass: " + idataPlugIn.getClass());
		return idataPlugIn;
	}
	
	/**
	 * Liefert der in der Konfiguration unter dem Schlüssel plugins.dataplugin definierten Bean
	 * @return
	 */
	public IOutputPlugin getConfiguratedOutputPlugin(){
		LOG.debug(outputPlugInBeanName);	
		IOutputPlugin ioutputPlugIn = outputPlugInMap.get(outputPlugInBeanName);
		LOG.debug("OutpuPlugInClass: " + ioutputPlugIn.getClass());
		return ioutputPlugIn;
	}
	
	/**
	 * Liefert der in der Konfiguration unter dem Schlüssel plugins.dataplugin definierten Bean
	 * @return
	 */
	public IConfigPlugin getConfiguratedConfigPlugIn(){
		LOG.debug(configPlugInBeanName);	
		IConfigPlugin iConfigPlugIn = configPlugInMap.get(configPlugInBeanName);
		LOG.debug("ConfiPlugInClasse: " + iConfigPlugIn.getClass());
		return iConfigPlugIn;
	}

}
