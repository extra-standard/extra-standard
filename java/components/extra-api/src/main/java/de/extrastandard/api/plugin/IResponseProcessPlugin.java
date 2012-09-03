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
package de.extrastandard.api.plugin;

import java.io.InputStream;
import java.util.List;

import de.extrastandard.api.model.content.IResponseData;

/**
 * Verarbeitet Response vom eXTra Server.
 * 
 * @author Leonid Potap
 * @version $Id$
 */
public interface IResponseProcessPlugin {

	/**
	 * Handelt Response vom eXTra Server. Liefert eine Liste mit den Ergebnissen
	 * der Verarbeitung zurück
	 * 
	 * @param extraResponse
	 * @return
	 */
	List<IResponseData> processResponse(InputStream responseAsStream);

}
