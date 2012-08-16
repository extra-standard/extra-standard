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
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.util.Assert;

import de.extra.client.core.builder.IMessageBuilderLocator;
import de.extra.client.core.builder.IXmlComplexTypeBuilder;
import de.extra.client.core.builder.IXmlRootElementBuilder;
import de.extra.client.core.locator.PluginsLocatorManager;
import de.extra.client.core.model.SenderDataBean;

/**
 * MessageBuilderLocator sucht zu jedem elementType ein entsprechenen
 * MessageBuilder
 * 
 * @author Leonid Potap
 * 
 */
@Named("messageBuilderLocator")
public class MessageBuilderLocator implements IMessageBuilderLocator {

	private static final Logger logger = Logger
			.getLogger(PluginsLocatorManager.class);

	private static final String XML_BUILDER_KEY_SEPARATOR = ":";

	private static final String XML_BUILDER_CONFIG_KEY_PREFIX = "builder";

	@Inject
	@Named("extra-cli-properties")
	private Properties configProperties;

	/**
	 * Hier werden alle XMLRootElementbuilder injected
	 */
	@Inject
	Map<String, IXmlRootElementBuilder> rootElementsBuilderMap;

	/**
	 * In der InitMethode wird die Typ Map aufgebaut
	 */
	private Map<String, IXmlRootElementBuilder> xmlTypesToRootElementsBuilderAssignmentMap = new HashMap<String, IXmlRootElementBuilder>();

	/**
	 * Hier werden alle bekannten IXmlComplexTypeBuilder injected
	 */
	@Inject
	Map<String, IXmlComplexTypeBuilder> complexTypeBuilderMap;

	/**
	 * In diesem Map werden die dafault (Systemweit eindeutig)
	 * IXmlComplexTypeBuilder zu dem entsprechenen xmlType zugeordnet.
	 */
	private Map<String, IXmlComplexTypeBuilder> xmlDefaultTypesToComplexTypeBuilderAssignmentMap = new HashMap<String, IXmlComplexTypeBuilder>();

	/**
	 * In diesem Map werden die Konfigurierte IXmlComplexTypeBuilder zu dem
	 * entsprechenen xmlType zugeordnet.
	 */
	private Map<String, IXmlComplexTypeBuilder> xmlConfigurableTypesToComplexTypeBuilderAssignmentMap = new HashMap<String, IXmlComplexTypeBuilder>();

	/**
	 * In diesem Map werden die complexTypeBuilderMap zu dem entsprechenen
	 * xmlType zugeordnet.
	 */
	private List<String> xmlMultipleImplementationBuilder = new ArrayList<String>();

	@PostConstruct
	public void initMethod() {
		processXmlComplexTypeBuilder();

		processXmlRootElementBuilder();

	}

	/**
	 * Untersucht alle Implementierungen der XMLRootBuilder und ordnet
	 * implementierungen zu den xmlTypen
	 */
	private void processXmlRootElementBuilder() {
		Collection<IXmlRootElementBuilder> rootElementsBuilderEtrySet = rootElementsBuilderMap
				.values();
		for (IXmlRootElementBuilder rootElementBuilder : rootElementsBuilderEtrySet) {
			String rootElementBuilderXmlType = rootElementBuilder.getXmlType();
			xmlTypesToRootElementsBuilderAssignmentMap.put(
					rootElementBuilderXmlType, rootElementBuilder);
		}
	}

