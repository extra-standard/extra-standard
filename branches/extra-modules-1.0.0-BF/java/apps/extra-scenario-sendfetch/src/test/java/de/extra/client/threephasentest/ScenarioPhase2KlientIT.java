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
package de.extra.client.threephasentest;

import org.junit.Before;
import org.junit.Test;

import de.extra.client.starter.ExtraClient;
import de.extra.client.starter.ExtraClientTestBasic;

public class ScenarioPhase2KlientIT {

	private final ExtraClientTestBasic extraClientTestBasic = new ExtraClientTestBasic();

	private ExtraClient extraClient;

	private static final String TEST_CONFIG = "/conf/phase2";

	private static final String LOG_DIR = "/logs";

	@Before
	public void setUp() throws Exception {

		extraClient = extraClientTestBasic.createExtraKlient(TEST_CONFIG,
				LOG_DIR);
	}

	@Test
	public void testExecute() throws Exception {
		extraClientTestBasic.testExecute(extraClient);
	}
}
