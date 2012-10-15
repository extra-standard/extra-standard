package de.extra.client.core.builder.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;

import de.drv.dsrv.extrastandard.namespace.components.RootElementType;
import de.extra.client.core.config.impl.ExtraProfileConfiguration;
import de.extrastandard.api.exception.ExtraConfigRuntimeException;

@Named("extraRequestBuilderITBasic")
public class ExtraRequestBuilderITBasic {

	private static final Logger logger = LoggerFactory.getLogger(ExtraRequestBuilderITBasic.class);

	@Inject
	@Named("eXTrajaxb2Marshaller")
	private Marshaller marshaller;

	public String getResultAsString(final RootElementType xmlTransport) {
		try {
			final Writer writer = new StringWriter();
			final StreamResult streamResult = new StreamResult(writer);

			marshaller.marshal(xmlTransport, streamResult);
			return writer.toString();
		} catch (final XmlMappingException xmlException) {
			logger.debug("XmlMappingException beim Lesen des Results ", xmlException);
			throw xmlException;
		} catch (final IOException ioException) {
			logger.debug("IOException beim Lesen des Results ", ioException);
			throw new ExtraConfigRuntimeException(ioException);
		}
	}

	public ExtraProfileConfiguration createTransportBodyWithDataConfigFileBean(final String rootElement) {
		final ExtraProfileConfiguration config = new ExtraProfileConfiguration();
		config.setRootElement(rootElement);
		config.addElementsHierarchyMap("Transport", "req:TransportBody");
		config.addElementsHierarchyMap("Transport", "req:TransportPlugins");
		config.addElementsHierarchyMap("Transport", "req:TransportHeader");
		config.addElementsHierarchyMap("TransportHeader", "xcpt:Sender");
		config.addElementsHierarchyMap("TransportHeader", "xcpt:Receiver");
		config.addElementsHierarchyMap("TransportHeader", "xcpt:TestIndicator");
		config.addElementsHierarchyMap("TransportHeader", "xcpt:RequestDetails");
		config.addElementsHierarchyMap("TransportBody", "xcpt:Data");
		return config;
	}

}
