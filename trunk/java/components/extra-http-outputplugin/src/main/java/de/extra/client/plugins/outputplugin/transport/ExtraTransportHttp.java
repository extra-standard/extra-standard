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
package de.extra.client.plugins.outputplugin.transport;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.contrib.ssl.AuthSSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.extra.client.plugins.outputplugin.ExtraConstants;
import de.extra.client.plugins.outputplugin.config.HttpOutputPluginConnectConfiguration;
import de.extra.client.plugins.outputplugin.config.HttpOutputPluginSenderDataConfiguration;
import de.extra.client.plugins.outputplugin.crypto.ExtraCryptoUtil;

/**
 * ExtrasTransportHttp is an implementation of IExtraTransport and provides
 * Kommunikation via http(s) protocol
 */
public class ExtraTransportHttp implements IExtraTransport {

	private HttpClient client;

	private String requestURL;

	private static final Logger LOG = LoggerFactory
			.getLogger(ExtraTransportHttp.class);

	/**
	 * ExtrasTransportHttp is an implementation of IExtraTransport and provides
	 * Communication via http(s) protocol.
	 *
	 * @see de.extra.client.transport.IExtraTransport#senden(java.lang.String)
	 */
	@Override
	public InputStream senden(final InputStream extraRequest)
			throws ExtraTransportException {

		if (client != null) {

			// Init response
			InputStream extraResponse = null;

			// Build url String and create post request
			PostMethod method = new PostMethod(requestURL);

			try {

				RequestEntity entity = new InputStreamRequestEntity(
						extraRequest);
				method.setRequestEntity(entity);

				// Execute the method - send it
				int statusCode = client.executeMethod(method);

				// Something goes wrong
				if (statusCode != HttpStatus.SC_OK) {
					throw new ExtraTransportException(
							"Versand von Request fehlgeschlagen: "
									+ method.getStatusLine());
				} else {

					// Read the response body and save it

					extraResponse = method.getResponseBodyAsStream();
				}

			} catch (HttpException e) {
				throw new ExtraTransportException(
						"Schwere Protokollverletzung: " + e.getMessage(), e);
			} catch (IOException e) {
				throw new ExtraTransportException("Schwerer Transportfehler: "
						+ e.getMessage(), e);
			} finally {

				// Release the connection.
				method.releaseConnection();
			}
			return extraResponse;
		} else {
			throw new ExtraTransportException(
					"Http Client nicht initialisiert!");
		}
	}

	/**
	 * Inits ExtraTransportHttp including setup of HttpClient, Proxy,
	 * Authentication and Truststore.
	 *
	 * @param extraConnectData
	 * @return void
	 */
	@Override
	public void initTransport(
			final HttpOutputPluginConnectConfiguration extraConnectData)
			throws ExtraTransportException {

		// Create new client instance
		client = new HttpClient();

		// Do the setup
		setupHttpClient(extraConnectData, client);
		setupProxy(extraConnectData, client);
		setupAuthentification(extraConnectData, client);
		setupTruststore(extraConnectData);

		// Init single connection
		requestURL = buildUrlString(extraConnectData);

	}

	/**
	 * Creates a LogString for the requested URL.
	 *
	 * @param extraConnectData
	 * @param requestURL
	 * @return
	 */
	private String buildLogString(
			final HttpOutputPluginConnectConfiguration extraConnectData,
			final String requestURL) {

		HttpOutputPluginSenderDataConfiguration senderData = extraConnectData
				.getSenderData();

		StringBuffer logString = new StringBuffer("HttpClient initialisiert;");
		logString.append("\n- RequestURL = ");
		logString.append(requestURL);
		if (extraConnectData.isProxySet()) {
			logString.append("\n- Proxy = ");
			logString.append(extraConnectData.getProxyHost());
			logString.append(":");
			logString.append(extraConnectData.getProxyPort());
			if (extraConnectData.isProxyAuth()) {
				logString.append("\n- Proxy-Authentifikation: = \"");
				logString.append(extraConnectData.getProxyUser());
				logString.append("\":\"");
				logString.append(extraConnectData.getProxyPwd());
				logString.append("\"");
			} else {
				logString.append("\n- Keine Proxy-Authentifikation");
			}
		} else {
			logString.append("\n- Kein Proxy konfiguriert");
		}
		if (senderData.isServerJ2EESecurity()) {
			logString.append("\n- J2EE Security = ");

			if (senderData.isCertificateAuthentication()) {
				logString
						.append("Zertifikats-basierte Authentifizierung aus \"");
				logString.append(senderData.getPrivateKeyStoreLocation());
				logString.append("\"");
			} else {
				logString.append("\"");
				logString.append(senderData.getServerJ2EEUser());
				// logString.append("\":\"");
				// logString.append(senderData.getServerJ2EEPwd());
				logString.append("\" f�r Realm \"");
				logString.append(senderData.getServerJ2EERealm());
				logString.append("\"");
			}
		} else {
			logString.append("\n- Keine J2EE Security konfiguriert");
		}
		return logString.toString();
	}

