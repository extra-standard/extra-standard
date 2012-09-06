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
package de.extrastandard.api.model.content;

/**
 * @author Leonid Potap
 * @version $Id$
 */
public class ResponseData implements IResponseData {

	private final String requestId;

	private final String returnCode;

	private final String returnText;

	private final String responseId;

	/**
	 * @param requestId
	 * @param returnCode
	 * @param returnText
	 */
	public ResponseData(final String requestId, final String returnCode, final String returnText,
			final String responseId) {
		this.requestId = requestId;
		this.returnCode = returnCode;
		this.returnText = returnText;
		this.responseId = responseId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extrastandard.api.model.IResponseData#getRequestId()
	 */
	@Override
	public String getRequestId() {
		return requestId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extrastandard.api.model.IResponseData#getReturnCode()
	 */
	@Override
	public String getReturnCode() {
		return returnCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extrastandard.api.model.IResponseData#getReturnText()
	 */
	@Override
	public String getReturnText() {
		return returnText;
	}

	/**
	 * @return the responseId
	 */
	public String getResponseId() {
		return responseId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ResponseData [");
		if (requestId != null) {
			builder.append("requestId=");
			builder.append(requestId);
			builder.append(", ");
		}
		if (returnCode != null) {
			builder.append("returnCode=");
			builder.append(returnCode);
			builder.append(", ");
		}
		if (returnText != null) {
			builder.append("returnText=");
			builder.append(returnText);
			builder.append(", ");
		}
		if (responseId != null) {
			builder.append("responseId=");
			builder.append(responseId);
		}
		builder.append("]");
		return builder.toString();
	}

}
