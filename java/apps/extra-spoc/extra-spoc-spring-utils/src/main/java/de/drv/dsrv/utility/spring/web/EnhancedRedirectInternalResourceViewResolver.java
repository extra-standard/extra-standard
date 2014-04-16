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