package de.drv.dsrv.spoc.commons.crypto;

import de.dsrv.core.crypto.CryptoException;
import de.dsrv.core.crypto.FideaHSResponse;

/**
 * Definiert die Methoden fuer die Verschluesselung und Entschluesselung von
 * Daten mittels des Fidea Health Servers.
 */
public interface FideaHSHelper {

	/**
	 * Fuehrt die Verschluesselung der uebergebenen Daten durch.
	 * 
	 * @param id
	 *            die eindeutige ID der zu verschluesselnden Daten (wird fuer
	 *            das Logging verwendet)
	 * @param daten
	 *            die zu verschluesselnden Daten
	 * @param betriebsnummerEmpfaenger
	 *            die Betriebsnummer des Empfaengers an den die Daten gesendet
	 *            werden sollen
	 * @param betriebsnummerSender
	 *            die Betriebsnummer des Absenders
	 * @return das Datenobjekt, welches die Ergebnisse des
	 *         Verschluesselungsdienstes beinhaltet
	 * @throws CryptoException
	 *             wenn bei Durchfuehrung der Verschluesselung ein technischer
	 *             Fehler auftritt
	 */
	FideaHSResponse encrypt(final long id, // NOPMD
			final byte[] daten, final String betriebsnummerEmpfaenger, // NOPMD
			final String betriebsnummerSender) throws CryptoException; // NOPMD

	/**
	 * Fuehrt die Entschluesselung der uebergebenen Daten durch.
	 * 
	 * @param id
	 *            die eindeutige ID der zu entschluesselnden Daten (wird fuer
	 *            das Logging verwendet)
	 * @param verschluesselteDaten
	 *            die zu entschluesselnden Daten
	 * @return das Datenobjekt, welches die Ergebnisse des
	 *         Verschluesselungsdienstes beinhaltet
	 * @throws CryptoException
	 *             wenn bei Durchfuehrung der Entschluesselung ein technischer
	 *             Fehler auftritt
	 */
	FideaHSResponse decrypt(long id, // NOPMD
			byte[] verschluesselteDaten) throws CryptoException; // NOPMD
}
