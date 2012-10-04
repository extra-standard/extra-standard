package de.extra.client.core.plugin.dummies;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.stream.StreamSource;

import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

import de.drv.dsrv.extrastandard.namespace.request.XMLTransport;
import de.extrastandard.api.exception.ExtraOutputPluginRuntimeException;

@Named("dummyOutputPluginUtil")
public class DummyOutputPluginUtil {

	@Inject
	@Named("eXTrajaxb2Marshaller")
	private Unmarshaller unmarshaller;

	public String extractRequestId(final InputStream request) {
		try {
			final XMLTransport requestXml = (XMLTransport) unmarshaller.unmarshal(new StreamSource(request));
			// Ich gehe davon aus, dass requestId ein Mandatory Feld ist
			final String requestId = requestXml.getTransportHeader().getRequestDetails().getRequestID().getValue();
			return requestId;
		} catch (final XmlMappingException xmlMappingException) {
			throw new ExtraOutputPluginRuntimeException(xmlMappingException);
		} catch (final IOException ioException) {
			throw new ExtraOutputPluginRuntimeException(ioException);
		}
	}
}
