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
package de.drv.dsrv.extra.marshaller;

import java.util.Map;

public class NamespacePrefixMapper extends
		com.sun.xml.bind.marshaller.NamespacePrefixMapper {

	private Map<String, String> mappings;

	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion,
			boolean requirePrefix) {
		if (mappings.containsKey(namespaceUri)) {
			return mappings.get(namespaceUri);
		} else {
			return suggestion;
		}
	}

	public void setMappings(Map<String, String> mappings) {
		this.mappings = mappings;
	}
}
