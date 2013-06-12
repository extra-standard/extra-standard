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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import de.extra.client.core.builder.IMessageBuilderLocator;
import de.extra.client.core.builder.IXmlComplexTypeBuilder;
import de.extra.client.core.builder.impl.plugins.CompositePluginsBuilder;
import de.extra.client.core.util.IExtraValidator;
import de.extrastandard.api.model.content.IInputDataContainer;

/**
 * MessageBuilderLocator sucht zu jedem elementType ein entsprechenen
 * MessageBuilder
 * 
 * @author Leonid Potap
 * 
 */
@Named("messageBuilderLocator")
public class MessageBuilderLocator implements IMessageBuilderLocator {

	private static final Logger LOG = LoggerFactory
			.getLogger(MessageBuilderLocator.class);

	private static final String XML_BUILDER_KEY_SEPARATOR = ":";

	private static final String XML_BUILDER_CONFIG_KEY_PREFIX = "builder";

	private static final String PLUGIND_PREFIX = "xplg";

	private static final int DEFAULT_MAX_PLUGIN_COUNT = 9;

	@Value("${messagebuilderlocator.plugins.messages.count:9}")
	private final Integer maxPluginsMessageCount = DEFAULT_MAX_PLUGIN_COUNT;

	@Inject
	@Named("_extern_extra-properties-basic")
	private Properties configBasicProperties;

	@Inject
	@Named("_extern_extra-properties-user")
	private Properties configUserProperties;

	/**
	 * merged User and Basic Properties
	 */
	private final Properties configProperties = new Properties();

	@Inject
	@Named("extraValidator")
	private IExtraValidator validator;

	/**
	 * Hier werden alle bekannten IXmlComplexTypeBuilder injected
	 */
	@Inject
	private Map<String, IXmlComplexTypeBuilder> complexTypeBuilderMap;

	/**
	 * In diesem Map werden die dafault (Systemweit eindeutig)
	 * IXmlComplexTypeBuilder zu dem entsprechenen xmlType zugeordnet.
	 */
	private final Map<String, IXmlComplexTypeBuilder> xmlDefaultTypesToComplexTypeBuilderAssignmentMap = new HashMap<String, IXmlComplexTypeBuilder>();

	/**
	 * In diesem Map werden die Konfigurierte IXmlComplexTypeBuilder zu dem
	 * entsprechenen xmlType zugeordnet.
	 */
	private final Map<String, IXmlComplexTypeBuilder> xmlConfigurableTypesToComplexTypeBuilderAssignmentMap = new HashMap<String, IXmlComplexTypeBuilder>();

	/**
	 * In diesem Map werden die complexTypeBuilderMap zu dem entsprechenen
	 * xmlType zugeordnet.
	 */
	private final List<String> xmlMultipleImplementationBuilderList = new ArrayList<String>();

	@PostConstruct
	public void initMethod() {
		configProperties.putAll(configBasicProperties);
		configProperties.putAll(configUserProperties);

		processXmlComplexTypeBuilder();

	}

	/**
	 * Liefert ein MessageBuilder abhängig von dem ParentElement und Type von
	 * dem childElement
	 * 
	 * @param elementType
	 * @param senderData
	 * @return
	 */
	@Override
	public IXmlComplexTypeBuilder getXmlComplexTypeBuilder(
			final String elementType, final IInputDataContainer senderData) {
		// TODO Auswahl der Builder auch über Context (SenderDataBean)
		// vorgenommen werden. Konzept erstellen
		IXmlComplexTypeBuilder complexTypeBuilder = xmlDefaultTypesToComplexTypeBuilderAssignmentMap
				.get(elementType);
		if (complexTypeBuilder == null) {
			complexTypeBuilder = xmlConfigurableTypesToComplexTypeBuilderAssignmentMap
					.get(elementType);
		}
		if (complexTypeBuilder == null) {
			throw new BeanCreationException(
					"XmlComplexTypeBuilder for ElementType " + elementType
							+ " not found");
		}
		LOG.info("MessageBuilder  " + complexTypeBuilder
				+ " found for elementType: " + elementType);
		validator.validate(complexTypeBuilder);

		return complexTypeBuilder;
	}

