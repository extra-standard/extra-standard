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
package de.extra_standard.namespace.webservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import de.drv.dsrv.extrastandard.namespace.components.Base64CharSequenceType;
import de.drv.dsrv.extrastandard.namespace.components.DataType;
import de.drv.dsrv.extrastandard.namespace.response.ResponseTransport;
import de.drv.dsrv.extrastandard.namespace.response.ResponseTransportBody;

/**
 * @author Leonid Potap
 * @version $Id$
 */
public class ExtraServiceImpl implements Extra {

	private static final Logger logger = LoggerFactory
			.getLogger(ExtraServiceImpl.class);

	@Inject
	@Value("${plugins.wsserver.outputVerzeichnis}")
	private File outputDirectory;

	@Inject
	@Value("${plugins.wsserver.testDataFile}")
	private File testDataFile;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extra_standard.namespace.webservice.Extra#execute(de.drv.dsrv.
	 * extrastandard.namespace.request.Transport)
	 */
	@Override
	@WebMethod(action = "http://www.extra-standard.de/namespace/webservice/execute")
	@WebResult(name = "Transport", targetNamespace = "http://www.extra-standard.de/namespace/response/1", partName = "response")
	public ResponseTransport execute(
			@WebParam(name = "Transport", targetNamespace = "http://www.extra-standard.de/namespace/request/1", partName = "request") final de.drv.dsrv.extrastandard.namespace.request.RequestTransport request)
			throws ExtraFault {
		try {
			logger.info("receive Extra ResponseTransport");
			final Base64CharSequenceType base64CharSequence = request
					.getTransportBody().getData().getBase64CharSequence();
			final DataHandler dataHandler = base64CharSequence.getValue();
			final InputStream inputStream = dataHandler.getInputStream();
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			final String dateSuffix = sdf.format(new Date(System
					.currentTimeMillis()));
			final String dataHandlerName = dataHandler.getName();
			logger.info("Receiving File : " + dataHandlerName);
			final File receivedFile = new File(outputDirectory, dataHandlerName
					+ "_" + dateSuffix);
			final FileOutputStream fileOutputStream = new FileOutputStream(
					receivedFile);
			IOUtils.copyLarge(inputStream, fileOutputStream);
			logger.info("Input file is stored under "
					+ receivedFile.getAbsolutePath());
			logger.info("ChecksumCRC32 "
					+ FileUtils.checksumCRC32(receivedFile));
			logger.info("Filesize: " + FileUtils.sizeOf(receivedFile));
		} catch (final IOException e) {
			logger.error("IOException in Server:", e);
		}
		// TODO TestTransport erzeugen!!
		final ResponseTransport responseTransport = new ResponseTransport();
		final ResponseTransportBody responseTransportBody = new ResponseTransportBody();
		final DataType dataType = new DataType();
		final Base64CharSequenceType base64CharSequenceType = new Base64CharSequenceType();
		final DataSource ds = new FileDataSource(testDataFile);
		final DataHandler dataHandler = new DataHandler(ds);
		base64CharSequenceType.setValue(dataHandler);
		dataType.setBase64CharSequence(base64CharSequenceType);
		responseTransportBody.setData(dataType);
		responseTransport.setTransportBody(responseTransportBody);
		return responseTransport;
	}
}
