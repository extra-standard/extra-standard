# eXTra-CLI-Anwendungsarchitektur (Command Line Interface) #

**Inhaltsverzeichnis**



# Philosophie des eXTra-Clients #
Bisherige Versuche Außenstehenden einen Beispiel-eXTra-Client zur Verfügung zu stellen, scheiterten bisher meist daran, dass der verwendete Client sehr starr auf ein Verfahren und die vorhandene Infrastruktur vor Ort zugeschnitten war.
Anpassungen in einem der beiden Punkte, führten zwangsläufig immer zu programmatischen Änderungen, die in die Grundstrukturen des Clients eingreifen mussten.
Und selbst nach diesen Änderungen war es teilweise nicht möglich die Wünsche der Kundenseite zur vollsten Zufriedenheit abdecken zu können.
So wurden, mit zunehmender Anzahl der Verfahren, welche ihre Daten über eXTra austauschen, der Wunsch nach einem Hilfsmittel zum Versenden und Testen der verschiedenen Verfahren immer lauter.
Um den verschiedenen Ansprüchen gerecht zu werden, wurden für die Entwicklung des neuen eXTra-Clients folgende Eckpfeiler festgelegt:

  * Anwendung unabhängig von der bestehenden Systemumgebung
  * Leicht anpassbar
  * Verfahrensunabhängige Programmierung

# Plugin-Mechanismus #
Um diese Ziele zu erreichen, wurde zugunsten eines Plugin-Systems auf eine starre Programmierung verzichtet.
So lässt sich der Client individuell auf die Bedürfnisse des jeweiligen Verfahrens und der Systemumgebung, in der er verwendet werden soll, anpassen.
Die Plugins haben dazu fest definierte Schnittstellen nach außen, über welche sie untereinander kommunizieren. Die Implementierungsabhängigkeit der einzelnen Plugins und Core-Library wird wie folgt definiert:

