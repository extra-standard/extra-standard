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

package de.extra.extraClientLight.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.extra.extraClientLight.model.RequestExtraBean;

public class RequestBeanValidator {

	private static Logger LOGGER = LoggerFactory
			.getLogger(RequestBeanValidator.class);

	public static boolean validateRequestBean(RequestExtraBean requestBean) {

		boolean isValid = true;

		// Pruefe Verfahren, Fachdienst, URL und Profile
		if (requestBean.getVerfahren().length() == 0) {
			LOGGER.warn("Verfahren nicht gesetzt");
			return false;
		}
		if (!checkUrl(requestBean.getFachdienst())) {
			LOGGER.warn("Fachdienst falsch gesetzt");
			return false;
		}
		if (!checkUrl(requestBean.getUrl())) {
			LOGGER.warn("Zielurl falsch gesetzt");
			return false;
		}
		if (!checkUrl(requestBean.getProfile())) {
			LOGGER.warn("Profil falsch gesetzt");
			return false;
		}

		// Pruefe Absender und Empfaenger

		if (!checkAdress(requestBean.getAbsender())) {

			return false;
		}
		if (!checkAdress(requestBean.getEmpfaenger())) {

			return false;
		}

		return isValid;
	}

	private static boolean checkUrl(String url) {
		boolean isValid = true;
		if (url != null) {
			if (!url.startsWith("http")) {
				isValid = false;
			}
		} else {

			isValid = false;
		}
		return isValid;
	}

	private static boolean checkAdress(String adress) {
		boolean isValid = true;
		if (adress != null) {
			if (adress.isEmpty()) {
				return false;
			}

		} else {
			return false;
		}

		return isValid;
	}

}
