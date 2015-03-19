# DbMaxResponseIdQueryDataPlugin #
**Art:** DataPlugin
## Beschreibung ##

Ermittelt in der Protokollierungs-Tabelle (COMMUNICATION\_PROTOCOL) die zuletzt vom eXTra-Server erhaltene ResponseId (= die größte Id) für die konfigurierte Prozedur und Phase. Als weiteres Suchkriterium kann ein Ausdruck für die Spalte 'SUBQUERY' angegeben werden. In dieser Spalte werden z.B. im Sterbedaten-Verfahren die Länderkennungen von Ergebnisdateien abgelegt.

Dieses Plugin wird in Phase 2 des Verfahrens _Sterbedaten Ausland_ verwendet, um vom Server noch nicht erhaltene Ergebnisdateien zu erfragen. Für eine Länderkennung ermittelt dieses Plugin die zuletzt erhaltene Server-Response-ID.

## Konfiguration ##

```
plugins.dataplugin=dbMaxResponseIdQueryDataPlugin
# Suchanfrage nach der letzten Serverantwort fuer eine Laenderkennung
plugins.dataplugin.dbMaxResponseIdQueryDataPlugin.subquery=IT
```