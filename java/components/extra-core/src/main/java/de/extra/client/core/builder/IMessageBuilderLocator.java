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
package de.extra.client.core.builder;

import de.extrastandard.api.model.content.IInputDataContainer;

/**
 * @author Leonid Potap Locator für die MessageBuilder
 * 
 */
public interface IMessageBuilderLocator {

	/**
	 * Liefert ein MessageBuilder abhängig von dem ParentElement und Type von
	 * dem childElement
	 * 
	 * @param elementXmlType
	 * @param senderData
	 * @return
	 */
	public IXmlComplexTypeBuilder getXmlComplexTypeBuilder(String elementXmlType,
			IInputDataContainer senderData);

	/**
	 * Liefert ein MessageBuilder abhängig von dem ParentElement und Type von
	 * dem childElement
	 * 
	 * @param elementType
	 * @return
	 */
	public IXmlRootElementBuilder getRootXmlBuilder(String elementType);
}
