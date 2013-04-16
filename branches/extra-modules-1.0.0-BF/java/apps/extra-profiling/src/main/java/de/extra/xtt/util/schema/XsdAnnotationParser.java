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

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.sun.xml.xsom.parser.AnnotationContext;
import com.sun.xml.xsom.parser.AnnotationParser;

/**
 * Klasse zum Parsen von Anmerkungen eines Schemas.
 * 
 * @author Beier
 */
public class XsdAnnotationParser extends AnnotationParser {

	private final StringBuilder documentation = new StringBuilder();

	/**
	 * {@inheritdoc}
	 */
	@Override
	public ContentHandler getContentHandler(AnnotationContext context,
			String parentElementName, ErrorHandler handler,
			EntityResolver resolver) {
		return new ContentHandler() {
			private boolean parsingDocumentation = false;

			@Override
			public void characters(char[] ch, int start, int length)
					throws SAXException {
				if (parsingDocumentation) {
					documentation.append(ch, start, length);
				}
			}

			@Override
			public void endElement(String uri, String localName, String name)
					throws SAXException {
				if (localName.equals("documentation")) {
					parsingDocumentation = false;
				}
			}

			@Override
			public void startElement(String uri, String localName, String name,
					Attributes atts) throws SAXException {
				if (localName.equals("documentation")) {
					parsingDocumentation = true;
				}
			}

			@Override
			public void endDocument() throws SAXException {

			}

			@Override
			public void endPrefixMapping(String arg0) throws SAXException {
			}

			@Override
			public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
					throws SAXException {
			}

			@Override
			public void processingInstruction(String arg0, String arg1)
					throws SAXException {
			}

			@Override
			public void setDocumentLocator(Locator arg0) {
			}

			@Override
			public void skippedEntity(String arg0) throws SAXException {
			}

			@Override
			public void startDocument() throws SAXException {
			}

			@Override
			public void startPrefixMapping(String arg0, String arg1)
					throws SAXException {
			}
		};
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public Object getResult(Object existing) {
		return documentation.toString().trim();
	}
}