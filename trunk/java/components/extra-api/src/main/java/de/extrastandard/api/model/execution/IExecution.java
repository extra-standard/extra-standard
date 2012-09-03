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
package de.extrastandard.api.model.execution;

import java.util.Date;

/**
 * Ein Execution bietet Möglichkeiten zur Protokollierung der zu verarbeitenden
 * Eingangsdaten.
 *
 * @author Thorsten Vogel
 * @version $Id$
 */
public interface IExecution extends PersistentEntity {

	/**
	 * Parameter für diese Execution.
	 *
	 * @return Parameter dieser Execution
	 */
	String getParameters();

	/**
	 * Zeitpunkt, an dem diese Execution gestartet wurde.
	 *
	 * @return Startzeitpunkt.
	 */
	Date getStartTime();

	/**
	 * Name des Verfahrens.
	 *
	 * @return Verfahren
	 */
	IProcedure getProcedure();

	/**
	 * Zeitpunkt, an dem diese Execution beendet wurde.
	 *
	 * @return Endzeitpunkt, oder <code>null</code> falls noch nicht beendet.
	 */
	Date getEndTime();

	/**
	 * @return
	 */
	//Set<IInputData> getInputDataSet();

	/**
	 * Momentaner Status dieser Execution.
	 *
	 * @return Status
	 */
	IStatus getStatus();

	/**
	 * Persistiert eine neue {@link IInputData} Instanz.
	 *
	 * @param inputIdentifier Identifikation der Eingabedaten
	 * @param hashCode Hashcode der Eingabedaten
	 * @return Instanz
	 */
	IInputData startInputData(String inputIdentifier, String hashCode);

	/**
	 * Beendet diese Execution.
	 *
	 * @param status Der Status, mit dem die Execution beendet werden soll.
	 */
	void endExecution(IStatus status);

}
