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
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.drv.dsrv.extrastandard.namespace.components.RootElementType;
import de.extra.client.core.builder.IXmlComplexTypeBuilder;
import de.extra.client.core.builder.IXmlRootElementBuilder;
import de.extra.client.core.builder.impl.components.TransportBodyDataBuilder;
import de.extra.client.core.builder.impl.components.TransportBodyFileInputBase64CharSequenceBuilder;
import de.extra.client.core.builder.impl.components.TransportBodyRequestQueryElementSequenceBuilder;
import de.extra.client.core.builder.impl.components.TransportHeaderReceiverBuilder;
import de.extra.client.core.builder.impl.components.TransportHeaderRequestDetailsBuilder;
import de.extra.client.core.builder.impl.components.TransportHeaderSenderBuilder;
import de.extra.client.core.builder.impl.components.TransportHeaderTestIndicatorBuilder;
import de.extra.client.core.builder.impl.plugins.DataSourcePluginsBuilder;
import de.extra.client.core.builder.impl.plugins.TransportPluginsBuilder;
import de.extra.client.core.builder.impl.request.RequestTransportBodyBuilder;
import de.extra.client.core.builder.impl.request.RequestTransportBuilder;
import de.extra.client.core.builder.impl.request.RequestTransportHeaderBuilder;
import de.extra.client.core.config.impl.ExtraProfileConfiguration;
import de.extra.client.core.model.inputdata.impl.DbQueryInputDataContainer;
import de.extrastandard.api.model.content.IDbQueryInputDataContainer;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;
import de.extrastandard.api.model.content.IInputDataContainer;

/**
 * Test for ExtraRequestBuilder.
 * 
 * @author Leonid Potap
 * @since 1.0.0
 * @version 1.0.0
 */
@RunWith(MockitoJUnitRunner.class)
public class ExtraRequestBuilderTest {

	@InjectMocks
	private ExtraRequestBuilder extraRequestBuilder;

	@Mock
	MessageBuilderLocator messageBuilderLocator;

	private String requiredXmlType;

	@Before
	public void setUp() throws Exception {
		// Builder Anmelden in dem MessageBuilderLocator
		final IXmlRootElementBuilder xmlRootElementBuilder = new RequestTransportBuilder();
		final String rootXmlType = xmlRootElementBuilder.getXmlType();
		when(messageBuilderLocator.getRootXmlBuilder(rootXmlType)).thenReturn(
				xmlRootElementBuilder);
		requiredXmlType = rootXmlType;

		final Map<String, IXmlComplexTypeBuilder> builderMap = new HashMap<String, IXmlComplexTypeBuilder>();
		final IXmlComplexTypeBuilder requestTransportHeaderBuilder = new RequestTransportHeaderBuilder();
		final IXmlComplexTypeBuilder requestTransportBody = new RequestTransportBodyBuilder();
		final IXmlComplexTypeBuilder transportHeaderSenderBuilder = new TransportHeaderSenderBuilder();
		final IXmlComplexTypeBuilder transportHeaderReceiverBuilder = new TransportHeaderReceiverBuilder();
		final IXmlComplexTypeBuilder transportHeaderTestIndicatorBuilder = new TransportHeaderTestIndicatorBuilder();
		final IXmlComplexTypeBuilder transportHeaderRequestDetailsBuilder = new TransportHeaderRequestDetailsBuilder();
		final IXmlComplexTypeBuilder transportBodyDataBuilder = new TransportBodyDataBuilder();
		final IXmlComplexTypeBuilder transportBodyFileInputBase64CharSequenceBuilder = new TransportBodyFileInputBase64CharSequenceBuilder();
		final IXmlComplexTypeBuilder transportBodyElementSequenceBuilder = new TransportBodyRequestQueryElementSequenceBuilder();
		final IXmlComplexTypeBuilder dataSourcePluginsBuilder = new DataSourcePluginsBuilder();
		final IXmlComplexTypeBuilder transportPluginsBuilder = new TransportPluginsBuilder();
		builderMap.put(requestTransportHeaderBuilder.getXmlType(),
				requestTransportHeaderBuilder);
		builderMap.put(requestTransportBody.getXmlType(), requestTransportBody);
		builderMap.put(transportHeaderSenderBuilder.getXmlType(),
				transportHeaderSenderBuilder);
		builderMap.put(transportHeaderReceiverBuilder.getXmlType(),
				transportHeaderReceiverBuilder);
		builderMap.put(transportHeaderTestIndicatorBuilder.getXmlType(),
				transportHeaderTestIndicatorBuilder);
		builderMap.put(transportHeaderRequestDetailsBuilder.getXmlType(),
				transportHeaderRequestDetailsBuilder);
		builderMap.put(transportBodyDataBuilder.getXmlType(),
				transportBodyDataBuilder);
		builderMap.put(
				transportBodyFileInputBase64CharSequenceBuilder.getXmlType(),
				transportBodyFileInputBase64CharSequenceBuilder);
		builderMap.put(transportBodyElementSequenceBuilder.getXmlType(),
				transportBodyElementSequenceBuilder);
		builderMap.put(transportPluginsBuilder.getXmlType(),
				transportPluginsBuilder);
		builderMap.put(dataSourcePluginsBuilder.getXmlType(),
				dataSourcePluginsBuilder);
		for (final String key : builderMap.keySet()) {
			when(
					messageBuilderLocator.getXmlComplexTypeBuilder(
							Matchers.eq(key),
							(IInputDataContainer) Matchers.anyObject()))
					.thenReturn(builderMap.get(key));
		}
	}

