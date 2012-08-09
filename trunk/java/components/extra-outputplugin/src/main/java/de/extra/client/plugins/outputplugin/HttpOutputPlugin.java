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

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import de.drv.dsrv.extrastandard.namespace.response.XMLTransport;
import de.extra.client.core.plugin.IOutputPlugin;

@Named("httpOutputPlugin")
public class HttpOutputPlugin implements IOutputPlugin {

	private static Logger logger = Logger.getLogger(HttpOutputPlugin.class);

	@Inject
	@Named("httpOutputPluginSender")
	private HttpOutputPluginSender httpSender;

	@Override
	public XMLTransport outputData(String request) {
		logger.info("Start des Versands...");
		request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + request;
		return httpSender.processOutput(request);
	}
}
