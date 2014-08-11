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
package de.extra.client.core.model.impl;

import de.extrastandard.api.model.content.IInputDataPluginDescription;

public class CompressionPluginDescription implements
		IInputDataPluginDescription {

	/**
	 * CompressionAlgorithmId
	 */
	private String compAlgoId;
	/**
	 * CompressionAlogrithmVersion
	 */
	private String compAlgoVers;
	/**
	 * CompressionAlgorithmName
	 */
	private String compAlgoName;
	/**
	 * CompressionSpecifictaionURL
	 */
	private String compSpecUrl;
	/**
	 * CompressionSpecificationName
	 */
	private String compSpecName;
	/**
	 * CompressionSpecificationVersion
	 */
	private String compSpecVers;
	/**
	 * Dateigr��e vor der Kompression
	 */
	private int compInput;
	/**
	 * Dateigr��e nach der Kompression
	 */
	private int compOutput;
	/**
	 * Verarbeitungsschritt
	 */
	private int order;

	public String getCompAlgoId() {
		return compAlgoId;
	}

	public void setCompAlgoId(String compAlgoId) {
		this.compAlgoId = compAlgoId;
	}

	public String getCompAlgoName() {
		return compAlgoName;
	}

	public void setCompAlgoName(String compAlgoName) {
		this.compAlgoName = compAlgoName;
	}

	public String getCompAlgoVers() {
		return compAlgoVers;
	}

	public void setCompAlgoVers(String compAlgoVers) {
		this.compAlgoVers = compAlgoVers;
	}

	public int getCompInput() {
		return compInput;
	}

	public void setCompInput(int compInput) {
		this.compInput = compInput;
	}

	public int getCompOutput() {
		return compOutput;
	}

	public void setCompOutput(int compOutput) {
		this.compOutput = compOutput;
	}

	public String getCompSpecName() {
		return compSpecName;
	}

	public void setCompSpecName(String compSpecName) {
		this.compSpecName = compSpecName;
	}

	public String getCompSpecUrl() {
		return compSpecUrl;
	}

	public void setCompSpecUrl(String compSpecUrl) {
		this.compSpecUrl = compSpecUrl;
	}

	public String getCompSpecVers() {
		return compSpecVers;
	}

	public void setCompSpecVers(String compSpecVers) {
		this.compSpecVers = compSpecVers;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