	@Test
	public final void testBuildXmlSimleMessage() {
		final IInputDataContainer senderData = createTestDummyDBQueryInputData();
		senderData.setRequestId("requestId");
		final IExtraProfileConfiguration config = createSimpleConfigFileBean();
		final RootElementType elementType = extraRequestBuilder
				.buildXmlMessage(senderData, config);
		assertNotNull(elementType);
	}

	// @Test(expected = UnsupportedOperationException.class)
	// public final void testBuildXmlSimleMessageWithException() {
	// final IInputDataContainer senderData = createTestDummyDBQueryInputData();
	// senderData.setRequestId("requestId");
	// final ExtraProfileConfiguration config = createSimpleConfigFileBean();
	// config.addElementsHierarchyMap("TransportPlugins",
	// "xplg:DataTransforms");
	// final RootElementType elementType =
	// extraRequestBuilder.buildXmlMessage(senderData, config);
	// assertNotNull(elementType);
	// }
	/**
	 * @return
	 */
	private IDbQueryInputDataContainer createTestDummyDBQueryInputData() {
		final DbQueryInputDataContainer senderData = new DbQueryInputDataContainer();
		senderData.addSingleDBQueryInputData(1L, "sourceRequestId 1",
				"sourceResponseId 1");
		senderData.addSingleDBQueryInputData(2L, "sourceRequestId 2 ",
				"sourceResponseId 2");
		senderData.setRequestId("requestId");
		return senderData;
	}

	private ExtraProfileConfiguration createSimpleConfigFileBean() {
		final ExtraProfileConfiguration config = new ExtraProfileConfiguration();
		config.setRootElement(requiredXmlType);
		config.addElementsHierarchyMap("XMLTransport", "req:TransportBody");
		config.addElementsHierarchyMap("XMLTransport", "req:TransportPlugins");
		config.addElementsHierarchyMap("XMLTransport", "req:TransportHeader");
		config.addElementsHierarchyMap("TransportHeader", "xcpt:Sender");
		config.addElementsHierarchyMap("TransportHeader", "xcpt:Receiver");
		config.addElementsHierarchyMap("TransportHeader", "xcpt:TestIndicator");
		config.addElementsHierarchyMap("TransportHeader", "xcpt:RequestDetails");
		config.addElementsHierarchyMap("TransportBody", "xcpt:Data");
		config.addElementsHierarchyMap("Data", "xcpt:ElementSequence");
		config.addElementsHierarchyMap("TransportPlugins", "xplg:DataSource");
		return config;
	}
}
