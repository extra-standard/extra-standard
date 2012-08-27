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
package de.extra.client.core.model;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import de.drv.dsrv.extrastandard.namespace.plugins.AbstractPlugInType;
import de.drv.dsrv.extrastandard.namespace.plugins.DataContainerType;
import de.drv.dsrv.extrastandard.namespace.plugins.DataSource;
import de.extrastandard.api.model.IInputDataPluginDescription;

public class DataSourcePluginHelper extends PluginHelperBase {

	@Override
	public AbstractPlugInType getPluginElement(IInputDataPluginDescription plugindatenBean) {
		DataSourcePluginDescription dataSourcePluginBean = (DataSourcePluginDescription) plugindatenBean;

		DataSource dataSource = new DataSource();
		DataContainerType dataContainer = new DataContainerType();

		// Fuellen des DataContainers

		dataContainer.setEncoding(dataSourcePluginBean.getDsEncoding());
		dataContainer.setCreated(formatCreateDate(dataSourcePluginBean
				.getDsCreated()));
		dataContainer.setType(dataSourcePluginBean.getDsType());
		dataContainer.setName(dataSourcePluginBean.getDsName());

		dataSource.setDataContainer(dataContainer);

		return dataSource;
	}

	private XMLGregorianCalendar formatCreateDate(Date datum) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(datum);
		XMLGregorianCalendar datumXML = null;
		try {
			datumXML = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return datumXML;
	}
}
