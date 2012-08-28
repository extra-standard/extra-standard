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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

import de.drv.dsrv.extrastandard.namespace.components.DataType;
import de.drv.dsrv.extrastandard.namespace.components.FlagType;
import de.drv.dsrv.extrastandard.namespace.components.ReportType;
import de.drv.dsrv.extrastandard.namespace.components.RequestDetailsType;
import de.drv.dsrv.extrastandard.namespace.components.ResponseDetailsType;
import de.drv.dsrv.extrastandard.namespace.response.Message;
import de.drv.dsrv.extrastandard.namespace.response.Package;
import de.drv.dsrv.extrastandard.namespace.response.TransportBody;
import de.drv.dsrv.extrastandard.namespace.response.TransportHeader;
import de.drv.dsrv.extrastandard.namespace.response.XMLTransport;
import de.extra.client.core.observer.TransportInfoBuilder;
import de.extrastandard.api.model.IResponseData;
import de.extrastandard.api.model.ResponseData;
import de.extrastandard.api.observer.ITransportObserver;
import de.extrastandard.api.observer.impl.TransportInfo;
import de.extrastandard.api.plugin.IResponseProcessPlugin;

@Named("fileSystemResponseProcessPlugin")
public class FileSystemResponseProcessPlugin implements IResponseProcessPlugin {

	private static Logger logger = Logger.getLogger(FileSystemResponseProcessPlugin.class);

	@Inject
	@Named("eXTrajaxb2Marshaller")
	private Marshaller marshaller;

	@Value("${plugins.responseprocessplugin.fileSystemResponseProcessPlugin.eingangOrdner}")
	private File eingangOrdner;

	@Value("${plugins.responseprocessplugin.fileSystemResponseProcessPlugin.reportOrdner}")
	private File reportOrdner;

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
	public List<IResponseData> processResponse(final InputStream responseAsStream) {
		final List<IResponseData> responseDataList = new ArrayList<IResponseData>();
		try {

			de.drv.dsrv.extrastandard.namespace.response.XMLTransport extraResponse;

			extraResponse = (de.drv.dsrv.extrastandard.namespace.response.XMLTransport) unmarshaller
					.unmarshal(new StreamSource(responseAsStream));

			printResult(extraResponse);

			pruefeVerzeichnis();

			final TransportHeader transportHeader = extraResponse.getTransportHeader();
			final TransportInfo transportInfo = transportInfoBuilder.createTransportInfo(transportHeader);
			transportObserver.responseFilled(0, transportInfo);

			final ResponseDetailsType responseDetails = transportHeader.getResponseDetails();
			final RequestDetailsType requestDetails = transportHeader.getRequestDetails();
			if (!isBodyEmpty(extraResponse.getTransportBody())) {

				final List<Package> packageList = extraResponse.getTransportBody().getPackage();
				if (packageList == null || packageList.size() == 0) {
					final String responseId = responseDetails.getResponseID().getValue();
					logger.debug("Keine Pakete vorhanden");
					final byte[] responseBody = extraResponse.getTransportBody().getData().getBase64CharSequence()
							.getValue();

					if (saveBodyToFilesystem(responseId, responseBody)) {
						logger.debug("Speicheren des Body auf Filesystem erfolgreich");
					}

					final IResponseData responseData = new ResponseData(requestDetails.getRequestID().getValue(),
							"C00", "RETURNTEXT", responseId);
					responseDataList.add(responseData);

				} else {
					for (final Iterator<Package> iter = packageList.iterator(); iter.hasNext();) {
						final Package extraPackage = iter.next();

						final String responseId = extraPackage.getPackageHeader().getResponseDetails().getResponseID()
								.getValue();
						DataType data = new DataType();
						data = extraPackage.getPackageBody().getData();
						byte[] packageBody = null;

						if (data.getBase64CharSequence() != null) {
							packageBody = data.getBase64CharSequence().getValue();

						} else {
							if (data.getCharSequence() != null) {
								packageBody = data.getCharSequence().getValue().getBytes();
							}
						}

						if (packageBody != null) {
							if (saveBodyToFilesystem(responseId, packageBody)) {
								if (logger.isDebugEnabled()) {
									logger.debug("Speichern für RespId " + responseId + " erfolgreich");
								}
							}
						} else {
							logger.error("PackageBody nicht gefüllt");

						}
					}
				}
			} else {

				final ReportType report = responseDetails.getReport();
				final String requestId = requestDetails.getRequestID().getValue();
				final String responseId = responseDetails.getResponseID().getValue();

				saveReportToFilesystem(report, responseId, requestId);

				final IResponseData responseData = new ResponseData(requestId, "C00", "RETURNTEXT", responseId);
				responseDataList.add(responseData);
				logger.info("Body leer");
			}

		} catch (final XmlMappingException xmlMappingException) {
			// TODO Exceptionhandling
			throw new IllegalStateException(xmlMappingException);
		} catch (final IOException ioException) {
			// TODO Auto-generated catch block
			throw new IllegalStateException(ioException);
		}
		return responseDataList;
	}

