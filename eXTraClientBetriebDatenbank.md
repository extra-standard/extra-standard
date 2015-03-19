# Datenbank für den eXTra-Client #

Für den Betrieb benötigt der eXTra Client eine Datenbank. In dieser Datenbank werden die Fachverfahren verwaltet und die Kommunikationsabläufe protokolliert.

Das Datenbank-Schema läßt sich in die beiden Bereich _Stammdaten_ und _Ausführung_ aufteilen_:_

## Stammdaten ##
  * Einem _MANDATOR_ können mehrere _PROCEDURE_ zugeordnet werden
  * Eine _PROCEDURE_ besitzt einen _PROCEDURE\_TYPE_
  * Ein _PROCEDURE\_TYPE_ enthält mehrere _PROCEDURE\_PHASE\_CONFIGURATION_
  * Eine _PROCEDURE\_PHASE\_CONFIGURATION_ kann einen Nachfolger besitzen
![http://extra-standard.googlecode.com/svn/wiki/images/extra-betrieb-db-stamm.png](http://extra-standard.googlecode.com/svn/wiki/images/extra-betrieb-db-stamm.png)

### Beispiel einer Konfiguration ###

Konfiguration für die Procedure Sterbedatenabgleich sieht wie folgt aus:

![http://extra-standard.googlecode.com/svn/wiki/images/eXTra-konfiguration-sterbemeldungausland.jpg](http://extra-standard.googlecode.com/svn/wiki/images/eXTra-konfiguration-sterbemeldungausland.jpg)

Damit wird festgehalten, dass die Procedure aus 3 Phasen besteht. Die Phasen werden mit der Phase1  , Phase 2 und Phase 3 benannt.
Die Phase3 folgt der Phase 2.



## Ausführung ##
  * Eine _EXECUTION_ ist einer _PROCEDURE_ zugeordnet.
    * Eine Execution trägt Daten einer Programmausführung.
  * Jede _EXECUTION_ sind mehrere _COMMUNICATION\_PROTOCOL_ Einträge zugeordnet
    * In Laufe einer Execution können mehrere Dateien zwischen Systemen ausgetauscht werden. Die Daten einer Austausch werden in der Tabelle COMMUNICATION\_PROTOCOL festgehalten. Dazugehören RequestId und ResponseId, ReturnCodes, Identifier sowie einen Hashcode bei der Dateien.

![http://extra-standard.googlecode.com/svn/wiki/images/extra-betrieb-db-execution.png](http://extra-standard.googlecode.com/svn/wiki/images/extra-betrieb-db-execution.png)