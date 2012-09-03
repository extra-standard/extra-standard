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
 * Die Parent-Class der unchecked Exceptions in Extra.
 *
 * @author Leonid Potap
 * @version $Id$
 */
public abstract class ExtraRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 4193374961001009768L;

	protected final ExceptionCode code;

	public ExtraRuntimeException() {
		this.code = ExceptionCode.UNEXPECTED_INTERNAL_EXCEPTION;
	}

	public ExtraRuntimeException(final ExceptionCode code) {
		this.code = code;
	}

	public ExtraRuntimeException(final ExceptionCode code, final String message) {
		super(message);
		this.code = code;
	}

	public ExtraRuntimeException(final ExceptionCode code, final Throwable cause) {
		super(cause);
		this.code = code;
	}

	public ExtraRuntimeException(final Throwable cause) {
		super(cause);
		this.code = ExceptionCode.UNEXPECTED_INTERNAL_EXCEPTION;
	}

	public ExtraRuntimeException(final ExceptionCode code, final String message, final Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public ExceptionCode getCode() {
		return code;
	}

}
