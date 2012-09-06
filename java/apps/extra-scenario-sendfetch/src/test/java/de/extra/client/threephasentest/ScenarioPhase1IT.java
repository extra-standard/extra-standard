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

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.extra.client.core.ClientCore;
import de.extra.client.core.ClientProcessResult;

/**
 * @author DSRV
 * @version $Id$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/threephaseszenario/phase1/spring-properties.xml",
		"classpath:/threephaseszenario/phase1/spring-configuration-phase1-datasent.xml" })
public class ScenarioPhase1IT {

	private static Logger logger = LoggerFactory.getLogger(ScenarioPhase1IT.class);

	@Inject
	@Named("clientCore")
	private ClientCore clientCore;

	@Test
	public final void test() {
		final ClientProcessResult clientProcessResult = clientCore.process();

		final boolean successful = clientProcessResult.isSuccessful();
		Assert.assertTrue("Exception bei verarbeitung", successful);

	}

}
