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

import de.drv.dsrv.extrastandard.namespace.messages.DataRequest;

/**
 * Bean für die Nutzdaten.
 */
public class SenderDataBean {

	private String requestId;

	private InputStream inputData;

	private List<PlugindatenBean> plugins;

	private DataRequest dataRequest;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public List<PlugindatenBean> getPlugins() {
		return plugins;
	}

	public void setPlugins(List<PlugindatenBean> plugins) {
		this.plugins = plugins;
	}

	public DataRequest getDataRequest() {
		return dataRequest;
	}

	public void setDataRequest(DataRequest dataRequest) {
		this.dataRequest = dataRequest;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SenderDataBean [");
		if (requestId != null)
			builder.append("requestId=").append(requestId).append(", ");
		if (plugins != null)
			builder.append("plugins=").append(plugins).append(", ");
		if (dataRequest != null)
			builder.append("dataRequest=").append(dataRequest);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * @return the inputData
	 */
	public InputStream getInputData() {
		return inputData;
	}

	/**
	 * @param inputData
	 *            the inputData to set
	 */
	public void setInputData(InputStream inputData) {
		this.inputData = inputData;
	}

}
