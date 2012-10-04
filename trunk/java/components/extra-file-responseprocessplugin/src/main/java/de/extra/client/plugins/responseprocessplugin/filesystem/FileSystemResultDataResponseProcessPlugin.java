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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

import de.drv.dsrv.extrastandard.namespace.components.RequestDetailsType;
import de.drv.dsrv.extrastandard.namespace.components.ResponseDetailsType;
import de.drv.dsrv.extrastandard.namespace.response.Message;
import de.drv.dsrv.extrastandard.namespace.response.Package;
import de.drv.dsrv.extrastandard.namespace.response.TransportBody;
import de.drv.dsrv.extrastandard.namespace.response.TransportHeader;
import de.drv.dsrv.extrastandard.namespace.response.XMLTransport;
import de.extra.client.core.observer.TransportInfoBuilder;
import de.extrastandard.api.exception.ExceptionCode;
import de.extrastandard.api.exception.ExtraResponseProcessPluginRuntimeException;
import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.model.content.ISingleResponseData;
import de.extrastandard.api.model.content.ResponseData;
import de.extrastandard.api.model.content.SingleResponseData;
import de.extrastandard.api.observer.ITransportObserver;
import de.extrastandard.api.observer.impl.TransportInfo;
import de.extrastandard.api.plugin.IResponseProcessPlugin;

/**
 * 
 * Speichert Verarbeitungsergebnisse des Fachverfahren in dem Filesystem. Hier
 * wird initial eine einfache Verarbeitung vorrausgesetzt. Die Daten werden in
 * dem MessageBodybereich in dem Data-Fragment erwartet.
 * 
 * @author DPRS
 * @version $Id$
 */
@Named("fileSystemResultDataResponseProcessPlugin")
public class FileSystemResultDataResponseProcessPlugin implements IResponseProcessPlugin {

	private static final Logger LOG = LoggerFactory.getLogger(FileSystemResultDataResponseProcessPlugin.class);

	@Inject
	@Named("eXTrajaxb2Marshaller")
	private Marshaller marshaller;

	@Value("${plugins.responseprocessplugin.fileSystemResponseProcessPlugin.eingangOrdner}")
	private File eingangOrdner;

	@Inject
	@Named("transportObserver")
	private ITransportObserver transportObserver;

	@Inject
	@Named("eXTrajaxb2Marshaller")
	private Unmarshaller unmarshaller;

	@Inject
	@Named("transportInfoBuilder")
	private TransportInfoBuilder transportInfoBuilder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.extra.client.core.plugin.IResponsePlugin#processResponse(de.drv.dsrv
	 * .extrastandard.namespace.response.XMLTransport)
	 */
	@Override
	public IResponseData processResponse(final InputStream responseAsStream) {
		final IResponseData responseData = new ResponseData();
		try {

			de.drv.dsrv.extrastandard.namespace.response.XMLTransport extraResponse;

			extraResponse = (de.drv.dsrv.extrastandard.namespace.response.XMLTransport) unmarshaller
					.unmarshal(new StreamSource(responseAsStream));

			printResult(extraResponse);

			final TransportHeader transportHeader = extraResponse.getTransportHeader();
			final TransportInfo transportInfo = transportInfoBuilder.createTransportInfo(transportHeader);
			transportObserver.responseFilled(transportInfo);

			final ResponseDetailsType responseDetails = transportHeader.getResponseDetails();
			final RequestDetailsType requestDetails = transportHeader.getRequestDetails();
			if (isBodyEmpty(extraResponse.getTransportBody())) {
				throw new ExtraResponseProcessPluginRuntimeException(ExceptionCode.UNEXPECTED_INTERNAL_EXCEPTION,
						"Keine Daten vorhanden. Body Element ist leer");
			}

			final String responseId = responseDetails.getResponseID().getValue();

			// TODO Valiedierung
			final byte[] responseBody = extraResponse.getTransportBody().getData().getBase64CharSequence().getValue();

			final byte[] decodedData = Base64.decodeBase64(responseBody);

			saveBodyToFilesystem(responseId, decodedData);

			final ISingleResponseData singleResponseData = new SingleResponseData(requestDetails.getRequestID()
					.getValue(), "RETURNCODE", "RETURNTEXT", responseId);
			responseData.addSingleResponse(singleResponseData);

		} catch (final XmlMappingException xmlMappingException) {
			throw new ExtraResponseProcessPluginRuntimeException(xmlMappingException);
		} catch (final IOException ioException) {
			throw new ExtraResponseProcessPluginRuntimeException(ioException);
		}
		return responseData;
	}

	private void printResult(final XMLTransport extraResponse) {
		try {
			final Writer writer = new StringWriter();
			final StreamResult streamResult = new StreamResult(writer);

			marshaller.marshal(extraResponse, streamResult);
			LOG.debug("ExtraResponse: " + writer.toString());
		} catch (final XmlMappingException xmlException) {
			LOG.debug("XmlMappingException beim Lesen des Results ", xmlException);
		} catch (final IOException ioException) {
			LOG.debug("IOException beim Lesen des Results ", ioException);
		}

	}

	private static boolean isBodyEmpty(final TransportBody transportBody) {
		boolean isEmpty = false;

		if (transportBody == null) {
			isEmpty = true;
		} else {
			if (transportBody.getData() == null && transportBody.getEncryptedData() == null) {

				isEmpty = true;
			}

			final List<Package> packageList = transportBody.getPackage();
			final List<Message> messageList = transportBody.getMessage();
			if (messageList.size() == 0 && packageList.size() == 0 && isEmpty) {
				isEmpty = true;
			} else {
				isEmpty = false;
			}
		}

		return isEmpty;
	}

	/**
	 * @param responseId
	 * @param responseBody
	 * @return
	 */
	private void saveBodyToFilesystem(final String responseId, final byte[] responseBody) {
		try {

			final String dateiName = buildFilename(responseId);

			final File responseFile = new File(eingangOrdner, dateiName);

			FileUtils.writeByteArrayToFile(responseFile, responseBody);

			transportObserver.responseDataForwarded(responseFile.getAbsolutePath(), responseBody.length);

			LOG.info("Response gespeichert in File: '" + dateiName + "'");

		} catch (final IOException ioException) {
			throw new ExtraResponseProcessPluginRuntimeException(ExceptionCode.UNEXPECTED_INTERNAL_EXCEPTION,
					"Fehler beim schreiben der Antwort", ioException);
		}
	}

	/**
	 * Erzeugt einen eindeitigen Filenamen mit milissekunden und ResponseID
	 * 
	 * @param responseId
	 * @return
	 */
	private String buildFilename(final String responseId) {
		final StringBuilder fileName = new StringBuilder();
		fileName.append("RESPONSE_").append(responseId);
		fileName.append("_").append(System.currentTimeMillis());
		return fileName.toString();
	}

}