	/**
	 * Untersucht alle Implementierungen der IXmlComplexTypeBuilder und ordnet
	 * implementierungen zu den xmlTypen. Es werden doppelten Implementierungen
	 * untersucht und dazu eine in der Konfiguration definierte Implementierung
	 * ausgewählt.
	 */
	private void processXmlComplexTypeBuilder() {
		analyseComplexBuilderMap();
		analyseMultipleImplementations();
	}

	/**
	 * Analysiert die mehrfachen Implementierungen und sucht die Konfigurierten
	 * beans über den Schlüssel. Für die Plugins Builder können mehrere oder
	 * keine Implementierungen konfiguriert sein..
	 */
	/**
	 *
	 */
	private void analyseMultipleImplementations() {
		for (final String currentBuilderXmlType : xmlMultipleImplementationBuilderList) {
			// Wenn es um xplg geht kann mehrere Implementierungen vorhanden
			// sein.
			IXmlComplexTypeBuilder complexTypeBuilder;
			if (isPluginMessageType(currentBuilderXmlType)) {
				complexTypeBuilder = getComplexTypeBuilderForPluginType(currentBuilderXmlType);
			} else {
				complexTypeBuilder = getComplexTypeBuilder(currentBuilderXmlType);
			}

			xmlConfigurableTypesToComplexTypeBuilderAssignmentMap.put(
					currentBuilderXmlType, complexTypeBuilder);
		}
	}

	/**
	 * Liefert einen Builder für ein xmlType
	 * 
	 * @param currentBuilderXmlType
	 * @return
	 */
	private IXmlComplexTypeBuilder getComplexTypeBuilder(
			final String currentBuilderXmlType) {
		final String configKey = calculateConfigKey(currentBuilderXmlType);
		// Jetzt Implementierung holen und den Typ vergleichen
		final String beanName = getBeanName(configKey);
		Assert.notNull(
				beanName,
				"Configuration with the key '"
						+ configKey
						+ "' for XmlBuilderType "
						+ currentBuilderXmlType
						+ " not found, although multiple XmlComplexTypeBuilder defined system-wide.");
		final IXmlComplexTypeBuilder complexTypeBuilder = complexTypeBuilderMap
				.get(beanName);
		// Assert.notNull(complexTypeBuilder, "ComplexTypeBuilder for XmlType "
		// + currentBuilderXmlType + " not found");
		validateComplexTypeBuilder(complexTypeBuilder, currentBuilderXmlType,
				beanName);
		return complexTypeBuilder;
	}

	/**
	 * Liefert einen Builder für ein Plugin XmlType
	 * 
	 * @param xmlType
	 * @return
	 */
	private IXmlComplexTypeBuilder getComplexTypeBuilderForPluginType(
			final String xmlType) {
		final List<IXmlComplexTypeBuilder> pluginBuilderList = new LinkedList<IXmlComplexTypeBuilder>();
		// zuerst mal eine simple Key
		final String initialConfigKey = calculateConfigKey(xmlType);
		final String beanName = getBeanName(initialConfigKey);
		final IXmlComplexTypeBuilder complexTypeBuilder = complexTypeBuilderMap
				.get(beanName);
		if (complexTypeBuilder != null) {
			pluginBuilderList.add(complexTypeBuilder);
		}

		// Dann eine Key mit der Nummer als Suffix
		for (int counter = 1; counter < maxPluginsMessageCount; counter++) {
			final String currentConfigKey = initialConfigKey + counter;
			final String currentBeanName = getBeanName(currentConfigKey);
			if (currentBeanName != null) {
				final IXmlComplexTypeBuilder currenComplexTypeBuilder = complexTypeBuilderMap
						.get(currentBeanName);
				Assert.notNull(currenComplexTypeBuilder,
						"ComplexTypeBuilderBean with Name: " + beanName
								+ " not found");
				validateComplexTypeBuilder(currenComplexTypeBuilder, xmlType,
						currentBeanName);
				pluginBuilderList.add(currenComplexTypeBuilder);
			}
		}
		// Assert.notEmpty(pluginBuilderList,
		// "XmlComplexTypeBuilder for ElementType " + xmlType
		// + " not found");
		IXmlComplexTypeBuilder returnComplexTypeBuilder = null;
		if (!pluginBuilderList.isEmpty()) {
			if (pluginBuilderList.size() > 1) {
				returnComplexTypeBuilder = new CompositePluginsBuilder(
						pluginBuilderList);
			} else {
				returnComplexTypeBuilder = pluginBuilderList.get(0);
			}
		}

		return returnComplexTypeBuilder;
	}

