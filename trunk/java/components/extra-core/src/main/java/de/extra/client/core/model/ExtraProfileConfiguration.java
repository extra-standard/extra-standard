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
package de.extra.client.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.extrastandard.api.model.IExtraProfileConfiguration;

/**
 * Configuration DataBean beinhaltet die profilierungs Informationen
 * 
 */
public class ExtraProfileConfiguration implements IExtraProfileConfiguration {

	/**
	 * Speichert der name des parentElements
	 */
	private String rootElement;

	/**
	 * Speichert die Hierarchy des Elements evtl. eine neue Klasse erstellen
	 */
	private Map<String, List<String>> elementsHierarchyMap = new HashMap<String, List<String>>();

	// Paketebene vorhanden
	private boolean packageLayer = false;
	// Nachrichtenebene vorhanden
	private boolean messageLayer = false;
	// Art der Nutzdaten
	private String contentType;

	/* (non-Javadoc)
	 * @see de.extra.client.core.model.IExtraProfileConfiguration#getContentType()
	 */
	@Override
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/* (non-Javadoc)
	 * @see de.extra.client.core.model.IExtraProfileConfiguration#isMessageLayer()
	 */
	@Override
	public boolean isMessageLayer() {
		return messageLayer;
	}

	public void setMessageLayer(boolean messageLayer) {
		this.messageLayer = messageLayer;
	}

	/* (non-Javadoc)
	 * @see de.extra.client.core.model.IExtraProfileConfiguration#isPackageLayer()
	 */
	@Override
	public boolean isPackageLayer() {
		return packageLayer;
	}

	public void setPackageLayer(boolean packageLayer) {
		this.packageLayer = packageLayer;
	}

	/* (non-Javadoc)
	 * @see de.extra.client.core.model.IExtraProfileConfiguration#getRootElement()
	 */
	@Override
	public String getRootElement() {
		return rootElement;
	}

	/**
	 * @param parentElement
	 */
	public void setRootElement(String rootElement) {
		this.rootElement = rootElement;
	}

	/* (non-Javadoc)
	 * @see de.extra.client.core.model.IExtraProfileConfiguration#getElementsHierarchyMap()
	 */
	@Override
	public Map<String, List<String>> getElementsHierarchyMap() {
		return elementsHierarchyMap;
	}

	/**
	 * @param elementsHierarchyMap
	 *            the elementsHierarchyMap to set
	 */
	public void addElementsHierarchyMap(String elternElement, String elementName) {
		List<String> childs = elementsHierarchyMap.get(elternElement);
		if (childs == null) {
			childs = new LinkedList<String>();
			elementsHierarchyMap.put(elternElement, childs);
		}
		childs.add(elementName);

	}

	/* (non-Javadoc)
	 * @see de.extra.client.core.model.IExtraProfileConfiguration#getChildElements(java.lang.String)
	 */
	@Override
	public List<String> getChildElements(String parentElement) {
		String preparedParentElement = removePrefix(parentElement);
		List<String> childSlements = elementsHierarchyMap
				.get(preparedParentElement);
		if (childSlements == null) {
			childSlements = new ArrayList<String>();
		}
		return childSlements;
	}

	/* (non-Javadoc)
	 * @see de.extra.client.core.model.IExtraProfileConfiguration#getFieldName(java.lang.String, java.lang.String)
	 */
	@Override
	public String getFieldName(String parentElement, String childElement) {
		// Die Implementierung geht davon aus, dass Feldname des Elements dem
		// childElementName ohne Prefix entspricht.
		// TODO Den Profiler anschauen.
		String childElementOhnePrefix = removePrefix(childElement);
		String childElementFieldName = StringUtils
				.uncapitalize(childElementOhnePrefix);
		// Noch ein Workaround Plugin -> PlugIn konvertieren
		childElementFieldName = convertPluginName(childElementFieldName);
		return childElementFieldName;
	}

	/**
	 * @param elemenNameMitPrefix
	 * @return
	 */
	private String removePrefix(String elemenNameMitPrefix) {
		// Workaround. Momentan stimmen die Namem in
		// request-abholen-DEUEV-profil-konfiguration.xml nicht
		// überrein
		// Hier entferne ich prefix vor dem ':'
		String[] splitArray = StringUtils.split(elemenNameMitPrefix, ":");
		return splitArray[1];
	}

	/**
	 * // Noch ein Workaround Plugin -> PlugIn konvertieren
	 * 
	 * @param fieldName
	 * @return
	 */
	private String convertPluginName(String fieldName) {
		String plugInConverted = StringUtils.replace(fieldName, "Plugin",
				"PlugIn");
		return plugInConverted;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("ConfigFileBean [parentElement=");
		builder.append(rootElement);
		builder.append(", elementsHierarchyMap=");
		builder.append(elementsHierarchyMap != null ? toString(
				elementsHierarchyMap.entrySet(), maxLen) : null);
		builder.append(", packageLayer=");
		builder.append(packageLayer);
		builder.append(", messageLayer=");
		builder.append(messageLayer);
		builder.append(", contentType=");
		builder.append(contentType);
		builder.append("]");
		return builder.toString();
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
				&& i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

}
