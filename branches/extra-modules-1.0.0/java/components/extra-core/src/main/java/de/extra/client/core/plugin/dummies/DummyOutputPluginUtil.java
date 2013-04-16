package de.extra.client.core.plugin.dummies;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.oxm.XmlMappingException;

import de.drv.dsrv.extra.marshaller.IExtraUnmarschaller;
import de.drv.dsrv.extrastandard.namespace.request.Transport;
import de.extrastandard.api.exception.ExtraOutputPluginRuntimeException;

@Named("dummyOutputPluginUtil")
public class DummyOutputPluginUtil {

	@Inject
	@Named("extraUnmarschaller")
	private IExtraUnmarschaller extraUnmarschaller;

	public String extractRequestId(final InputStream request) {
		try {
			final Transport requestXml = extraUnmarschaller.unmarshal(request,
					Transport.class);
			// Ich gehe davon aus, dass requestId ein Mandatory Feld ist
			final String requestId = requestXml.getTransportHeader()
					.getRequestDetails().getRequestID().getValue();
			return requestId;
		} catch (final XmlMappingException xmlMappingException) {
			throw new ExtraOutputPluginRuntimeException(xmlMappingException);
		} catch (final IOException ioException) {
			throw new ExtraOutputPluginRuntimeException(ioException);
		}
	}
}
