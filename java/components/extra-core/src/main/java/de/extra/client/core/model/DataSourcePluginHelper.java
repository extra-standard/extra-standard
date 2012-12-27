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

import java.util.GregorianCalendar;

import de.drv.dsrv.extrastandard.namespace.plugins.AbstractPlugInType;
import de.drv.dsrv.extrastandard.namespace.plugins.DataContainerType;
import de.drv.dsrv.extrastandard.namespace.plugins.DataSource;
import de.extra.client.core.model.impl.DataSourcePluginDescription;
import de.extrastandard.api.model.content.IInputDataPluginDescription;

public class DataSourcePluginHelper extends PluginHelperBase {

	@Override
	public AbstractPlugInType getPluginElement(
			final IInputDataPluginDescription plugindatenBean) {
		final DataSourcePluginDescription dataSourcePluginBean = (DataSourcePluginDescription) plugindatenBean;

		final DataSource dataSource = new DataSource();
		final DataContainerType dataContainer = new DataContainerType();

		// Fuellen des DataContainers

		dataContainer.setEncoding(dataSourcePluginBean.getDsEncoding());
		dataContainer.setCreated(new GregorianCalendar());
		dataContainer.setType(dataSourcePluginBean.getDsType());
		dataContainer.setName(dataSourcePluginBean.getDsName());

		dataSource.setDataContainer(dataContainer);

		return dataSource;
	}

	// private XMLGregorianCalendar formatCreateDate(final Date datum) {
	// final GregorianCalendar c = new GregorianCalendar();
	// c.setTime(datum);
	// XMLGregorianCalendar datumXML = null;
	// try {
	// datumXML = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	// } catch (final DatatypeConfigurationException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return datumXML;
	// }
}
