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
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.extra.client.core.ClientProcessResult;
import de.extra.client.core.ReturnCode;
import de.extra.client.exit.JvmSystemExiter;
import de.extra.client.exit.SystemExiter;
import de.extra.client.logging.LogFileHandler;

/**
 * eXTRa-CLI Startklasse.
 *
 * @author DRV
 * @author Thorsten Vogel
 * @version $Id: ClientStarter.java 563 2012-09-06 14:15:35Z
 *          thorstenvogel@gmail.com $
 */
public class ClientStarter {

    private static final Logger LOG = LoggerFactory.getLogger(ClientStarter.class);

    private static final Logger opperation_logger = LoggerFactory.getLogger("de.extra.client.operation");

    // TODO KOnstante Definieren. Konfiguration
    private static final SystemExiter EXITER = new JvmSystemExiter();

    /**
     * Main
     *
     * @param args
     *            Kommandozeilenparameter
     */
    public static void main(final String[] args) {
        ReturnCode returnCode = ReturnCode.SUCCESS;

        final ClientArgumentParser clientArguments = new ClientArgumentParser(args, EXITER);
        final ExtraClientParameters parameters = clientArguments.parseArgs();

        // show errors
        if (parameters.hasErrors()) {
            clientArguments.printHelpText(null);
            parameters.printErrors();
            EXITER.exit(ReturnCode.TECHNICAL);
        }

        // only help display
        if (parameters.getShowHelp()) {
            clientArguments.printHelpText(null);
            EXITER.exit(returnCode);
        }

        opperation_logger.info("Eingabeparameter: " + Arrays.toString(args));

        final File configurationDirectory = parameters.getConfigurationDirectory();

        // initialisiert logging
        new LogFileHandler(parameters.getLogDirectory(), configurationDirectory);

        // config dir zur konfiguration des clients nutzen
        final ExtraClient extraClient = new ExtraClient(parameters);

        // (12.12.12) Aufruf Externer Anwendungen (Bestaetigung von
        // Output-Dateien)
        if (parameters.isExternalCall()) {
            // Externer Aufruf
            try {
                final ExternalCall externalCall = new ExternalCall();
                final boolean success = externalCall.executeExternalCall(parameters, extraClient);
                returnCode = success ? ReturnCode.SUCCESS : ReturnCode.TECHNICAL;
            } catch (final Exception e) {
                LOG.error("Fehler bei der Verarbeitung", e);
                returnCode = ReturnCode.TECHNICAL;
            }
        } else {
            // Normale eXTra-Client Verarbeitung (Phase...)
            try {
                final ClientProcessResult result = extraClient.execute();
                returnCode = result.getReturnCode();
            } catch (final Exception e) {
                LOG.error("Fehler bei der Verarbeitung", e);
                returnCode = ReturnCode.BUSINESS;
            }
        }
        if (returnCode.getCode() != 0) {
            LOG.error("Exit mit code " + returnCode);
        } else {
            LOG.info("Verarbeitung erfolgreich.");
        }
        EXITER.exit(returnCode);
    }

}
