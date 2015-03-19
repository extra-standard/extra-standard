**Inhaltsverzeichnis**


# Nachverfolgung des Nachrichtenaustauchs in der Datenbank #

In der eXTra-Datenbank werden die Fachverfahren verwaltet und die Kommunikationsabläufe protokolliert. Anhang der Fachverfahren "Sterbedatenaustausch Ausland" werde folgende Punkte dargestellt:

  * Wie eine Fachverfahren konfiguriert wird.
  * Wie die Nachrichten in der Datenbank verfolgt werden können.

## Konfigurationsbeispiel Anhang des Fachverfahrens Sterbedatenaustausch Ausland ##

Das Datenbankmodell für die Konfiguration ist unter [eXTraClientBetriebDatenbank](eXTraClientBetriebDatenbank.md) dokumentiert. Der Datenaustauch besteht aus drei Schritten. Details siehe [eXTraSterbedatenVerfahren](eXTraSterbedatenVerfahren.md):

  1. PHASE1: Senden der fachlichen Daten an eine eXTra Server
  1. PHASE2: Sender fordert die Verarbeitungsergebnisse des Fachvverfahren an.
  1. PHASE3: Bestätigung der erfolgreich abgeholten Rückmeldungen

Dabei erfolgen die Schritte PHASE2 und PHASE3 abhängig voneinander. Die Bestätigung der abgeholten Rückmeldungen (PHASE3), bezieht sich auf die Nachrichten, die in der PHASE2 abgeholt worden sind und nach der Bestätigung der Fachanwendung.

Konfiguration für das Fachverfahren Sterbedatenabgleich sieht wie folgt aus:

