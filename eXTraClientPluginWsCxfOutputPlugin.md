# WsCxfOutputPlugin - Kommunikation über Webservice (CXF Framework) #
**Art:** OutputPlugin
## Beschreibung ##

Dieses Plugin steuert den Datenaustausch mit dem eXTra-Server über einen Webservice. Die Umsetzung ist mit CXF Framework erfolgt.
Dieses Plugin unterstützt MTOM technologie

## Konfiguration ##
Duch folgende Einträge in den Konfigurationsdateien wird das Plugin aktiviert und konfiguriert:
```
plugins.outputplugin=wsCxfOutputPlugin
webservice.endpoint.url=http://localhost:8088/mockExtraSterbedatenausPhase2
# default (kein Eintrag in der Konfiguration) false
plugins.outputplugin.wsCxfOutputPlugin.emtom-enabled=true
# default (kein Eintrag in der Konfiguration) false
plugins.outputplugin.wsCxfOutputPlugin.validation=true
```
Das Plugin versendet das eXTra-Anfragedokument an den eXTra-Server und nimmt das Ergebnisdokument entgegen.

## Logging ##

Die SOAP Nachrichten können über Logging-Konfiguration ab und angeschaltet werden.
```
<!--Level INFO schaltet Logging der SOAP-Nachrichten an, WARN ab -->
<logger name="org.apache.cxf.services.ExtraService" level="INFO" />
```