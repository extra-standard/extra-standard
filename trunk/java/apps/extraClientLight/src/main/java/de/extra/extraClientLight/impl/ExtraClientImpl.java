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

package de.extra.extraClientLight.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.request.TransportRequestType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.response.TransportResponseType;
import de.extra.extraClientLight.IextraClient;
import de.extra.extraClientLight.helper.BuildExtraTransport;
import de.extra.extraClientLight.helper.ExtraRequestHelper;
import de.extra.extraClientLight.helper.ExtraResponseHelper;
import de.extra.extraClientLight.model.RequestExtraBean;
import de.extra.extraClientLight.model.ResponseExtraBean;
import de.extra.extraClientLight.util.RequestBeanValidator;
import de.extra.extraClientLight.util.SendWebService;


public class ExtraClientImpl implements IextraClient {
	private Logger LOGGER = LoggerFactory.getLogger(ExtraClientImpl.class);

	public ResponseExtraBean sendExtra(RequestExtraBean requestExtra) {
		LOGGER.info("Client aufgerufen");
		ResponseExtraBean responseBean = new ResponseExtraBean();
		int returnCode = 99;

		if(!RequestBeanValidator.validateRequestBean(requestExtra)){
			
			returnCode = 1;
			
		}else{
		
		SendWebService sendWebService = new SendWebService();
		TransportRequestType extraRequest = BuildExtraTransport
				.buildTransportRequest(requestExtra);

		ExtraRequestHelper.printRequest(extraRequest);

		TransportResponseType extraResponse = sendWebService.sendRequest(extraRequest, requestExtra.getUrl(),
				requestExtra.isMtom());
		responseBean = ExtraResponseHelper.convertExtraResponse(extraResponse);
		}
		responseBean.setReturnCode(returnCode);
		
		return responseBean;
	}

}
