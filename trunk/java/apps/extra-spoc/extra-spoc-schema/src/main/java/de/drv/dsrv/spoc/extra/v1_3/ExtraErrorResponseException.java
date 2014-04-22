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
package de.drv.dsrv.spoc.extra.v1_3;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorType;

/**
 * Die Exception hat ein {@link ExtraErrorType} als Instanzvariable, die immer
 * gesetzt sein muss. Deswegen gibt es auch nur einen Konstruktor mit diesem
 * Parameter. Verwendet wird die Exception, wenn statt einem
 * TransportResponseType ein {@link ExtraErrorType} zur&uuml;ckgegeben werden
 * soll. Die Unterscheidung findet also in einem <code>try-catch</code> -Block
 * statt.
 */
public class ExtraErrorResponseException extends Exception {

	private static final long serialVersionUID = 906192905275346978L;
	private final ExtraErrorType extraErrorType;

	/**
	 * Konstruktor.
	 * 
	 * @param extraErrorType
	 */
	ExtraErrorResponseException(final ExtraErrorType extraErrorType) {
		this.extraErrorType = extraErrorType;
	}

	public ExtraErrorType getExtraErrorType() {
		return extraErrorType;
	}
}
