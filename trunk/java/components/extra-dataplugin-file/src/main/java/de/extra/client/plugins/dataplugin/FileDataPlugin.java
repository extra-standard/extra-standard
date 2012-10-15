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
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import de.extra.client.core.model.inputdata.impl.FileInputData;
import de.extra.client.core.model.inputdata.impl.InputDataContainer;
import de.extra.client.plugins.dataplugin.helper.DataPluginHelper;
import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.observer.ITransportObserver;
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

	@Inject
	@Named("dataPluginHelper")
	private DataPluginHelper dataPluginHelper;

	@Inject
	@Named("transportObserver")
	private ITransportObserver transportObserver;

	@Override
	public IInputDataContainer getData() {
		final Collection<File> inputFiles = FileUtils.listFiles(inputDirectory, TrueFileFilter.INSTANCE, null);
		final InputDataContainer inputDataContainer = new FileInputData(inputFiles);
		logger.info("FileDataPlugin finisched for Directory: {}. Found {} files", inputDirectory, inputFiles.size());
		return inputDataContainer;

	}

}
