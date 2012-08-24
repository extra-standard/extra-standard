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

import java.io.IOException;

import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import de.drv.dsrv.extrastandard.namespace.components.Base64CharSequenceType;
import de.drv.dsrv.extrastandard.namespace.components.DataType;
import de.drv.dsrv.extrastandard.namespace.components.ElementSequenceType;
import de.drv.dsrv.extrastandard.namespace.request.TransportBody;
import de.extra.client.core.model.ConfigFileBean;
import de.extra.client.core.model.SenderDataBean;

@Named("bodyHelper")
public class BuildBodyHelper {

	private static Logger logger = Logger.getLogger(BuildBodyHelper.class);

	/**
	 * Funktion zum Aufbau des Transportbodys.
	 * 
	 * @param configBean
	 *            ConfigFileBean mit den Informationen aus der Profildatei
	 * @param versanddatenBean
	 *            VersanddatenBean mit den Nutzdaten
	 * @return
	 */
	public TransportBody buildTransportBody(ConfigFileBean configBean,
			SenderDataBean versanddatenBean) {
		logger.debug("TransportBody aufbauen");

		TransportBody transportBody = new TransportBody();
		logger.debug("Baue TransportBody auf");
		if (configBean.getContentType().equalsIgnoreCase(
				"xcpt:Base64CharSequence")) {
			DataType data = new DataType();
			Base64CharSequenceType base64CharSequence = new Base64CharSequenceType();

			/*
			 * base64CharSequence.setValue(new BASE64Encoder().encode(
			 * versanddatenBean.getNutzdaten()).getBytes());
			 */
			byte[] inputDataArray;
			try {
				inputDataArray = IOUtils.toByteArray(versanddatenBean
						.getInputData());
			} catch (IOException e) {
				// TODO exception einbauen
				throw new IllegalStateException(e);
			}
			base64CharSequence.setValue(inputDataArray);
			data.setBase64CharSequence(base64CharSequence);

			transportBody.setData(data);
		}

		if (configBean.getContentType()
				.equalsIgnoreCase("xcpt:ElementSequence")) {
			DataType data = new DataType();
			ElementSequenceType elementSequence = new ElementSequenceType();

			elementSequence.getAny().add(versanddatenBean.getDataRequest());
			data.setElementSequence(elementSequence);
			transportBody.setData(data);
		}

		// TODO implementieren weiterer Möglichkeiten nach dem aktuellen
		// eXTraSchema

		return transportBody;
	}
}
