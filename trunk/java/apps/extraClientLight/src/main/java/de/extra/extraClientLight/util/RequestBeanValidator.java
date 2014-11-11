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

import de.extra.extraClientLight.model.RequestExtraBean;

public class RequestBeanValidator {

	public static boolean validateRequestBean(RequestExtraBean requestBean) {

		boolean isValid = true;

		// Pruefe Verfahren, Fachdienst, URL und Profile
		if (!checkUrl(requestBean.getVerfahren())) {
			return false;
		}
		if (!checkUrl(requestBean.getFachdienst())) {
			return false;
		}
		if (!checkUrl(requestBean.getUrl())) {
			return false;
		}
		if (!checkUrl(requestBean.getProfile())) {
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
