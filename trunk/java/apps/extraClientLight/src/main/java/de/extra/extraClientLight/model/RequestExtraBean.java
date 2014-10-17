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

package de.extra.extraClientLight.model;

public class RequestExtraBean {

	private String verfahren;
	private String fachdienst;
	private String empfaenger;
	private String absender;
	private String fachschluessel;
	private String url;
	private boolean synchron;
	private boolean mtom;
	private DataObjektBean dataObjekt;
	private String profile;

	public String getVerfahren() {
		return verfahren;
	}

	public void setVerfahren(String verfahren) {
		this.verfahren = verfahren;
	}

	public String getFachdienst() {
		return fachdienst;
	}

	public void setFachdienst(String fachdienst) {
		this.fachdienst = fachdienst;
	}

	public String getEmpfaenger() {
		return empfaenger;
	}

	public void setEmpfaenger(String empfaenger) {
		this.empfaenger = empfaenger;
	}

	public String getAbsender() {
		return absender;
	}

	public void setAbsender(String absender) {
		this.absender = absender;
	}

	public String getFachschluessel() {
		return fachschluessel;
	}

	public void setFachschluessel(String fachschluessel) {
		this.fachschluessel = fachschluessel;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isSynchron() {
		return synchron;
	}

	public void setSynchron(boolean synchron) {
		this.synchron = synchron;
	}

	public boolean isMtom() {
		return mtom;
	}

	public void setMtom(boolean mtom) {
		this.mtom = mtom;
	}

	public DataObjektBean getDataObjekt() {
		return dataObjekt;
	}

	public void setDataObjekt(DataObjektBean dataObjekt) {
		this.dataObjekt = dataObjekt;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

}
