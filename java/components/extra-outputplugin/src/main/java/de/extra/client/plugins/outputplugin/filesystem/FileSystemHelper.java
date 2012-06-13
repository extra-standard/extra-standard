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
package de.extra.client.plugins.outputplugin.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import de.extra.client.plugins.outputplugin.utils.IResponseSaver;
import de.extra.client.plugins.outputplugin.utils.OutputPluginHelper;

public class FileSystemHelper implements IResponseSaver, Serializable {

	private final String eingangOrdner;

	private final String reportOrdner;

	public FileSystemHelper(final String eingangOrdner,
			final String reportOrdner) {
		super();
		this.eingangOrdner = eingangOrdner;
		this.reportOrdner = reportOrdner;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1607616003288362662L;

	private static Logger logger = Logger.getLogger(FileSystemHelper.class);

	public boolean processResponse(XMLTransport extraResponse) {

		pruefeVerzeichnis();

		List<Package> packageList = extraResponse.getTransportBody()
				.getPackage();

		if (!OutputPluginHelper.isBodyEmpty(extraResponse.getTransportBody())) {

			if (packageList == null || packageList.size() == 0) {
				String responseId = extraResponse.getTransportHeader()
						.getResponseDetails().getResponseID().getValue();

				logger.debug("Keine Pakete vorhanden");

				byte[] responseBody = extraResponse.getTransportBody()
						.getData().getBase64CharSequence().getValue();

				if (saveBodyToFilesystem(responseId, responseBody)) {

					logger.debug("Speicheren des Body auf Filesystem erfolgreich");

				}

			} else {

				for (Iterator iter = packageList.iterator(); iter.hasNext();) {
					Package extraPackage = (Package) iter.next();

					String responseId = extraPackage.getPackageHeader()
							.getResponseDetails().getResponseID().getValue();
					DataType data = new DataType();

					data = extraPackage.getPackageBody().getData();
					byte[] packageBody = null;
					if (data.getBase64CharSequence() != null) {

						packageBody = data.getBase64CharSequence().getValue();

					} else {
						if (data.getCharSequence() != null) {

							packageBody = data.getCharSequence().getValue()
									.getBytes();

						}
					}

					if (packageBody != null) {
						if (saveBodyToFilesystem(responseId, packageBody)) {
							if (logger.isDebugEnabled()) {
								logger.debug("Speichern f�r RespId "
										+ responseId + " erfolgreich");
							}
						}
					} else {

						logger.error("PackageBody nicht gef�llt");

					}
				}

			}
		} else {

			ReportType report = extraResponse.getTransportHeader()
					.getResponseDetails().getReport();

			String requestId = extraResponse.getTransportHeader()
					.getRequestDetails().getRequestID().getValue();
			String responseId = extraResponse.getTransportHeader()
					.getResponseDetails().getResponseID().getValue();

			saveReportToFilesystem(report, responseId, requestId);

			logger.info("Body leer");

		}

		return false;
	}

	private boolean saveBodyToFilesystem(String responseId, byte[] responseBody) {

		boolean erfolgreichGespeichert = false;

		StringBuffer dateiName = new StringBuffer();

		dateiName.append(eingangOrdner);
		dateiName.append(baueDateiname());
		dateiName.append("-");
		dateiName.append(responseId);

		File responseFile = new File(dateiName.toString());

		FileOutputStream fos = null;

		try {

			fos = new FileOutputStream(dateiName.toString());

			fos.write(responseBody);

		} catch (IOException e) {
			logger.error("Fehler beim schreiben der Antwort", e);
		} finally {

			try {
				fos.close();
			} catch (IOException e) {
				logger.error("Fehler beim schliessen des FileOutputStreams");
			}

		}

		if (logger.isTraceEnabled()) {
			logger.trace("Dateiname: '" + dateiName + "'");
		}
		return erfolgreichGespeichert;
	}

	private boolean saveReportToFilesystem(ReportType report,
			String responseId, String requestId) {
		boolean erfolgreichGespeichert = false;

		StringBuffer dateiName = new StringBuffer();

		dateiName.append(reportOrdner);
		dateiName.append(baueDateiname());
		dateiName.append(".rep");

		File reportFile = new File(dateiName.toString());

		List<FlagType> flagList = report.getFlag();
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
			sb.append(flagItem.getText().getValue());

			fw.write(sb.toString());

		} catch (IOException e) {
			logger.error("Fehler beim Schreiben des Reports", e);
		} finally {

			try {
				fw.close();

			} catch (IOException e) {
				logger.error("Fehler beim schlie�en das FileWriters");
			}

		}

		if (logger.isTraceEnabled()) {
			logger.trace("Report: '" + dateiName + "'");
		}

		return erfolgreichGespeichert;
	}

	private String baueDateiname() {

		Date now = Calendar.getInstance().getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmm");

		sdf.format(now);

		return sdf.format(now);
	}

	private void pruefeVerzeichnis() {

		File eingangsOrdner = new File(eingangOrdner);
		File reportsOrdner = new File(reportOrdner);

		if (!eingangsOrdner.exists()) {

			logger.debug("Eingangsordner anlegen");

			eingangsOrdner.mkdir();
		}
		if (!reportsOrdner.exists()) {

			logger.debug("Reportordner anlegen");

			reportsOrdner.mkdir();
		}

	}

}
