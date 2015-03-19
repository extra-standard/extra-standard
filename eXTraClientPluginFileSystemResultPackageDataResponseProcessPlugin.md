# FileSystemResultPackageDataResponseProcessPlugin #
**Art:** ResponseProcessPlugin
## Beschreibung ##

Liest aus dem TransportBody des Ergebnisdokumentes eine Liste von Ergebnisdokumenten aus und legt diese im Outputverzeichnis ab.

Wird im Verfahren _Send Fetch_ in Phase 2 zur Abholung der Ergebnisdokumente verwendet.

Beispiel:
Ein Ergebnisdokument ist im Data-Element von PackageBody abgelegt:
```
<xres:TransportBody>
	<xres:Package>
		<xres:PackageHeader>
			<xcpt:RequestDetails>
				<xcpt:RequestID>7010</xcpt:RequestID>
			</xcpt:RequestDetails>
			<xcpt:ResponseDetails>
				<xcpt:ResponseID>10004</xcpt:ResponseID>
				<xcpt:Report>
					<xcpt:Flag>
						<xcpt:Code>C00</xcpt:Code>
						<xcpt:Text>O.K.</xcpt:Text>
					</xcpt:Flag>
				</xcpt:Report>
			</xcpt:ResponseDetails>
		</xres:PackageHeader>
		<xres:PackageBody>
			<xcpt:Data>
				<xcpt:Base64CharSequence>UkZWTlRWa2dVbVZ6Y0c5dWMyVWdabTl5SUZGMVpYSjVJRUZ5WjNWdFpXNTBPbEpGVTFCUFRsTkZYMGxFWDFCb1lYTmxYekZUUlU1RVgwWkZWRWhmTVE9PQ==</xcpt:Base64CharSequence>
			</xcpt:Data>
		</xres:PackageBody>
	</xres:Package>
</xres:TransportBody>
```
## Konfiguration ##

```
plugins.responseprocessplugin=fileSystemResultPackageDataResponseProcessPlugin
# Erfolgreich vom Server empfangene Dateien erhalten diesen Status (WAIT oder DONE)
plugins.responseprocessplugin.fileSystemResultPackageDataResponseProcessPlugin.successStatus=WAIT

```