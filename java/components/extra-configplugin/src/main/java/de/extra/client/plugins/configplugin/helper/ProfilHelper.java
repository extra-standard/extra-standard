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
package de.extra.client.plugins.configplugin.helper;

import java.util.Iterator;
import java.util.List;

import javax.inject.Named;

import org.apache.log4j.Logger;

import de.drv.dsrv.schema.ElementType;
import de.extra.client.core.model.ConfigFileBean;
import de.extra.client.plugins.configplugin.ConfigConstants;

@Named("profilHelper")
public class ProfilHelper {

	private static final Logger LOGGER = Logger.getLogger(ProfilHelper.class);


	/**
	 * Funktion zum befüllen der ConfigFileBean.
	 * 
	 * @param profilListe
	 *            JaxB-Element der Profildatei
	 * @return ConfigFileBean
	 */
	public ConfigFileBean configFileBeanLoader(List<ElementType> profilListe) {
		ConfigFileBean cfb = new ConfigFileBean();

		for (Iterator<ElementType> iter = profilListe.iterator(); iter
				.hasNext();) {
			ElementType element = iter.next();

			// Ermitteln der Content-Art
			if (element.getElternelement() != null && element.getName() != null) {
				if (element.getElternelement().equalsIgnoreCase(
						ConfigConstants.PROFIL_DATA)) {
					// Setzen des Content-Types
					cfb.setContentType(element.getName());
				}

				// Ueberpruefen ob Package-Element angegeben wurde
				if (element.getName().equalsIgnoreCase(
						ConfigConstants.PROFIL_PACKAGE)) {
					// Setzen ob PackageLayer benötigt
					cfb.setPackageLayer(true);
				}

				// Ueberpruefen ob Message-Element angegeben wurde
				if (element.getName().equalsIgnoreCase(
						ConfigConstants.PROFIL_MESSAGE)) {
					// Setzen ob MessageLayer benötigt
					cfb.setMessageLayer(true);
				}
			}
		}
		return cfb;
	}

	/**
	 * Generiert zur Zeit aus dem TimeStamp die RequestId.
	 * 
	 * @return requestId
	 */
	public String generateReqId() {
		return new String(Long.toString(System.currentTimeMillis()));
	}
}
