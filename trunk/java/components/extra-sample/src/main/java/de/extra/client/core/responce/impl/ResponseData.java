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
package de.extra.client.core.responce.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.model.content.ISingleResponseData;

/**
 * Daten eines Responses
 * 
 * @author Leonid Potap
 * @version $Id$
 */
public class ResponseData implements IResponseData {

	private final Map<String, ISingleResponseData> responseDatenMap = new HashMap<String, ISingleResponseData>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extrastandard.api.model.content.IResponseData#getReponses()
	 */
	@Override
	public Collection<ISingleResponseData> getResponses() {
		return responseDatenMap.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.extrastandard.api.model.content.IResponseData#getResponse(java.lang
	 * .String)
	 */
	@Override
	public ISingleResponseData getResponse(final String reguestId) {
		return responseDatenMap.get(reguestId);
	}

	/**
	 * Fügt einen neuen Datensatz zu den Responses
	 * 
	 * @param singleResponseData
	 */
	@Override
	public void addSingleResponse(final ISingleResponseData singleResponseData) {
		responseDatenMap.put(singleResponseData.getRequestId(),
				singleResponseData);
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
		final Collection<ISingleResponseData> responses = getResponses();
		if (responses != null && !responses.isEmpty()) {
			for (final ISingleResponseData singleResponseData : responses) {
				builder.append(" SingleResponseData: ").append(
						singleResponseData);
			}
		}
		builder.append("]");
		return builder.toString();
	}

}
