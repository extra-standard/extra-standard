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
package de.extra.client.plugins.outputplugin.dummy;

import javax.inject.Named;

import org.apache.log4j.Logger;

import de.drv.dsrv.extrastandard.namespace.components.ClassifiableIDType;
import de.drv.dsrv.extrastandard.namespace.components.FlagCodeType;
import de.drv.dsrv.extrastandard.namespace.components.FlagType;
import de.drv.dsrv.extrastandard.namespace.components.ReportType;
import de.drv.dsrv.extrastandard.namespace.components.RequestDetailsType;
import de.drv.dsrv.extrastandard.namespace.components.ResponseDetailsType;
import de.drv.dsrv.extrastandard.namespace.response.TransportHeader;
import de.drv.dsrv.extrastandard.namespace.response.XMLTransport;
import de.extra.client.core.plugin.IOutputPlugin;

@Named("dummyOutputPlugin")
public class DummyOutputPlugin implements IOutputPlugin {

	// if (flagCode.getValue().equalsIgnoreCase("C00")
	// || flagCode.getValue().equalsIgnoreCase("I000")
	// || flagCode.getValue().equalsIgnoreCase("E98"))

	private static final Logger logger = Logger
			.getLogger(DummyOutputPlugin.class);

	@Override
	public XMLTransport outputData(String request) {
		logger.info(request);
		XMLTransport response = createExtraResponse(request);
		return response;
	}

	private XMLTransport createExtraResponse(String request) {
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
		return response;
	}
}
