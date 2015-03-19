# FileDataPlugin #
**Art:** DataPlugin
## Beschreibung ##
Liest aus einem Verzeichnis Dateien aus. Der Dateiinhalt wird anschließend von einem XML-Builder verpackt.

Wird in Phase 1 des Verfahrens _Send Fetch_ eingesetzt um aus einem Verzeichnis die Eingabedateien zu lesen.
## Konfiguration ##
```
plugins.dataplugin=fileDataPlugin
# Maximale Anzahl von Dateien, die in einer Nachricht verpackt werden.
plugins.dataplugin.fileDataPlugin.inputDataLimit=1
```