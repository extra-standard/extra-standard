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
package de.extra.client.core.helper;

import java.util.Calendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import de.drv.dsrv.extrastandard.namespace.components.ApplicationType;
import de.drv.dsrv.extrastandard.namespace.components.ClassifiableIDType;
import de.drv.dsrv.extrastandard.namespace.components.ReceiverType;
import de.drv.dsrv.extrastandard.namespace.components.RequestDetailsType;
import de.drv.dsrv.extrastandard.namespace.components.SenderType;
import de.drv.dsrv.extrastandard.namespace.components.TextType;
import de.drv.dsrv.extrastandard.namespace.response.TransportHeader;
import de.extra.client.core.model.ConfigFileBean;

public class BuildHeaderHelper {

	/**
	 * 
	 * Funktion zum aufbau des Headers
	 * 
	 * @param configFile
	 *            ConfigFileBean mit den Informationen aus der ProfilDatei
	 * @param requestID
	 *            RequestID der Sendung
	 * @return TransportHeader
	 */
	public TransportHeader baueHeader(ConfigFileBean configFile,
			String requestID) {

		TransportHeader header = new TransportHeader();

		String testindicator = "";

		// Objects f�r Senderinformation

		SenderType sender = new SenderType();
		ClassifiableIDType senderId = new ClassifiableIDType();
		TextType senderName = new TextType();

		// Objects f�r Receiverinformation

		ReceiverType receiver = new ReceiverType();
		ClassifiableIDType receiverId = new ClassifiableIDType();
		TextType receiverName = new TextType();

		// Objects f�r RequestDetails

		RequestDetailsType requestDetails = new RequestDetailsType();
		ClassifiableIDType requestId = new ClassifiableIDType();
		ApplicationType application = new ApplicationType();
		TextType product = new TextType();
		ClassifiableIDType registrationId = new ClassifiableIDType();

		// Setting Testindicator

		if (configFile.isTestIndicator()) {
			// if testindicator is set value is set to "NONE". Message will not
			// be
			// processed in livesystem
			testindicator = "http://extra-standard.de/test/NONE";

		} else {
			testindicator = "http://extra-standard.de/test/PROCESS";

		}

		header.setTestIndicator(testindicator);

		// Setting Senderinformation

		senderId.setClazz("Betriebsnummer");
		senderId.setValue(configFile.getAbsBbnr());

		senderName.setValue(configFile.getAbsName());

		sender.setSenderID(senderId);
		sender.setName(senderName);

		// Setting Receiverinformation

		receiverId.setClazz("Betriebsnummer");
		receiverId.setValue(configFile.getEmpfBbnr());

		receiverName.setValue(configFile.getEmpfName());

		receiver.setReceiverID(receiverId);
		receiver.setName(receiverName);

		// Setting RequestDetails

		requestId.setClazz("0");
		requestId.setValue(requestID);

		requestDetails.setRequestID(requestId);
		requestDetails.setTimeStamp(CreateCurrentTimestamp());

		// Applicationinformation

		product.setValue(configFile.getProductName());

		application.setManufacturer(configFile.getProductManuf());
		application.setProduct(product);
		application.setRegistrationID(registrationId);

		requestDetails.setApplication(application);

		// Controllerinformation

		requestDetails.setProcedure(configFile.getProcedure());
		requestDetails.setDataType(configFile.getDataType());
		requestDetails.setScenario(configFile.getScenario());

		// Assembling Header

		header.setSender(sender);
		header.setReceiver(receiver);
		header.setRequestDetails(requestDetails);

		return header;
	}

	/**
	 * 
	 * Erstellen des aktuellen Timestamps
	 * 
	 * @return XMLGregorianCalendar mit aktuellem Datum
	 */
	private XMLGregorianCalendar CreateCurrentTimestamp() {
		XMLGregorianCalendar timestamp = null;

		try {
			timestamp = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
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
}
