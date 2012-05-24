package de.extra.client.plugins.configPlugin.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import de.drv.dsrv.schema.ProfilkonfigurationType;
import de.extra.client.core.model.ConfigFileBean;
import de.extra.client.plugins.configPlugin.ConfigConstants;
import de.extra.client.plugins.configPlugin.helper.ProfilHelper;

public class ConfigPluginController {

	private final String profilDatei;

	private final ProfilHelper profilHelper;

	Logger logger = Logger.getLogger(ConfigPluginController.class);

	/**
	 * 
	 * Konstruktor f�r das laden der Informationen aus der Spring-Config
	 * 
	 * @param profilDatei
	 *            Pfad zur Profildatei
	 * @param profilHelper
	 *            Objekt vom Typ ProfilHelper
	 */

	public ConfigPluginController(final String profilDatei,
			final ProfilHelper profilHelper) {
		super();
		this.profilDatei = profilDatei;
		this.profilHelper = profilHelper;
	}

	/**
	 * 
	 * Verarbeitung der Konfigurations-Files
	 * 
	 * @return ConfigFileBean
	 */

	public ConfigFileBean processConfigFile() {

		File profilFile = new File(profilDatei);
		FileInputStream in = null;
		ConfigFileBean cfb = null;

		// Laden der Profildatei

		try {
			in = new FileInputStream(profilDatei);

			if (logger.isDebugEnabled()) {

				logger.debug("Unmarshaling der Profildatei");
			}
			if (in != null) {

				// Unmarshalling der Profildatei

				ProfilkonfigurationType profilConfig = unmarshalConfig(profilDatei);

				cfb = profilHelper.configFileBeanLoader(profilConfig
						.getElement());

			} else {

				logger.error("InputStream null");

			}

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

			logger.debug("F�llen der Versandinformationen");

		}

		cfb = profilHelper.fuelleVersandInfo(cfb);

		// Erzeugen der RequestId

		/*
		 * Auskommentiert, da RequestId �ber den Auftragssatz der Nutzdatei
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
	 * 
	 * Unmarshalling der Profildatei
	 * 
	 * @param dateiName
	 *            Vollst�ndiger Pfad der Profildatei
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
