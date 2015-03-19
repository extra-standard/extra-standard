# eXTra-WebApp-Anwendungsarchitektur (Webanwendung - eXTra-Server) #

**Inhaltsverzeichnis**


# STATUS: DRAFT! #


# Philosophie des eXTra-Servers Webanwendung #

Ein eXTra-Server (im Sprachgebrauch von DSRV: [Single Point of Contact - SPoC](http://extra-standard.googlecode.com/svn/wiki/presentations/extra-server-anforderungen-architektur-impl-dsrv.pdf)) beinhaltet im Prinzip zwei Aufgaben:
  * **Routing** und **Transformation**: Empfang einer Nachricht und weiterleiten dieser Nachricht an ein bestimmtes Zielsystem.
    * Konkret wird eine eXTra-Nachricht empfangen und anschließend über mehrere Transformatoren an eine Fachanwendung weitergereicht.
    * Es kann wiederum eine eXTra-Client angesprochen werden.
  * **Integration**: Unterschiedliche Systeme bzw. Fachanwendungen müssen integriert und angesprochen werden.

Genau diese zwei Aufgaben sind die Stärke des Frameworks [Apache Camel](http://www.manning.com/ibsen/Camel_ch01_update.pdf). Daher wird die Webanwendung des eXTra-Servers auf Apache-Camel umgesetzt. Ein gutes Beispielszenario wofür Camel genutzt werden kann, kann in diesem Tutorial gefunden werden: [Tutorial Camel in Real Life Integration](http://goo.gl/cuo9cy).

**Architektur von Apache Camel:**

![http://camel.apache.org/architecture.data/camel-components.png](http://camel.apache.org/architecture.data/camel-components.png)

Übertragen auf eXTra-Server sieht die **Architektur von eXTra-Server Webanwendung** so aus:

![http://extra-standard.googlecode.com/svn/wiki/images/extra-webapp-architecture.jpg](http://extra-standard.googlecode.com/svn/wiki/images/extra-webapp-architecture.jpg)

[Das Tutorial Camel in Real Life Integration für Webanwendung und Webservice](http://goo.gl/cuo9cy) ist ein sehr gutes Beispiel und besitzt einen ähnlichen Anwendungsfall wie von eXTra-Server.

# Vorteile dieser Architektur #

  * Es existiert eine Menge von Camels Prozessoren und Komponenten, die wiederverwendet werden können.
  * Camel ist weitverbreitet.
  * Camel ist leichtgewichtig und besitzt hervorragende Unterstützung für Spring Framework.


# Referenzen #
  * Apache Camel: http://camel.apache.org
  * Spring Framework: http://www.springsource.org
  * eXTra: http://www.extra-standard.de
