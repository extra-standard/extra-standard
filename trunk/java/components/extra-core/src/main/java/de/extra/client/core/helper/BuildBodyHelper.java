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
package de.extra.client.core.helper;

import java.io.IOException;
import java.math.BigInteger;

import javax.inject.Named;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import de.drv.dsrv.extrastandard.namespace.components.Base64CharSequenceType;
import de.drv.dsrv.extrastandard.namespace.components.DataType;
import de.drv.dsrv.extrastandard.namespace.components.ElementSequenceType;
import de.drv.dsrv.extrastandard.namespace.messages.Control;
import de.drv.dsrv.extrastandard.namespace.messages.DataRequest;
import de.drv.dsrv.extrastandard.namespace.messages.DataRequestArgument;
import de.drv.dsrv.extrastandard.namespace.messages.DataRequestQuery;
import de.drv.dsrv.extrastandard.namespace.messages.Operand;
import de.drv.dsrv.extrastandard.namespace.request.TransportBody;
import de.extrastandard.api.model.content.IDbQueryInputData;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;
import de.extrastandard.api.model.content.IFileInputdata;
import de.extrastandard.api.model.content.IInputDataContainer;

@Named("bodyHelper")
public class BuildBodyHelper {

	private static final Logger LOG = LoggerFactory.getLogger(BuildBodyHelper.class);

	@Value("${builder.xcpt.ElementSequencelocator.transportBodyElementSequenceBuilder.packageLimit}")
	private String packageLimit;

	/**
	 * Funktion zum Aufbau des Transportbodys.
	 * 
	 * @param configBean
	 *            ConfigFileBean mit den Informationen aus der Profildatei
	 * @param versanddatenBean
	 *            VersanddatenBean mit den Nutzdaten
	 * @return
	 */
	public TransportBody buildTransportBody(final IExtraProfileConfiguration configBean,
			final IInputDataContainer versanddatenBean) {
		LOG.debug("TransportBody aufbauen");

		final TransportBody transportBody = new TransportBody();
		LOG.debug("Baue TransportBody auf");
		if (configBean.getContentType().equalsIgnoreCase("xcpt:Base64CharSequence")) {
			final DataType data = new DataType();
			final Base64CharSequenceType base64CharSequence = new Base64CharSequenceType();

			/*
			 * base64CharSequence.setValue(new BASE64Encoder().encode(
			 * versanddatenBean.getNutzdaten()).getBytes());
			 */
			byte[] inputDataArray;
			try {
				final IFileInputdata fileInputdata = versanddatenBean.cast(IFileInputdata.class);
				inputDataArray = IOUtils.toByteArray(fileInputdata.getInputData());
			} catch (final IOException e) {
				// TODO exception einbauen
				throw new IllegalStateException(e);
			}
			base64CharSequence.setValue(inputDataArray);
			data.setBase64CharSequence(base64CharSequence);

			transportBody.setData(data);
		}

		if (configBean.getContentType().equalsIgnoreCase("xcpt:ElementSequence")) {
			final DataType data = new DataType();
			final ElementSequenceType elementSequence = new ElementSequenceType();

			final DataRequest dataRequest = createDataRequest(versanddatenBean);
			elementSequence.getAny().add(dataRequest);
			data.setElementSequence(elementSequence);
			transportBody.setData(data);
		}

		// TODO implementieren weiterer Möglichkeiten nach dem aktuellen
		// eXTraSchema

		return transportBody;
	}

	private DataRequest createDataRequest(final IInputDataContainer senderData) {
		final DataRequest dataRequest = new DataRequest();

		final Control controlElement = new Control();
		final DataRequestQuery query = new DataRequestQuery();
		final DataRequestArgument dataRequestArgument = new DataRequestArgument();
		final Operand operand = new Operand();
		final IDbQueryInputData dnQueryInputData = senderData.cast(IDbQueryInputData.class);
		operand.setValue(dnQueryInputData.getRequestId());

		// Setzen des Tags
		final QName qname = new QName("xs:string");
		final JAXBElement<Operand> jaxbOperand = new JAXBElement<Operand>(new QName(
				"http://www.extra-standard.de/namespace/message/1", "GE"), Operand.class, operand);
		jaxbOperand.setValue(operand);

		// Setzen der Property
		dataRequestArgument.setProperty("http://www.extra-standard.de/property/ResponseID");
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
