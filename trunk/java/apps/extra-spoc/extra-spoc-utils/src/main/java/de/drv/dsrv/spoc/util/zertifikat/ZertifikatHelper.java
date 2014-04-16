package de.drv.dsrv.spoc.util.zertifikat;

import java.security.cert.X509Certificate;

/**
 * Definiert die Hilfs-Methoden fuer den Zugriff auf die Zertifikate.
 */
public interface ZertifikatHelper {

	/**
	 * Ermittelt die in den uebergebenen Zertifikaten gespeicherte
	 * Betriebsnummer. <br />
	 * 
	 * @param zertifikate
	 *            die Zertifikate in welchen die Betriebsnummer gespeichert ist
	 * @return die aus den uebergebenen Zertifikaten ermittelte Betriebsnummer,
	 *         falls vorhanden; andernfalls <code>null</code>
	 */
	String ermittleBetriebsnummer(final X509Certificate[] zertifikate);
}
