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
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.Base64CharSequenceType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.ClassifiableIDType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.DataType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.ElementSequenceType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.ReceiverType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.RequestDetailsType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.SenderType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.SupportedVersionsType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.messages.DataRequestArgumentType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.messages.DataRequestQueryType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.messages.DataRequestType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.messages.OperandSetType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.messages.OperandType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.request.TransportRequestBodyType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.request.TransportRequestHeaderType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.request.TransportRequestType;
import de.extra.extraClientLight.model.RequestExtraBean;
import de.extra.extraClientLight.util.ClientConstants;

public class BuildExtraTransport {

	public static TransportRequestType buildTransportRequest(
			RequestExtraBean requestBean) {

		TransportRequestType request = new TransportRequestType();

		request.setVersion(SupportedVersionsType.VERSION_1_3);
		request.setProfile(requestBean.getProfile());
		request.setTransportHeader(buildHeader(requestBean));
		if (!requestBean.getDataObjekt().isQuery()) {
			request.setTransportBody(buildBody(requestBean.getDataObjekt()
					.getData()));
		} else {

			request.setTransportBody(buildQueryBody(requestBean));
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

	private static TransportRequestBodyType buildQueryBody(
			RequestExtraBean requestBean) {

		TransportRequestBodyType transportBody = new TransportRequestBodyType();

		ElementSequenceType elementSequence = new ElementSequenceType();

		elementSequence.getAny().add(0, buildQuery(requestBean));
		DataType data = new DataType();
		data.setElementSequence(elementSequence);
		transportBody.setData(data);

		return transportBody;

	}

	private static DataRequestType buildQuery(RequestExtraBean requestBean) {

		DataRequestType dataRequest = new DataRequestType();
		DataRequestQueryType dataQuery = new DataRequestQueryType();
		DataRequestArgumentType requestIdArgument = new DataRequestArgumentType();
		DataRequestArgumentType procedureArgument = new DataRequestArgumentType();
		DataRequestArgumentType dataTypeArgument = new DataRequestArgumentType();

		requestIdArgument.setProperty(ClientConstants.QUERY_REQUESTID);

		OperandType operand = new OperandType();
		operand.setValue("1234");

		final JAXBElement<OperandType> jaxbOperand = new JAXBElement<OperandType>(
				new QName("http://www.extra-standard.de/namespace/message/1",
						"GT"), OperandType.class, operand);
		jaxbOperand.setValue(operand);
		
		requestIdArgument.getContent().add(jaxbOperand);
		dataQuery.getArgument().add(requestIdArgument);
/*
		procedureArgument.setProperty(ClientConstants.QUERY_PROCEDURE);

		dataQuery.getArgument().add(procedureArgument);

		dataTypeArgument.setProperty(ClientConstants.QUERY_DATATYPE);
		dataQuery.getArgument().add(dataTypeArgument);
*/
		dataRequest.setQuery(dataQuery);

		return dataRequest;
	}

}
