/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.extra.xtt.util;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSchemaSet;

import de.extra.xtt.gui.model.ProfilingTreeModel;
import de.extra.xtt.gui.model.ProfilingTreeNode;
import de.extra.xtt.util.pdf.PdfCreator;
import de.extra.xtt.util.pdf.PdfCreatorImpl;
import de.extra.xtt.util.schema.SchemaElement;
import de.extra.xtt.util.tools.Configurator;
import de.extra.xtt.util.tools.Configurator.SchemaType;
import de.extra.xtt.util.tools.XsdXmlHelper;

/**
 * Controller für die Anwendung; kapselt alle notwendigen Funktionen für die
 * Oberfläche.
 * 
 * @author Beier
 */
public class XsdCreatorCtrlImpl implements XsdCreatorCtrl {

	private static Logger logger = Logger.getLogger(XsdCreatorCtrlImpl.class);
	private final Configurator configurator;

	private Document docProfilXml;
	private Configurator.SchemaType schemaType;
	private XSSchemaSet ssQuellSchema;
	private String pathCurrentQuellSchema;
	private String pathCurrentXmlConfig;
	private String targetNamespace;
	private String bezeichnungKurzVerfahren;
	private String bezeichnungVerfahren;

	/**
	 * Initialisiert das Konfiguratorobjekt
	 * 
	 * @param configurator
	 *            Konfiguratorobjekt mit Zugriff auf Properties und
	 *            Einstellungen
	 */
	public XsdCreatorCtrlImpl(Configurator configurator) {
		this.configurator = configurator;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public ProfilingTreeModel createTreeModelForRequest()
			throws XsdCreatorCtrlException {
		try {
			schemaType = SchemaType.REQUEST;
			pathCurrentQuellSchema = configurator
					.getPropertyUser(Configurator.PropBezeichnungUser.VERZEICHNIS_BASIS_SCHEMATA)
					+ configurator
							.getPropertyUser(Configurator.PropBezeichnungUser.DATEINAME_XSD_REQUEST);
			ssQuellSchema = XsdXmlHelper.leseXsd(pathCurrentQuellSchema);
			targetNamespace = configurator
					.getSchemaTypeNsUrl(SchemaType.REQUEST);
			return createTreeModel(ssQuellSchema, true);
		} catch (Exception e) {
			throw new XsdCreatorCtrlException(
					"Fehler beim Erstellen des TreeModels für das Request-Schema.",
					e);
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public ProfilingTreeModel createTreeModelForResponse()
			throws XsdCreatorCtrlException {
		try {
			schemaType = SchemaType.RESPONSE;
			pathCurrentQuellSchema = configurator
					.getPropertyUser(Configurator.PropBezeichnungUser.VERZEICHNIS_BASIS_SCHEMATA)
					+ configurator
							.getPropertyUser(Configurator.PropBezeichnungUser.DATEINAME_XSD_RESPONSE);
			ssQuellSchema = XsdXmlHelper.leseXsd(pathCurrentQuellSchema);
			targetNamespace = configurator
					.getSchemaTypeNsUrl(SchemaType.RESPONSE);
			return createTreeModel(ssQuellSchema, true);
		} catch (Exception e) {
			throw new XsdCreatorCtrlException(
					"Fehler beim Erstellen des TreeModels f�r das Response-Schema.",
					e);
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public ProfilingTreeModel createTreeModelForCurrentConfig()
			throws XsdCreatorCtrlException {
		if (docProfilXml != null) {
			try {
				// TreeModel f�r Ausgangsschema erzeugen
				ProfilingTreeModel treeModel = createTreeModel(ssQuellSchema,
						false);
				// Knoten m�ssen nach der Konfiguration selektiert werden
				treeModel.applyXmlConfig(docProfilXml);
				return treeModel;
			} catch (Exception e) {
				throw new XsdCreatorCtrlException(
						"Fehler beim Erstellen des TreeModels.", e);
			}
		} else {
			throw new XsdCreatorCtrlException(
					"Aktuelle Profilkonfiguration ung�ltig (NULL).");
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void setXmlFileConfig(File fileConfig)
			throws XsdCreatorCtrlException {
		try {
			// Anmerkungen zur�cksetzen
			configurator.reloadPropertiesAnmerkungen();

			if (fileConfig == null) {
				docProfilXml = null;
				pathCurrentXmlConfig = "";
				pathCurrentQuellSchema = "";
				bezeichnungKurzVerfahren = "";
				bezeichnungVerfahren = "";
			} else {
				pathCurrentXmlConfig = fileConfig.getPath();
				loadXmlProfil(pathCurrentXmlConfig);
			}
		} catch (Exception e) {
			throw new XsdCreatorCtrlException(
					"Fehler beim Laden der Profilkonfiguration.", e);
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public boolean validateBezeichnungKurzVerfahren(String bezKurzVerfahren) {
		if ((bezKurzVerfahren == null) || (bezKurzVerfahren.equals(""))) {
			return false;
		} else {
			Pattern p = Pattern.compile("^[a-zA-Z0-9_]*");
			Matcher m = p.matcher(bezKurzVerfahren);
			boolean valid = m.matches();
			return valid;
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public String getTargetNamespace() {
		return targetNamespace;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public boolean isDocXmlLoaded() {
		return (docProfilXml != null);
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public String getBezeichnungKurzVerfahren() {
		return bezeichnungKurzVerfahren;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public String getBezeichnungVerfahren() {
		return bezeichnungVerfahren;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void createXmlProf(ProfilingTreeModel treeModelMain,
			ProfilingTreeModel treeModelAdd, String targetNamespace,
			String bezVerfahrenKurz, String bezVerfahren)
			throws XsdCreatorCtrlException {
		if (treeModelMain != null) {
			ProfilingTreeNode rootNodeRef = null;
			if (treeModelAdd != null) {
				rootNodeRef = (ProfilingTreeNode) treeModelAdd.getRoot();
			}
			this.targetNamespace = targetNamespace;
			this.bezeichnungKurzVerfahren = bezVerfahrenKurz;
			this.bezeichnungVerfahren = bezVerfahren;
			// Falls ein TreeModel mit referenzierten Elementen vorhanden ist,
			// dann zuerst beide Models zusammenf�hren
			ExtraTailoring tailoring = new ExtraTailoringImpl(configurator);
			try {
				docProfilXml = tailoring.erzeugeProfilkonfiguration(schemaType,
						(ProfilingTreeNode) treeModelMain.getRoot(),
						rootNodeRef, targetNamespace, bezVerfahrenKurz,
						bezVerfahren);
			} catch (ExtraTailoringException e) {
				throw new XsdCreatorCtrlException(
						"Fehler beim Erzeugen des XML-Dokuments f�r die Profilkonfiguration.",
						e);
			}
		} else {
			throw new XsdCreatorCtrlException("Ung�ltiges TreeModel.");
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public Map<String, Document> createSchemaProf()
			throws XsdCreatorCtrlException {
		try {
			// Schema profilieren ...
			if (logger.isInfoEnabled()) {
				logger.info("Generiere profiliertes Schema ...");
			}
			ExtraTailoring tailoring = new ExtraTailoringImpl(configurator);
			Map<String, Document> docZielSchema = tailoring
					.erzeugeProfiliertesExtraSchema(ssQuellSchema,
							docProfilXml, bezeichnungKurzVerfahren);
			return docZielSchema;
		} catch (ExtraTailoringException e) {
			throw new XsdCreatorCtrlException(
					"Fehler beim Erzeugen des profilierten Schemas.", e);
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void createPdfDoku(String pathFileDoku, String filePathSchema)
			throws XsdCreatorCtrlException {
		if ((pathFileDoku == null) || (filePathSchema == null)) {
			throw new XsdCreatorCtrlException(
					"Ung�ltiger Pfad f�r Doku- bzw. Schemadatei.");
		} else {
			try {
				// Doku erzeugen profilieren ...
				if (logger.isInfoEnabled()) {
					logger.info("Generiere PDF-Dokumentation ...");
				}
				XSSchemaSet schema = XsdXmlHelper.leseXsd(filePathSchema);
				PdfCreator pdfCreator = new PdfCreatorImpl(configurator);
				pdfCreator.erzeugePdfDoku(pathFileDoku, schema,
						bezeichnungVerfahren);
			} catch (Exception e) {
				throw new XsdCreatorCtrlException(
						"Fehler beim Erzeugen der PDF-Dokumentation.", e);
			}
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public String getPathQuellSchema() {
		return pathCurrentQuellSchema;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public String getPathXmlConfig() {
		return pathCurrentXmlConfig;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void saveXmlConfig(String path) throws XsdCreatorCtrlException {
		try {
			pathCurrentXmlConfig = path;
			XsdXmlHelper.schreibeXsdXml(path, docProfilXml);
		} catch (Exception e) {
			throw new XsdCreatorCtrlException(
					"Fehler beim Speichern der XML-Datei.", e);
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void saveXsdSchema(String path, Map<String, Document> docXsd)
			throws XsdCreatorCtrlException {
		try {
			for (Map.Entry<String, Document> currEntry : docXsd.entrySet()) {
				String dateiname = path
						+ "\\"
						+ configurator.getDateinameFuerSchema(
								bezeichnungKurzVerfahren, currEntry.getKey());
				XsdXmlHelper.schreibeXsdXml(dateiname, currEntry.getValue());
				logger.info("Schemadatei '" + dateiname
						+ "' erfolgreich gespeichert.");
			}
		} catch (Exception e) {
			throw new XsdCreatorCtrlException(
					"Fehler beim Speichern des XSD-Schemas.", e);
		}
	}

	/**
	 * L�dt die Profilkonfiguration aus der �bergebenen Datei; anschlie�end wird
	 * das XML-Dokument validiert und das entsprechende Schema geladen.
	 * 
	 * @param fileNameProfilXml
	 *            Dateiname der zu ladenden Profilkonfiguration
	 * @throws XsdCreatorCtrlException
	 */
	private void loadXmlProfil(String fileNameProfilXml)
			throws XsdCreatorCtrlException {
		try {

			// Profilkonfiguration einlesen
			if (logger.isInfoEnabled()) {
				logger.info(String.format(
						"Lese Profilkonfiguration ('%s') ...",
						fileNameProfilXml));
			}
			docProfilXml = XsdXmlHelper.leseXsdXml(fileNameProfilXml);

			// Profilkonfiguration validieren (Schema und semantische Pr�fung)
			if (logger.isInfoEnabled()) {
				logger.info("Validieren Profilkonfiguration ...");
			}
			XsdXmlHelper.validiereProfilXml(docProfilXml,
					configurator.getPathTailoringSchema());

			// Core-Schema (entweder Request oder Response) einlesen
			setPathAndTypeCoreSchema(docProfilXml);
			// TargetNamespace auslesen
			targetNamespace = XsdXmlHelper
					.getTargetNamespaceFromXmlProf(docProfilXml);
			// Kurzbezeichnung f�r Verfahren auslesen
			bezeichnungKurzVerfahren = XsdXmlHelper
					.getBezeichnungKurzVerfahrenFromXmlProf(docProfilXml);
			// Bezeichnung f�r Verfahren auslesen
			bezeichnungVerfahren = XsdXmlHelper
					.getBezeichnungVerfahrenFromXmlProf(docProfilXml);

			// Anmerkungen einlesen und Properties aktualisieren
			// 1. allg. Anmerkungen
			String xPathStr = "//element[count(./anmerkung)=1]";
			NodeList nl = XsdXmlHelper.xpathSuche(xPathStr, docProfilXml);
			for (int i = 0; i < nl.getLength(); i++) {
				Node currElementNode = nl.item(i);
				String nodeName = XsdXmlHelper
						.xpathSuche("./name/text()", currElementNode).item(0)
						.getNodeValue();
				SchemaElement currSE = configurator.getSchemaElement(nodeName);
				String nodeAnmerkungText = XsdXmlHelper
						.xpathSuche("./anmerkung/text()", currElementNode)
						.item(0).getNodeValue();
				configurator.setAnmerkungAllgemein(currSE, nodeAnmerkungText);
			}
			// 2. Anmerkungen zur Verwendung
			xPathStr = "//element/kind[count(@anmerkung)=1]";
			nl = XsdXmlHelper.xpathSuche(xPathStr, docProfilXml);
			for (int i = 0; i < nl.getLength(); i++) {
				Node currKindNode = nl.item(i);
				String nodeKindName = XsdXmlHelper
						.xpathSuche("./text()", currKindNode).item(0)
						.getNodeValue();
				String nodeParentName = XsdXmlHelper
						.xpathSuche("./name/text()",
								currKindNode.getParentNode()).item(0)
						.getNodeValue();
				SchemaElement currKindSE = configurator
						.getSchemaElement(nodeKindName);
				SchemaElement currParentSE = configurator
						.getSchemaElement(nodeParentName);
				String nodeAnmerkungText = XsdXmlHelper
						.xpathSuche("./attribute::anmerkung", currKindNode)
						.item(0).getNodeValue();
				configurator.setAnmerkungVerwendung(currKindSE, currParentSE,
						nodeAnmerkungText);
			}

			if (logger.isInfoEnabled()) {
				logger.info(String.format("Lese Core-Schema ('%s') ...",
						pathCurrentQuellSchema));
			}
			ssQuellSchema = XsdXmlHelper.leseXsd(pathCurrentQuellSchema);
		} catch (Exception e) {
			throw new XsdCreatorCtrlException(
					"Fehler beim Laden der Profilkonfiguration.", e);
		}
	}

	/**
	 * Aus der �bergebenen Profilkonfiguration wird der Typ und der Pfad des
	 * passenden Schemas (Request oder Response) bestimmt und gespeichert.
	 * 
	 * @param docProfilXml
	 *            Profilkonfiguration, f�r die das Schema bestimmt werden soll
	 * @throws XPathExpressionException
	 */
	private void setPathAndTypeCoreSchema(Document docProfilXml)
			throws XPathExpressionException {
		// Je nach Namespace-Definition im Root-Element (XMLTransport) entweder
		// Request- oder Response-Schema w�hlen
		try {
			String strWurzelElement = configurator
					.getPropertySystem(Configurator.PropBezeichnungSystem.SCHEMA_ROOT_ELEMENT);
			NodeList list = XsdXmlHelper.xpathSuche(
					"//element/name[text()='xres:" + strWurzelElement + "']",
					docProfilXml);
			if ((list != null) && (list.getLength() > 0)) {
				pathCurrentQuellSchema = configurator
						.getPropertyUser(Configurator.PropBezeichnungUser.VERZEICHNIS_BASIS_SCHEMATA)
						+ configurator
								.getPropertyUser(Configurator.PropBezeichnungUser.DATEINAME_XSD_RESPONSE);
				schemaType = SchemaType.RESPONSE;
			} else {
				pathCurrentQuellSchema = configurator
						.getPropertyUser(Configurator.PropBezeichnungUser.VERZEICHNIS_BASIS_SCHEMATA)
						+ configurator
								.getPropertyUser(Configurator.PropBezeichnungUser.DATEINAME_XSD_REQUEST);
				schemaType = SchemaType.REQUEST;
			}
		} catch (XPathExpressionException e) {
			logger.error("Zugrunde liegendes Schema konnte aus der Profilkonfiguration nicht ermittelt werden.");
			throw e;
		}
	}

	/**
	 * F�r das angegebene SchemaSet wird ein TreeModel erzeugt, das alle
	 * Haupt-Elemente und alle referenzierten Elemente in einem eigenen
	 * TreeModel enth�lt.
	 * 
	 * @param schemaSet
	 *            SchemaSet, das die Elemente f�r das TreeModel enth�lt
	 * @param allCheck
	 *            Gibt an, ob standardm��ig alle Knoten des TreeModels
	 *            selektiert sein sollen
	 * @return TreeModel mit den Haupt- und referenzierten Elementen
	 * @throws XsdCreatorCtrlException
	 */
	private ProfilingTreeModel createTreeModel(XSSchemaSet schemaSet,
			boolean allCheck) throws XsdCreatorCtrlException {
		try {
			// Liste mit doppelt referenzierten Elementen erstellen
			LinkedList<SchemaElement> listMultipleElements = XsdXmlHelper
					.getMultipleReferencedElements(schemaSet, configurator);

			// F�r den "Hauptbaum" werden die mehrfach referenzierten Elemente
			// ohne Kinder erzeugt
			String nsUrl = configurator.getSchemaTypeNsUrl(schemaType);
			String strWurzelElement = configurator
					.getPropertySystem(Configurator.PropBezeichnungSystem.SCHEMA_ROOT_ELEMENT);
			SchemaElement seWurzel = new SchemaElement(strWurzelElement, nsUrl,
					configurator.getPropertyNamespace(nsUrl));
			XSElementDecl currElement = schemaSet.getElementDecl(
					seWurzel.getNsUrl(), seWurzel.getName());
			ProfilingTreeNode treeNodeMain = XsdXmlHelper
					.generateNodeForElementWithChildren(schemaSet, currElement,
							seWurzel, -1, -1, false, false, false, allCheck,
							null, listMultipleElements, configurator, false);
			ProfilingTreeModel profModelRefElements = null;
			if (listMultipleElements.size() > 0) {
				// Mehrfach referenzierte Elemente in eigenes TreeModel einf�gen
				String strTextRoot = configurator
						.getResString("XSDCREATORCTRL_TEXT_MEHRFACH_REF_ELEMENTE");
				SchemaElement seRootRef = new SchemaElement(strTextRoot, "", "");
				ProfilingTreeNode rootNode = new ProfilingTreeNode(seRootRef,
						-1, -1, false, false, false, allCheck, false, false,
						null);
				// Liste zuerst sortieren
				Collections.sort(listMultipleElements);
				Vector<ProfilingTreeNode> vecChildren = new Vector<ProfilingTreeNode>();
				for (SchemaElement currSchemaElement : listMultipleElements) {
					@SuppressWarnings("unchecked")
					LinkedList<SchemaElement> listMultipleWithoutCurrent = (LinkedList<SchemaElement>) listMultipleElements
							.clone();
					listMultipleWithoutCurrent.remove(currSchemaElement);
					currElement = schemaSet.getElementDecl(
							currSchemaElement.getNsUrl(),
							currSchemaElement.getName());
					ProfilingTreeNode currTreeNode = XsdXmlHelper
							.generateNodeForElementWithChildren(schemaSet,
									currElement, currSchemaElement, -1, -1,
									false, false, false, allCheck, rootNode,
									listMultipleWithoutCurrent, configurator,
									false);
					vecChildren.add(currTreeNode);
				}
				rootNode.addChildren(vecChildren);
				profModelRefElements = new ProfilingTreeModel(rootNode, null);
			}

			ProfilingTreeModel profModelMain = new ProfilingTreeModel(
					treeNodeMain, profModelRefElements);
			return profModelMain;
		} catch (XPathExpressionException e) {
			throw new XsdCreatorCtrlException(
					"Fehler beim Erzeugen des TreeModels.", e);
		}
	}
}
