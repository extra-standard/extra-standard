# eXTra Client CLI und Fachverfahren Scenario Send Fetch sowie Fachverfahren Sterbedaten-Ausland Italien und Luxemburg Version 2.2.0 FINAL #

**Inhaltsverzeichnis**


# Konformität zur eXTra-Spezifikation #
  * Unterstützte Spezifikationsversion: 1.3.1
  * Unterstützte Szenarien: Sende-/Holbetrieb mit drei Phasen

# What's New #

### Versand größer Datei bzw. MTOM-Unterstützung ###
  * Siehe: [eXTraClientArchitekturMTOM](eXTraClientArchitekturMTOM.md)

### Optimierung der Konfigurationsmöglichkeiten ###
  * In dieser Version wird eine globale und mandantenspezifische Konfiguration eingeführt ([issue 84](https://code.google.com/p/extra-standard/issues/detail?id=84))
  * Dokumentation siehe [eXTraClientBetriebsanleitung](eXTraClientBetriebsanleitung.md)

### Verschlüssellung von Passwörtern für den Zugriff auf die Datenbank ###
  * In dieser Version kann das Password in der Properties-Datei verschlüsselt werden ([issue 53](https://code.google.com/p/extra-standard/issues/detail?id=53)).
  * Dokumentation siehe [eXTraClientBetriebsanleitung](eXTraClientBetriebsanleitung.md)

# Bekannte Einschränkungen #
  * Nicht bekannt

# Datenbank-Unterstützung #
  * Oracle XE oder Oracle 11
  * HSQLDB (nicht produktionsgeeignet)

# Installationsanleitung #

Die Auslieferung besteht aus diesen Archiven:

| **Archiv** | **Beschreibung** |
|:-----------|:-----------------|
| extra-cli-2.2.0.zip | Ausführbarer Basisbereich des eXTra-Clients. |
| extra-scenario-sendfetch-2.2.0.zip | Technisches Referenzverfahren 'Send-Fetch'. Dieses dient als exemplarisches Einsatzbeispiel des eXTra-Clients. Aufbauend auf diesem 'Muster' können weitere Fachverfahren entwickelt werden. |
| extra-sterbedaten-ausland-2.2.0.zip | Fachverfahren [Sterbedaten Ausland](eXTraSterbedatenVerfahren.md). |
| extra-common-conf-2.2.0.zip | Globalkonfiguration und Mandantenkonfiguration für sämtliche Fachverfahren. |

Jedes Fachverfahren wird in einem gesonderten Archiv ausgeliefert.
Auf der Seite [eXTraClientBetriebSzenarioSendFetch](eXTraClientBetriebSzenarioSendFetch.md) wird beschrieben wie eine Installation des eXTra-Clients und des Technischem Referenzverfahrens 'Send-Fetch' durchgeführt werden kann.


# OSS-Sonatype und Maven-Central #

  * Jede eXTra-SNAPSHOT-Versionen (Zip, Jar, usw.) stehen unter [OSS-Sonatype-Maven-Repository](https://oss.sonatype.org/index.html#nexus-search;quick~extra-standard) zur Verfügung.
  * Ab Final-Release stehen sämtliche eXTra-Dateien (Zip, Jar, usw.) direkt unter [Maven-Central-Repository](http://search.maven.org) zur Verfügung.

# Erledigte Aufgaben für die Version 2.2.0 #
Siehe bitte: http://goo.gl/2rhK4r

# Download #
Google Code hat die Download-Möglichkeit eingestellt, siehe: https://code.google.com/p/support/wiki/DownloadsFAQ. Daher sollen die Releasedateien ab sofort in Google Drive bzw. DropBox zur Verfügung stehen

Download Release 2.2.0: http://goo.gl/CKpMdw