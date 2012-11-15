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
package de.extra.client.core.util.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import de.extra.client.core.util.IExtraValidator;
import de.extrastandard.api.exception.ExceptionCode;
import de.extrastandard.api.exception.ExtraConfigRuntimeException;

/**
 * @author Leonid Potap
 * @version $Id$
 */
@Named("extraValidator")
public class ExtraValidator implements IExtraValidator {

	@Inject
	@Named("validator")
	private Validator validator;

	/**
	 * Valiedier Plugin und Produziert eine ExtraConfigRuntimeException, wenn
	 * die Validierungsregeln verletzt sind
	 * 
	 * @param iExtraObject
	 */
	@Override
	public void validate(final Object iExtraObject) {
		final Errors errors = new DirectFieldBindingResult(iExtraObject,
				iExtraObject.getClass().getName());
		// Validierung. Sind alle Elemente vorhanden
		validator.validate(iExtraObject, errors);
		if (errors.hasErrors()) {
			throw new ExtraConfigRuntimeException(
					ExceptionCode.EXTRA_CONFIGURATION_EXCEPTION,
					convertToString(errors));
		}
	}

	private String convertToString(final Errors errors) {
		final List<FieldError> fieldErrors = errors.getFieldErrors();
		final StringBuilder stringBuilder = new StringBuilder(
				fieldErrors.size() + " Konfigurationsfehler: ");
		final String sep = ";";
		for (final FieldError fieldError : fieldErrors) {
			// TODO statt Klassennamen den Konfigurationskey ausgeben
			stringBuilder.append(fieldError.getObjectName());
			stringBuilder.append(".").append(fieldError.getField());
			stringBuilder.append(" ").append(fieldError.getDefaultMessage());
			stringBuilder.append(sep);
		}
		return stringBuilder.toString();
	}

}
