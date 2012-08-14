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

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ClientStarter {

	static{
		// Startzeitpunkt als System.property setzen, zum Beispiel für Log4J Dateinamen
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	    System.setProperty("current.date", dateFormat.format(new Date()));
	}

	private static Logger logger = Logger.getLogger(ClientStarter.class);

	/**
	 * Wertet Kommandozeilenparameter aus.
	 * 
	 * @param args Kommandozeilenparameter
	 * @return auszuführende Aktion des Clients
	 */
	private static ClientArguments.ClientActions evaluateArgs(String[] args) {
		
		ClientArguments clientArgs = new ClientArguments(args);
		
		// Log-Level laut Kommandozeile einstellen
		Level selectedLogLevel = clientArgs.getSelectedLogLevel();
		if (selectedLogLevel != null) {
			LogManager.getRootLogger().setLevel(selectedLogLevel);
			LogManager.getLoggerRepository().setThreshold(selectedLogLevel);
		}
	
		// auszuführende Aktion bestimmen
		ClientArguments.ClientActions action = clientArgs.getSelectedAction();
		if (ClientArguments.ClientActions.PRINT_HELP.equals(action)) {
			clientArgs.printHelpText(new PrintWriter(System.out, true));
		}
		
		return action;
	}
	
	/**
	 * Main 
	 * 
	 * @param args Kommandozeilenparameter
	 */
	public static void main(String[] args) {
		ExtraClient extraClient = new ExtraClient();
		int returnCode = 0;
		try {
			
			ClientArguments.ClientActions action = evaluateArgs(args);
			OpLogger.log.info("Eingabeparameter: " + Arrays.toString(args));
			
			if (ClientArguments.ClientActions.PROCESS.equals(action)) {
				OpLogger.log.info("Start der Verarbeitung " + OpLogger.timestampFormat.format(new Date()));
				
				extraClient.execute();
				
				OpLogger.log.info("Ende der Verarbeitung " + OpLogger.timestampFormat.format(new Date()));
				returnCode = OpLogger.exitStatus;
			}
			
			if (returnCode != 0) {
				logger.error("Fehler bei der Verarbeitung: " + returnCode);

			} else {
				logger.info("Verarbeitung erfolgreich");
			}
		} catch (Exception e) {
			logger.error("Fehler bei der Verarbeitung", e);
			returnCode = OpLogger.STATUS_LOGICAL_ERROR;
		}
		System.exit(returnCode);
	}
}
