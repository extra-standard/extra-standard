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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.extrastandard.api.model.content.IFileInputData;
import de.extrastandard.api.model.content.ISingleContentInputData;

/**
 * Identifiziert InputDaten aus der Filesystem
 * 
 * @author DPRS
 * @version $Id$
 */
public class FileInputData extends InputDataContainer implements IFileInputData {

	public FileInputData() {
	}

	private final List<ISingleContentInputData> inputDataList = new ArrayList<ISingleContentInputData>();

	public FileInputData(final Collection<File> inputFiles) {
		for (final File inputFile : inputFiles) {
			addSingleInputData(inputFile);
		}
	}

	public FileInputData(final List<String> inputContents) {
		for (final String inputContent : inputContents) {
			addSingleInputData(inputContent);
		}
	}

	public void addSingleInputData(final ISingleContentInputData singleInputData) {
		inputDataList.add(singleInputData);
	}

	/**
	 * This method is used to test purposes, the input data setted as a string
	 * 
	 * @param inputContent
	 * @return
	 */
	private boolean addSingleInputData(final String inputContent) {
		final SingleStringInputData singleFileInputData = new SingleStringInputData(inputContent);
		return inputDataList.add(singleFileInputData);
	}

	@Override
	public List<ISingleContentInputData> getInputData() {
		return Collections.unmodifiableList(inputDataList);
	}

	/**
	 * Adds a new input file to the input data
	 * 
	 * @param inputFile
	 * @return
	 */
	public boolean addSingleInputData(final File inputFile) {
		final SingleFileInputData singleFileInputData = new SingleFileInputData(inputFile);
		return inputDataList.add(singleFileInputData);
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
		if (inputDataList != null) {
			builder.append("inputDataList=");
			builder.append(inputDataList);
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	public boolean isContentEmpty() {
		return this.inputDataList.isEmpty();
	}

	@Override
	public int getContentSize() {
		return this.inputDataList.size();
	}

}
