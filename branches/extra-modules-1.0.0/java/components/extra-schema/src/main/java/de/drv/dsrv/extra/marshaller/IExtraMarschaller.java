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

import javax.xml.transform.Result;

import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;

/**
 * @author Leonid Potap
 * @version $Id$
 */
public interface IExtraMarschaller extends Marshaller {

	/**
	 * Marshals the object graph with the given root into the provided
	 * {@link Result}.
	 * 
	 * @param graph
	 *            the root of the object graph to marshal
	 * @param result
	 *            the result to marshal to
	 * @param validation
	 *            , If validation true, the result is validated
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws XmlMappingException
	 *             if the given object cannot be marshalled to the result
	 */
	void marshal(Object graph, Result result, boolean validation)
			throws IOException, XmlMappingException;

}