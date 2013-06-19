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
package de.extra.client.plugins.outputplugin.mtomws;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.drv.dsrv.extrastandard.namespace.components.FlagCodeType;
import de.drv.dsrv.extrastandard.namespace.components.FlagType;
import de.drv.dsrv.extrastandard.namespace.components.ReportType;
import de.drv.dsrv.extrastandard.namespace.components.TextType;
import de.drv.dsrv.extrastandard.namespace.error.Error;
import de.drv.dsrv.extrastandard.namespace.request.RequestTransport;
import de.drv.dsrv.extrastandard.namespace.response.ResponseTransport;
import de.extra_standard.namespace.webservice.Extra;
import de.extra_standard.namespace.webservice.ExtraFault;
import de.extrastandard.api.exception.ExceptionCode;
import de.extrastandard.api.exception.ExtraOutputPluginRuntimeException;
import de.extrastandard.api.plugin.IOutputPlugin;

/**
 * @author Leonid Potap
 * @version $Id: $
 */
@Named("wsCxfOutputPlugin")
public class WsCxfOutputPlugin implements IOutputPlugin {

	private static final Logger logger = LoggerFactory
			.getLogger(WsCxfOutputPlugin.class);

	private static final Logger operation_logger = LoggerFactory
			.getLogger("de.extra.client.operation");

	@Inject
	@Named("extraClientMTOMWS")
	private Extra extraClientMTOMWS;

	/**
	 * @throws ExtraFault
	 * @see de.extrastandard.api.plugin.IOutputPlugin#outputData(java.io.InputStream)
	 */
	@Override
	public ResponseTransport outputData(
			final RequestTransport transportRequestType) {

		logger.debug("sending request");
		try {
			final ResponseTransport transportResponseType = extraClientMTOMWS
					.execute(transportRequestType);
			logger.debug("receive response: " + transportResponseType);
			return transportResponseType;
		} catch (final ExtraFault extraFault) {
			logger.error("Fault:", extraFault);
			// Faengt eine vom Server gemeldete SoapFaultClientException ab
			operation_logger.error("Server meldet SOAP-Fehler: {}",
					extraFault.getFaultInfo());

			final ExtraOutputPluginRuntimeException extraOutputPluginRuntimeException = extractSoapFaultClientException(extraFault);
			throw (extraOutputPluginRuntimeException);
		}
	}

	private ExtraOutputPluginRuntimeException extractSoapFaultClientException(
			final ExtraFault extraFault) {

		final StringBuilder message = new StringBuilder();
		final String extraFaultMessage = extraFault.getMessage();
		message.append("ExtraFaultMessage: ").append(extraFaultMessage);
		final Error faultInfo = extraFault.getFaultInfo();
		final String reason = faultInfo.getReason();
		message.append(" Reason: ").append(reason);
		final ReportType report = faultInfo.getReport();
		final String highestWeight = report.getHighestWeight();
		message.append(" HighestWeight: ").append(highestWeight);
		final List<FlagType> flagList = report.getFlag();
		for (final FlagType flagType : flagList) {
			final FlagCodeType code = flagType.getCode();
			final TextType text = flagType.getText();
			if (code != null) {
				message.append(" FlagType.code: ").append(code.getValue());
			}
			if (text != null) {
				message.append(" FlagType.text: ").append(text.getValue());
			}

		}
		return new ExtraOutputPluginRuntimeException(
				ExceptionCode.EXTRA_TRANSFER_EXCEPTION, message.toString());

	}
}
