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

import javax.inject.Named;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.drv.dsrv.extrastandard.namespace.components.Base64CharSequenceType;
import de.extra.client.core.builder.impl.XmlComplexTypeBuilderAbstr;
import de.extrastandard.api.exception.ExtraCoreRuntimeException;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;
import de.extrastandard.api.model.content.IFileInputdata;
import de.extrastandard.api.model.content.IInputDataContainer;

/**
 * IFileInputData expect. Stores inputdata in Base64CharSequenceType.value
 * 
 * @author Leonid Potap
 * @version $Id$
 */
@Named("transportBodyFileInputBase64CharSequenceBuilder")
public class TransportBodyFileInputBase64CharSequenceBuilder extends XmlComplexTypeBuilderAbstr {

	private static final Logger LOG = LoggerFactory.getLogger(TransportBodyFileInputBase64CharSequenceBuilder.class);

	private static final String BUILDER_XML_MESSAGE_TYPE = "xcpt:Base64CharSequence";

	@Override
	public Object buildXmlFragment(final IInputDataContainer senderData, final IExtraProfileConfiguration config) {
		LOG.debug("Base64CharSequenceType aufbauen");
		final Base64CharSequenceType base64CharSequence = new Base64CharSequenceType();
		final IFileInputdata iFileInputdata = senderData.cast(IFileInputdata.class);
		final Base64InputStream base64InputStream = new Base64InputStream(iFileInputdata.getInputData());
		byte[] encodeData = null;
		try {
			encodeData = IOUtils.toByteArray(base64InputStream);
		} catch (final IOException ioException) {
			throw new ExtraCoreRuntimeException(ioException);
		}
		base64CharSequence.setValue(encodeData);

		return base64CharSequence;
	}

	@Override
	public String getXmlType() {
		return BUILDER_XML_MESSAGE_TYPE;
	}

}
