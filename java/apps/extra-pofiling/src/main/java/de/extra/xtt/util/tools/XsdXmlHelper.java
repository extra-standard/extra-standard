package de.extra.xtt.util.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.XSWildcard;
import com.sun.xml.xsom.XSModelGroup.Compositor;
import com.sun.xml.xsom.impl.ElementDecl;
import com.sun.xml.xsom.parser.XSOMParser;

import de.extra.xtt.gui.model.ProfilingTreeNode;
import de.extra.xtt.util.ValidationException;
import de.extra.xtt.util.schema.AnnotationFactory;
import de.extra.xtt.util.schema.MySchemaWriter;
import de.extra.xtt.util.schema.SchemaElement;

/**
 * Diese Klasse stellt Hilfsfunktionen für die Verarbeitung von XML- und XSD-Dateien zur Verfügung.
 * 
 * @author Beier
 * 
 */
public class XsdXmlHelper {

	private static Logger logger = Logger.getLogger(XsdXmlHelper.class);

	/**
	 * Lest und erstellt ein DOM-Dokument aus dem übergebenen Dateinamen.
	 * 
	 * @param fileName
	 *            Dateiname für die XML-Datei
	 * @return XML-Datei als DOM-Dokument
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document leseXsdXml(String fileName) throws ParserConfigurationException, SAXException, IOException {
		// Datei einlesen und als DOM-Baum zurück geben
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setNamespaceAware(true);
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		// Wichtig, hier das Fileobjekt zu verwenden, da damit der WindowsXP-Bug umgangen wird (kann keine Leerzeichen
		// im Pfad verarbeiten)
		Document doc = docBuilder.parse(new File(fileName));
		return doc;
	}

	/**
	 * Liest und erstellt ein XSD-SchemaSet aus der angegebenen Datei. Dabei werden auch verknüpfte Schemadateien
	 * eingelesen.
	 * 
	 * @param fileName
	 *            Dateiname der einzulesenden Schemadatei
	 * @return SchemSet mit allen Elementen und Typen des angegebenen Schemas
	 * @throws SAXException
	 * @throws IOException
	 */
	public static XSSchemaSet leseXsd(String fileName) throws SAXException, IOException {
		XSOMParser parser = new XSOMParser();
		parser.setAnnotationParser(new AnnotationFactory());
		parser.parse(new File(fileName));
		return parser.getResult();
	}

	/**
	 * Das übergabene XMl-Dokument wird asl Textdatei unter Verwendung des angegebenen Dateinamens gespeichert.
	 * 
	 * @param fileName
	 *            Dateiname für die neue Textdatei
	 * @param document
	 *            Zu speicherndes XML-Dokument
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public static void schreibeXsdXml(String fileName, Document document) throws TransformerFactoryConfigurationError,
			TransformerException {
		// überflüssige Leerzeichen entfernen
		Node firstElement = document.getFirstChild();
		while (!(firstElement instanceof Element)) {
			firstElement = firstElement.getNextSibling();
		}
		entferneLeerzeichenRekursiv((Element) firstElement);
		// übergebenen DOM-Baum in die Datei speichern.
		Transformer tFormer = TransformerFactory.newInstance().newTransformer();
		tFormer.setOutputProperty(OutputKeys.METHOD, "xml");
		// Einzug mit 4 Zeichen
		tFormer.setOutputProperty(OutputKeys.INDENT, "yes");
		tFormer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		Source source = new DOMSource(document);
		Result result = new StreamResult(new File(fileName));
		tFormer.transform(source, result);
	}

	/**
	 * Die übergebene Profilkonfiguration in Form eines XML-Dokuments wird gegen das angegebene Schema validiert.
	 * Außerdem erfolgt eine semantische Prüfung der Profilkonfiguration, bei der geprüft wird, ob alle angegebenen
	 * Kind-Knoten wieder als eigene Elemente aufgeführt sind.
	 * 
	 * @param docProfilXml
	 *            Zu validierende Profilkonfiguration
	 * @param dateiNameSchema
	 *            Dateiname des Schemas für die Profilkonfiguration
	 * @throws ValidationException
	 *             Ausnahme wird erzeugt, falls die profilkonfiguration semantisch inkorrekt ist
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 */
	public static void validiereProfilXml(Document docProfilXml, String dateiNameSchema) throws ValidationException,
			SAXException, IOException, XPathExpressionException {
		// 1. Schema-Validierung
		// SchemaFactory und ein Schema-Objekt erstellen
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		InputStream is = XsdXmlHelper.class.getResourceAsStream(dateiNameSchema);
		Schema schema = schemaFactory.newSchema(new StreamSource(is));
		is.close();
		// Validatorobjekt erstellen und XML-Dokument validieren
		Validator validator = schema.newValidator();
		validator.validate(new DOMSource(docProfilXml));
		// 2. Semantische Prüfung
		// Alle Kindknoten müssen wieder als eigenes Element aufgeführt sein
		// Sammle alle Kind-Knoten
		NodeList nodesKindElemente = xpathSuche("//kind/text()", docProfilXml);
		for (int i = 0; i < nodesKindElemente.getLength(); i++) {
			String strCurrNodeValue = nodesKindElemente.item(i).getNodeValue();
			NodeList nodesMainElement = xpathSuche("//element/name[text()='" + strCurrNodeValue + "']", docProfilXml);
			// Für den Kindknoten muss ein entsprechendes Haupt-Element definiert sein
			if (nodesMainElement.getLength() == 0) {
				throw new ValidationException(String.format(
						"Für das Kind-Element '%s' ist kein eigenes Element definiert.", strCurrNodeValue));
			}
		}
	}

