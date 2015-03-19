# Erfolgs- und Fehlerbehandlung (Return Codes) #
Im _ResponseProcessPlugin_ des eXTra-Clients wird das vom Server übertragene XML-Ergebnisdokument ausgewertet und dabei eine Fehlerbehandlung durchgeführt.

## Konfiguration ##
In der Konfigurationsdatei _extra-properties-basic.properties_ kann eine Liste positiver Server-Return Codes angegeben werden:
```
# positive Server Return-Codes
de.extra.client.core.util.ExtraReturnCodeAnalyser.returncodelist=C00,I000,E98
```
Falls im Code Element der Server-Nachricht (siehe http://www.extra-standard.de/upload/pdf/eXtra_Transport_Spezifikation_V130.pdf) einer dieser Werte gefunden wird, wird die Kommunikation als erfolgreich angesehen. Ansonsten als nicht erfolgreich:
```
<Report highestWeight="http://www.extra-standard.de/weight/ERROR">
  <Flag weight="http://www.extra-standard.de/weight/ERROR">
    <Code>X3301-500</Code>
    <Text>Http error 500 was raised</Text>
  </Flag>
</Report>
```

Die Elemente _highestWeight_ und _weight_ werden aktuell nicht ausgewertet.

## eXTra-Client Return Codes ##

Dem aufrufenden Programm gibt der eXTra-Client folgende Return Codes zurück:

| **Code** | **Bedeutung** |
|:---------|:--------------|
| 0 | Erfolg |
| 16 | Warnung |
| 32 | Technischer Fehler |
| 64 | Fachlicher Fehler |