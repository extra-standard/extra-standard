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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.ReflectionUtils;

import de.extra.client.core.builder.IXmlComplexTypeBuilder;
import de.extra.client.core.builder.IXmlRootElementBuilder;
import de.extra.client.core.builder.impl.plugins.DataTransformConfigurablePluginsBuilder;
import de.extra.client.core.builder.impl.plugins.DataTransformPluginsBuilder;
import de.extra.client.core.builder.impl.request.RequestTransportBuilder;
import de.extra.client.core.model.inputdata.impl.FileInputData;
import de.extra.client.core.util.IExtraValidator;
import de.extrastandard.api.model.content.IInputDataContainer;

/**
 * Test for PluginsLocatorManager.
 * 
 * @author Leonid Potap
 * @since 1.0.0
 * @version 1.0.0
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageBuilderLocatorTest {

	@Mock
	private IExtraValidator validator;

	// @Before
	// public void setUp() {
	// final Errors errors = mock(Errors.class);
	// when(validator.validate(anyObject(), errors));
	// }

	/**
	 * Test method for
	 * {@link de.extra.client.core.builder.impl.MessageBuilderLocator#getXmlComplexTypeBuilder(java.lang.String, de.extra.client.core.model.InputDataContainer)}
	 * .
	 */
	@Test
	public void testGetXmlComplexDefaultTypeBuilder() {
		final Map<String, IXmlComplexTypeBuilder> complexTypeBuilderMap = new HashMap<String, IXmlComplexTypeBuilder>();
		final IXmlComplexTypeBuilder expectedXmlComplexTypeBuilder = new DataTransformConfigurablePluginsBuilder();
		complexTypeBuilderMap.put("test1", expectedXmlComplexTypeBuilder);
		final MessageBuilderLocator messageBuilderLocator = createMessageBuilderlocator(complexTypeBuilderMap);
		final IInputDataContainer senderData = createTestinputData("Dummy Content");
		final IXmlComplexTypeBuilder currentXmlComplexTypeBuilder = messageBuilderLocator.getXmlComplexTypeBuilder(
				expectedXmlComplexTypeBuilder.getXmlType(), senderData);
		Assert.assertEquals("Unexpected XmlComplexTypeBuilder found", currentXmlComplexTypeBuilder,
				expectedXmlComplexTypeBuilder);
	}

	/**
	 * Test method for
	 * {@link de.extra.client.core.builder.impl.MessageBuilderLocator#getXmlComplexTypeBuilder(java.lang.String, de.extra.client.core.model.InputDataContainer)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetXmlComplexMultipleTypeBuilderException() {
		final Map<String, IXmlComplexTypeBuilder> complexTypeBuilderMap = new HashMap<String, IXmlComplexTypeBuilder>();
		final IXmlComplexTypeBuilder expectedXmlComplexTypeBuilder = new DataTransformConfigurablePluginsBuilder();
		complexTypeBuilderMap.put("test1", expectedXmlComplexTypeBuilder);
		complexTypeBuilderMap.put("test2", expectedXmlComplexTypeBuilder);
		final MessageBuilderLocator messageBuilderLocator = createMessageBuilderlocator(complexTypeBuilderMap);

		final IInputDataContainer senderData = createTestinputData("Dummy Content");
		messageBuilderLocator.getXmlComplexTypeBuilder(expectedXmlComplexTypeBuilder.getXmlType(), senderData);
	}

	/**
	 * @return
	 */
	private FileInputData createTestinputData(final String singleContent) {
		return new FileInputData(new ArrayList<String>(Arrays.asList(singleContent)));
	}

	/**
	 * Test method for
	 * {@link de.extra.client.core.builder.impl.MessageBuilderLocator#getXmlComplexTypeBuilder(java.lang.String, de.extra.client.core.model.InputDataContainer)}
	 * .
	 */
	@Test
	public void testGetXmlComplexMultipleTypeBuilder() {
		final Map<String, IXmlComplexTypeBuilder> complexTypeBuilderMap = new HashMap<String, IXmlComplexTypeBuilder>();
		final IXmlComplexTypeBuilder expectedXmlComplexTypeBuilder = new DataTransformConfigurablePluginsBuilder();
		final IXmlComplexTypeBuilder secondXmlComplexTypeBuilder = new DataTransformPluginsBuilder();
		complexTypeBuilderMap.put("test1", secondXmlComplexTypeBuilder);
		complexTypeBuilderMap.put("test2", expectedXmlComplexTypeBuilder);
		final Properties properties = new Properties();
		final String key = "builder." + StringUtils.replace(expectedXmlComplexTypeBuilder.getXmlType(), ":", ".");
		properties.put(key, "test2");
		final MessageBuilderLocator messageBuilderLocator = createMessageBuilderlocator(complexTypeBuilderMap,
				properties);
		// Implementierung setzen in Propertyes
		final IInputDataContainer senderData = createTestinputData("Dummy Content");
		final IXmlComplexTypeBuilder currentXmlComplexTypeBuilder = messageBuilderLocator.getXmlComplexTypeBuilder(
				expectedXmlComplexTypeBuilder.getXmlType(), senderData);
		Assert.assertEquals("Unexpected XmlComplexTypeBuilder found", currentXmlComplexTypeBuilder,
				expectedXmlComplexTypeBuilder);
	}

	/**
	 * Test method for
	 * {@link de.extra.client.core.builder.impl.MessageBuilderLocator#getRootXmlBuilder(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetRootXmlBuilder() {
		final Map<String, IXmlRootElementBuilder> rootElementsBuilderMap = new HashMap<String, IXmlRootElementBuilder>();
		final IXmlRootElementBuilder xmlRootElementBuilder = new RequestTransportBuilder();
		rootElementsBuilderMap.put("testRootElementBuilder", xmlRootElementBuilder);
		final Map<String, IXmlComplexTypeBuilder> complexTypeBuilderMap = new HashMap<String, IXmlComplexTypeBuilder>();

		final MessageBuilderLocator messageBuilderLocator = createMessageBuilderlocator(rootElementsBuilderMap,
				complexTypeBuilderMap, new Properties());

		final IXmlRootElementBuilder expectedXmlRootElementBuilder = messageBuilderLocator
				.getRootXmlBuilder(xmlRootElementBuilder.getXmlType());
		Assert.assertEquals("Unexpected RootElementBuilder found", expectedXmlRootElementBuilder, xmlRootElementBuilder);
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
			final Map<String, IXmlRootElementBuilder> rootElementsBuilderMap,
			final Map<String, IXmlComplexTypeBuilder> complexTypeBuilderMap, final Properties properties) {
		final MessageBuilderLocator messageBuilderLocator = new MessageBuilderLocator();
		injectValue(messageBuilderLocator, rootElementsBuilderMap, "rootElementsBuilderMap");
		injectValue(messageBuilderLocator, complexTypeBuilderMap, "complexTypeBuilderMap");
		injectValue(messageBuilderLocator, properties, "configBasicProperties");
		injectValue(messageBuilderLocator, new Properties(), "configUserProperties");
		injectValue(messageBuilderLocator, validator, "validator");
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
			final Map<String, IXmlComplexTypeBuilder> complexTypeBuilderMap) {
		final Map<String, IXmlRootElementBuilder> rootElementsBuilderMap = new HashMap<String, IXmlRootElementBuilder>();
		return createMessageBuilderlocator(rootElementsBuilderMap, complexTypeBuilderMap, new Properties());
	}

	/**
	 * Die Maps werden von dem Spring Kontainer befüllt. Danach wird init Merhod
	 * ausgeführt.
	 * 
	 * @param complexTypeBuilderMap
	 * @return
	 */
	private MessageBuilderLocator createMessageBuilderlocator(
			final Map<String, IXmlComplexTypeBuilder> complexTypeBuilderMap, final Properties properties) {
		final Map<String, IXmlRootElementBuilder> rootElementsBuilderMap = new HashMap<String, IXmlRootElementBuilder>();
		return createMessageBuilderlocator(rootElementsBuilderMap, complexTypeBuilderMap, properties);
	}

	/**
	 * Setzt eine Value über ReflectionUtils
	 * 
	 * @param messageBuilderLocator
	 * @param rootElementsBuilderMap
	 * @param string
	 */
	private void injectValue(final Object object, final Object value, final String fieldName) {
		final Field fieldToSet = ReflectionUtils.findField(object.getClass(), fieldName);
		ReflectionUtils.makeAccessible(fieldToSet);
		ReflectionUtils.setField(fieldToSet, object, value);
	}

}
