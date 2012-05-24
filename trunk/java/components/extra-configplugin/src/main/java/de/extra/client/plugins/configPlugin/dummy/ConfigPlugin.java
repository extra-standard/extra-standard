package de.extra.client.plugins.configPlugin.dummy;

import de.extra.client.core.model.ConfigFileBean;
import de.extra.client.core.plugin.IConfigPlugin;

public class ConfigPlugin implements IConfigPlugin {

	/**
	 * 
	 * Dummy zum Testen der Anwendung
	 * 
	 */

	public ConfigFileBean getConfigFile() {

		return loadConfigFile();
	}

	private ConfigFileBean loadConfigFile() {

		ConfigFileBean config = new ConfigFileBean();

		config.setTestIndicator(true);
		config.setAbsClass("Betriebsnummer");
		config.setAbsBbnr("12345678");
		config.setAbsName("Softwarehaus");
		config.setEmpfClass("Betriebsnummer");
		config.setEmpfBbnr("66667777");
		config.setEmpfName("DRV Bund Wuerzburg");

		config.setProductName("ExtraClient");
		config.setProductManuf("DRV Bund");

		config.setProcedure("http://www.extra-standard.de/procedures/DEUEV");
		config.setDataType("http://www.extra-standard.de/datatypes/Sofortmeldung");
		config.setScenario("http://www.extra-standard.de/scenario/request-with-acknowledgement");

		config.setContentType("xcpt:Base64CharSequence");
		config.setPackageLayer(false);
		config.setMessageLayer(false);

		config.setRequestId("rqId12345");

		return config;
	}

}
