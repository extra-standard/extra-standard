# XML Builder transportBodyRequestQueryElementSequenceBuilder #

Mit diesem Builder kann eine Anfrage an den Server erzeugt werden. Der Builder erhält eine Liste von Anfrage-Elementen und verarbeitet diese zu einer XML-Anfrage. Die Anfrage-Elemente werden von einem Data-Plugin zur Verfügung gestellt.

**Beispiel**

Anfrage _(ResponseID > 3) AND (Procedure = Sterbemeldung) AND (DataType = IT)_

Profil-Datei
```
<element>
  <Name>xcpt:ElementSequence</Name>
  <Elternelement>Data</Elternelement>
</element>
```

Properties-Datei
```
builder.xcpt.ElementSequence=transportBodyRequestQueryElementSequenceBuilder
```

Erzeugter XML-Abschnitt
```
<xcpt:ElementSequence>
  <xmsg:dataRequest>
    <xmsg:Query>
      <xmsg:Argument property="http://www.extra-standard.de/property/ResponseID"
        type="xs:string">
        <xmsg:GT>3</xmsg:GT>
      </xmsg:Argument>
      <xmsg:Argument property="http://www.extra-standard.de/property/Procedure"
        type="xs:string">			
        <xmsg:EQ>Sterbemeldung</xmsg:EQ>
      </xmsg:Argument>
      <xmsg:Argument property="http://www.extra-standard.de/property/DataType"
        type="xs:string">			
        <xmsg:EQ>IT</xmsg:EQ>
      </xmsg:Argument>
    </xmsg:Query>
    <xmsg:Control />
  </xmsg:dataRequest>
</xcpt:ElementSequence>
```