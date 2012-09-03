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
package de.extrastandard.api.exception;

/**
 * Config Exception
 *
 * @author Leonid Potap
 * @version $Id$
 */
public class ExtraConfigRuntimeException extends ExtraRuntimeException {

	private static final long serialVersionUID = 8985688274178113929L;

	public ExtraConfigRuntimeException() {
		super();
	}

	public ExtraConfigRuntimeException(final ExceptionCode code) {
		super(code);
	}

	public ExtraConfigRuntimeException(final ExceptionCode code, final String message) {
		super(code, message);
	}

	public ExtraConfigRuntimeException(final ExceptionCode code, final Throwable cause) {
		super(code, cause);
	}

	public ExtraConfigRuntimeException(final Throwable cause) {
		super(cause);
	}

	public ExtraConfigRuntimeException(final ExceptionCode code, final String message, final Throwable cause) {
		super(code, message, cause);
	}

}
