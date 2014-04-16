package de.drv.dsrv.spoc.commons.crypto;

import de.dsrv.core.crypto.CryptoException;
import de.dsrv.core.crypto.FideaHSClient;
import de.dsrv.core.crypto.FideaHSClientFactory;
import de.dsrv.core.crypto.FideaHSResponse;

/**
 * Implementiert die Methoden fuer die Verschluesselung und Entschluesselung von
 * Daten mittels dem Fidea Health Server.
 */
public class FideaHSHelperImpl implements FideaHSHelper {

	private final transient String[] verschluesselungURLs; // NOPMD

	private final transient String[] entschluesselungURLs; // NOPMD

	/**
	 * Konstruktor.
	 * 
	 * @param verschluesselungURLs
	 *            die URLs des Fidea Health Server, welche fuer die
	 *            Durchfuehrung der Verschluesselung verwendet werden sollen
	 * @param entschluesselungURLs
	 *            die URLs des Fidea Health Server, welche fuer die
	 *            Durchfuehrung der Entschluesselung verwendet werden sollen
	 */
	public FideaHSHelperImpl(final String[] verschluesselungURLs, // NOPMD
			final String[] entschluesselungURLs) { // NOPMD
		super();
		this.verschluesselungURLs = verschluesselungURLs;
		this.entschluesselungURLs = entschluesselungURLs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FideaHSResponse encrypt(final long id, // NOPMD
			final byte[] daten, final String betriebsnummerEmpfaenger, // NOPMD
			final String betriebsnummerSender) throws CryptoException { // NOPMD
		return getVerschluesselungClient().encrypt(id, daten, betriebsnummerEmpfaenger, betriebsnummerSender);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FideaHSResponse decrypt(final long id, // NOPMD
			final byte[] verschluesselteDaten) throws CryptoException { // NOPMD
		return getEntschluesselungClient().decrypt(id, verschluesselteDaten);
	}

	/**
	 * Ermittelt den Client zum Aufruf der Verschluesselung.
	 * 
	 * @return der Client zum Aufruf der Verschluesselung
	 * @throws CryptoException
	 *             falls bei Ermittlung des Clients zum Aufruf der
	 *             Verschluesselung ein technischer Fehler auftritt
	 */
	protected FideHSClientAdapter getVerschluesselungClient() throws CryptoException {
		return new FideHSClientAdapter(FideaHSClientFactory.getInstance(this.verschluesselungURLs,
				this.entschluesselungURLs).getEncryptClient());
	}

	/**
	 * Ermittelt den Client zum Aufruf der Entschluesselung.
	 * 
	 * @return der Client zum Aufruf der Entschluesselung
	 * @throws CryptoException
	 *             falls bei Ermittlung des Clients zum Aufruf der
	 *             Entschluesselung ein technischer Fehler auftritt
	 */
	protected FideHSClientAdapter getEntschluesselungClient() throws CryptoException {
		return new FideHSClientAdapter(FideaHSClientFactory.getInstance(this.verschluesselungURLs,
				this.entschluesselungURLs).getDecryptClient());
	}

	/**
	 * Adapter fuer den Client zum Zugriff auf den Fidea Health Server. Dieser
	 * Adapter ist notwendig, um die Testbarkeit der hier implementierten
	 * Klassen und Methoden zu gewaehrleisten.
	 */
	protected static class FideHSClientAdapter {
		private final transient FideaHSClient fideaHSClient;

		/**
		 * Konstruktor.
		 * 
		 * @param fideaHSClient
		 *            der Client zum Zugriff auf den Fidea Health Server
		 */
		public FideHSClientAdapter(final FideaHSClient fideaHSClient) {
			super();
			this.fideaHSClient = fideaHSClient;
		}

		/**
		 * @see FideaHSClient#decrypt(long, byte[])
		 */
		public FideaHSResponse decrypt(final long id, final byte[] encryptedData) // NOPMD
				throws CryptoException {
			return this.fideaHSClient.decrypt(id, encryptedData);
		}

		/**
		 * @see FideaHSClient#encrypt(long, byte[], String, String)
		 */
		public FideaHSResponse encrypt(final long id, final byte[] data, // NOPMD
				final String empfBBNR, final String sendBBNR) throws CryptoException {
			return this.fideaHSClient.encrypt(id, data, empfBBNR, sendBBNR);
		}
	}
}
