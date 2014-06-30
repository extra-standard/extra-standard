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
package de.drv.dsrv.utility.spring.common;

import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class StageAwarePropertyPlaceholderConfigurer extends
		PropertyPlaceholderConfigurer {

	private final transient String stage;

	/**
	 * Konstruktor.
	 */
	public StageAwarePropertyPlaceholderConfigurer(final String stage) {
		super();
		this.stage = stage;
	}

	/**
	 * Ausgelagerte Methode, um die Testbarkeit über Unit Tests sicher zu
	 * stellen.
	 * 
	 * @see PropertyPlaceholderConfigurer#resolvePlaceholder(String, Properties)
	 */
	protected String resolvePlaceholderFromSuper(final String placeholder,
			final Properties props) {
		return super.resolvePlaceholder(placeholder, props);
	}

	@Override
	protected String resolvePlaceholder(final String placeholder,
			final Properties props) {
		// Ermittle Wert speziell für die auf dem Server eingestellte Stage
		String wert = resolvePlaceholderFromSuper(placeholder + "."
				+ this.stage, props);

		// Prüfe, ob der Wert für die Stageun gültig ist
		if (wert == null) {
			// Ermittle den Wert ohne Stage
			wert = resolvePlaceholderFromSuper(placeholder, props);
		}

		// Geben Wert zurück else {
		return wert;
	}
}