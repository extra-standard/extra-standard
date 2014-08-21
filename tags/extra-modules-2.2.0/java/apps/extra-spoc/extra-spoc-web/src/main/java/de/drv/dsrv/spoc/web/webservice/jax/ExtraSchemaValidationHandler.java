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
package de.drv.dsrv.spoc.web.webservice.jax;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import de.drv.dsrv.spoc.extra.v1_3.ExtraHelper;
import de.drv.dsrv.spoc.extra.v1_3.ExtraJaxbMarshaller;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorReasonType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorType;
import de.drv.dsrv.spoc.web.util.Messages;
import de.drv.dsrv.spoc.web.webservice.InvalidExtraRequestException;

/**
 * Handler zur Validierung des eXTra-Requests gegen das eXTra-Schema. Falls bei
 * der Validierung ein Fehler auftritt, wird die Verarbeitung abgebrochen und
 * als eXTra-Response ein SOAP-Fault mit einem eXTraError erstellt.
 */
public class ExtraSchemaValidationHandler implements SOAPHandler<SOAPMessageContext> {

	private static final Log LOG = LogFactory.getLog(ExtraSchemaValidationHandler.class);

	private static final Locale DEFAULT_LOCALE = Locale.GERMAN;

	private static final String SCHEMA_PATH = "/WEB-INF/schema/eXTra-request-1.xsd";

	private static final String SOAP_FAULT_CODE = "Client";

	private static ResourceBundle resourceBundle;

	// TODO - aus Properties
	private final String soapFaultString = "eXTra Error";
	private final String extraErrorCode = "E84";

	static {
		resourceBundle = ResourceBundle.getBundle("bundles.Messages", DEFAULT_LOCALE);
	}

	@Override
	public boolean handleMessage(final SOAPMessageContext context) {

		// Nur fuer den Eingang
		final Boolean isOutBound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (isOutBound) {
			return true;
		}

		// Servlet-Context speichern
		final ServletContext servletContext = (ServletContext) context.get(MessageContext.SERVLET_CONTEXT);

		SOAPBody soapBody = getSoapBody(context);

		try {

			// Pruefe SOAP-Body
			if (soapBody == null) {
				try {
					// Erstelle neue SOAP-Message mit SOAP-Body
					final SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
					soapBody = soapMessage.getSOAPBody();
					context.setMessage(soapMessage);
				} catch (final SOAPException e) {
					LOG.error("Exception beim Erstellen einer SOAP-Message.", e);
				}
				// Request ungueltig - z.B. ungueltiges XML
				throw new InvalidExtraRequestException(resourceBundle.getString(Messages.ERROR_REQUEST_NO_EXTRA));
			}

			// Hole Transport-Element
			final Node transportNode = getTransportElement(soapBody);
			if (transportNode == null) {
				// kein 'Transport'-Element, Request ungueltig
				throw new InvalidExtraRequestException(resourceBundle.getString(Messages.ERROR_REQUEST_NO_EXTRA));
			}

			// Validiere Request-XML gegen eXTra-Schema
			validateExtraRequest(transportNode, servletContext);

		} catch (final InvalidExtraRequestException e) {
			return handleException(soapBody, e.getMessage(), ExtraErrorReasonType.INVALID_REQUEST);
		} catch (final Exception e) {
			LOG.error("Unbekannter Fehler beim Request-Validierung.", e);
			return handleException(soapBody, resourceBundle.getString(Messages.ERROR_REQUEST_VALIDATION_UNKNOWN),
					ExtraErrorReasonType.UNSPECIFIED);
		}
		return true;
	}

	@Override
	public boolean handleFault(final SOAPMessageContext context) {
		return true;
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

	@Override
	public void close(final MessageContext context) {
	}

	private SOAPBody getSoapBody(final SOAPMessageContext context) {
		SOAPBody soapBody = null;
		try {
			final SOAPMessage soapMessage = context.getMessage();
			soapBody = soapMessage.getSOAPBody();
		} catch (final Exception e) {
			LOG.error("Exception beim Auslesen des SOAP-Bodys.", e);
		}
		return soapBody;
	}

	private Node getTransportElement(final SOAPBody soapBody) {

		Node transportElement = null;

		try {
			transportElement = soapBody.getElementsByTagNameNS("*", "Transport").item(0);
		} catch (final Exception e) {
			LOG.error("Exception beim Auslesen des Transport-Elements.", e);
		}
		return transportElement;
	}

	private boolean handleException(final SOAPBody soapBody, final String errorText, final ExtraErrorReasonType reason) {

		try {
			// Bisherigen Inhalt des SOAP-Bodys entfernen
			soapBody.removeContents();

			// SOAP-Fault erzeugen
			final SOAPFault fault = soapBody.addFault();
			fault.setFaultString(this.soapFaultString);
			fault.setFaultCode(new QName(SOAPConstants.URI_NS_SOAP_ENVELOPE, SOAP_FAULT_CODE));

			final ExtraJaxbMarshaller extraJaxbMarshaller = new ExtraJaxbMarshaller();
			final ExtraErrorType extraError = ExtraHelper.generateError(reason, this.extraErrorCode, errorText);
			extraJaxbMarshaller.marshalExtraError(extraError, fault.addDetail());

			return false;
		} catch (final Exception e) {
			LOG.error("Fehler bei Exception-Behandlung.", e);
			throw new WebServiceException(resourceBundle.getString(Messages.ERROR_NON_EXTRA_TEXT));
		}
	}

	private void validateExtraRequest(final Node transportNode, final ServletContext servletContext) throws Exception {

		// Validator-Objekt mit eXTra-Schema als Basis erstellen
		final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		final Schema schema = factory.newSchema(servletContext.getResource(SCHEMA_PATH));
		final Validator validator = schema.newValidator();

		try {
			// Validiere Transport-Element gegen eXTra-Schema
			validator.validate(new DOMSource(transportNode));
		} catch (final SAXException e) {
			// Falls MTOM-Attachement, dann den Fehler bzgl. cid-Referenz
			// ignorieren
			if (!(e.getMessage().contains("cid:") && e.getMessage().contains("'base64Binary'"))) {
				LOG.warn("Fehler bei der XML-Validierung: " + e.getMessage());
				throw new InvalidExtraRequestException(e.getMessage());
			}
		}
	}
}
