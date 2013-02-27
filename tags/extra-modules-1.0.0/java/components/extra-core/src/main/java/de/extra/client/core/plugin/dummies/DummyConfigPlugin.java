package de.extra.client.core.plugin.dummies;

import javax.inject.Named;

import de.extra.client.core.config.impl.ExtraProfileConfiguration;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;
import de.extrastandard.api.plugin.IConfigPlugin;

@Named("dummyConfigPlugin")
public class DummyConfigPlugin implements IConfigPlugin {

	/**
	 * Dummy zum Testen der Anwendung.
	 */
	@Override
	public IExtraProfileConfiguration getConfigFile() {
		return loadConfigFile();
	}

	private IExtraProfileConfiguration loadConfigFile() {
		final ExtraProfileConfiguration config = new ExtraProfileConfiguration();

		config.setContentType("xcpt:Base64CharSequence");
		config.setPackageLayer(false);
		config.setMessageLayer(true);

		return config;
	}

}
