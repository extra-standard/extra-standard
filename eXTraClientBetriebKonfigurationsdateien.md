# Konfigurationsdateien #

Ein Fachverfahren wird in folgenden Dateien konfiguriert:
| **Datei** | **Beschreibung** |
|:----------|:-----------------|
| [extra-global.properties](eXTraClientGlobalConfig.md) | Globale Konfigurationsdatei. In dieser Datei werden werden z.B. die Verbindungsparameter zur Datenbank konfiguriert. |
| [extra-properties-basic.properties](eXTraClientBetriebConfigBasis.md) | Zentrale Konfigurationsdatei. Besteht aus Attribut-Werte Paaren (Java-properties Format). In dieser Datei werden z.B. das Fachverfahren, die Plugins und die XML-Builder konfiguriert. |
| [extra-properties-user.properties](eXTraClientBetriebConfigUser.md) | Ergänzende/überschreibende Einstellungen. Üblicherweise sind hier die Verbindungsparameter zur Webservice-Zieladresse abgelegt. |
| [extra-mandant-xxx.properties](eXTraClientMandatorConfig.md) | Globale Mandantenkonfigurationsdatei, optional. In dieser Datei werden werden z.B. die Parameter zum bestimmten Mandaten konfiguriert, z.B.: Die Webservice-Adresse des eXTra-Servers. |
| [logging-config.xml](eXTraClientBetriebLogging.md) | Logging Einstellungen (z.B. Name der Logfiles und Loglevel) |
| [profile.xml](eXTraClientBetriebConfigProfile.md) | eXTra-Profil Datei (siehe eXTra Standard). In der Profil-Date sind die Elemente des Extra-Standards aufgeführt, die zur Kommunikation zwischen Client und Server verwendet werden sollen (Der Name ist frei wählbar.)|