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

import de.extrastandard.api.model.content.ISingleQueryInputData;

/**
 * Identifiziert QueryInputDaten aus der DB
 * 
 * @author DPRS
 * @version $Id$
 */
public class InDBQueryInputData implements ISingleQueryInputData {

	/**
	 * Ursprung RequestId der Anfrage
	 */
	String originRequestId;

	/**
	 * zurückgegebene Server ResponseId
	 */
	String serverResponceId;

	/**
	 * requestId der Nachfrage
	 */
	String requestId;

	/**
	 * @param originRequestId
	 * @param serverResponceId
	 */
	public InDBQueryInputData(final String originRequestId, final String serverResponceId) {
		super();
		this.originRequestId = originRequestId;
		this.serverResponceId = serverResponceId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra.client.core.model.IDbQueryInputData#getDbInputDataId()
	 */
	@Override
	public String getOriginRequestId() {
		return originRequestId;
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





	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InDBQueryInputData [originRequestId=");
		builder.append(originRequestId);
		builder.append(", serverResponceId=");
		builder.append(serverResponceId);
		builder.append(", requestId=");
		builder.append(requestId);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public void setRequestId(final String requestId) {
		this.requestId = requestId;

	}

	@Override
	public String getRequestId() {
		return requestId;
	}

}
