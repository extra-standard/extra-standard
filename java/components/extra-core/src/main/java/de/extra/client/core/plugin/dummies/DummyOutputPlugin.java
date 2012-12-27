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
package de.extra.client.core.plugin.dummies;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.GregorianCalendar;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

import de.drv.dsrv.extra.schemaversion.ExtraSchemaVersion;
import de.drv.dsrv.extrastandard.namespace.components.ClassifiableIDType;
import de.drv.dsrv.extrastandard.namespace.components.FlagCodeType;
import de.drv.dsrv.extrastandard.namespace.components.FlagType;
import de.drv.dsrv.extrastandard.namespace.components.ReceiverType;
import de.drv.dsrv.extrastandard.namespace.components.ReportType;
import de.drv.dsrv.extrastandard.namespace.components.RequestDetailsType;
import de.drv.dsrv.extrastandard.namespace.components.ResponseDetailsType;
import de.drv.dsrv.extrastandard.namespace.components.SenderType;
import de.drv.dsrv.extrastandard.namespace.components.TextType;
import de.drv.dsrv.extrastandard.namespace.response.Transport;
import de.drv.dsrv.extrastandard.namespace.response.TransportBody;
import de.drv.dsrv.extrastandard.namespace.response.TransportHeader;
import de.extra.client.core.observer.impl.TransportInfoBuilder;
import de.extrastandard.api.observer.ITransportInfo;
import de.extrastandard.api.observer.ITransportObserver;
import de.extrastandard.api.plugin.IOutputPlugin;

@Named("dummyOutputPlugin")
public class DummyOutputPlugin implements IOutputPlugin {

	// if (flagCode.getValue().equalsIgnoreCase("C00")
	// || flagCode.getValue().equalsIgnoreCase("I000")
	// || flagCode.getValue().equalsIgnoreCase("E98"))

	private static final Logger LOG = LoggerFactory
			.getLogger(DummyOutputPlugin.class);

	private static final String TEST_INDICATOR = "http://www.extra-standard.de/test/NONE";

	@Inject
	@Named("eXTrajaxb2Marshaller")
	private Marshaller marshaller;

	@SuppressWarnings("unused")
	@Inject
	@Named("eXTrajaxb2Marshaller")
	private Unmarshaller unmarshaller;

	@Inject
	@Named("transportObserver")
	private ITransportObserver transportObserver;

	@Inject
	@Named("transportInfoBuilder")
	private TransportInfoBuilder transportInfoBuilder;

	@Inject
	@Named("dummyOutputPluginUtil")
	private DummyOutputPluginUtil dummyOutputPluginUtil;

	@Override
	public InputStream outputData(final InputStream request) {
		InputStream responseAsinputStream = null;
		try {
			LOG.info("request={}", request);
			final Transport response = createExtraResponse(request);

			final Writer writer = new StringWriter();
			final StreamResult streamResult = new StreamResult(writer);

			marshaller.marshal(response, streamResult);

			responseAsinputStream = new ByteArrayInputStream(writer.toString()
					.getBytes());
			return responseAsinputStream;
		} catch (final IOException ioException) {
			// Hier kommt eine ExtraTechnischeRuntimeException
			LOG.error("Unerwarteter Fehler: ", ioException);
		}
		return responseAsinputStream;

	}

	private Transport createExtraResponse(final InputStream request) {
		final String requestId = dummyOutputPluginUtil
				.extractRequestId(request);
		final Transport response = new Transport();
		response.setVersion(ExtraSchemaVersion.CURRENT_SCHEMA_VERSION
				.getVersion());

		response.setProfile("http://code.google.com/p/extra-standard/profile/1");
		final TransportHeader transportHeader = new TransportHeader();
		transportHeader.setTestIndicator(TEST_INDICATOR);
		final ResponseDetailsType responseDetailsType = new ResponseDetailsType();
		final ClassifiableIDType idType = new ClassifiableIDType();
		idType.setValue("42");
		responseDetailsType.setResponseID(idType);
		responseDetailsType.setTimeStamp(new GregorianCalendar());
		final ReportType reportType = new ReportType();
		reportType.setHighestWeight("http://www.extra-standard.de/weight/OK");
		final FlagType flagType = new FlagType();
		final FlagCodeType flagCodeType = new FlagCodeType();
		flagCodeType.setValue("C00");
		flagType.setCode(flagCodeType);
		final TextType flagText = new TextType();
		flagText.setValue("O.K");
		flagType.setText(flagText);
		flagType.setWeight("http://www.extra-standard.de/weight/OK");
		reportType.getFlag().add(flagType);
		responseDetailsType.setReport(reportType);
		transportHeader.setResponseDetails(responseDetailsType);
		final RequestDetailsType requestDetailsType = new RequestDetailsType();
		final ClassifiableIDType requestIdType = new ClassifiableIDType();
		requestIdType.setValue(requestId);
		requestDetailsType.setRequestID(requestIdType);
		transportHeader.setRequestDetails(requestDetailsType);
		response.setTransportHeader(transportHeader);
		final SenderType sender = new SenderType();
		final TextType testSender = new TextType();
		testSender.setValue("TEST SENDER");
		sender.setName(testSender);
		final ClassifiableIDType senderId = new ClassifiableIDType();
		senderId.setValue("SENDER_ID");
		senderId.setClazz("TestClass");
		sender.setSenderID(senderId);
		transportHeader.setSender(sender);
		final ReceiverType receiver = new ReceiverType();
		final TextType receiverName = new TextType();
		receiverName.setValue("TEST_RECEIVER");
		final ClassifiableIDType receiverId = new ClassifiableIDType();
		receiverId.setValue("RECEIVER_ID");
		receiverId.setClazz("TEST_CLASS");
		receiver.setReceiverID(receiverId);
		receiver.setName(receiverName);
		transportHeader.setReceiver(receiver);

		final TransportBody transportBody = new TransportBody();
		response.setTransportBody(transportBody);
		final de.drv.dsrv.extrastandard.namespace.request.TransportHeader requestHeader = new de.drv.dsrv.extrastandard.namespace.request.TransportHeader();
		requestHeader.setRequestDetails(requestDetailsType);
		final ITransportInfo transportInfo = transportInfoBuilder
				.createTransportInfo(requestHeader);
		transportObserver.requestFilled(transportInfo);
		transportObserver.requestForwarded("dummy, keine Weiterleitung", 0);

		return response;
	}

}
