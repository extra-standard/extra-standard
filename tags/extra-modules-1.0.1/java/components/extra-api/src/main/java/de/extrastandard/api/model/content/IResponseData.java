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
package de.extrastandard.api.model.content;

import java.util.Collection;

/**
 * Provides results back to a request / transmission.
 * 
 * @author Leonid Potap
 * @version $Id: IResponseData.java 1072 2012-11-14 07:14:08Z
 *          werner.rentenservice@gmail.com $
 */
public interface IResponseData {

	/**
	 * 
	 * @return all results contained in the response
	 */
	Collection<ISingleResponseData> getResponses();

	/**
	 * returns an {@link SingleResponseData} specified by reguestId
	 * 
	 * @param requestId
	 * @return
	 */
	Collection<ISingleResponseData> getResponse(String requestId);

	/**
	 * Add a {@link SingleResponseData} to ResponseData
	 * 
	 * @param singleResponseData
	 */
	void addSingleResponse(ISingleResponseData singleResponseData);

	/**
	 * Zeigt an, ob die Server-Verarbeitung erfolgreich war.
	 * 
	 * @return
	 */
	Boolean isSuccessful();
	
	/**
	 * Zeigt an, ob eine Warnung aufgetreten ist (z.B. keine Ergebnisdatei erhalten).
	 * 
	 * @return
	 */
	Boolean isWarning();
	
	/**
	 * Wird benoetigt, um bei bestimmten Ereignissen (z.B. keine Ergebnisdatei vorhanden) eine Warnung signalisieren zu können.
	 * @param warning
	 */
	void setWarning(Boolean warning);

}
