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

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.springframework.oxm.Marshaller;

import de.drv.dsrv.extrastandard.namespace.components.ClassifiableIDType;
import de.drv.dsrv.extrastandard.namespace.components.FlagCodeType;
import de.drv.dsrv.extrastandard.namespace.components.FlagType;
import de.drv.dsrv.extrastandard.namespace.components.ReportType;
import de.drv.dsrv.extrastandard.namespace.components.RequestDetailsType;
import de.drv.dsrv.extrastandard.namespace.components.ResponseDetailsType;
import de.drv.dsrv.extrastandard.namespace.response.TransportHeader;
import de.drv.dsrv.extrastandard.namespace.response.XMLTransport;
import de.extra.client.core.observation.ITransportObserver;
import de.extra.client.core.plugin.IOutputPlugin;

@Named("dummyOutputPlugin")
public class DummyOutputPlugin implements IOutputPlugin {

	// if (flagCode.getValue().equalsIgnoreCase("C00")
	// || flagCode.getValue().equalsIgnoreCase("I000")
	// || flagCode.getValue().equalsIgnoreCase("E98"))

	private static final Logger logger = Logger
			.getLogger(DummyOutputPlugin.class);

	@Inject
	@Named("eXTrajaxb2Marshaller")
	private Marshaller marshaller;

	@Inject
	@Named("transportObserver")
	private ITransportObserver transportObserver;

	@Override
	public InputStream outputData(InputStream request) {
		InputStream responseAsinputStream = null;
		try {
			logger.info(request);
			XMLTransport response = createExtraResponse(request);

			Writer writer = new StringWriter();
			StreamResult streamResult = new StreamResult(writer);

			marshaller.marshal(response, streamResult);

			responseAsinputStream = new ByteArrayInputStream(writer.toString()
					.getBytes());
			return responseAsinputStream;
		} catch (IOException ioException) {
			// Hier kommt eine ExtraTechnischeRuntimeException
			logger.error("Unerwarteter Fehler: ", ioException);
		}
		return responseAsinputStream;

	}

	private XMLTransport createExtraResponse(InputStream request) {
		XMLTransport response = new XMLTransport();
		TransportHeader transportHeader = new TransportHeader();
		ResponseDetailsType responseDetailsType = new ResponseDetailsType();
		ClassifiableIDType idType = new ClassifiableIDType();
		idType.setValue("42");
		responseDetailsType.setResponseID(idType);
		ReportType reportType = new ReportType();
		FlagType flagType = new FlagType();
		FlagCodeType flagCodeType = new FlagCodeType();
		flagCodeType.setValue("C00");
		flagType.setCode(flagCodeType);
		reportType.getFlag().add(flagType);
		responseDetailsType.setReport(reportType);
		transportHeader.setResponseDetails(responseDetailsType);
		RequestDetailsType requestDetailsType = new RequestDetailsType();
		ClassifiableIDType requestIdType = new ClassifiableIDType();
		requestIdType.setValue("4242");
		requestDetailsType.setRequestID(requestIdType);
		transportHeader.setRequestDetails(requestDetailsType);
		response.setTransportHeader(transportHeader);

		de.drv.dsrv.extrastandard.namespace.request.TransportHeader requestHeader = new de.drv.dsrv.extrastandard.namespace.request.TransportHeader();
		requestHeader.setRequestDetails(requestDetailsType);
		transportObserver.requestFilled(requestHeader);
		transportObserver.requestForwarded("dummy, keine Weiterleitung", 0);

		return response;
	}
}
