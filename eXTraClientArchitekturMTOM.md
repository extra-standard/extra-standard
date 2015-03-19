**Inhaltsverzeichnis**



# Einführung #

Derzeit ist eXTra nicht dafür gedacht, dass große Dateien auch versendet und empfangen werden können, da die Dateien als Daten innerhalb der XML-Datei eingefügt werden.

Problembeschreibung und Infos: https://code.google.com/p/extra-standard/issues/detail?id=76

# MTOM Umstellung Hintergrunde und schrittweise Einführung #

MTOM (SOAP Message Transmission Optimization Mechanism) ist ein Standard, der erlaubt die binären Daten effizient und komfortabel zu übertragen.

Wenn die binären Daten als Teil eines XML-Dokuments eingefügt sind, müssen sie Base64 kodiert sein, welches CPU-Zeit und Größe des Nutzlast erhöhen wird.

Wenn MTOM auf einem Service aktiviert ist, nimmt dieser Mechanismus binären Daten, die Teil eines XML-Dokuments sind, und bildet ein Attachment für die diese Daten.


Die Grundlagen der MTOM Übertragung sind in folgenden Quellen beschrieben:
  * http://www.redbooks.ibm.com/redpapers/pdfs/redp4884.pdf (Sending binary data using MTOM. Seite 64, Default mapping. Seite 65)
  * http://www.w3.org/TR/soap12-mtom/#xop-serialization


## Schritte für die Umstellung ##


Um MTOM zu aktivieren, müssen mehrere Schritte durchgeführt werden:

  * Zunächst muss ein bestimmtes Feld im XML-Schema annotiert werden, damit JAXB weiß, dass dieses Feld ein Kandidat für MTOM-Optimierung ist.
  * Danach muss über CXF (Apache Webservice Framework) die Konfiguration für MTOM aktiviert werden.

## Schema Annotieren ##

Vorher

```
    <xs:complexType name="Base64CharSequenceType">
        <xs:simpleContent>
            <xs:extension base="xs:base64Binary"/>
        </xs:simpleContent>
    </xs:complexType>
```


Nachher

```
    <xs:complexType name="Base64CharSequenceType">
        <xs:simpleContent>
            <xs:extension base="xs:base64Binary" xmime:expectedContentTypes="application/octet-stream"/>
        </xs:simpleContent>
    </xs:complexType>
```

Dies informiert JAXB (JAXB nutzt WSDL2Java, um Klassen zu erzeugen), dass dieses Feld alle mögliche Inhalte beinhalten kann.