	/**
	 * Für das übergebene XSD-SchemaSet werden alle mehrfach referenzierten Elemente bestimmt.
	 * 
	 * @param sset
	 *            SchemaSet, in dem nach mehrfach referenzierten Elementen gesucht wird
	 * @param configurator
	 *            Configurator-Objekt mit dem Zugriff auf Properties und sonstige Einstellungen
	 * @return Liste mit allen mehrfach referenzierten Elementen
	 */
	public static LinkedList<SchemaElement> getMultipleReferencedElements(XSSchemaSet sset, Configurator configurator) {
		if (sset != null) {
			LinkedList<String> listRefElements = new LinkedList<String>();
			LinkedList<SchemaElement> listMultipleElements = new LinkedList<SchemaElement>();
			// Alle Elemente durchlaufen
			Iterator<XSElementDecl> iterator = sset.iterateElementDecls();
			while (iterator.hasNext()) {
				XSElementDecl currElement = iterator.next();
				XSType currType = currElement.getType();
				// Nur complexTypes betrachten
				if (currType.isComplexType()) {
					XSParticle currParticle = null;
					if (currType.asComplexType().getExplicitContent() != null) {
						currParticle = currType.asComplexType().getExplicitContent().asParticle();
					} else if (currType.asComplexType().getContentType() != null) {
						currParticle = currType.asComplexType().getContentType().asParticle();
					}
					if ((currParticle != null) && (currParticle.getTerm() instanceof XSModelGroup)) {
						XSModelGroup currModelGroup = currParticle.getTerm().asModelGroup();
						getMultipleReferencedElementsForModelGroup(sset, configurator, listRefElements,
								listMultipleElements, currModelGroup);
					}
				}
			}
			return listMultipleElements;
		} else {
			return new LinkedList<SchemaElement>();
		}
	}

