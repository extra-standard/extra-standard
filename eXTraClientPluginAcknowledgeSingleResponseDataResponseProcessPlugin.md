# AcknowledgeSingleResponseDataResponseProcessPlugin #
**Art:** ResponseProcessPlugin
## Beschreibung ##

In der Server-Anwort wird nur ein Response-Element eerwartet, das für alle in der Anfrage übermittelten Elemente als Antwort gilt.
Kann zum Beispiel eingesetzt werden, wenn der Client den Empfang mehrerer Dateien bestätigt und der Server in einer Antwort die Bestätigung wiederum bestätigt (_ConfirmationOfReceipt)._

Wird in Phase 3 des Verfahrens _Send Fetch_ verwendet.

## Konfiguration ##

```
plugins.responseprocessplugin=acknowledgeSingleResponseDataResponseProcessPlugin
```