	/**
	 * Sets up the HttpClient by setting Parameters.
	 *
	 * @param extraConnectData
	 * @param client
	 */
	private void setupHttpClient(
			final HttpOutputPluginConnectConfiguration extraConnectData,
			final HttpClient client) {

		// Setup user agent and charset
		client.getParams().setParameter("http.useragent",
				extraConnectData.getUserAgent());
		client.getParams().setParameter("http.protocol.content-charset",
				ExtraConstants.REQ_ENCODING);
	}

	/**
	 * Sets up Authentication for the client.
	 *
	 * @param extraConnectData
	 * @param client
	 * @throws ExtraTransportException
	 */
	private void setupAuthentification(
			final HttpOutputPluginConnectConfiguration extraConnectData,
			final HttpClient client) throws ExtraTransportException {
		HttpOutputPluginSenderDataConfiguration senderData = extraConnectData
				.getSenderData();

		// Check if J2EE security is requested (default)
		if (senderData.isServerJ2EESecurity()) {

			String j2eeUser = null;
			String j2eePwd = null;

			// Pass our credentials to HttpClient, they will only be used for
			// authenticating to servers with realm "extra"
			if (senderData.isCertificateAuthentication()) {
				j2eeUser = ExtraCryptoUtil.decrypt(senderData
						.getServerJ2EEUser());
				j2eePwd = ExtraCryptoUtil
						.decrypt(senderData.getServerJ2EEPwd());
			} else {
				j2eeUser = senderData.getServerJ2EEUser();
				j2eePwd = senderData.getServerJ2EEPwd();
			}

			// Pr�fung, ob Benutzer und Passwort f�r die Autentifizierung
			// gesetzt sind
			if (j2eeUser == null || j2eePwd == null) {
				throw new ExtraTransportException(
						"Benutzer und/oder Passwort f�r die J2EE-Sicherheit wurden nicht definiert.");
			}

			client.getState()
					.setCredentials(
							new AuthScope(extraConnectData.getServerHost(),
									AuthScope.ANY_PORT,
									senderData.getServerJ2EERealm()),
							new UsernamePasswordCredentials(j2eeUser, j2eePwd));

			// Send authentication data without extra request
			client.getParams().setAuthenticationPreemptive(true);
		}
		// Load TrustStoreLocation from properties
		String truststoreLocation = extraConnectData.getSslTruststoreLocation();

		// If no location specified -> fallback to JRE default
		if (truststoreLocation == null || truststoreLocation.length() == 0) {
			truststoreLocation = System.getProperty("java.home")
					+ File.separatorChar + "lib" + File.separatorChar
					+ "security" + File.separatorChar + "cacerts";
		}

		try {

			URL keystoreUrl = null;
			String keyPasswd = "";

			if (senderData.isCertificateAuthentication()) {

				keystoreUrl = new URL("file:/"
						+ senderData.getPrivateKeyStoreLocation());
				keyPasswd = senderData.getPrivateKeyPassword();

			}

			ProtocolSocketFactory authSSLProtocolSocketFactory = new AuthSSLProtocolSocketFactory(
					keystoreUrl, keyPasswd, new URL("file:/"
							+ truststoreLocation),
					extraConnectData.getSslTruststorePassword());
			Protocol authhttps = new Protocol("https",
					authSSLProtocolSocketFactory, 9443);
			Protocol.registerProtocol("https", authhttps);
		} catch (MalformedURLException e) {
			throw new ExtraTransportException(
					"SSL-Client Authentification nicht richtig konfiguriert", e);
		}

	}

