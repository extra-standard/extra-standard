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
package de.extrastandard.api.model.execution;

/**
 * Qualifizierungen.
 * 
 * @author Thorsten Vogel
 * @version $Id: PhaseQualifier.java 487 2012-09-03 13:07:52Z
 *          thorstenvogel@gmail.com $
 */
public enum PhaseQualifier {

	PHASE1("PHASE1"),

	PHASE2("PHASE2"),

	PHASE3("PHASE3");

	private String name;

	private PhaseQualifier(final String name) {
		this.name = name;
	}

	public static PhaseQualifier resolveByName(final String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name is null");
		}
		for (final PhaseQualifier phaseQualifier : PhaseQualifier.values()) {
			if (name.equalsIgnoreCase(phaseQualifier.name)) {
				return phaseQualifier;
			}
		}
		throw new IllegalArgumentException("PhaseQualifier not found for Name:"
				+ name);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
