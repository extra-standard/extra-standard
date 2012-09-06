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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import de.extra.client.core.ClientProcessResult;

public class ExtraClientTestBasic {

	private static final Logger LOG = LoggerFactory.getLogger(ExtraClientTestBasic.class);

	/**
	 * Erstellt einen ExtraKlient mit der vorgegebenen Konfiguration Die
	 * Konfigurationsverzeichniss wird in dem Klassenpfad als ClassPathResource
	 * gesucht
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 *             , wenn die Konfigurationsverzeichnis nicht vorhanden ist.
	 * 
	 */
	public ExtraClient createExtraKlient(final String path) throws IOException {
		org.springframework.util.Assert.notNull(path, "Path is null");
		final ExtraClient extraClient = new ExtraClient(new ClassPathResource(path).getFile());
		return extraClient;
	}

	/**
	 * Führt die Verarbeitung aus und wertet die Fehlermeldungen
	 * 
	 * @param extraClient
	 * @throws Exception
	 */
	public void testExecute(final ExtraClient extraClient) throws Exception {
		final ClientProcessResult clientProcessResult = extraClient.execute();
		if (clientProcessResult.isSuccessful()) {
			assertTrue(true);
		} else {
			if (clientProcessResult.hasExceptions()) {
				LOG.error(clientProcessResult.exceptionsToString());
			}

			LOG.info(clientProcessResult.printReturnCodes());

			fail("Fehler bei der Verarbeitung!");
		}
	}
}
