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
package de.drv.dsrv.extra.marshaller.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.Assert;

import de.drv.dsrv.extra.marshaller.IExtraUnmarschaller;
import de.drv.dsrv.extrastandard.namespace.response.Transport;

/**
 * @author Leonid Potap
 * @version $Id$
 */
@Named("extraUnmarschaller")
public class ExtraUnmarschaller implements IExtraUnmarschaller {

	private static final Logger logger = LoggerFactory
			.getLogger(ExtraUnmarschaller.class);
	@Inject
	@Named("eXTrajaxb2Marshaller")
	private Jaxb2Marshaller validationJaxb2Marshaller;

	@Inject
	@Named("eXTraNoValidationjaxb2Marshaller")
	private Jaxb2Marshaller noValidationJaxb2Marshaller;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.drv.dsrv.extra.marshaller.IExtraUnmarschaller#unmarschall(java.io.
	 * InputStream, java.lang.Class)
	 */
	@Override
	public <X> X unmarshal(final InputStream inputStream,
			final Class<X> extraTransportClass) throws XmlMappingException,
			IOException {
		return unmarshal(inputStream, extraTransportClass, false);
	}

	@Override
	public <X> X unmarshal(final InputStream inputStream,
			final Class<X> extraTransportClass, final boolean validation)
			throws XmlMappingException, IOException {
		Assert.notNull(inputStream, "InputStream is null");
		Assert.notNull(extraTransportClass, "ExtraTransportClass is null");

		final Unmarshaller unmarshaller = findUnmarschaller(validation);
		final Object responseObject = unmarshaller.unmarshal(new StreamSource(
				inputStream));
		logger.debug("ResponseObject Class: {}", responseObject.getClass());
		Assert.notNull(responseObject, "Response is null");
		X extraTransport = null;
		if (Transport.class.isAssignableFrom(responseObject.getClass())) {
			extraTransport = extraTransportClass.cast(responseObject);
		} else if (JAXBElement.class
				.isAssignableFrom(responseObject.getClass())) {
			// TODO Wie funktioniert es besser?
			@SuppressWarnings("rawtypes")
			final JAXBElement jaxbElementResponse = JAXBElement.class
					.cast(responseObject);
			final Object jaxBElementValue = jaxbElementResponse.getValue();

			Assert.isAssignable(extraTransportClass,
					jaxBElementValue.getClass(),
					"JaxBElement.value  can not be converted to the response.Transport");
			extraTransport = extraTransportClass.cast(jaxBElementValue);
		} else {
			throw new IllegalArgumentException(
					"Response can not be converted to the response.Transport. ResponseObjectClass: "
							+ responseObject.getClass());
		}
		return extraTransport;
	}

	private Jaxb2Marshaller findUnmarschaller(final boolean validation) {
		Jaxb2Marshaller marshaller = null;
		if (validation) {
			marshaller = validationJaxb2Marshaller;
		} else {
			marshaller = noValidationJaxb2Marshaller;
		}
		return marshaller;
	}
}
