/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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