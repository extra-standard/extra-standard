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
package de.extra.xtt.gui;

import java.io.File;
import java.io.FileFilter;

/**
 * FileFilter für *.xml und *.xsd-Dateien.
 * 
 * @author Beier
 */
public class XmlXsdFileFilter extends javax.swing.filechooser.FileFilter
		implements FileFilter {

	private final SUFFIX suffix;

	/**
	 * Konstruktor mit der Angabe der Endung f�r den Filter
	 * 
	 * @param suffix
	 *            Endung aus den vorgegebenen Typen
	 */
	public XmlXsdFileFilter(SUFFIX suffix) {
		this.suffix = suffix;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public boolean accept(File f) {
		return (f.isDirectory() || f.getName().toLowerCase()
				.endsWith(suffixStr(suffix)));
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public String getDescription() {
		String rvVal = "";
		switch (suffix) {
		case XML:
			rvVal = "XML-Dateien";
			break;

		case XSD:
			rvVal = "XSD-Dateien";
			break;
		}
		return rvVal;
	}

	/**
	 * M�gliche Dateitypen f�r den Filterprozess
	 * 
	 * @author Beier
	 * 
	 */
	public static enum SUFFIX {
		XML, XSD
	}

	private String suffixStr(SUFFIX suff) {
		String rvVal = "";
		switch (suff) {
		case XML:
			rvVal = ".xml";
			break;

		case XSD:
			rvVal = ".xsd";
			break;
		}

		return rvVal;
	}

}
