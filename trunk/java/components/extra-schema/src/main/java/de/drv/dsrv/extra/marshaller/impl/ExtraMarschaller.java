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

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.Result;

import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import de.drv.dsrv.extra.marshaller.IExtraMarschaller;

/**
 * Leitet Aufrufe an den vorkonfigurierte Marschaller. Beachten
 * Validierungsmerkmal.
 * 
 * @author Leonid Potap
 * @version $Id$
 */
@Named("extraMarschaller")
public class ExtraMarschaller implements IExtraMarschaller {

	@Inject
	@Named("eXTrajaxb2Marshaller")
	private Jaxb2Marshaller validationJaxb2Marshaller;

	@Inject
	@Named("eXTraNoValidationjaxb2Marshaller")
	private Jaxb2Marshaller noValidationJaxb2Marshaller;

	@Override
	public boolean supports(final Class<?> clazz) {
		return noValidationJaxb2Marshaller.supports(clazz);
	}

	@Override
	public void marshal(final Object graph, final Result result)
			throws IOException, XmlMappingException {
		marshal(graph, result, false);
	}

	@Override
	public void marshal(final Object graph, final Result result,
			final boolean validation) throws IOException, XmlMappingException {
		final Marshaller marshaller = getMarschaller(validation);
		marshaller.marshal(graph, result);
	}

	/**
	 * Liefert Marschaller mit oder ohen Validation je nach param
	 * 
	 * @param validation
	 *            , wenn true, wird Marschllar mit validierung zurückgegeben
	 * @return
	 */
	public Marshaller getMarschaller(final boolean validation) {
		Marshaller marshaller = null;
		if (validation) {
			marshaller = validationJaxb2Marshaller;
		} else {
			marshaller = noValidationJaxb2Marshaller;
		}
		return marshaller;
	}
}
