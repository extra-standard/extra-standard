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

    private static final String INPUT_DATA_TYPE = "DB_QUERY";

    Long sourceIdenrificationId;

    /**
     * Ursprung RequestId der Anfrage
     */
    String sourceRequestId;

    /**
     * zurückgegebene Server ResponseId
     */
    String sourceResponceId;

    /**
     * requestId der Nachfrage
     */
    String requestId;

    /**
     * @param sourceIdentificationId
     * @param sourceRequestId
     * @param sourceResponceId
     */
    public InDBQueryInputData(final Long sourceIdentificationId,
	    final String sourceRequestId, final String sourceResponceId) {
	super();
	this.sourceRequestId = sourceRequestId;
	this.sourceResponceId = sourceResponceId;
	this.sourceIdenrificationId = sourceIdentificationId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.extra.client.core.model.IDbQueryInputData#getDbInputDataId()
     */
    @Override
    public String getSourceRequestId() {
	return sourceRequestId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.extra.client.core.model.IDbQueryInputData#getServerResponceId()
     */
    @Override
    public String getSourceResponceId() {
	return sourceResponceId;
    }

    @Override
    public void setRequestId(final String requestId) {
	this.requestId = requestId;

    }

    @Override
    public String getRequestId() {
	return requestId;
    }

    /**
     * @return the sourceIdenrificationId
     */
    @Override
    public Long getSourceIdentificationId() {
	return sourceIdenrificationId;
    }

    @Override
    public String getInputIdentifier() {
	return requestId;
    }

    @Override
    public String getInputDataType() {
	return INPUT_DATA_TYPE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	final StringBuilder builder = new StringBuilder();
	builder.append("InDBQueryInputData [");
	if (sourceIdenrificationId != null) {
	    builder.append("sourceIdenrificationId=");
	    builder.append(sourceIdenrificationId);
	    builder.append(", ");
	}
	if (sourceRequestId != null) {
	    builder.append("sourceRequestId=");
	    builder.append(sourceRequestId);
	    builder.append(", ");
	}
	if (sourceResponceId != null) {
	    builder.append("sourceResponceId=");
	    builder.append(sourceResponceId);
	}
	builder.append("]");
	return builder.toString();
    }
}
