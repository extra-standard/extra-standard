package de.drv.dsrv.spoc.web.service.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.drv.dsrv.spoc.dao.SpocConfigDAO;
import de.drv.dsrv.spoc.dto.SPoCConfigDTO;
import de.drv.dsrv.spoc.web.service.SpocRoutingService;

/**
 * Standard-Implementierung des <code>SpocRoutingService</code>.
 */
public class SpocRoutingServiceImpl implements SpocRoutingService {

	private static final Log LOG = LogFactory.getLog(SpocRoutingServiceImpl.class);

	private final SpocConfigDAO spocConfigDAO;

	/**
	 * Konstruktor.
	 * 
	 * @param spocConfigDAO
	 *            DAO-Klasse zum Zugriff auf die SPoC-Datenbank
	 */
	public SpocRoutingServiceImpl(final SpocConfigDAO spocConfigDAO) {
		this.spocConfigDAO = spocConfigDAO;
	}

	@Override
	public URI getFachverfahrenUrl(final String version, final String profile, final String procedure,
			final String dataType) {

		// Evtl. vorhandene Leerzeichen und Zeilenumbrueche eliminieren (und
		// null-Werte in leere Strings umwandeln).
		final String trimmedVersion = StringUtils.trimToEmpty(version);
		final String trimmedProfile = StringUtils.trimToEmpty(profile);
		final String trimmedProcedure = StringUtils.trimToEmpty(procedure);
		final String trimmedDataType = StringUtils.trimToEmpty(dataType);

		// Zu den uebergebenen Werten wird die passende URL aus der SPoC-DB
		// geholt.
		final SPoCConfigDTO spocConfigDTO = this.spocConfigDAO.selectConfig(trimmedProcedure, trimmedDataType,
				trimmedProfile, trimmedVersion);

		final URI fachverfahrenUri = null;
		if (spocConfigDTO != null) {
			final String startUrl = spocConfigDTO.getStartUrl();
			if (startUrl != null) {
				try {
					return new URI(startUrl);
				} catch (final URISyntaxException exc) {
					LOG.error("Ung\u00fcltiger DB-Eintrag f\u00fcr die Parameter version=" + version + ", profile="
							+ profile + ", procedure=" + procedure + ", dataType=" + dataType + ": " + startUrl, exc);
				}
			}
		}

		return fachverfahrenUri;
	}
}
