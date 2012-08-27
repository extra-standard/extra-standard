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
package de.extra.client.core.builder.impl.request;

import javax.inject.Named;

import org.apache.log4j.Logger;

import de.drv.dsrv.extrastandard.namespace.request.TransportBody;
import de.extra.client.core.builder.impl.XmlComplexTypeBuilderAbstr;
import de.extrastandard.api.model.IExtraProfileConfiguration;
import de.extrastandard.api.model.IInputDataContainer;

/**
 * @author Leonid Potap
 * 
 */
@Named("requestTransportBodyBuilder")
public class RequestTransportBodyBuilder extends XmlComplexTypeBuilderAbstr {

	private static Logger logger = Logger
			.getLogger(RequestTransportBodyBuilder.class);

	private static final String BUILDER_XML_MESSAGE_TYPE = "req:TransportBody";

	@Override
	public Object buildXmlFragment(IInputDataContainer senderData,
			IExtraProfileConfiguration config) {
		logger.debug("TransportBody aufbauen");

		TransportBody transportBody = new TransportBody();
		return transportBody;
	}

	@Override
	public String getXmlType() {
		return BUILDER_XML_MESSAGE_TYPE;
	}

}
