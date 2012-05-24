package de.extra.client.plugins.configPlugin.helper;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.drv.dsrv.schema.ElementType;
import de.extra.client.core.model.ConfigFileBean;
import de.extra.client.plugins.configPlugin.ConfigConstants;

public class ProfilHelper {

	private final Map<String, String> versandMap;

	private final Logger logger = Logger.getLogger(ProfilHelper.class);

	/**
	 * 
	 * Helper-Klasse f�r die Verarbeitung der Profildatei
	 * 
	 * @param versandMap
	 *            Map mit den festen Parametern aus der Properties-Datei
	 */

	public ProfilHelper(final Map<String, String> versandMap) {
		super();
		this.versandMap = versandMap;
	}

	/**
	 * 
	 * Funktion zum bef�llen der ConfigFileBean
	 * 
	 * @param profilListe
	 *            JaxB-Element der Profildatei
	 * @return ConfigFileBean
	 */
	public ConfigFileBean configFileBeanLoader(List<ElementType> profilListe) {

		ConfigFileBean cfb = new ConfigFileBean();

		for (Iterator iter = profilListe.iterator(); iter.hasNext();) {
			ElementType element = (ElementType) iter.next();

			// Ermitteln der Content-Art
			if (element.getElternelement() != null && element.getName() != null) {
				if (element.getElternelement().equalsIgnoreCase(
						ConfigConstants.PROFIL_DATA)) {

					// Setzen des Content-Types

					cfb.setContentType(element.getName());

				}

				// �berpr�fen ob Package-Element angegeben wurde
				if (element.getName().equalsIgnoreCase(
						ConfigConstants.PROFIL_PACKAGE)) {

					// Setzen ob PackageLayer ben�tigt

					cfb.setPackageLayer(true);

				}

				// �berpr�fen ob Message-Element angegeben wurde
				if (element.getName().equalsIgnoreCase(
						ConfigConstants.PROFIL_MESSAGE)) {

					// Setzen ob MessageLayer ben�tigt

					cfb.setMessageLayer(true);

				}
			}

		}

		return cfb;

	}

	/**
	 * 
	 * F�llen der Versandinformationen in die ConfigFileBean
	 * 
	 * @param cfb
	 *            Vorgef�llte ConfigFileBean
	 * @return ConfigFileBean
	 */

	public ConfigFileBean fuelleVersandInfo(ConfigFileBean cfb) {

		// Setzen des Testindikators

		if (versandMap.get("testIndicator") == null
				|| versandMap.get("testIndicator") == "") {

			if (logger.isDebugEnabled()) {

				logger.debug("Testindicator nicht gesetzt");

			}

			// Wenn Testindikator in den Properties nicht gesetzt ist

			cfb.setTestIndicator(true);

		} else {

			// Wert f�r Testindikator aus Properties �bernehmen

			cfb.setTestIndicator(new Boolean(versandMap.get("testIndicator")));

		}

		try {

			logger.debug("Setzen der Absenderinformationen");
			// Typ der Absenderinformationen
			cfb.setAbsClass(versandMap.get("absenderClass"));
			// Betriebsnummer
			cfb.setAbsBbnr(versandMap.get("absenderBbnr"));
			// Absendername
			cfb.setAbsName(versandMap.get("absenderName"));

			logger.debug("Setzen der Empf�ngerinformationen");

			// Typ der Empf�ngerinformationen
			cfb.setEmpfClass(versandMap.get("empfaengerClass"));
			// Betriebsnummer
			cfb.setEmpfBbnr(versandMap.get("empfaengerBbnr"));
			// Name des Empf�ngers
			cfb.setEmpfName(versandMap.get("empfaengerName"));

			logger.debug("Produktinformationen");

			// Name des Programms
			cfb.setProductName(versandMap.get("productName"));
			// Hersteller
			cfb.setProductManuf(versandMap.get("productManuf"));

			logger.debug("Verfahrensinformationen");

			// Procedure
			cfb.setProcedure(versandMap.get("procedure"));
			// DataType
			cfb.setDataType(versandMap.get("dataType"));
			// Scenario
			cfb.setScenario(versandMap.get("scenario"));

		} catch (Exception e) {
			logger.error("Fehler beim Bef�llen der Versandinformationen", e);
		}

		return cfb;

	}

	/**
	 * 
	 * Generiert zur Zeit aus dem TimeStamp die RequestId
	 * 
	 * @return requestId
	 */

	public String generateReqId() {

		return new String(Long.toString(System.currentTimeMillis()));
	}

}
