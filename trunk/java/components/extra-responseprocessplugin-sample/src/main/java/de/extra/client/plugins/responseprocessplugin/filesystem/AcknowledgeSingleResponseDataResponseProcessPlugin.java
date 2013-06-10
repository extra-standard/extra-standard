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
package de.extra.client.plugins.responseprocessplugin.filesystem;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.oxm.XmlMappingException;
import org.springframework.util.Assert;

import de.drv.dsrv.extra.marshaller.IExtraMarschaller;
import de.drv.dsrv.extra.marshaller.IExtraUnmarschaller;
import de.drv.dsrv.extrastandard.namespace.components.ClassifiableIDType;
import de.drv.dsrv.extrastandard.namespace.components.ReportType;
import de.drv.dsrv.extrastandard.namespace.components.RequestDetailsType;
import de.drv.dsrv.extrastandard.namespace.components.ResponseDetailsType;
import de.drv.dsrv.extrastandard.namespace.response.ResponseTransport;
import de.drv.dsrv.extrastandard.namespace.response.ResponseTransportHeader;
import de.extra.client.core.observer.impl.TransportInfoBuilder;
import de.extra.client.core.responce.impl.SingleReportData;
import de.extra.client.core.responce.impl.SingleResponseData;
import de.extra.client.core.responce.impl.SingleResponseDataForMultipleRequest;
import de.extrastandard.api.exception.ExtraResponseProcessPluginRuntimeException;
import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.model.content.ISingleResponseData;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.api.observer.ITransportInfo;
import de.extrastandard.api.observer.ITransportObserver;
import de.extrastandard.api.plugin.IResponseProcessPlugin;
import de.extrastandard.api.util.IExtraReturnCodeAnalyser;

/**
 * Erwartet nur ein Response in RequestTransport, der für alle Requests gilt
 * 
 * @author DPRS
 * @version $Id$
 */
@Named("acknowledgeSingleResponseDataResponseProcessPlugin")
public class AcknowledgeSingleResponseDataResponseProcessPlugin implements
		IResponseProcessPlugin {

	@Inject
	@Named("extraMarschaller")
	private IExtraMarschaller marshaller;

	@Inject
	@Named("extraUnmarschaller")
	private IExtraUnmarschaller extraUnmarschaller;

	@Inject
	@Named("extraMessageReturnDataExtractor")
	private ExtraMessageReturnDataExtractor returnCodeExtractor;

	@Inject
	@Named("transportObserver")
	private ITransportObserver transportObserver;

	@Inject
	@Named("transportInfoBuilder")
	private TransportInfoBuilder transportInfoBuilder;

	@Inject
	@Named("extraReturnCodeAnalyser")
	private IExtraReturnCodeAnalyser extraReturnCodeAnalyser;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.extra.client.core.plugin.IResponsePlugin#processResponse(de.drv.dsrv
	 * .extrastandard.namespace.response.XMLTransport)
	 */
	@Override
	public IResponseData processResponse(final InputStream responseAsStream) {
		try {

			final ResponseTransport extraResponse = extraUnmarschaller
					.unmarshal(responseAsStream, ResponseTransport.class);

			// Ausgabe der Response im log
			ExtraMessageReturnDataExtractor.printResult(marshaller,
					extraResponse);

			// TODO Validierungsmodul
			final ResponseTransportHeader transportHeader = extraResponse
					.getTransportHeader();
			Assert.notNull(transportHeader,
					"Transportheader in der Acknowledge sind leer");
			final ITransportInfo transportInfo = transportInfoBuilder
					.createTransportInfo(transportHeader);

			transportObserver.responseFilled(transportInfo);

			final ResponseDetailsType responseDetails = transportHeader
					.getResponseDetails();
			Assert.notNull(responseDetails,
					"ResponseDetailsType in der Acknowledge ist leer");
			final RequestDetailsType requestDetails = transportHeader
					.getRequestDetails();
			Assert.notNull(transportHeader,
					"RequestDetailsType in der Acknowledge ist leer");
			final ClassifiableIDType classifiableResponseIDType = responseDetails
					.getResponseID();
			Assert.notNull(classifiableResponseIDType,
					"ResponseID in der Acknowledge ist leer");
			final String responseId = classifiableResponseIDType.getValue();

			// final ReportType report = responseDetails.getReport();

			final ClassifiableIDType classifiableRequestIDType = requestDetails
					.getRequestID();
			Assert.notNull(classifiableRequestIDType,
					"RequestIDType  in der Acknowledge ist leer");
			final String requestId = classifiableRequestIDType.getValue();

			final ReportType report = responseDetails.getReport();
			final SingleReportData reportData = returnCodeExtractor
					.extractReportData(report);
			final String returnCode = reportData.getReturnCode();
			final boolean returnCodeSuccessful = extraReturnCodeAnalyser
					.isReturnCodeSuccessful(returnCode);
			// Status (DONE oder FAIL)
			final PersistentStatus persistentStatus = returnCodeSuccessful ? PersistentStatus.DONE
					: PersistentStatus.FAIL;

			// (17.12.12) keine Server Daten -> kein OutputIdentifier
			final String outputIdentifier = null;

			final ISingleResponseData singleResponseData = new SingleResponseData(
					requestId, returnCode, reportData.getReturnText(),
					responseId, returnCodeSuccessful, persistentStatus,
					outputIdentifier);
			final IResponseData responseData = new SingleResponseDataForMultipleRequest(
					singleResponseData);
			return responseData;
		} catch (final XmlMappingException xmlMappingException) {
			throw new ExtraResponseProcessPluginRuntimeException(
					xmlMappingException);
		} catch (final IOException ioException) {
			throw new ExtraResponseProcessPluginRuntimeException(ioException);
		}

	}

}
