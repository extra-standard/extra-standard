# Plugins im eXTra-Client #

Der eXTra-Client bietet für verschiedene Aufgabenbereiche unterschiedliche Plugins an. So kann z.B. durch Auswahl eines Plugins gesteuert werden, ob das das erzeugte XML-Dokument im Filesystem abgelegt oder über einen Webservice verschickt wird.

## Produktive Plugins ##
Diese Plugins können produktiv eingesetzt werden.
| **Name** | **Art** | **Kurzbeschreibung** |
|:---------|:--------|:---------------------|
| [defaultConfigPlugin](eXTraClientPluginDefaultConfigPlugin.md) | ConfigPlugin | Verarbeitung der Konfigurationsdateien |
| [dbMaxResponseIdQueryDataPlugin](eXTraClientPluginDbMaxResponseIdQueryDataPlugin.md) | DataPlugin | Ermittelt letzte Server-Antwort für Fachverfahren und Phase |
| [fileDataPlugin](eXTraClientPluginFileDataPlugin.md) | DataPlugin | Liest Dateien aus einem Verzeichnis |
| [dbQueryDataPlugin](eXTraClientPluginDbQueryDataPlugin.md) | DataPlugin | Ermittelt Daten-ID's die dem Server noch bestätigt werden müssen |
| [wsOutputPlugin](eXTraClientPluginWsOutputPlugin.md) | OutputPlugin | Steuert den Datenaustausch mit dem Server über einen Webservice |
| [wsCxfOutputPlugin](eXTraClientPluginWsCxfOutputPlugin.md) | OutputPlugin | Steuert den Datenaustausch mit dem Server über einen Webservice (CXF Framework mit MTOM unterstützung) |
| [acknowledgePhase1ResponseProcessPlugin](eXTraClientPluginAcknowledgePhase1ResponseProcessPlugin.md) | ResponseProcessPlugin | Verarbeitet eine Server-Bestätigung für einen Datensatz (z.B. 'Datei erhalten'). |
| [acknowledgeSingleResponseDataResponseProcessPlugin](eXTraClientPluginAcknowledgeSingleResponseDataResponseProcessPlugin.md) |  ResponseProcessPlugin | Verarbeitet eine Server-Bestätigung für mehrere Datensätze (z.B. 'Bestätigungen erhalten') |
| [fileSystemResponseProcessPlugin](eXTraClientPluginFileSystemResponseProcessPlugin.md) | ResponseProcessPlugin | - |
| [fileSystemResultDataResponseProcessPlugin](eXTraClientPluginFileSystemResultDataResponseProcessPlugin.md) | ResponseProcessPlugin | Liest einen Serverdatensatz aus dem Data-Bereich des TransportBody und legt diesen in einer Datei ab.|
| [fileSystemResultPackageDataResponseProcessPlugin](eXTraClientPluginFileSystemResultPackageDataResponseProcessPlugin.md) | ResponseProcessPlugin | Liest mehrere Serverdatensätze aus den Data-Bereichen von PackageBody Elementen und speichert diese jeweils in einer Datei.|

## Plugins für Test- und Entwicklung ##
Folgende Plugins sind für Test- und Entwicklungszwecke gedacht. Sie simulieren z.B. Serverantworten und werden in Unit-Tests verwendet.
| **Name** | **Art** |
|:---------|:--------|
| dummyConfigPlugin | ConfigPlugin |
| dummyDataPlugin | DataPlugin |
| dummyDataResponceOutputPlugin | OutputPlugin |
| dummyOutputPlugin | OutputPlugin |
| dummyQueryDataResponceOutputPlugin | OutputPlugin |
| dummyResponseProcessPlugin | ResponsePlugin |