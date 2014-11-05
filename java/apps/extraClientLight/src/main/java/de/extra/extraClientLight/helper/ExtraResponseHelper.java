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

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.extra.extraClientLight.model.ResponseExtraBean;
import de.extra.extraClientLight.model.enums.ReportLevelEnum;
import de.extra.extraClientLight.util.ClientConstants;
import de.extra_standard.namespace.components._1.FlagType;
import de.extra_standard.namespace.response._1.TransportResponseType;

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
		try {
			final InputStream in = extraResponse.getTransportBody().getData()
					.getBase64CharSequence().getValue().getInputStream();

			responseBean.setData(IOUtils.toByteArray(in));
		} catch (IOException e) {
			LOGGER.error("Fehler beim Lesen des Datenstreams");
			responseBean.setReturnCode(9);
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
}
