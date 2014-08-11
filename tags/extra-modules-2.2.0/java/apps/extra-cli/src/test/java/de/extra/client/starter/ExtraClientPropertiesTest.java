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

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 * @author Thorsten Vogel
 */
public class ExtraClientPropertiesTest {

    private final ExtraClientTestBasic extraClientTestBasic = new ExtraClientTestBasic();

    private static final String DRV = "DRV";

    private static final String GLOBAL_CONFIG_PATH = "/testglobalconfig";

    private static final String CONFIG_PATH = "/testconfig";

    private static final String LOG_DIR = "/testlog";

    protected ExtraClient extraClient;

    @Before
    public void setUp() throws Exception {
        extraClient = extraClientTestBasic.createExtraClient(DRV, GLOBAL_CONFIG_PATH, CONFIG_PATH, LOG_DIR);
    }

    @Test
    public void testProperties() throws Exception {
        final ApplicationContext context = extraClient.createApplicationContext();
        final Properties basicProperties = context.getBean(ExtraClient.BEAN_NAME_EXTRA_PROPERTIES_BASIC, Properties.class);
        assertEquals("DRV", basicProperties.get("extra.mandant"));
        assertEquals("drv property content", basicProperties.get("drv.property"));
    }

}
