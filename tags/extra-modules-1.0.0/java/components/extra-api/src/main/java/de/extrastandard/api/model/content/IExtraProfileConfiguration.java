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

import java.util.List;
import java.util.Map;

/**
 * @author DSRV
 * @version $Id: IExtraProfileConfiguration.java 756 2012-10-15 14:14:40Z
 *          potap.rentenservice@gmail.com $
 */
public interface IExtraProfileConfiguration {

	/**
	 * @return rootElement Type
	 */
	String getRootElement();

	/**
	 * @return the elementsHierarchyMap
	 */
	Map<String, List<String>> getElementsHierarchyMap();

	/**
	 * Liefert alle Kinderelemente der Knote. Wenn Knote keine kinderelemente
	 * hat, wird ein leeren List zurückgeliefert
	 * 
	 * @param parentElement2
	 * @return
	 */
	List<String> getChildElements(String parentElement);

	/**
	 * @param parentElement
	 * @param childElement
	 * @return
	 */
	String getFieldName(String parentElement, String childElement);

}