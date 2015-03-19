# XML-Builder configurableSMTPContactsPluginsBuilder #

Profil-Datei
```
<element>
  <Name>xplg:Contacts</Name>
  <Elternelement>TransportPlugins</Elternelement>
</element>
```

Properties-Datei
```
builder.xplg.Contacts.configurableSMTPContactsPluginsBuilder.emailaddress=test@rentenservice.de
```

Erzeugter XML-Abschnitt
```
<xplg:contactType>
  <xplg:Endpoint type="SMTP">test@rentenservice.de</xplg:Endpoint>
</xplg:contactType>
```