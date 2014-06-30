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
package de.drv.dsrv.spoc.web.webservice.jax;

import javax.xml.ws.WebFault;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorType;

/**
 * Repraesentiert einen eXTra-Fehler als Ausnahme eines Webservice-Aufrufs.
 */
@WebFault(name = "ExtraError", targetNamespace = "http://www.extra-standard.de/namespace/service/1")
public class ExtraError extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Java type that goes as soapenv:Fault detail element.
	 * 
	 */
	private final ExtraErrorType faultInfo;

	/**
	 * Konstruktor.
	 * 
	 * @param faulString
	 *            Fehlertext
	 * @param faultInfo
	 *            Fehlerinfo-Objekt
	 */
	public ExtraError(final String faulString, final ExtraErrorType faultInfo) {
		super(faulString);
		this.faultInfo = faultInfo;
	}

	public ExtraErrorType getFaultInfo() {
		return this.faultInfo;
	}
}
