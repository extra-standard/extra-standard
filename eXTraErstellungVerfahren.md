<h1>Vorgehensmodell zur Erstellung neuer Fachverfahren</h1>

Im folgenden wird aus Erfahrungswerten, die bei der Entwicklung der Fachverfahren _Sterbedatenabgleich_ und _Send Fetch_ gewonnen wurden, ein Vorgehensmodell zur Entwicklung neuer Fachverfahren mit dem eXTra-Client abgeleitet. Aktuell sind noch einige manuelle Schritte erforderlich. In einem Ausblick werden mögliche Vereinfachungen angesprochen.

Das Vorgehensmodell im Überblick:

![http://extra-standard.googlecode.com/svn/wiki/images/procedure-creation-model.png](http://extra-standard.googlecode.com/svn/wiki/images/procedure-creation-model.png)

Das Vorgehensmodell besteht aus folgenden Schritten:



## Anforderungen definieren ##

### Kurze Beschreibung des Fachverfahrens ###

Im ersten Schritt sollte das Fachverfahren grob beschrieben werden. Z.B.:

  * Das Verfahren besteht aus drei Phasen.
  * In der ersten Phase werden Dateien an den Server gesendet. Der Server bestätigt den Empfang.
  * In der zweiten Phase werden Ergebnisdateien vom Server abgeholt.
  * In der dritten Phase sendet der Client eine Verarbeitungsbestätigung.

### Abstimmung der Client-Server Kommunikation ###

Der eXTra-Client gibt in wesentlichen Punkten Gestaltungsfreiheit (Z.b. der Frage wo Daten abgelegt werden). Deshalb sollten in diesem Schritt mit dem Server-Betreuer wesentliche Eckpunkte der Kommunikation besprochen und definiert werden. Z.B.:

  * _Wo befinden sich zu sendende Daten?_ (z.B. Base64 kodiert im Data-Element des Transport-Bodys)
  * _Wie stellt der Client eine Anfrage an den Server?_
  * _Wo legt der Server Ergebnisse ab?_ (z.B. in getrennten Packages)
  * _Fehlerbehandlung_ (z.B. Return Codes vereinbaren)

Ideal wäre es, wenn in diesem Schritt auch schon die XML-Request und -Response Dokumente definiert würden. Als Basis für die Dokumente können die im 'docs' Verzeichnis mitgelieferten Dokumente eines Demo-Fachverfahrens genommen werden. Als Beispiel hier der Request Phase 1 des Fachverfahrens Sterbedaten (Senden einer Datei):

```
    <xreq:TransportHeader>
        <xcpt:TestIndicator>http://www.extra-standard.de/test/NONE</xcpt:TestIndicator>
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
            <xcpt:TimeStamp>2012-12-18T15:14:30</xcpt:TimeStamp>
            <xcpt:Application>
                <xcpt:Product>eXTra Klient OpenSource</xcpt:Product>
                <xcpt:Manufacturer>OpenSource</xcpt:Manufacturer>
                <xcpt:RegistrationID/>
            </xcpt:Application>
            <xcpt:Procedure>http://www.extra-standard.de/procedures/SterbemeldungAusland</xcpt:Procedure>
            <xcpt:DataType>http://www.extra-standard.de/datatypes/DataSend</xcpt:DataType>
            <xcpt:Scenario>http://www.extra-standard.de/scenario/request-with-acknowledgement</xcpt:Scenario>
        </xcpt:RequestDetails>
    </xreq:TransportHeader>
    <xreq:TransportPlugIns>
    	<xplg:DataSource> 
           <xplg:DataContainer type="http://www.extra-standard.de/container/FILE" created="2012-12-21T10:20:11.0Z" encoding="I1" name="FIKELA.PRD.DS48300.P0000017.DFF7"/> 
	</xplg:DataSource> 
    </xreq:TransportPlugIns>
    <xreq:TransportBody>
        <xcpt:Data>
            <xcpt:Base64CharSequence>VTNSbGNtSmxaR0YwWlc0Z015QkpkR0ZzYVdWdQ==</xcpt:Base64CharSequence>
        </xcpt:Data>
    </xreq:TransportBody>
```

## Infrastruktur aufbauen ##
Zunächst muss die notwendige Infrastruktur aufgebaut werden. Diese besteht aus:

  * Entwicklungsversion (Verzeichisse)
    * eXTra-Client Auslieferung
    * Auslieferungen der Fachverfahren _extra-sterbedaten-ausland_ und _extra-scenario-sendfetch_ (zu Anschauungszwecken)
    * Einem Ordner für das neue Fachverfahren
  * [eXTra-Client Datenbank](eXTraClientBetriebDatenbank.md). Aktuell wird Oracle als Datenbank unterstützt. Es bietet sich an, das Fachverfahren lokal mit Oracle XE zu entwickeln. (Weitere Datenbankunterstützung ist angedacht.)
  * [soapUI](http://www.soapui.org) (simuliert den eXTra-Server)
  * Java Laufzeitumgebung (Version 6 ist getestet)
  * [flyway](http://flywaydb.org) (Optionales Werkzeug für die Datenbank-Verwaltung)

### Entwicklungsversion (Verzeichisse) ###

Als Basis für neue Fachverfahren empfiehlt es sich, die beiden bestehenden Fachverfahren Sterbedatenabgleich und Send-Fetch als Ausgangsbasis zu verwenden. Insbesondere wenn das neu zu entwickelnde Fachverfahren große Ähnlichkeit mit einem bestehenden aufweist. Durch Auspacken der Auslieferungsversionen sollte folgende Verzeichnis-Grundstruktur aufgebaut werden (siehe [Betriebsanleitung](eXTraClientBetriebsanleitung.md):
```
D:\eXTra
- extra-client
  - bin
  - lib
  - sql
- fachverfahren
  - extra-scenario-sendfetch
    - bin
    - conf
      - phase1
      - phase2
      - phase3
    - docs
    - logs
    - soapui
    - sql
    - work
      - input
      - reports
      - responses
  + extra-sterbedaten-ausland
  - neu
    - bin
    - conf
    - docs
    - logs
    - soapui
    - sql
    - work
```

Exemplarisch ist als Basisverzeichnis _D:\extra_ gewählt. Es kann aber auch jedes andere Verzeichnis genommen werden.

### Datenbank aufbauen ###
Der Aufbau der Datenbank erfolgt über SQL-Skripte. Diese können mit einem Datenbankwerkzeug (z.B. Toad) oder mit dem Open Source-Werkzeug
[flyway](http://flywaydb.org) ausgeführt werden. Flyway hat den Vorteil, das sehr schnell und einfach die Datenbank in einen Urzustand versetzt werden kann.

Im folgenden wird der Aufbau mit flyway gezeigt:

  * Die aktuelle flyway-Version wird von der [Projektseite](http://flywaydb.org) geladen und im Basisverzeichnis (_D:\eXTra_) entpackt.
  * Oracle JDBC Treiber (z.B. ojdbc14-10.2.0.4.0.jar) in D:\extra\flyway\jars ablegen. **Dieser Treiber muss auch in _extra-client\lib_ abgelegt werden.**
  * Die SQL-Skripte aus den jeweiligen SQL-Verzeichnissen werden in das Verzeichnis D:\extra\flyway\sql kopiert. Folgende Struktur ergibt sich:

```
D:\eXTra
+ extra-client
+ fachverfahren
- flyway
  - sql
    - V1__1_0_0_createInitialDatabase.sql
    - V2__1_0_0_createInitialData.sql
    - V3__1_0_0_createInitialData.sql
    - V4__1_0_0_createInitialData.sql
```

In der Datei _flyway\conf\flyway.properties_ müssen noch die Datenbank-Verbindungsparameter eingetragen werden:

```
# Fully qualified classname of the jdbc driver
flyway.driver=oracle.jdbc.OracleDriver
# Jdbc url to use to connect to the database
flyway.url=jdbc:oracle:thin:@localhost:1521:xe
# User to use to connect to the database
flyway.user=flyway
# Password to use to connect to the database (default: <<blank>>)
flyway.password=flyway
```

Zur Erstellung der Datenbank muss jetzt in der Kommandozeile im Verzeichnis _D:\extra\flyway\bin_ der Befehl:

**flyway.cmd migrate**

ausgeführt werden ([flyway Befehlsübersicht](http://flywaydb.org/documentation/commandline/))

### soapUI Projekt ###

Ein wichtiges Werkzeug für die Entwicklung und den Test eines Fachverfahrens ist soapUI. Mit soapUI kann sowohl der eXTra-Server simuliert werden als auch die Kommunikationsabläufe (Requests und Responses in den einzelnen Phasen) verfeinert werden. Die Demo-Fachverfahren enthalten jeweils ein soapUI Projekt (im Ordner _soapUI_) in dem die Requests und Responses abgelegt sind (kann als Basis für die eigene Entwicklung dienen):

![http://extra-standard.googlecode.com/svn/wiki/images/extra-demo-sterbedaten-soapui.png](http://extra-standard.googlecode.com/svn/wiki/images/extra-demo-sterbedaten-soapui.png)

## Konfiguration des Fachverfahren ##

### Datenbankscript für das Fachverfahren ###
Zur Zeit muss ein neues Fachverfahren noch über ein Datenbank-Script im Stammdatenbereich der eXTra-Datenbank registriert werden:

![http://extra-standard.googlecode.com/svn/wiki/images/extra-betrieb-db-stamm.png](http://extra-standard.googlecode.com/svn/wiki/images/extra-betrieb-db-stamm.png)

Beispiel:
  * Der Mandant 'DRV' wird eingetragen
  * Der PROCEDURE\_TYPE 'STERBEMELDUNG' wird angelegt
  * Dieser PROCEDURE\_TYPE besitzt drei Phasen. Phase 2 ist der Vorgänger von Phase 3 (und stellt die Eingabedaten für Phase 3 zur Verfügung).
  * Eine PROCEDURE 'STERBEMELDUNG' wird angelegt

```
--------------------------------------------------------
-- Initiale Datenbankbefuellung Sterbedatenabgleich
-- Oracle Database.
--------------------------------------------------------
Insert into MANDATOR (ID, NAME) Values (seq_mandator_id.nextval, 'DRV');
   
Insert into PROCEDURE_TYPE (ID, NAME)
 Values (seq_procedure_type_id.nextval, 'STERBEMELDUNG');
   
Insert into PROCEDURE_PHASE_CONFIGURATION (ID, PHASE, PROCEDURE_TYPE_ID)
 Values
   (seq_procephase_config_id.nextval,
    'PHASE1', 
    (select id from procedure_type where name = 'STERBEMELDUNG') );
   
Insert into PROCEDURE_PHASE_CONFIGURATION (ID, PHASE, PROCEDURE_TYPE_ID)
 Values
   (seq_procephase_config_id.nextval,
    'PHASE3', 
    (select id from procedure_type where name = 'STERBEMELDUNG') );
   
Insert into PROCEDURE_PHASE_CONFIGURATION
   (ID, PHASE, PROCEDURE_TYPE_ID, NEXT_PHASE_CONFIGURATION_ID)
 Values
   (seq_procephase_config_id.nextval,
    'PHASE2', 
    (select id from procedure_type where name = 'STERBEMELDUNG'),
     (select id from PROCEDURE_PHASE_CONFIGURATION 
         where PROCEDURE_PHASE_CONFIGURATION.PROCEDURE_TYPE_ID =
        (select id from PROCEDURE_TYPE where PROCEDURE_TYPE.NAME = 'STERBEMELDUNG')
        and   PROCEDURE_PHASE_CONFIGURATION.PHASE = 'PHASE3'  ));
     
Insert into PROCEDURE
   (ID, NAME, SHORT_KEY, MANDATOR_ID, PROCEDURE_TYPE_ID)
 Values
   (seq_procedure_id.nextval, 'Sterbemeldung', 'PROC_STERBEMELDUNG', 
   (select id from MANDATOR where name = 'DRV'),
    (select id from PROCEDURE_TYPE where name = 'STERBEMELDUNG'));
```

Für das neue Fachverfahren wird eine SQL-Datei _V5__1\_0\_0\_createInitialData.sql_ im sql-Verzeichnis angelegt. Durch Ausführen dieses Skriptes (z.B. über flyway) wird die Datenbank aktualisiert.

### Profil Dateien erstellen ###
Für jede Phase muss nun eine Profil-Datei erstellt werden. In dieser Datei wird die Client-Response XML-Datei beschrieben. Ausschnitt:

```
<element>
  <Name>xcpt:Transport</Name>
</element>
<element>
  <Name>req:TransportHeader</Name>
  <Elternelement>Transport</Elternelement>
</element>
<element>
  <Name>req:TransportBody</Name>
  <Elternelement>Transport</Elternelement>
</element>
```

Zu den XML-Elementen müssen in den Konfigurationsdateien Plugins und XML-Builder zugeordnet werden.

### Konfigurationsdateien erstellen ###
Für jede Phase müssen die beiden [Konfigurationsdateien](eXTraClientBetriebKonfigurationsdateien.md) [extra-properties-basic.properties](eXTraClientBetriebConfigBasis.md) und [extra-properties-user.properties](eXTraClientBetriebConfigUser.md) erstellt werden (am besten, indem sie aus einem Beispiel kopiert und angepasst werden). Beide Dateien sind in Bereiche unterteilt

#### extra-properties-basic.properties ####

In dieser Datei werden die Bausteine zur Erstellung von Client-XML-Dokumenten und der Verarbeitung von Server-Ergebnissen ausgewählt und konfiguriert.

##### Plugins auswählen #####

```
# -------------
# -- Plugins --
# -------------
plugins.configplugin=defaultConfigPlugin
plugins.dataplugin=fileDataPlugin
plugins.outputplugin=wsOutputPlugin
plugins.responseprocessplugin=acknowledgePhase1ResponseProcessPlugin
plugins.execution=executionPersistenceJpa
```

Von Interesse sind _dataplugin_, _outputplugin_ und _responseprocessplugin_. _configplugin_ und _execution_ besitzen nur eine Implementierung und können nicht geändert werden.

  * Im obigen Beispiel wird die Datenbeschaffung durch das [fileDataPlugin](eXTraClientPluginFileDataPlugin.md) durchgeführt.
  * Die Kommunikation mit dem Server wird von dem [wsOutputPlugin](eXTraClientPluginWsOutputPlugin.md) durchgeführt.
  * Die Server-Ergebnisse werden von dem [acknowledgePhase1ResponseProcessPlugin](eXTraClientPluginAcknowledgePhase1ResponseProcessPlugin.md) verarbeitet

Eine Übersicht mit Dokumentation aller Plugins findet sich [hier](eXTraClientBetriebPlugins.md).
##### XML-Builder auswählen #####

Analog zu den Plugins existiert für die XML-Builder ein eigener Bereich:
```
# -------------------------------
# -- Xml-Builder Konfiguration --
# -------------------------------
```

Für viele Elemente aus der Profil-Datei muss kein XML-Builder gewählt werden, da eine eindeutige Zuordnung existiert (für das Profil-Element gibt es nur einen Builder).

#### extra-properties-user.properties ####

In dieser Datei werden Umgebungsinformationen (DB-Verbindung, Webservice-Adresse und Ein-/Ausgabe Verzeichnisse eingetragen.

### soapUI Projekt überarbeiten ###

Das soapUI-Projekt sollte nun so überarbeitet werden, das für die Client-Requests entsprechende Server-Responses in Form von Mock-Services bereitstehen und gestartet sind.

## Fachverfahren (Phasen) durchführen ##

Im Verzeichnis _bin_ befinden sich ausführbare Batchdateien (für Windows). Die Datei _extra.bat_ ist die Haupt-Batchdatei. Sie braucht nicht angepasst zu werden. Die übrigen Batchdateien (z.B. phase1.bat) bestehen nur aus einer Zeile und rufen die Haupt-Batchdatei auf:

```
extra.bat conf\phase1
```

## Neue Funktionalität in den eXTra-Client einbauen ##

Falls die bestehende Funktionalität zur Realisierung eines Fachverfahrens nicht ausreicht, kann der eXTra-Client (Open Source Projekt!) durch die Entwicklung neuer Plugins oder XML-Builder erweitert werden. Dazu muss die Entwicklungsumgebung auf einem Rechner eingerichtet werden.

## Ausblick ##
Wie beschrieben können mit dem eXTra-Client neue Fachverfahren entwickelt werden. Es sind aber noch einige manuelle Schritte notwendig. Außerdem benötigt man noch einiges an Hintergrundwissen.

Durch einige Änderungen kann das Vorgehensmodell vereinfacht werden.

### Vereinfachung der Konfiguration ###

  * Die Registrierung eines neuen Fachverfahrens sollte ohne Datenbank-script erfolgen.
  * Bei der Konfiguration von Plugins und XML-Buildern gibt es noch Pflichtangaben. Diese sollten nicht mehr notwendig sein.

### Web Anwendung ###
Über eine Webanwendung könnte ein sehr mächtiges und einfach zu bedienendes Werkzeug für die Erstellung neuer Fachverfahren angeboten werden.

  * In einer Web-Anwendung könnten grob die Grundzüge des neuen Fachverfahrens definiert werden. (z.B. '3 Phasen', Senden - Holen - Bestätigen)
  * Mit einem Assistenten wird man durch die weitere Konfiguration geführt (z.b. Datenablage in: ..., Serverdaten werden erwartet in: ...)
  * Das über die Webanwendung konfigurierte Fachverfahren kann gespeichert werden und per Knopfdruck als lauffähiges Fachverfahren exportiert werden.