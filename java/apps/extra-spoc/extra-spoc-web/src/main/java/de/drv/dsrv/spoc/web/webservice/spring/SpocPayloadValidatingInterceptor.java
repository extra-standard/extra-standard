package de.drv.dsrv.spoc.web.webservice.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.xml.sax.SAXParseException;

import de.drv.dsrv.spoc.web.webservice.InvalidExtraRequestException;

/**
 * Erweitert den von Spring-WS vorgegebenen
 * <code>PayloadValidatingInterceptor</code>, damit der zur&uuml;ck gegebene
 * Fehler im eXTra Format ist.
 */
public class SpocPayloadValidatingInterceptor extends PayloadValidatingInterceptor {

	private static final Log LOG = LogFactory.getLog(SpocPayloadValidatingInterceptor.class);

	public SpocPayloadValidatingInterceptor() {
		super();
	}

	@Override
	protected boolean handleRequestValidationErrors(
			final MessageContext messageContext, final SAXParseException[] errors) {

		final StringBuilder errorText = new StringBuilder();

        for (final SAXParseException error : errors) {
			errorText.append(error.getMessage()).append("   ");
			LOG.warn("Fehler bei der XML-Validierung: " + error.getMessage());
        }
		throw new InvalidExtraRequestException(errorText.toString());
	}
}
