# eXTra-Client-Exceptions #

**Inhaltsverzeichnis**



# Einführung #

Die Exceptions in eXTra-Client leiten von der Klasse ExtraRuntimeException ab und sind hiermit **_"unchecked"_**. Eine "checked" Exception wird nur dann geworfen, wenn der Aufrufer darauf reagieren kann. Bei anderen Exception wird das ExceptionHandling in einer zentralen Stelle (am Start der Verarbeitung) stattfinden.


# Details #

In dem folgendem Diagram sind die Exception grafisch dargestellt.

![http://extra-standard.googlecode.com/svn/wiki/images/exception-hierarchie.png](http://extra-standard.googlecode.com/svn/wiki/images/exception-hierarchie.png)

Alle Exception leiten von dem ExtraRuntimeException ab und beinhalten einen ExceptionCode. Bei der Auswertung der Exception wird ExceptionCode berücksichtigt.