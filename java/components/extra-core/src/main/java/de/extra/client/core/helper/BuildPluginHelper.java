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
package de.extra.client.core.helper;

import java.util.Iterator;
import java.util.List;

import javax.inject.Named;

import de.drv.dsrv.extrastandard.namespace.components.AnyPlugInContainerType;
import de.drv.dsrv.extrastandard.namespace.plugins.CompressionType;
import de.drv.dsrv.extrastandard.namespace.plugins.DataTransforms;
import de.drv.dsrv.extrastandard.namespace.plugins.EncryptionType;
import de.drv.dsrv.extrastandard.namespace.plugins.SignatureType;
import de.extra.client.core.model.CompressionPluginDescription;
import de.extra.client.core.model.CompressionPluginHelper;
import de.extra.client.core.model.DataSourcePluginDescription;
import de.extra.client.core.model.DataSourcePluginHelper;
import de.extra.client.core.model.EncryptionPluginDescription;
import de.extra.client.core.model.EncryptionPluginHelper;
import de.extra.client.core.model.SignaturePluginDescription;
import de.extra.client.core.model.SignaturePluginHelper;
import de.extrastandard.api.model.content.IInputDataPluginDescription;

@Named("pluginHelper")
public class BuildPluginHelper {

	/**
	 * Funktion zum Aufbau der Plugin-Informationen.
	 * 
	 * @param pluginListe
	 *            Liste mit den PluginBeans
	 * @return AnyContainerType mit den Plugins f�r den Request
	 */
	public AnyPlugInContainerType buildPluginContainer(
			List<IInputDataPluginDescription> pluginListe) {
		AnyPlugInContainerType pluginContainer = new AnyPlugInContainerType();
		DataTransforms dataTransforms = new DataTransforms();

		// Durchlaufen der Liste und befüllen des Plugin-Bereichs
		for (Iterator<IInputDataPluginDescription> i = pluginListe.iterator(); i.hasNext();) {
			IInputDataPluginDescription pluginBean = i.next();

			// Pruefe ob gewaehlte Bean eine CompressionPluginBean ist und
			// befuelle mit Informationen
			if (pluginBean instanceof CompressionPluginDescription) {
				CompressionPluginHelper compPluginHelper = new CompressionPluginHelper();

				dataTransforms.getCompression().add(
						(CompressionType) compPluginHelper
								.getTransformElement(pluginBean));

			}

			// Pruefe ob gewaehlte Bean eine EncryptionPluginBean ist und
			// befuelle mit Informationen
			if (pluginBean instanceof EncryptionPluginDescription) {
				EncryptionPluginHelper encPluginHelper = new EncryptionPluginHelper();

				dataTransforms.getEncryption().add(
						(EncryptionType) encPluginHelper
								.getTransformElement(pluginBean));

			}

			// Pruefe ob gewaehlte Bean eine SignaturePluginBean ist und
			// befuelle mit Informationen
			if (pluginBean instanceof SignaturePluginDescription) {
				SignaturePluginHelper sigPluginHelper = new SignaturePluginHelper();

				dataTransforms.getSignature().add(
						(SignatureType) sigPluginHelper
								.getTransformElement(pluginBean));
			}

			// Pruefe ob gewaehlte Bean eine DataSourcePluginBean ist und
			// befuelle mit Informationen
			if (pluginBean instanceof DataSourcePluginDescription) {
				DataSourcePluginHelper dataSourcePluginHelper = new DataSourcePluginHelper();

				pluginContainer.getAny().add(
						dataSourcePluginHelper.getPluginElement(pluginBean));
			}
		}
		pluginContainer.getAny().add(dataTransforms);

		return pluginContainer;
	}
}
