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

import de.drv.dsrv.extrastandard.namespace.components.ElementSequenceType;
import de.drv.dsrv.extrastandard.namespace.messages.ListOfConfirmationOfReceipt;
import de.drv.dsrv.extrastandard.namespace.messages.ListOfConfirmationOfReceipt.ConfirmationOfReceipt;
import de.drv.dsrv.extrastandard.namespace.messages.PropertySet;
import de.drv.dsrv.extrastandard.namespace.messages.Value;
import de.extra.client.core.builder.impl.XmlComplexTypeBuilderAbstr;
import de.extra.client.core.model.inputdata.impl.DbQueryInputDataContainer;
import de.extrastandard.api.model.content.IDbQueryInputData;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;
import de.extrastandard.api.model.content.IInputDataContainer;

/**
 * @author Leonid Potap
 * 
 */
@Named()
public class TransportBodyRequestListOfConfirmationOfReceiptSequenceBuilder
		extends XmlComplexTypeBuilderAbstr {

	/**
	 * TODO Constanten fehlen?
	 */
	private static final String PROPERTY_NAME_RESPONSE_ID = "http://www.extra-standard.de/property/ResponseID";

	private static final String VERSION_OF_LISTOF_CONFIRMATIONOFRECEIPT = "1.1";

	private final static Logger LOG = LoggerFactory
			.getLogger(TransportBodyRequestListOfConfirmationOfReceiptSequenceBuilder.class);

	private static final String BUILDER_XML_MESSAGE_TYPE = "xcpt:ElementSequence";

	@Override
	public Object buildXmlFragment(final IInputDataContainer senderData,
			final IExtraProfileConfiguration config) {
		LOG.debug("xcpt:ElementSequence aufbauen");
		// TODO DataRequest anders aufbauen. Der sollte über die Nutzdaten zu
		// ziehen sein.
		final ConfirmationOfReceipt confirmationOfReceipt = createConfirmationOfReceipt(senderData);
		final ListOfConfirmationOfReceipt listOfConfirmationOfReceipt = new ListOfConfirmationOfReceipt();
		listOfConfirmationOfReceipt
				.setVersion(VERSION_OF_LISTOF_CONFIRMATIONOFRECEIPT);
		// Die Methoden der Klasse
		// TransportBodyRequestConfirmationOfReceiptSequenceBuilder können
		// leider nicht verwendet werden.
		listOfConfirmationOfReceipt.getConfirmationOfReceipt().add(
				confirmationOfReceipt);

		final ElementSequenceType elementSequence = new ElementSequenceType();
		elementSequence.getAny().add(listOfConfirmationOfReceipt);
		return elementSequence;
	}

	private ConfirmationOfReceipt createConfirmationOfReceipt(
			final IInputDataContainer senderData) {
		final DbQueryInputDataContainer dbQueryInputData = senderData
				.cast(DbQueryInputDataContainer.class);

		final List<IDbQueryInputData> singleDBQueryinputDataList = dbQueryInputData
				.getInputData();
		final ConfirmationOfReceipt confirmationOfReceipt = new ConfirmationOfReceipt();
		final PropertySet valuePropertySet = new PropertySet();
		valuePropertySet.setName(PROPERTY_NAME_RESPONSE_ID);
		final List<Value> valueList = valuePropertySet.getValue();
		confirmationOfReceipt.setPropertySet(valuePropertySet);
		for (final IDbQueryInputData singleDBQueryInputData : singleDBQueryinputDataList) {
			final Value value = new Value();
			value.setValue(singleDBQueryInputData.getSourceResponceId());
			valueList.add(value);
		}
		return confirmationOfReceipt;
	}

	@Override
	public String getXmlType() {
		return BUILDER_XML_MESSAGE_TYPE;
	}

}
