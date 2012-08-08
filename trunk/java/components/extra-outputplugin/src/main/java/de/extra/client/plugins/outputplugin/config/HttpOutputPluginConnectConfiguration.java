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
/*
 * Created on Jan 10, 2007
 */
package de.extra.client.plugins.outputplugin.config;

import javax.inject.Inject;
import javax.inject.Named;

import de.extra.client.core.annotation.PlugInConfigType;
import de.extra.client.core.annotation.PlugInConfigutation;
import de.extra.client.core.annotation.PlugInValue;

@Named("httpOutputPluginConnectConfiguration")
@PlugInConfigutation(plugInBeanName="httpOutputPlugIn", plugInType = PlugInConfigType.OutputPlugIns)
public class HttpOutputPluginConnectConfiguration {

	@PlugInValue(key="userAgent")
	private String userAgent;

	@PlugInValue(key="sslServer")
	private boolean sslServer ;

	@PlugInValue(key="sslTruststoreLocation")
	private String sslTruststoreLocation;

	@PlugInValue(key="sslTruststorePassword")
	private String sslTruststorePassword;

	@PlugInValue(key="sslCertificate")
	private String sslCertificate;

	@PlugInValue(key="sslCertificateRefresh")
	private boolean sslCertificateRefresh = false;

	@PlugInValue(key="serverHost")
	private String serverHost;

	@PlugInValue(key="serverPort")
	private String serverPort;

	@PlugInValue(key="serverURL")
	private String serverURL;

	@PlugInValue(key="proxySet")
	private boolean proxySet = false;

	@PlugInValue(key="proxyHost")
	private String proxyHost;

	@PlugInValue(key="proxyPort")
	private String proxyPort;

	@PlugInValue(key="proxyAuth")
	private boolean proxyAuth = false;

	@PlugInValue(key="proxyUser")
	private String proxyUser;

	@PlugInValue(key="proxyPwd")
	private String proxyPwd;

	@Inject
	@Named("httpOutputPluginSenderDataConfiguration")
	private HttpOutputPluginSenderDataConfiguration senderData;

	/**
	 * @return Returns the proxyAuth.
	 */
	public boolean isProxyAuth() {
		return proxyAuth;
	}

	/**
	 * @param proxyAuth
	 *            The proxyAuth to set.
	 */
	public void setProxyAuth(boolean proxyAuth) {
		this.proxyAuth = proxyAuth;
	}

	/**
	 * @return Returns the proxyHost.
	 */
	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * @param proxyHost
	 *            The proxyHost to set.
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	/**
	 * @return Returns the proxyPort.
	 */
	public String getProxyPort() {
		return proxyPort;
	}

	/**
	 * @param proxyPort
	 *            The proxyPort to set.
	 */
	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}

	/**
	 * @return Returns the proxyPwd.
	 */
	public String getProxyPwd() {
		return proxyPwd;
	}

	/**
	 * @param proxyPwd
	 *            The proxyPwd to set.
	 */
	public void setProxyPwd(String proxyPwd) {
		this.proxyPwd = proxyPwd;
	}

	/**
	 * @return Returns the proxySet.
	 */
	public boolean isProxySet() {
		return proxySet;
	}

	/**
	 * @param proxySet
	 *            The proxySet to set.
	 */
	public void setProxySet(boolean proxySet) {
		this.proxySet = proxySet;
	}

	/**
	 * @return Returns the proxyUser.
	 */
	public String getProxyUser() {
		return proxyUser;
	}

	/**
	 * @param proxyUser
	 *            The proxyUser to set.
	 */
	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	/**
	 * @return Returns the serverHost.
	 */
	public String getServerHost() {
		return serverHost;
	}

	/**
	 * @param serverHost
	 *            The serverHost to set.
	 */
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	/**
	 * @return Returns the serverPort.
	 */
	public String getServerPort() {
		return serverPort;
	}

	/**
	 * @param serverPort
	 *            The serverPort to set.
	 */
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * @return Returns the serverURL.
	 */
	public String getServerURL() {
		return serverURL;
	}

	/**
	 * @param serverURL
	 *            The serverURL to set.
	 */
	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	/**
	 * @return Returns the sslServer.
	 */
	public boolean isSslServer() {
		return sslServer;
	}

	/**
	 * @param sslServer
	 *            The sslServer to set.
	 */
	public void setSslServer(boolean sslServer) {
		this.sslServer = sslServer;
	}

	/**
	 * @return Returns the sslTruststoreLocation.
	 */
	public String getSslTruststoreLocation() {
		return sslTruststoreLocation;
	}

	/**
	 * @param sslTruststoreLocation
	 *            The sslTruststoreLocation to set.
	 */
	public void setSslTruststoreLocation(String sslTruststoreLocation) {
		this.sslTruststoreLocation = sslTruststoreLocation;
	}

	/**
	 * @return Returns the userAgent.
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @param userAgent
	 *            The userAgent to set.
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * @return
	 */
	public int getProxyIntPort() {
		return Integer.parseInt(proxyPort);
	}

	/**
	 * @return Returns the sslTruststorePassword.
	 */
	public String getSslTruststorePassword() {
		return sslTruststorePassword;
	}

	/**
	 * @param sslTruststorePassword
	 *            The sslTruststorePassword to set.
	 */
	public void setSslTruststorePassword(String sslTruststorePassword) {
		this.sslTruststorePassword = sslTruststorePassword;
	}

	/**
	 * @return Returns the sslCertificate.
	 */
	public String getSslCertificate() {
		return sslCertificate;
	}

	/**
	 * @param sslCertificate
	 *            The sslCertificate to set.
	 */
	public void setSslCertificate(String sslCertificate) {
		this.sslCertificate = sslCertificate;
	}

	public boolean isSslCertificateRefresh() {
		return sslCertificateRefresh;
	}

	public void setSslCertificateRefresh(boolean sslCertificateRefresh) {
		this.sslCertificateRefresh = sslCertificateRefresh;
	}

	public HttpOutputPluginSenderDataConfiguration getSenderData() {
		return senderData;
	}

	public void setSenderData(HttpOutputPluginSenderDataConfiguration senderData) {
		this.senderData = senderData;
	}
}