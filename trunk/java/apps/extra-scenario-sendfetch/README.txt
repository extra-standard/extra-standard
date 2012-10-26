----------------------------------------------
eXTRa Szenario Send-Fetch (Sende-Hole Betrieb)
Version: 0.8
----------------------------------------------

In diesem Archiv  befinden sich die Konfigurationsdateien fuer das technische Szenario 'Send-Fetch'. 
Dieses Szenario kann als Ausgangspunkt für die Realisierung von Fachverfahren verwendet werden.
Für die Ausführung wird eine Version des eXTra Clients benoetigt.

--------------------------------
Verzeichnisbeschreibung
--------------------------------
/bin
Hier befinden sich ausführbare Batch-Dateien (Windows) für die Phasen 1 bis 3 (phase1.bat, phase2.bat, phase3.bat). 

/conf/phase1, /conf/phase2, /conf/phase3
Die Konfigurationsdateien sind für jede Phase in einem eigenen Verzeichnis abgelegt. In jedem dieser Verzeichnisse
befinden sich diese Dateien:
- extra-properties-basic.properties : Allgemeine Konfigurationseinstellungen
- extra-properties-user.properties  : Spezifische Einstellungen (Datenbankverbindung, Eingabedateien, ...)
- logging-config.xml                : Logging Konfiguration
- phase...profil.xml                : eXTra Profil-Datei.
 
/work/input
In diesem Verzeichnis werden Eingabedateien erwartet.

/work/reports
Ausgabeverzeichnis für Reports.

/work/responses
Ergebnisdateien werden hier abgelegt.

/logs
Ausgabeort für Logdateien die während der Programmausführung erzeugt werden.

--------------------------------
Ausführbare Version erstellen
--------------------------------

- eXTra Client (Archiv: extra-cli-1.0.0-SNAPSHOT.zip) auspacken, z.B. D:\extra-client
- Parallel dazu, in einem Verzeichnis (z.B. fachverfahren) dieses Archiv (extra-scenario-sendfetch-1.0.0-SNAPSHOT.zip) auspacken
Folgende Struktur ergibt sich:

D:
- extra-client
  - bin
  - lib
- fachverfahren
  - extra-scenario-sendfetch
    - bin
    - conf
      - phase1
      - phase2
      - phase3
    - logs
    - work
      - input
      - reports
      - responses
      
- Die eXTra Client Datenbank muss angelegt und initial befüllt werden (siehe WIKI: https://code.google.com/p/extra-standard/wiki/eXTraEinfuehrung?tm=6)
- Die DB-Verbindung muss in den Dateien 'extra-properties-user.properties' (in den Verzeichnissen conf/phase1, conf/phase2, conf/phase3) angegeben werden.
- Aufruf des eXTra Clients: Im Verzeichnis 'extra-scenario-sendfetch/bin' befindet sich für jede Phase eine ausführbare Batchdatei.   

--------------------------------
Zusatzinformationen
--------------------------------
Für die Anwendungsarchitektur des extra-cli Programms steht folgende Seite 
als Information zur Verfügung:
http://code.google.com/p/extra-standard/wiki/eXTraArchitekturAnwendung

Für den Betrieb des extra-cli Programms steht folgende Seite 
als Information zur Verfügung:
http://code.google.com/p/extra-standard/wiki/eXTraClientBetriebsanleitung
