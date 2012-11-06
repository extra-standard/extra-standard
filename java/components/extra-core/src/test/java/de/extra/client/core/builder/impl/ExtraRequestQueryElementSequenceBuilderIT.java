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
package de.extra.client.core.builder.impl;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.drv.dsrv.extrastandard.namespace.components.RootElementType;
import de.extra.client.core.config.impl.ExtraProfileConfiguration;
import de.extra.client.core.model.inputdata.impl.DBQueryInputData;
import de.extrastandard.api.model.content.IDbQueryInputData;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;

/**
 * Test for Requst mit dem Element ElementSequenceBuilder.
 * 
 * @author Leonid Potap
 * @since 1.0.0
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-core.xml", "/spring-schema.xml",
		"/builder/spring-ittest-query-elementsequencebuilder-properties.xml" })
public class ExtraRequestQueryElementSequenceBuilderIT {

	private static final Logger logger = LoggerFactory.getLogger(ExtraRequestQueryElementSequenceBuilderIT.class);

	private final String requiredXmlType = "xcpt:Transport";

	@Inject
	@Named("extraRequestBuilder")
	private ExtraRequestBuilder extraRequestBuilder;

	@Inject
	@Named("extraRequestBuilderITBasic")
	private ExtraRequestBuilderITBasic extraRequestBuilderITBasic;

	@Test
	public final void testBuildElementSequenseDataXmlMessage() {
		final IDbQueryInputData senderData = createTestDummyDBQueryInputData();
		final IExtraProfileConfiguration config = createConfigFileBeanForElementSequenseData();
		final RootElementType elementType = extraRequestBuilder.buildXmlMessage(senderData, config);
		assertNotNull(elementType);
		final String messageAsString = extraRequestBuilderITBasic.getResultAsString(elementType);
		logger.debug("ExtraResponse: " + messageAsString);
		// final String expectedMessage = buildExpectedMessage();
		// TODO Timestamp ist anders ??
		// Assert.assertEquals(expectedMessage, messageAsString);

	}

	public String buildExpectedMessage() {
		final String expectedMessage = "<ns6:xmlTransport xmlns:xlog=\"http://www.extra-standard.de/namespace/logging/1\" xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\" xmlns:xsrv=\"http://www.extra-standard.de/namespace/service/1\" xmlns:xplg=\"http://www.extra-standard.de/namespace/plugins/1\" xmlns:xres=\"http://www.extra-standard.de/namespace/response/1\" xmlns:ns6=\"http://www.extra-standard.de/namespace/request/1\" xmlns:xcpt=\"http://www.extra-standard.de/namespace/components/1\" xmlns:xmsg=\"http://www.extra-standard.de/namespace/message/1\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\r\n"
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
				+ "            <xcpt:RequestID class=\"0\">requestId</xcpt:RequestID>\r\n"
				+ "            <xcpt:TimeStamp>2012-10-09T10:39:38</xcpt:TimeStamp>\r\n"
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
				+ "        <xplg:dataSource>\r\n"
				+ "            <xplg:DataContainer type=\"http://extra-standard.de/container/FILE\" name=\"EDUA0000003\" encoding=\"UTF-8\"/>\r\n"
				+ "        </xplg:dataSource>\r\n"
				+ "    </ns6:TransportPlugIns>\r\n"
				+ "    <ns6:TransportBody>\r\n"
				+ "        <xcpt:Data>\r\n"
				+ "            <xcpt:ElementSequence>\r\n"
				+ "                <xmsg:dataRequest>\r\n"
				+ "                    <xmsg:Query>\r\n"
				+ "                        <xmsg:Argument property=\"http://www.extra-standard.de/property/ResponseID\" type=\"xs:string\" event=\"http://www.extra-standard.de/event/RequestData\" xmlns=\"\">\r\n"
				+ "                            <xmsg:IN>\r\n"
				+ "                                <xmsg:EQ>23</xmsg:EQ>\r\n"
				+ "                                <xmsg:EQ>24</xmsg:EQ>\r\n"
				+ "                            </xmsg:IN>\r\n"
				+ "                        </xmsg:Argument>\r\n"
				+ "                    </xmsg:Query>\r\n"
				+ "                    <xmsg:Control/>\r\n"
				+ "                </xmsg:dataRequest>\r\n"
				+ "            </xcpt:ElementSequence>\r\n"
				+ "        </xcpt:Data>\r\n" + "    </ns6:TransportBody>\r\n" + "</ns6:xmlTransport>";
		return expectedMessage;
	}

	/**
	 * @return
	 */
	private IDbQueryInputData createTestDummyDBQueryInputData() {
		final DBQueryInputData senderData = new DBQueryInputData();
		senderData.addSingleDBQueryInputData(1L, "STERBEDATENABGLEICH-7777777-1-RESPONSE",
				"STERBEDATENABGLEICH-7777777-1-RESPONSE");
		senderData.addSingleDBQueryInputData(2L, "STERBEDATENABGLEICH-7777778-2-RESPONSE",
				"STERBEDATENABGLEICH-7777778-2-RESPONSE");
		senderData.setRequestId("STERBEDATENABGLEICH_8888888");
		return senderData;
	}

	private ExtraProfileConfiguration createConfigFileBeanForElementSequenseData() {
		final ExtraProfileConfiguration config = extraRequestBuilderITBasic
				.createTransportBodyWithDataConfigFileBean(requiredXmlType);
		config.addElementsHierarchyMap("Data", "xcpt:ElementSequence");
		return config;
	}

}
