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

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;

import de.drv.dsrv.extrastandard.namespace.request.XMLTransport;
import de.extra.client.core.model.IExtraProfileConfiguration;
import de.extra.client.core.model.IInputDataContainer;

@Named("requestHelper")
public class RequestHelper {

	@Inject
	@Named("pluginHelper")
	private BuildPluginHelper pluginHelper;

	@Inject
	@Named("headerHelper")
	private MessageHeaderBuilder headerHelper;

	@Inject
	@Named("bodyHelper")
	private BuildBodyHelper bodyHelper;

	@Value("${message.builder.transport.attributes.extraProfile}")
	private String extraProfile;

	/**
	 * Funktion zum Aufbau des XML-Requests.
	 * 
	 * @param versanddatenBean
	 *            Enthält alles Informtionen zu den Nutzdaten
	 * @param configBean
	 *            Enthält alle Informationen zum den Versandinformationen
	 * @return JaxB-Objekt XMLTransport
	 */
	public XMLTransport buildRequest(IInputDataContainer versanddatenBean,
			IExtraProfileConfiguration configBean) {
		XMLTransport request = new XMLTransport();

		request.setProfile(extraProfile);

		// Aufbau des Headers
		request.setTransportHeader(headerHelper.createHeader(configBean,
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
