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

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import de.drv.dsrv.extrastandard.namespace.components.CharSequenceType;
import de.extra.client.core.builder.impl.XmlComplexTypeBuilderAbstr;
import de.extrastandard.api.exception.ExtraCoreRuntimeException;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;
import de.extrastandard.api.model.content.IInputDataContainer;

/**
 * Initiale Umsetzung.
 * 
 * @author DSRV
 * 
 */
@Named("transportBodyCharSequenceBuilder")
public class TransportBodyCharSequenceBuilder extends XmlComplexTypeBuilderAbstr {

	private static Logger logger = LoggerFactory.getLogger(TransportBodyCharSequenceBuilder.class);

	private static final String BUILDER_XML_MESSAGE_TYPE = "xcpt:CharSequence";

	@Override
	public Object buildXmlFragment(final IInputDataContainer senderData, final IExtraProfileConfiguration config) {
		try {
			logger.debug("CharSequenceType aufbauen");
			final CharSequenceType charSequence = new CharSequenceType();

			final InputStream inputData = senderData.getInputData();

			Assert.notNull(inputData, "InputData is null.");

			final String inpurDataString = IOUtils.toString(inputData);

			charSequence.setValue(inpurDataString);

			return charSequence;

		} catch (final IOException ioException) {
			throw new ExtraCoreRuntimeException(ioException);
		}
	}

	@Override
	public String getXmlType() {
		return BUILDER_XML_MESSAGE_TYPE;
	}

}
