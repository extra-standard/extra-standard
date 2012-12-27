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
package de.drv.dsrv.extra.marshaller.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.drv.dsrv.extra.marshaller.IExtraUnmarschaller;
import de.drv.dsrv.extrastandard.namespace.request.Transport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-schema.xml" })
public class ExtraUnmarschallerTest {

	@Inject
	@Named("extraUnmarschaller")
	private transient IExtraUnmarschaller extraUnmarschaller;

	@Inject
	@Named("eXTrajaxb2Marshaller")
	private Marshaller marshaller;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testUnmarschallRequest() {
		final InputStream inputStream = new ByteArrayInputStream(
				validTestRequest.getBytes());
		try {
			final Transport unmarschall = extraUnmarschaller
					.unmarshal(
							inputStream,
							de.drv.dsrv.extrastandard.namespace.request.Transport.class);
			// Keine Exception und der Object nicht leer
			Assert.assertNotNull("transport is null", unmarschall);
		} catch (final XmlMappingException e) {
			Assert.fail(e.getMessage());
		} catch (final IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	private final String validTestRequest = "<xreq:Transport xmlns:xcpt=\"http://www.extra-standard.de/namespace/components/1\" xmlns:xreq=\"http://www.extra-standard.de/namespace/request/1\" xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:xlog=\"http://www.extra-standard.de/namespace/logging/1\" xmlns:xres=\"http://www.extra-standard.de/namespace/response/1\" xmlns:xplg=\"http://www.extra-standard.de/namespace/plugins/1\" xmlns:xmsg=\"http://www.extra-standard.de/namespace/message/1\" xmlns:xsrv=\"http://www.extra-standard.de/namespace/service/1\" version=\"1.3\" profile=\"http://code.google.com/p/extra-standard/profile/1\">\r\n"
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
			+ "            <xcpt:RequestID>STMELD_AUSL_14</xcpt:RequestID>\r\n"
			+ "            <xcpt:TimeStamp>2012-12-24T11:34:42</xcpt:TimeStamp>\r\n"
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
			+ "    <xreq:TransportPlugIns/>\r\n"
			+ "    <xreq:TransportBody>\r\n"
			+ "        <xcpt:Data>\r\n"
			+ "            <xcpt:Base64CharSequence>VTNSbGNtSmxaR0YwWlc0Z015QkpkR0ZzYVdWdQ==</xcpt:Base64CharSequence>\r\n"
			+ "        </xcpt:Data>\r\n"
			+ "    </xreq:TransportBody>\r\n"
			+ "</xreq:Transport>";

	@Test(expected = XmlMappingException.class)
	public final void testUnmarschallInvalidRequest()
			throws XmlMappingException, IOException {
		final InputStream inputStream = new ByteArrayInputStream(
				invalidTestRequest.getBytes());
		extraUnmarschaller.unmarshal(inputStream,
				de.drv.dsrv.extrastandard.namespace.request.Transport.class);
		Assert.fail("Expected XmlMappingException");

	}

	private final String invalidTestRequest = "<xreq:Transport xmlns:xcpt=\"http://www.extra-standard.de/namespace/components/1\" xmlns:xreq=\"http://www.extra-standard.de/namespace/request/1\" xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:xlog=\"http://www.extra-standard.de/namespace/logging/1\" xmlns:xres=\"http://www.extra-standard.de/namespace/response/1\" xmlns:xplg=\"http://www.extra-standard.de/namespace/plugins/1\" xmlns:xmsg=\"http://www.extra-standard.de/namespace/message/1\" xmlns:xsrv=\"http://www.extra-standard.de/namespace/service/1\"  profile=\"http://code.google.com/p/extra-standard/profile/1\">\r\n"
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
			+ "            <xcpt:RequestID>STMELD_AUSL_11</xcpt:RequestID>\r\n"
			+ "            <xcpt:TimeStamp>2012-12-24T11:17:33</xcpt:TimeStamp>\r\n"
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
			+ "    <xreq:TransportPlugIns/>\r\n"
			+ "    <xreq:TransportBody>\r\n"
			+ "        <xcpt:Data>\r\n"
			+ "            <xcpt:Base64CharSequence>VTNSbGNtSmxaR0YwWlc0Z015QkpkR0ZzYVdWdQ==</xcpt:Base64CharSequence>\r\n"
			+ "        </xcpt:Data>\r\n"
			+ "    </xreq:TransportBody>\r\n"
			+ "</xreq:Transport>";

}
