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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author Thorsten Vogel
 */
public class ExtraClientParametersTest {

    @Test
    public void testExtraClientParametersHelp() {
        final ExtraClientParameters parameters = new ExtraClientParameters();
        assertTrue(parameters.getShowHelp());
    }

    @Test
    public void testExtraClientParameters() throws Exception {
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
                "outputConfirm", "outputFailure", backupPath, false, false, errors);

        assertFalse(parameters.getShowHelp());

        assertEquals(globalConfigPath, parameters.getGlobalConfigurationDirectory());
        assertEquals(configPath, parameters.getConfigurationDirectory());
        assertEquals(logPath, parameters.getLogDirectory());
        assertEquals(errors, parameters.getErrors());
        assertEquals(backupPath, parameters.getBackupDirectory());
        assertEquals(true, parameters.shouldCreateBackup());
        assertEquals("mandant", parameters.getMandant());
        assertEquals("outputConfirm", parameters.getOutputConfirm());
        assertEquals("outputFailure", parameters.getOutputFailure());
    }
}
