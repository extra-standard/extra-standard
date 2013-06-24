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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.activation.DataHandler;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBElement;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.XmlMappingException;
import org.springframework.util.Assert;

import de.drv.dsrv.extra.codelist.DataContainerCode;
import de.drv.dsrv.extra.marshaller.IExtraMarschaller;
import de.drv.dsrv.extra.marshaller.IExtraUnmarschaller;
import de.drv.dsrv.extrastandard.namespace.components.AnyPlugInContainerType;
import de.drv.dsrv.extrastandard.namespace.components.Base64CharSequenceType;
import de.drv.dsrv.extrastandard.namespace.components.DataType;
import de.drv.dsrv.extrastandard.namespace.components.FlagType;
import de.drv.dsrv.extrastandard.namespace.components.ReportType;
import de.drv.dsrv.extrastandard.namespace.components.RequestDetailsType;
import de.drv.dsrv.extrastandard.namespace.components.ResponseDetailsType;
import de.drv.dsrv.extrastandard.namespace.plugins.DataContainerType;
import de.drv.dsrv.extrastandard.namespace.plugins.DataSource;
import de.drv.dsrv.extrastandard.namespace.response.ResponseMessage;
import de.drv.dsrv.extrastandard.namespace.response.ResponsePackage;
import de.drv.dsrv.extrastandard.namespace.response.ResponsePackageBody;
import de.drv.dsrv.extrastandard.namespace.response.ResponsePackageHeader;
import de.drv.dsrv.extrastandard.namespace.response.ResponseTransport;
import de.drv.dsrv.extrastandard.namespace.response.ResponseTransportBody;
import de.drv.dsrv.extrastandard.namespace.response.ResponseTransportHeader;
import de.extra.client.core.annotation.PluginConfigType;
import de.extra.client.core.annotation.PluginConfiguration;
import de.extra.client.core.annotation.PluginValue;
import de.extra.client.core.observer.impl.TransportInfoBuilder;
import de.extra.client.core.responce.impl.ResponseData;
import de.extra.client.core.responce.impl.SingleReportData;
import de.extra.client.core.responce.impl.SingleResponseData;
import de.extrastandard.api.exception.ExceptionCode;
import de.extrastandard.api.exception.ExtraResponseProcessPluginRuntimeException;
import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.model.content.ISingleResponseData;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.api.observer.ITransportInfo;
import de.extrastandard.api.observer.ITransportObserver;
import de.extrastandard.api.plugin.IResponseProcessPlugin;
import de.extrastandard.api.util.IExtraReturnCodeAnalyser;

/**
 * 
 * Speichert Verarbeitungsergebnisse des Fachverfahren in dem Filesystem. Hier
 * wird initial eine einfache Verarbeitung vorrausgesetzt. Die Daten werden in
 * dem ResponseTransportBody.Package.PackageBody in dem Data-Fragment erwartet.
 * 
 * @author DPRS
 * @version $Id$
 */
