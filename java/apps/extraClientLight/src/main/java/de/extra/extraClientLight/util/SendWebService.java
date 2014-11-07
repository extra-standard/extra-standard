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

package de.extra.extraClientLight.util;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.ws.soap.SOAPFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.request.TransportRequestType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.response.TransportResponseType;
import de.extra.extraClientLight.helper.ExtraErrorHelper;
import de.extra.extraClientLight.webservice.Extra;
import de.extra.extraClientLight.webservice.ExtraFault;
import de.extra.extraClientLight.webservice.Extra_Service;


public class SendWebService {
	private Logger LOGGER = LoggerFactory.getLogger(SendWebService.class);

	private static final QName SERVICE_NAME = new QName(
			"https://www.eservicet-drv.de/SPoC", "execute");

	public TransportResponseType sendRequest(TransportRequestType extraRequest,
			String url, boolean mtomActive) {
		TransportResponseType response = new TransportResponseType();

		Extra_Service extraService = new Extra_Service(null, SERVICE_NAME);

		Extra extraPort = extraService.getPort(Extra.class);
		BindingProvider bp = (BindingProvider) extraPort;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				url);
		
		SOAPBinding soapBinding =  (SOAPBinding) bp.getBinding();
		soapBinding.setMTOMEnabled(false);

		try {

			response = extraPort.execute(extraRequest);
		} catch (SOAPFaultException e) {
			SOAPFault soapFault = e.getFault();

			LOGGER.error(soapFault.getTextContent(),e);

		} catch (ExtraFault e) {
			ExtraErrorHelper.printExtraError(e);
		}

		return response;

	}

}
