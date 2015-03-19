# Beschreibung eXTra-properties-basic.properties #
In dieser Konfigurationsdatei ist die Basiskonfiguration des Fachverfahrens untergebracht. Im Normalfall sollte man diese nicht ändern müssen. Die Konfigurationsdatei besteht aus Attribut-Werte Paaren (Java-properties Format). In jeder Zeile wird der Wert für ein Attribut angegeben. Die Datei kann in folgende Bereiche aufgeteilt werden:

  * **Fachverfahren**
    * Angabe von _Procedure_ und _Phase_
  * **Plugins**
    * Angabe der Plugins
    * Konfiguration der Plugins (z.B. Profil-Datei)
  * **Validierung**
    * Validierung des ausgehenden XMLTransports
  * **Message-Header**
    * _Request_, _Sender_ und _Receiver_ definieren
  * **XML-Builder**
    * Auswahl der Bausteine, die aus den Eingabedaten und der Profildatei die Client-Nachricht zusammenbauen.


## Beispiel ##

**Fachverfahren Sterbedatenaustausch Phase 1**# =========================================================
# == Sterbedatenaustausch Ausland zwischen DSRV und DPRS ==
# == Basiskonfigurationsdatei Phase 1 Italien            ==
# == Muss im allgemeinen nicht mehr angepasst werden!    ==
# ========================================================= 

# Procedure und Phase
core.execution.procedure=Sterbemeldung
core.execution.phase=PHASE1

# -------------
# -- Plugins --
# -------------
plugins.configplugin=defaultConfigPlugin
plugins.dataplugin=fileDataPlugin
plugins.outputplugin=wsOutputPlugin
plugins.responseprocessplugin=acknowledgePhase1ResponseProcessPlugin
plugins.execution=executionPersistenceJpa

# -------------
# -- XML Processing --
# -------------
core.outgoing.validation=true


# Profilierungsdatei
plugins.configplugin.defaultConfigPlugin.profilFileName=phase1-sent-profil.xml

# Maximale Anzahl von Verarbeitungs-Objekten (z.b. Dateien)
# In einer eXTra-Client Nachricht wird eine Datei verpackt (siehe Profil)!
plugins.dataplugin.fileDataPlugin.inputDataLimit=1
plugins.dataplugin.dbQueryDataPlugin.inputDataLimit=100

# Suchanfrage nach der letzten Serverantwort fuer eine Laenderkennung
plugins.dataplugin.dbMaxResponseIdQueryDataPlugin.subquery=IT

# positive Server Return-Codes (fuer die Fehlerbehandlung)
de.extra.client.core.util.ExtraReturnCodeAnalyser.returncodelist=C00,I000,E98

# ----------------------------------------
# -- MessageHeaderBuilder Konfiguration --
# ----------------------------------------
message.builder.header.requestDetail.procedure=Sterbemeldung
message.builder.header.requestDetail.dataType=http://www.extra-standard.de/datatypes/DataSend
message.builder.header.requestDetail.scenario=http://www.extra-standard.de/scenario/request-with-response

message.builder.header.testIndicator=http://extra-standard.de/test/NONE
message.builder.header.senderId.class=
message.builder.header.senderId.value=ec-1
message.builder.header.senderNameValue=eXTra-Client
message.builder.header.receiverId.class=
message.builder.header.receiverId.value=es-1
message.builder.header.receiverNameValue=eXTra-Server
message.builder.transport.attributes.extraProfile=http://www.extra-standard.de/profile/DUSKO/1.0

# -------------------------------
# -- Xml-Builder Konfiguration --
# -------------------------------
builder.xplg.DataTransforms1=dataTransformConfigurablePluginsBuilder
builder.xplg.DataSource=dataSourceConfigurablePluginsBuilder
builder.xcpt.ElementSequence=transportBodyRequestQueryElementSequenceBuilder```