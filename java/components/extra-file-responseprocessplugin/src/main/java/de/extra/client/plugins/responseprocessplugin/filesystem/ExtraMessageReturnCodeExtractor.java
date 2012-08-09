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
package de.extra.client.plugins.responseprocessplugin.filesystem;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.drv.dsrv.extrastandard.namespace.components.FlagCodeType;
import de.drv.dsrv.extrastandard.namespace.components.FlagType;
import de.drv.dsrv.extrastandard.namespace.response.XMLTransport;

public class ExtraMessageReturnCodeExtractor {
	
	private static final Logger logger = Logger.getLogger(ExtraMessageReturnCodeExtractor.class);
	
	/**
	 * Liefert FlagCode aus der Nachricht
	 * @param extraResponse
	 * @return
	 */
	public FlagCodeType getReturnCode(XMLTransport extraResponse){
//		TODO Allgemeine Lösung 
		List<FlagType> flagList = new ArrayList<FlagType>();

		flagList = extraResponse.getTransportHeader().getResponseDetails()
				.getReport().getFlag();

		FlagType flag = flagList.get(0);

		FlagCodeType flagCode = flag.getCode();

		if (flagCode.getValue().equalsIgnoreCase("C00")
				|| flagCode.getValue().equalsIgnoreCase("I000")
				|| flagCode.getValue().equalsIgnoreCase("E98")) {

			logger.debug("Verarbeitung erfolgreich");

		} else {

			logger.debug("Verarbeitung nicht erfolgreich");

		}
		return flagCode;
	}
}
