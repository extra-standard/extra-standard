package de.extra.xtt.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Locator;

import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSType;

import de.extra.xtt.gui.model.ProfilingTreeNode;
import de.extra.xtt.util.schema.MySchemaWriter;
import de.extra.xtt.util.schema.SchemaElement;
import de.extra.xtt.util.tools.Configurator;
import de.extra.xtt.util.tools.XsdXmlHelper;

/**
 * Implementierung für die Erzeugung eines spezifischen eXTra-Schemas.
 * 
 * @author Beier
 * 
 */
public class ExtraTailoringImpl implements ExtraTailoring {

	private final Configurator configurator;

	/**
	 * Konstruktur mit der Zuweisung eines initialisierten Configurator-Objekts.
	 * 
	 * @param configurator
	 *            Configurator zum Zugriff u.a. auf Properties
	 */
	public ExtraTailoringImpl(Configurator configurator) {
		this.configurator = configurator;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public Map<String, Document> erzeugeProfiliertesExtraSchema(XSSchemaSet ssQuellSchema, Document profilXml,
			String bezeichnungKurzVerfahren) throws ExtraTailoringException {
		if ((ssQuellSchema != null) && (profilXml != null) && (bezeichnungKurzVerfahren != null)) {
			try {
				MySchemaWriter schemaWriter = new MySchemaWriter("xs", "http://www.w3.org/2001/XMLSchema",
						configurator, 1);
				
				// Element-Knoten aus der Profilierungsdatei abarbeiten
				List<XSElementDecl> xsElementsToInsert = new LinkedList<XSElementDecl>();
				List<XSElementDecl> xsElementsInserted = new LinkedList<XSElementDecl>();
				NodeList xmlNodesElementProf = XsdXmlHelper.xpathSuche("//element", profilXml);
				if (xmlNodesElementProf.getLength() > 0) {
					for (int i = 0; i < xmlNodesElementProf.getLength(); i++) {
						erzeugeSchemaTypUndElementFuerProfElement(xmlNodesElementProf.item(i), ssQuellSchema,
								xsElementsToInsert, xsElementsInserted, schemaWriter);
					}
				} else {
					throw new ExtraTailoringException("Profilkonfiguration enthält keine Elemente.");
				}

				// Zusätzlich referenzierte Elemente behandeln, die nicht bereits in der Liste der noch zu hinzufügenden Elementen stehen
				printRefElements(schemaWriter, ssQuellSchema, xsElementsToInsert, xsElementsInserted);

				// Verwendete Typen schreiben
				schemaWriter.printReferencedTypes(profilXml);

				// Am Ende Elemente schreiben
				for (XSElementDecl currElement : xsElementsToInsert) {
					// entsprechenden Knoten in Profilkonfiguration suchen (für evtl. Anmerkungen)
					String strNameCurrElement = configurator.getPropertyNamespace(currElement.getTargetNamespace()) + ":" + currElement.getName();
					Node currElementNode = XsdXmlHelper.xpathSuche("//element[name/text()='" + strNameCurrElement + "']", profilXml).item(0);
					schemaWriter.elementDecl(currElement, "", null, currElementNode, "");
				}

				// Schemadateien schreiben
				Map<String, Document> xsdDocs = new HashMap<String, Document>();
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				docFactory.setNamespaceAware(true);
				DocumentBuilder parser = docFactory.newDocumentBuilder();
				Map<String, Writer> nsWriter = schemaWriter.getNsWriter();
				Map<String, ByteArrayOutputStream> nsOutputStream = schemaWriter.getNsOutputStream();

				// Einzelne Namespace-Witer abarbeiten
				for (Map.Entry<String, Writer> currEntry : nsWriter.entrySet()) {
					String currNsPrefix = currEntry.getKey();
					String currNsUrl = configurator.getPropertyNamespace(currNsPrefix);
					List<String> usedNamespaces = schemaWriter.getNsUsedNamespaces().get(currNsPrefix);

					// Schema-Header schreiben
					ByteArrayOutputStream outStreamCurrDocXsd = new ByteArrayOutputStream();
					ByteArrayOutputStream outStream = new ByteArrayOutputStream();
					
					OutputStreamWriter swHeader = new OutputStreamWriter(outStream, "UTF8");
					
					swHeader.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
					swHeader.write("<xs:schema targetNamespace=\"" + currNsUrl + "\"\r\n");
					swHeader.write("  xmlns=\"" + currNsUrl + "\"\r\n");

					// Verwendete Namespaces im Header angeben
					for (String strNsPrefixUsed : usedNamespaces) {
						if (!strNsPrefixUsed.equals("xs")) {
							String currNsUrlUsed = configurator.getPropertyNamespace(strNsPrefixUsed);
							swHeader.write("  xmlns:" + strNsPrefixUsed + "=\"" + currNsUrlUsed + "\"\r\n");
						}
					}

					// Attribute attributeFormDefault, elementFormDefault und version (falls vorhanden) werden übernommen
					Locator loc = ssQuellSchema.getSchema(currNsUrl).getLocator();
					String fileNameSchema = loc.getSystemId().substring(loc.getSystemId().indexOf("file:/") + 6)
							.replaceAll("%20", " ");
					Document docCurrSchema = XsdXmlHelper.leseXsdXml(fileNameSchema);
					NodeList listNodes = XsdXmlHelper.xpathSuche("/*/attribute::*", docCurrSchema);
					for (int i = 0; i < listNodes.getLength(); i++) {
						Node currAttrNode = listNodes.item(i);
						if (!currAttrNode.getNodeName().equals("targetNamespace")) {
							swHeader.write("  " + currAttrNode.getNodeName() + "=\"" + currAttrNode.getNodeValue()
									+ "\"\r\n");
						}
					}

					swHeader.write("  xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\r\n");

					// Import-Anweisungen erzeugen
					for (String strNsPrefixUsed : usedNamespaces) {
						if (!strNsPrefixUsed.equals("xs")) {
							String currNsUrlUsed = configurator.getPropertyNamespace(strNsPrefixUsed);
							String currDateinameImport = configurator.getDateinameFuerSchema(bezeichnungKurzVerfahren,
									strNsPrefixUsed);
							swHeader.write("  <xs:import namespace=\"" + currNsUrlUsed + "\" schemaLocation=\""
									+ currDateinameImport + "\"/>\r\n");
						}
					}

					swHeader.flush();

					// Header schreiben
					outStream.writeTo(outStreamCurrDocXsd);
					// Hauptdaten schreiben
					nsOutputStream.get(currEntry.getKey()).writeTo(outStreamCurrDocXsd);
					// Ende schreiben
					outStream.reset();
					swHeader.write("</xs:schema>");
					swHeader.flush();
					outStream.writeTo(outStreamCurrDocXsd);

					// XSD-Dokument aus Stream erzeugen
					Document currDocSchemaProf = parser.parse(new ByteArrayInputStream(outStreamCurrDocXsd
							.toByteArray()));
					xsdDocs.put(currNsPrefix, currDocSchemaProf);
				}
				return xsdDocs;
			} catch (Exception e) {
				throw new ExtraTailoringException("Fehler beim Erzeugen des profilierten Schemas.", e);
			}
		} else {
			throw new ExtraTailoringException(
					"Übergebenes Quell-Schema, Profildokument oder Bezeichnung ungültig (NULL).");
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public Document erzeugeProfilkonfiguration(Configurator.SchemaType schemaType, ProfilingTreeNode rootNodeMain,
			ProfilingTreeNode rootNodeRef, String targetNamespace, String bezVerfahrenKurz, String bezVerfahren)
			throws ExtraTailoringException {
		if (configurator != null) {
			try {

				// Dokument mit Wurzelknoten erzeugen
				Document docXml = erzeugeLeeresDokument("profil-konfiguration");
				Element root = (Element) docXml.getFirstChild();

				// Attribute setzen
				root.setAttribute("tnsUrl", targetNamespace);
				root.setAttribute("bezKurzVerfahren", bezVerfahrenKurz);
				root.setAttribute("bezVerfahren", bezVerfahren);

				// Baum durchlaufen und für jedes Element mit Kindknoten element-Knoten erzeugen
				List<String> elementsAdded = new LinkedList<String>();
				insertNodeProfXml(rootNodeMain, rootNodeRef, docXml, elementsAdded);

				return docXml;
			} catch (Exception e) {
				throw new ExtraTailoringException("Fehler beim Erzeugen des Profildokuments.", e);
			}
		} else {
			throw new ExtraTailoringException("Configurator nicht vorhanden.");
		}
	}

	/**
	 * Erzeugt ein zum übergebenen ProfilingTreeNode-Objekt passendes element-XML-Element mit den entsprechenden Kind-Elementen und hängt es dan das übergebene XML-Dokument an.
	 * 
	 * @param currNode
	 *            Zu bearbeitender ProfilingTreeNode
	 * @param rootNodeRef
	 *            Wruzelelemet des Bausm mit allen referenzierten Elementen
	 * @param docXml
	 *            Profilkonfiguration, in die das neue Element eingefügt wird
	 * @param elementsAdded
	 *            Liste aller bereits hinzugefügter Elemente
	 */
	private void insertNodeProfXml(ProfilingTreeNode currNode, ProfilingTreeNode rootNodeRef, Document docXml,
			List<String> elementsAdded) {
		SchemaElement currSchemaElement = currNode.getSchemaElement();
		String bezeichnungWithNamespacePrefix = currSchemaElement.getNameWithPrefix();
		// Falls schon hinzugefügt, dann nicht erneut Knoten erzeugen
		if (!elementsAdded.contains(bezeichnungWithNamespacePrefix)) {
			// 'element'
			Node nodeElement = docXml.createElement("element");

			// Kind 'name'
			Node nodeName = docXml.createElement("name");
			nodeName.setTextContent(bezeichnungWithNamespacePrefix);
			nodeElement.appendChild(nodeName);
			// zur Liste der bereits hinzugefügten Elemente hinzufügen
			elementsAdded.add(bezeichnungWithNamespacePrefix);
			
			// Kind 'anmerkung'
			String anmerkungAllgText = configurator.getAnmerkungAllgemein(currNode);
			if (anmerkungAllgText.length() > 0) {
				Node nodeAnmerkung = docXml.createElement("anmerkung");
				nodeAnmerkung.setTextContent(anmerkungAllgText);
				nodeElement.appendChild(nodeAnmerkung);
			}

			// element hinzufügen
			docXml.getFirstChild().appendChild(nodeElement);

			// Kinder 'kind'
			Enumeration<ProfilingTreeNode> enumChilds = null;
			if (currNode.getChildCount() > 0) {
				// eigene Kinder behandeln
				enumChilds = currNode.children();
			} else if (rootNodeRef != null) {
				// Prüfen, ob in den referenzierten Knoten Elemente für dieses aktuelle Element vorhanden sind
				ProfilingTreeNode nodeRef = rootNodeRef.getChildWithSameSchemaElement(currSchemaElement);
				if ((nodeRef != null) && (nodeRef.getChildCount() > 0)) {
					// Kindern vom referenzierten Knoten behandeln
					enumChilds = nodeRef.children();
				}
			}
			// Kind-Elemente erzeugen
			if (enumChilds != null) {
				for (Enumeration<ProfilingTreeNode> e = enumChilds; e.hasMoreElements();) {
					ProfilingTreeNode currNodeChild = e.nextElement();
					if (currNodeChild.isChecked()) {
						Element nodeKind = docXml.createElement("kind");
						
						// min- und maxOccurs
						if (currNodeChild.isMinOccursChangeable() && (currNodeChild.getMinOccursUser() != currNodeChild.getMinOccursDefault())) {
							nodeKind.setAttribute("minOccurs", "" + currNodeChild.getMinOccursUser());
						}
						if (currNodeChild.isMaxOccursChangeable() && (currNodeChild.getMaxOccursUser() != currNodeChild.getMaxOccursDefault())) {
							nodeKind.setAttribute("maxOccurs", "" + currNodeChild.getMaxOccursUser());
						}
						
						// Anmerkung zur Verwendnug
						String anmerkungVerwendung = configurator.getAnmerkungVerwendung(currNodeChild);
						if (anmerkungVerwendung.length() > 0) {
							nodeKind.setAttribute("anmerkung", anmerkungVerwendung);
						}
						
						nodeKind.setTextContent(currNodeChild.getSchemaElement().getNameWithPrefix());
						nodeElement.appendChild(nodeKind);
						// rekursiver Aufruf
						insertNodeProfXml(currNodeChild, rootNodeRef, docXml, elementsAdded);
					}
				}
			}
		}
	}

	/**
	 * Für den angebenen XML-Knoten aus der Profilkonfiguration wird ein SchemaElement erzeugt und im Quellschema das passende Element gesucht. Für dieses Element aus dem Quellschema wird dann der
	 * Abschnitt für das profilierte Schema mittels SchemaWriter erzeugt.
	 * 
	 * @param xmlNodeElement
	 *            Aktueller XMl-Knoten aus der Profilkonfiguration
	 * @param ssQuellSchema
	 *            Quellschema mit allen Elementen und Typen
	 * @param xsElementsToInsert
	 *            Liste aller Elemente, die im aktuellen Profil verwendet werden; diese werden erst gesammelt und nach den Typen geschrieben.
	 * @param xsElementsInserted
	 *            Liste aller Elemente, die bereits hinzugefügt wurden
	 * @param schemaWriter
	 *            Objekt zum Erzeugen des XML-Textes für die zu erzeugende Schemadatei
	 * @throws XPathExpressionException
	 */
	private void erzeugeSchemaTypUndElementFuerProfElement(Node xmlNodeElement, XSSchemaSet ssQuellSchema,
			List<XSElementDecl> xsElementsToInsert, List<XSElementDecl> xsElementsInserted, MySchemaWriter schemaWriter)
			throws XPathExpressionException {
		// Name des XML-Knotens in der Profilkonfiguration
		String strXmlElementName = XsdXmlHelper.xpathSuche("./name/text()", xmlNodeElement).item(0).getNodeValue();
		// Passendes SchemaElement erzeugen
		SchemaElement currSchemaElement = configurator.getSchemaElement(strXmlElementName);
		// passendes Element im Schema suchen
		XSElementDecl currXsElement = ssQuellSchema.getElementDecl(currSchemaElement.getNsUrl(), currSchemaElement
				.getName());
		if (currXsElement != null) {
			erzeugeSchemaTypUndElement(currXsElement, xmlNodeElement, ssQuellSchema, xsElementsToInsert,
					xsElementsInserted, schemaWriter);
		}
	}

	/**
	 * Für das angegebene Schema-Element wird unter Berücksichtigung der Profilkonfiguration der entsprechende Eintrag für das profilierte Schema erzeugt.
	 * 
	 * @param currXsElement
	 *            Zu bearbeitendes Schema-Element, das in der profilierte Schema übernommen werden soll
	 * @param xmlNodeElement
	 *            XML-Knoten aus der Konfiguration für dieses Element
	 * @param ssQuellSchema
	 *            Quellschema mit allen Elementen und Typen
	 * @param xsElementsToInsert
	 *            Liste aller Elemente, die im aktuellen Profil verwendet werden; diese werden erst gesammelt und nach den Typen geschrieben.
	 * @param xsElementsInserted
	 *            Liste aller Elemente, die bereits hinzugefügt wurden
	 * @param schemaWriter
	 *            Objekt zum Erzeugen des XML-Textes für die zu erzeugende Schemadatei
	 */
	private void erzeugeSchemaTypUndElement(XSElementDecl currXsElement, Node xmlNodeElement,
			XSSchemaSet ssQuellSchema, List<XSElementDecl> xsElementsToInsert, List<XSElementDecl> xsElementsInserted,
			MySchemaWriter schemaWriter) {
		if (!xsElementsInserted.contains(currXsElement)) {
			// angegebenen Typ bestimmen
			XSType currXsType = currXsElement.getType();
			if ((currXsType != null) && (currXsType.getName() == null)) {
				// Typ hat keine Bezeichnung und muss mit dem Element sofort geschrieben werden
				schemaWriter.elementDecl(currXsElement, "", null, xmlNodeElement, "");
				xsElementsInserted.add(currXsElement);
			} else {
				// XSD-Element im Zielschema einfügen
				xsElementsToInsert.add(currXsElement);
				if (currXsType != null) {
					// XSD-Typ schreiben
					if (currXsType instanceof XSComplexType) {
						schemaWriter.complexType(currXsType.asComplexType(), null, xmlNodeElement);
					} else if (currXsType instanceof XSSimpleType) {
						schemaWriter.simpleType(currXsType.asSimpleType(), null, xmlNodeElement);
					}
				}
			}
		}
	}

	/**
	 * Im SchemaWriter sind alle referenzierten Typen aufgesammelt worden; diese werden mit dieser Methode geschrieben. Dabei wird berücksichtigt, dass hierbei wieder Typen referenziert sein können.
	 * 
	 * @param sw
	 *            Objekt zum Erzeugen des XML-Textes für die zu erzeugende Schemadatei
	 * @param ssQuellSchema
	 *            Quellschema mit allen Elementen und Typen
	 * @param xsElementsToInsert
	 *            Liste aller Elemente, die im aktuellen Profil verwendet werden; diese werden erst gesammelt und nach den Typen geschrieben.
	 * @param xsElementsInserted
	 *            Liste aller Elemente, die bereits geschrieben wurden
	 */
	private void printRefElements(MySchemaWriter sw, XSSchemaSet ssQuellSchema, List<XSElementDecl> xsElementsToInsert,
			List<XSElementDecl> xsElementsInserted) {
		// Liste kopieren
		List<XSElementDecl> listElementsRef = new LinkedList<XSElementDecl>();
		for (XSElementDecl currElement : sw.getXsElementsReferenced()) {
			listElementsRef.add(currElement);
		}

		// interne Liste SchemaWriter zurücksetzen
		sw.setXsElementsReferenced(new LinkedList<XSElementDecl>());

		// Elemente behandeln
		for (XSElementDecl currElement : listElementsRef) {
			if (!xsElementsInserted.contains(currElement) && !xsElementsToInsert.contains(currElement)) {
				erzeugeSchemaTypUndElement(currElement, null, ssQuellSchema, xsElementsToInsert, xsElementsInserted, sw);
			}
		}

		// Falls wieder neue Elemente vorhanden sind, rekursiver Aufruf
		if (sw.getXsElementsReferenced().size() > 0) {
			printRefElements(sw, ssQuellSchema, xsElementsToInsert, xsElementsInserted);
		}
	}

	/**
	 * Mit dieser Methode wird ein leeres DOM-Dokument mit dem Wurzelelement erzeugt.
	 * 
	 * @param strRootElement
	 *            Bezeichnung des Wurzelelements
	 * @return Leeres DOM-Dokument mit Wurzelelement
	 * @throws ParserConfigurationException
	 */
	private Document erzeugeLeeresDokument(String strRootElement)
			throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setNamespaceAware(true);
		DocumentBuilder parser = docFactory.newDocumentBuilder();
		// Leeres DOM Dokument erstellen
		Document doc = parser.newDocument();
		// root-Element erstellen
		Element root = doc.createElement(strRootElement);
		// root-Element dem Dokument hinzufügen
		doc.appendChild(root);
		return doc;
	}
}
