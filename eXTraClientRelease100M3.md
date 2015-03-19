# eXTra Client CLI und Fachverfahren Scenario Send Fetch sowie Fachverfahren Sterbedaten-Ausland Italien und Luxemburg Version 1.0.0-M3 #

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



# Datenbank-Unterstützung #
  * Oracle XE oder Oracle 11

# Installationsanleitung #

Die Auslieferung besteht aus diesen Archiven:

| **Archiv** | **Beschreibung** |
|:-----------|:-----------------|
| extra-cli-1.0.0-M3.zip | Ausführbarer Basisbereich des eXTra-Clients. |
| extra-scenario-sendfetch-1.0.0-M3.zip | Technisches Referenzverfahren 'Send-Fetch'. Dieses dient als exemplarisches Einsatzbeispiel des eXTra-Clients. Aufbauend auf diesem 'Muster' können weitere Fachverfahren entwickelt werden. |
| extra-sterbedaten-ausland-1.0.0-M3.zip | Fachverfahren [Sterbedaten Ausland](eXTraSterbedatenVerfahren.md). |

Jedes Fachverfahren wird in einem gesonderten Archiv ausgeliefert.
Auf der Seite [eXTraClientBetriebSzenarioSendFetch](eXTraClientBetriebSzenarioSendFetch.md) wird beschrieben wie eine Installation des eXTra-Clients und des Technischem Referenzverfahrens 'Send-Fetch' durchgeführt werden kann.

# Bis jetzt erledigte Aufgaben für die Version 1.0.0 #
Siehe bitte: http://goo.gl/naokO