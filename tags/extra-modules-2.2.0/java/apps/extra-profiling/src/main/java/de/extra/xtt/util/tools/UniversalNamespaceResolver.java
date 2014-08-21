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
package de.extra.xtt.util.tools;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Document;

/**
 * Klasse zum Auflösen von Namespace-URL bzw. -Präfix für ein DOM-Dokument.
 * 
 * @author Beier
 */
public class UniversalNamespaceResolver implements NamespaceContext {

	private final Document sourceDocument;

	/**
	 * Das Quell-Dokument wird f�r die Suche nach Namespaces gespeichert.
	 * 
	 * @param document
	 *            Quell-Dokument
	 */
	public UniversalNamespaceResolver(Document document) {
		sourceDocument = document;
	}

	/**
	 * Die Namespace-Suche wird an das Quell-Dokument delegiert.
	 * 
	 * @param prefix
	 *            zu suchender Namespace-Pr�fix
	 * @return uri
	 */
	@Override
	public String getNamespaceURI(String prefix) {
		if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
			return sourceDocument.lookupNamespaceURI(null);
		} else {
			return sourceDocument.lookupNamespaceURI(prefix);
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public String getPrefix(String namespaceURI) {
		return sourceDocument.lookupPrefix(namespaceURI);
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public Iterator<String> getPrefixes(String namespaceURI) {
		// nicht implementiert
		return null;
	}

}