Daraufhin wird statt eines Byte-Array für den Base64Binary Element ein DataHandler (siehe http://docs.oracle.com/javaee/1.4/api/javax/activation/DataHandler.html) erstellt, um den Datenstrom (Stream) übertragen zu können.



## Client konfigurieren ##

Die Konfigurations des Clients findet über folgende JAXWS XML Konfiguration statt:

```
	<jaxws:client id="extraClientMTOMWS" serviceClass="de.extra_standard.namespace.webservice.Extra" address="${webservice.endpoint.url}">
		<jaxws:properties>
			<entry key="mtom-enabled" value="false" />
		</jaxws:properties>    
	</jaxws:client>     
```


## DataHandler verwenden ##

DataHandlers sind einfach zu bedienen und zu erstellen. DataHandler kann - wie unten beschrieben - konsumiert werden:
```
	final DataHandler dataHandler = base64CharSequence.getValue();
	final InputStream inputStream = dataHandler.getInputStream();
```

Es gibt viele Möglichkeiten, einen DataHandler zu erstellen. Es kann FileDataSource, ByteArrayDataSource vewendet werden. Ansonsten kann DataSource auch für bestimmte Zwecke implementiert werden.
```
	File inputFile = new File ("Pfad zum File");
	final DataSource source = new FileDataSource(inputFile);
```

## Beispiel SOAP Nachrichten ##

  * eXTra-Nachricht mit der Datei als Attachment


```
Encoding: UTF-8
Http-Method: POST
Content-Type: multipart/related; type="application/xop+xml"; boundary="uuid:e44d6a5a-d35a-4c14-ab64-a64fda6bdf12"; start="<root.message@cxf.apache.org>"; start-info="text/xml"
Headers: {Accept=[*/*], SOAPAction=["http://www.extra-standard.de/namespace/webservice/execute"]}
Payload: 

--uuid:e44d6a5a-d35a-4c14-ab64-a64fda6bdf12
Content-Type: application/xop+xml; charset=UTF-8; type="text/xml";
Content-Transfer-Encoding: binary
Content-ID: <root.message@cxf.apache.org>

<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
	<soap:Body>
		<xreq:Transport xmlns:xcpt="http://www.extra-standard.de/namespace/components/1"
			xmlns:xreq="http://www.extra-standard.de/namespace/request/1"
			xmlns:xenc="http://www.w3.org/2001/04/xmlenc#" xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
			xmlns:xlog="http://www.extra-standard.de/namespace/logging/1"
			xmlns:xres="http://www.extra-standard.de/namespace/response/1"
			xmlns:xsrv="http://www.extra-standard.de/namespace/service/1"
			xmlns:xplg="http://www.extra-standard.de/namespace/plugins/1"
			version="1.3" profile="http://code.google.com/p/extra-standard/profile/1">
			<xreq:TransportHeader>
				<xcpt:TestIndicator>http://www.extra-standard.de/test/NONE
				</xcpt:TestIndicator>
				<xcpt:Sender>
					<xcpt:SenderID>ec-1</xcpt:SenderID>
					<xcpt:Name>eXTra-Client</xcpt:Name>
				</xcpt:Sender>
				<xcpt:Receiver>
					<xcpt:ReceiverID>es-1</xcpt:ReceiverID>
					<xcpt:Name>eXTra-Server</xcpt:Name>
				</xcpt:Receiver>
				<xcpt:RequestDetails>
					<xcpt:RequestID>STMELD_AUSL_3</xcpt:RequestID>
					<xcpt:TimeStamp>2013-02-21T11:38:00.854+01:00</xcpt:TimeStamp>
					<xcpt:Application>
						<xcpt:Product>eXTra Client OpenSource</xcpt:Product>
						<xcpt:Manufacturer>OpenSource</xcpt:Manufacturer>
						<xcpt:RegistrationID></xcpt:RegistrationID>
					</xcpt:Application>
					<xcpt:Procedure>http://www.extra-standard.de/procedures/SterbemeldungAusland
					</xcpt:Procedure>
					<xcpt:DataType>http://www.extra-standard.de/datatypes/DataSend
					</xcpt:DataType>
					<xcpt:Scenario>http://www.extra-standard.de/scenario/request-with-acknowledgement
					</xcpt:Scenario>
				</xcpt:RequestDetails>
			</xreq:TransportHeader>
			<xreq:TransportPlugIns>
				<xplg:DataSource />
			</xreq:TransportPlugIns>
			<xreq:TransportBody>
				<xcpt:Data>
					<xcpt:Base64CharSequence>
						<xop:Include xmlns:xop="http://www.w3.org/2004/08/xop/include"
							href="cid:4d303223-8c94-4fb2-80ef-84d7a0d46561-1@www.extra-standard.de" />
					</xcpt:Base64CharSequence>
				</xcpt:Data>
			</xreq:TransportBody>
		</xreq:Transport>
	</soap:Body>
</soap:Envelope>

--uuid:e44d6a5a-d35a-4c14-ab64-a64fda6bdf12
Content-Type: text/plain
Content-Transfer-Encoding: binary
Content-ID: <4d303223-8c94-4fb2-80ef-84d7a0d46561-1@www.extra-standard.de>
Content-Disposition: attachment;name="italienSterbedatenReq1.txt"

Sterbedaten 1 Italien
--uuid:e44d6a5a-d35a-4c14-ab64-a64fda6bdf12--
```

  * eXTra-Nachricht mit der Datei als Inline


```
Address: http://localhost:8088/mockExtraSterbedatenausPhase1
Encoding: UTF-8
Http-Method: POST
Content-Type: text/xml
Headers: {Accept=[*/*], SOAPAction=["http://www.extra-standard.de/namespace/webservice/execute"]}
Payload: 

<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
	<soap:Body>
		<xreq:Transport xmlns:xcpt="http://www.extra-standard.de/namespace/components/1"
			xmlns:xreq="http://www.extra-standard.de/namespace/request/1"
			xmlns:xenc="http://www.w3.org/2001/04/xmlenc#" xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
			xmlns:xlog="http://www.extra-standard.de/namespace/logging/1"
			xmlns:xres="http://www.extra-standard.de/namespace/response/1"
			xmlns:xsrv="http://www.extra-standard.de/namespace/service/1"
			xmlns:xplg="http://www.extra-standard.de/namespace/plugins/1"
			version="1.3" profile="http://code.google.com/p/extra-standard/profile/1">
			<xreq:TransportHeader>
				<xcpt:TestIndicator>http://www.extra-standard.de/test/NONE
				</xcpt:TestIndicator>
				<xcpt:Sender>
					<xcpt:SenderID>ec-1</xcpt:SenderID>
					<xcpt:Name>eXTra-Client</xcpt:Name>
				</xcpt:Sender>
				<xcpt:Receiver>
					<xcpt:ReceiverID>es-1</xcpt:ReceiverID>
					<xcpt:Name>eXTra-Server</xcpt:Name>
				</xcpt:Receiver>
				<xcpt:RequestDetails>
					<xcpt:RequestID>STMELD_AUSL_3</xcpt:RequestID>
					<xcpt:TimeStamp>2013-02-21T11:38:00.854+01:00</xcpt:TimeStamp>
					<xcpt:Application>
						<xcpt:Product>eXTra Client OpenSource</xcpt:Product>
						<xcpt:Manufacturer>OpenSource</xcpt:Manufacturer>
						<xcpt:RegistrationID></xcpt:RegistrationID>
					</xcpt:Application>
					<xcpt:Procedure>http://www.extra-standard.de/procedures/SterbemeldungAusland
					</xcpt:Procedure>
					<xcpt:DataType>http://www.extra-standard.de/datatypes/DataSend
					</xcpt:DataType>
					<xcpt:Scenario>http://www.extra-standard.de/scenario/request-with-acknowledgement
					</xcpt:Scenario>
				</xcpt:RequestDetails>
			</xreq:TransportHeader>
			<xreq:TransportPlugIns>
				<xplg:DataSource />
			</xreq:TransportPlugIns>
			<xreq:TransportBody>
				<xcpt:Data>
					<xcpt:Base64CharSequence>U3RlcmJlZGF0ZW4gMSBJdGFsaWVu
					</xcpt:Base64CharSequence>
				</xcpt:Data>
			</xreq:TransportBody>
		</xreq:Transport>
	</soap:Body>
</soap:Envelope>

```
