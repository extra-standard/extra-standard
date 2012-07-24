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

public class ConfigFileBean {

	/**
	 * Bean welche die einzelnen Konfigurationsinformationen vorh�lt
	 */
	// Testindicator
	private boolean testIndicator;

	// Art der Absenderadresse
	private String absClass;
	// Absenderadresse
	private String absBbnr;
	// Absendername
	private String absName;

	// Art der Empfängeradresse
	private String empfClass;
	// Empfängeradresse
	private String empfBbnr;
	// Empfängername
	private String empfName;

	// Produktname
	private String productName;
	// Hersteller
	private String productManuf;

	// Procedure
	private String procedure;
	// DataType
	private String dataType;
	// Szenario
	private String scenario;
	// Paketebene vorhanden
	private boolean packageLayer = false;
	// Nachrichtenebene vorhanden
	private boolean messageLayer = false;
	// Art der Nutzdaten
	private String contentType;

	// RequestID
	private String requestId;

	public String getAbsBbnr() {
		return absBbnr;
	}

	public void setAbsBbnr(String absBbnr) {
		this.absBbnr = absBbnr;
	}

	public String getAbsName() {
		return absName;
	}

	public void setAbsName(String absName) {
		this.absName = absName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getEmpfBbnr() {
		return empfBbnr;
	}

	public void setEmpfBbnr(String empfBbnr) {
		this.empfBbnr = empfBbnr;
	}

	public String getEmpfName() {
		return empfName;
	}

	public void setEmpfName(String empfName) {
		this.empfName = empfName;
	}

	public boolean isMessageLayer() {
		return messageLayer;
	}

	public void setMessageLayer(boolean messageLayer) {
		this.messageLayer = messageLayer;
	}

	public boolean isPackageLayer() {
		return packageLayer;
	}

	public void setPackageLayer(boolean packageLayer) {
		this.packageLayer = packageLayer;
	}

	public String getProcedure() {
		return procedure;
	}

	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	public String getProductManuf() {
		return productManuf;
	}

	public void setProductManuf(String productManuf) {
		this.productManuf = productManuf;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public boolean isTestIndicator() {
		return testIndicator;
	}

	public void setTestIndicator(boolean testIndicator) {
		this.testIndicator = testIndicator;
	}

	public String getAbsClass() {
		return absClass;
	}

	public void setAbsClass(String absClass) {
		this.absClass = absClass;
	}

	public String getEmpfClass() {
		return empfClass;
	}

	public void setEmpfClass(String empfClass) {
		this.empfClass = empfClass;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
}
