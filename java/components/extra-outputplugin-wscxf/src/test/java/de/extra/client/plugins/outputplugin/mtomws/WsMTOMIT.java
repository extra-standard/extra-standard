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
package de.extra.client.plugins.outputplugin.mtomws;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.drv.dsrv.extra.marshaller.IExtraUnmarschaller;
import de.drv.dsrv.extrastandard.namespace.components.Base64CharSequenceType;
import de.drv.dsrv.extrastandard.namespace.request.Transport;

/**
 * @author evpqq5
 * @version $Id$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/spring-properties.xml",
		"/spring-extra-plugin-output-ws-mtom.xml", "/spring-schema.xml",
		"/spring-extra-ws-mtom-server.xml" })
public class WsMTOMIT {

	private static final Logger logger = LoggerFactory
			.getLogger(WsMTOMIT.class);

	@Inject
	@Named("wsMTOMOutputPlugin")
	private WsMTOMOutputPlugin plugin;

	@Inject
	@Named("extraUnmarschaller")
	private IExtraUnmarschaller extraUnmarschaller;

	@Inject
	@Value("${plugins.dataplugin.fileDataPlugin.inputVerzeichnis}")
	private File inputDirectory;

	/**
	 * Test method for
	 * {@link de.extra.client.plugins.outputplugin.ws.WsOutputPlugin#outputData(java.io.InputStream)}
	 */
	@Test
	public void testOutputData() throws Exception {
		logger.info("Start Test OutputData");
		final List<Transport> transportsToSend = createTestsTransportsWithFileAttachment();
		for (final Transport requestTransport : transportsToSend) {
			logger.info("Sending Transport");
			final de.drv.dsrv.extrastandard.namespace.response.Transport responseTransport = plugin
					.outputData(requestTransport);
			logger.info("Receive Response");
			printResponse(responseTransport);
		}

	}

	private void printResponse(
			final de.drv.dsrv.extrastandard.namespace.response.Transport responseTransport) {
		logger.info("printResponse: " + responseTransport.getClass());
	}

	/**
	 * @return
	 * @throws XmlMappingException
	 * @throws IOException
	 */
	private List<Transport> createTestsTransportsWithFileAttachment()
			throws XmlMappingException, IOException {
		logger.info("Looking for Inptut into the Directory: "
				+ inputDirectory.getAbsolutePath());
		final List<Transport> transports = new ArrayList<Transport>();
		final Collection<File> listFiles = FileUtils.listFiles(inputDirectory,
				TrueFileFilter.INSTANCE, null);
		for (final File inputFile : listFiles) {
			final Transport requestTransport = createDummyRequestTransport();
			final Transport filledTransport = fillInputFile(requestTransport,
					inputFile);
			transports.add(filledTransport);
			logger.info("Find File to send: " + inputFile.getName());
			logger.info("ChecksumCRC32: " + FileUtils.checksumCRC32(inputFile));
			logger.info("Filesize: " + FileUtils.sizeOf(inputFile));
		}
		return transports;
	}

	/**
	 * Führt zu dem Transport InputFile
	 * 
	 * @param requestTransport
	 * @param inputFile
	 * @return
	 */
	private Transport fillInputFile(final Transport requestTransport,
			final File inputFile) {
		final Base64CharSequenceType base64CharSequence = requestTransport
				.getTransportBody().getData().getBase64CharSequence();
		final DataSource source = new FileDataSource(inputFile);
		base64CharSequence.setValue(new DataHandler(source));

		return requestTransport;

	}

	/**
	 * Erstellt einen Dummy extraTransport Request
	 * 
	 * @return
	 * @throws IOException
	 * @throws XmlMappingException
	 */
	private Transport createDummyRequestTransport() throws XmlMappingException,
			IOException {
		final InputStream inputStream = new ByteArrayInputStream(
				dummyRequest.getBytes());
		final Transport transportRequestType = extraUnmarschaller.unmarshal(
				inputStream, Transport.class);
		return transportRequestType;
	}

	private final String dummyRequest = "<xreq:Transport xmlns:xcpt=\"http://www.extra-standard.de/namespace/components/1\" xmlns:xreq=\"http://www.extra-standard.de/namespace/request/1\" xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:xlog=\"http://www.extra-standard.de/namespace/logging/1\" xmlns:xres=\"http://www.extra-standard.de/namespace/response/1\" xmlns:xplg=\"http://www.extra-standard.de/namespace/plugins/1\" xmlns:xmsg=\"http://www.extra-standard.de/namespace/message/1\" xmlns:xsrv=\"http://www.extra-standard.de/namespace/service/1\" version=\"1.3\" profile=\"http://code.google.com/p/extra-standard/profile/1\">\r\n"
			+ "    <xreq:TransportHeader>\r\n"
			+ "        <xcpt:TestIndicator>http://www.extra-standard.de/test/NONE</xcpt:TestIndicator>\r\n"
			+ "        <xcpt:Sender>\r\n"
			+ "            <xcpt:SenderID>ec-1</xcpt:SenderID>\r\n"
			+ "            <xcpt:Name>eXTra-Client</xcpt:Name>\r\n"
			+ "        </xcpt:Sender>\r\n"
			+ "        <xcpt:Receiver>\r\n"
			+ "            <xcpt:ReceiverID>es-1</xcpt:ReceiverID>\r\n"
			+ "            <xcpt:Name>eXTra-Server</xcpt:Name>\r\n"
			+ "        </xcpt:Receiver>\r\n"
			+ "        <xcpt:RequestDetails>\r\n"
			+ "            <xcpt:RequestID>STMELD_AUSL_3</xcpt:RequestID>\r\n"
			+ "            <xcpt:TimeStamp>2013-02-21T11:38:00.854+01:00</xcpt:TimeStamp>\r\n"
			+ "            <xcpt:Application>\r\n"
			+ "                <xcpt:Product>eXTra Klient OpenSource</xcpt:Product>\r\n"
			+ "                <xcpt:Manufacturer>OpenSource</xcpt:Manufacturer>\r\n"
			+ "                <xcpt:RegistrationID/>\r\n"
			+ "            </xcpt:Application>\r\n"
			+ "            <xcpt:Procedure>http://www.extra-standard.de/procedures/SterbemeldungAusland</xcpt:Procedure>\r\n"
			+ "            <xcpt:DataType>http://www.extra-standard.de/datatypes/DataSend</xcpt:DataType>\r\n"
			+ "            <xcpt:Scenario>http://www.extra-standard.de/scenario/request-with-acknowledgement</xcpt:Scenario>\r\n"
			+ "        </xcpt:RequestDetails>\r\n"
			+ "    </xreq:TransportHeader>\r\n"
			+ "    <xreq:TransportPlugIns>\r\n"
			+ "        <xplg:DataSource>\r\n"
			+ "        </xplg:DataSource>\r\n"
			+ "    </xreq:TransportPlugIns>\r\n"
			+ "    <xreq:TransportBody>\r\n"
			+ "        <xcpt:Data>\r\n"
			+ "            <xcpt:Base64CharSequence></xcpt:Base64CharSequence>\r\n"
			+ "        </xcpt:Data>\r\n"
			+ "    </xreq:TransportBody>\r\n"
			+ "</xreq:Transport>";

}