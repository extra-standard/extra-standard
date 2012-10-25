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
package de.extra.client.plugins.configplugin.controller;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import de.drv.dsrv.schema.ProfilkonfigurationType;
import de.extra.client.plugins.configplugin.ConfigConstants;
import de.extra.client.plugins.configplugin.helper.ProfilHelper;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;

@Named("configController")
public class ConfigPluginController {

	private static final Logger LOG = LoggerFactory
			.getLogger(ConfigPluginController.class);

	@Inject
	@Named("_configurationDirectory")
	private File configurationDirectory;

	@Value("${plugins.configplugin.defaultConfigPlugin.profilFileName}")
	private String profileFileName;

	@Inject
	@Named("profilHelper")
	private ProfilHelper profilHelper;

	private File profileFile;

	@PostConstruct
	public void initProfileFile() {
		this.profileFile = new File(configurationDirectory.getAbsolutePath(),
				profileFileName);
	}


	/**
	 * Verarbeitung der Konfigurations-Files.
	 *
	 * @return ConfigFileBean
	 */
	public IExtraProfileConfiguration processConfigFile() {
		IExtraProfileConfiguration cfb = null;

		// Laden der Profildatei
		LOG.debug("Unmarshaling der Profildatei");

		// Unmarshalling der Profildatei
		ProfilkonfigurationType profilConfig = unmarshalConfig(profileFile);
		cfb = profilHelper.configFileBeanLoader(profilConfig.getElement());

		LOG.debug("Füllen der Versandinformationen");

		// Erzeugen der RequestId
		/*
		 * Auskommentiert, da RequestId über den Auftragssatz der Nutzdatei
		 * mitgeliefert wird
		 * 
		 * String requestId = profilHelper.generateReqId();
		 * 
		 * logger.info("RequestId: " + requestId);
		 * 
		 * cfb.setRequestId(requestId);
		 */

		return cfb;
	}

	/**
	 * Unmarshalling der Profildatei.
	 *
	 * @param dateiName
	 *            Vollstaendiger Pfad der Profildatei
	 * @return JaxB-Element
	 */
	private ProfilkonfigurationType unmarshalConfig(final File profileFile) {
		ProfilkonfigurationType pkt = null;
		JAXBContext jc;
		try {
			// Initialisieren des JaxB-Contextes
			jc = JAXBContext.newInstance(ConfigConstants.UNMARSHAL_CONFIG);

			// Aufruf des Unmarshallers
			Unmarshaller u = jc.createUnmarshaller();
			pkt = (ProfilkonfigurationType) u.unmarshal(profileFile);
		} catch (JAXBException e) {
			LOG.error("Fehler beim Verarbeiten des XML", e);
		} catch (Exception e) {
			LOG.error("Fehler beim Verarbeiten des XML", e);
		}
		return pkt;
	}
}
