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

package de.extra.extraClientLight.helper;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

import de.extra.extraClientLight.model.RequestExtraBean;
import de.extra.extraClientLight.util.ClientConstants;
import de.extra_standard.namespace.components._1.Base64CharSequenceType;
import de.extra_standard.namespace.components._1.ClassifiableIDType;
import de.extra_standard.namespace.components._1.DataType;
import de.extra_standard.namespace.components._1.ReceiverType;
import de.extra_standard.namespace.components._1.RequestDetailsType;
import de.extra_standard.namespace.components._1.SenderType;
import de.extra_standard.namespace.request._1.TransportRequestBodyType;
import de.extra_standard.namespace.request._1.TransportRequestHeaderType;
import de.extra_standard.namespace.request._1.TransportRequestType;

public class BuildExtraTransport {

	public static TransportRequestType buildTransportRequest(
			RequestExtraBean requestBean) {

		TransportRequestType request = new TransportRequestType();

		request.setVersion(ClientConstants.EXTRA_VERSION);
		request.setProfile(requestBean.getProfile());
		request.setTransportHeader(buildHeader(requestBean));
		if (!requestBean.getDataObjekt().isQuery()) {
			request.setTransportBody(buildBody(requestBean.getDataObjekt()
					.getData()));
		} else {

			// TODO Aufbau der Query
		}
		return request;

	}

	private static TransportRequestHeaderType buildHeader(
			RequestExtraBean requestBean) {

		TransportRequestHeaderType requestHeader = new TransportRequestHeaderType();

		// SenderID

		SenderType sender = new SenderType();
		ClassifiableIDType senderId = new ClassifiableIDType();
		senderId.setValue(requestBean.getAbsender());
		sender.setSenderID(senderId);

		// ReceiverID

		ReceiverType receiver = new ReceiverType();
		ClassifiableIDType receiverId = new ClassifiableIDType();
		receiverId.setValue(requestBean.getEmpfaenger());
		receiver.setReceiverID(receiverId);

		// RequestDetails

		requestHeader.setSender(sender);
		requestHeader.setReceiver(receiver);
		requestHeader.setRequestDetails(buildRequestDetails(requestBean));

		return requestHeader;

	}

	private static TransportRequestBodyType buildBody(byte[] nutzdaten) {
		TransportRequestBodyType requestBody = new TransportRequestBodyType();
		DataType data = new DataType();

		Base64CharSequenceType payload = new Base64CharSequenceType();
		DataSource ds = new ByteArrayDataSource(nutzdaten, "application");
		DataHandler dataHandler = new DataHandler(ds);
		payload.setValue(dataHandler);
		data.setBase64CharSequence(payload);
		requestBody.setData(data);

		return requestBody;

	}

	private static RequestDetailsType buildRequestDetails(
			RequestExtraBean requestBean) {

		RequestDetailsType requestDetails = new RequestDetailsType();

		requestDetails.setDataType(requestBean.getFachdienst());
		requestDetails.setProcedure(requestBean.getVerfahren());
		ClassifiableIDType requestId = new ClassifiableIDType();
		requestId.setValue(requestBean.getFachschluessel());
		requestDetails.setRequestID(requestId);
		
		if (requestBean.isSynchron()) {
			requestDetails
					.setScenario(ClientConstants.DETAILS_REQUEST_RESPONSE);
		} else {

			requestDetails.setScenario(ClientConstants.DETAILS_ACKNOWLEDGEMENT);
		}

		return requestDetails;
	}

}
