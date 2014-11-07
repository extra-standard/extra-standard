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
package de.extra.extraClientLight.helper;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorType;
import de.extra.extraClientLight.webservice.ExtraFault;

public class ExtraErrorHelper {

	private static Logger LOGGER = LoggerFactory
			.getLogger(ExtraErrorHelper.class);

	public static void printExtraError(ExtraFault extraFault) {
		
		ExtraErrorType extraError = extraFault.getFaultInfo();

		try {
			JAXBContext jaxbContext = JAXBContext
					.newInstance(ExtraErrorType.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);

			Writer writer = new StringWriter();
			marshaller.marshal(extraError, writer);

			LOGGER.debug(writer.toString());

		} catch (JAXBException e) {

			LOGGER.error("Fehler beim Konvertieren", e);

		}

	}

}
