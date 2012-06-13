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

public class EncryptionPluginBean extends PlugindatenBean {

	private String encAlgoId;
	private String encAlgoVers;
	private String encAlgoName;
	private String encSpecUrl;
	private String encSpecName;
	private String encSpecVers;
	private int encInput;
	private int encOutput;
	private int order;

	public String getEncAlgoId() {
		return encAlgoId;
	}

	public void setEncAlgoId(String encAlgoId) {
		this.encAlgoId = encAlgoId;
	}

	public String getEncAlgoName() {
		return encAlgoName;
	}

	public void setEncAlgoName(String encAlgoName) {
		this.encAlgoName = encAlgoName;
	}

	public String getEncAlgoVers() {
		return encAlgoVers;
	}

	public void setEncAlgoVers(String encAlgoVers) {
		this.encAlgoVers = encAlgoVers;
	}

	public int getEncInput() {
		return encInput;
	}

	public void setEncInput(int encInput) {
		this.encInput = encInput;
	}

	public int getEncOutput() {
		return encOutput;
	}

	public void setEncOutput(int encOutput) {
		this.encOutput = encOutput;
	}

	public String getEncSpecName() {
		return encSpecName;
	}

	public void setEncSpecName(String encSpecName) {
		this.encSpecName = encSpecName;
	}

	public String getEncSpecUrl() {
		return encSpecUrl;
	}

	public void setEncSpecUrl(String encSpecUrl) {
		this.encSpecUrl = encSpecUrl;
	}

	public String getEncSpecVers() {
		return encSpecVers;
	}

	public void setEncSpecVers(String encSpecVers) {
		this.encSpecVers = encSpecVers;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
