# AcknowledgePhase1ResponseProcessPlugin #
**Art:** ResponseProcessPlugin
## Beschreibung ##

Dieses Plugin sollte verwendet werden, wenn der Client Daten an den server sendet und eine Bestätigung verarbeiten muss. Das Plugin wertet die Server-Bestätigung aus. Typischer Einsatz ist ein _Request With Response_ Szenario (Phase).

```
<xcpt:DataType>http://www.extra-standard.de/datatypes/DataSend</xcpt:DataType>
<xcpt:Scenario>http://www.extra-standard.de/scenario/request-with-response</xcpt:Scenario>
```

## Konfiguration ##

```
plugins.responseprocessplugin=acknowledgePhase1ResponseProcessPlugin
```