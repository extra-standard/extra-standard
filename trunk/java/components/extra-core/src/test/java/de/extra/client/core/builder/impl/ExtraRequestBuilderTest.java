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
import de.extra.client.core.builder.impl.components.TransportBodyBase64CharSequenceBuilder;
import de.extra.client.core.builder.impl.components.TransportBodyDataBuilder;
import de.extra.client.core.builder.impl.components.TransportBodyElementSequenceBuilder;
import de.extra.client.core.builder.impl.components.TransportHeaderReceiverBuilder;
import de.extra.client.core.builder.impl.components.TransportHeaderRequestDetailsBuilder;
import de.extra.client.core.builder.impl.components.TransportHeaderSenderBuilder;
import de.extra.client.core.builder.impl.components.TransportHeaderTestIndicatorBuilder;
import de.extra.client.core.builder.impl.plugins.DataSourcePluginsBuilder;
import de.extra.client.core.builder.impl.plugins.TransportPluginsBuilder;
import de.extra.client.core.builder.impl.request.RequestTransportBodyBuilder;
import de.extra.client.core.builder.impl.request.RequestTransportBuilder;
import de.extra.client.core.builder.impl.request.RequestTransportHeaderBuilder;
import de.extra.client.core.model.ConfigFileBean;
import de.extra.client.core.model.SenderDataBean;

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
		IXmlRootElementBuilder xmlRootElementBuilder = new RequestTransportBuilder();
		String rootXmlType = xmlRootElementBuilder.getXmlType();
		when(messageBuilderLocator.getRootXmlBuilder(rootXmlType)).thenReturn(
				xmlRootElementBuilder);
		requiredXmlType = rootXmlType;

		Map<String, IXmlComplexTypeBuilder> builderMap = new HashMap<String, IXmlComplexTypeBuilder>();
		IXmlComplexTypeBuilder requestTransportHeaderBuilder = new RequestTransportHeaderBuilder();
		IXmlComplexTypeBuilder requestTransportBody = new RequestTransportBodyBuilder();
		IXmlComplexTypeBuilder transportHeaderSenderBuilder = new TransportHeaderSenderBuilder();
		IXmlComplexTypeBuilder transportHeaderReceiverBuilder = new TransportHeaderReceiverBuilder();
		IXmlComplexTypeBuilder transportHeaderTestIndicatorBuilder = new TransportHeaderTestIndicatorBuilder();
		IXmlComplexTypeBuilder transportHeaderRequestDetailsBuilder = new TransportHeaderRequestDetailsBuilder();
		IXmlComplexTypeBuilder transportBodyDataBuilder = new TransportBodyDataBuilder();
		IXmlComplexTypeBuilder transportBodyBase64CharSequenceBuilder = new TransportBodyBase64CharSequenceBuilder();
		IXmlComplexTypeBuilder transportBodyElementSequenceBuilder = new TransportBodyElementSequenceBuilder();
		IXmlComplexTypeBuilder dataSourcePluginsBuilder = new DataSourcePluginsBuilder();
		IXmlComplexTypeBuilder transportPluginsBuilder = new TransportPluginsBuilder();
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
		builderMap.put(transportBodyBase64CharSequenceBuilder.getXmlType(),
				transportBodyBase64CharSequenceBuilder);
		builderMap.put(transportBodyElementSequenceBuilder.getXmlType(),
				transportBodyElementSequenceBuilder);
		builderMap.put(transportPluginsBuilder.getXmlType(),
				transportPluginsBuilder);
		builderMap.put(dataSourcePluginsBuilder.getXmlType(),
				dataSourcePluginsBuilder);
		for (String key : builderMap.keySet()) {
			when(
					messageBuilderLocator.getXmlComplexTypeBuilder(
							Matchers.eq(key),
							(SenderDataBean) Matchers.anyObject())).thenReturn(
					builderMap.get(key));
		}
	}

	@Test
	public final void testBuildXmlSimleMessage() {
		SenderDataBean senderData = new SenderDataBean();
		ConfigFileBean config = createSimpleConfigFileBean();
		RootElementType elementType = extraRequestBuilder.buildXmlMessage(
				senderData, config);
		assertNotNull(elementType);
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void testBuildXmlSimleMessageWithException() {
		SenderDataBean senderData = new SenderDataBean();
		ConfigFileBean config = createSimpleConfigFileBean();
		config.addElementsHierarchyMap("TransportPlugins",
				"xplg:DataTransforms");
		RootElementType elementType = extraRequestBuilder.buildXmlMessage(
				senderData, config);
		assertNotNull(elementType);
	}

	private ConfigFileBean createSimpleConfigFileBean() {
		ConfigFileBean config = new ConfigFileBean();
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
