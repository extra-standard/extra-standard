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
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import de.extra.client.core.model.impl.DataSourcePluginDescription;
import de.extrastandard.api.exception.ExtraDataPluginRuntimeException;
import de.extrastandard.api.model.content.IInputDataPluginDescription;
import de.extrastandard.api.model.content.ISingleContentInputData;

/**
 * Identifiziert InputDaten aus der Filesystem
 * 
 * @author DPRS
 * @version $Id$
 */
public class SingleFileInputData extends Implementor implements
		ISingleContentInputData {

	private static final Charset ENCODING_UTF_8 = Charsets.UTF_8;

	private static final String INPUT_DATA_TYPE = "FILE_INPUT_DATA";
	private final File inputDataFile;

	private String requestId;

	private List<IInputDataPluginDescription> plugins = new ArrayList<IInputDataPluginDescription>();

	/**
	 * @param inputDataFile
	 */
	public SingleFileInputData(final File inputDataFile) {
		this.inputDataFile = inputDataFile;
		final IInputDataPluginDescription fileInputDataDescriptor = new DataSourcePluginDescription(
				inputDataFile, ENCODING_UTF_8.name());
		plugins.add(fileInputDataDescriptor);
	}

	public SingleFileInputData(final File inputDataFile,
			final List<IInputDataPluginDescription> plugins) {
		this.inputDataFile = inputDataFile;
		this.plugins = plugins;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra.client.core.model.ISenderDataBean#getPlugins()
	 */
	@Override
	public List<IInputDataPluginDescription> getPlugins() {
		return plugins;
	}

	/**
	 * @param plugins
	 *            the plugins to set
	 */
	public void addPlugin(final IInputDataPluginDescription plugin) {
		plugins.add(plugin);
	}

	public void setPlugins(final List<IInputDataPluginDescription> pluginListe) {
		this.plugins = pluginListe;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra.client.core.model.IFileInputdata#getFileName()
	 */
	public String getFileName() {
		return inputDataFile.getName();
	}

	@Override
	public InputStream getInputDataAsStream() {
		try {
			// TODO wie schliesse bzw. lasse ich den Stream schliessen
			return FileUtils.openInputStream(inputDataFile);
		} catch (final IOException ioException) {
			throw new ExtraDataPluginRuntimeException(ioException);
		}
	}

	@Override
	public String getInputDataAsString() {
		return getInputDataAsString(ENCODING_UTF_8);
	}

	@Override
	public String getInputDataAsString(final Charset encoding) {
		try {
			return FileUtils.readFileToString(inputDataFile, encoding);
		} catch (final IOException ioException) {
			throw new ExtraDataPluginRuntimeException(ioException);
		}
	}

	@Override
	public byte[] getInputDataAsByteArray() {
		InputStream in = null;
		try {
			// Angepasst. Statt FileUtils.readFileToByteArray prevent
			// outOfMemory
			// FileUtils.readFileToByteArray(inputDataFile); OutOfMemory bei
			// sehr großen Dateien
			in = FileUtils.openInputStream(inputDataFile);
			return IOUtils.toByteArray(in);
		} catch (final IOException ioException) {
			throw new ExtraDataPluginRuntimeException(ioException);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SingleFileInputData [");
		if (getFileName() != null) {
			builder.append("fileName=");
			builder.append(getFileName());
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	public void setRequestId(final String requestId) {
		this.requestId = requestId;

	}

	/**
	 * @return the requestId
	 */
	@Override
	public String getRequestId() {
		return requestId;
	}

	@Override
	public String getHashCode() {
		String checkSumCRC32String = null;
		try {
			final long checkSumCRC32 = FileUtils.checksumCRC32(inputDataFile);
			checkSumCRC32String = String.valueOf(checkSumCRC32);
		} catch (final IOException ioException) {
			throw new ExtraDataPluginRuntimeException(ioException);
		}

		return checkSumCRC32String;
	}

	@Override
	public String getInputIdentifier() {
		try {
			return inputDataFile.getCanonicalPath();
		} catch (final IOException ioException) {
			throw new ExtraDataPluginRuntimeException(ioException);
		}
	}

	@Override
	public String getInputDataType() {
		return INPUT_DATA_TYPE;
	}

	@Override
	public DataSource getInputDataAsDataSource() {
		return new FileDataSource(this.inputDataFile);
	}

}
