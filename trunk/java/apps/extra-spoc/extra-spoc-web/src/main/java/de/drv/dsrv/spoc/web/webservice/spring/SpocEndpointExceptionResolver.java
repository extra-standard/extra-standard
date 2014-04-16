package de.drv.dsrv.spoc.web.webservice.spring;

import java.util.Locale;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.AbstractEndpointExceptionResolver;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapMessage;

import de.drv.dsrv.spoc.extra.v1_3.ExtraJaxbMarshaller;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorType;
import de.drv.dsrv.spoc.web.util.Messages;
import de.drv.dsrv.spoc.web.webservice.ExtraExceptionHelper;
import de.drv.dsrv.spoc.web.webservice.InvalidExtraRequestException;
import de.drv.dsrv.spoc.web.webservice.UnidentifiedFachverfahrenException;

/**
 * Schreibt <code>Exception</code>s als <code>ExtraErrorType</code> in ein
 * SOAP-Fault Objekt.
 */
public class SpocEndpointExceptionResolver extends AbstractEndpointExceptionResolver {

	private static final Log LOG = LogFactory.getLog(SpocEndpointExceptionResolver.class);

	/**
	 * Standard <code>Locale</code> f&uuml; Textausgaben.
	 */
	private static final Locale DEFAULT_LOCALE = Locale.GERMANY;

	private final ExtraJaxbMarshaller extraJaxbMarshaller;

	private final ExtraExceptionHelper extraExceptionHelper;

	private final MessageSource messages;

	private final String soapFaultString;

	public SpocEndpointExceptionResolver(final ExtraExceptionHelper extraExceptionHelper,
			final ExtraJaxbMarshaller extraJaxbMarshaller, final MessageSource messageSource,
			final String soapFaultString) {
		super();
		this.extraExceptionHelper = extraExceptionHelper;
		this.extraJaxbMarshaller = extraJaxbMarshaller;
		this.messages = messageSource;
		this.soapFaultString = soapFaultString;
	}

	@Override
	protected boolean resolveExceptionInternal(final MessageContext ctx, final Object endpoint,
			final Exception exception) {

		LOG.error("Behandlung von Exception: ", exception);

		if (!(ctx.getResponse() instanceof SoapMessage)) {
			LOG.error("Aufruf mit anderer Response als SoapMessage: " + ctx.getResponse());
			throw new IllegalArgumentException("SpocEndpointExceptionResolver benoetigt eine SoapMessage");
		}

		final SoapBody body = ((SoapMessage) ctx.getResponse()).getSoapBody();
		final SoapFault fault = generateSoapFault(exception, body);
		final SoapFaultDetail faultDetail = fault.addFaultDetail();

		try {
			final ExtraErrorType extraErrorType = this.extraExceptionHelper.generateExtraErrorFromException(exception);
			this.extraJaxbMarshaller.marshalExtraError(extraErrorType, faultDetail.getResult());
		} catch (final Exception e) {
			LOG.error("Exception beim Marshalling von ExtraError.", e);
			faultDetail.addFaultDetailElement(
					new QName(this.messages.getMessage(Messages.ERROR_NON_EXTRA_ELEMENT_NAME, null, DEFAULT_LOCALE)))
					.addText(this.messages.getMessage(Messages.ERROR_NON_EXTRA_TEXT, null, DEFAULT_LOCALE));
		}
		return true;
	}

	private SoapFault generateSoapFault(final Exception exception, final SoapBody body) {

		SoapFault fault = null;
		if ((exception instanceof InvalidExtraRequestException)
				|| (exception instanceof UnidentifiedFachverfahrenException)) {
			fault = body.addClientOrSenderFault(this.soapFaultString, DEFAULT_LOCALE);
		} else {
			fault = body.addServerOrReceiverFault(this.soapFaultString, DEFAULT_LOCALE);
		}
		return fault;
	}
}
