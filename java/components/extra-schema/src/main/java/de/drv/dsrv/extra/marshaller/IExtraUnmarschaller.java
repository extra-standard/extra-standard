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

import java.io.IOException;
import java.io.InputStream;

import org.springframework.oxm.XmlMappingException;

/**
 * @author Leonid Potap
 * @version $Id$
 */
public interface IExtraUnmarschaller {

	/**
	 * Aus dem InputStream wird ein ExtraTransport Class ermittelt und
	 * unmarschalled. Es findet keine Validierung statt
	 * 
	 * @param inputStream
	 * @param extraTransportClass
	 *            das erwartete extraRootElement.Class
	 * @return the instance of extraTransportClass
	 * @throws XmlMappingException
	 * @throws IOException
	 */
	public <X> X unmarshal(InputStream inputStream, Class<X> extraTransportClass)
			throws XmlMappingException, IOException;

	/**
	 * From an InputStream create an object corresponding to the specified
	 * extraTransportClass. Checks if the input stream contains the specified
	 * Object
	 * 
	 * @param inputStream
	 * @param extraTransportClass
	 *            das erwartete extraRootElement.Class
	 * @param validation
	 *            , wenn validation true, inputStream wird beim marschallen
	 *            validiert
	 * @return the instance of extraTransportClass
	 * @throws XmlMappingException
	 * @throws IOException
	 */
	public <X> X unmarshal(InputStream inputStream,
			Class<X> extraTransportClass, boolean validation)
			throws XmlMappingException, IOException;

}