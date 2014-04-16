package de.drv.dsrv.spoc.util.zertifikat;

import java.security.cert.X509Certificate;

/**
 * Implementiert die Hilfs-Methoden fuer den Zugriff auf die Zertifikate.
 */
public class ZertifikatHelperImpl implements ZertifikatHelper {

	public static final String BETRIEBSNUMMER_PRAEFIX = "OU=BN";

	private static final int BETRIEBSNUMMER_START_POSITION = 5;

	private static final int BETRIEBSNUMMER_LAENGE = 8;

	/**
	 * {@inheritDoc}
	 * 
	 * Hier wird zunaechst geprueft, ob das uebergebene Array mit Zertifikaten
	 * gueltig (ungleich <code>null</code>) ist und mindestens ein Zertifikat
	 * enthaelt. Wenn dies der Fall ist, so wird die Betriebsnummer (laut
	 * Vereinbarung) aus dem ersten Zertifikat ermittelt und zurueckgegeben.
	 */
	@Override
	public String ermittleBetriebsnummer(final X509Certificate[] zertifikate) {
		String betriebsnummer = null;

		// Pruefe, ob Zertifikate vorhanden sind
		if ((zertifikate != null) && (zertifikate.length >= 1)) {
			// Das erste Zertifikat ist immer das Client-Zertifikat
			final X509Certificate zertifkat = zertifikate[0];

			// Pruefe "Subject Distinguished Name"
			if ((zertifkat.getSubjectDN() != null)
					&& (zertifkat.getSubjectDN().getName() != null)) {
				// Ermittle "Subject Distinguished Name" des Zertifikats
				final String name = zertifkat.getSubjectDN().getName();

				// Ermittle Startposition der Betriebsnummer
				final int betriebsnummerStart = name
						.indexOf(BETRIEBSNUMMER_PRAEFIX);

				// Pruefe, ob eine Betriebsnummer gespeichert ist
				if (betriebsnummerStart != -1) {
					// Ermittle Betriebsnummer
					betriebsnummer = name.substring(betriebsnummerStart
							+ BETRIEBSNUMMER_START_POSITION,
							betriebsnummerStart + BETRIEBSNUMMER_START_POSITION
									+ BETRIEBSNUMMER_LAENGE);
				}
			}
		}

		// Gebe ermittelte Betriebsnummer zurueck
		return betriebsnummer;
	}
}
