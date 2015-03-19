# Sterbedatenaustausch Ausland zwischen DSRV und DPRS mit eXTra #

**Inhaltsverzeichnis**


# Abkürzungen #
  * DSRV: Datenstelle der Rentenversicherung
  * DPRS: Deutsche Post Renten Service

# Kommunikationspartner #
  * eXTra-Client: DPRS
  * eXTra-Server: DSRV

# Phasen #

![http://extra-standard.googlecode.com/svn/wiki/images/extra-nachrichtenaustausch-sendeholbetrieb.jpg](http://extra-standard.googlecode.com/svn/wiki/images/extra-nachrichtenaustausch-sendeholbetrieb.jpg)

  1. Senden Fachverfahren Sterbedatenabgleich (Request - Response)
  1. Abrufen Verarbeitungsergebnisse Fachverfahren Sterbedatenabgleich (Request - Response)
  1. Bestätigung Verarbeitungsergebnisse Fachverfahren Sterbedatenabgleich (Request - Response)

Eine Detailierte Beschreibung ist in dem Dokument **_Anl-3-Spektrum-Szenarien-eXTra-in-Betrieb\_20110619.pdf_** beschrieben.
Das Dokument ist unter http://www.extra-standard.de/upload/pdf/Anl-3-Spektrum_Szenarien_eXTra-in-Betrieb_20110619.pdf verfügbar.




# XSD: Profilierung des eXTra-Schemas #

Auf Basis der eXTra XSD-Schema-Dateien werden die Profilierungsdateien erstellt. Hier liegen die XSD-Schema-Dateien von eXTra 1.3:
[XSD-Schemadateien eXTra 1.3](https://code.google.com/p/extra-standard/source/browse/#svn%2Ftrunk%2Fjava%2Fcomponents%2Fextra-schema%2Fsrc%2Fmain%2Fxsd)

# XML: Beispieldateien #
  * [Phase 1](https://code.google.com/p/extra-standard/source/browse/#svn%2Ftrunk%2Fjava%2Fprocedures%2Fextra-sterbedaten-ausland%2Fsrc%2Fmain%2Fdocs%2Fphase1%253Fstate%253Dclosed)
  * [Phase 2](https://code.google.com/p/extra-standard/source/browse/#svn%2Ftrunk%2Fjava%2Fprocedures%2Fextra-sterbedaten-ausland%2Fsrc%2Fmain%2Fdocs%2Fphase2)
  * [Phase 3](https://code.google.com/p/extra-standard/source/browse/#svn%2Ftrunk%2Fjava%2Fprocedures%2Fextra-sterbedaten-ausland%2Fsrc%2Fmain%2Fdocs%2Fphase3)