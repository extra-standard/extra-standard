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