![http://extra-standard.googlecode.com/svn/wiki/images/extra-cli-app-architecture.png](http://extra-standard.googlecode.com/svn/wiki/images/extra-cli-app-architecture.png)

Folgende Interfaces stehen als zentrale Schnittstellen in der eXTra-Verarbeitung zur Verfügung:

![http://extra-standard.googlecode.com/svn/wiki/images/extra-plugin-interfaces.png](http://extra-standard.googlecode.com/svn/wiki/images/extra-plugin-interfaces.png)

In Core-Library (extra-core) der Klasse _ClientCore_ in der Methode _buildRequest_ werden die einzelnen Aufrufe der Plugins gesteuert. Mit Hilfe des folgenden Sequenzdiagramms wird der Sachverhalt grafisch dargestellt:

![http://extra-standard.googlecode.com/svn/wiki/images/extra-core-messages.png](http://extra-standard.googlecode.com/svn/wiki/images/extra-core-messages.png)


## Command Line Interface - CLI (extra-cli) ##
Im CLI wird der Aufruf und die betriebliche Schnittstelle des Clients realisiert.
Hierbei gibt es außer der aufzurufenden Funktion zum Starten des Clients und den Returncodes durch die Anwendung keinerlei Einschränkungen,
Folgende Szenarien wären denkbar
  * Konsolenbasierter Aufruf z.B. innerhalb eines Scripts
  * Aufruf aus einer grafischen Oberfläche
  * Einbinden der Plugins in eine neue oder bestehende Anwendung


## Schema-Library (extra-schema) ##
Diese Bibliothek beinhaltet bereits die fertig konfigurierten JAXB-Klassen für eXTra 1.3 inklusive der Annotationen für den Einsatz der Messages, wie zum Beispiel für den Aufbau der Query. Falls neue eXTra-Standard umgesetzt werden sollten, wird die Anpassung der XSD-Dateien in dieser Bibliothek stattfinden.


## Core-Library (extra-core) ##
Die Core-Library bildet das Herzstück des eXTra-Clients. Sie wird durch den CLI-Starter aufgerufen und in ihr werden aus den durch die anderen Plugins gelieferten Informationen eXTra-Requests generiert, welche dann zur weiteren Verarbeitung an das Output-Plugin übergeben wird.


## Config-Plugin (extra-configplugin-default) ##
Im Config-Plugin werden generelle Informationen hinterlegt, welche sendungsunabhängig und –übergreifend gültig sind. Dazu zählen z.B. die Absender- und Empfängerinformationen und welche Verfahren angesprochen werden.
Außerdem werden vom Config-Plugin wichtige Informationen zum Aufbau des Request an die Core-Library übergeben.
Um hierzu nicht das ganze Schema verwenden zu müssen, könnte an dieser Stelle auch die aus dem XSDCreator gewonnene Profildatei ausgewertet werden.


## Data-Plugin (extra-dataplugin-file, extra-dataplugin-simplequery, extra-dataplugin-dbquery) ##
Das Data-Plugin versorgt die Core-Library mit den entsprechenden Nutzdaten, die transportiert werden sollen. Neben den reinen Nutzdaten werden hier auch die Zusatzinformationen zu den Nutzdaten abgelegt, wie u. a. Daten mit denen die Plugins im Request befüllt werden, z.B. die RequestID zum Datensatz. Eine mögliche Implementierung des Data-Plugins ist das Query-Data-Plugin. Beim Abruf der Daten werden vom Data-Plugin keine Nutzdaten im Sinne von Datensätzen übergeben, sondern lediglich die fertig zusammengestellte Query zum Abruf der Daten.
Diese werden nicht als fertig geparstes XML übergeben, sondern als JAXB-Element, welches direkt in den Request eingefügt werden kann.


## Output-Plugin (extra-outputplugin-http, extra-outputplugin-ws) ##
Das Output-Plugin übernimmt von der Core-Library den Request und kümmert sich um dessen Verarbeitung.
Ein Weg, was mit dem Request geschieht, ist nicht vorgeschrieben.
So sind zum Beispiel die folgenden Szenarien denkbar:

  * Der Request wird für die Qualitätssicherung oder als Beispiel-Request auf einem Laufwerk abgelegt oder zur Anzeige gebracht.
  * Der Request wird asynchron per FTP, E-Mail oder per MQ verschickt
  * Der Request wird einer synchronen Verarbeitung per http(s) oder Webservice zugeführt

Bei Verfahren in denen eine Antwort in Form einer eXTra-Response erwartet wird, muss die Verarbeitung der Response und Auswertung der darin enthaltenen Informationen auch durch das Output-Plugin bzw. in dem noch zu definierenden Confirmation-Plugin geschehen.


## Responseprocess-Plugin (extra-responseprocessplugin-sample) ##
Noch zu definieren, siehe: http://code.google.com/p/extra-standard/issues/detail?id=4


# Implementierung #
Die Implementierung des eXTraClients wird am Beispiel für Sofortmeldungen über https nach dem DSRV-Schema vorgestellt.

## Verwendete Bibliotheken und Frameworks ##
Zur Vereinfachung der Implementierung wurde auf verschiedene bestehenden Bibliotheken und Frameworks zurückgegriffen. Diese werden nachfolgend kurz erläutert und ggf. zusammengefasst.

### Spring-Framework ###
Das Spring-Framework bildet bei der vorliegenden Implementierung die Grundlage des Clients.
Dadurch lassen sich die einzelnen Plugins einfach über eine Konfigurations-Datei austauschen. Vereinfacht wird dadurch auch die Handhabung mit klassischen Property-Dateien über den PropertyFileHelper.

### JAXB-Framework ###
Durch Einsatz des JAXB-Frameworks wird auf ein manuelles aufbauen und parsen der XML-Strukturen verzichtet.
Dieses Framework stellt eine API zum Marshalling und Unmarshalling von XML-Strukturen zur Verfügung.
Voraussetzung dafür ist ein vorhandenes Schema, welches in Java-Klassen umgesetzt werden kann.
Dadurch, dass die XML-Struktur in eine Java-Bean überführt wird, lässt sich diese innerhalb der Programmstruktur komfortabler befüllen und auslesen.

### Log4j ###
Als Logging-Framework wird innerhalb der Anwendung das Log4j-Framework verwendet.

### Apache Commons CLI ###
Zur Auswertung von Kommandozeilenargumenten wird innerhalb des eXTRa Command-Line-Interface Client die Apache Commons CLI Bibliothek herangezogen.

### Command Line Interface (CLI) ###
Im CLI-Projekt befindet sich die main-Methode zum Aufruf des Clients. Aus ihr heraus wird eine neue Instanz des Clients erzeugt. Hier liegt auch die betriebliche Schnittstelle eines Client, zum Beispiel das Log für die Anwendungsüberwachung.
Im Starter werden auch sämtliche Bibliotheken referenziert, die zur Ausführung des Clients notwendig sind.
Gegenwärtig liegen dort auch die Konfigurationsdateien für den Gesamt-Client und die einzelnen Plugins in einem Properties-Ordner.

### Schema-Library ###
XSD-Dateien und Generierung mit JAXB finden hier statt.

### Core-Library ###
Bei der Core-Library wurde bereits in der ersten Version versucht ein möglichst breites Spektrum des eXTra-Standards 1.1 generisch abzudecken.

Hierfür wurden innerhalb des Projekts die Schnittstellen zu den Plugins definiert. Ebenfalls in der Core-Library hinterlegt sind die Java-Beans die zur Datenübergabe und –haltung vorgesehen sind, sowie die erforderlichen Zugriffs-Klassen hinterlegt.

### Config-Plugin ###
Im Config-Plugin werden die Sendungsübergreifenden Informationen eingelesen und an die Core-Library übergeben.
Die Informationen hierzu werden aus zwei Quellen ermittelt.
Der Aufbau und die Inhaltsart des Body wird aus der Profildatei, welche vom XSD-Creator erstellt wird, bezogen.
Die anderen Informationen wie z.B. Absender und Empfängerinformationen werden aus der klassischen Property-Datei geladen, welche im Starter referenziert wird.

### Data-Plugin ###
Das Data-Plugin liest die Nutzdaten aus einem vorher in den Properties festgelegtem Ordner aus. Zu jeder Nutzdaten-Datei muss noch ein Auftragssatz vorhanden sein. Für diesen wurde während der Entwicklung ein eigenes XML-Schema entwickelt. Dadurch lässt sich der Autragssatz wieder mit Hilfe von JAXB einfacher innerhalb der Anwendung verarbeiten und auswerten.
Ebenfalls aus dem Auftragssatz werden die Informationen für die ggf. vorhandenen Plugins befüllt. Die Java-Beans mit den Plugin-Informationen werden dazu von einer generischen Bean abgeleitet. Das Query-Data-Plugin ist eine spezielle Implementierung des Data-Plugins. Hier werden nicht die Nutzdaten befüllt, sondern nur die Query zum Abrufen der Verarbeitungsergebnisse. Diese werden wieder als JAXB-Element aufgebaut und an die Core-Library übergeben.
Gegenwärtig wird im Query-Data-Plugin fest nur der Operand Greater-Equals unterstützt. Die weiteren Informationen wie die maximale Anzahl der Pakete und der Startwert, der bei der Abfrage mit an den Server geschickt wird, werden in der Property-Datei statisch hinterlegt.

### Output-Plugin ###
Das Output-Plugin übernimmt den von der Core-Library erstellten Request und verarbeitet diesen. Im implementierten Beispiel für Sofortmeldungen heißt dies, dass der Request per https mit Client-Zertifikat an die Ziel-URL übermittelt wird.
Die Response von der Server-Anwendung wird ausgewertet und auf dem Dateisystem abgelegt.
Beim Abruf der Verarbeitungsergebnisse wird neben dem Report auch jedes Paket einzeln als Antwort auf dem Dateisystem abgelegt. Als Dateiname dient die eindeutige ResponseID.

### Responseprocess-Plugin ###
Noch zu definieren, siehe: http://code.google.com/p/extra-standard/issues/detail?id=4


# Anpassen an die Systemumgebung #
Durch den modularen Aufbau lässt sich der eXTraClient an die jeweilige Einsatzumgebung anpassen.
Dies beginnt schon beim Aufruf des Clients, der wahlweise über die Konsole, aus einem Programm heraus oder über eine grafische Oberfläche ausgeführt werden kann. So kann das Programm individuell an die Bedürfnisse des Nutzers angepasst werden.
Auch die Zulieferung der Nutz- und Versanddaten zur Core-Library kann an das jeweilige Zielsystem angepasst werden. Im vorliegenden Beispiel für Sofortmeldungen geschieht dies über Zugriffe auf die Property-Datei und auslesen der Daten vom Dateisystem. Denkbar hierbei ist, neben fest kodierten Programmstrukturen, wie sie im Dummy vorhanden sind, auch dass die Daten z.B. direkt aus einer Datenbank geholt werden und zuvor eine erweiterte Programmlogik durchlaufen. Ein Anwendungsfall hierfür wäre beispielsweise der Abgleich, welche Datensätze bereits wieder empfangen wurden und welche in einer Datenbank noch offen sind.

Neben dem Bezug der Daten kann natürlich auch der Umgang mit dem Request den jeweiligen technischen und fachlichen Vorgaben angepasst werden. Durch die Modifikation des Output-Plugins ist es auch denkbar, dass die Daten direkt auf das Dateisystem abgelegt werden um z. B. als Beispiel-Requests für die Registrierung eines neuen Verfahrens zu dienen. Auch andere Versandarten als nur der Versand per https sind denkbar, indem man das Plugin entsprechend umschreibt.


# Konfigurations- und Property-Dateien #
Die Konfiguration des eXTra-Clients ist auf verschiedene Dateien aufgeteilt.
Zum einen werden mehrere XML-Dateien vom Spring-Framework verwendet. Zum anderen wird zur Befüllung von Werten eine Property-Datei verwendet.

## Spring-Konfiguration ##
Zentrale Konfigurations-Datei des Clients ist die Datei spring-cli.xml, in welcher die Property-Datei referenziert wird, die Konfigurations-Datei der Core-Library importiert wird und die einzelnen Plugins definiert werden.
Da es sich bei den Plugins um eigenständige Projekte handelt, besitzen diese jeweils noch mal eine eigenständige Konfigurations-Datei, welche beim Aufruf des Plugins geladen wird.
Somit sind die einzelnen Teile des Clients, abgesehen von der Referenz in der spring-cli.xml innerhalb des Starter-Projekts vollkommen unabhängig voneinander und können mit einer kleinen Änderung an der Konfiguration anders belegt werden.

## Property-Datei ##
Bei der Property-Datei handelt es sich um eine klassische Liste von Schlüsselbegriffen, welche Werte zugewiesen bekommen, die im Laufe der Verarbeitung gelesen und verwendet werden.

So sind in der Property-Datei in der gegenwärtigen Implementierung die Informationen für das Config-Plugin hinterlegt, die Ablage- und Lese-Orte für die Dateien sowie die Steuerinformationen für den Abruf der Daten.
Innerhalb des Spring-Frameworks können diese Werte mit Hilfe des PropertyPlaceholderConfigurers recht einfach an die Logik übergeben und dort verwendet werden.


# Referenzen #
  * Spring Framework: http://www.springsource.org
  * JAXB: http://www.oracle.com/technetwork/articles/javase/index-140168.html
  * eXTra: http://www.extra-standard.de
