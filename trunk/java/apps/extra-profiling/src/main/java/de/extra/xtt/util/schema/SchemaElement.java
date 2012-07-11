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
package de.extra.xtt.util.schema;

/**
 * Objekt zum Bündeln der Informationen zu einem Schema-Element: Name des
 * Elements, Namespace-Präfix und Namespace-URL.
 * 
 * @author Beier
 */
public class SchemaElement implements Comparable<SchemaElement> {

	private final String name;
	private final String nsUrl;
	private final String nsPrefix;

	/**
	 * Konstruktor mit Initialisierungsparametern
	 * 
	 * @param name
	 *            Name des Schema-Elements ohne Namespace-Pr�fix
	 * @param nsUrl
	 *            Namespace-URL des Schema-Elements
	 * @param nsPrefix
	 *            Namespace-Pr�fix des Schema-Elements
	 */
	public SchemaElement(String name, String nsUrl, String nsPrefix) {
		super();
		this.name = name;
		this.nsUrl = nsUrl;
		this.nsPrefix = nsPrefix;
	}

	/**
	 * Gibt den Namen des Schema-Elements zur�ck (ohne Namespace-Pr�fix)
	 * 
	 * @return Name des Schema-Elements
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gibt die Namespace-URL des Schema-Elements zur�ck
	 * 
	 * @return Namespace-URL
	 */
	public String getNsUrl() {
		return nsUrl;
	}

	/**
	 * Gibt den Namespace-Pr�fix des Schema-Elements zur�ck
	 * 
	 * @return Namespace-Pr�fix
	 */
	public String getNsPrefix() {
		return nsPrefix;
	}

	/**
	 * Gibt den Namen des Schema-Elements zusammen mit dem Namespace-Pr�fix
	 * zur�ck
	 * 
	 * @return Name inkl. Namspace-Pr�fix
	 */
	public String getNameWithPrefix() {
		String nameWithPrefix = name;
		if (nsPrefix.length() > 0) {
			nameWithPrefix = nsPrefix + ":" + name;
		}
		return nameWithPrefix;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public boolean equals(Object objToProof) {
		if (!(objToProof instanceof SchemaElement)) {
			return false;
		} else {
			SchemaElement seToProof = (SchemaElement) objToProof;
			boolean nameEqual = ((this.name == null) && (seToProof.getName() == null))
					|| this.name.equals(seToProof.getName());
			boolean urlEqual = ((this.nsUrl == null) && (seToProof.getNsUrl() == null))
					|| this.nsUrl.equals(seToProof.getNsUrl());
			boolean prefixEqual = ((this.nsPrefix == null) && (seToProof
					.getNsPrefix() == null))
					|| this.nsPrefix.equals(seToProof.getNsPrefix());
			return (nameEqual && urlEqual && prefixEqual);
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + name.hashCode();
		result = 31 * result + nsUrl.hashCode();
		result = 31 * result + nsPrefix.hashCode();
		return result;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public int compareTo(SchemaElement o) {
		return this.name.compareTo(o.getName());
	}
}
