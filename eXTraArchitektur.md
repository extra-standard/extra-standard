# eXTra-Architektur #

**Inhaltsverzeichnis**



# Analogie zum Briefumschlag und Brief #

eXTra (**Briefumschlag**) schreibt die Struktur der zu transportierenden Daten (**Brief**) _**nicht**_ vor! Der Briefumschlag wird per Client- und Server-Kommunikation übertragen. Das Protokoll (**Zusteller**) für die Client- und Server-Kommunikation (FTP, HTTP, SMTP, ...) ist ebenfalls offen und kann frei ausgewählt werden.

![http://extra-standard.googlecode.com/svn/wiki/images/extra-arc-briefumschlag.jpg](http://extra-standard.googlecode.com/svn/wiki/images/extra-arc-briefumschlag.jpg)

# Client- und Server-Kommunikation #

eXTra basiert auf einer **Client-** und **Server-Architektur**.

![http://extra-standard.googlecode.com/svn/wiki/images/extra-client-server.jpg](http://extra-standard.googlecode.com/svn/wiki/images/extra-client-server.jpg)

# Ebenen, Kommunikation und Rollen #

## Ebenen ##
  * Transportebene (**Transport**) – enthält Informationen zum physikalischen Transport der Datenpakete
  * Paketebene (**Package**) – enthält Informationen zum logischen Transport der Datenpakete
  * Datenebene (**Message**) – enthält Informationen zu den einzelnen Daten

## Kommunikation ##
Auf jeder dieser Kommunikationsebenen kommunizieren ein Sender und ein Empfänger miteinander, und zwar unabhängig von den anderen Kommunikationsebenen. Es stehen folgende Kommunikationsszenarien zur Verfügung:

  * Keine Response (**fire-and-forget**)
  * Response mit Empfangsbestätigung (**request-with-acknowledgement**)
  * Response mit fachlicher Antwort (**request-with-response**).

## Rollen ##
  * **Fachverfahren**, Nutzdatenebene
  * **Logistikverfahren**, Paketebene
  * **Transport-Verfahren**, Transportebene

**Mehr hierzu siehe** [Kompedium eXTra-Standard](http://www.extra-standard.de/upload/pdf/eXTra_Kompendium_1.3.0.pdf).


# Profilierung #
Einschränkung bzw. Anpassung des **eXTra-Basisschemas** auf die Anforderungen des jeweiligen Fachverfahrens.

# Plugins #
Verfahrensspezifische Funktionen, die sich für eine Standardisierung eignen, weil sie potenziell von anderen Verfahren genutzt und in einer allgemeinen Form definiert und implementiert werden können. **Diese Funktionen erweitern den Kernbereich um standardisierte Funktionen**. Sie sind nicht Bestandteil des unprofilierten eXTra Schemas, **ihre Nutzung muss daher profiliert werden**. Diese Funktionen und die sie abbildenden XML-Strukturen werden in der eXTra Terminologie als Plug-Ins bezeichnet. Ein Plug-In standardisiert eine Funktion, ein Datenmodell und eine XMLStruktur, und seine Nutzung ist für jede Ebene separat profilierbar.

Beispiele von Plugins:

  * Plugin _**Certificates**_
  * Plugin _**DataSource**_
  * Plugin _**DataTransform**_
  * Plugin _**Contacts**_