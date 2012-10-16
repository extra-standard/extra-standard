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
package de.extra.client.plugins.dataplugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import de.extra.client.core.model.inputdata.impl.FileInputData;
import de.extrastandard.api.exception.ExceptionCode;
import de.extrastandard.api.exception.ExtraDataPluginRuntimeException;
import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.plugin.IDataPlugin;

/**
 * @author DPRS
 * @version $Id$
 */
@Named("fileDataPlugin")
public class FileDataPlugin implements IDataPlugin {

	private static final Logger logger = LoggerFactory.getLogger(FileDataPlugin.class);

	@Value("${plugins.dataplugin.fileDataPlugin.inputVerzeichnis}")
	private File inputDirectory;

	@Value("${plugins.dataplugin.fileDataPlugin.inputDataLimit}")
	private Integer inputDataLimit;

	private Collection<File> inputFiles = new ArrayList<File>();

	private Iterator<File> inputFilesIterator;

	@Override
	public void initInputData() {
		inputFiles = FileUtils.listFiles(inputDirectory, TrueFileFilter.INSTANCE, null);
		inputFilesIterator = inputFiles.iterator();
		logger.info("FileData Plugin instanziiert for Directory: {}. Found {} files", inputDirectory, inputFiles.size());
	}

	@Override
	public boolean hasMoreData() {
		if (inputFiles == null) {
			throw new ExtraDataPluginRuntimeException(ExceptionCode.UNEXPECTED_INTERNAL_EXCEPTION,
					"InputData not instanziiert");
		}
		return inputFilesIterator.hasNext();
	}

	@Override
	public IInputDataContainer getData() {
		if (inputFiles == null) {
			throw new ExtraDataPluginRuntimeException(ExceptionCode.UNEXPECTED_INTERNAL_EXCEPTION,
					"InputData not instanziiert");
		}
		final FileInputData inputDataContainer = new FileInputData();
		for (Integer counter = 0; inputFilesIterator.hasNext() && counter < inputDataLimit; counter++) {
			final File inputFile = inputFilesIterator.next();
			inputDataContainer.addSingleInputData(inputFile);
		}
		return inputDataContainer;
	}

	@Override
	public boolean isEmpty() {
		if (inputFiles == null) {
			throw new ExtraDataPluginRuntimeException(ExceptionCode.UNEXPECTED_INTERNAL_EXCEPTION,
					"InputData not instanziiert");
		}
		return inputFiles.isEmpty();
	}

	/**
	 * @param inputDataLimit
	 *            the inputDataLimit to set
	 */
	public void setInputDataLimit(final Integer inputDataLimit) {
		this.inputDataLimit = inputDataLimit;
	}

}