![http://extra-standard.googlecode.com/svn/wiki/images/eXTra-konfiguration-sterbemeldungausland.jpg](http://extra-standard.googlecode.com/svn/wiki/images/eXTra-konfiguration-sterbemeldungausland.jpg)

## Nachverfolgung gesamter PHASEN ##

Die Kommunikation in Rahmen des Fachverfahrens kann mit folgendem SQL-Statement nachverfolgt werden:

```
select MANDATOR.NAME, PROCEDURE.NAME, EXECUTION.PHASE,EXECUTION.ID,
executionStatus.NAME as EXECUTION_STATUS,
COMMUNICATIONPROTOKOLSTATUS.NAME as COMMUNICATION_STATUS,
EXECUTION.START_TIME,
COMMUNICATION_PROTOCOL.INPUT_IDENTIFIER , 
COMMUNICATION_PROTOCOL.INPUT_DATA_QUALIFIER,
COMMUNICATION_PROTOCOL.OUTPUT_IDENTIFIER, 
EXECUTION.END_TIME, 
COMMUNICATION_PROTOCOL.request_id, 
COMMUNICATION_PROTOCOL.response_id,
COMMUNICATION_PROTOCOL.RETURN_CODE, 
COMMUNICATION_PROTOCOL.RETURN_TEXT
from  EXECUTION, COMMUNICATION_PROTOCOL, PROCEDURE, STATUS executionStatus, STATUS communicationProtokolStatus,PROCESS_TRANSITION, MANDATOR
where PROCEDURE.ID = EXECUTION.PROCEDURE_ID  
and MANDATOR.ID = PROCEDURE.MANDATOR_ID
and EXECUTION.ID = COMMUNICATION_PROTOCOL.execution_id
and EXECUTION.PROCEDURE_ID = PROCEDURE.ID
and EXECUTION.LAST_TRANSITION_ID = PROCESS_TRANSITION.ID
and PROCESS_TRANSITION.CURRENT_STATUS_ID=executionStatus.ID 
and COMMUNICATION_PROTOCOL.STATUS_ID=communicationProtokolStatus.ID
and PROCEDURE.NAME = 'SterbemeldungAusland'
order by phase
```

## Nachverfolgung für die PHASE1 ##

Die Kommunikation in Rahmen des Fachverfahrens kann mit folgenden SQL Statement nachverfolgt werden:

```
select MANDATOR.NAME, PROCEDURE.NAME, EXECUTION.PHASE,EXECUTION.ID,
executionStatus.NAME as EXECUTION_STATUS,
COMMUNICATIONPROTOKOLSTATUS.NAME as COMMUNICATION_STATUS,
EXECUTION.START_TIME, EXECUTION.END_TIME, 
COMMUNICATION_PROTOCOL.INPUT_IDENTIFIER , 
COMMUNICATION_PROTOCOL.request_id, 
COMMUNICATION_PROTOCOL.response_id
--, COMMUNICATION_PROTOCOL.INPUT_DATA_QUALIFIER
from  EXECUTION, COMMUNICATION_PROTOCOL, PROCEDURE, STATUS executionStatus, STATUS communicationProtokolStatus,PROCESS_TRANSITION, MANDATOR
where PROCEDURE.ID = EXECUTION.PROCEDURE_ID  
and MANDATOR.ID = PROCEDURE.MANDATOR_ID
and EXECUTION.ID = COMMUNICATION_PROTOCOL.execution_id
and EXECUTION.PROCEDURE_ID = PROCEDURE.ID
and EXECUTION.LAST_TRANSITION_ID = PROCESS_TRANSITION.ID
and PROCESS_TRANSITION.CURRENT_STATUS_ID=executionStatus.ID 
and COMMUNICATION_PROTOCOL.STATUS_ID=communicationProtokolStatus.ID
and EXECUTION.PHASE = 'PHASE1'
and PROCEDURE.NAME = 'SterbemeldungAusland'
and  COMMUNICATION_PROTOCOL.INPUT_DATA_QUALIFIER = 'CONTENT'
```


Die Ergebnisse dieser Abfrage sehen wie folgt aus:


![http://extra-standard.googlecode.com/svn/wiki/images/dblog-sterbedatenausland-phase1.jpg](http://extra-standard.googlecode.com/svn/wiki/images/dblog-sterbedatenausland-phase1.jpg)

Über die Datenbank können folgende Sachverhalte nachvollzogen werden:

  * Wann der Nachrichtenaustausch stattgefunden hat.
  * Welche Daten ausgetauscht wurden (INPUT\_IDENTIFIERT und INPUT\_QUALIFIER).
  * Wie erfolgreich die Ausführung (EXECUTION\_STATUS) sowie der einzelne Datenaustausch (COMMUNICATION\_PROTOKOL\_STATUS) war.


## Nachverfolgung für die PHASE2 ##

Für die PHASE2 können die Ergebnisse mit folgenden Statement abgefragt werden:

```
select PROCEDURE.NAME, EXECUTION.PHASE,EXECUTION.ID, executionStatus.NAME as EXECUTION_STATUS,
 EXECUTION.START_TIME, EXECUTION.END_TIME, 
--COMMUNICATION_PROTOCOL.request_id, 
COMMUNICATION_PROTOCOL.response_id,
--COMMUNICATION_PROTOCOL.subquery
 COMMUNICATION_PROTOCOL.INPUT_IDENTIFIER , 
COMMUNICATION_PROTOCOL.OUTPUT_IDENTIFIER, 
COMMUNICATION_PROTOCOL.INPUT_DATA_QUALIFIER as INPUT_QUALIFIER,
COMMUNICATIONPROTOKOLSTATUS.NAME as COMMUNICATION_STATUS,
COMMUNICATION_PROTOCOL.RETURN_CODE, 
COMMUNICATION_PROTOCOL.RETURN_TEXT
from  EXECUTION, COMMUNICATION_PROTOCOL, PROCEDURE, STATUS executionStatus, STATUS communicationProtokolStatus,PROCESS_TRANSITION
where PROCEDURE.ID = EXECUTION.PROCEDURE_ID  
and EXECUTION.ID = COMMUNICATION_PROTOCOL.execution_id
and EXECUTION.PROCEDURE_ID = PROCEDURE.ID
and EXECUTION.LAST_TRANSITION_ID = PROCESS_TRANSITION.ID
and PROCESS_TRANSITION.CURRENT_STATUS_ID=executionStatus.ID 
and COMMUNICATION_PROTOCOL.STATUS_ID=communicationProtokolStatus.ID
and EXECUTION.PHASE = 'PHASE2'
and PROCEDURE.NAME = 'SterbemeldungAusland'
```


Die Ergebnisse dieser Abfrage sehen wie folgt aus:


![http://extra-standard.googlecode.com/svn/wiki/images/dblog-sterbedatenausland-phase2.jpg](http://extra-standard.googlecode.com/svn/wiki/images/dblog-sterbedatenausland-phase2.jpg)

Über die Datenbank können folgende Punkte nachvolzogen werden:

  * Wann der Nachrichtenaustausch stattgefunden hat.
  * Welche Daten ausgetausch wurden (INPUT\_IDENTIFIERT und INPUT\_QUALIFIER).
  * Wie erfolgreich die Ausführung (EXECUTION\_STATUS) sowie der einzelne Datenaustausch (COMMUNICATION\_PROTOKOL\_STATUS) war.
  * Welche Daten vom Server empfangen werden (OUTPUT\_IDENTIFIER).
  * Mit welchen Parametern die Abfrage nach Daten erfolgte (INPUT\_IDENTIFIERT , INPUT\_DATA\_CRITERIA).


Es sind unterschiedlichen Ergebnisse der Jobausführung möglich:

  1. Abfrage mit der ID (EXECUTION.ID) = 1 hat keine Ergebnisse vom Server geliefert (OUTPUT\_IDENTIFIER ist leer).
  1. Abfragen mit ID's (EXECUTION.ID) 5 und 10 haben von dem Server jeweils drei Dateien abgeholt. In der Spalte OUTPUT\_IDENTIFIER sind die Namen der Dateien aufgelistet.
  1. Abfrage mit dem  ID (EXECUTION.ID) = 11 wurde technisch erfolgreich ausgeführt (Execution\_STATUS=DONE), jedoch fachlich wurde vom Server ein Fehler gemeldet (COMMUNICATION\_STATUS=FAIL). In der Spalte COMMUNICATION\_PROTOCOL.RETURN\_TEXT stehen genaue Angaben zu dieser Servermeldung.
  1. Abfrage mit dem ID (EXECUTION.ID) = 11 hat einen technishen Fehler verursacht, was aus dem (Execution\_STATUS=FAIL) abgeleitet werden kann.

## Nachverfolgung für die PHASE3 ##

Für die PHASE3 sieht SQL Abfrage wie folgt aus:
```
select PROCEDURE.NAME, EXECUTION.PHASE,EXECUTION.ID, executionStatus.NAME as EXECUTION_STATUS,
 EXECUTION.START_TIME, EXECUTION.END_TIME, 
--COMMUNICATION_PROTOCOL.request_id, 
COMMUNICATION_PROTOCOL.response_id,
--COMMUNICATION_PROTOCOL.subquery
 COMMUNICATION_PROTOCOL.INPUT_IDENTIFIER , 
COMMUNICATION_PROTOCOL.OUTPUT_IDENTIFIER, 
COMMUNICATION_PROTOCOL.INPUT_DATA_QUALIFIER as INPUT_QUALIFIER,
COMMUNICATIONPROTOKOLSTATUS.NAME as COMMUNICATION_STATUS,
COMMUNICATION_PROTOCOL.RETURN_CODE, 
COMMUNICATION_PROTOCOL.RETURN_TEXT
from  EXECUTION, COMMUNICATION_PROTOCOL, PROCEDURE, STATUS executionStatus, STATUS communicationProtokolStatus,PROCESS_TRANSITION
where PROCEDURE.ID = EXECUTION.PROCEDURE_ID  
and EXECUTION.ID = COMMUNICATION_PROTOCOL.execution_id
and EXECUTION.PROCEDURE_ID = PROCEDURE.ID
and EXECUTION.LAST_TRANSITION_ID = PROCESS_TRANSITION.ID
and PROCESS_TRANSITION.CURRENT_STATUS_ID=executionStatus.ID 
and COMMUNICATION_PROTOCOL.STATUS_ID=communicationProtokolStatus.ID
and EXECUTION.PHASE = 'PHASE3'
and PROCEDURE.NAME = 'SterbemeldungAusland'
```

Die Ergebnisse dieser Abfrage sehen wie folgt aus:


![http://extra-standard.googlecode.com/svn/wiki/images/dblog-sterbedatenausland-phase3.jpg](http://extra-standard.googlecode.com/svn/wiki/images/dblog-sterbedatenausland-phase3.jpg)

Die Ergebnisse der Ausführung der PHASE3 zeigen, dass die in der PHASE2  abgeholten Daten mit dem INPUT\_IDENTIFIER (RESPONSE\_ID des Servers) 110016, 110017, 110018 erfolgreich von der Fachanwendung verarbeitet worden sind und gegenüber dem Server bestätigt wurden.

## Nachverfolgung für die nicht bestätigte Nachrichten (COMMUNICATION\_PROTOCOL Status WAIT) ##

Die SQL Abfrage für die Identifizierung der nicht bestätigten Nachrichten sieht wie folgt aus:
```
select PROCEDURE.NAME, EXECUTION.PHASE,EXECUTION.ID, executionStatus.NAME as EXECUTION_STATUS,
 EXECUTION.START_TIME, EXECUTION.END_TIME, 
--COMMUNICATION_PROTOCOL.request_id, 
COMMUNICATION_PROTOCOL.response_id,
--COMMUNICATION_PROTOCOL.subquery
 COMMUNICATION_PROTOCOL.INPUT_IDENTIFIER , 
COMMUNICATION_PROTOCOL.OUTPUT_IDENTIFIER, 
COMMUNICATION_PROTOCOL.INPUT_DATA_QUALIFIER as INPUT_QUALIFIER,
COMMUNICATIONPROTOKOLSTATUS.NAME as COMMUNICATION_STATUS,
COMMUNICATION_PROTOCOL.RETURN_CODE, 
COMMUNICATION_PROTOCOL.RETURN_TEXT
from  EXECUTION, COMMUNICATION_PROTOCOL, PROCEDURE, STATUS executionStatus, STATUS communicationProtokolStatus,PROCESS_TRANSITION
where PROCEDURE.ID = EXECUTION.PROCEDURE_ID  
and EXECUTION.ID = COMMUNICATION_PROTOCOL.execution_id
and EXECUTION.PROCEDURE_ID = PROCEDURE.ID
and EXECUTION.LAST_TRANSITION_ID = PROCESS_TRANSITION.ID
and PROCESS_TRANSITION.CURRENT_STATUS_ID=executionStatus.ID 
and COMMUNICATION_PROTOCOL.STATUS_ID=communicationProtokolStatus.ID
and COMMUNICATIONPROTOKOLSTATUS.NAME = 'WAIT'
```