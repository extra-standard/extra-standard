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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.extra.client.core.ClientProcessResult;
import de.extra.client.exit.JvmSystemExiter;
import de.extra.client.exit.SystemExiter;

/**
 * @author DRV
 * @version $Id$
 */
public class ClientStarter {

	private static final Logger LOG = LoggerFactory.getLogger(ClientStarter.class);

	private static final String KEY_CURRENT_DATE = "current.date";

	private static final SystemExiter EXITER = new JvmSystemExiter();

	static {
		// Startzeitpunkt als System.property setzen, zum Beispiel für Log4J
		// Dateinamen
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		System.setProperty(KEY_CURRENT_DATE, dateFormat.format(new Date()));
	}

	/**
	 * Main
	 *
	 * @param args
	 *            Kommandozeilenparameter
	 */
	public static void main(final String[] args) {
		OpLogger.log.info("Eingabeparameter: " + Arrays.toString(args));
		ReturnCode returnCode = ReturnCode.SUCCESS;

		ClientArguments clientArguments = new ClientArguments(args);
		try {
			clientArguments.parseArgs();
		} catch (Exception e) {
			clientArguments.printHelpText(e);
			EXITER.exit(ReturnCode.TECHNICAL);
		}

		if (clientArguments.isShowHelp()) {
			clientArguments.printHelpText(null);
			EXITER.exit(returnCode);
		}

		File configDir = clientArguments.getConfigDirectory();
		// TODO config dir zur konfiguration des clients nutzen
		ExtraClient extraClient = new ExtraClient();
		try {

			OpLogger.log.info("Start der Verarbeitung "
					+ OpLogger.timestampFormat.format(new Date()));

			ClientProcessResult result = extraClient.execute();

			OpLogger.log.info("Ende der Verarbeitung "
					+ OpLogger.timestampFormat.format(new Date()));

			// TODO refactor
			returnCode = !result.isSuccessful() ? ReturnCode.BUSINESS :
				(result.hasExceptions() ? ReturnCode.TECHNICAL : ReturnCode.SUCCESS);

			if (returnCode.getCode() != 0) {
				LOG.error("Fehler bei der Verarbeitung: " + returnCode);
			} else {
				LOG.info("Verarbeitung erfolgreich");
			}

		} catch (Exception e) {
			LOG.error("Fehler bei der Verarbeitung", e);
			returnCode = ReturnCode.BUSINESS;
		}
		EXITER.exit(returnCode);
	}
}
