<h1> Demonstration Sterbedatenabgleich (Testumgebung) </h1>

Im folgenden wird gezeigt, wie das Fachverfahren _Sterbedatenabgleich_ in einer Testumgebung (eXTra-Server wird mit soapUI simuliert) durchgeführt werden kann.



## Kurze Beschreibung des Fachverfahrens ##

Das Fachverfahren ist in drei Phasen aufgeteilt:

  * In **Phase 1** werden alle Dateien die sich im Eingabe-Verzeichnis befinden, jeweils einzeln in einer eXTra-Nachricht verpackt und an den eXTra-Server versendet.
  * In **Phase 2** fragt der eXTra-Client den eXTra-Server ob neue Ergebnisdateien vorliegen. Der eXTra-Server übermittelt dem eXTra-Client 0 bis n Ergebnisdateien (mit Erfolgsstatus). Die Ergebnisdateien werden im Ausgabe-Verzeichnis abgelegt.
  * In **Phase 3** bestätigt der eXTra-Client dem eXTra-Server den Empfang der erfolgreich verarbeiteten Dateien. Der Empfang einer fehlerhaft verarbeiteten Datei wird nicht bestätigt

## Fachverfahren einrichten ##

Das Fachverfahren wird wie in der [Betriebsanleitung](eXTraClientBetriebsanleitung.md) beschrieben konfiguriert und eingerichtet. Folgende Verzeichnisstruktur ergibt sich:

```
D:\eXTra
- extra-client
  - bin
  - lib
  - sql
- fachverfahren
  - extra-sterbedaten-ausland
    - bin
    - conf
      - italien
        - phase1
        - phase2
        - phase3
      + luxemburg
    - docs
    - logs
    - soapui
    - sql
    - work
      - italien
        - input
        - reports
        - responses
      + luxemburg
```

Die [eXTra-Client Datenbank](eXTraClientBetriebDatenbank.md) (Oracle XE) ist mit dem Werkzeug _flyway_ aufgebaut worden.

## Server simulieren (soapUI Projekt starten) ##

Im Auslieferungsarchiv des Fachverfahrens befindet sich auch ein soapUI-Projekt mit dem der eXTra-Server simuliert wird. Für die möglichen Server-Responses sind Mock-Services eingerichtet:

![http://extra-standard.googlecode.com/svn/wiki/images/extra-demo-sterbedaten-soapui.png](http://extra-standard.googlecode.com/svn/wiki/images/extra-demo-sterbedaten-soapui.png)

Für jeden Service wird eine Adresse angegeben:

![http://extra-standard.googlecode.com/svn/wiki/images/extra-demo-sterbedaten-soapui-service.png](http://extra-standard.googlecode.com/svn/wiki/images/extra-demo-sterbedaten-soapui-service.png)

Diese Adresse ist in der Konfigurationsdatei _extra-properties-user.properties_ eingetragen:

```
webservice.endpoint.url=http://localhost:8088/mockExtraSterbedatenausPhase1
```

In soapUI werden die benötigten Services gestartet (_ExtraMockServiceSterbedatenausPhase1-3_, jeweils Klick auf grünes Dreieck)

## Durchführung ##
### Phase 1 durchführen ###

In Phase 1 sendet der eXTra-Client alle Dateien aus dem Verzeichnis _work/input_ an den Server. Jede Datei wird in einer eigenen eXTra-Nachricht versendet.

**Aufruf:** bin/italienPhase1.bat

In soapUI wird die Kommunikation für jeden Aufruf protokolliert und läßt sich dort einsehen:

![http://extra-standard.googlecode.com/svn/wiki/images/extra-demo-sterbedaten-soapui-execute.png](http://extra-standard.googlecode.com/svn/wiki/images/extra-demo-sterbedaten-soapui-execute.png)

Jeder eXTra-Client Nachricht ist in der [eXTra-Client Datenbank](eXTraClientBetriebDatenbank.md) ein Eintrag in der Tabelle _EXECUTION_ zugeordnet. Folgendes SQL liefert eine Übersicht über den Kommunikationsablauf:

```
select EX.ID AS "Ex.Id", INP.ID AS "In.ID", EX.PHASE,
 INP.INPUT_IDENTIFIER, INP.REQUEST_ID, INP.RESPONSE_ID, ST.NAME AS
 "Status", INP.RETURN_TEXT from EXECUTION ex, COMMUNICATION_PROTOCOL 
inp, STATUS st where EX.ID = INP.EXECUTION_ID and INP.STATUS_ID = st.ID order by EX.ID, INP.ID;
```

Zusätzlich zur Datenbank-Protokollierung werden pro Fachverfahren-Aufruf zwei Log-Dateien im Verzeichnis _logs_ angelegt. Die erste Log-Datei gibt in komprimierter Form die wichtigsten Ergebnisse wieder z.B.:

```
2012-11-26 16:36:51,561: Start Of Processing.
2012-11-26 16:36:56,030: Webservice Aufruf von: http://localhost:8088/mockExtraSterbedatenausPhase1
2012-11-26 16:36:57,811: Webservice Aufruf von: http://localhost:8088/mockExtraSterbedatenausPhase1
2012-11-26 16:36:57,920: Webservice Aufruf von: http://localhost:8088/mockExtraSterbedatenausPhase1
2012-11-26 16:36:57,983: ExecutionsResults: 
Anzahl verarbeitete Saetze (Serveranfragen): 3
Anzahl erfolgreiche Serverantworten: 3
Anzahl fehlerhafte Serverantworten: 0

Erfolgreich verarbeitete Datensätze: 
...
```

Die zweite Log-Datei (developer) ist wesentlich größer und gibt ausführliche Informationen.

### Phase 2 durchführen ###
Der eXTra-Client erfragt neue Dokumente vom Server.

**Aufruf:** bin/italienPhase2.bat

### Phase 3 durchführen ###
Der eXTra-Client bestätigt die erfolgreich verarbeiteten Dateien.

**Aufruf:** bin/italienPhase3.bat

## Fehlerbehandlung ##

Im soapUI-Projekt sind für die möglichen Fehlerfälle Server-Antworten vorbereitet. Durch Änderung der Webservice-Adresse in der Konfigurations-Datei [extra-properties-user.properties](eXTraClientBetriebConfigUser.md) kann ein fehlerhafter Kommunikationsablauf simuliert werden.