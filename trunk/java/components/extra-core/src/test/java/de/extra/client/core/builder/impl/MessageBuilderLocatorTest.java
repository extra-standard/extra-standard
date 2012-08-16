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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.ReflectionUtils;

import de.extra.client.core.builder.IXmlComplexTypeBuilder;
import de.extra.client.core.builder.IXmlRootElementBuilder;
import de.extra.client.core.builder.impl.plugins.DataTransformConfigurablePluginsBuilder;
import de.extra.client.core.builder.impl.plugins.DataTransformPluginsBuilder;
import de.extra.client.core.builder.impl.request.RequestTransportBuilder;
import de.extra.client.core.model.SenderDataBean;

/**
 * Test for PluginsLocatorManager.
 * 
 * @author Leonid Potap
 * @since 1.0.0
 * @version 1.0.0
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageBuilderLocatorTest {

	/**
	 * Test method for
	 * {@link de.extra.client.core.builder.impl.MessageBuilderLocator#getXmlComplexTypeBuilder(java.lang.String, de.extra.client.core.model.SenderDataBean)}
	 * .
	 */
	@Test
	public void testGetXmlComplexDefaultTypeBuilder() {
		Map<String, IXmlComplexTypeBuilder> complexTypeBuilderMap = new HashMap<String, IXmlComplexTypeBuilder>();
		IXmlComplexTypeBuilder expectedXmlComplexTypeBuilder = new DataTransformConfigurablePluginsBuilder();
		complexTypeBuilderMap.put("test1", expectedXmlComplexTypeBuilder);
		MessageBuilderLocator messageBuilderLocator = createMessageBuilderlocator(complexTypeBuilderMap);
		IXmlComplexTypeBuilder currentXmlComplexTypeBuilder = messageBuilderLocator
				.getXmlComplexTypeBuilder(
						expectedXmlComplexTypeBuilder.getXmlType(),
						new SenderDataBean());
		Assert.assertEquals("Unexpected XmlComplexTypeBuilder found",
				currentXmlComplexTypeBuilder, expectedXmlComplexTypeBuilder);
	}

	/**
	 * Test method for
	 * {@link de.extra.client.core.builder.impl.MessageBuilderLocator#getXmlComplexTypeBuilder(java.lang.String, de.extra.client.core.model.SenderDataBean)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetXmlComplexMultipleTypeBuilderException() {
		Map<String, IXmlComplexTypeBuilder> complexTypeBuilderMap = new HashMap<String, IXmlComplexTypeBuilder>();
		IXmlComplexTypeBuilder expectedXmlComplexTypeBuilder = new DataTransformConfigurablePluginsBuilder();
		complexTypeBuilderMap.put("test1", expectedXmlComplexTypeBuilder);
		complexTypeBuilderMap.put("test2", expectedXmlComplexTypeBuilder);
		MessageBuilderLocator messageBuilderLocator = createMessageBuilderlocator(complexTypeBuilderMap);
		messageBuilderLocator.getXmlComplexTypeBuilder(
				expectedXmlComplexTypeBuilder.getXmlType(),
				new SenderDataBean());
	}

	/**
	 * Test method for
	 * {@link de.extra.client.core.builder.impl.MessageBuilderLocator#getXmlComplexTypeBuilder(java.lang.String, de.extra.client.core.model.SenderDataBean)}
	 * .
	 */
	@Test
	public void testGetXmlComplexMultipleTypeBuilder() {
		Map<String, IXmlComplexTypeBuilder> complexTypeBuilderMap = new HashMap<String, IXmlComplexTypeBuilder>();
		IXmlComplexTypeBuilder expectedXmlComplexTypeBuilder = new DataTransformConfigurablePluginsBuilder();
		IXmlComplexTypeBuilder secondXmlComplexTypeBuilder = new DataTransformPluginsBuilder();
		complexTypeBuilderMap.put("test1", secondXmlComplexTypeBuilder);
		complexTypeBuilderMap.put("test2", expectedXmlComplexTypeBuilder);
		Properties properties = new Properties();
		String key = "builder."
				+ StringUtils.replace(
						expectedXmlComplexTypeBuilder.getXmlType(), ":", ".");
		properties.put(key, "test2");
		MessageBuilderLocator messageBuilderLocator = createMessageBuilderlocator(
				complexTypeBuilderMap, properties);
		// Implementierung setzen in Propertyes

		IXmlComplexTypeBuilder currentXmlComplexTypeBuilder = messageBuilderLocator
				.getXmlComplexTypeBuilder(
						expectedXmlComplexTypeBuilder.getXmlType(),
						new SenderDataBean());
		Assert.assertEquals("Unexpected XmlComplexTypeBuilder found",
				currentXmlComplexTypeBuilder, expectedXmlComplexTypeBuilder);
	}

	/**
	 * Test method for
	 * {@link de.extra.client.core.builder.impl.MessageBuilderLocator#getRootXmlBuilder(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetRootXmlBuilder() {
		Map<String, IXmlRootElementBuilder> rootElementsBuilderMap = new HashMap<String, IXmlRootElementBuilder>();
		IXmlRootElementBuilder xmlRootElementBuilder = new RequestTransportBuilder();
		rootElementsBuilderMap.put("testRootElementBuilder",
				xmlRootElementBuilder);
		Map<String, IXmlComplexTypeBuilder> complexTypeBuilderMap = new HashMap<String, IXmlComplexTypeBuilder>();

		MessageBuilderLocator messageBuilderLocator = createMessageBuilderlocator(
				rootElementsBuilderMap, complexTypeBuilderMap, null);

		IXmlRootElementBuilder expectedXmlRootElementBuilder = messageBuilderLocator
				.getRootXmlBuilder(xmlRootElementBuilder.getXmlType());
		Assert.assertEquals("Unexpected RootElementBuilder found",
				expectedXmlRootElementBuilder, xmlRootElementBuilder);
	}

	/**
	 * Die 2 Maps werden von dem Spring Kontainer befüllt. Danach wird init
	 * Merhod ausgeführt.
	 * 
	 * @param rootElementsBuilderMap
	 * @param complexTypeBuilderMap
	 * @return
	 */
	private MessageBuilderLocator createMessageBuilderlocator(
			Map<String, IXmlRootElementBuilder> rootElementsBuilderMap,
			Map<String, IXmlComplexTypeBuilder> complexTypeBuilderMap,
			Properties properties) {
		MessageBuilderLocator messageBuilderLocator = new MessageBuilderLocator();
		injectValue(messageBuilderLocator, rootElementsBuilderMap,
				"rootElementsBuilderMap");
		injectValue(messageBuilderLocator, complexTypeBuilderMap,
				"complexTypeBuilderMap");
		injectValue(messageBuilderLocator, properties, "configProperties");
		messageBuilderLocator.initMethod();
		return messageBuilderLocator;
	}

	/**
	 * Die Maps werden von dem Spring Kontainer befüllt. Danach wird init Merhod
	 * ausgeführt.
	 * 
	 * @param complexTypeBuilderMap
	 * @return
	 */
	private MessageBuilderLocator createMessageBuilderlocator(
			Map<String, IXmlComplexTypeBuilder> complexTypeBuilderMap) {
		Map<String, IXmlRootElementBuilder> rootElementsBuilderMap = new HashMap<String, IXmlRootElementBuilder>();
		return createMessageBuilderlocator(rootElementsBuilderMap,
				complexTypeBuilderMap, null);
	}

	/**
	 * Die Maps werden von dem Spring Kontainer befüllt. Danach wird init Merhod
	 * ausgeführt.
	 * 
	 * @param complexTypeBuilderMap
	 * @return
	 */
	private MessageBuilderLocator createMessageBuilderlocator(
			Map<String, IXmlComplexTypeBuilder> complexTypeBuilderMap,
			Properties propertiers) {
		Map<String, IXmlRootElementBuilder> rootElementsBuilderMap = new HashMap<String, IXmlRootElementBuilder>();
		return createMessageBuilderlocator(rootElementsBuilderMap,
				complexTypeBuilderMap, propertiers);
	}

	/**
	 * Setzt eine Value über ReflectionUtils
	 * 
	 * @param messageBuilderLocator
	 * @param rootElementsBuilderMap
	 * @param string
	 */
	private void injectValue(Object object, Object value, String fieldName) {
		Field fieldToSet = ReflectionUtils.findField(object.getClass(),
				fieldName);
		ReflectionUtils.makeAccessible(fieldToSet);
		ReflectionUtils.setField(fieldToSet, object, value);
	}

}
