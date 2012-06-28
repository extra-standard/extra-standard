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
package de.drv.dsrv.extra.marshaller;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class ExtraJaxb2Marshaller extends Jaxb2Marshaller {

	private NamespacePrefixMapper namespacePrefixMapper;

	@Override
	protected void initJaxbMarshaller(Marshaller marshaller)
			throws JAXBException {
		super.initJaxbMarshaller(marshaller);

		if (namespacePrefixMapper != null) {
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
					namespacePrefixMapper);
		}
	}

	public void setNamespacePrefixMapper(
			NamespacePrefixMapper namespacePrefixMapper) {
		this.namespacePrefixMapper = namespacePrefixMapper;
	}
}
