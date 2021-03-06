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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-11/13/2008 12:46 PM(foreman)-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.08.30 at 03:14:38 PM CEST 
//
package de.extra.client.plugins.dataplugin.auftragssatz;

import java.math.BigInteger;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the
 * de.extra.client.plugins.dataPlugin.auftragssatz package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _DsName_QNAME = new QName("", "dsName");
	private final static QName _DsCreateDate_QNAME = new QName("",
			"dsCreateDate");
	private final static QName _CompressionInfo_QNAME = new QName("",
			"compressionInfo");
	private final static QName _DataSourceInfo_QNAME = new QName("",
			"dataSourceInfo");
	private final static QName _SpecName_QNAME = new QName("", "specName");
	private final static QName _Order_QNAME = new QName("", "order");
	private final static QName _AlgoVersion_QNAME = new QName("", "algoVersion");
	private final static QName _SpecUrl_QNAME = new QName("", "specUrl");
	private final static QName _AlgoName_QNAME = new QName("", "algoName");
	private final static QName _OutputSize_QNAME = new QName("", "outputSize");
	private final static QName _DsEncoding_QNAME = new QName("", "dsEncoding");
	private final static QName _EncryptionInfo_QNAME = new QName("",
			"encryptionInfo");
	private final static QName _DsType_QNAME = new QName("", "dsType");
	private final static QName _InputSize_QNAME = new QName("", "inputSize");
	private final static QName _AlgoId_QNAME = new QName("", "algoId");
	private final static QName _SpecVers_QNAME = new QName("", "specVers");
	private final static QName _Auftragssatz_QNAME = new QName("",
			"Auftragssatz");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package:
	 * de.extra.client.plugins.dataPlugin.auftragssatz
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link EncryptionInfoType }
	 * 
	 */
	public EncryptionInfoType createEncryptionInfoType() {
		return new EncryptionInfoType();
	}

	/**
	 * Create an instance of {@link CompressionInfoType }
	 * 
	 */
	public CompressionInfoType createCompressionInfoType() {
		return new CompressionInfoType();
	}

	/**
	 * Create an instance of {@link AuftragssatzType }
	 * 
	 */
	public AuftragssatzType createAuftragssatzType() {
		return new AuftragssatzType();
	}

	/**
	 * Create an instance of {@link DataSourceInfoType }
	 * 
	 */
	public DataSourceInfoType createDataSourceInfoType() {
		return new DataSourceInfoType();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "dsName")
	public JAXBElement<String> createDsName(String value) {
		return new JAXBElement<String>(_DsName_QNAME, String.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link XMLGregorianCalendar }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "dsCreateDate")
	public JAXBElement<XMLGregorianCalendar> createDsCreateDate(
			XMLGregorianCalendar value) {
		return new JAXBElement<XMLGregorianCalendar>(_DsCreateDate_QNAME,
				XMLGregorianCalendar.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link CompressionInfoType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "compressionInfo")
	public JAXBElement<CompressionInfoType> createCompressionInfo(
			CompressionInfoType value) {
		return new JAXBElement<CompressionInfoType>(_CompressionInfo_QNAME,
				CompressionInfoType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link DataSourceInfoType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "dataSourceInfo")
	public JAXBElement<DataSourceInfoType> createDataSourceInfo(
			DataSourceInfoType value) {
		return new JAXBElement<DataSourceInfoType>(_DataSourceInfo_QNAME,
				DataSourceInfoType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "specName")
	public JAXBElement<String> createSpecName(String value) {
		return new JAXBElement<String>(_SpecName_QNAME, String.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "order")
	public JAXBElement<BigInteger> createOrder(BigInteger value) {
		return new JAXBElement<BigInteger>(_Order_QNAME, BigInteger.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "algoVersion")
	public JAXBElement<String> createAlgoVersion(String value) {
		return new JAXBElement<String>(_AlgoVersion_QNAME, String.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "specUrl")
	public JAXBElement<String> createSpecUrl(String value) {
		return new JAXBElement<String>(_SpecUrl_QNAME, String.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "algoName")
	public JAXBElement<String> createAlgoName(String value) {
		return new JAXBElement<String>(_AlgoName_QNAME, String.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "outputSize")
	public JAXBElement<BigInteger> createOutputSize(BigInteger value) {
		return new JAXBElement<BigInteger>(_OutputSize_QNAME, BigInteger.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "dsEncoding")
	public JAXBElement<String> createDsEncoding(String value) {
		return new JAXBElement<String>(_DsEncoding_QNAME, String.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link EncryptionInfoType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "encryptionInfo")
	public JAXBElement<EncryptionInfoType> createEncryptionInfo(
			EncryptionInfoType value) {
		return new JAXBElement<EncryptionInfoType>(_EncryptionInfo_QNAME,
				EncryptionInfoType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "dsType")
	public JAXBElement<String> createDsType(String value) {
		return new JAXBElement<String>(_DsType_QNAME, String.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "inputSize")
	public JAXBElement<BigInteger> createInputSize(BigInteger value) {
		return new JAXBElement<BigInteger>(_InputSize_QNAME, BigInteger.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "algoId")
	public JAXBElement<String> createAlgoId(String value) {
		return new JAXBElement<String>(_AlgoId_QNAME, String.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "specVers")
	public JAXBElement<String> createSpecVers(String value) {
		return new JAXBElement<String>(_SpecVers_QNAME, String.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link AuftragssatzType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "Auftragssatz")
	public JAXBElement<AuftragssatzType> createAuftragssatz(
			AuftragssatzType value) {
		return new JAXBElement<AuftragssatzType>(_Auftragssatz_QNAME,
				AuftragssatzType.class, null, value);
	}
}
