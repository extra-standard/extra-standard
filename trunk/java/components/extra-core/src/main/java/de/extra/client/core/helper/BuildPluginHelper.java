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

import de.drv.dsrv.extrastandard.namespace.components.AnyPlugInContainerType;
import de.drv.dsrv.extrastandard.namespace.plugins.CompressionType;
import de.drv.dsrv.extrastandard.namespace.plugins.DataTransforms;
import de.drv.dsrv.extrastandard.namespace.plugins.EncryptionType;
import de.drv.dsrv.extrastandard.namespace.plugins.SignatureType;
import de.extra.client.core.model.CompressionPluginBean;
import de.extra.client.core.model.CompressionPluginHelper;
import de.extra.client.core.model.DataSourcePluginBean;
import de.extra.client.core.model.DataSourcePluginHelper;
import de.extra.client.core.model.EncryptionPluginBean;
import de.extra.client.core.model.EncryptionPluginHelper;
import de.extra.client.core.model.PlugindatenBean;
import de.extra.client.core.model.SignaturePluginBean;
import de.extra.client.core.model.SignaturePluginHelper;

public class BuildPluginHelper {

	/**
	 * Funktion zum Aufbau der Plugin-Informationen
	 * 
	 * @param pluginListe
	 *            Liste mit den PluginBeans
	 * @return AnyContainerType mit den Plugins f�r den Request
	 */
	public AnyPlugInContainerType buildPluginContainer(
			List<PlugindatenBean> pluginListe) {

		AnyPlugInContainerType pluginContainer = new AnyPlugInContainerType();
		DataTransforms dataTransforms = new DataTransforms();

		// Durchlaufen der Liste und bef�llen des Plugin-Bereichs

		for (Iterator i = pluginListe.iterator(); i.hasNext();) {
			PlugindatenBean pluginBean = (PlugindatenBean) i.next();

			// Pruefe ob gewaehlte Bean eine CompressionPluginBean ist und
			// befuelle
			// mit Informationen
			if (pluginBean instanceof CompressionPluginBean) {
				CompressionPluginHelper compPluginHelper = new CompressionPluginHelper();

				dataTransforms.getCompression().add(
						(CompressionType) compPluginHelper
								.getTransformElement(pluginBean));

			}

			// Pruefe ob gewaehlte Bean eine EncryptionPluginBean ist und
			// befuelle
			// mit Informationen

			if (pluginBean instanceof EncryptionPluginBean) {

				EncryptionPluginHelper encPluginHelper = new EncryptionPluginHelper();

				dataTransforms.getEncryption().add(
						(EncryptionType) encPluginHelper
								.getTransformElement(pluginBean));

			}

			// Pruefe ob gewaehlte Bean eine SignaturePluginBean ist und
			// befuelle mit Informationen

			if (pluginBean instanceof SignaturePluginBean) {

				SignaturePluginHelper sigPluginHelper = new SignaturePluginHelper();

				dataTransforms.getSignature().add(
						(SignatureType) sigPluginHelper
								.getTransformElement(pluginBean));

			}

			// Pruefe ob gewaehlte Bean eine DataSourcePluginBean ist und
			// befuelle
			// mit Informationen

			if (pluginBean instanceof DataSourcePluginBean) {

				DataSourcePluginHelper dataSourcePluginHelper = new DataSourcePluginHelper();

				pluginContainer.getAny().add(
						dataSourcePluginHelper.getPluginElement(pluginBean));

			}
		}

		pluginContainer.getAny().add(dataTransforms);

		return pluginContainer;
	}
}
