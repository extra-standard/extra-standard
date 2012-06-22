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
package de.extra.client.plugins.queryplugin.helper;

import java.math.BigInteger;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import de.drv.dsrv.extrastandard.namespace.messages.Control;
import de.drv.dsrv.extrastandard.namespace.messages.DataRequest;
import de.drv.dsrv.extrastandard.namespace.messages.DataRequestArgument;
import de.drv.dsrv.extrastandard.namespace.messages.Operand;
import de.drv.dsrv.extrastandard.namespace.messages.Query;

public class QueryHelper {

	/**
	 * Hilfsklasse zum Aufbau des DataRequests
	 * 
	 * @param startId
	 *            StartID aus der Property-Datei
	 * @param packageLimit
	 *            Maximale Anzahl der Pakete aus der Property-Datei
	 * @return DataRequest mit der Query
	 */
	public DataRequest createQuery(String startId, String packageLimit) {
		DataRequest dataRequest = new DataRequest();

		Control controlElement = new Control();
		Query query = new Query();
		DataRequestArgument dataRequestArgument = new DataRequestArgument();
		Operand operand = new Operand();
		operand.setValue(startId);

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
		controlElement.setMaximumPackages(new BigInteger(packageLimit));

		dataRequest.setQuery(query);
		dataRequest.setControl(controlElement);

		return dataRequest;
	}
}