	/**
	 * Eine Plugin Message hat der Prefix xplg
	 * 
	 * @param currentBuilderXmlBype
	 * @return
	 */
	private boolean isPluginMessageType(final String currentBuilderXmlBype) {
		boolean isPluginMessageType = false;
		if (currentBuilderXmlBype.startsWith(PLUGIND_PREFIX)) {
			isPluginMessageType = true;
		}
		return isPluginMessageType;
	}

	/**
	 * Analysiert das ComplexBuilderMap und filtert alle mehrfache
	 * Implementierungen in den xmlMultipleImplementationBuilderList. Die
	 * Implementierungen, die nur einmal vorhanden sind werden in den defaultMap
	 * aussortiert.
	 */
	private void analyseComplexBuilderMap() {
		final Collection<IXmlComplexTypeBuilder> entrySet = complexTypeBuilderMap
				.values();
		for (final IXmlComplexTypeBuilder messageBuilder : entrySet) {
			final String builderXmlType = messageBuilder.getXmlType();
			// prüfen ob eine Implementierung bereits angemeldet ist
			if (!xmlMultipleImplementationBuilderList.contains(builderXmlType)) {
				if (!xmlDefaultTypesToComplexTypeBuilderAssignmentMap
						.containsKey(builderXmlType)) {
					xmlDefaultTypesToComplexTypeBuilderAssignmentMap.put(
							builderXmlType, messageBuilder);
				} else {
					// die 2 Realisierung in den MultipleImplementationBuilder
					// hinzufügen
					xmlMultipleImplementationBuilderList.add(builderXmlType);
					// Builder aus der defaultMap entfernen
					xmlDefaultTypesToComplexTypeBuilderAssignmentMap
							.remove(builderXmlType);
				}
			}
		}
	}

	/**
	 * Holt aus der konfiguration die entsprechende BeanNaem
	 * 
	 * @param configKey
	 * @return
	 */
	private String getBeanName(final String configKey) {
		Assert.notNull(configProperties,
				"Unexpected Container Exception. ConfigProperties is null");
		return configProperties.getProperty(configKey);
	}

	/**
	 * Prüft. ob der gefundene Builder zu dem deklarierten Teil passt
	 * 
	 * @param complexTypeBuilder
	 * @param builderXmlType
	 */
	private void validateComplexTypeBuilder(
			final IXmlComplexTypeBuilder complexTypeBuilder,
			final String currentBuilderXmlBype, final String beanName) {
		Assert.notNull(complexTypeBuilder, "XmlBuilder "
				+ currentBuilderXmlBype + " not found for Bean " + beanName);
		final String compexTypeBuilderXmlType = complexTypeBuilder.getXmlType();
		if (!currentBuilderXmlBype.equals(compexTypeBuilderXmlType)) {
			throw new BeanCreationException(
					"Configured IXmlComplexTypeBuilder : " + complexTypeBuilder
							+ " does not match the desired XmlType: "
							+ currentBuilderXmlBype);
		}

	}

	/**
	 * Berechnet ConfigKey für ein mehrfach vorhandeten Builder
	 * 
	 * @param builderXmlType
	 * @return
	 */
	private String calculateConfigKey(final String builderXmlType) {
		// Es wäre Möglich ein key.pattern zu erstellen.

		final String[] splittedBuilderXmlType = StringUtils.split(
				builderXmlType, XML_BUILDER_KEY_SEPARATOR);
		final StringBuilder configKey = new StringBuilder(
				XML_BUILDER_CONFIG_KEY_PREFIX);

		for (final String splittedBuilderXmlTypeString : splittedBuilderXmlType) {
			configKey.append(".");
			configKey.append(splittedBuilderXmlTypeString);
		}
		return configKey.toString();
	}

}
