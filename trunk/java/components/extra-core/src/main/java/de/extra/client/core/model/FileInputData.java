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

import java.io.InputStream;

import de.extrastandard.api.model.content.IFileInputdata;

/**
 * Identifiziert InputDaten aus der Filesystem
 * 
 * @author DPRS
 * @version $Id$
 */
public class FileInputData extends InputDataContainer implements IFileInputdata {

	private final InputStream inputData;

	private final String hashCode;

	private final String fileName;

	public FileInputData(final String fileName, final InputStream inputData, final String hashCode) {
		super();
		this.inputData = inputData;
		this.hashCode = hashCode;
		this.fileName = fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra.client.core.model.IFileInputdata#getInputData()
	 */
	@Override
	public InputStream getInputData() {
		return inputData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra.client.core.model.IFileInputdata#getHashCode()
	 */
	@Override
	public String getHashCode() {
		return hashCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra.client.core.model.IFileInputdata#getFileName()
	 */
	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public String getInputIdentification() {
		return fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("FileInputData [");
		if (hashCode != null) {
			builder.append("hashCode=");
			builder.append(hashCode);
			builder.append(", ");
		}
		if (fileName != null) {
			builder.append("fileName=");
			builder.append(fileName);
		}
		builder.append("]");
		return builder.toString();
	}

}
