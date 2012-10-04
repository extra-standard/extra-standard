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
package de.extra.client.core.model.inputdata.impl;

import de.extrastandard.api.model.content.IDbQueryInputData;

/**
 * Identifiziert QueryInputDaten aus der DB
 * 
 * @author DPRS
 * @version $Id$
 */
public class DBQueryInputData extends InputDataContainer implements IDbQueryInputData {

	String dbInputDataId;

	String serverResponceId;

	/**
	 * @param dbInputDataId
	 * @param requestId
	 * @param serverResponceId
	 */
	public DBQueryInputData(final String dbInputDataId, final String requestId, final String serverResponceId) {
		super();
		super.setRequestId(requestId);
		this.dbInputDataId = dbInputDataId;
		this.serverResponceId = serverResponceId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra.client.core.model.IDbQueryInputData#getDbInputDataId()
	 */
	@Override
	public String getDbInputDataId() {
		return dbInputDataId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra.client.core.model.IDbQueryInputData#getServerResponceId()
	 */
	@Override
	public String getServerResponceId() {
		return serverResponceId;
	}

	@Override
	public String getInputIdentification() {
		return dbInputDataId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("DBQueryInputData [");
		if (getRequestId() != null) {
			builder.append("requestId=");
			builder.append(getRequestId());
			builder.append(", ");
		}
		if (dbInputDataId != null) {
			builder.append("dbInputDataId=");
			builder.append(dbInputDataId);
			builder.append(", ");
		}
		if (serverResponceId != null) {
			builder.append("serverResponceId=");
			builder.append(serverResponceId);
		}
		builder.append("]");
		return builder.toString();
	}
}
