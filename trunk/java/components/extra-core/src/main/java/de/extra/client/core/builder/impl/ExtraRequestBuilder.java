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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyValue;
import org.springframework.util.Assert;

import de.drv.dsrv.extrastandard.namespace.components.AnyPlugInContainerType;
import de.drv.dsrv.extrastandard.namespace.request.RequestTransport;
import de.extra.client.core.builder.IExtraRequestBuilder;
import de.extra.client.core.builder.IMessageBuilderLocator;
import de.extra.client.core.builder.IRequestTransportBuilder;
import de.extra.client.core.builder.IXmlComplexTypeBuilder;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;
import de.extrastandard.api.model.content.IInputDataContainer;

/**
 * Diese klasse steuer XmlBuilder Anhang konfiguration an und erstellt einen
 * XmlRootElement
 * 
 * @author Leonid Potap
 * @since 1.0.0
 * @version 1.0.0
 */
@Named("extraRequestBuilder")
public class ExtraRequestBuilder implements IExtraRequestBuilder {

	private static final Logger LOG = LoggerFactory
			.getLogger(ExtraRequestBuilder.class);

	@Inject
	@Named("messageBuilderLocator")
	IMessageBuilderLocator messageBuilderLocator;

	@Inject
	@Named("requestTransportBuilder")
	IRequestTransportBuilder requestTransportBuilder;

	@Override
	public RequestTransport buildExtraRequestMessage(
			final IInputDataContainer senderData,
			final IExtraProfileConfiguration config) {
		Assert.notNull(senderData, "SenderData is null");
		Assert.notNull(config, "ConfigFileBean is null");
		final String rootElementName = config.getRootElement();
		Assert.isTrue(
				requestTransportBuilder.getXmlType().equals(rootElementName),
				"Unexpected RootElement " + rootElementName + "expected: "
						+ requestTransportBuilder.getXmlType());

		final RequestTransport rootXmlFragment = requestTransportBuilder
				.buildRequestTransport(config);

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
	private void buildXmlMessage(final Object parentXmlFragement,
			final String parentElementName,
			final IExtraProfileConfiguration config,
			final IInputDataContainer senderData) {
		final List<String> childElements = config
				.getChildElements(parentElementName);
		for (final String childElementName : childElements) {
			final IXmlComplexTypeBuilder childElementComplexTypeBuilder = messageBuilderLocator
					.getXmlComplexTypeBuilder(childElementName, senderData);
			if (childElementComplexTypeBuilder == null) {
				throw new UnsupportedOperationException(
						"MessageBuilder for ElementType not found: "
								+ childElementName);
			}

			final Object xmlChildElement = childElementComplexTypeBuilder
					.buildXmlFragment(senderData, config);
			final String fieldName = config.getFieldName(parentElementName,
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
	private void setXmlElement(final Object parentXMLElement,
			final Object childXmlElement, final String fieldName) {
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
	private void processField(final Object parentXMLElement,
			final Object childXmlElement, final String propertyName) {
		final BeanWrapper beanWrapper = new BeanWrapperImpl(parentXMLElement);
		final PropertyValue propertyValue = new PropertyValue(propertyName,
				childXmlElement);
		beanWrapper.setPropertyValue(propertyValue);
		LOG.debug("{}", parentXMLElement);
	}

	/**
	 * Bearbeitet Plugins Elements
	 * 
	 * @param parentXMLElement
	 * @param childXmlElement
	 */
	private void processPlugins(final Object parentXMLElement,
			final Object childXmlElement) {
		// Bei dem AnyPlugInContainerType werden die childElements zu einem
		// List hinzugefügt
		final AnyPlugInContainerType parentAnyPlugInContainerType = (AnyPlugInContainerType) parentXMLElement;
		if (childXmlElement instanceof Collection<?>) {
			// Es kann sein, dass mehrere Plugins hinzugefügt werden
			final Collection<?> collectionChildXmlElement = (Collection<?>) childXmlElement;
			parentAnyPlugInContainerType.getAny().addAll(
					collectionChildXmlElement);
		} else {
			parentAnyPlugInContainerType.getAny().add(childXmlElement);
		}
	}
}
