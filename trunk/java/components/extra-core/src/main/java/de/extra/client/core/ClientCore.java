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
package de.extra.client.core;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import de.drv.dsrv.extrastandard.namespace.request.XMLTransport;
import de.extra.client.core.helper.RequestHelper;
import de.extra.client.core.model.ConfigFileBean;
import de.extra.client.core.model.VersanddatenBean;
import de.extra.client.core.plugin.IConfigPlugin;
import de.extra.client.core.plugin.IDataPlugin;
import de.extra.client.core.plugin.IOutputPlugin;

@Named("clientCore")
public class ClientCore {

	public static final int STATUS_CODE_OK = 0;

	public static final int STATUS_CODE_ERROR = 9;

	@Inject
	@Named("dataPlugin")
	private IDataPlugin dataPlugin;

	@Inject
	@Named("configPlugin")
	private IConfigPlugin configPlugin;

	@Inject
	@Named("outputPlugin")
	private IOutputPlugin outputPlugin;

	@Inject
	@Named("namespacePrefixMapper")
	private NamespacePrefixMapper namespacePrefixMapper;

	@Inject
	@Named("requestHelper")
	private RequestHelper requestHelper;

	private static Logger logger = Logger.getLogger(ClientCore.class);

	/**
	 * Funktion in der der Request aufgebaut wird.
	 * 
	 * @return StatusCode nach der Verarbeitung
	 */
	public int buildRequest() {
		List<VersanddatenBean> versandDatenListe = dataPlugin.getVersandDaten();
		ConfigFileBean configFile = configPlugin.getConfigFile();

		int statusCode = STATUS_CODE_ERROR;
		XMLTransport request = new XMLTransport();

		try {
			// Transformation XML zu String
			// Laden der Parameter für JaxB
			JAXBContext context = JAXBContext
					.newInstance("de.drv.dsrv.extrastandard.namespace.request:de.drv.dsrv.extrastandard.namespace.plugins:de.drv.dsrv.extrastandard.namespace.messages");

			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
					namespacePrefixMapper);

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");

			VersanddatenBean versanddatenBean = null;

			// Überprüfen ob ein PackageLayer benötigt wird
			if (!configFile.isPackageLayer()) {
				for (Iterator<VersanddatenBean> iter = versandDatenListe
						.iterator(); iter.hasNext();) {
					versanddatenBean = iter.next();

					request = requestHelper.buildRequest(versanddatenBean,
							configFile);

					Writer writer = new StringWriter();
					marshaller.marshal(request, writer);

					logger.debug("Ausgabe: " + writer.toString());
					logger.debug("Übergabe an OutputPlugin");

					if (outputPlugin.outputData(writer.toString())) {
						statusCode = STATUS_CODE_OK;
					} else {
						logger.error("Fehler beim Versand des Requests");
						statusCode = STATUS_CODE_ERROR;
					}
				}

			} else {
				// TODO Aufbau der Logik zum Versand von mehreren Nachrichten
				// als Package oder Message

			}

		} catch (JAXBException e) {
			logger.error("Fehler beim Erstellen des Requests", e);
		}

		return statusCode;
	}
}
