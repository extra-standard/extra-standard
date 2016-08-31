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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.bind.JAXBException;

import org.apache.cxf.attachment.LazyDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.drv.dsrv.spoc.extra.v1_3.ExtraJaxbMarshaller;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.FlagType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.response.TransportResponseType;
import de.extra.extraClientLight.model.ResponseExtraBean;
import de.extra.extraClientLight.model.enums.ReportLevelEnum;
import de.extra.extraClientLight.util.ClientConstants;

public class ExtraResponseHelper {

	private static Logger LOGGER = LoggerFactory
			.getLogger(ExtraResponseHelper.class);

	private static ResponseExtraBean responseBean;

	public static ResponseExtraBean convertExtraResponse(
			TransportResponseType extraResponse) {
		responseBean = new ResponseExtraBean();

		responseBean.setResponseId(extraResponse.getTransportHeader()
				.getResponseDetails().getResponseID().getValue());
		responseBean.setRequestId(extraResponse.getTransportHeader()
				.getRequestDetails().getRequestID().getValue());

		FlagType reportFlag = extraResponse.getTransportHeader()
				.getResponseDetails().getReport().getFlag().get(0);

		getReportInformation(reportFlag);

		if (extraResponse.getTransportBody().getData() != null) {
			try {

				// Ermitteln der DataSource aus dem DataHandler

				DataSource nutzdatenDS = extraResponse.getTransportBody()
						.getData().getBase64CharSequence().getValue()
						.getDataSource();

				InputStream in = null;

				// Wenn Nutzdaten inline sind, dann ist DataSource vom Typ
				// ByteArrayDataSource

				if (LOGGER.isDebugEnabled()) {

					LOGGER.debug("Datentyp der Nutzdaten: "
							+ nutzdatenDS.getClass());

				}

				// Wenn Nutzdaten als MTOM-Attachment �bermittelt werden, dann
				// sind
				// die Daten innerhalb einer LazyDataSource

				if (nutzdatenDS instanceof LazyDataSource) {
					LOGGER.debug("Payload wurde als MTOM/LazyDataSource erkannt");

					in = nutzdatenDS.getInputStream();

				} else {

					LOGGER.warn("Es konnte kein passender Datentyp gefunden werden! Wandle in Stream um");

					in = extraResponse.getTransportBody().getData()
							.getBase64CharSequence().getValue()
							.getInputStream();

				}

				responseBean.setData(in);
			} catch (IOException e) {
				LOGGER.error("Fehler beim Lesen des Datenstreams");
				responseBean.setReturnCode(9);
			}

		} else {

			LOGGER.info("Keine Nutzdaten gefunden");
		}

		return responseBean;
	}

	private static void getReportInformation(FlagType reportFlag) {

		responseBean.setReportCode(reportFlag.getCode().getValue());
		responseBean.setReportText(reportFlag.getText().getValue());

		if (reportFlag.getWeight().equalsIgnoreCase(
				ClientConstants.EXTRA_WEIGHT_INFO)) {

			responseBean.setReportLevel(ReportLevelEnum.INFO);
			responseBean.setReturnCode(0);
		} else if (reportFlag.getWeight().equalsIgnoreCase(
				ClientConstants.EXTRA_WEIGHT_WARN)) {
			responseBean.setReturnCode(1);
			responseBean.setReportLevel(ReportLevelEnum.WARN);
		} else {

			responseBean.setReportLevel(ReportLevelEnum.ERROR);
			responseBean.setReturnCode(2);
		}
	}

	public static void printResponse(TransportResponseType response) {

		try {

			ExtraJaxbMarshaller extraMarshaller = new ExtraJaxbMarshaller();

			OutputStream outputStream = new ByteArrayOutputStream();

			String responseString = extraMarshaller
					.marshalTransportResponse(response);

			LOGGER.debug("eXTra-Marshaller: " + responseString);

		} catch (JAXBException e) {
			LOGGER.error("Fehler beim marshalling", e);
		}
	}
}
