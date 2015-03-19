# WsOutputPlugin - Kommunikation über Webservice #
**Art:** OutputPlugin
## Beschreibung ##

Dieses Plugin steuert den Datenaustausch mit dem eXTra-Server über einen Webservice.

## Konfiguration ##
Duch folgende Einträge in den Konfigurationsdateien wird das Plugin aktiviert und konfiguriert:
```
plugins.outputplugin=wsOutputPlugin
webservice.endpoint.url=http://localhost:8088/mockExtraSterbedatenausPhase2
```
Das Plugin versendet das eXTra-Anfragedokument an den eXTra-Server und nimmt das Ergebnisdokument entgegen.