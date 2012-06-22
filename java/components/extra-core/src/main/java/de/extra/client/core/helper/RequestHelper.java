/*
 * Licensed to the Apache Software Fouimport de.extra.client.core.model.ConfigFileBean;
import de.extra.client.core.model.VersanddatenBean;
 ownership.  The ASF licenses this file
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
package de.extra.client.core.helper;

import de.drv.dsrv.extrastandard.namespace.response.XMLTransport;
import de.extra.client.core.model.ConfigFileBean;
import de.extra.client.core.model.VersanddatenBean;

public class RequestHelper {

	private final BuildPluginHelper pluginHelper;

	private final BuildHeaderHelper headerHelper;

	private final BuildBodyHelper bodyHelper;
	private final String extraProfile;

	/**
	 * Konstruktor fuer den Requesthelper zum Befuellen der Werte aus der
	 * SpringConfig
	 * 
	 * @param pluginHelper
	 *            Bean des PluginHelpers
	 * @param headerHelper
	 *            Bean des HeaderHelpers
	 * @param bodyHelper
	 *            Bean des BodyHelpers
	 */
	public RequestHelper(final BuildPluginHelper pluginHelper,
			final BuildHeaderHelper headerHelper,
			final BuildBodyHelper bodyHelper, final String extraProfile) {
		super();
		this.pluginHelper = pluginHelper;
		this.headerHelper = headerHelper;
		this.bodyHelper = bodyHelper;
		this.extraProfile = extraProfile;
	}

	/**
	 * 
	 * Funktion zum Aufbau des XML-Requests
	 * 
	 * @param versanddatenBean
	 *            Enth�tlt alles Informtionen zu den Nutzdaten
	 * @param configBean
	 *            Enth�lt alle Informationen zum den Versandinformtionen
	 * @return JaxB-Objekt XMLTransport
	 */
	public XMLTransport baueRequest(VersanddatenBean versanddatenBean,
			ConfigFileBean configBean) {

		XMLTransport request = new XMLTransport();

		request.setProfile(extraProfile);

		// Aufbau des Headers

		request.setTransportHeader(headerHelper.baueHeader(configBean,
				versanddatenBean.getRequestId()));

		// Aufbau der TransportPlugins
		if (versanddatenBean.getPlugins() != null) {
			request.setTransportPlugIns(pluginHelper
					.buildPluginContainer(versanddatenBean.getPlugins()));
		}
		// Aufbau des TransportBodys

		request.setTransportBody(bodyHelper.buildTransportBody(configBean,
				versanddatenBean));

		return request;
	}
}