@Named("fileSystemResultPackageDataResponseProcessPlugin")
@PluginConfiguration(pluginBeanName = "fileSystemResultPackageDataResponseProcessPlugin", pluginType = PluginConfigType.ResponseProcessPlugins)
public class FileSystemResultPackageDataResponseProcessPlugin implements
		IResponseProcessPlugin {

	private static final Logger logger = LoggerFactory
			.getLogger(FileSystemResultPackageDataResponseProcessPlugin.class);

	@PluginValue(key = "eingangOrdner")
	@NotNull
	private File eingangOrdner;

	/**
	 * Wird benötigt, um nach einer Serververarbeitung den Status im
	 * Communication Protocol zu setzen. So kann z.B. der Status 'WAIT'
	 * anzeigen, das das Ergebnis vom Server erfolgreich empfangen wurde aber
	 * eine Bearbeitung/Prüfung durch eine externe Anwendung noch aussteht.
	 */
	@PluginValue(key = "successStatus")
	private String successStatus;

	@Inject
	@Named("transportInfoBuilder")
	private TransportInfoBuilder transportInfoBuilder;

	@Inject
	@Named("extraMarschaller")
	private IExtraMarschaller marshaller;

	@Inject
	@Named("extraUnmarschaller")
	private IExtraUnmarschaller extraUnmarschaller;

	@Inject
	@Named("transportObserver")
	private ITransportObserver transportObserver;

	@Inject
	@Named("extraMessageReturnDataExtractor")
	private ExtraMessageReturnDataExtractor returnCodeExtractor;

	@Inject
	@Named("extraReturnCodeAnalyser")
	private IExtraReturnCodeAnalyser extraReturnCodeAnalyser;

	/**
	 * Erwartet Ergebnisse als Daten in den Felder
	 * ResponseTransportBody.Package.PackageBody in dem Data-Fragment
	 * 
	 * @see de.extra.client.core.plugin.IResponsePlugin#processResponse(de.drv.dsrv
	 *      .extrastandard.namespace.response.XMLTransport)
	 */
	@Override
	public IResponseData processResponse(final ResponseTransport extraResponse) {
		final IResponseData responseData = new ResponseData();
		try {

			// Ausgabe der Response im log
			// ExtraMessageReturnDataExtractor.printResult(marshaller,
			// extraResponse);

			logger.info("ResponsePlugin. ProcessResponse");

			final ResponseTransportHeader transportHeader = extraResponse
					.getTransportHeader();
			final ITransportInfo transportInfo = transportInfoBuilder
					.createTransportInfo(transportHeader);
			transportObserver.responseFilled(transportInfo);

			final ResponseDetailsType responseDetails = transportHeader
					.getResponseDetails();
			final RequestDetailsType requestDetails = transportHeader
					.getRequestDetails();
			if (isBodyEmpty(extraResponse.getTransportBody())) {
				// (21.11.12) Keine Ergebnisse ermoeglichen!
				// (04.01.13) Warnung signalisieren!
				responseData.setWarning(true);
				return responseData;
				// throw new ExtraResponseProcessPluginRuntimeException(
				// ExceptionCode.UNEXPECTED_INTERNAL_EXCEPTION,
				// "Keine Daten vorhanden. Body Element ist leer");
			}

			// -- Fehler/Status aus TransportHeader auswerten --
			final String responseId = responseDetails.getResponseID()
					.getValue();

			final ReportType report = responseDetails.getReport();
			final SingleReportData reportData = returnCodeExtractor
					.extractReportData(report);

			final String returnCode = reportData.getReturnCode();
			final boolean returnCodeSuccessful = extraReturnCodeAnalyser
					.isReturnCodeSuccessful(returnCode);
			if (returnCodeSuccessful == false) {
				// Falls ein Fehler im Header angezeigt wird, wird der Body (=
				// Einzelergebnisse)
				// nicht mehr ausgewertet! Fehler wird hier zugeordnet
				// final String outputIdentifier = responseId;
				final ISingleResponseData singleResponseData = new SingleResponseData(
						requestDetails.getRequestID().getValue(), returnCode,
						reportData.getReturnText(), responseId,
						returnCodeSuccessful, PersistentStatus.FAIL, responseId);
				responseData.addSingleResponse(singleResponseData);
			} else {
				// Einzelergebnisse auswerten
				final ResponseTransportBody transportBody = extraResponse
						.getTransportBody();
				Assert.notNull(transportBody, "ResponseTransportBody is null");
				final List<ResponsePackage> packages = transportBody
						.getPackage();
				Assert.notEmpty(packages,
						"ResponseTransportBody.Package() is empty");
				for (final ResponsePackage transportBodyPackage : packages) {
					final ResponsePackageBody packageBody = transportBodyPackage
							.getPackageBody();
					Assert.notNull(packageBody, "PackageBody is null");
					final DataType data = packageBody.getData();
					Assert.notNull(data, "PackageBody.data is null");
					final Base64CharSequenceType base64CharSequence = data
							.getBase64CharSequence();
					Assert.notNull(base64CharSequence,
							"Base64CharSequenceType.data is null");
					final DataHandler packageBodyDataHandler = base64CharSequence
							.getValue();

					// final byte[] decodedpackageBodyData = Base64
					// .decodeBase64(packageBodyData);
					final ResponsePackageHeader packageHeader = transportBodyPackage
							.getPackageHeader();

					final ResponseDetailsType packageHeaderResponseDetails = packageHeader
							.getResponseDetails();
					final String packageHeaderResponseId = packageHeaderResponseDetails
							.getResponseID().getValue();
					final String incomingFileName = extractIncomingFileNameFromDataSource(transportBodyPackage
							.getPackagePlugIns());
					final String savedFileName = saveBodyToFilesystem(
							incomingFileName, packageHeaderResponseId,
							packageBodyDataHandler);
					final ISingleResponseData singlePackageResponseData = extractResponseDetail(
							packageHeader, extraReturnCodeAnalyser,
							savedFileName);
					responseData.addSingleResponse(singlePackageResponseData);
				}
			}

			logger.info("ReponseData processed. {}", responseData);
			return responseData;
		} catch (final XmlMappingException xmlMappingException) {
			throw new ExtraResponseProcessPluginRuntimeException(
					xmlMappingException);
		}

	}

	/**
	 * 
	 * @param packagePlugIns
	 * @return element Name aus der PackagePlugIns.DataSource mit dem Type File,
	 *         wenn Element nicht vorhanden ist, wird null zurückgeliefert
	 */
	private String extractIncomingFileNameFromDataSource(
			final AnyPlugInContainerType packagePlugIns) {
		String fileName = null;
		if (packagePlugIns != null) {
			final List<Object> plugins = packagePlugIns.getAny();
			for (final Object plugin : plugins) {

				final JAXBElement<?> jaxbElement = (JAXBElement<?>) plugin;

				if (DataSource.class.isAssignableFrom(jaxbElement
						.getDeclaredType())) {
					final DataSource dataSource = DataSource.class
							.cast(jaxbElement.getValue());
					final DataContainerType dataContainer = dataSource
							.getDataContainer();
					// Strikte Validierung. Element DataSource ohne
					// DataContainer
					// ist in diesem Scenario nicht vereinbart
					Assert.notNull(dataContainer,
							"DataContainer from ElementDataSource is null");
					if (DataContainerCode.FILE.equals(dataContainer.getType())) {
						if (fileName == null) {
							fileName = dataContainer.getName();
						} else {
							throw new ExtraResponseProcessPluginRuntimeException(
									ExceptionCode.EXTRA_ILLEGAL_ACCESS_EXCEPTION,
									"Multiple DataContainer for DataSource found");
						}
					}
				}
			}
		}
		return fileName;
	}

	private ISingleResponseData extractResponseDetail(
			final ResponsePackageHeader packageHeader,
			final IExtraReturnCodeAnalyser extraReturnCodeAnalyser,
			final String savedFileName) {
		Assert.notNull(packageHeader, "PackageHeader.data is null");
		final RequestDetailsType packageHeaderRequestDetails = packageHeader
				.getRequestDetails();
		final String packageHeaderRequestId = packageHeaderRequestDetails
				.getRequestID().getValue();
		final ResponseDetailsType packageHeaderResponseDetails = packageHeader
				.getResponseDetails();
		final String packageHeaderResponseId = packageHeaderResponseDetails
				.getResponseID().getValue();
		final ReportType report = packageHeaderResponseDetails.getReport();
		String reportCode = null;
		String reportText = null;
		Boolean successful = null;
		if (report != null) {
			final List<FlagType> flag = report.getFlag();
			if (flag != null && !flag.isEmpty()) {
				final FlagType flagType = flag.get(0);
				if (flagType != null) {
					reportCode = flagType.getCode().getValue();
					reportText = flagType.getText().getValue();
					successful = extraReturnCodeAnalyser
							.isReturnCodeSuccessful(reportCode);
				}
			}
		}
		// (17.12.12) Ergebnis-Dateiname als OutputIdentifier, Status setzen
		// ('WAIT'?!)
		final ISingleResponseData singleResponseData = new SingleResponseData(
				packageHeaderRequestId, reportCode, reportText,
				packageHeaderResponseId, successful,
				calcResponseStatus(successful), savedFileName);
		return singleResponseData;
	}

	private PersistentStatus calcResponseStatus(final Boolean successful) {
		if (successful == null || successful.booleanValue() == false) {
			return PersistentStatus.FAIL;
		}
		// TODO WAIT aus Konstante?!
		if (successStatus != null && successStatus.toLowerCase().equals("wait")) {
			return PersistentStatus.WAIT;
		}
		return PersistentStatus.DONE;
	}

	private static boolean isBodyEmpty(final ResponseTransportBody transportBody) {
		boolean isEmpty = false;

		if (transportBody == null) {
			isEmpty = true;
		} else {
			if (transportBody.getData() == null
					&& transportBody.getEncryptedData() == null) {

				isEmpty = true;
			}

			final List<ResponsePackage> packageList = transportBody
					.getPackage();
			final List<ResponseMessage> messageList = transportBody
					.getMessage();
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
	 * @return fileName
	 */
	private String saveBodyToFilesystem(final String incomingFileName,
			final String responseId, final DataHandler packageBodyDataHandler) {
		try {

			final String dateiName = buildFilename(incomingFileName, responseId);

			final File responseFile = new File(eingangOrdner, dateiName);

			final FileOutputStream fileOutputStream = new FileOutputStream(
					responseFile);
			final String dataHandlerName = packageBodyDataHandler.getName();
			logger.info("Receiving File : " + dataHandlerName);

			final List<String> readLines = IOUtils
					.readLines(packageBodyDataHandler.getInputStream());

			IOUtils.copy(packageBodyDataHandler.getInputStream(),
					fileOutputStream);

			IOUtils.closeQuietly(fileOutputStream);

			logger.info("Input file is stored under "
					+ responseFile.getAbsolutePath());
			logger.info("ChecksumCRC32 "
					+ FileUtils.checksumCRC32(responseFile));
			logger.info("Filesize: " + FileUtils.sizeOf(responseFile));

			transportObserver.responseDataForwarded(
					responseFile.getAbsolutePath(), responseFile.length());

			logger.info("Response gespeichert in File: '" + dateiName + "'");

			return dateiName;
		} catch (final IOException ioException) {
			throw new ExtraResponseProcessPluginRuntimeException(
					ExceptionCode.UNEXPECTED_INTERNAL_EXCEPTION,
					"Fehler beim schreiben der Antwort", ioException);
		}
	}

	/**
	 * 
	 * Wenn IncomingFileName ber DataSource Plugin gesetzt ist , wird dieser
	 * Name übernommen, sonst Erzeugt einen eindeitigen Filenamen mit
	 * milissekunden und ResponseID.
	 * 
	 * @param responseId
	 * @return
	 */
	private String buildFilename(final String incomingFileName,
			final String responseId) {
		final StringBuilder fileName = new StringBuilder();
		if (incomingFileName != null) {
			fileName.append(incomingFileName);
		} else {
			final String cleanResponseId = FilenameUtils.normalize(responseId);
			fileName.append("RESPONSE_").append(cleanResponseId);
			fileName.append("_").append(System.currentTimeMillis());
		}
		return fileName.toString();
	}

	/**
	 * @param eingangOrdner
	 *            the eingangOrdner to set
	 */
	public void setEingangOrdner(final File eingangOrdner) {
		this.eingangOrdner = eingangOrdner;
	}

	public void setSuccessStatus(final String aSuccessStatus) {
		successStatus = aSuccessStatus;
	}
}
