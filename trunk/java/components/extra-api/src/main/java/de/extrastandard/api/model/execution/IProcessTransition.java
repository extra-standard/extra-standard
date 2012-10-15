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
 * Bildet den Übergang zwischen zwei Status einer {@link IExecution}-Instanz ab.
 * 
 * @author Thorsten Vogel
 * @version $Id: IProcessTransition.java 487 2012-09-03 13:07:52Z
 *          thorstenvogel@gmail.com $
 */
public interface IProcessTransition extends PersistentEntity {

	/**
	 * Aktueller Status.
	 * 
	 * @return Momentaner Status
	 */
	IStatus getCurrentStatus();

	/**
	 * Vorheriger Status.
	 * 
	 * @return Vorheriger Status
	 */
	IStatus getPreviousStatus();

	/**
	 * Zeitpunkt der letzten Statusänderung.
	 * 
	 * @return Zeitpunkt dieser Transition
	 */
	Date getTransitionDate();

	/**
	 * Dauer der Transition.
	 * 
	 * @return Dauer der Transisition in Millisekunden.
	 */
	Long getDuration();

	/**
	 * Bezogene Instanz.
	 * 
	 * @return Inputdaten, auf die sich diese Transition bezieht.
	 */
	IExecution getExecuton();

}