	/**
	 * Für eine ModelGroup (sequence oder choice) werden mehrfach referenzierte Elemente unter Berücksichtung der
	 * bereits gefundenen Referenzen bestimmt.
	 * 
	 * @param sset
	 *            SchemaSet, das alle relevanten Elemente und Typen enthält
	 * @param configurator
	 *            Configurator-Objekt mit dem Zugriff auf Properties und sonstige Einstellungen
	 * @param listRefElements
	 *            Liste, das alle bisher gefundenen referenzierten Elemente enthält (mindestens eine Referenz)
	 * @param listMultipleElements
	 *            Liste mit bisher gefundenen mehrfach referenzierten Elementen
	 * @param modelGroup
	 *            ModelGroup, in der aktuell gesucht werden soll
	 */
	private static void getMultipleReferencedElementsForModelGroup(XSSchemaSet sset, Configurator configurator,
			LinkedList<String> listRefElements, LinkedList<SchemaElement> listMultipleElements, XSModelGroup modelGroup) {
		// Kinder durchlaufen
		for (XSParticle currChild : modelGroup.getChildren()) {
			if (currChild.getTerm() instanceof ElementDecl) {
				XSElementDecl currChildElement = currChild.getTerm().asElementDecl();
				getMultipleReferencedElementsForElement(sset, configurator, listRefElements, listMultipleElements,
						currChildElement);
			} else if (currChild.getTerm() instanceof XSModelGroup) {
				XSModelGroup currModelGroup = currChild.getTerm().asModelGroup();
				getMultipleReferencedElementsForModelGroup(sset, configurator, listRefElements, listMultipleElements,
						currModelGroup);
			} else if (currChild.getTerm() instanceof XSWildcard) {
				// Element von sequence/choice kann ein Wildcard-Objekt sein, z.B. 'xs:any'
				XSWildcard wildCard = currChild.getTerm().asWildcard();
				String currNamespaceStr = wildCard.apply(MySchemaWriter.WILDCARD_NS);
				if ((currNamespaceStr != null) && (currNamespaceStr.length() > 0) && !(currNamespaceStr.contains("##"))) {
					// Alle Elemente merken, die per Wildcard hier verwendet werden dürfen
					// Dazu alle Schemata durchlaufen und prüfen, ob der Targetnamespace von Wildcard akzeptiert wird
					for (XSSchema currSchema : sset.getSchemas()) {
						if (wildCard.acceptsNamespace(currSchema.getTargetNamespace())) {
							// Alle Elemente dieses Namespaces durchgehen und behandeln
							Map<String, XSElementDecl> elementDecls = currSchema.getElementDecls();
							for (Map.Entry<String, XSElementDecl> currEntry : elementDecls.entrySet()) {
								XSElementDecl currElement = currEntry.getValue();
								getMultipleReferencedElementsForElement(sset, configurator, listRefElements,
										listMultipleElements, currElement);
							}
						}
					}
				}
			}
		}

	}

	/**
	 * Für ein Schema-Element werden mehrfach referenzierte Elemente unter Berücksichtung der bereits gefundenen
	 * Referenzen bestimmt.
	 * 
	 * @param sset
	 *            SchemaSet, das alle relevanten Elemente und Typen enthält
	 * @param configurator
	 *            Configurator-Objekt mit dem Zugriff auf Properties und sonstige Einstellungen
	 * @param listRefElements
	 *            Liste, das alle bisher gefundenen referenzierten Elemente enthält (mindestens eine Referenz)
	 * @param listMultipleElements
	 *            Liste mit bisher gefundenen mehrfach referenzierten Elementen
	 * @param currChildElement
	 *            Element, in dem aktuell gesucht werden soll
	 */
	private static void getMultipleReferencedElementsForElement(XSSchemaSet sset, Configurator configurator,
			LinkedList<String> listRefElements, LinkedList<SchemaElement> listMultipleElements,
			XSElementDecl currChildElement) {
		// Prüfen, ob dieses Element selbst Kinder hat (nur dann in die Liste aufnehmen)
		if (schemaElementHasChilds(sset, currChildElement.getName(), currChildElement.getTargetNamespace())) {
			String strBezeichnung = currChildElement.getName() + "_" + currChildElement.getTargetNamespace();
			if (listRefElements.contains(strBezeichnung)) {
				// Falls schon mal referenziert, dann in die Liste mit den mehrfach referenzierten Elementen einfügen
				// (falls dort noch nicht eingetragen)
				SchemaElement newSchemaElement = new SchemaElement(currChildElement.getName(), currChildElement
						.getTargetNamespace(), configurator.getPropertyNamespace(currChildElement.getTargetNamespace()));
				if (!listMultipleElements.contains(newSchemaElement)) {
					listMultipleElements.add(newSchemaElement);
				}
			} else {
				// Falls erstes Auftreten, dann in die Liste der referenzierten Elemente einfügen
				listRefElements.add(strBezeichnung);
			}
		}
	}

