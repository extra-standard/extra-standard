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
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * ExtraClient External Call Test.
 * 
 */
public class ExtraClientExternerCallTest {

	private final ExtraClientTestBasic extraClientTestBasic = new ExtraClientTestBasic();

	private static final String DRV = "DRV";

	private static final String GLOBAL_CONFIG_PATH = "/testglobalconfig";

	private static final String CONFIG_PATH = "/testconfig";

	private static final String LOG_DIR = "/testlog";

	protected ExtraClient extraClient;

	@Before
	public void setUp() throws Exception {
		
		extraClient = extraClientTestBasic.createExtraClient(DRV,
				GLOBAL_CONFIG_PATH, CONFIG_PATH, LOG_DIR);
	}

	@Test
	public void testExecute() throws Exception {
        final List<String> errors = Collections.<String>emptyList();

        final Resource globalConfigDir = new ClassPathResource("/testglobalconfig");
        final File globalConfigPath = globalConfigDir.getFile();

        final Resource configDir = new ClassPathResource("/testconfig");
        final File configPath = configDir.getFile();

        final Resource logDir = new ClassPathResource("/testlog");
        final File logPath = logDir.getFile();

        final Resource backupDir = new ClassPathResource("/testBackup");
        final File backupPath = backupDir.getFile();

        final ExtraClientParameters parameters = new ExtraClientParameters("mandant",
                globalConfigPath, configPath, logPath,
                "Test", null, backupPath, false, false, errors);
        ExternalCall externalCall = new ExternalCall();
        externalCall.executeExternalCall(parameters, extraClient);
	}

}
