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
package de.extra.client.core.builder.impl;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.drv.dsrv.extrastandard.namespace.components.RootElementType;
import de.extra.client.core.config.impl.ExtraProfileConfiguration;
import de.extra.client.core.model.inputdata.impl.ContentInputDataContainer;
import de.extrastandard.api.model.content.IContentInputDataContainer;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;

/**
 * Test for ExtraRequestBuilder.
 * 
 * @author Leonid Potap
 * @since 1.0.0
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-core.xml", "/spring-schema.xml",
		"/builder/spring-ittest-base64charsequencebuilder-properties.xml" })
public class ExtraRequestFileInputBase64CharSequenceBuilderIT {

	private static final Logger logger = LoggerFactory
			.getLogger(ExtraRequestFileInputBase64CharSequenceBuilderIT.class);

	private final String requiredXmlType = "xcpt:Transport";

	@Inject
	@Named("extraRequestBuilder")
	private ExtraRequestBuilder extraRequestBuilder;

	@Inject
	@Named("extraRequestBuilderITBasic")
	private ExtraRequestBuilderITBasic extraRequestBuilderITBasic;

	@Test
	public final void testBuildRequestFileInputBase64CharSequence() {
		final IContentInputDataContainer senderData = createTestDummyFileInputData();
		senderData.setRequestId("STERBEDATENABGLEICH-7777777");
		final IExtraProfileConfiguration config = createConfigFileBeanForElementSequenceWithListOfConfirmationOfReceipt();
		final RootElementType elementType = extraRequestBuilder
				.buildExtraRequestMessage(senderData, config);
		assertNotNull(elementType);
		final String messageAsString = extraRequestBuilderITBasic
				.getResultAsString(elementType);
		logger.debug("ExtraResponse: " + messageAsString);
	}

	/**
	 * @return
	 */
	private IContentInputDataContainer createTestDummyFileInputData() {
		final IContentInputDataContainer senderData = new ContentInputDataContainer(
				Arrays.asList("Extra Test Message 235 ö ä"));
		return senderData;
	}

	private ExtraProfileConfiguration createConfigFileBeanForElementSequenceWithListOfConfirmationOfReceipt() {
		final ExtraProfileConfiguration config = extraRequestBuilderITBasic
				.createTransportBodyWithDataConfigFileBean(requiredXmlType);

		config.addElementsHierarchyMap("Data", "xcpt:Base64CharSequence");

		config.addElementsHierarchyMap("TransportPlugins", "xplg:DataSource");

		return config;
	}
}
