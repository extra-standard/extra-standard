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

import java.util.List;

import de.drv.dsrv.extrastandard.namespace.messages.DataRequest;

public class VersanddatenBean {

	/**
	 * Bean fuer die Nutzdaten
	 */
	private String requestId;

	private byte[] nutzdaten;

	private List<PlugindatenBean> plugins;

	private DataRequest dataRequest;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public byte[] getNutzdaten() {
		return nutzdaten;
	}

	public void setNutzdaten(byte[] nutzdaten) {
		this.nutzdaten = nutzdaten;
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
}
