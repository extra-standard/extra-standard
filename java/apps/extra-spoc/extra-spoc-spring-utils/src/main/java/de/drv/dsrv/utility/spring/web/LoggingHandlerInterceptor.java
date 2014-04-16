package de.drv.dsrv.utility.spring.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoggingHandlerInterceptor extends HandlerInterceptorAdapter {

	private static final String PASSWORT_REGEX = ".*[P|p]asswort.*";

	private static final Log LOG = LogFactory
			.getLog(LoggingHandlerInterceptor.class);

	@Override
	public boolean preHandle(final HttpServletRequest request,
			final HttpServletResponse response, final Object handler) {
		// Prüfe, ob Log-Level aktiviert ist
		if (LOG.isDebugEnabled()) {
			// Schreibe Log-Meldung
			LOG.debug("URL " + request.getRequestURL() + " aufgerufen");

			// Schreibe Parameter
			for (final Object key : request.getParameterMap().keySet()) {
				// Prüfe, ob Parameter gültig (ungleich null) ist
				if (key != null) {
					// Prüfe, ob sich um ein Passwort handelt
					if (key.toString().matches(PASSWORT_REGEX)) {
						LOG.debug("+ Parameter: " + key.toString()
								+ "=********");
					} else {
						LOG.debug("+ Parameter: " + key.toString() + "="
								+ request.getParameter(key.toString()));
					}
				}
			}
		}

		// Führe weitere Verarbeitung durch
		return true;
	}
}