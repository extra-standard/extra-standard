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

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.RedirectView;

public class EnhancedRedirectInternalResourceViewResolver extends
		InternalResourceViewResolver {

	protected static final String SERVER_REL_PREFIX = "serverRelative:";

	@Override
	protected View createView(final String viewName, final Locale locale)
			throws Exception {
		// Erweitere Funktionalität bei Redirect Views
		return checkAndEnhanceRedirectView(super.createView(viewName, locale));
	}

	protected View checkAndEnhanceRedirectView(final View view) {
		// Prüfe, ob die über die Basisklasse ermittelte View einen Redirect
		// repräsentiert
		if (view instanceof RedirectView) {
			// Führe Typumwandlung durch und ermittle gespeicherte URL
			final RedirectView redirectView = (RedirectView) view;
			final String url = redirectView.getUrl();

			// Prüfe, ob die gespeicherte URL den Präfix für eine Pfadangabe
			// relativ zur Basis-URI des Servers ist
			if (url != null && url.startsWith(SERVER_REL_PREFIX)) {
				// Entferne Präfix
				redirectView.setUrl(url.substring(SERVER_REL_PREFIX.length()));

				// URL ist nicht reletiv zur Context Root
				redirectView.setContextRelative(false);
			}
		}

		// Gebe View zurück
		return view;
	}
}