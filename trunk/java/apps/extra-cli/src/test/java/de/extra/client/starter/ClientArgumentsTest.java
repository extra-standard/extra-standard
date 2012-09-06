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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import de.extra.client.exit.SystemExiter;

/**
 * @author Thorsten Vogel
 * @version $Id$
 */
public class ClientArgumentsTest {

	static class NotExiter implements SystemExiter {
		@Override
		public void exit(final ReturnCode code) {
			// do nothing
		}
	}

	/**
	 * Test method for {@link de.extra.client.starter.ClientArguments#ClientArguments(java.lang.String[])}.
	 */
	@Test
	public void testClientArguments() throws Exception {
		Resource configDir = new ClassPathResource("testconfig");
		String[] args = {"-c " + configDir.getFile().getAbsolutePath()};
		ClientArguments arguments = new ClientArguments(args, new NotExiter());
		arguments.parseArgs();
		assertNotNull(arguments.getConfigDirectory());
	}

	/**
	 * Test method for {@link de.extra.client.starter.ClientArguments#parseArgs()}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testParseNonArgs() {
		ClientArguments arguments = new ClientArguments(null, new NotExiter());
		arguments.parseArgs();
	}

	public void testHelp() throws Exception {
		ClientArguments arguments = new ClientArguments(new String[] {"-h"}, new NotExiter());
		arguments.parseArgs();
		assertTrue(arguments.isShowHelp());
	}

}
