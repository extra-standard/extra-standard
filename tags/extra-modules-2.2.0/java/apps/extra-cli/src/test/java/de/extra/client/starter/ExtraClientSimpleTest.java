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

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import de.extra.client.core.model.inputdata.impl.SingleFileInputData;
import de.extrastandard.api.model.content.ISingleInputData;

/**
 * ExtraClientSimpleTest.
 * 
 * @author Lofi Dewanto
 */
public class ExtraClientSimpleTest {

	protected ExtraClient extraClient;

	@Before
	public void setUp() throws Exception {
		ExtraClientParameters parameters = new ExtraClientParameters();
		extraClient = new ExtraClient(parameters);
	}

	@Test
	public void testHandleInputFile() {
		File inputDataFile = new File(
				"src/test/resources/testDirectories/input/order.txt");
		ISingleInputData singleInputData = new SingleFileInputData(
				inputDataFile);
		extraClient.handleInputFile(singleInputData);

		assertTrue(true);
	}

}
