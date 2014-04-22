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
package de.drv.dsrv.spoc.web.manager;

/**
 * Ausnahme f&uuml;r Fehler bei der Verarbeitung der eXTra-Nutzdaten.
 */
public class SpocNutzdatenManagerException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 *            Fehlernachricht
	 */
	public SpocNutzdatenManagerException(final String message) {
		super(message);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 *            Fehlernachricht
	 * @param cause
	 *            Fehlergrund
	 */
	public SpocNutzdatenManagerException(final String message, final Throwable cause) {
		super(message, cause);
	}
}