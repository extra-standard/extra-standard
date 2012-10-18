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
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.util.Assert;

import de.drv.dsrv.extrastandard.namespace.components.Base64CharSequenceType;
import de.drv.dsrv.extrastandard.namespace.components.ClassifiableIDType;
import de.drv.dsrv.extrastandard.namespace.components.DataType;
import de.drv.dsrv.extrastandard.namespace.components.ElementSequenceType;
import de.drv.dsrv.extrastandard.namespace.components.FlagCodeType;
import de.drv.dsrv.extrastandard.namespace.components.FlagType;
import de.drv.dsrv.extrastandard.namespace.components.ReportType;
import de.drv.dsrv.extrastandard.namespace.components.RequestDetailsType;
import de.drv.dsrv.extrastandard.namespace.components.ResponseDetailsType;
import de.drv.dsrv.extrastandard.namespace.components.TextType;
import de.drv.dsrv.extrastandard.namespace.messages.DataRequest;
import de.drv.dsrv.extrastandard.namespace.messages.DataRequestArgument;
import de.drv.dsrv.extrastandard.namespace.messages.DataRequestQuery;
import de.drv.dsrv.extrastandard.namespace.messages.Operand;
import de.drv.dsrv.extrastandard.namespace.messages.OperandSet;
import de.drv.dsrv.extrastandard.namespace.request.Transport;
import de.drv.dsrv.extrastandard.namespace.response.Package;
import de.drv.dsrv.extrastandard.namespace.response.PackageBody;
import de.drv.dsrv.extrastandard.namespace.response.PackageHeader;
import de.drv.dsrv.extrastandard.namespace.response.TransportBody;
import de.drv.dsrv.extrastandard.namespace.response.TransportHeader;
import de.drv.dsrv.extrastandard.namespace.response.XMLTransport;
import de.extra.client.core.observer.impl.TransportInfoBuilder;
import de.extrastandard.api.exception.ExtraOutputPluginRuntimeException;
import de.extrastandard.api.observer.ITransportInfo;
import de.extrastandard.api.observer.ITransportObserver;
import de.extrastandard.api.plugin.IOutputPlugin;

/**
 * Setzt die erwarteten Felder für ein FetchResponse. Dieser Implementierung
 * simuliert ein Response auf eine DataRequest.Query
 * 
 * @author Leonid Potap
 * @version $Id$
 */
@Named("dummyQueryDataResponceOutputPlugin")
public class DummyQueryDataResponceOutputPlugin implements IOutputPlugin {

	private static final Logger LOG = LoggerFactory.getLogger(DummyQueryDataResponceOutputPlugin.class);

	@Inject
	@Named("eXTrajaxb2Marshaller")
	private Unmarshaller unmarshaller;

	@Inject
	@Named("eXTrajaxb2Marshaller")
	private Marshaller marshaller;

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
			final XMLTransport response = createExtraResponse(request);

			final Writer writer = new StringWriter();
			final StreamResult streamResult = new StreamResult(writer);

			marshaller.marshal(response, streamResult);

