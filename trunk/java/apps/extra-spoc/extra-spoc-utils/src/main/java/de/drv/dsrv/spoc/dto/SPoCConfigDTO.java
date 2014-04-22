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
package de.drv.dsrv.spoc.dto;

public class SPoCConfigDTO {

	private final String extraProcedure;
	private final String extraDatatype;
	private final String extraProfile;
	private final String extraVersion;
	private final String startUrl;

	public SPoCConfigDTO(final String extraProcedure,
			final String extraDatatype, final String extraProfile,
			final String extraVersion, final String startUrl) {
		super();
		this.extraProcedure = extraProcedure;
		this.extraDatatype = extraDatatype;
		this.extraProfile = extraProfile;
		this.extraVersion = extraVersion;
		this.startUrl = startUrl;
	}

	public String getProcedure() {
		return extraProcedure;
	}

	public String getDatatype() {
		return extraDatatype;
	}

	public String getProfile() {
		return extraProfile;
	}

	public String getStartUrl() {
		return startUrl;
	}

	public String getVersion() {
		return extraVersion;
	}
}
