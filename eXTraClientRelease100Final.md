# eXTra Client CLI und Fachverfahren Scenario Send Fetch sowie Fachverfahren Sterbedaten-Ausland Italien und Luxemburg Version 1.0.0 FINAL #

**Inhaltsverzeichnis**


# Konformität zur eXTra-Spezifikation #
  * Unterstützte Spezifikationsversion: 1.3
  * Unterstützte Szenarien: Sende-/Holbetrieb mit drei Phasen

# eXTra-Builder für das Schreiben von XMLSegments des eXTra-Transports #
  * TransportHeader (xreq:TransportHeader)
    * TestIndicator (xcpt:TestIndicator)
    * Sender (xcpt:Sender)
    * Receiver (xcpt:Receiver)
    * RequestDetail (xcpt:RequestDetails)
  * TransportPlugins
    * DataSource (xplg:DataSource)
      * DataContainer (xplg:DataContainer)
      * DataTransform (xplg:DataTransforms)
    * Contact (xplg:Contacts)
  * TransportBody (xreq:TransportBody)
    * Data (xcpt:Data)
      * Base64CharSequense(xcpt:Base64CharSequence)
      * ElementSequence (xcpt:ElementSequence)
    * Package (xres:Package)
      * PackageHeader (xres:PackageHeader)
        * TestIndicator	(xcpt:TestIndicator)
        * Sender (xcpt:Sender)
        * Receiver (xcpt:Receiver)
        * RequestDetail (xcpt:RequestDetails)
      * PackageBody (xres:PackageBody)

# eXTra-Builder für das Schreiben von eXTra Standard-Nachrichten #
  * DataRequest (xmsg:dataRequest)
  * ConfirmationOfReceipt (xmsg:confirmationOfReceipt)

# Bekannte Einschränkungen #
  * Es kann keine sehr große Datei in einem eXTra-Briefumschlag verschickt werden, da die Datei als String innerhalb der XML-Datei eingefügt wird. Erklärung zu diesen Einschränkungen siehe:
    * https://code.google.com/p/extra-standard/wiki/eXTraSpecIssues
    * https://code.google.com/p/extra-standard/wiki/eXTraClientArchitekturMTOM
    * Issue: https://code.google.com/p/extra-standard/issues/detail?id=76

# Datenbank-Unterstützung #
  * Oracle XE oder Oracle 11
  * HSQLDB (nicht produktionsgeeignet)

# Installationsanleitung #

Die Auslieferung besteht aus diesen Archiven:

| **Archiv** | **Beschreibung** |
|:-----------|:-----------------|
| extra-cli-1.0.0.zip | Ausführbarer Basisbereich des eXTra-Clients. |
| extra-scenario-sendfetch-1.0.0.zip | Technisches Referenzverfahren 'Send-Fetch'. Dieses dient als exemplarisches Einsatzbeispiel des eXTra-Clients. Aufbauend auf diesem 'Muster' können weitere Fachverfahren entwickelt werden. |
| extra-sterbedaten-ausland-1.0.0.zip | Fachverfahren [Sterbedaten Ausland](eXTraSterbedatenVerfahren.md). |

Jedes Fachverfahren wird in einem gesonderten Archiv ausgeliefert.
Auf der Seite [eXTraClientBetriebSzenarioSendFetch](eXTraClientBetriebSzenarioSendFetch.md) wird beschrieben wie eine Installation des eXTra-Clients und des Technischem Referenzverfahrens 'Send-Fetch' durchgeführt werden kann.


# OSS-Sonatype und Maven-Central #

  * Jede eXTra-SNAPSHOT-Versionen (Zip, Jar, usw.) stehen unter [OSS-Sonatype-Maven-Repository](https://oss.sonatype.org/index.html#nexus-search;quick~extra-standard) zur Verfügung.
  * Ab Final-Release stehen sämtliche eXTra-Dateien (Zip, Jar, usw.) direkt unter [Maven-Central-Repository](http://search.maven.org) zur Verfügung.

# Bis jetzt erledigte Aufgaben für die Version 1.0.0 #
Siehe bitte: http://goo.gl/naokO