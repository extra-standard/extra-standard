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

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import de.extra.client.core.ReturnCode;
import de.extra.client.exit.SystemExiter;

/**
 * @author Thorsten Vogel
 * @version $Id$
 */
public class ClientArgumentsTest {

    private static final SystemExiter EXITER = new NotExiter();

    private static final String MANDANT_NAME = "DRV";

    static class NotExiter implements SystemExiter {
        @Override
        public void exit(final ReturnCode code) {
            // do nothing
        }
    }

    @Test
    public void testClientArguments() throws Exception {
        final Resource globalConfigDir = new ClassPathResource("testglobalconfig");
        final String globalConfigPath = globalConfigDir.getFile().getAbsolutePath();

        final Resource configDir = new ClassPathResource("testconfig");
        final String configPath = configDir.getFile().getAbsolutePath();

        final Resource logDir = new ClassPathResource("testlog");
        final String logPath = logDir.getFile().getAbsolutePath();

        final String[] args = {
                "-m", MANDANT_NAME,
                "-g", globalConfigPath,
                "-c", configPath,
                "-l", logPath,
                "-oc", "xxx",
                "-of", "yyy"
        };

        final ClientArgumentParser arguments = new ClientArgumentParser(args, EXITER);
        final ExtraClientParameters parameters = arguments.parseArgs();
        assertEquals(MANDANT_NAME, parameters.getMandant());
        assertEquals(globalConfigDir.getFile(), parameters.getGlobalConfigurationDirectory());
        assertEquals(configDir.getFile(), parameters.getConfigurationDirectory());
        assertEquals(logDir.getFile(), parameters.getLogDirectory());
        assertEquals("xxx", parameters.getOutputConfirm());
        assertEquals("yyy", parameters.getOutputFailure());
    }

    @Test
    public void testParseNonArgs() {
        final ClientArgumentParser arguments = new ClientArgumentParser(null, EXITER);
        final ExtraClientParameters parameters = arguments.parseArgs();
        assertTrue(parameters.hasErrors());
    }

    @Test
    public void testHelp() throws Exception {
        final ClientArgumentParser arguments = new ClientArgumentParser(new String[] { "-h" }, EXITER);
        final ExtraClientParameters parameters = arguments.parseArgs();
        assertFalse(parameters.hasErrors());
        assertTrue(parameters.getShowHelp());
    }

}