	/**
	 * Untersucht alle Implementierungen der IXmlComplexTypeBuilder und ordnet
	 * implementierungen zu den xmlTypen. Es wwerden doppelten Implementierungen
	 * untersucht und dazu eine in der Konfiguration definierte Implementierung
	 * ausgewählt.
	 */
	private void processXmlComplexTypeBuilder() {
		// Füllt typeMap mit Daten
		Collection<IXmlComplexTypeBuilder> entrySet = complexTypeBuilderMap
				.values();
		for (IXmlComplexTypeBuilder messageBuilder : entrySet) {
			String builderXmlType = messageBuilder.getXmlType();
			// prüfen ob eine Implementierung bereits angemeldet ist
			if (xmlMultipleImplementationBuilder.contains(builderXmlType)) {
				// Nothing To Do
			} else if (xmlDefaultTypesToComplexTypeBuilderAssignmentMap
					.containsKey(builderXmlType)) {
				// muss ich prüfen, ob die Version in der Konfiguration
				// vorhanden ist?
				xmlMultipleImplementationBuilder.add(builderXmlType);
				// Builder aus der defaultMap entfernen
				xmlDefaultTypesToComplexTypeBuilderAssignmentMap
						.remove(builderXmlType);
			} else {
				xmlDefaultTypesToComplexTypeBuilderAssignmentMap.put(
						builderXmlType, messageBuilder);
			}
		}
		// Prüfen die Liste der mehrfachen Implementierungen und eine
		// Vorkonfigurierte implementierung aussuchen.
		for (String currentBuilderXmlBype : xmlMultipleImplementationBuilder) {
			String configKey = calculateConfigKey(currentBuilderXmlBype);
			// Jetzt Implementierung holen und den Typ vergleichen
			String beanName = getBeanName(configKey);
			Assert.notNull(
					beanName,
					"Configuration with the key '"
							+ configKey
							+ "' for XmlBuilderType "
							+ currentBuilderXmlBype
							+ " not found, although multiple XmlComplexTypeBuilder defined system-wide.");
			IXmlComplexTypeBuilder complexTypeBuilder = complexTypeBuilderMap
					.get(beanName);

			validateComplexTypeBuilder(complexTypeBuilder,
					currentBuilderXmlBype, beanName);
			xmlConfigurableTypesToComplexTypeBuilderAssignmentMap.put(
					currentBuilderXmlBype, complexTypeBuilder);
		}
	}

	/**
	 * Holt aus der konfiguration die entsprechende BeanNaem
	 * 
	 * @param configKey
	 * @return
	 */
	private String getBeanName(String configKey) {
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
			IXmlComplexTypeBuilder complexTypeBuilder,
			String currentBuilderXmlBype, String beanName) {
		Assert.notNull(complexTypeBuilder, "XmlBuilder "
				+ currentBuilderXmlBype + " not found for Bean " + beanName);
		String compexTypeBuilderXmlType = complexTypeBuilder.getXmlType();
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
	private String calculateConfigKey(String builderXmlType) {
		// Es wäre Möglich ein key.pattern zu erstellen.

		String[] splittedBuilderXmlType = StringUtils.split(builderXmlType,
				XML_BUILDER_KEY_SEPARATOR);
		StringBuilder configKey = new StringBuilder(
				XML_BUILDER_CONFIG_KEY_PREFIX);

		for (String splittedBuilderXmlTypeString : splittedBuilderXmlType) {
			configKey.append(".");
			configKey.append(splittedBuilderXmlTypeString);
		}
		return configKey.toString();
	}

	/**
	 * Liefert ein MessageBuilder abhängig von dem ParentElement und Type von
	 * dem childElement
	 * 
	 * @param elementType
	 * @param senderData
	 * @return
	 */
	public IXmlComplexTypeBuilder getXmlComplexTypeBuilder(String elementType,
			SenderDataBean senderData) {
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
		logger.info("MessageBuilder  " + complexTypeBuilder
				+ " found for elementType: " + elementType);
		return complexTypeBuilder;
	}

	/**
	 * Liefert ein MessageBuilder abhängig von dem ParentElement und Type von
	 * dem childElement
	 * 
	 * @param elementType
	 * @return
	 */
	public IXmlRootElementBuilder getRootXmlBuilder(String elementType) {
		IXmlRootElementBuilder rootElementBuilder = xmlTypesToRootElementsBuilderAssignmentMap
				.get(elementType);
		return rootElementBuilder;
	}

}
