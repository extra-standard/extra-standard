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
package de.extra.client.core.helper;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import de.drv.dsrv.extrastandard.namespace.components.Base64CharSequenceType;
import de.drv.dsrv.extrastandard.namespace.components.DataType;
import de.drv.dsrv.extrastandard.namespace.request.TransportBody;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;
import de.extrastandard.api.model.content.ISingleContentInputData;

@Named("bodyHelper")
public class BuildBodyHelper {

	private static final Logger LOG = LoggerFactory.getLogger(BuildBodyHelper.class);

	@Value("${builder.xcpt.ElementSequencelocator.transportBodyElementSequenceBuilder.packageLimit}")
	private String packageLimit;

	/**
	 * Funktion zum Aufbau des Transportbodys.
	 * 
	 * @param configBean
	 *            ConfigFileBean mit den Informationen aus der Profildatei
	 * @param inputData
	 *            VersanddatenBean mit den Nutzdaten
	 * @return
	 */
	public TransportBody buildTransportBody(final IExtraProfileConfiguration configBean,
			final ISingleContentInputData inputData) {
		LOG.debug("TransportBody aufbauen");

		final TransportBody transportBody = new TransportBody();
		LOG.debug("Baue TransportBody auf");
		if (configBean.getContentType().equalsIgnoreCase("xcpt:Base64CharSequence")) {
			final DataType data = new DataType();
			final Base64CharSequenceType base64CharSequence = new Base64CharSequenceType();

			/*
			 * base64CharSequence.setValue(new BASE64Encoder().encode(
			 * versanddatenBean.getNutzdaten()).getBytes());
			 */
			byte[] inputDataArray;

			inputDataArray = inputData.getInputDataAsByteArray();
			base64CharSequence.setValue(inputDataArray);
			data.setBase64CharSequence(base64CharSequence);

			transportBody.setData(data);
		}

		// TODO implementieren weiterer Möglichkeiten nach dem aktuellen
		// eXTraSchema

		return transportBody;
	}

}
