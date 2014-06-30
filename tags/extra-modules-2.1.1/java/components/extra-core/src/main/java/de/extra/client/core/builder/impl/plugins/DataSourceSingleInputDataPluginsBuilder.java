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

import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import de.drv.dsrv.extrastandard.namespace.plugins.DataContainerType;
import de.drv.dsrv.extrastandard.namespace.plugins.DataSource;
import de.extra.client.core.builder.impl.XmlComplexTypeBuilderAbstr;
import de.extra.client.core.model.impl.DataSourcePluginDescription;
import de.extrastandard.api.exception.ExtraCoreRuntimeException;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;
import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.model.content.IInputDataPluginDescription;
import de.extrastandard.api.model.content.ISingleContentInputData;
import de.extrastandard.api.model.content.ISingleInputData;

/**
 * Erstellt ein Datasource XML Fragment für genau 1 Inputdata
 * 
 * @author Leonid Potap
 * 
 */
@Named("dataSourceSingleInputDataPluginsBuilder")
public class DataSourceSingleInputDataPluginsBuilder extends
		XmlComplexTypeBuilderAbstr {

	private static final Logger LOG = LoggerFactory
			.getLogger(DataSourceSingleInputDataPluginsBuilder.class);

	private static final String BUILDER_XML_MESSAGE_TYPE = "xplg:DataSource";

	/**
	 * Erstellt die SenderInformationen im Kontext von Header (non-Javadoc)
	 * 
	 * @see de.extra.client.core.builder.IXmlComplexTypeBuilder#buildXmlFragment(de.extra.client.core.model.SenderDataBean,
	 *      de.extra.client.core.model.ExtraProfileConfiguration)
	 */
	@Override
	public Object buildXmlFragment(final IInputDataContainer senderData,
			final IExtraProfileConfiguration config) {
		Assert.notNull(senderData, "InputDataContainer is null");
		final List<ISingleInputData> content = senderData.getContent();
		// Hier wird vorerst nur eine InputData erwartet
		Assert.notEmpty(content, "Keine InputDaten vorhanden");
		Assert.isTrue(content.size() == 1,
				"InputDataContainer beinhaltet mehr als 1 Element. Erwartet ist nur 1 Element");
		final ISingleInputData iSingleInputData = content.get(0);
		final DataSource dataSource = new DataSource();
		if (ISingleContentInputData.class.isAssignableFrom(iSingleInputData
				.getClass())) {
			final ISingleContentInputData singleContentInputData = ISingleContentInputData.class
					.cast(iSingleInputData);
			final DataSourcePluginDescription dataSourcePluginDescriptor = getDataSourcePluginDescriptor(singleContentInputData);
			if (dataSourcePluginDescriptor != null) {
				final DataContainerType dataContainer = new DataContainerType();
				dataContainer.setName(dataSourcePluginDescriptor.getName());
				dataSourcePluginDescriptor.getType();
				dataContainer.setType(dataSourcePluginDescriptor.getType()
						.getDataContainerCode());
				// 27.12.2012 Die Semantik des Feldes ist unklar. Ist das
				// Erstellungsdatum des Files?
				final GregorianCalendar calenderCreated = new GregorianCalendar();
				calenderCreated
						.setTime(dataSourcePluginDescriptor.getCreated());
				dataContainer.setCreated(calenderCreated);
				dataContainer.setEncoding(dataSourcePluginDescriptor
						.getEncoding());
				dataSource.setDataContainer(dataContainer);
			}
		}
		LOG.debug("DataSourcePlugin created.");
		return dataSource;

	}

	private DataSourcePluginDescription getDataSourcePluginDescriptor(
			final ISingleContentInputData singleContentInputData) {
		DataSourcePluginDescription dataSourcePluginDescription = null;
		for (final IInputDataPluginDescription inputDataPluginDescription : singleContentInputData
				.getPlugins()) {
			if (DataSourcePluginDescription.class
					.isAssignableFrom(inputDataPluginDescription.getClass())) {
				if (dataSourcePluginDescription != null) {
					throw new ExtraCoreRuntimeException(
							"MultipleDataSourcePluginDescriptors for iSingleInputData: "
									+ singleContentInputData
											.getInputIdentifier());
				}
				dataSourcePluginDescription = DataSourcePluginDescription.class
						.cast(inputDataPluginDescription);
			}
		}
		return dataSourcePluginDescription;
	}

	@Override
	public String getXmlType() {
		return BUILDER_XML_MESSAGE_TYPE;
	}

}
