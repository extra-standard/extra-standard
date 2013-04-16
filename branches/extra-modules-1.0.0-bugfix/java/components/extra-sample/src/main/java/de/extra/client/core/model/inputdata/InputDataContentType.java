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
package de.extra.client.core.model.inputdata;

import java.util.HashMap;
import java.util.Map;

import de.drv.dsrv.extra.codelist.DataContainerCode;

/**
 * Enum für die Input Data Content Type
 * 
 * @author Leonid Potap
 * @version $Id:$
 */
public enum InputDataContentType {
	FILE("FILE");

	String type;

	DataContainerCode dataContainerCode;

	private final static Map<InputDataContentType, String> contentTypeToContainerCodeMap = new HashMap<InputDataContentType, String>();

	static {
		contentTypeToContainerCodeMap.put(FILE, DataContainerCode.FILE);
	}

	private InputDataContentType(final String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	/**
	 * @return the dataContainerCode
	 */
	public String getDataContainerCode() {
		return contentTypeToContainerCodeMap.get(this);
	}

}
