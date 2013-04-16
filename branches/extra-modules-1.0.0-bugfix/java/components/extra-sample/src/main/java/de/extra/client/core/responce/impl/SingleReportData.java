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
package de.extra.client.core.responce.impl;

public class SingleReportData {

	private final String returnText;
	private final String returnCode;

	/**
	 * DefaultConstruktor for SingleReportData
	 * 
	 * @param returnText
	 * @param returnCode
	 */
	public SingleReportData(final String returnText, final String returnCode) {
		super();
		this.returnText = returnText;
		this.returnCode = returnCode;
	}

	/**
	 * @return Liefert Return-Code der Nachricht.
	 */
	public String getReturnCode() {
		return returnCode;
	}

	/**
	 * Liefert Return-Text der Nachricht, wenn vorhanden.
	 * 
	 * @return
	 */
	public String getReturnText() {
		return returnText;
	}

}
