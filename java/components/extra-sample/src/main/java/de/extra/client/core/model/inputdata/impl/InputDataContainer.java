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
package de.extra.client.core.model.inputdata.impl;

import java.util.List;

import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.model.content.IInputDataPluginDescription;

/**
 * Bean für die Nutzdaten.
 */
public abstract class InputDataContainer implements IInputDataContainer {

	private List<IInputDataPluginDescription> plugins;

	private String requestId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra.client.core.model.ISenderDataBean#getPlugins()
	 */
	@Override
	public List<IInputDataPluginDescription> getPlugins() {
		return plugins;
	}

	/**
	 * casten übergebene Klasse Klasse ist.
	 */
	@Override
	public <X> X cast(final Class<X> iface) {
		if (iface == null) {
			throw new IllegalArgumentException("Parameter 'cls' muss angegeben werden");
		}
		if (iface.isAssignableFrom(this.getClass())) {
			return iface.cast(this);
		}
		throw new ClassCastException("Klasse " + this.getClass() + " kann nicht nach " + iface + " gewandelt werden");
	}

	@Override
	/**
	 * prueft, ob uebergebene Klasse <code>assignable</code> von aktueller
	 * Klasse ist.
	 */
	public <X> boolean isImplementationOf(final Class<X> cls) {
		return cls == null ? false : cls.isAssignableFrom(this.getClass());
	}

	/**
	 * @param plugins
	 *            the plugins to set
	 */
	public void setPlugins(final List<IInputDataPluginDescription> plugins) {
		this.plugins = plugins;
	}

	/**
	 * @return the requestId
	 */
	@Override
	public String getRequestId() {
		return requestId;
	}

	/**
	 * @param requestId
	 *            the requestId to set
	 */
	public void setRequestId(final String requestId) {
		this.requestId = requestId;
	}

}
