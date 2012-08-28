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

import java.io.InputStream;
import java.util.List;

import de.extrastandard.api.exception.ExtraCoreRuntimeException;
import de.extrastandard.api.model.IInputDataContainer;
import de.extrastandard.api.model.IInputDataPluginDescription;

/**
 * Bean für die Nutzdaten.
 */
public class InputDataContainer implements IInputDataContainer {

	private String requestId;

	private InputStream inputData;

	private List<IInputDataPluginDescription> plugins;

	private String dataRequestId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra.client.core.model.ISenderDataBean#getRequestId()
	 */
	@Override
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(final String requestId) {
		this.requestId = requestId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra.client.core.model.ISenderDataBean#getPlugins()
	 */
	@Override
	public List<IInputDataPluginDescription> getPlugins() {
		return plugins;
	}

	public void setPlugins(final List<IInputDataPluginDescription> plugins) {
		this.plugins = plugins;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra.client.core.model.ISenderDataBean#getDataRequest()
	 */
	@Override
	public String getDataRequestId() {
		return dataRequestId;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SenderDataBean [");
		if (requestId != null)
			builder.append("requestId=").append(requestId).append(", ");
		if (plugins != null)
			builder.append("plugins=").append(plugins).append(", ");
		if (dataRequestId != null)
			builder.append("dataRequestId=").append(dataRequestId);
		builder.append("]");
		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra.client.core.model.ISenderDataBean#getInputData()
	 */
	@Override
	public InputStream getInputData() {
		return inputData;
	}

	/**
	 * @param inputData
	 *            the inputData to set
	 */
	public void setInputData(final InputStream inputData) {
		this.inputData = inputData;
	}

	public void setDataRequestId(final String startId) {
		this.dataRequestId = startId;

	}

	@Override
	public String getInputIdentification() {
		// Erste bilige Refactoring. Muss in 2 Klassen verteilt werden.
		String identification = null;
		if (requestId != null) {
			identification = requestId;
		} else if (dataRequestId != null) {
			identification = dataRequestId;
		} else {
			throw new ExtraCoreRuntimeException("Keine Nachrichtidentification vorhanden!!!!");
		}
		return identification;
	}

}
