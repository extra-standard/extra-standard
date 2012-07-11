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

import com.sun.xml.xsom.XSType;

/**
 * Diese Klasse ist ein Container für einen Schema-Typ und den dazugehörigen
 * Namen des Schema-Elements, von dem dieser Typ verwendet wird.
 * 
 * @author Beier
 */
public class ContXsType {

	private final XSType type;
	private final String elementNameWithPrefix;

	/**
	 * Konstruktor zur Belegung des Typs und Elementsnamens
	 * 
	 * @param type
	 *            XML-Schematyp
	 * @param elementNameWithPrefix
	 *            Name inkl. Pr�fix des Elements, das diesen Typ verwendet
	 */
	public ContXsType(XSType type, String elementNameWithPrefix) {
		this.type = type;
		this.elementNameWithPrefix = elementNameWithPrefix;
	}

	/**
	 * Gibt den Schematyp zur�ck
	 * 
	 * @return XML-Schematyp
	 */
	public XSType getType() {
		return type;
	}

	/**
	 * Gibt den Namen des Elements inkl. Pr�fix zur�ck
	 * 
	 * @return Name des Elements inkl. Pr�fix
	 */
	public String getElementNameWithPrefix() {
		return elementNameWithPrefix;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof ContXsType) {
			return type.equals(((ContXsType) o).getType());
		} else {
			return super.equals(o);
		}
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}

}
