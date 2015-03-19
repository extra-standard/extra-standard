# eXTra-CLI-Anwendungsbetrieb #

**Inhaltsverzeichnis**


# Betriebliche Schnittstelle #

Zur betrieblichen Schnittstelle des eXTra Clients gehören:
  * Aufruf und Argumente des CLI
  * Exit-Code
  * das Filtern und Loggen betrieblich wichtiger Ereignisse

Entsprechend der Philosophie, dass dieses Projekt eine von der Systemumgebung unabhängige Anwendung schafft, muss der Client auch weitgehend unabhängig von der Betriebsumgebung und organisationsspezifischen betrieblichen Anforderungen sein.

# Entkopplung von Kern und Plugins #

Die betriebliche Schnittstelle ist in der Komponente `extra-cli` enthalten. Idealerweise sind andere Komponenten von ihr entkoppelt, d.h. sie sind von Änderungen der betrieblichen Schnittstelle nicht berührt.

Betrieblich wichtige Ereignisse können aber in Plugins oder anderen Komponenten außerhalb von `extra-cli` auftreten. So gibt es zum Beispiel in manchen Betriebsumgebungen die Anforderung, gelesene und geschriebene Dateien und deren Größe zu loggen. D.h. Komponenten müssen (potentiell) betrieblich wichtige Ereignisse über eine Schnittstelle, einem `transportObserver` melden können. Die Auswertung der Ereignisse, also zum Beispiel das Filtern und Aufbereiten der dann tatsächlich zu loggenden Nachrichten, erfolgt in einer betriebsspezifischen Implementierung dieses `transportObservers` in `extra-cli`.

Plugins lassen sich einen konkreten `transportObserver` injizieren und melden alle seiner Schnittstelle definierten Ereignisse. Potentiell lässt sich dieses Vorgehen zu einem vollständigen Observable/Observer-Mechanismus im `core` ausbauen, in dem sich auch andere Observer zur Benachrichtigung bei wichtigen Ereignissen registrieren können.


# Argumente #

Die Auswertung von Argumenten des Client-Aufrufs erfolgt in Komponente `extra-cli`. Die Klasse `ClientArguments` nimmt die übergebenen Argumente entgegen und stellt die auszuführende Aktion und Optionen des Clients bereit. Sie verwendet _Apache Commons CLI_.

Generell werden folgende Optionen berücksichtigt:
  * `-[quiet|verbose|debug|trace]`: Log-Level setzen (überschreibt konfigurierte Level aller Logger)
  * `-help`: Hilfe über Kommandozeilensyntax des Client auf die Konsole ausgeben

Weitere Optionen ergeben sich aus den Anforderungen der Plugins.

# Exit-Code #

Im einfachsten Fall muss man hier nur zwischen 0 (Erfolg) und != 0 (Fehler) unterscheiden. Es gibt Anforderungen nach feineren Abstufungen, zum Beispiel "Warnung", "technischer Fehler" bzw. "Fehler der Transportebene", "fachlicher Fehler".

Da ein Client-Lauf mehrere Requests versenden kann, entsteht der Exit-Code aus dem Ergebnis mehrerer einzelner Transportvorgänge. Plugins melden dementsprechend den Erfolg oder Fehler einzelner Transportvorgängen als betriebliche Ereignisse. Diese kann dann ein `transportObserver` zu einem Gesamt-Exit-Code aggregieren, zum Beispiel zum Maximum aller Codes der einzelnen Transportvorgänge.

# Logging #

Es gibt eine eigene Log4J Kategorie für ein Log, dass der Überwachung durch den Betrieb dient und daher unter Umständen dedizierte betriebliche Anforderungen an Format, Inhalt und Reihenfolge der erfüllen muss.

Eine eigene Klasse `OpLogger` empfängt als `transportObserver` alle von Plugins gemeldeten Ereignisse, filtert die betriebsrelevanten und schreibt passende Nachrichten auf das Betriebslog. Sie kann auch Ergebnisse aggregieren und diese zum Ende der Verarbeitung als Nachricht schreiben, zum Beispiel die Gesamtzahl verarbeiteter Dateien bzw. versendeter Transportnachrichten.