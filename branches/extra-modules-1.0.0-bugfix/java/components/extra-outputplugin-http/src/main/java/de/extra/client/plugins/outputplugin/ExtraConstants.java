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
package de.extra.client.plugins.outputplugin;

public class ExtraConstants {

	public static String BASE_DIR = null;

	public static final boolean DISABLE_CONSOLE_OUTPUT = false;

	public static final boolean RESPONSE_STATUS_OK = true;

	public static final String RESPONSE_OK_CODE = "200-";

	public static final boolean RESPONSE_STATUS_FAILURE = false;

	public static final String RESPONSE_FAILURE_CODE = "500-";

	public static final String REQUEST_PARAM_NAME = "extraRequest";

	// public static final String LINESEPARATOR =
	// System.getProperty("line.separator");
	public static final String LINESEPARATOR = "\r\n";

	// Kann beim Init des Clients �berschrieben werden !!
	public static String REQ_ENCODING = "ISO8859_1";

	public static final String SUBDIR_BIN = "BIN";

	public static final String SUBDIR_CONFIG = "CONFIG";

	public static final String SUBDIR_REQUEST = "SENDEN";

	public static final String SUBDIR_PROCESSED = "VERSENDET";

	public static final String SUBDIR_RESPONSE = "EMPFANGEN";

	public static final String SUBDIR_LOGGING = "LOGGING";

	public static final String MSG_PART_PREFIX = "TEILE-";

	public static final String MSG_RESPONSE_PREFIX = "RESPONSE-";

	public static final String CANONICALIZATION_METHOD = "none";

	// Unmarshalling der Response

	public static final String UNMARSHALL_RESPONSE = "de.drv.dsrv.extrastandard.namespace.response:de.drv.dsrv.extrastandard.namespace.components:de.drv.dsrv.extrastandard.namespace.messages";

}
