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
package de.extra.client.core.model.impl;

import java.io.File;
import java.util.Date;

import de.extra.client.core.model.inputdata.InputDataContentType;
import de.extrastandard.api.model.content.IInputDataPluginDescription;

public class DataSourcePluginDescription implements IInputDataPluginDescription {

	// DataSource-Plugin
	private static final String DEFAULT_ENCODING = "UTF-8";

	// Datentyp
	private InputDataContentType type;
	// Dateiname
	private String name;
	// Erstellungsdatum
	private Date created;
	// Encoding
	private String encoding = null;

	public DataSourcePluginDescription() {
	}

	/**
	 * @param inputFile
	 */
	public DataSourcePluginDescription(final File inputFile,
			final String encoding) {
		name = inputFile.getName();
		final long lastModified = inputFile.lastModified();
		created = new Date(lastModified);
		type = InputDataContentType.FILE;
		this.encoding = encoding;
	}

	/**
	 * @param inputFile
	 */
	public DataSourcePluginDescription(final File inputFile) {
		this(inputFile, DEFAULT_ENCODING);
	}

	/**
	 * @return
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @return
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public InputDataContentType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(final InputDataContentType type) {
		this.type = type;
	}

	/**
	 * @param type
	 *            the type to set. Versucht type zu dem InputDataContentType
	 *            umzuwandeln
	 */
	public void setType(final String type) {
		this.type = InputDataContentType.valueOf(type);
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(final Date created) {
		this.created = created;
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(final String encoding) {
		this.encoding = encoding;
	}

}
