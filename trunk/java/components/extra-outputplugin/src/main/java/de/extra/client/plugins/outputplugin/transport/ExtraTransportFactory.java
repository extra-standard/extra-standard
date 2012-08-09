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
package de.extra.client.plugins.outputplugin.transport;

import javax.inject.Named;

import de.extra.client.core.annotation.PluginConfigType;
import de.extra.client.core.annotation.PluginConfigutation;
import de.extra.client.core.annotation.PluginValue;
import de.extra.client.plugins.outputplugin.config.HttpOutputPluginConnectConfiguration;

@Named("extraTransportFactory")
@PluginConfigutation(plugInBeanName="httpOutputPlugin", plugInType = PluginConfigType.OutputPlugins)
public class ExtraTransportFactory {
	
	@PluginValue(key="implClassName")
	private String implClassName;

	public void setImplClassName(String implClassName) {
		this.implClassName = implClassName;
	}

	/**
	 * 
	 * @param ecd
	 * @return an instance implementing the interface IExtraTransport
	 */
	public IExtraTransport loadTransportImpl() {
		IExtraTransport implClass = null;
		try {


			implClass = (IExtraTransport) Class.forName(implClassName)
					.newInstance();
		} catch (Throwable e) {
			e.printStackTrace();

		} finally {
			if (implClass == null) {
				try {
					// Fallback to default implementation
					implClass = new ExtraTransportHttp();

				} catch (RuntimeException e) {

					throw e;
				}
			}
		}
		return implClass;
	}
}