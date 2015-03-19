# Beschreibung eXTra-mandator.properties (Optional) #
In dieser Konfigurationsdatei können mandantenspezifische Einstellungen vorgenommen werden.

## Beispiel ##

**Mandanten-Konfiguration**
# ================================
# == Webservices (eXTra Server) ==
# ================================
# Webservices (eXTra Server)
plugins.outputplugin.wsOutputPlugin.endpoint.url=http://localhost:8088/mockExtraSterbedaten

# Fuer interne Tests ohne Webservice-Aufruf kann ein 'dummyOutputPlugin' verwendet werden
# dazu folgende Zeile auskommentieren: 
#plugins.outputplugin=dummyOutputPlugin```