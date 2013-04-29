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

import java.util.List;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import de.drv.dsrv.extrastandard.namespace.components.CharSequenceType;
import de.extra.client.core.builder.impl.XmlComplexTypeBuilderAbstr;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;
import de.extrastandard.api.model.content.IContentInputDataContainer;
import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.model.content.ISingleContentInputData;

/**
 * Initiale Umsetzung.
 * 
 * @author DSRV
 * 
 */
@Named("transportBodyCharSequenceBuilder")
public class TransportBodyCharSequenceBuilder extends
		XmlComplexTypeBuilderAbstr {

	private static Logger logger = LoggerFactory
			.getLogger(TransportBodyCharSequenceBuilder.class);

	private static final String BUILDER_XML_MESSAGE_TYPE = "xcpt:CharSequence";

	@Override
	public Object buildXmlFragment(final IInputDataContainer senderData,
			final IExtraProfileConfiguration config) {
		logger.debug("CharSequenceType aufbauen");
		final CharSequenceType charSequence = new CharSequenceType();
		final IContentInputDataContainer fileInputdata = senderData
				.cast(IContentInputDataContainer.class);
		final List<ISingleContentInputData> inputDataList = fileInputdata
				.getInputData();
		// Es kann nicht in Transport mehrere Datensätze übertragen werden!!
		Assert.isTrue(inputDataList.size() != 1, "Unexpected InputData size.");
		final ISingleContentInputData singleFileInputData = inputDataList
				.get(0);
		final String inpurDataString = singleFileInputData
				.getInputDataAsString();

		charSequence.setValue(inpurDataString);

		return charSequence;
	}

	@Override
	public String getXmlType() {
		return BUILDER_XML_MESSAGE_TYPE;
	}

}
