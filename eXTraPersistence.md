# eXTra-Client-Persistenz #

**Inhaltsverzeichnis**



# Einführung #

In der Dokumentation  http://www.extra-standard.de/upload/pdf/Anl-3-Spektrum_Szenarien_eXTra-in-Betrieb_20110619.pdf sind mehrstufige
Austauschvarianten beschrieben.
Für die bessere Unterstützung diesen Szenarien ist ein Persistenz-Modul entwickelt worden.


# Domänenmodell #

Das Domänenmodell besteht aus mehreren Entitäten. Im folgenden Diagramm sind die Entitäten, deren Funktionen sowie die Beziehungen dargestellt.

![http://extra-standard.googlecode.com/svn/wiki/images/persistence_layer.png](http://extra-standard.googlecode.com/svn/wiki/images/persistence_layer.png)

Die oberste Entity in der Hierarchy ist Mandant (Klasse Mandator). Zu einem Mandanten können mehrere Verfahren (Klasse Procedure) zugeordnet werden. Die Läufe (Die Klasse Execution) stellen einzelne Ausführungen dar. Eine Ausführung wird durch Start, Ende und dazugehörigen Parameters gekennzeichnet. Während eines Laufs können mehrere Dateien übertragen werden. Die Dateien sind durch einen Identifikation (Identifier) und ggf. hashcode eindeutig gekennzeichnet.

Jede Datei in einem mehrstufigen Szenario durchläuft unterschiedliche Stati:

![http://extra-standard.googlecode.com/svn/wiki/images/input_data_states.png](http://extra-standard.googlecode.com/svn/wiki/images/input_data_states.png)

Jede Statusänderung zwischen 2 Übertragungsschritten wird durch Objekt Übergang (Transition) festgehalten. Durch Qualifier werden die Phasen der Übertragung gekennzeichnet. Dabei werden nicht nur die einzelne Phasen festgehalten, sondern auch die internen Übergängen innerhalb der eXTra-Client. Die Dauer der Verpackung und Übertragung einer Nachricht wird protokolliert und kann hiermit ggf. analysiert werden.