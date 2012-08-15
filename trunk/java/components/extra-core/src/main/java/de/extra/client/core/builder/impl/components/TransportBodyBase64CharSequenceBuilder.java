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
package de.extra.client.core.builder.impl.components;

import javax.inject.Named;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import de.drv.dsrv.extrastandard.namespace.components.Base64CharSequenceType;
import de.extra.client.core.builder.impl.XmlComplexTypeBuilderAbstr;
import de.extra.client.core.model.ConfigFileBean;
import de.extra.client.core.model.SenderDataBean;

/**
 * @author Leonid Potap
 * 
 */
@Named("transportBodyBase64CharSequenceBuilder")
public class TransportBodyBase64CharSequenceBuilder extends
		XmlComplexTypeBuilderAbstr {

	private static Logger logger = Logger
			.getLogger(TransportBodyBase64CharSequenceBuilder.class);

	private static final String BUILDER_XML_MESSAGE_TYPE = "xcpt:Base64CharSequence";

	@Override
	public Object buildXmlFragment(SenderDataBean senderData,
			ConfigFileBean config) {
		logger.debug("Base64CharSequenceType aufbauen");
		Base64CharSequenceType base64CharSequence = new Base64CharSequenceType();
		byte[] encodeData = new Base64().encode(senderData.getNutzdaten());
		base64CharSequence.setValue(encodeData);
		return base64CharSequence;
	}

	@Override
	public String getXmlType() {
		return BUILDER_XML_MESSAGE_TYPE;
	}

}
