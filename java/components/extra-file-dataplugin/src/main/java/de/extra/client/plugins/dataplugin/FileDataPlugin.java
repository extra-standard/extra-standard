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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import de.extra.client.core.model.InputDataContainer;
import de.extra.client.plugins.dataplugin.auftragssatz.AuftragssatzType;
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

	private static final Logger LOG = Logger.getLogger(FileDataPlugin.class);

	// @Inject
	// @Named("dataPluginController")
	// private IDataPluginController dataPluginController;

	@Value("${plugins.dataplugin.fileDataPlugin.inputVerzeichnis}")
	private File inputDirectory;

	@Inject
	@Named("dataPluginHelper")
	private DataPluginHelper dataPluginHelper;

	@Inject
	@Named("transportObserver")
	private ITransportObserver transportObserver;

	@Override
	public Iterator<IInputDataContainer> getData() {
		final Iterator<File> iterator = FileUtils.iterateFiles(inputDirectory, TrueFileFilter.INSTANCE, null);
		return new Iterator<IInputDataContainer>() {

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public IInputDataContainer next() {
				final File inputFile = iterator.next();
				InputDataContainer inputDataContainer = new InputDataContainer();
				FileInputStream inputData;
				try {
					inputData = new FileInputStream(inputFile);
				} catch (final FileNotFoundException e) {
					// TODO exception reinbauen
					throw new IllegalStateException(e);
				}
				inputDataContainer.setInputData(inputData);
				final AuftragssatzType auftragssatz = new AuftragssatzType();
				auftragssatz.setRequestId(inputFile.getAbsolutePath());

				// Setzen der RequestId
				inputDataContainer.setRequestId(auftragssatz.getRequestId());
				inputDataContainer = dataPluginHelper.fuelleVersandatenBean(inputDataContainer, auftragssatz);

				transportObserver.requestDataReceived(inputFile.getName(), inputFile.length());
				return inputDataContainer;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