	/**
	 * Ausgehend vom angegebenen Knoten-Element (kann auch ein DOM-Dokument sein) wird eine XPath-Suche mit dem
	 * übergebenen Ausdruck durchgeführt.
	 * 
	 * @param strXpathExpr
	 *            XPath-Ausdruck
	 * @param nodeToEvaluate
	 *            DOM-Knoten, an dem die Suche startet
	 * @return Liste der gefundenen Knoten
	 * @throws XPathExpressionException
	 */
	public static NodeList xpathSuche(String strXpathExpr, Node nodeToEvaluate) throws XPathExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		// Quelldokument suchen (für Namespace-Resolver)
		Document docToEvaluate = nodeToEvaluate.getOwnerDocument();
		if (nodeToEvaluate instanceof Document) {
			docToEvaluate = (Document) nodeToEvaluate;
		}
		// Für die Suche nach Namespace-Elementen ist der Namespace-Resolver notwendig
		xpath.setNamespaceContext(new UniversalNamespaceResolver(docToEvaluate));
		NodeList nodes = null;
		try {
			nodes = (NodeList) xpath.evaluate(strXpathExpr, nodeToEvaluate, XPathConstants.NODESET);
		} catch (XPathExpressionException ex) {
			// Evtl. kann Namespace-Prefix nicht aufgelöst werden
			if (strXpathExpr.contains("xs:")) {
				strXpathExpr = strXpathExpr.replace("xs:", "");
				nodes = (NodeList) xpath.evaluate(strXpathExpr, nodeToEvaluate, XPathConstants.NODESET);
			} else {
				throw ex;
			}
		}
		return nodes;
	}

	/**
	 * Für ein Element des Schemas wird ein entsprechendes TreeNode-Objekt mit Kind-Elementen (falls vorhanden) erzeugt.
	 * Dabei wird bestimmt, ob der Knoten eine Referenz auf einen mehrfach referenzierten Knoten besitzt. Bei der
	 * Erstellung der Kind-Elemente wird diese Methode wieder rekursiv aufgerufen.
	 * 
	 * @param schemaSet
	 *            Aktuell verwendetes SchemaSet
	 * @param element
	 *            Element aus dem Schema, für das der TreeNode erzeugt werden soll
	 * @param schemaElementRoot
	 *            SchemaElement, unter dem das aktuelle Element verwendet wird
	 * @param minOccurs
	 *            minOccurs-Wert de aktuellen Elements
	 * @param maxOccurs
	 *            maxOccurs-Wert des aktuellen Elements
	 * @param belongsToChoice
	 *            Gibt an, ob das aktuelle Element zu einer choice gehört
	 * @param belongsToSequence
	 *            Gibt an, ob das aktuelle Element zu einer sequence gehört
	 * @param belongsToWildcard
	 *            Gibt an, ob das aktuelle Element über ein wildcard-Element referenziert wird
	 * @param checkAll
	 *            Gibt an, ob alle Knoten standardmäßig selektiert sein sollen
	 * @param parent
	 *            Vater-TreeNode, zu dem dieser neu zu erzeugende Knoten als Kindknoten eingefügt werden soll
	 * @param listMultipleElements
	 *            Liste der mehrfach referenzierten Elemente
	 * @param configurator
	 *            Configurator-Objekt mit dem Zugriff auf Properties und sonstige Einstellungen
	 * @param rekursiv
	 *            Gibt an, ob diese Methode rekursiv aufgerufen wurde
	 * @return TreeNode-Ojekt zum angegebenen Schema-Element inkl. Kind-Knoten
	 * @throws XPathExpressionException
	 */
	public static ProfilingTreeNode generateNodeForElementWithChildren(XSSchemaSet schemaSet, XSElementDecl element,
			SchemaElement schemaElementRoot, int minOccurs, int maxOccurs, boolean belongsToChoice,
			boolean belongsToSequence, boolean belongsToWildcard, boolean checkAll, ProfilingTreeNode parent,
			LinkedList<SchemaElement> listMultipleElements, Configurator configurator, boolean rekursiv)
			throws XPathExpressionException {
		if (element != null) {
			// Schema-Element erstellen
			SchemaElement schemaElement = new SchemaElement(element.getName(), element.getTargetNamespace(),
					configurator.getPropertyNamespace(element.getTargetNamespace()));
			boolean hasReferenzExtern = listMultipleElements.contains(schemaElement)
					|| (rekursiv && (schemaElementRoot != null) && schemaElementRoot.equals(schemaElement));
			boolean isLocalElement = element.isLocal();
			// Neues Knotenobjekt für Element erstellen
			ProfilingTreeNode newNode = new ProfilingTreeNode(schemaElement, minOccurs, maxOccurs, belongsToChoice,
					belongsToSequence, belongsToWildcard, checkAll, hasReferenzExtern, isLocalElement, parent);
			// Kind-Elemente für Knoten mit externer Referenz nicht erzeugen
			if (!hasReferenzExtern) {
				// Suche das aktuelle Element im SchemaSat
				if (element != null) {
					// Typdefinition für Element suchen und ggf. Kindobjekte erzeugen
					XSType currType = element.getType();
					if (currType.isComplexType()) {
						XSParticle currParticle = null;
						if (currType.asComplexType().getExplicitContent() != null) {
							currParticle = currType.asComplexType().getExplicitContent().asParticle();
						} else if (currType.asComplexType().getContentType() != null) {
							currParticle = currType.asComplexType().getContentType().asParticle();
						}
						if (currParticle != null) {
							XSModelGroup modelGroup = currParticle.getTerm().asModelGroup();
							Vector<ProfilingTreeNode> children = new Vector<ProfilingTreeNode>();
							// Knoten-Objekte für aktuelle ModelGroup generieren
							generateChildrenForModelGroup(schemaSet, configurator, checkAll, listMultipleElements,
									newNode, children, modelGroup, schemaElementRoot);
							if (!children.isEmpty()) {
								newNode.addChildren(children);
							}
						}
					}
				}
			}
			return newNode;
		} else {
			return null;
		}
	}

	/**
	 * Für die Kind-Elemente einer Modelgroup (sequence / choice) des Schemas werden entsprechende TreeNode-Objekte mit
	 * Kind-Elementen (falls vorhanden) erzeugt. Dabei wird bestimmt, ob die Knoten eine Referenz auf einen mehrfach
	 * referenzierten Knoten besitzt.
	 * 
	 * @param schemaSet
	 *            Aktuell verwendetes SchemaSet
	 * @param configurator
	 *            Configurator-Objekt mit dem Zugriff auf Properties und sonstige Einstellungen
	 * @param checkAll
	 *            Gibt an, ob alle Knoten standardmäßig selektiert sein sollen
	 * @param listMultipleElements
	 *            Liste der mehrfach referenzierten Elemente
	 * @param newNode
	 *            Knoten-Objekt, in das die zu erstellenden Knoten als Kind-Elemente hinzugefügt werden
	 * @param children
	 *            Liste, in der alle neuen Kind-Elementen gesammelt werden
	 * @param modelGroup
	 *            ModelGroup-Objekt, für das Elemente erstellt werden sollen
	 * @param schemaElementRoot
	 *            SchemaElement, unter dem das aktuelle Element verwendet wird
	 * @throws XPathExpressionException
	 */
	private static void generateChildrenForModelGroup(XSSchemaSet schemaSet, Configurator configurator,
			boolean checkAll, LinkedList<SchemaElement> listMultipleElements, ProfilingTreeNode newNode,
			Vector<ProfilingTreeNode> children, XSModelGroup modelGroup, SchemaElement schemaElementRoot)
			throws XPathExpressionException {
		// Choice oder Sequence
		Compositor compositor = modelGroup.getCompositor();
		boolean belongsToSequenceChilds = compositor.equals(com.sun.xml.xsom.XSModelGroup.Compositor.SEQUENCE);
		boolean belongsToChoiceChilds = compositor.equals(com.sun.xml.xsom.XSModelGroup.Compositor.CHOICE);
		XSParticle[] childs = modelGroup.getChildren();
		for (XSParticle currChild : childs) {
			if (currChild.getTerm() instanceof ElementDecl) {
				// Elemente von sequence bzw. choice abarbeiten
				XSElementDecl currElement = currChild.getTerm().asElementDecl();
				int minOccursChild = currChild.getMinOccurs();
				int maxOccursChild = currChild.getMaxOccurs();
				if (maxOccursChild == XSParticle.UNBOUNDED) {
					maxOccursChild = Integer.MAX_VALUE;
				}
				// TreeNode-Objekt für Kind rekursiv erzeugen
				// Falls im Typ des gerade angelegten Elements wieder auf das Element verwiesen wird, dafür keine Kinder
				// mehr generieren
				ProfilingTreeNode treeNodeChild = generateNodeForElementWithChildren(schemaSet, currElement,
						schemaElementRoot, minOccursChild, maxOccursChild, belongsToChoiceChilds,
						belongsToSequenceChilds, false, checkAll, newNode, listMultipleElements, configurator, true);
				children.add(treeNodeChild);
			} else if (currChild.getTerm() instanceof XSModelGroup) {
				// Element von sequence/choice kann wieder eine ModelGroup (sequence/choice) sein
				XSModelGroup mg = currChild.getTerm().asModelGroup();
				generateChildrenForModelGroup(schemaSet, configurator, checkAll, listMultipleElements, newNode,
						children, mg, schemaElementRoot);
			} else if (currChild.getTerm() instanceof XSWildcard) {
				// Element von sequence/choice kann ein Wildcard-Objekt sein, z.B. 'xs:any'
				XSWildcard wildCard = currChild.getTerm().asWildcard();
				String currNamespaceStr = wildCard.apply(MySchemaWriter.WILDCARD_NS);
				if ((currNamespaceStr != null) && (currNamespaceStr.length() > 0) && !(currNamespaceStr.contains("##"))) {
					// Alle Elemente merken, die per Wildcard hier verwendet werden dürfen
					// Dazu alle Schemata durchlaufen und prüfen, ob der Targetnamespace von Wildcard akzeptiert wird
					for (XSSchema currSchema : schemaSet.getSchemas()) {
						if (wildCard.acceptsNamespace(currSchema.getTargetNamespace())) {
							// Alle Elemente dieses Namespaces durchgehen und entsprechende Knotenobjekte erzeugen
							Map<String, XSElementDecl> elementDecls = currSchema.getElementDecls();
							for (Map.Entry<String, XSElementDecl> currEntry : elementDecls.entrySet()) {
								XSElementDecl currElement = currEntry.getValue();
								// TreeNode-Objekt für Kind rekursiv erzeugen
								// Falls im Typ des gerade angelegten Elements wieder auf das Element verwiesen wird,
								// dafür keine Kinder mehr generieren
								int minOccursChild = currChild.getMinOccurs();
								int maxOccursChild = currChild.getMaxOccurs();
								if (maxOccursChild == XSParticle.UNBOUNDED) {
									maxOccursChild = Integer.MAX_VALUE;
								}
								ProfilingTreeNode treeNodeChild = generateNodeForElementWithChildren(schemaSet,
										currElement, schemaElementRoot, minOccursChild, maxOccursChild,
										belongsToChoiceChilds, belongsToSequenceChilds, true, checkAll, newNode,
										listMultipleElements, configurator, true);
								children.add(treeNodeChild);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Liefert den TargetNamespace aus der Profilkonfiguration.
	 * 
	 * @param docXml
	 *            Profilkonfiguration als DOM-Dokument
	 * @return TargetNamespace
	 * @throws DOMException
	 * @throws XPathExpressionException
	 */
	public static String getTargetNamespaceFromXmlProf(Document docXml) throws DOMException, XPathExpressionException {
		if (docXml != null) {
			NodeList listNodes = xpathSuche("//profil-konfiguration/attribute::tnsUrl", docXml);
			if (listNodes.getLength() > 0) {
				return listNodes.item(0).getNodeValue();
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	/**
	 * Liefert die Kurzbezeichnung des Verfahrens aus der Profilkonfiguration
	 * 
	 * @param docXml
	 *            Profilkonfiguration als DOM-Dokument
	 * @return Kurzbezeichnung des Verfahrens
	 * @throws DOMException
	 * @throws XPathExpressionException
	 */
	public static String getBezeichnungKurzVerfahrenFromXmlProf(Document docXml) throws DOMException,
			XPathExpressionException {
		if (docXml != null) {
			NodeList listNodes = xpathSuche("//profil-konfiguration/attribute::bezKurzVerfahren", docXml);
			if (listNodes.getLength() > 0) {
				return listNodes.item(0).getNodeValue();
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	/**
	 * Liefert die Vezeichnung des Verfahrens aus der Profilkonfiguration
	 * 
	 * @param docXml
	 *            Profilkonfiguration als DOM-Dokument
	 * @return Vezeichnung des Verfahrens
	 * @throws DOMException
	 * @throws XPathExpressionException
	 */
	public static String getBezeichnungVerfahrenFromXmlProf(Document docXml) throws DOMException,
			XPathExpressionException {
		if (docXml != null) {
			NodeList listNodes = xpathSuche("//profil-konfiguration/attribute::bezVerfahren", docXml);
			if (listNodes.getLength() > 0) {
				return listNodes.item(0).getNodeValue();
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	/**
	 * Prüft, ob das angegebene Schema-Element (falls vorhanden) Kindelemente besitzt.
	 * 
	 * @param sset
	 *            SchemaSet
	 * @param elementName
	 *            Name des Elements
	 * @param elementNamespace
	 *            Namespace-Präfix des Elements
	 * @return <code>true</code>, falls Element Kindelemente besitzt, sonst <code>false</code>
	 */
	private static boolean schemaElementHasChilds(XSSchemaSet sset, String elementName, String elementNamespace) {
		XSElementDecl currElement = sset.getElementDecl(elementNamespace, elementName);
		if (currElement != null) {
			XSType currType = currElement.getType();
			// Nur complexTypes betrachten
			if (currType.isComplexType()) {
				XSParticle currParticle = null;
				if (currType.asComplexType().getExplicitContent() != null) {
					currParticle = currType.asComplexType().getExplicitContent().asParticle();
				} else if (currType.asComplexType().getContentType() != null) {
					currParticle = currType.asComplexType().getContentType().asParticle();
				}
				if (currParticle != null) {
					// Kinder durchlaufen
					XSParticle[] childs = currParticle.getTerm().asModelGroup().getChildren();
					if (childs.length > 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Aus dem übergbenen DOM-Element werden alle Knoten ohne Text entfernt
	 * 
	 * @param elem
	 *            DOM-Element
	 */
	private static void entferneLeerzeichenRekursiv(Element elem) {
		NodeList children = elem.getChildNodes();
		for (int i = children.getLength() - 1; i >= 0; i--) {
			Node child = children.item(i);
			switch (child.getNodeType()) {
			case Node.ELEMENT_NODE:
				entferneLeerzeichenRekursiv((Element) child);
				break;
			case Node.CDATA_SECTION_NODE:
			case Node.TEXT_NODE:
				if ("".equals(child.getNodeValue().trim())) {
					elem.removeChild(child);
				}
				break;
			default:
				// nichts zu tun
			}
		}
	}

	/**
	 * Loggt die übergebene Knotenliste im Debug-Modus
	 * 
	 * @param listNodes
	 *            Liste von Knoten
	 */
	public static void printNodeList(NodeList listNodes) {
		for (int i = 0; i < listNodes.getLength(); i++) {
			Node nodeElement = listNodes.item(i);
			printNode(nodeElement);
		}
	}

	/**
	 * Gibt den angegebenen Knoten als Debug-String aus
	 * 
	 * @param node
	 *            Zu bahndelnder Knoten
	 */
	private static void printNode(Node node) {
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Element: %s:%s (Wert: %s) (Typ: %s)", node.getPrefix(), node.getNodeName(),
					node.getNodeValue(), node.getNodeType()));
		}
	}

}