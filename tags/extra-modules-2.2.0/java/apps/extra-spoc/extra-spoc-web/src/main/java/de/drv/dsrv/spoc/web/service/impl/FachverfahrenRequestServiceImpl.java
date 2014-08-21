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
package de.drv.dsrv.spoc.web.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreProtocolPNames;

import de.drv.dsrv.spoc.extra.v1_3.ExtraErrorResponseException;
import de.drv.dsrv.spoc.extra.v1_3.ExtraJaxbMarshaller;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.request.TransportRequestType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.response.TransportResponseType;
import de.drv.dsrv.spoc.web.manager.SpocNutzdatenManager;
import de.drv.dsrv.spoc.web.manager.SpocNutzdatenManagerException;
import de.drv.dsrv.spoc.web.service.FachverfahrenRequestService;
import de.drv.dsrv.spoc.web.webservice.jax.ExtraError;

/**
 * Standard-Implementierung des <code>FachverfahrenRequestService</code>.
 */
public class FachverfahrenRequestServiceImpl implements FachverfahrenRequestService {

	private static final ContentType CONTENT_TYPE = ContentType.create("text/xml", Consts.UTF_8);

	private final String soapFaultString;
	private final HttpClient httpClient;
	private final SpocResponseHandler responseHandler;
	private final ExtraJaxbMarshaller extraJaxbMarshaller;
	private final SpocNutzdatenManager spocNutzdatenManager;

	/**
	 * Konstruktor.
	 * 
	 * @param maxConnections
	 *            die maximale Anzahl von gleichzeitigen HTTP-Verbindungen, die
	 *            vom Service insgesamt zu Fachverfahren aufgebaut werden; wenn
	 *            die Grenze erreicht wird, werden weitere Verbindungen
	 *            geblockt, bis eine Connection frei wird
	 * @param maxConnectionsPerRoute
	 *            die maximale Anzahl von gleichzeitigen HTTP-Verbindungen, die
	 *            vom Service zu einer bestimmten Route (entspricht einem
	 *            Server) aufgebaut werden; wenn die Grenze erreicht wird,
	 *            werden weitere Verbindungen geblockt, bis eine Connection frei
	 *            wird
	 * @param soapFaultString
	 *            Text des SOAP-Fehlers
	 * @param responseHandler
	 *            Handler zur Verarbeitung der Antworten des Fachverfahrens
	 * @param extraJaxbMarshaller
	 *            JAXB-Marshaller
	 * @param spocNutzdatenManager
	 *            Manager zum ZUgriff auf die Nutzerdaten
	 * 
	 * @throws IllegalArgumentException
	 *             wenn <code>maxConnections</code> oder
	 *             <code>maxConnectionsPerRoute</code> kleiner als 0 sind
	 */
	public FachverfahrenRequestServiceImpl(final int maxConnections, final int maxConnectionsPerRoute,
			final String soapFaultString, final SpocResponseHandler responseHandler,
			final ExtraJaxbMarshaller extraJaxbMarshaller, final SpocNutzdatenManager spocNutzdatenManager) {
		this.soapFaultString = soapFaultString;
		final PoolingClientConnectionManager conman = new PoolingClientConnectionManager();
		conman.setMaxTotal(maxConnections);
		conman.setDefaultMaxPerRoute(maxConnectionsPerRoute);
		this.httpClient = new DefaultHttpClient(conman);
		this.httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, Consts.UTF_8.name());
		this.responseHandler = responseHandler;
		this.extraJaxbMarshaller = extraJaxbMarshaller;
		this.spocNutzdatenManager = spocNutzdatenManager;
	}

	@Override
	public StreamSource executeFachverfahrenRequest(final URI fachverfahrenUrl, final InputStream payloadInputStream)
			throws IOException {

		final HttpPost httpPost = new HttpPost(fachverfahrenUrl);
		httpPost.setEntity(new InputStreamEntity(payloadInputStream, -1L, CONTENT_TYPE));

		StreamSource response;
		try {
			response = this.httpClient.execute(httpPost, this.responseHandler);
		} finally {
			httpPost.releaseConnection();
		}

		return response;
	}

	@Override
	public TransportResponseType executeFachverfahrenRequest(final URI fachverfahrenUri,
			final TransportRequestType transportRequestType) throws IOException, ExtraError, JAXBException,
			SpocNutzdatenManagerException {

		// Verarbeite evtl. vorhandene Nutzdaten des Requests
		this.spocNutzdatenManager.processRequestNutzdaten(transportRequestType);

		// Schicke Request an Fachverfahren
		final HttpResponse httpResponse = processRequest(fachverfahrenUri, transportRequestType);

		// Verarbeite Response
		final TransportResponseType transportResponseType = processResponse(httpResponse);

		// Verarbeite evtl. vorhandene Nutzdaten der Response
		this.spocNutzdatenManager.processResponseNutzdaten(transportResponseType);

		return transportResponseType;
	}

	private HttpResponse processRequest(final URI fachverfahrenUri, final TransportRequestType transportRequestType)
			throws IOException, JAXBException {

		final HttpPost httpPost = new HttpPost(fachverfahrenUri);

		try {
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			this.extraJaxbMarshaller.marshalTransportRequest(transportRequestType, outputStream);
			httpPost.setEntity(new InputStreamEntity(new ByteArrayInputStream(outputStream.toByteArray()), -1L,
					CONTENT_TYPE));
			// Schicke Request an Fachverfahren
			return this.httpClient.execute(httpPost);
		} finally {
			httpPost.releaseConnection();
		}
	}

	private TransportResponseType processResponse(final HttpResponse httpResponse) throws IOException, JAXBException,
			ExtraError {

		final HttpEntity httpResponseEntity = this.responseHandler.getAndCheckHttpEntity(httpResponse);

		// Erzeuge Source aus HttpResponseEntity
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		httpResponseEntity.writeTo(outputStream);
		final Source source = new StreamSource(new ByteArrayInputStream(outputStream.toByteArray()));

		try {
			return this.extraJaxbMarshaller.unmarshalTransportResponse(source);
		} catch (final ExtraErrorResponseException e) {
			// falls die Response vom Fachverfahren ein ExtraError ist
			throw new ExtraError(this.soapFaultString, e.getExtraErrorType());
		}
	}

}