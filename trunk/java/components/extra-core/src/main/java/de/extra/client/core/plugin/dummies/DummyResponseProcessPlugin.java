package de.extra.client.core.plugin.dummies;

import javax.inject.Named;

import de.drv.dsrv.extrastandard.namespace.response.ResponseTransport;
import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.plugin.IResponseProcessPlugin;

@Named("dummyResponseProcessPlugin")
public class DummyResponseProcessPlugin implements IResponseProcessPlugin {

	@Override
	public IResponseData processResponse(
			final ResponseTransport responseTransport) {
		return null;
	}

}