			responseAsinputStream = new ByteArrayInputStream(writer.toString().getBytes());
			return responseAsinputStream;
		} catch (final IOException ioException) {
			// Hier kommt eine ExtraTechnischeRuntimeException
			LOG.error("Unerwarteter Fehler: ", ioException);
		}
		return responseAsinputStream;

	}

	private XMLTransport createExtraResponse(final InputStream request) {
		try {

			final Transport requestXml = (Transport) unmarshaller.unmarshal(new StreamSource(request));

			// Ich gehe davon aus, dass requestId ein Mandatory Feld ist
			final String requestId = requestXml.getTransportHeader().getRequestDetails().getRequestID().getValue();
			final XMLTransport response = new XMLTransport();
			final TransportHeader transportHeader = new TransportHeader();
			final ResponseDetailsType responseDetailsType = new ResponseDetailsType();
			final ClassifiableIDType idType = new ClassifiableIDType();
			idType.setValue("42");
			responseDetailsType.setResponseID(idType);
			final ReportType reportType = createPositiveReportType();
			responseDetailsType.setReport(reportType);
			transportHeader.setResponseDetails(responseDetailsType);
			final RequestDetailsType requestDetailsType = new RequestDetailsType();
			final ClassifiableIDType requestIdType = new ClassifiableIDType();
			requestIdType.setValue(requestId);
			requestDetailsType.setRequestID(requestIdType);
			transportHeader.setRequestDetails(requestDetailsType);
			response.setTransportHeader(transportHeader);
			final de.drv.dsrv.extrastandard.namespace.request.TransportHeader requestHeader = new de.drv.dsrv.extrastandard.namespace.request.TransportHeader();
			requestHeader.setRequestDetails(requestDetailsType);
			final ITransportInfo transportInfo = transportInfoBuilder.createTransportInfo(requestHeader);
			final TransportBody transportBody = new TransportBody();
			response.setTransportBody(transportBody);
			final List<String> queryArguments = getQueryArguments(requestXml);
			for (final String queryArgument : queryArguments) {
				final Package trancportBodyPackage = new Package();
				final PackageBody packageBody = createDummyBodyResponse(queryArgument);
				trancportBodyPackage.setPackageBody(packageBody);
				final PackageHeader packageHeader = new PackageHeader();

				// Dummy ResponseId
				final ClassifiableIDType packageHeaderResponseDetailsIdType = new ClassifiableIDType();
				packageHeaderResponseDetailsIdType.setValue(queryArgument);
				final ResponseDetailsType packageResponseDetailsType = new ResponseDetailsType();
				packageResponseDetailsType.setResponseID(packageHeaderResponseDetailsIdType);
				packageHeader.setResponseDetails(packageResponseDetailsType);
				// Dummy RequestId
				final ClassifiableIDType packageHeaderRequestIdType = new ClassifiableIDType();
				packageHeaderRequestIdType.setValue(queryArgument);
				final RequestDetailsType headerRequestDetailsType = new RequestDetailsType();
				headerRequestDetailsType.setRequestID(packageHeaderRequestIdType);
				packageHeader.setRequestDetails(headerRequestDetailsType);

				final ReportType packageReportType = createPositiveReportType();
				packageResponseDetailsType.setReport(packageReportType);

				trancportBodyPackage.setPackageHeader(packageHeader);
				transportBody.getPackage().add(trancportBodyPackage);
			}
			transportObserver.requestFilled(transportInfo);
			transportObserver.requestForwarded("dummy, keine Weiterleitung", 0);
			return response;
		} catch (final XmlMappingException xmlMappingException) {
			throw new ExtraOutputPluginRuntimeException(xmlMappingException);
		} catch (final IOException ioException) {
			throw new ExtraOutputPluginRuntimeException(ioException);
		}
	}

	/**
	 * @param responseDetailsType
	 */
	private ReportType createPositiveReportType() {
		final ReportType reportType = new ReportType();
		final FlagType flagType = new FlagType();
		final FlagCodeType flagCodeType = new FlagCodeType();
		flagCodeType.setValue("C00");
		flagType.setCode(flagCodeType);
		final TextType flagCodeText = new TextType();
		flagCodeText.setValue("O.K.");
		flagType.setText(flagCodeText);
		reportType.getFlag().add(flagType);
		return reportType;

	}

	/**
	 * @return Dummy Body Response
	 */
	private PackageBody createDummyBodyResponse(final String queryArgument) {
		final PackageBody packageBody = new PackageBody();
		final DataType value = new DataType();
		final String stringValue = "DUMMY Response for Query Argument:" + queryArgument;
		final byte[] decodeBase64Value = Base64.encodeBase64(stringValue.getBytes());

		final Base64CharSequenceType base64CharSequenceType = new Base64CharSequenceType();
		base64CharSequenceType.setValue(decodeBase64Value);
		value.setBase64CharSequence(base64CharSequenceType);
		packageBody.setData(value);
		return packageBody;
	}

	/**
	 * Liefert QueryArguments als List
	 * 
	 * @return
	 */
	private List<String> getQueryArguments(final Transport requestXml) {
		final de.drv.dsrv.extrastandard.namespace.request.TransportBody transportBody = requestXml.getTransportBody();
		Assert.notNull(transportBody, "TransportBody is null");
		final DataType data = transportBody.getData();
		Assert.notNull(data, "TransportData is null");
		final ElementSequenceType elementSequence = data.getElementSequence();
		Assert.notNull(elementSequence, "TransportData.elementSequence is null");
		final List<Object> any = elementSequence.getAny();
		Assert.notNull(any, "ElementSequence is empty is null");
		Assert.isTrue(any.size() == 1, "ElementSequense beinhaltet mehr als ein Element");
		final Object dataRequestObject = any.get(0);
		Assert.isAssignable(DataRequest.class, dataRequestObject.getClass(), "Unexpectede ElementSequence entry"
				+ dataRequestObject.getClass() + " Expected DataRequest.");
		final DataRequest dataRequest = (DataRequest) dataRequestObject;
		final DataRequestQuery query = dataRequest.getQuery();
		Assert.notNull(query, "Query is null");
		final List<DataRequestArgument> argument = query.getArgument();
		Assert.notNull(argument, "DataRequestArgument List is null");
		Assert.isTrue(argument.size() == 1, "DataRequestArgument List beinhaltet mehr als ein Element");
		final DataRequestArgument dataRequestArgument = argument.get(0);
		Assert.notNull(dataRequestArgument, "DataRequestArgument is null");
		final List<JAXBElement<?>> dataRequestArgumentContent = dataRequestArgument.getContent();
		Assert.notNull(dataRequestArgumentContent, "DataRequestArgument.content is null");
		Assert.isTrue(dataRequestArgumentContent.size() == 1,
				"DataRequestArgument.content  beinhaltet mehr als ein Element");
		final JAXBElement<?> jaxbElement = dataRequestArgumentContent.get(0);

		final Object operandSetObject = jaxbElement.getValue();
		Assert.isAssignable(OperandSet.class, operandSetObject.getClass(),
				"Unexpectede dataRequestArgumentContent entry" + dataRequestObject.getClass() + " Expected OperandSet.");
		final OperandSet operandSet = (OperandSet) operandSetObject;
		final List<Operand> operandEQ = operandSet.getEQ();
		Assert.notNull(operandEQ, "operandEQ is null");
		Assert.notEmpty(operandEQ, "operandEQ is empty");
		final List<String> queryArgumentList = new ArrayList<String>();
		for (final Operand operand : operandEQ) {
			final String operandValue = operand.getValue();
			queryArgumentList.add(operandValue);
		}
		return queryArgumentList;
	}
}
