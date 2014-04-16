package de.drv.dsrv.spoc.web.webservice.jax;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorType;
import de.drv.dsrv.spoc.web.webservice.ExtraExceptionHelper;

/**
 * Erweitert den {@link ExtraExceptionHelper} um die Behandlung von Ausnahmen im
 * JAX-WS-Umfeld.
 */
public class JaxWsExtraExceptionHelper extends ExtraExceptionHelper {

	private static final Log LOG = LogFactory.getLog(JaxWsExtraExceptionHelper.class);

	private final String soapFaultString;

	/**
	 * Konstruktor.
	 * 
	 * @param messages
	 *            Quelle mit den Fehlertexten
	 * @param soapFaultString
	 *            SOAP-Fehler
	 * @param extraErrorCode
	 *            eXTRa-Fehlercode
	 */
	public JaxWsExtraExceptionHelper(final MessageSource messages, final String soapFaultString,
			final String extraErrorCode) {
		super(messages, extraErrorCode);
		this.soapFaultString = soapFaultString;
	}

	/**
	 * Behandelt verschiedene Ausnahmen und erstellt dazu passende
	 * ExtraError-Objekte.
	 * 
	 * @param exception
	 *            zu behandelnde Ausnahme
	 * @return ExtraError erstellter ExtraError
	 */
	public ExtraError handleExceptionJaxWs(final Exception exception) {

		ExtraError extraError = null;

		LOG.error("Behandlung von Exception: ", exception);

		if (exception instanceof ExtraError) {
			extraError = (ExtraError) exception;
		} else {

			try {
				final ExtraErrorType extraErrorType = generateExtraErrorFromException(exception);
				extraError = new ExtraError(this.soapFaultString, extraErrorType);
			} catch (final Exception e) {
				LOG.error("Exception beim Marshalling von ExtraError.", e);
			}
		}
		return extraError;
	}

}
