**Inhaltsverzeichnis**


# Einführung #

Mit dem RequestBuilder ist eine Komponente entstanden, der eine modulare eXTra-Nachrichtenaufbau realisiert.

Die 3 wesentlichen Elemente werden bei der Aufbereitung einer Nachricht berücksichtigt:

  * ExtraProfileKonfiguration
  * InputDataContainer
  * Anwendungsspezifische Builder Konfiguration

ExtraProfileKonfiguration wird bei der Profilierung vereinbart. Die regelt, welche XML Segmente in einer eXTra Nachricht vorkommen können.
Die zu übertragende dynamischen Daten werden in InputDataContainer durch DataPlugin aufbereitet.
Die anwendungsspezifische Builder Konfiguration gibt an, wie die Inputdaten in eine eXTra Nachricht verpackt werden.


# eXTra-RequestBuilder Interfaces #


Die wesentlichen Interfaces eines RequestBuilder sind auf dem Diagramm abgebildet.

![http://extra-standard.googlecode.com/svn/wiki/images/extra-request-builder.png](http://extra-standard.googlecode.com/svn/wiki/images/extra-request-builder.png)






# eXTra-RequestBuilder Nachrichtenaustausch #

Auf dem folgenden Diagramm sind die Nachrichtenaustausch bei der Erstellung eines eXTra-Nachricht abgebildet.

![http://extra-standard.googlecode.com/svn/wiki/images/extra-nachrichtenaustausch-requestbuilder.png](http://extra-standard.googlecode.com/svn/wiki/images/extra-nachrichtenaustausch-requestbuilder.png)