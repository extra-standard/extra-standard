# Beschreibung extra-properties-user.properties #
In dieser Datei können spezielle Einstellungen eingetragen werden, die die Einstellungen aus eXTra-properties-basic.properties ergänzen oder überschreiben. Üblicherweise werden hier Umgebungsspezifische Einstellungen hinterlegt:
  * Verzeichnisse für Ein- und Ausgabedateien, Kontaktdaten
  * Datenbank-Verbindung
  * Webservice-Adresse (eXTra-Server)

## Beispiel ##
**Fachverfahren Sterbedatenaustausch Phase 1**# =========================================================
# == Sterbedatenaustausch Ausland zwischen DSRV und DPRS ==
# == Benutzerkonfigurationsdatei Phase 1 Italien         ==
# == Bitte Einstellungen anpassen!                       ==
# ========================================================= 

# =====================================
# == Verzeichnisse und Kontakt-Daten ==
# =====================================
plugins.dataplugin.fileDataPlugin.inputVerzeichnis=../work/italien/input
plugins.responseprocessplugin.fileSystemResponseProcessPlugin.eingangOrdner=../work/italien/responses
plugins.responseprocessplugin.fileSystemResponseProcessPlugin.reportOrdner=../work/italien/reports

builder.xplg.Contacts.configurableSMTPContactsPluginsBuilder.emailaddress=test@rentenservice.de

# ==========================
# == Datenbank-Verbindung ==
# ==========================

# Oracle XE
plugins.execution.executionPersistenceJpa.hibernate.generateDdl=false
plugins.execution.executionPersistenceJpa.hibernate.database=ORACLE
plugins.execution.executionPersistenceJpa.database.driver_class=oracle.jdbc.OracleDriver
plugins.execution.executionPersistenceJpa.database.connect_url=jdbc:oracle:thin:@localhost:1521:xe
plugins.execution.executionPersistenceJpa.database.username=extracli
plugins.execution.executionPersistenceJpa.database.password=extracli

# ================================
# == Webservices (eXTra Server) ==
# ================================
webservice.endpoint.url=http://localhost:8088/mockExtraSterbedatenausPhase1```