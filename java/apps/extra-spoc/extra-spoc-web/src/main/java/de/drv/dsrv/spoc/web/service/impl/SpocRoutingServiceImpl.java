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
