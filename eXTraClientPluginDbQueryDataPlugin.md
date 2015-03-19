# DbQueryDataPlugin #
**Art:** DataPlugin
## Beschreibung ##
Sucht in der Protokollierungs-DB alle Server-Response-ID's, die dem Server in der anstehenden Phase übermittelt werden sollen.

Wird in Phase 3 des Verfahrens _Send Fetch_ verwendet, um alle zu bestätigenden Ergebnisdokumente (bzw. deren ID's) zu ermitteln.
## Konfiguration ##

```
plugins.dataplugin=dbQueryDataPlugin
# Maximale Anzahl von Verarbeitungs-Objekten die dem Server in einer Nachricht gesendet werden
plugins.dataplugin.dbQueryDataPlugin.inputDataLimit=100
# Wenn value True ist, werden nicht erfolgreich abgeschlosene Phasen wiederholt. 
# Z.B. in der Phase 3 werden Bestätigungen erneut an den Server geliefert. Default Wert false
plugins.dataplugin.dbQueryDataPlugin.resendFailed=true
```