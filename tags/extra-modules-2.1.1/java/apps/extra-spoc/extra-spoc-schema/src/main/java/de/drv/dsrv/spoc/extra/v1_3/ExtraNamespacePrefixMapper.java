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

import java.util.HashMap;
import java.util.Map;

import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;

/**
 * Erweitert den
 * {@link com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper} um die
 * Namespace-Pr&auml;fixe des eXTra-Schemas in der Version 1.3. <br />
 * Es wird ein Standard-Mapping angeboten, das aber auch mit der Methode
 * <code>setMapping</code> angepasst werden kann.
 */
public class ExtraNamespacePrefixMapper extends NamespacePrefixMapper {

	// In Java 6.0 ist das Namespace-Prefix-Mapping noch nicht per Annotationen
	// moeglich
	// siehe hier:
	// http://hwellmann.blogspot.de/2011/03/jaxb-marshalling-with-custom-namespace.html
	//
	// Deshalb in diesem Fall noch die Loesung ueber das etwas unschoene Package
	// com.sun.xml.internal.bind.marshaller
	// Anleitung siehe hier:
	// http://pragmaticintegration.blogspot.de/2007/11/moving-jaxb-20-applications-built-by.html

	private transient Map<String, String> mapping;

	/**
	 * Konstruktor.
	 */
	public ExtraNamespacePrefixMapper() {
		super();
		setDefaultMapping();
	}

	/**
	 * Ermittelt das Pr&auml;fix für die &uuml;bergebene
	 * <code>namespaceUri</code>. Falls dieser Namespace nicht im Mapping
	 * definiert ist, wird als Pr&auml;fix <code>suggestion</code>
	 * zur&uuml;ckgegeben.
	 * 
	 * @param namespaceUri
	 *            Namespace, f&uuml;r den das Pr&auml;fix ermittelt werden soll
	 * @param suggestion
	 *            wird als Pr&auml;fix verwendet, falls
	 *            <code>namespaceUri</code> nicht im Mapping definiert ist
	 * @param requirePrefix
	 *            wird in dieser Implementierung nicht verwendet
	 */
	@Override
	public String getPreferredPrefix(final String namespaceUri, final String suggestion, final boolean requirePrefix) {
		String prefix = suggestion;

		if (this.mapping.containsKey(namespaceUri)) {
			prefix = this.mapping.get(namespaceUri);
		}

		return prefix;
	}

	/**
	 * Speichert das &uuml;bergebene Mapping.<br/>
	 * Damit wird das Standard-Mapping &uuml;berschrieben.
	 * 
	 * @param mapping
	 *            Mapping f&uuml;r die Namespace-Pr&auml;fixe
	 */
	public void setMapping(final Map<String, String> mapping) {
		this.mapping = mapping;
	}

	private void setDefaultMapping() {
		this.mapping = new HashMap<String, String>();
		this.mapping.put("http://www.extra-standard.de/namespace/request/1", "xreq");
		this.mapping.put("http://www.extra-standard.de/namespace/response/1", "xres");
		this.mapping.put("http://www.extra-standard.de/namespace/service/1", "xsrv");
		this.mapping.put("http://www.extra-standard.de/namespace/components/1", "xcpt");
		this.mapping.put("http://www.extra-standard.de/namespace/logging/1", "xlog");
		this.mapping.put("http://www.extra-standard.de/namespace/plugins/1", "xplg");
		this.mapping.put("http://www.extra-standard.de/namespace/message/1", "xmsg");
		this.mapping.put("http://www.w3.org/2001/04/xmlenc#", "xenc");
		this.mapping.put("http://www.w3.org/2000/09/xmldsig#", "ds");
	}
}
