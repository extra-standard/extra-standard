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
package de.extra.client.plugins.outputplugin.config;

import javax.inject.Named;

import de.extra.client.core.annotation.PluginConfigType;
import de.extra.client.core.annotation.PluginConfiguration;
import de.extra.client.core.annotation.PluginValue;


@Named("httpOutputPluginSenderDataConfiguration")
@PluginConfiguration(pluginBeanName="httpOutputPlugin", pluginType = PluginConfigType.OutputPlugins)
public class HttpOutputPluginSenderDataConfiguration {

	@PluginValue(key="senderID")
	private String senderID = "";

	@PluginValue(key="senderClassification")
	private String senderClassification = null;

	@PluginValue(key="senderName")
	private String senderName = null;

	@PluginValue(key="serverJ2EESecurity")
	private boolean serverJ2EESecurity = true;

	@PluginValue(key="serverJ2EEUser")
	private String serverJ2EEUser = "";

	@PluginValue(key="serverJ2EEPwd")
	private String serverJ2EEPwd = "";

	@PluginValue(key="serverJ2EERealm")
	private String serverJ2EERealm = "";

	@PluginValue(key="certificateAuthentication")
	private boolean certificateAuthentication = false;

	@PluginValue(key="privateKeyPassword")
	private String privateKeyPassword = "";

	@PluginValue(key="privateKeyStoreLocation")
	private String privateKeyStoreLocation = "";

	@PluginValue(key="privateKeyStoreType")
	private String privateKeyStoreType = "";

	@PluginValue(key="privateKeyType")
	private String privateKeyType = "";

	@PluginValue(key="signatureAlgorithm")
	private String signatureAlgorithm = "";

	@PluginValue(key="messageDigestAlgorithm")
	private String messageDigestAlgorithm = "";

	@PluginValue(key="directoryOverwrite")
	private boolean directoryOverwrite = false;

	@PluginValue(key="directoryPath")
	private String directoryPath = "";

	@PluginValue(key="directorySeparation")
	private boolean directorySeparation = true;

	public String getPrivateKeyStoreLocation() {
		return privateKeyStoreLocation;
	}

	public void setPrivateKeyStoreLocation(String keyStoreLocation) {
		this.privateKeyStoreLocation = keyStoreLocation;
	}

	public String getPrivateKeyPassword() {
		return privateKeyPassword;
	}

	public void setPrivateKeyPassword(String privateKeyPassword) {
		this.privateKeyPassword = privateKeyPassword;
	}

	/**
	 * @return Returns the serverJ2EESecurity.
	 */
	public boolean isServerJ2EESecurity() {
		return serverJ2EESecurity;
	}

	/**
	 * @param serverJ2EESecurity
	 *            The serverJ2EESecurity to set.
	 */
	public void setServerJ2EESecurity(boolean serverJ2EESecurity) {
		this.serverJ2EESecurity = serverJ2EESecurity;
	}

	/**
	 * @return Returns the serverJ2EEPwd.
	 */
	public String getServerJ2EEPwd() {
		return serverJ2EEPwd;
	}

	/**
	 * @param serverJ2EEPwd
	 *            The serverJ2EEPwd to set.
	 */
	public void setServerJ2EEPwd(String serverJ2EEPwd) {
		this.serverJ2EEPwd = serverJ2EEPwd;
	}

	/**
	 * @return Returns the serverJ2EEUser.
	 */
	public String getServerJ2EEUser() {
		return serverJ2EEUser;
	}

	/**
	 * @param serverJ2EEUser
	 *            The serverJ2EEUser to set.
	 */
	public void setServerJ2EEUser(String serverJ2EEUser) {
		this.serverJ2EEUser = serverJ2EEUser;
	}

	/**
	 * @return Returns the serverJ2EERealm.
	 */
	public String getServerJ2EERealm() {
		return serverJ2EERealm;
	}

	/**
	 * @param serverJ2EERealm
	 *            The serverJ2EERealm to set.
	 */
	public void setServerJ2EERealm(String serverJ2EERealm) {
		this.serverJ2EERealm = serverJ2EERealm;
	}

	/**
	 * @return Returns the senderClassification.
	 */
	public String getSenderClassification() {
		return senderClassification;
	}

	/**
	 * @param senderClassification
	 *            The senderClassification to set.
	 */
	public void setSenderClassification(String senderClassification) {
		this.senderClassification = senderClassification;
	}

	/**
	 * @return Returns the senderID.
	 */
	public String getSenderID() {
		return senderID;
	}

	/**
	 * @param senderID
	 *            The senderID to set.
	 */
	public void setSenderID(String senderID) {
		this.senderID = senderID;
	}

	/**
	 * @return Returns the senderName.
	 */
	public String getSenderName() {
		return senderName;
	}

	/**
	 * @param senderName
	 *            The senderName to set.
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public boolean isCertificateAuthentication() {
		return certificateAuthentication;
	}

	public void setCertificateAuthentication(boolean certificateAuthentication) {
		this.certificateAuthentication = certificateAuthentication;
	}

	public String getPrivateKeyStoreType() {
		return privateKeyStoreType;
	}

	public void setPrivateKeyStoreType(String keyStoreType) {
		this.privateKeyStoreType = keyStoreType;
	}

	public String getPrivateKeyType() {
		return privateKeyType;
	}

	public void setPrivateKeyType(String keyType) {
		this.privateKeyType = keyType;
	}

	public String getMessageDigestAlgorithm() {
		return messageDigestAlgorithm;
	}

	public void setMessageDigestAlgorithm(String messageDigestAlgorithm) {
		this.messageDigestAlgorithm = messageDigestAlgorithm;
	}

	public String getSignatureAlgorithm() {
		return signatureAlgorithm;
	}

	public void setSignatureAlgorithm(String signatureAlgorithm) {
		this.signatureAlgorithm = signatureAlgorithm;
	}

	public boolean isDirectoryOverwrite() {
		return directoryOverwrite;
	}

	public void setDirectoryOverwrite(boolean directoryOverwrite) {
		this.directoryOverwrite = directoryOverwrite;
	}

	public String getDirectoryPath() {
		return directoryPath;
	}

	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}

	public boolean isDirectorySeparation() {
		return directorySeparation;
	}

	public void setDirectorySeparation(boolean directorySeparation) {
		this.directorySeparation = directorySeparation;
	}
}