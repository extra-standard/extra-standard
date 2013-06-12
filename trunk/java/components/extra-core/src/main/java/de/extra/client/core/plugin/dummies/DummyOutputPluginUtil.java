package de.extra.client.core.plugin.dummies;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.oxm.XmlMappingException;

import de.drv.dsrv.extra.marshaller.IExtraUnmarschaller;
import de.drv.dsrv.extrastandard.namespace.request.RequestTransport;
import de.extrastandard.api.exception.ExtraOutputPluginRuntimeException;

@Named("dummyOutputPluginUtil")
public class DummyOutputPluginUtil {

	@Inject
	@Named("extraUnmarschaller")
	private IExtraUnmarschaller extraUnmarschaller;

	public String extractRequestId(final RequestTransport request) {
		try {
			// Ich gehe davon aus, dass requestId ein Mandatory Feld ist
			final String requestId = request.getTransportHeader()
					.getRequestDetails().getRequestID().getValue();
			return requestId;
		} catch (final XmlMappingException xmlMappingException) {
			throw new ExtraOutputPluginRuntimeException(xmlMappingException);
		}
	}
}
