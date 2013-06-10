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
package de.extra.client.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import de.extra.client.core.ClientCore;
import de.extrastandard.api.exception.ExceptionCode;
import de.extrastandard.api.exception.ExtraConfigRuntimeException;
import de.extrastandard.api.model.execution.PersistentStatus;

/**
 * In dieser Klasse sind Externe Aufrufe untergebracht, deren Abarbeitung von
 * der normalen Verarbeitung abweichen. Ein Beispiel für einen solchen Aufruf
 * ist das Bestätigen von übermittelten Ergebnissen. In der eXTra-Verarbeitung
 * sind Ergebnisse empfangen worden, die sich noch im Status 'WAIT' befinden.
 * Eine externe Anwendung muss diese Ergebnisse prüfen und diese anschliessend
 * bestätigen.
 * 
 * @author r52gma
 * 
 */
class ExternalCall {
	private static final Logger opperation_logger = LoggerFactory
			.getLogger("de.extra.client.operation");

	ExternalCall() {
	}

	/**
	 * Führt den externen Aufruf durch. Erkannt wird der externe Aufruf durch
	 * die übergebenen 'ClientArguments'.
	 * 
	 * @param clientArguments
	 * @param extraClient
	 * @return true: externer Aufruf erfolgreich
	 */
	boolean executeExternalCall(final ClientArguments clientArguments,
			final ExtraClient extraClient) {
		opperation_logger.info("Starte Verarbeitung fuer Externen Aufruf.");
		try {
			final ApplicationContext applicationContext = extraClient
					.createApplicationContext();

			final ClientCore clientCore = applicationContext.getBean(
					"clientCore", ClientCore.class);

			// Aktuell (17.12.12) zwei Aufrufe moeglich:
			// -oc: OutputConfirm - Ergebnisdatei Bestaetigen
			// -of: OutputFailure - Ergebnisdatei konnte nicht verarbeitet
			// werden
			boolean success = false;
			final String outputConfirm = clientArguments.getOutputConfirm();
			final String outputFailure = clientArguments.getOutputFailure();
			final PersistentStatus persistentStatusNeu;
			final String outputIdentifier;
			final String aktion;
			if (outputConfirm != null && outputConfirm.length() > 0) {
				outputIdentifier = outputConfirm;
				persistentStatusNeu = PersistentStatus.DONE;
				aktion = "Externer Aufruf: Datei fehlerfrei verarbeitet, outputIdentifier: "
						+ outputIdentifier;
			} else if (outputFailure != null && outputFailure.length() > 0) {
				outputIdentifier = outputFailure;
				persistentStatusNeu = PersistentStatus.FAIL;
				aktion = "Externer Aufruf: Fehler bei Dateiverarbeitung, outputIdentifier: "
						+ outputIdentifier;
			} else {
				// Unbekannter Aufruf!
				opperation_logger.error("Unbekannter Aufruf!");
				throw new ExtraConfigRuntimeException(
						ExceptionCode.EXTRA_CONFIGURATION_EXCEPTION);
			}
			// Statusaenderung durchfuehren
			opperation_logger.info(aktion);
			success = clientCore
					.changeCommunicationProtocolStatusByOutputIdentifier(
							outputIdentifier, persistentStatusNeu);
			opperation_logger
					.info(success ? "Statusaenderung erfolgreich"
							: "Fehler bei Statusaenderung (falscher OutputIdentifier?)");
			return success;

		} catch (final Exception e) {
			opperation_logger.error("Fehler bei Externer Verarbeitung", e);
			throw new ExtraConfigRuntimeException(e);
		}

	}
	// outputConfirm()
}
