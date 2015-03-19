# Entwicklungsprozess #

Hier wird der Entwicklungsprozess, die Entwicklungsumgebung und die Coding-Regeln definiert.

**Inhaltsverzeichnis**



# Entwicklungsumgebung #
  * Java 6
  * Maven 3
  * Eclipse 3.7.2 mit Maven Plugin m2e und AspectJ Plugin oder SpringSource Tool Suite 2.9.2.RELEASE. Damit das Plugin m2e richtig funktionieren kann, müssen die entsprechenden m2e-Konnektoren installiert werden. Beispiel: Der Konnektor _m2e connector for build-helper-maven-plugin_ kommt bereits mit m2e Eclipse plugin. Ansonsten beschwert sich Eclipse, dass der Konnektor fehlt und bietet an, diesen nachzuinstallieren.
  * Encoding UTF-8 (Eclipse -> Window -> Preferences -> _encoding_ in Suchfeld eintippen -> Content Types und Workspace)

# Coding-Regeln #
  * Code-Formatter: Eclipse [built-in] unverändert!

# Modellierungsumgebung #
  * UML 2.x
  * MagicDraw 17.x. Die MagicDraw-Modelldateien besitzen die Namenserweiterung von **.mdzip**.
  * Zum Lesen der Modelldateien reicht einen kostenlosen MagicDraw-Reader aus. Das Programm MagicDraw-Reader kann unter folgender Adresse http://www.nomagic.com/products/magicdraw/magicdraw-reader.html heruntergeladen werden.

# Bauen von Quellcode mit Hilfe von Maven #
  * Ausschecken von SVN unter: **trunk/java**
  * **Parent-POM-Projekt:** Im Ordner **_java/components/extra-parent_** steht das Parent-POM-Projekt zur Verfügung. Dieses Projekt muss zuerst mit dem folgenden Befehl gebaut werden: _mvn clean install_
  * **Modul-Projekt:** Im Ordner **_java_** befindet sich eine Maven-Datei namens _pom.xml_. In dieser Datei werden sämtliche Module von eXTra mit der richtigen Reihenfolge beschrieben. Nach der Installation des Parent-POM-Projekts kann in diesem Ordner der Maven-Befehl wie beispielsweise _mvn clean package_ ausgeführt werden. Damit werden sämtliche Module in der richtigen Reihenfolge gebaut.

# Import von Maven-Projekten ins Eclipse-Workspace #

Dir Projekte aus dem SVN können über einen SVN-Client in den Workspace importiert werden.
Dabei ist zu beachten, dass es sich bei den Projekten bereits um Maven-Projekte handelt.

## Auschecken aus dem SVN ##

Auch wenn es sich bereits um Maven-Projekte handelt, müssen die Projekte im Eclipse nur als "Projekte" ausgecheckt werden. Ein auschecken als Java-Projekt führt z.B. zu Problemen bei der Paketstruktur.

![http://extra-standard.googlecode.com/svn/wiki/images/SVNCheckout.png](http://extra-standard.googlecode.com/svn/wiki/images/SVNCheckout.png)

## Konvertieren in Maven-Projekt ##

Nach dem erfolgreichen Import in den eigenen Workspace müssen die Projekte noch in ein Maven-Projekt konvertiert werden.
Dies geschieht über das Kontext-Menü. Hierzu sollte dann aber das Plugin m2e installiert sein.
Danach kann normal mit dem Projekt gearbeitet werden.

![http://extra-standard.googlecode.com/svn/wiki/images/toMavenContext.png](http://extra-standard.googlecode.com/svn/wiki/images/toMavenContext.png)


# Continuous Intergration (CI) Jenkins für eXTra-Projekt #
  * Unter: https://extra-standard.ci.cloudbees.com steht eine CloudBees-CI mit Jenkins für das Projekt eXTra zur Verfügung. Hier kann der Build-Status und Unit-Test-Status von eXTra beobachtet werden.

  * So sehen die Maven-Module in Jenkins CloudBees aus:

![http://extra-standard.googlecode.com/svn/wiki/images/jenkins-extra-cloudbees-modules.jpg](http://extra-standard.googlecode.com/svn/wiki/images/jenkins-extra-cloudbees-modules.jpg)


# Maven Repository #

Sämtliche eXTra-Artefakte werden in folgenden Maven-Repositories deployed:

  * [Sonatype OSS Maven Repository](https://oss.sonatype.org) für Snapshots und Releases
  * Sämtliche Release-Artefakte werden mit [Maven Central Repository](http://search.maven.org/) synchroniziert.