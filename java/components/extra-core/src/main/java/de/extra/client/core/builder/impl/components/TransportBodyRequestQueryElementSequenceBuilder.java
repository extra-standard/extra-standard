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
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.drv.dsrv.extrastandard.namespace.components.ElementSequenceType;
import de.drv.dsrv.extrastandard.namespace.messages.Control;
import de.drv.dsrv.extrastandard.namespace.messages.DataRequest;
import de.drv.dsrv.extrastandard.namespace.messages.DataRequestArgument;
import de.drv.dsrv.extrastandard.namespace.messages.DataRequestQuery;
import de.drv.dsrv.extrastandard.namespace.messages.Operand;
import de.drv.dsrv.extrastandard.namespace.messages.OperandSet;
import de.extra.client.core.builder.impl.XmlComplexTypeBuilderAbstr;
import de.extra.client.core.model.inputdata.impl.DBQueryInputData;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;
import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.model.content.ISingleQueryInputData;

/**
 * @author Leonid Potap
 * 
 */
@Named("transportBodyRequestQueryElementSequenceBuilder")
public class TransportBodyRequestQueryElementSequenceBuilder extends XmlComplexTypeBuilderAbstr {

	private final static Logger LOG = LoggerFactory.getLogger(TransportBodyRequestQueryElementSequenceBuilder.class);

	private static final String BUILDER_XML_MESSAGE_TYPE = "xcpt:ElementSequence";

	@Override
	public Object buildXmlFragment(final IInputDataContainer senderData, final IExtraProfileConfiguration config) {
		LOG.debug("xcpt:ElementSequence aufbauen");
		// TODO DataRequest anders aufbauen. Der sollte über die Nutzdaten zu
		// ziehen sein.
		final DataRequest dataRequest = createDataRequest(senderData);
		final ElementSequenceType elementSequence = new ElementSequenceType();
		elementSequence.getAny().add(dataRequest);
		return elementSequence;
	}

	private DataRequest createDataRequest(final IInputDataContainer senderData) {
		final DBQueryInputData dbQueryInputData = senderData.cast(DBQueryInputData.class);
		final DataRequest dataRequest = new DataRequest();

		final Control controlElement = new Control();
		final DataRequestQuery query = new DataRequestQuery();
		final DataRequestArgument dataRequestArgument = new DataRequestArgument();
		dataRequestArgument.setProperty("http://www.extra-standard.de/property/ResponseID");
		final OperandSet operandSet = new OperandSet();
		final QName qnameStringType = new QName("xs:string");
		dataRequestArgument.setType(qnameStringType);
		dataRequestArgument.setEvent("http://www.extra-standard.de/event/RequestData");
		// Setzen des Tags
		final JAXBElement<OperandSet> jaxbOperand = new JAXBElement<OperandSet>(new QName(
				"http://www.extra-standard.de/namespace/message/1", "IN"), OperandSet.class, operandSet);
		jaxbOperand.setValue(operandSet);
		// Setzen der Property
		dataRequestArgument.getContent().add(jaxbOperand);
		for (final ISingleQueryInputData singleQueryInputData : dbQueryInputData.getInputData()) {
			final Operand operand = new Operand();
			operand.setValue(singleQueryInputData.getServerResponceId());
			operandSet.getEQ().add(operand);
		}
		query.getArgument().add(dataRequestArgument);
		// Befüllen des Control-Arguments
		dataRequest.setQuery(query);
		dataRequest.setControl(controlElement);
		return dataRequest;
	}

	@Override
	public String getXmlType() {
		return BUILDER_XML_MESSAGE_TYPE;
	}

}
