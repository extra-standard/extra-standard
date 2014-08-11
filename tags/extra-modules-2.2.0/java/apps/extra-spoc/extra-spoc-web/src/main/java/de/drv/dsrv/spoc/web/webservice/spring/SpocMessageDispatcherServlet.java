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
package de.drv.dsrv.spoc.web.webservice.spring;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ws.soap.saaj.SaajSoapEnvelopeException;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

import de.drv.dsrv.spoc.extra.v1_3.ExtraHelper;
import de.drv.dsrv.spoc.extra.v1_3.ExtraJaxbMarshaller;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorReasonType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorType;
import de.drv.dsrv.spoc.web.util.Messages;

/**
 * Erweitert das {@link MessageDispatcherServlet} um die Fehlerbehandlung in dem
 * Fall, dass der Request keine g\u00fcltige SOAP-Nachricht ist.
 */
public class SpocMessageDispatcherServlet extends MessageDispatcherServlet {

	private static final long serialVersionUID = 1L;

	private static final Log LOG = LogFactory.getLog(SpocMessageDispatcherServlet.class);

	private String soapFaultString = "eXTra Error";
	private String extraErrorCode = "E84";

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Request eingegangen, Spring-WS-Verarbeitung wird aufgerufen.");
			}
			super.doService(request, response);
		} catch (final SaajSoapEnvelopeException ex) {
			if (LOG.isInfoEnabled()) {
				LOG.info("Behandlung von Exception - exTRa-Error wird generiert: ", ex);
			}
			// Request enthaelt gueltiges XML, ist aber keine SOAP-Nachricht
			createExtraErrorAndWriteResponse(response,
					getWebApplicationContext().getMessage(Messages.ERROR_REQUEST_NO_SOAP, null, request.getLocale()));
		}

		// Falls das XML im Request ungueltig ist, "verschluckt" der
		// WebServiceMessageReceiverHandlerAdapter
		// die Exception und setzt den HTTP Status auf 400.
		// Ganz sicher koennen wir hier aber nicht sein, ob das tatsaechlich die
		// Ursache ist.
		if (response.getStatus() == HttpServletResponse.SC_BAD_REQUEST) {
			try {
				if (LOG.isInfoEnabled()) {
					LOG.info("HTTP-Status 400 - exTRa-Error wird generiert.");
				}
				createExtraErrorAndWriteResponse(response,
						getWebApplicationContext().getMessage(Messages.ERROR_REQUEST_NO_XML, null, request.getLocale()));
			} catch (final Exception ignored) {
				// Evtl. kann nicht mehr in die Response geschrieben werden,
				// weil das vorher schon geschehen ist.
				LOG.warn("Exception beim Versuch, einen eigenen Extra-Error in die HttpResponse zu schreiben.", ignored);
			}
		}
	}

	private void createExtraErrorAndWriteResponse(final HttpServletResponse httpServletResponse, final String errorText)
			throws SOAPException, JAXBException, DatatypeConfigurationException, IOException {

		final MessageFactory factory = MessageFactory.newInstance();
		final SOAPMessage message = factory.createMessage();
		final SOAPBody body = message.getSOAPBody();

		final SOAPFault fault = body.addFault();
		final QName faultName = new QName(SOAPConstants.URI_NS_SOAP_ENVELOPE, FaultCode.CLIENT.toString());
		fault.setFaultCode(faultName);
		fault.setFaultString(this.soapFaultString);

		final Detail detail = fault.addDetail();

		final ExtraJaxbMarshaller extraJaxbMarshaller = new ExtraJaxbMarshaller();
		final ExtraErrorReasonType reason = ExtraErrorReasonType.INVALID_REQUEST;
		final ExtraErrorType extraError = ExtraHelper.generateError(reason, this.extraErrorCode, errorText);
		extraJaxbMarshaller.marshalExtraError(extraError, detail);

		// Schreibt die SOAPMessage in einen String.
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		message.writeTo(out);
		// Das Encoding, in dem sich die Message rausschreibt, kann man als
		// Property abfragen.
		final Object encodingProperty = message.getProperty(SOAPMessage.CHARACTER_SET_ENCODING);
		String soapMessageEncoding = "UTF-8";
		if (encodingProperty != null) {
			soapMessageEncoding = encodingProperty.toString();
		}
		final String errorXml = out.toString(soapMessageEncoding);

		httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		httpServletResponse.setContentType("text/xml");
		httpServletResponse.getWriter().write(errorXml);
		httpServletResponse.getWriter().flush();
		httpServletResponse.getWriter().close();
	}

	/**
	 * @param soapFaultString
	 *            Text im <code>&lt;faultstring&gt;</code> Element eines
	 *            zur&uuml;ck gegebenen SOAP Faults; Default ist &quot;eXTra
	 *            Error&quot;
	 */
	public void setSoapFaultString(final String soapFaultString) {
		this.soapFaultString = soapFaultString;
	}

	/**
	 * @param extraErrorCode
	 *            Code f&uuml;r den zur&uuml;ck gegebenen eXTra-Error, wenn der
	 *            Request kein SOAP-Request ist, oder das XML ung&uuml;ltig ist;
	 *            Default ist &quot;E84&quot;
	 */
	public void setExtraErrorCode(final String extraErrorCode) {
		this.extraErrorCode = extraErrorCode;
	}
}
