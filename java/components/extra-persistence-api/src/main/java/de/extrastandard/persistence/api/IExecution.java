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
package de.extrastandard.persistence.api;

/**
 * Ein Execution bietet Möglichkeiten zur Protokollierung der zu verarbeitenden
 * Eingangsdaten.
 *
 * @author DPRS
 * @version $Id$
 */
public interface IExecution {

	/**
	 * Beendet diese Execution.
	 *
	 * @param status
	 *            Der Status der Execution.
	 */
	void endExecution(IStatus status);

	/**
	 *
	 *
	 * @param type
	 * @param inputIdentifier
	 * @param hashCode
	 * @return
	 */
	IInputData startInputData(InputType type, String inputIdentifier,
			String hashCode);

}
