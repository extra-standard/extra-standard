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

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyValue;
import org.springframework.util.Assert;

import de.drv.dsrv.extrastandard.namespace.components.AnyPlugInContainerType;
import de.drv.dsrv.extrastandard.namespace.components.RootElementType;
import de.extra.client.core.builder.IExtraMessageBuilder;
import de.extra.client.core.builder.IMessageBuilderLocator;
import de.extra.client.core.builder.IXmlComplexTypeBuilder;
import de.extra.client.core.builder.IXmlRootElementBuilder;
import de.extra.client.core.model.ConfigFileBean;
import de.extra.client.core.model.SenderDataBean;

/**
 * Diese klasse steuer XmlBuilder Anhang konfiguration an und erstellt einen
 * XmlRootElement
 * 
 * @author Leonid Potap
 * @since 1.0.0
 * @version 1.0.0
 */
@Named("extraRequestBuilder")
public class ExtraRequestBuilder implements IExtraMessageBuilder {

	private static final Logger logger = Logger
			.getLogger(ExtraRequestBuilder.class);

	@Inject
	@Named("messageBuilderLocator")
	IMessageBuilderLocator messageBuilderLocator;

	@Override
	public RootElementType buildXmlMessage(SenderDataBean senderData,
			ConfigFileBean config) {
		Assert.notNull(senderData, "SenderData is null");
		Assert.notNull(config, "ConfigFileBean is null");
		String rootElementName = config.getRootElement();
		IXmlRootElementBuilder rootElementBuilder = messageBuilderLocator
				.getRootXmlBuilder(rootElementName);
		RootElementType rootXmlFragment = rootElementBuilder
				.buildXmlRootElement(config);

		buildXmlMessage(rootXmlFragment, rootElementName, config, senderData);
		return rootXmlFragment;
	}

	/**
	 * <pre>
	 * Fügt ein XML Fragment zu dem ParentXmlFragement. Über die Konfiguration
	 * wird ein Builder gefunden und die Methode zum bauen der XML Nachricht aufgerufen.
	 * </pre>
	 * 
	 * @param parentXmlFragement
	 * @param parentElementName
	 * @param config
	 * @param senderData
	 */
	private void buildXmlMessage(Object parentXmlFragement,
			String parentElementName, ConfigFileBean config,
			SenderDataBean senderData) {
		List<String> childElements = config.getChildElements(parentElementName);
		for (String childElementName : childElements) {
			IXmlComplexTypeBuilder childElementComplexTypeBuilder = messageBuilderLocator
					.getXmlComplexTypeBuilder(childElementName, senderData);
			if (childElementComplexTypeBuilder == null) {
				throw new UnsupportedOperationException(
						"MessageBuilder for ElementType not found: "
								+ childElementName);
			}

			Object xmlChildElement = childElementComplexTypeBuilder
					.buildXmlFragment(senderData, config);
			String fieldName = config.getFieldName(parentElementName,
					childElementName);
			setXmlElement(parentXmlFragement, xmlChildElement, fieldName);
			buildXmlMessage(xmlChildElement, childElementName, config,
					senderData);
		}
	}

	/**
	 * Set XMLChildValue in XMLParenObjekt
	 * 
	 * @param parentXMLElement
	 * @param childXmlElement
	 * @param fieldName
	 */
	private void setXmlElement(Object parentXMLElement, Object childXmlElement,
			String fieldName) {
		if (parentXMLElement instanceof AnyPlugInContainerType) {
			processPlugins(parentXMLElement, childXmlElement);
		} else {
			processField(parentXMLElement, childXmlElement, fieldName);
		}
	}

	/**
	 * Set Fields.
	 * 
	 * @param parentXMLElement
	 * @param childXmlElement
	 * @param propertyName
	 */
	private void processField(Object parentXMLElement, Object childXmlElement,
			String propertyName) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(parentXMLElement);
		PropertyValue propertyValue = new PropertyValue(propertyName,
				childXmlElement);
		beanWrapper.setPropertyValue(propertyValue);
		logger.debug(parentXMLElement);
	}

	/**
	 * Bearbeitet Plugins Elements
	 * 
	 * @param parentXMLElement
	 * @param childXmlElement
	 */
	private void processPlugins(Object parentXMLElement, Object childXmlElement) {
		// Bei dem AnyPlugInContainerType werden die childElements zu einem
		// List hinzugefügt
		AnyPlugInContainerType parentAnyPlugInContainerType = (AnyPlugInContainerType) parentXMLElement;
		if (childXmlElement instanceof Collection<?>) {
			// Es kann sein, dass mehrere Plugins hinzugefügt werden
			Collection<?> collectionChildXmlElement = (Collection<?>) childXmlElement;
			parentAnyPlugInContainerType.getAny().addAll(
					collectionChildXmlElement);
		} else {
			parentAnyPlugInContainerType.getAny().add(childXmlElement);
		}
	}
}
