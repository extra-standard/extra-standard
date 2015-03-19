<h1>Konfiguration Fachverfahren</h1>
Im folgenden wird beschrieben, wie mit dem eXTra-Client ein Fachverfahren konfiguriert wird. Ergänzend zur Auslieferung des eXTra-Clients werden vorkonfigurierte Fachverfahren in eigenen Verzeichnissen ausgeliefert. Die Verzeichnisstruktur ist dabei stets gleich.

Exemplarisch wird die Konfiguration am Fachverfahren _eXTra Szenario Send-Fetch (Sende-Hole Betrieb)_ gezeigt. Dieses Fachverfahren ist ein technisches Referenzverfahren, das die Arbeit mit dem eXTra-Client verdeutlichen soll. Dieses Szenario kann als Ausgangspunkt für die Realisierung eigener Fachverfahren verwendet werden. Für die Ausführung wird eine Version des eXTra Clients benötigt.

Das Verfahren _Send-Fetch_ arbeitet in drei Phasen:
  * In **Phase 1** werden 0 bis n Dateien an den den Server versendet (pro Datei eine eXTra-Nachricht)
  * In **Phase 2** fragt der Server für jede in Phase 1 versendete Datei das Ergebnis vom Server ab. Die Ergebnisse werden jeweils als Datei in einem Verzeichnis abgelegt.
  * In **Phase 3** bestätigt der Client dem Server die empfangenen Ergebnisse.

Im folgenden wird beschrieben, wie das Szenario Send-Fetch auf einem Windows-Rechner eingerichtet werden kann (Anpassung an andere Betriebssysteme (z.B. Linux) mit überschaubarem Aufwand möglich). Folgende Schritte müssen durchgeführt werden:



## Auslieferungsarchive auspacken ##

Im ersten Schritt müssen die beiden Auslieferungsarchive (extra-cli-1.0.0-M1.zip, extra-scenario-sendfetch-1.0.0-M1.zip) ausgepackt werden:

| **Schritt** | **Datei/Ordner** | **Aktion** |
|:------------|:-----------------|:-----------|
| 1 | D:\eXTra  | Verzeichnis anlegen |
| 2 | D:\eXTra\fachverfahren | Verzeichnis anlegen |
| 3 | extra-cli-_Version_.zip | Auspacken in: D:\eXTra |
| 4 | extra-scenario-sendfetch-_Version_.zip | Auspacken in: D:\eXTra\fachverfahren |

Folgende Struktur ergibt sich:

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
```

### Verzeichnisbeschreibung ###

Im folgenden werden die Verzeichnisse des Verfahrens Send-Fetch beschrieben (unter fachverfahren/extra-scenario-sendfetch):
| **Verzeichnis** | **Beschreibung** |
|:----------------|:-----------------|
| /bin | Hier befinden sich ausführbare Batch-Dateien (Windows) für die Phasen 1 bis 3 (phase1.bat, phase2.bat, phase3.bat). |
| /conf | Die Konfigurationsdateien sind für jede Phase in einem eigenen Verzeichnis abgelegt (/conf/phase1, /conf/phase2, /conf/phase3). |
| /docs | Exemplarisch sind hier für jede Phase die XML-Kommunikationsdokumente abgelegt. |
| /logs | Ausgabeort für Logdateien die während der Programmausführung erzeugt werden. |
| /soapui | soapUI Demoprojekt. Mit diesem Projekt kann ein eXTra-Server simuliert werden. |
| /sql | Datenbankskripte, mit denen die Initial-Datenbankbefüllung für das Szenario Send-Fetch erzeugt wird. |
| /work/input | In diesem Verzeichnis werden Eingabedateien erwartet. |
| /work/reports | Ausgabeverzeichnis für Reports. |
| /work/responses | Ergebnisdateien werden hier abgelegt. |

## Datenbank anlegen ##
Im ersten Schritt muss die eXTra-Client Datenbank angelegt werden. Aktuell wird nur Oracle unterstützt. In den Konfigurationsdateien wird eine lokale Oracle Datenbank mit _Username/Passwort = extracli_ erwartet (anpassbar).
Mit folgenden SQL-Skripten wird diese Datenbank initial aufgebaut und befüllt:

| **Skript** | **Beschreibung** |
|:-----------|:-----------------|
| extra-client \ sql \ V1 createInitialDatabase.sql | Initiale Datenbankerzeugung (Tabellen)|
| extra-client \ sql \ V2 createInitialData.sql | Initiale Datenbefüllung |
| extra-scenario-sendfetch \ sql \ V3 createInitialData.sql | Initiale Datenbefüllung für das Szenario Send-Fetch |

Die Skripte können mit einem DB-Administrierungswerkzeug oder mit Flyway (http://code.google.com/p/flyway/) ausgeführt werden.

## Datenbankverbindung eintragen ##

Für jede Phase muss die Datenbankverbindung in der Datei _conf/phase.../extra-properties-user.properties_ eingestellt werden. Es müssen die JDBC Verbindungsparameter eingetragen werden. Voreingestellt ist eine lokale Oracle Express Datenbank:

```
plugins.execution.executionPersistenceJpa.hibernate.generateDdl=false
plugins.execution.executionPersistenceJpa.hibernate.database=ORACLE
plugins.execution.executionPersistenceJpa.database.driver_class=oracle.jdbc.OracleDriver
plugins.execution.executionPersistenceJpa.database.connect_url=jdbc:oracle:thin:@localhost:1521:xe
plugins.execution.executionPersistenceJpa.database.username=extracli
plugins.execution.executionPersistenceJpa.database.password=extracli
```

Zusätzlich muss im Verzeichnis _extra-client/lib_ der Oracle JDBC Treiber (z.B. ojdbc14-10.2.0.4.0.jar) abgelegt werden (dieser Treiber befindet sich nicht in der Auslieferung).

## Szenario durchführen ##

Im Verzeichnis 'extra-scenario-sendfetch/bin' befindet sich für jede Phase eine ausführbare Batchdatei. Im Verzeichnis 'logs' wird für jede Ausführung ein Ausführungsprotokoll erzeugt.

### Anpassungen ###

Für jede Phase des Szenario-Send-Fetch gibt es ein eigenes Konfigurationsverzeichnis in dem Anpassungen vorgenommen werden können. So kann z.B. konfigurativ ein eXTra-Server eingetragen werden, der über einen Webservice aufgerufen wird. In jedem dieser Konfigurationsverzeichnis (conf/phase1, ...) befinden sich diese Dateien:

| **Datei** | **Beschreibung** |
|:----------|:-----------------|
| extra-properties-basic.properties | Allgemeine Konfigurationseinstellungen |
| extra-properties-user.properties | Spezifische Einstellungen (Datenbankverbindung, Eingabedateien, ...)|
| logging-config.xml | Logging Konfiguration |
| phase...profil.xml | eXTra Profil-Datei |

### Weitere Fachverfahren konfigurieren ###
Die Konfiguration weiterer realer Fachverfahren erfolgt analog zu dem vorgestellten Beispielsverfahren. Das Fachverfahren _Sterbedatenabgleich Ausland_ besitzt z.b. die gleiche Verzeichnisstruktur.