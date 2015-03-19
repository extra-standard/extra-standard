# Offene Punkte der eXTra-Spezifikation zur Diskussion #


Im Rahmen der eXTra-Client Entwicklung wurden folgende Fragen bzw. Anmerkungen bzgl. der eXTra-Spezifikation festgehalten.
Die Punkte werden im Wiki festgehalten und von Lofi Dewanto an die AWV herangetragen. Link: http://code.google.com/p/extra-standard/wiki/eXTraSpecIssues



## a) Ablage von Nutzdaten ##

Die Ablage der eigentlichen Nutzdaten ist z.Z. unklar. Insgesamt können fünf Ablageorte in Betracht kommen:



  * `EncryptedData`
  * `TransformedData`
  * `Data`
  * `Data.CharSequence`
  * `Data.Base64CharSequence`
  * `Data.ElementSequence`
  * `Data.AnyXML`

Es ist nicht klar definiert, wo die Daten zu extrahieren sind. Hier ist zu großer Interpretationsraum vorhanden.


## b) Verarbeitung großer Datenmengen ##

Die Verarbeitung großer Datenmengen ist aktuell mit dem eXTra Standard nicht vereinbar. Nach der eXTra 1.3 Spezifikation enthalten die eXTra-XML-Nachrichten auch die Nutzdaten. Für die Nutzdaten ist daher eine Base64-Kodierung notwendig, welche im eXTra-XML abgelegt wird. Für die Kodierung werden die Nutzdaten in den Hauptspeicher geladen. Die in der eXTra-Spezifikation beschriebene „Größenunabhängigkeit der Nutzdaten“ ist nicht realisierbar, da eine direkte Abhängigkeit zur Größe des Hauptspeichers besteht.

Erster Vorschlag aus dem Projekt: Der eXTra-Standard sollte dahingehend entwickelt werden, dass die Nutzdaten und der Lieferschein getrennt gespeichert werden können (zusätzliche Datei zur eXTra-XML == Attachments).

Anbei sind einige gute Artikel zu Webservice- bzw. SOAP-Attachment:
  * Überblick über das Versenden von großen Dateien in Webservice: http://goo.gl/zTAVv
  * Überblick über MTOM: http://goo.gl/JHlIn
  * Überblick über Webservice auch mit MTOM: http://goo.gl/VzxR6
  * Nutzung von MTOM in Webservice: http://goo.gl/9dIHY
  * Java Webservice Attachment: SAAJ: http://goo.gl/fg8NE



Zweiter Vorschlag: Nutzung von MapReduce-Framework wie Hadoop, um große XML-Datei parsen zu können. Folgende Infos sind interessant:
  * http://heise.de/-1763366
  * http://goo.gl/OG2m5
  * http://goo.gl/48ATs
  * http://goo.gl/MIbWE
  * http://goo.gl/ez13f
  * http://goo.gl/gBmO9


## c) Spezifizierung bzw. Sematik von Plugins ##

In der eXTra-Spezifikation 1.3 werden Plugins nur sehr grob spezifiert.

  * Verhalten bzw. Semantik von Plugins (Contact, ...) werden in der Spezifikation nicht beschrieben:
  * Was passiert auf der Client-Seite, wenn ich ein Contact-Plugin auf der ebenen des Transports definiert habe?
    * Und auf der Server-Seite?