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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import de.extra.client.core.helper.RequestHelper;
import de.extra.client.core.model.ConfigFileBean;
import de.extra.client.core.model.VersanddatenBean;
import de.extra.client.core.plugin.IConfigPlugin;
import de.extra.client.core.plugin.IDataPlugin;
import de.extra.client.core.plugin.IOutputPlugin;

public class ClientCore {

	private final IDataPlugin dataPlugin;

	private final IConfigPlugin configPlugin;

	private final IOutputPlugin outputPlugin;

	private final NamespacePrefixMapper namespacePrefixMapper;

	private final RequestHelper requestHelper;

	private static Logger logger = Logger.getLogger(ClientCore.class);

	/**
	 * 
	 * Konstruktor
	 * 
	 * @param dataPlugin
	 *            DataPlugin aus SpringConfig
	 * @param configPlugin
	 *            ConfigPlugin aus SpringConfig
	 * @param outputPlugin
	 *            OutputPlugin aus SpringConfig
	 * @param namespacePrefixMapper
	 *            Mapper um die Standardnamespace zu �berschreiben
	 * @param requestHelper
	 *            Helper zum Aufbau des Requests
	 */
	public ClientCore(final IDataPlugin dataPlugin,
			final IConfigPlugin configPlugin, final IOutputPlugin outputPlugin,
			final NamespacePrefixMapper namespacePrefixMapper,
			RequestHelper requestHelper) {
		super();
		this.dataPlugin = dataPlugin;
		this.configPlugin = configPlugin;
		this.outputPlugin = outputPlugin;
		this.namespacePrefixMapper = namespacePrefixMapper;
		this.requestHelper = requestHelper;
	}

	/**
	 * 
	 * Funktion in der der Request aufgebaut wird
	 * 
	 * @return StatusCode nach der Verarbeitung
	 */
	public int buildRequest() {
		List<VersanddatenBean> versandDatenListe = dataPlugin.getVersandDaten();
		ConfigFileBean configFile = configPlugin.getConfigFile();

		int statusCode = 9;
		XMLTransport request = new XMLTransport();

		try {

			// Transformation XML zu String

			// Laden der Parameter f�r JaxB
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

			// �berpr�fen ob ein PackageLayer ben�tigt wird

			if (!configFile.isPackageLayer()) {

				for (Iterator iter = versandDatenListe.iterator(); iter
						.hasNext();) {
					versanddatenBean = (VersanddatenBean) iter.next();

					request = requestHelper.baueRequest(versanddatenBean,
							configFile);

					Writer writer = new StringWriter();

					marshaller.marshal(request, writer);

					logger.trace("Ausgabe: " + writer.toString());
					logger.debug("�bergabe an OutputPlugin");

					if (outputPlugin.outputData(writer.toString())) {

						statusCode = 0;

					} else {

						logger.error("Fehler beim Versand des Requests");

						statusCode = 9;

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
