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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.extrastandard.api.model.content.IDbQueryInputData;
import de.extrastandard.api.model.content.ISingleQueryInputData;

/**
 * Identifiziert QueryInputDaten aus der DB
 * 
 * @author DPRS
 * @version $Id$
 */
public class DBQueryInputData extends InputDataContainer implements IDbQueryInputData {

	private final List<ISingleQueryInputData> inDBQueryInputDataList = new ArrayList<ISingleQueryInputData>();

	@Override
	public List<ISingleQueryInputData> getInputData() {
		return Collections.unmodifiableList(inDBQueryInputDataList);
	}

	/**
	 * @param sourceRequestId
	 * @param requestId
	 * @param serverResponceId
	 */
	public DBQueryInputData() {
		super();
	}

	/**
	 * @param sourceRequestId
	 * @param requestId
	 * @param serverResponceId
	 */
	public DBQueryInputData(final Long dbInputDataId, final String sourceRequestId, final String sourceResponceId) {
		super();
		addSingleDBQueryInputData(dbInputDataId, sourceRequestId, sourceResponceId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extrastandard.api.model.content.IDbQueryInputData#
	 * addSingleDBQueryInputData(java.lang.String, java.lang.String)
	 */
	@Override
	public void addSingleDBQueryInputData(final Long dbInputDataId, final String sourceRequestId,
			final String sourceResponceId) {
		final InDBQueryInputData singleDBQueryInputData = new InDBQueryInputData(dbInputDataId, sourceRequestId,
				sourceResponceId);
		addSingleDBQueryInputData(singleDBQueryInputData);
	}

	/**
	 * @param singleQueryInputData
	 */
	@Override
	public void addSingleDBQueryInputData(final ISingleQueryInputData singleQueryInputData) {
		inDBQueryInputDataList.add(singleQueryInputData);
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
		if (inDBQueryInputDataList != null) {
			builder.append("QueryInputDataList=");
			builder.append(inDBQueryInputDataList);
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	public boolean isContentEmpty() {
		return inDBQueryInputDataList.isEmpty();
	}

	@Override
	public int getContentSize() {
		return inDBQueryInputDataList.size();
	}

}
