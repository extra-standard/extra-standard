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
package de.extra.client.plugins.outputplugin.ws;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Thorsten Vogel
 * @version $Id$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/spring-properties.xml",
		"/spring-extra-plugin-output-ws.xml" })
public class WsOutputPluginNotWorking {

	@Inject
	@Named("wsOutputPlugin")
	private WsOutputPlugin plugin;

	/**
	 * Test method for
	 * {@link de.extra.client.plugins.outputplugin.ws.WsOutputPlugin#outputData(java.io.InputStream)}
	 */
	@Test
	public void testOutputData() throws Exception {
		InputStream responseData = plugin.outputData(new ByteArrayInputStream(
				request.getBytes()));

		byte[] responseBuffer = new byte[256];
		StringBuilder response = new StringBuilder();
		int numRead = 0;
		while ((numRead = responseData.read(responseBuffer)) != -1) {
			response.append(new String(responseBuffer).substring(0, numRead));
		}

		System.err.println(response.toString());
	}

	private final String request = "<ns6:Transport xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:xcpt=\"http://www.extra-standard.de/namespace/components/1\" xmlns:xres=\"http://www.extra-standard.de/namespace/response/1\" xmlns:xlog=\"http://www.extra-standard.de/namespace/logging/1\" xmlns:ns6=\"http://www.extra-standard.de/namespace/request/1\" xmlns:xplg=\"http://www.extra-standard.de/namespace/plugins/1\" xmlns:xmsg=\"http://www.extra-standard.de/namespace/message/1\" xmlns:xsrv=\"http://www.extra-standard.de/namespace/service/1\">\r\n"
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
