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
package de.extra.client.core.builder.impl.components;

import java.util.Calendar;

import javax.inject.Named;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import de.drv.dsrv.extrastandard.namespace.components.ApplicationType;
import de.drv.dsrv.extrastandard.namespace.components.ClassifiableIDType;
import de.drv.dsrv.extrastandard.namespace.components.RequestDetailsType;
import de.drv.dsrv.extrastandard.namespace.components.TextType;
import de.extra.client.core.builder.IXmlComplexTypeBuilder;
import de.extra.client.core.model.IExtraProfileConfiguration;
import de.extra.client.core.model.IInputDataContainer;

/**
 * @author Leonid Potap
 * 
 */
@Named("transportHeaderRequestDetailsBuilder")
public class TransportHeaderRequestDetailsBuilder implements
		IXmlComplexTypeBuilder {

	private static Logger logger = Logger
			.getLogger(TransportHeaderRequestDetailsBuilder.class);

	private static final String BUILDER_XML_MESSAGE_TYPE = "xcpt:RequestDetails";

	private static final String PRODUCT_NAME = "eXTra Klient OpenSource";

	private static final String MANUFACTURE = "OpenSource";

	@Value("${message.builder.header.requestDetail.procedure}")
	private String requestDetailProcedure;
	@Value("${message.builder.header.requestDetail.dataType}")
	private String requestDetailDataType;
	@Value("${message.builder.header.requestDetail.scenario}")
	private String requestDetailScenario;

	/**
	 * Erstellt die SenderInformationen im Kontext von Header (non-Javadoc)
	 * 
	 * @see de.extra.client.core.builder.IXmlComplexTypeBuilder#buildXmlFragment(de.extra.client.core.model.SenderDataBean,
	 *      de.extra.client.core.model.ExtraProfileConfiguration)
	 */
	@Override
	public Object buildXmlFragment(IInputDataContainer senderData,
			IExtraProfileConfiguration config) {
		// Objects für RequestDetails
		RequestDetailsType requestDetails = new RequestDetailsType();
		ClassifiableIDType requestId = new ClassifiableIDType();
		ApplicationType application = new ApplicationType();
		TextType product = new TextType();
		ClassifiableIDType registrationId = new ClassifiableIDType();

		// Applicationinformation
		product.setValue(PRODUCT_NAME);
		application.setManufacturer(MANUFACTURE);
		application.setProduct(product);
		application.setRegistrationID(registrationId);
		requestDetails.setApplication(application);

		// Setting RequestDetails
		requestId.setClazz("0");
		String requestID = senderData.getRequestId();
		requestId.setValue(requestID);
		requestDetails.setRequestID(requestId);
		requestDetails.setTimeStamp(CreateCurrentTimestamp());

		// Controllerinformation
		requestDetails.setProcedure(requestDetailProcedure);
		requestDetails.setDataType(requestDetailDataType);
		requestDetails.setScenario(requestDetailScenario);

		logger.debug("TransportHeaderReceiver created.");
		return requestDetails;
	}

	/**
	 * 
	 * Erstellen des aktuellen Timestamps.
	 * 
	 * @return XMLGregorianCalendar mit aktuellem Datum
	 */
	private XMLGregorianCalendar CreateCurrentTimestamp() {
		XMLGregorianCalendar timestamp = null;

		try {
			timestamp = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		} catch (DatatypeConfigurationException e) {
			logger.error(e.getMessage());
		}

		Calendar now = Calendar.getInstance();
		timestamp.setDay(now.get(Calendar.DAY_OF_MONTH));
		// We do a +1 below because XMLGregorianCalendar goes from 1 to 12
		// while Calendar.MONTH goes from 0 to 11 !!!
		timestamp.setMonth(now.get(Calendar.MONTH) + 1);
		timestamp.setYear(now.get(Calendar.YEAR));
		timestamp.setTime(now.get(Calendar.HOUR_OF_DAY),
				now.get(Calendar.MINUTE), now.get(Calendar.SECOND));

		return timestamp;
	}

	@Override
	public String getXmlType() {
		return BUILDER_XML_MESSAGE_TYPE;
	}

}
