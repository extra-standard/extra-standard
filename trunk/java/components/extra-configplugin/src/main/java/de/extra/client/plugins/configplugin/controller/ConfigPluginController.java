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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import de.drv.dsrv.schema.ProfilkonfigurationType;
import de.extra.client.core.model.ConfigFileBean;
import de.extra.client.plugins.configplugin.ConfigConstants;
import de.extra.client.plugins.configplugin.helper.ProfilHelper;

@Named("configController")
public class ConfigPluginController {

	Logger logger = Logger.getLogger(ConfigPluginController.class);

	private String profileFile;

	@Inject
	@Named("profilHelper")
	private ProfilHelper profilHelper;

	@Value("${profilOrdner}")
	public void setProfileFile(String profileFile) {
		this.profileFile = getAbsoluteFilename(profileFile);
	}

	public String getAbsoluteFilename(String profileFile) {
		if (profileFile.startsWith("classpath:")) {
			String filename = profileFile.replaceFirst("classpath:", "");
			URL url = this.getClass().getClassLoader().getResource(filename);
			String absolutePath = url.getPath();
			return absolutePath;
		} else {
			return profileFile;
		}
	}

	/**
	 * Verarbeitung der Konfigurations-Files.
	 * 
	 * @return ConfigFileBean
	 */
	public ConfigFileBean processConfigFile() {
		FileInputStream in = null;
		ConfigFileBean cfb = null;

		// Laden der Profildatei
		try {
			in = new FileInputStream(profileFile);

			if (logger.isDebugEnabled()) {
				logger.debug("Unmarshaling der Profildatei");
			}

			// Unmarshalling der Profildatei
			ProfilkonfigurationType profilConfig = unmarshalConfig(profileFile);
			cfb = profilHelper.configFileBeanLoader(profilConfig.getElement());
		} catch (FileNotFoundException e) {
			logger.error("ProfilDatei konnte nicht gefunden werden", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				logger.error("Fehler beim Schliessen des InputStreams", e);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Füllen der Versandinformationen");

		}

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
	private ProfilkonfigurationType unmarshalConfig(String dateiName) {
		ProfilkonfigurationType pkt = null;
		JAXBContext jc;
		try {
			// Initialisieren des JaxB-Contextes
			jc = JAXBContext.newInstance(ConfigConstants.UNMARSHAL_CONFIG);

			// Aufruf des Unmarshallers
			Unmarshaller u = jc.createUnmarshaller();
			pkt = (ProfilkonfigurationType) u.unmarshal(new File(dateiName));
		} catch (JAXBException e) {
			logger.error("Fehler beim Verarbeiten des XML", e);
		} catch (Exception e) {
			logger.error("Fehler beim Verarbeiten des XML", e);
		}
		return pkt;
	}
}
