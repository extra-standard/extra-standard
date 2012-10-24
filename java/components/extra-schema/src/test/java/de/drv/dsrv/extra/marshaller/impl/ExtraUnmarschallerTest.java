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
				testRequest.getBytes());
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

	private final String testRequest = "<ns6:Transport xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:xcpt=\"http://www.extra-standard.de/namespace/components/1\" xmlns:xres=\"http://www.extra-standard.de/namespace/response/1\" xmlns:xlog=\"http://www.extra-standard.de/namespace/logging/1\" xmlns:ns6=\"http://www.extra-standard.de/namespace/request/1\" xmlns:xplg=\"http://www.extra-standard.de/namespace/plugins/1\" xmlns:xmsg=\"http://www.extra-standard.de/namespace/message/1\" xmlns:xsrv=\"http://www.extra-standard.de/namespace/service/1\">\r\n"
			+ "    <ns6:TransportHeader>\r\n"
			+ "        <xcpt:TestIndicator>http://extra-standard.de/test/NONE</xcpt:TestIndicator>\r\n"
			+ "        <xcpt:Sender>\r\n"
			+ "            <xcpt:SenderID class=\"Betriebsnummer\">875624</xcpt:SenderID>\r\n"
			+ "            <xcpt:Name>Tester 1</xcpt:Name>\r\n"
			+ "        </xcpt:Sender>\r\n"
			+ "        <xcpt:Receiver>\r\n"
			+ "            <xcpt:ReceiverID class=\"Betriebsnummer\">12345678</xcpt:ReceiverID>\r\n"
			+ "            <xcpt:Name>Tester 2</xcpt:Name>\r\n"
			+ "        </xcpt:Receiver>\r\n"
			+ "        <xcpt:RequestDetails>\r\n"
			+ "            <xcpt:RequestID class=\"0\">D:\\eclipse-workspaces\\extra\\extra-dev\\java\\apps\\extra-scenario-sendfetch\\target\\test-classes\\testDirectories\\input\\order.txt</xcpt:RequestID>\r\n"
			+ "            <xcpt:TimeStamp>2012-09-07T11:31:50</xcpt:TimeStamp>\r\n"
			+ "            <xcpt:Application>\r\n"
			+ "                <xcpt:Product>eXTra Klient OpenSource</xcpt:Product>\r\n"
			+ "                <xcpt:Manufacturer>OpenSource</xcpt:Manufacturer>\r\n"
			+ "                <xcpt:RegistrationID/>\r\n"
			+ "            </xcpt:Application>\r\n"
			+ "            <xcpt:Procedure>DeliveryServer</xcpt:Procedure>\r\n"
			+ "            <xcpt:DataType>http://www.extra-standard.de/datatypes/DataRequest</xcpt:DataType>\r\n"
			+ "            <xcpt:Scenario>http://www.extra-standard.de/scenario/request-with-response</xcpt:Scenario>\r\n"
			+ "        </xcpt:RequestDetails>\r\n"
			+ "    </ns6:TransportHeader>\r\n"
			+ "    <ns6:TransportPlugIns>\r\n"
			+ "        <xplg:contactType>\r\n"
			+ "            <xplg:Endpoint type=\"SMTP\">test@rentenservice.de</xplg:Endpoint>\r\n"
			+ "        </xplg:contactType>\r\n"
			+ "    </ns6:TransportPlugIns>\r\n"
			+ "    <ns6:TransportBody>\r\n"
			+ "        <xcpt:Data>\r\n"
			+ "            <xcpt:CharSequence>test</xcpt:CharSequence>\r\n"
			+ "        </xcpt:Data>\r\n"
			+ "    </ns6:TransportBody>\r\n"
			+ "</ns6:Transport>";

}
