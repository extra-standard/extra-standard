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

/**
 * Configuration DataBean beinhaltet die profilierungs Informationen  
 *
 */
public class ConfigFileBean {

	/**
	 * Bean welche die einzelnen Konfigurationsinformationen vorhält
	 */

	// Paketebene vorhanden
	private boolean packageLayer = false;
	// Nachrichtenebene vorhanden
	private boolean messageLayer = false;
//	 Art der Nutzdaten
	private String contentType;

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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
}
