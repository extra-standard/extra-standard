package de.extra.client.core.plugin.dummies;

import java.io.InputStream;

import javax.inject.Named;

import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.plugin.IResponseProcessPlugin;

@Named("dummyResponseProcessPlugin")
public class DummyResponseProcessPlugin implements IResponseProcessPlugin {

	@Override
	public IResponseData processResponse(final InputStream responseAsStream) {
		// TODO Auto-generated method stub
		return null;
	}

}
