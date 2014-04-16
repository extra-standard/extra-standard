package de.drv.dsrv.spoc.web.webservice;

import java.net.ConnectException;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;

import org.springframework.context.MessageSource;

import de.drv.dsrv.spoc.extra.v1_3.ExtraHelper;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorReasonType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorType;
import de.drv.dsrv.spoc.web.service.FachverfahrenResponseException;
import de.drv.dsrv.spoc.web.util.Messages;

/**
 * Stellt Funktionen zur Behandlung von Ausnahmen und zur Erstellung passender
 * ExtraError-Objekte bereit.
 */
public class ExtraExceptionHelper {

	/**
	 * Standard <code>Locale</code> f&uuml; Textausgaben.
	 */
	private static final Locale DEFAULT_LOCALE = Locale.GERMAN;

	private final MessageSource messages;
	private final String extraErrorCode;

	/**
	 * Konstruktor.
	 * 
	 * @param messages
	 *            Quelle mit den Fehlertexten
	 * @param extraErrorCode
	 *            eXTRa-Fehlercode
	 */
	public ExtraExceptionHelper(final MessageSource messages, final String extraErrorCode) {
		this.messages = messages;
		this.extraErrorCode = extraErrorCode;
	}

	/**
	 * Erstellt ein ExtraError-Objekt fuer die uebergebene Ausnahme.
	 * 
	 * @param exception
	 *            zu behandelnde Ausnahme
	 * @return ExtraError-Objekt
	 * @throws DatatypeConfigurationException
	 */
	public ExtraErrorType generateExtraErrorFromException(final Exception exception)
			throws DatatypeConfigurationException {

		final ExtraErrorReasonType reason;
		String errorText = null;

		if (exception instanceof InvalidExtraRequestException) {
			reason = ExtraErrorReasonType.INVALID_REQUEST;
			errorText = exception.getMessage();
		} else if (exception instanceof UnidentifiedFachverfahrenException) {
			reason = ExtraErrorReasonType.INVALID_REQUEST;

			final UnidentifiedFachverfahrenException unidentifiedFachverfahrenExc = (UnidentifiedFachverfahrenException) exception;
			errorText = this.messages.getMessage(Messages.ERROR_FACHVERFAHREN_UNCONFIGURED, new String[] {
					unidentifiedFachverfahrenExc.getProfile(), unidentifiedFachverfahrenExc.getVersion(),
					unidentifiedFachverfahrenExc.getProcedure(), unidentifiedFachverfahrenExc.getDataType() },
					DEFAULT_LOCALE);
		} else if (exception instanceof ConnectException) {
			// Wir gehen mal davon aus, dass wir wuessten, wenn das
			// Fachverfahren dauerhaft nicht da ist.
			reason = ExtraErrorReasonType.SERVICE_TEMPORARILY_UNAVAILABLE;
			errorText = this.messages.getMessage(Messages.ERROR_FACHVERFAHREN_NO_CONNECTION, null, DEFAULT_LOCALE);
		} else if (exception instanceof FachverfahrenResponseException) {
			reason = ExtraErrorReasonType.SERVICE_FAILURE;
			errorText = this.messages.getMessage(Messages.ERROR_FACHVERFAHREN_RESPONSE, null, DEFAULT_LOCALE);
		} else {
			reason = ExtraErrorReasonType.UNSPECIFIED;
		}
		if (errorText == null) {
			errorText = this.messages.getMessage(Messages.ERROR_REQUEST_PROCESSING_UNKNOWN, null, DEFAULT_LOCALE);
		}
		return ExtraHelper.generateError(reason, this.extraErrorCode, errorText);
	}
}
