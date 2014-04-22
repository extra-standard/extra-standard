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
package de.drv.dsrv.spoc.web.service;

/**
 * Exception f&uuml;r einen fehlerhafte Antwort des Fachverfahrens.
 */
public class FachverfahrenResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final int httpStatusCode;
	private final String reason;

	/**
	 * @param httpStatusCode der HTTP Status Code der Antwort des Fachverfahrens (z.B. 404 Not Found)
	 * @param reason der vom Fachverfahren mitgegebene Reason String
	 */
	public FachverfahrenResponseException(final int httpStatusCode, final String reason) {
		super();
		this.httpStatusCode = httpStatusCode;
		this.reason = reason;
	}

	/**
	 * @return der HTTP Status Code der Antwort des Fachverfahrens (z.B. 404 Not Found)
	 */
	public int getHttpStatusCode() {
		return this.httpStatusCode;
	}

	/**
	 * @return der vom Fachverfahren mitgegebene Reason String
	 */
	public String getReason() {
		return this.reason;
	}

	@Override
	public String toString() {
		return "Fehler bei der Antwort des Fachverfahrens mit HTTP Code >" + this.httpStatusCode
				+ "< und Reason String >" + this.reason + "<.";
	}
}
