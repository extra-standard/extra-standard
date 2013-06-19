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
import de.extrastandard.api.model.content.ICriteriaQueryInputData;
import de.extrastandard.api.model.content.ICriteriaQueryInputDataContainer;
import de.extrastandard.api.model.content.IDbQueryInputData;
import de.extrastandard.api.model.content.IDbQueryInputDataContainer;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;
import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.model.content.ISingleInputData;

/**
 * @author Leonid Potap
 * 
 */
@Named("transportBodyRequestQueryElementSequenceBuilder")
public class TransportBodyRequestQueryElementSequenceBuilder extends
		XmlComplexTypeBuilderAbstr {

	private final static Logger LOG = LoggerFactory
			.getLogger(TransportBodyRequestQueryElementSequenceBuilder.class);

	private static final String BUILDER_XML_MESSAGE_TYPE = "xcpt:ElementSequence";

	private static final String DATA_REQUEST_VERSION = "1.2";

	@Override
	public Object buildXmlFragment(final IInputDataContainer senderData,
			final IExtraProfileConfiguration config) {
		LOG.debug("xcpt:ElementSequence aufbauen");

		// Unterscheidung des Operanden
		final DataRequest dataRequest;
		if (ICriteriaQueryInputDataContainer.class.isAssignableFrom(senderData
				.getClass())) {
			dataRequest = createSingleDataRequest(senderData);
		} else {
			// Default: IDbMultiQueryInputData
			dataRequest = createMultiDataRequest(senderData);
		}

		final ElementSequenceType elementSequence = new ElementSequenceType();
		elementSequence.getAny().add(dataRequest);
		return elementSequence;
	}

	private DataRequest createMultiDataRequest(
			final IInputDataContainer senderData) {
		final IDbQueryInputDataContainer dbQueryInputData = senderData
				.cast(IDbQueryInputDataContainer.class);
		final DataRequest dataRequest = new DataRequest();

		final Control controlElement = new Control();
		final DataRequestQuery query = new DataRequestQuery();
		final DataRequestArgument dataRequestArgument = new DataRequestArgument();
		dataRequestArgument
				.setProperty("http://www.extra-standard.de/property/ResponseID");
		final OperandSet operandSet = new OperandSet();
		dataRequestArgument
				.setEvent("http://www.extra-standard.de/event/RequestData");
		// Setzen des Tags
		final JAXBElement<OperandSet> jaxbOperand = new JAXBElement<OperandSet>(
				new QName("http://www.extra-standard.de/namespace/message/1",
						"IN"), OperandSet.class, operandSet);
		jaxbOperand.setValue(operandSet);
		// Setzen der Property
		dataRequestArgument.getContent().add(jaxbOperand);
		for (final IDbQueryInputData singleQueryInputData : dbQueryInputData
				.getInputData()) {
			final Operand operand = new Operand();
			operand.setValue(singleQueryInputData.getSourceResponceId());
			operandSet.getEQ().add(operand);
		}
		query.getArgument().add(dataRequestArgument);

		// Befüllen des Control-Arguments
		dataRequest.setQuery(query);
		dataRequest.setControl(controlElement);
		dataRequest.setVersion(DATA_REQUEST_VERSION);
		return dataRequest;
	}

	/**
	 * Erzeugt ein DataRequestArgument für den übergebenen Procedure-Namen. ('EQ
	 * procedureName')
	 * 
	 * @param procedureName
	 * @return
	 */
	private DataRequestArgument createDataRequestArgumentProcedure(
			final String procedureName) {
		final DataRequestArgument dataRequestArgument = new DataRequestArgument();
		dataRequestArgument
				.setProperty("http://www.extra-standard.de/property/Procedure");

		final Operand operand = new Operand();
		operand.setValue(procedureName);

		final String operandAsString = "EQ";
		final JAXBElement<Operand> jaxbOperand = new JAXBElement<Operand>(
				new QName("http://www.extra-standard.de/namespace/message/1",
						operandAsString), Operand.class, operand);
		jaxbOperand.setValue(operand);

		dataRequestArgument
				.setProperty("http://www.extra-standard.de/property/Procedure");
		dataRequestArgument.getContent().add(jaxbOperand);

		return dataRequestArgument;
	}

	/**
	 * Erzeugt ein DataRequestArgument für den übergebenen Subquery-Ausdruck.
	 * ('EQ subquery')
	 * 
	 * @param subquery
	 * @return
	 */
	private DataRequestArgument createDataRequestArgumentSubquery(
			final String subquery) {
		final DataRequestArgument dataRequestArgument = new DataRequestArgument();
		dataRequestArgument
				.setProperty("http://www.extra-standard.de/property/DataType");

		final Operand operand = new Operand();
		operand.setValue(subquery);

		final String operandAsString = "EQ";
		final JAXBElement<Operand> jaxbOperand = new JAXBElement<Operand>(
				new QName("http://www.extra-standard.de/namespace/message/1",
						operandAsString), Operand.class, operand);
		jaxbOperand.setValue(operand);

		dataRequestArgument.getContent().add(jaxbOperand);
		return dataRequestArgument;
	}

	private DataRequest createSingleDataRequest(
			final IInputDataContainer senderData) {
		final ICriteriaQueryInputDataContainer dbQueryInputData = senderData
				.cast(ICriteriaQueryInputDataContainer.class);
		final DataRequest dataRequest = new DataRequest();

		final Control controlElement = new Control();
		final DataRequestQuery query = new DataRequestQuery();
		final DataRequestArgument dataRequestArgument = new DataRequestArgument();

		String procedureName = null;
		String subquery = null;
		for (final ISingleInputData singleInputData : dbQueryInputData
				.getContent()) {
			if (ICriteriaQueryInputData.class.isAssignableFrom(singleInputData
					.getClass())) {
				final ICriteriaQueryInputData singleQueryInputData = ICriteriaQueryInputData.class
						.cast(singleInputData);

				procedureName = singleQueryInputData.getProcedureName();
				subquery = singleQueryInputData.getSubquery();

				final Operand operand = new Operand();
				operand.setValue(String.valueOf(singleQueryInputData
						.getArgument()));

				final String operandAsString = singleQueryInputData
						.getQueryArgumentType().getType();
				final JAXBElement<Operand> jaxbOperand = new JAXBElement<Operand>(
						new QName(
								"http://www.extra-standard.de/namespace/message/1",
								operandAsString), Operand.class, operand);
				jaxbOperand.setValue(operand);

				dataRequestArgument
						.setProperty("http://www.extra-standard.de/property/ResponseID");
				dataRequestArgument.getContent().add(jaxbOperand);
			}
		}

		query.getArgument().add(dataRequestArgument);
		if (procedureName != null) {
			query.getArgument().add(
					createDataRequestArgumentProcedure(procedureName));
		}
		if (subquery != null && subquery.length() > 0) {
			query.getArgument()
					.add(createDataRequestArgumentSubquery(subquery));
		}

		dataRequest.setQuery(query);
		dataRequest.setControl(controlElement);
		dataRequest.setVersion(DATA_REQUEST_VERSION);
		return dataRequest;
	}

	@Override
	public String getXmlType() {
		return BUILDER_XML_MESSAGE_TYPE;
	}

}