	/**
	 * Sets up a Proxy for the client.
	 *
	 * @param extraConnectData
	 * @param client
	 */
	private void setupProxy(
			final HttpOutputPluginConnectConfiguration extraConnectData,
			final HttpClient client) {

		HttpOutputPluginSenderDataConfiguration senderData = extraConnectData
				.getSenderData();

		// Check if proxy communication is required
		if (extraConnectData.isProxySet()) {

			// Setup the proxy host and port
			client.getHostConfiguration().setProxy(
					extraConnectData.getProxyHost(),
					extraConnectData.getProxyIntPort());

			// Check if proxy authentication is required
			if (extraConnectData.isProxyAuth()) {

				// set the proxy credentials, only necessary
				// for authenticating proxies
				client.getState()
						.setProxyCredentials(
								new AuthScope(extraConnectData.getProxyHost(),
										extraConnectData.getProxyIntPort(),
										AuthScope.ANY_REALM),
								new UsernamePasswordCredentials(
										extraConnectData.getProxyUser(),
										extraConnectData.getProxyPwd()));
			} else {

				// setAuthenticationPreemptive == true
				if (senderData.isServerJ2EESecurity()) {

					// To prevent warnings create dummy proxy authentication
					client.getState().setProxyCredentials(
							new AuthScope(extraConnectData.getProxyHost(),
									extraConnectData.getProxyIntPort(),
									AuthScope.ANY_REALM),
							new UsernamePasswordCredentials("dummyUser",
									"dummyPwd"));
				}
			}
		}
	}

	/**
	 * Builds an URLString with "http://" or "https://" (if SSL is used).
	 *
	 * @param extraConnectData
	 * @param urlString
	 */
	private String buildUrlString(
			final HttpOutputPluginConnectConfiguration extraConnectData) {

		StringBuffer urlString = new StringBuffer();

		// Check if SSL should be used (default)
		if (extraConnectData.isSslServer()) {

			// SSL-protocol
			urlString.append("https://");

		} else {

			// HTTP-protocol
			urlString.append("http://");
		}

		// Add server address (ip or dn)
		urlString.append(extraConnectData.getServerHost());

		// Check if port setting available
		if (extraConnectData.getServerPort() != null
				&& extraConnectData.getServerPort().length() > 0) {
			urlString.append(":");
			urlString.append(extraConnectData.getServerPort());
		}

		// Add context root and action
		urlString.append(extraConnectData.getServerURL());

		return urlString.toString();
	}

	/**
	 * Sets up the Truststore.
	 *
	 * @param extraConnectData
	 * @return
	 */
	private void setupTruststore(
			final HttpOutputPluginConnectConfiguration extraConnectData)
			throws ExtraTransportException {

		// Load TrustStoreLocation from properties
		String truststoreLocation = extraConnectData.getSslTruststoreLocation();

		LOG.debug("TruststoreLoc: " + truststoreLocation);

		// If no location specified -> fallback to JRE default
		if (truststoreLocation == null || truststoreLocation.length() == 0) {
			truststoreLocation = System.getProperty("java.home")
					+ File.separatorChar + "lib" + File.separatorChar
					+ "security" + File.separatorChar + "cacerts";
		}

		LOG.debug("TruststoreLoc: " + truststoreLocation);

		try {
			// Create keystore instance
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			// KeyStore ks = KeyStore.getInstance("PKCS12");

			// Load keystore values
			FileInputStream fi = new FileInputStream(truststoreLocation);
			ks.load(fi, extraConnectData.getSslTruststorePassword()
					.toCharArray());
			fi.close();

			// Create new certificate based on stored value
			java.security.cert.CertificateFactory certFactory = CertificateFactory
					.getInstance("X.509");

			X509Certificate cert = (X509Certificate) certFactory
					.generateCertificate(new ByteArrayInputStream(
							extraConnectData.getSslCertificate().getBytes()));

			// Check if certificate is not already stored -> store and save
			if (extraConnectData.isSslCertificateRefresh()
					|| ks.getCertificateAlias(cert) == null) {

				LOG.info("Zertifikat wird eingetragen");

				ks.store(new FileOutputStream(truststoreLocation),
						extraConnectData.getSslTruststorePassword()
								.toCharArray());

			}

			// Set truststore location
			System.setProperty("javax.net.ssl.trustStore", truststoreLocation);

		} catch (KeyStoreException e) {
			throw new ExtraTransportException(
					"Fehler bei Zugriff auf Keystore.", e);
		} catch (FileNotFoundException e) {
			throw new ExtraTransportException(
					"Fehler beim Laden des Keystore.", e);
		} catch (NoSuchAlgorithmException e) {
			throw new ExtraTransportException(
					"Fehler beim Laden des Crypto-Algorithmus.", e);
		} catch (CertificateException e) {
			throw new ExtraTransportException(
					"Fehler beim Pr�fen des Zertifikats.", e);
		} catch (IOException e) {
			throw new ExtraTransportException("Fehler bei I/O-Operation.", e);
		}
	}

}