	private void printResult(final XMLTransport extraResponse) {
		try {
			final Writer writer = new StringWriter();
			final StreamResult streamResult = new StreamResult(writer);

			marshaller.marshal(extraResponse, streamResult);
			logger.debug("ExtraResponse: " + writer.toString());
		} catch (final XmlMappingException xmlException) {
			logger.debug("XmlMappingException beim Lesen des Results ", xmlException);
		} catch (final IOException ioException) {
			logger.debug("IOException beim Lesen des Results ", ioException);
		}

	}

	private static boolean isBodyEmpty(final TransportBody transportBody) {
		boolean isEmpty = false;

		if (transportBody == null) {
			isEmpty = true;
		} else {
			if (transportBody.getData() == null || transportBody.getEncryptedData() == null) {

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

	private boolean saveBodyToFilesystem(final String responseId, final byte[] responseBody) {
		final boolean erfolgreichGespeichert = false;

		final StringBuffer dateiName = new StringBuffer();

		dateiName.append(baueDateiname());
		dateiName.append("-");
		dateiName.append(responseId);

		final File responseFile = new File(eingangOrdner, dateiName.toString());

		FileWriter fw = null;

		try {
			fw = new FileWriter(responseFile);

			fw.write(new String(responseBody));
			transportObserver.responseDataForwarded(responseFile.getAbsolutePath(), responseBody.length);

		} catch (final IOException e) {
			logger.error("Fehler beim schreiben der Antwort", e);
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Dateiname: '" + dateiName + "'");
		}

		return erfolgreichGespeichert;
	}

	private boolean saveReportToFilesystem(final ReportType report, final String responseId, final String requestId) {
		final boolean erfolgreichGespeichert = false;

		final StringBuffer dateiName = new StringBuffer();

		dateiName.append(baueDateiname());
		dateiName.append(".rep");

		final File reportFile = new File(reportOrdner, dateiName.toString());

		final List<FlagType> flagList = report.getFlag();
		FlagType flagItem = null;

		if (flagList.size() >= 1) {
			flagItem = flagList.get(0);
		}

		FileWriter fw = null;
		StringBuffer sb;

		try {
			fw = new FileWriter(reportFile);

			sb = new StringBuffer();
			sb.append("Die Meldung mit ID ");
			sb.append(requestId);
			sb.append(" erhielt folgende Antwort:");
			sb.append("\r\n");
			sb.append("ResponseID: ");
			sb.append(responseId);
			sb.append("\r\n");
			sb.append("ReturnCode:");
			sb.append(flagItem.getCode().getValue());
			sb.append("\r\n");
			if (flagItem.getText() != null) {
				sb.append(flagItem.getText().getValue());
			}

			fw.write(sb.toString());
			transportObserver.responseDataForwarded(reportFile.getAbsolutePath(), 0);

		} catch (final IOException e) {
			logger.error("Fehler beim Schreiben des Reports", e);
		} finally {
			try {
				fw.close();
			} catch (final IOException e) {
				logger.error("Fehler beim schließen das FileWriters");
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Report: '" + reportFile.getAbsolutePath() + "'");
		}

		return erfolgreichGespeichert;
	}

	private String baueDateiname() {
		final Date now = Calendar.getInstance().getTime();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmm");
		sdf.format(now);

		return sdf.format(now);
	}

	private void pruefeVerzeichnis() {
		if (!eingangOrdner.exists()) {
			logger.debug("Eingangsordner anlegen");

			eingangOrdner.mkdir();
		}
		if (!reportOrdner.exists()) {
			logger.debug("Reportordner anlegen");

			reportOrdner.mkdir();
		}
	}
}
