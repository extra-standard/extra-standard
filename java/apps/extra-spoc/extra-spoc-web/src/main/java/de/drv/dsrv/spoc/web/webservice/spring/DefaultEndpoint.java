package de.drv.dsrv.spoc.web.webservice.spring;

import java.util.Locale;

import javax.xml.transform.Source;

import org.springframework.context.MessageSource;
import org.springframework.ws.server.endpoint.PayloadEndpoint;
import org.springframework.ws.server.endpoint.annotation.Endpoint;

import de.drv.dsrv.spoc.web.util.Messages;
import de.drv.dsrv.spoc.web.webservice.InvalidExtraRequestException;

/**
 * Web Service Endpoint fuer alle Nicht-eXTra-Requests. Es wird eine
 * {@link InvalidExtraRequestException} erzeugt, die dann im
 * {@link SpocEndpointExceptionResolver} behandelt wird.
 * <p>
 * Dieser Endpoint wird auch dann aufgerufen, wenn der Request nicht gegen das
 * eXTra-Schema validiert, da die Auswahl des Endpoints vor dem
 * Validierungs-Interceptor greift.
 */
@Endpoint
public class DefaultEndpoint implements PayloadEndpoint {

	private final MessageSource messages;

	public DefaultEndpoint(final MessageSource messages) {
		this.messages = messages;
	}

	@Override
	public Source invoke(final Source source) {

		throw new InvalidExtraRequestException(this.messages.getMessage(Messages.ERROR_REQUEST_NO_EXTRA, null,
				Locale.GERMAN));
	}
}
