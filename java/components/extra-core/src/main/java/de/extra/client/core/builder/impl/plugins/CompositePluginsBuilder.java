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
package de.extra.client.core.builder.impl.plugins;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.extra.client.core.builder.IXmlComplexTypeBuilder;
import de.extra.client.core.builder.impl.XmlComplexTypeBuilderAbstr;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;
import de.extrastandard.api.model.content.IInputDataContainer;

/**
 * @author Leonid Potap Composite Builder wird angewendet, wenn mehrere Plugins
 *         nacheinander ausgeführt werden.
 *
 */
public class CompositePluginsBuilder extends XmlComplexTypeBuilderAbstr {

	private static final Logger LOG = LoggerFactory
			.getLogger(CompositePluginsBuilder.class);

	private static final String BUILDER_XML_MESSAGE_TYPE = "xplg:?";

	private List<IXmlComplexTypeBuilder> delegatedPluginslist = new LinkedList<IXmlComplexTypeBuilder>();

	/**
	 * Construktor. CompositePluginBuilder kann nur mit einem List von Builder
	 * instanziert werden.
	 *
	 * @param delegatedPluginslist
	 */
	public CompositePluginsBuilder(
			final List<IXmlComplexTypeBuilder> delegatedPluginslist) {
		this.delegatedPluginslist = delegatedPluginslist;
	}

	/**
	 * Erstellt die Plugins Fragment, in dem mehrere Konfgurierten Plugins
	 * nacheinander aufgerufen werden (non-Javadoc)
	 *
	 * @see de.extra.client.core.builder.IXmlComplexTypeBuilder#buildXmlFragment(de.extra.client.core.model.SenderDataBean,
	 *      de.extra.client.core.model.ExtraProfileConfiguration)
	 */
	@Override
	public Object buildXmlFragment(final IInputDataContainer senderData,
			final IExtraProfileConfiguration config) {
		List<Object> compositeXmlPluginFragment = new LinkedList<Object>();

		for (IXmlComplexTypeBuilder xmlComplexTypeBuilder : delegatedPluginslist) {
			Object xmlPluginFragment = xmlComplexTypeBuilder.buildXmlFragment(
					senderData, config);
			compositeXmlPluginFragment.add(xmlPluginFragment);
		}
		LOG.debug("Plugins created.");
		return compositeXmlPluginFragment;

	}

	@Override
	public String getXmlType() {
		return BUILDER_XML_MESSAGE_TYPE;
	}

}
