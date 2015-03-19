# DataPlugin Architektur und API (Application Programming Interface) #

Das Data-Plugin versorgt die Applikation mit den entsprechenden Nutzdaten, die transportiert werden sollen. Es sind unterschiedlichen Implementierungen des Data-Plugins sowie unterscheidlichen Arten der Data-Objekte denkbar.

Im folgenden Diagramm ist eine technische Zusammensetzung der Dataobjekten zusammengefasst.

![http://extra-standard.googlecode.com/svn/wiki/images/input-data-plugin.png](http://extra-standard.googlecode.com/svn/wiki/images/input-data-plugin.png)

Hier sind 3 Arten von Inputdaten abgebildet:

| ContentInputData | Nutzdaten, Z.B. eine Datei|
|:-----------------|:--------------------------|
| DBQueryInputData | Ergebnisse einer Datenbankanfrage. |
| CriteriaQueryInputData | Informationen zum Erstellen einer Anfrage an den eXTra-Server (z.B. Operator '=', Procedure 'Sterbemeldung'). |

  * DBQueryInputData
  * CriteriaQueryInputData

Content Input Data bildet reinen Nutzdaten sowie dazugehörigen Steuerungsinformationen wie Plugins und RequestId.

Bei der DBQuery InputData handelt es sich um die Query zur Abruf der Daten