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

public class DataObjektBean {
	
	private byte[] data;
	private boolean query;
	private String queryId;
	private String queryProcedure;
	private String queryDataType;
	
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public boolean isQuery() {
		return query;
	}
	public void setQuery(boolean query) {
		this.query = query;
	}
	public String getQueryId() {
		return queryId;
	}
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	public String getQueryProcedure() {
		return queryProcedure;
	}
	public void setQueryProcedure(String queryProcedure) {
		this.queryProcedure = queryProcedure;
	}
	public String getQueryDataType() {
		return queryDataType;
	}
	public void setQueryDataType(String queryDataType) {
		this.queryDataType = queryDataType;
	}
	
	

}
