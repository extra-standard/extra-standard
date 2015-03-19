# Beschreibung eXTra-global.properties #
In dieser Konfigurationsdatei ist die globale technische Konfiguration des Fachverfahrens untergebracht. Datenbankverbindung oder Persistenzeinstellungen können in dieser Datei vorgenommen werden. Die Datei kann in folgende Bereiche aufgeteilt werden:

  * **Datenbankeinstellungen**
    * Technische Parameter für die eXTra-Client Datenbank
  * **Persistenzeinstellungen**
    * Technische Parameter für die eXTra-Client Datenbank

## Beispiel ##

**Globale Konfiguration**
# ==========================
# == Datenbank-Verbindung ==
# ==========================

# HSQLDB
plugins.execution.executionPersistenceJpa.hibernate.generateDdl=false
plugins.execution.executionPersistenceJpa.hibernate.database=HSQL
plugins.execution.executionPersistenceJpa.database.driver_class=org.hsqldb.jdbcDriver
plugins.execution.executionPersistenceJpa.database.connect_url=jdbc:hsqldb:file:target/test-classes/test-hsqldb/eXTra-persistence
plugins.execution.executionPersistenceJpa.database.username=sa
plugins.execution.executionPersistenceJpa.database.password=

# Oracle XE
#plugins.execution.executionPersistenceJpa.hibernate.generateDdl=false
#plugins.execution.executionPersistenceJpa.hibernate.database=ORACLE
#plugins.execution.executionPersistenceJpa.database.driver_class=oracle.jdbc.OracleDriver
#plugins.execution.executionPersistenceJpa.database.connect_url=jdbc:oracle:thin:@localhost:1521:xe
#plugins.execution.executionPersistenceJpa.database.username=extracli
#plugins.execution.executionPersistenceJpa.database.password=extracli

# Connection pool settings
plugins.execution.executionPersistenceJpa.db.pool.maxIdle=10
plugins.execution.executionPersistenceJpa.db.pool.maxActive=100
plugins.execution.executionPersistenceJpa.db.pool.maxWait=1000
plugins.execution.executionPersistenceJpa.db.pool.validationQuery=
plugins.execution.executionPersistenceJpa.db.pool.testOnBorrow=false
plugins.execution.executionPersistenceJpa.db.pool.testWhileIdle=true
plugins.execution.executionPersistenceJpa.db.pool.timeBetweenEvictionRunsMillis=1200000
plugins.execution.executionPersistenceJpa.db.pool.minEvictableIdleTimeMillis=1800000
plugins.execution.executionPersistenceJpa.db.pool.numTestsPerEvictionRun=5
plugins.execution.executionPersistenceJpa.db.pool.defaultAutoCommit=false```