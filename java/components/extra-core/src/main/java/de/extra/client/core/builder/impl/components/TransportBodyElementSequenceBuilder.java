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

import java.math.BigInteger;

import javax.inject.Named;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import de.drv.dsrv.extrastandard.namespace.components.ElementSequenceType;
import de.drv.dsrv.extrastandard.namespace.messages.Control;
import de.drv.dsrv.extrastandard.namespace.messages.DataRequest;
import de.drv.dsrv.extrastandard.namespace.messages.DataRequestArgument;
import de.drv.dsrv.extrastandard.namespace.messages.DataRequestQuery;
import de.drv.dsrv.extrastandard.namespace.messages.Operand;
import de.extra.client.core.builder.impl.XmlComplexTypeBuilderAbstr;
import de.extra.client.core.model.IExtraProfileConfiguration;
import de.extra.client.core.model.IInputDataContainer;

/**
 * @author Leonid Potap
 * 
 */
@Named("transportBodyElementSequenceBuilder")
public class TransportBodyElementSequenceBuilder extends
		XmlComplexTypeBuilderAbstr {

	private static Logger logger = Logger
			.getLogger(TransportBodyElementSequenceBuilder.class);

	private static final String BUILDER_XML_MESSAGE_TYPE = "xcpt:ElementSequence";

	@Value("${builder.xcpt.ElementSequencelocator.transportBodyElementSequenceBuilder.packageLimit}")
	private String packageLimit;

	@Override
	public Object buildXmlFragment(IInputDataContainer senderData,
			IExtraProfileConfiguration config) {
		logger.debug("xcpt:ElementSequence aufbauen");
		// TODO DataRequest anders aufbauen. Der sollte über die Nutzdaten zu
		// ziehen sein.
		DataRequest dataRequest = createDataRequest(senderData);
		ElementSequenceType elementSequence = new ElementSequenceType();
		elementSequence.getAny().add(dataRequest);
		return elementSequence;
	}

	private DataRequest createDataRequest(IInputDataContainer senderData) {
		DataRequest dataRequest = new DataRequest();

		Control controlElement = new Control();
		DataRequestQuery query = new DataRequestQuery();
		DataRequestArgument dataRequestArgument = new DataRequestArgument();
		Operand operand = new Operand();
		operand.setValue(senderData.getDataRequestId());

		// Setzen des Tags
		QName qname = new QName("xs:string");
		JAXBElement<Operand> jaxbOperand = new JAXBElement<Operand>(new QName(
				"http://www.extra-standard.de/namespace/message/1", "GE"),
				Operand.class, operand);
		jaxbOperand.setValue(operand);

		// Setzen der Property
		dataRequestArgument
				.setProperty("http://www.extra-standard.de/property/ResponseID");
		dataRequestArgument.setType(qname);
		dataRequestArgument.getContent().add(jaxbOperand);

		query.getArgument().add(dataRequestArgument);

		// Befüllen des Control-Arguments
		if (packageLimit != null) {
			controlElement.setMaximumPackages(new BigInteger(packageLimit));
		}

		dataRequest.setQuery(query);
		dataRequest.setControl(controlElement);

		return dataRequest;
	}

	@Override
	public String getXmlType() {
		return BUILDER_XML_MESSAGE_TYPE;
	}

}
