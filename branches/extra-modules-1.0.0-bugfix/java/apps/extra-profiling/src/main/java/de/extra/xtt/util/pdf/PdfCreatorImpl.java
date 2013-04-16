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
package de.extra.xtt.util.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSAttGroupDecl;
import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSDeclaration;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSModelGroup.Compositor;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.XSWildcard;
import com.sun.xml.xsom.impl.ElementDecl;

import de.extra.xtt.util.schema.MySchemaWriter;
import de.extra.xtt.util.tools.Configurator;

/**
 * Implementierung für die Erzeugung einer PDF-Dokumentation
 * 
 * @author Beier
 */
public class PdfCreatorImpl implements PdfCreator {

	private static Logger logger = Logger.getLogger(PdfCreatorImpl.class);

	private final String fontNameStandard = FontFactory.HELVETICA;

	private final int fontSizeChapter = 18;
	private final int fontSizeElement = 14;
	private final int fontSizeText = 11;
	private final int fontSizeFooter = 9;
	private final int fontSizeTextHalf = 5;

	// Aufz�hlungszeichen f�r die erste und zweite Ebene
	private final String bulletMain = "\u2022";
	private final String bulletSub = "-";

	private final Configurator configurator;
	private Document docPdf;
	private int chapterCounter;
	private String dateiname;
	private String bezVerfahren;

	private java.util.List<ContPdfEntry> listEntries;
	private int currPageNumber;

	/**
	 * Konstruktor mit der Initialisierung des Configurator-Objekts
	 * 
	 * @param configurator
	 *            Objekt zum Zugriff auf Properties und Einstellungen
	 */
	public PdfCreatorImpl(Configurator configurator) {
		this.configurator = configurator;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void erzeugePdfDoku(String filePath, XSSchemaSet xmlSchemaSet,
			String bezVerfahren) throws PdfCreatorException {
		try {
			this.dateiname = filePath;
			this.bezVerfahren = bezVerfahren;
			this.listEntries = new java.util.LinkedList<ContPdfEntry>();

			// Erzeuge PDF-Doku ohne Inhaltsverzeichnis
			erzeugePdfDatei(xmlSchemaSet, false);

			// Erzeuge temp. Inhaltsverzeichnis
			int anzPagesInhalt = erzeugeInhaltsverzeichnis(null, true);
			// Seitenzahlen aktualisieren
			for (ContPdfEntry currEntry : listEntries) {
				currEntry.setPageNumber(currEntry.getPageNumber()
						+ anzPagesInhalt);
			}

			// Dokument inkl. Inhaltsverzeichnis neu schreiben
			erzeugePdfDatei(xmlSchemaSet, true);

			if (logger.isInfoEnabled()) {
				logger.info("PDF-Dokumentation '" + filePath
						+ "' erfolgreich gespeichert.");
			}
		} catch (Exception e) {
			throw new PdfCreatorException("Fehler beim Erzeugen der PDF-Doku.",
					e);
		}
	}

	/**
	 * Erzeugt die PDF-Doku f�r das �bergebene SchemaSet und optional mit
	 * Inhaltsverzeichnis
	 * 
	 * @param xmlSchemaSet
	 *            SchemSet mit allen Elementen und Typen
	 * @param writeInhaltsverzeichnis
	 *            Gibt an, ob ein Inhaltsverzeichnis erstellt werden soll
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void erzeugePdfDatei(XSSchemaSet xmlSchemaSet,
			boolean writeInhaltsverzeichnis) throws DocumentException,
			MalformedURLException, IOException {
		// PDF-Writer und PDF-Dokument initalisieren
		docPdf = initPdfWriterAndDocument(dateiname, true);

		// Titelseite
		erzeugeTitelSeite();

		// Inhaltsverzeichnis
		if (writeInhaltsverzeichnis) {
			erzeugeInhaltsverzeichnis(docPdf, false);
		}

		// Queues f�r Schemata anlegen
		Map<String, Queue<XSElementDecl>> schemaQueues = new HashMap<String, Queue<XSElementDecl>>();

		// Erstes Element in allen Schemata suchen und Queue-Objekte anlegen
		String strRootElement = configurator
				.getPropertySystem(Configurator.PropBezeichnungSystem.SCHEMA_ROOT_ELEMENT);
		for (XSSchema currSchema : xmlSchemaSet.getSchemas()) {
			// Neue Queue f�r aktuelles Schema anlegen
			String nsPrefix = getNsPref(currSchema.getTargetNamespace());
			schemaQueues.put(nsPrefix, new LinkedList<XSElementDecl>());

			// Pr�fen, ob erstes Element in diesem Schema enthalten ist
			XSElementDecl currFirstElement = currSchema
					.getElementDecl(strRootElement);
			if (currFirstElement != null) {
				// Element als erstes Element dieser Queue hinzuf�gen
				schemaQueues.get(nsPrefix).add(currFirstElement);
			}
		}

		// Queues der Reihe nach abarbeiten
		for (String currPrefix : configurator.getSchemaPrefixSorted()) {
			behandleSchemaQueue(currPrefix, schemaQueues, xmlSchemaSet);
		}

		// Dokument schlie�en
		docPdf.close();

	}

	/**
	 * Erzeugt das Inhaltsverzeichnis aus den bereits vorhandenen Elementen in
	 * der Liste <code>listEntries</code>.
	 * 
	 * @param docPdf
	 *            Zieldokument, falls Inhaltsverzeichnis nicht tempor�r erzeugt
	 *            wird
	 * @param temp
	 *            Gibt an, ob das Inhaltsverzeichnis tempor�r in einer neuen
	 *            Datei/Dokument erzeugt werden soll
	 * @return Anzahl der Seiten
	 * @throws DocumentException
	 * @throws IOException
	 */
	private int erzeugeInhaltsverzeichnis(Document docPdf, boolean temp)
			throws DocumentException, IOException {

		int anzPages = 0;
		Document docInhalt = docPdf;
		String filePathTempInhaltString = "";

		if (temp) {
			// temp. Dateinamen bestimmen
			File fileDokuFile = new File(dateiname);
			filePathTempInhaltString = fileDokuFile.getParent()
					+ "/tmp_inhalt.pdf";
			// Neues Dokument erzeugen
			docInhalt = initPdfWriterAndDocument(filePathTempInhaltString,
					false);
		}

		// �berschrift
		Chapter currChapter = new Chapter(
				getParagraphChapter("Inhaltsverzeichnis"), 0);
		// 0, damit keine Nummerierung
		currChapter.setNumberDepth(0);
		docInhalt.add(currChapter);
		// eine Zeile Abstand
		docInhalt.add(getEmptyLineTextHalf());

		for (ContPdfEntry currEntry : listEntries) {

			// Eintrag erzeugen inkl. Abstand
			String strEintrag = currEntry.getBezeichnung() + "  ";
			Chunk chunkBezeichnung;
			Chunk chunkSeitenzahlChunk;
			if (currEntry.getParentEntry() == null) {
				// 1. Ebene => fett, Abstand davor einf�gen
				docInhalt.add(getEmptyLineTextHalf());
				chunkBezeichnung = getChunkTextBold(strEintrag);
				chunkSeitenzahlChunk = getChunkTextBold(""
						+ currEntry.getPageNumber());
			} else {
				// 2. Ebene
				chunkBezeichnung = getChunkText(strEintrag);
				chunkSeitenzahlChunk = getChunkText(""
						+ currEntry.getPageNumber());
			}
			// Referenz setzen
			chunkBezeichnung.setLocalGoto(currEntry.getDestination());
			chunkSeitenzahlChunk.setLocalGoto(currEntry.getDestination());
			// Abstandzeichen generieren, Breite auff�llen
			float widthAbstand = docInhalt.getPageSize().getWidth() * 0.81f;
			;
			while (chunkBezeichnung.getWidthPoint() <= widthAbstand) {
				chunkBezeichnung.append(".");
			}

			// Tabelle erzeugen und formatieren
			PdfPTable currTable = new PdfPTable(2);
			currTable.setWidthPercentage(100f);
			currTable.setWidths(new int[] { 96, 4 });

			// Inhalte einf�gen
			// Zelle Bezeichnung
			PdfPCell currCellBezeichnung = new PdfPCell(new Phrase(
					chunkBezeichnung));
			currCellBezeichnung.setBorder(0);
			currCellBezeichnung
					.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);

			// Zelle Seitennummer
			PdfPCell currCellPageNumberCell = new PdfPCell(new Phrase(
					chunkSeitenzahlChunk));
			currCellPageNumberCell.setBorder(0);
			currCellPageNumberCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

			// Zellen zur Tabelle hinzuf�gen
			currTable.addCell(currCellBezeichnung);
			currTable.addCell(currCellPageNumberCell);

			docInhalt.add(currTable);
		}

		if (temp) {
			// Dokument schlie�en
			docInhalt.close();

			// Anzahl der Seitenzahlen bestimmen
			PdfReader reader = new PdfReader(filePathTempInhaltString);
			anzPages = reader.getNumberOfPages();
			reader.close();

			// temp. Datei l�schen
			File currFileInhaltFile = new File(filePathTempInhaltString);
			currFileInhaltFile.delete();
		}
		return anzPages;
	}

	/**
	 * Alle Elemente, die sich f�r das angegebene Schema in der entsprechenden
	 * Queue befinden, werden behandelt.
	 * 
	 * @param currSchemaPrefix
	 *            Pr�fix des zu behandelnden Schemas
	 * @param schemaQueues
	 *            Map enth�lt Queues f�r jedes Schema (Key ist der
	 *            Namepace-Pr�fix)
	 * @param xmlSchemaSet
	 *            SchemaSet, in dem alle Typen und Elemente definiert sind
	 * @throws DocumentException
	 */
	private void behandleSchemaQueue(String currSchemaPrefix,
			Map<String, Queue<XSElementDecl>> schemaQueues,
			XSSchemaSet xmlSchemaSet) throws DocumentException {

		// Liste mit allen bereits behandelten Elementen
		java.util.List<XSElementDecl> listElementsInserted = new LinkedList<XSElementDecl>();

		// Aktuelle Queue
		Queue<XSElementDecl> currQueue = schemaQueues.get(currSchemaPrefix);

		if (currQueue != null) {

			// zur Sicherheit nochmal alle Elemente dieses Schema in Queue
			// einf�gen
			// damit ist sichergestellt, dass auch nicht direkt verkn�pfte
			// Elemente (any => Plugins) behandelt werden
			XSSchema currSchema = xmlSchemaSet.getSchema(configurator
					.getPropertyNamespace(currSchemaPrefix));
			for (Entry<String, XSElementDecl> currElement : currSchema
					.getElementDecls().entrySet()) {
				currQueue.add(currElement.getValue());
			}

			if (currQueue.peek() != null) {
				// Kapitel inkl. Sprungmarke erstellen
				String targetNsUrl = currQueue.peek().getTargetNamespace();
				String chapterTitle = getNsPref(targetNsUrl) + ":"
						+ targetNsUrl;
				chapterCounter++;
				Chunk chunkChapter = getChunkChapter(chapterTitle);
				chunkChapter.setLocalDestination(chapterTitle);
				Chapter currChapter = new Chapter(new Paragraph(chunkChapter),
						chapterCounter);

				// Kapitel dem Dokument hinzuf�gen
				docPdf.add(currChapter);

				// Eintrag f�r Inhaltsverzeichnis (Chapter)
				ContPdfEntry currEntry = new ContPdfEntry(null, currChapter
						.getTitle().getContent(), chapterTitle, currPageNumber);
				listEntries.add(currEntry);

				while (currQueue.peek() != null) {
					// Aktuelles Element aus Queue holen
					XSElementDecl currElement = currQueue.poll();
					if (!listElementsInserted.contains(currElement)) {
						behandleElement(currElement, currChapter, currEntry,
								schemaQueues);
						listElementsInserted.add(currElement);
					}
				}
			}
		}
	}

	/**
	 * Das Element wird mit Datentyp, seinen Attributen und Kindelementen
	 * geschrieben.
	 * 
	 * @param currElement
	 *            Zu behandelndes Element
	 * @param currChapter
	 *            Kapitel, zu dem das Element hinzugef�gt wird
	 * @param chapterEntry
	 *            Eintrag des Kapitels f�r das Inhaltsverzeichnis
	 * @param schemaQueues
	 *            Queues mit allen Elementen f�r die einzelnen Schemas
	 */
	private void behandleElement(XSElementDecl currElement,
			Chapter currChapter, ContPdfEntry chapterEntry,
			Map<String, Queue<XSElementDecl>> schemaQueues)
			throws DocumentException {
		// F�r jedes Element eine Section; Titel ist der Elementname

		// eine Zeile Abstand
		docPdf.add(getEmptyLineText());

		// Referenz des Elements
		String currReferenzString = getReferenceForElement(currElement);

		// Titel des Elements inkl. Sprungmarke
		Chunk chunkElem = getChunkElement(currElement.getName());
		chunkElem.setLocalDestination(currReferenzString);

		Paragraph currElemPara = new Paragraph(getChunkElement(""));
		currElemPara.add(chunkElem);
		Section currSection = currChapter.addSection(currElemPara);

		// Eintrag f�r Inhaltsverzeichnis (Element)
		listEntries.add(new ContPdfEntry(chapterEntry, currSection.getTitle()
				.getContent(), currReferenzString, currPageNumber));

		// Sektion dem Dokument (vorl�ufig) hinzuf�gen (wichtig f�r das
		// Aktualisieren der Seitenzahl)
		currSection.setComplete(false);
		docPdf.add(currSection);

		// Anmerkung zum Element
		XSAnnotation ann = currElement.getAnnotation();
		if (ann != null) {
			// Abstand
			currSection.add(getEmptyLineTextHalf());

			String beschreibung = ann.getAnnotation().toString();
			currSection.add(getParagraphTextItalic(beschreibung));
		}

		// Abstand
		currSection.add(getEmptyLineTextHalf());

		// Datentyp
		Paragraph paraTyp = new Paragraph();
		XSType currType = currElement.getType();
		paraTyp.add(getPhraseTextBold("Datentyp: "));
		String strTyp = getNameWithPrefix(currType);
		if (currType.isLocal()) {
			strTyp = "<lokal definiert>";
		}
		paraTyp.add(getPhraseText(strTyp + "\n"));

		// Basistyp
		if ((currType.getDerivationMethod() == XSType.RESTRICTION)
				|| (currType.getDerivationMethod() == XSType.EXTENSION)) {
			paraTyp.add(getPhraseTextBold("basiert auf: "));
			paraTyp.add(getPhraseText(getNameWithPrefix(currType.getBaseType())
					+ "\n"));
		}
		currSection.add(paraTyp);

		// Attribute (falls vorhanden)
		behandleAttribute(currType, currSection);

		// Kind-Elemente (falls vorhanden)
		if (currType.isComplexType()) {
			// Gruppentyp (sequence oder choice)
			String strCompositor = getCompositorStr(currType.asComplexType());
			if (strCompositor.length() > 0) {
				// Abstand
				currSection.add(getEmptyLineTextHalf());

				currSection.add(getParagraphTextBold(strCompositor
						+ "-Elemente:"));
				behandleKindElemente(currElement, currSection, schemaQueues);
			}
		}

		// Sektion dem Dokument (endg�ltig) hinzuf�gen
		currSection.setComplete(true);
		docPdf.add(currSection);
	}

	/**
	 * Die Kindelemente (Sequence oder Choice) des �bergebenen Elements werden
	 * behandelt
	 * 
	 * @param currElementMain
	 *            Element, dessen Kindelemente gesucht und behandelt werden
	 * @param currSection
	 *            Sektion des PDF-Dokuments
	 * @param schemaQueues
	 *            Queues mit allen Elementen f�r die einzelnen Schemas
	 */
	private void behandleKindElemente(XSElementDecl currElementMain,
			Section currSection, Map<String, Queue<XSElementDecl>> schemaQueues) {

		// Kind-Elemente bestimmen
		XSParticle[] childs = null;
		XSType currType = currElementMain.getType();
		if (currType.isComplexType()) {
			XSParticle currParticle = null;
			if (currType.asComplexType().getExplicitContent() != null) {
				currParticle = currType.asComplexType().getExplicitContent()
						.asParticle();
			} else if (currType.asComplexType().getContentType() != null) {
				currParticle = currType.asComplexType().getContentType()
						.asParticle();
			}
			if (currParticle != null) {
				XSModelGroup modelGroup = currParticle.getTerm().asModelGroup();
				childs = modelGroup.getChildren();
			}
		}

		if ((childs != null) && (childs.length > 0)) {
			// F�r jedes Kind wird ein Listeneintrag erstellt
			List currList = erzeugeListeFuerKindElemente(childs, schemaQueues,
					bulletMain);
			currSection.add(currList);
		}
	}

	/**
	 * F�r die angegebenen Kindlemente wird ein Listeneintrag f�r das PDF
	 * erzeugt.
	 * 
	 * @param childs
	 *            Kindelemente
	 * @param schemaQueues
	 *            Queues mit allen Elementen f�r die einzelnen Schemas
	 * @param strBullet
	 *            Zeichen, das als Aufz�hlunsgzeichen f�r die Liste verwendet
	 *            wird
	 * @return PDF-Liste mit den Kindelementen
	 */
	private List erzeugeListeFuerKindElemente(XSParticle[] childs,
			Map<String, Queue<XSElementDecl>> schemaQueues, String strBullet) {

		List currList = new List(false, 12);
		currList.setListSymbol(strBullet);

		for (XSParticle currChild : childs) {

			if (currChild.getTerm() instanceof ElementDecl) {
				// Elemente von sequence bzw. choice abarbeiten
				XSElementDecl currElement = currChild.getTerm().asElementDecl();

				Phrase currPhrase = new Phrase();

				// Name, inkl. Referenz auf das Element
				currPhrase.add(getChunkTextBoldWithReference(
						getNameWithPrefix(currElement),
						getReferenceForElement(currElement)));

				// Auftreten (direkt hinter dem Namen)
				currPhrase.add(getChunkText(" (" + getMinMaxStr(currChild)
						+ ")\n"));

				// Beschreibung
				XSAnnotation currAnn = currChild.getAnnotation();
				if ((currAnn == null) && currElement.isLocal()
						&& (currElement.getAnnotation() != null)) {
					currAnn = currElement.getAnnotation();
				}
				if (currAnn != null) {
					currPhrase.add(getChunkTextItalic(currAnn.getAnnotation()
							.toString() + "\n"));
				}

				currList.add(new ListItem(currPhrase));

				// Element in Queue einf�gen
				String currPrefix = getNsPref(currElement.getTargetNamespace());
				schemaQueues.get(currPrefix).add(currElement);

			} else if (currChild.getTerm() instanceof XSModelGroup) {
				// Element von sequence/choice kann wieder eine ModelGroup
				// (sequence/choice) sein
				XSModelGroup mg = currChild.getTerm().asModelGroup();
				XSParticle[] childChilds = mg.getChildren();
				if ((childChilds != null) && (childChilds.length > 0)) {

					// Art der Gruppe
					String strCompositor = "";
					Compositor compositor = mg.getCompositor();
					if (compositor
							.equals(com.sun.xml.xsom.XSModelGroup.Compositor.SEQUENCE)) {
						strCompositor = "Sequence:";
					} else if (compositor
							.equals(com.sun.xml.xsom.XSModelGroup.Compositor.CHOICE)) {
						strCompositor = "Choice:";
					}
					currList.add(new ListItem(getPhraseTextBold(strCompositor)));

					// neue Liste f�r aktuelle Kindelemente erzeugen
					List subList = erzeugeListeFuerKindElemente(childChilds,
							schemaQueues, bulletSub);

					// Als Subliste hinzuf�gen
					currList.add(subList);
				}

			} else if (currChild.getTerm() instanceof XSWildcard) {
				// Element von sequence/choice kann ein Wildcard-Objekt sein,
				// z.B. 'xs:any'
				XSWildcard wildCard = currChild.getTerm().asWildcard();
				String currNamespaceStr = wildCard
						.apply(MySchemaWriter.WILDCARD_NS);

				Phrase currPhrase = new Phrase();
				currPhrase.add(getChunkTextBold("any"));
				currPhrase.add(getChunkText(" (" + getMinMaxStr(currChild)
						+ ")\n"));
				if (currNamespaceStr.length() > 0) {
					currPhrase.add(getChunkText(currNamespaceStr));
				}
				currList.add(new ListItem(currPhrase));
			}
		}
		return currList;
	}

	/**
	 * F�r die Attribute des aktuellen Typs wird eine Liste erzeugt und der
	 * angegebenen Sektion hinzugef�gt.
	 * 
	 * @param currType
	 *            Schemtyp, f�r den die Attribute bestimmt werden
	 * @param currSection
	 *            Aktuelle Sektion des PDF-Dokuments
	 */
	private void behandleAttribute(XSType currType, Section currSection) {
		if (currType.isComplexType()) {
			@SuppressWarnings("unchecked")
			Iterator<XSAttGroupDecl> itr1 = (Iterator<XSAttGroupDecl>) currType
					.asComplexType().iterateAttGroups();
			@SuppressWarnings("unchecked")
			Iterator<XSAttributeUse> itr2 = (Iterator<XSAttributeUse>) currType
					.asComplexType().iterateAttributeUses();

			if (itr1.hasNext() || itr2.hasNext()) {
				// Abstand
				currSection.add(getEmptyLineTextHalf());

				currSection.add(getParagraphTextBold("Attribute:"));

				List currList = new List(false, 12);
				currList.setListSymbol(bulletMain);

				while (itr1.hasNext()) {
					XSAttGroupDecl currAttr = itr1.next();
					String strAttribute = currAttr.getName();
					currList.add(new ListItem(getPhraseText(strAttribute)));
				}

				while (itr2.hasNext()) {
					XSAttributeDecl currAttr = itr2.next().getDecl();
					String strAttribute = currAttr.getName();
					// Typ kann lokal sein
					if (!currAttr.getType().isLocal()) {
						strAttribute += " ("
								+ getNameWithPrefix(currAttr.getType()) + ")";
					}
					currList.add(new ListItem(getPhraseText(strAttribute)));
				}
				currSection.add(currList);
			}
		}
	}

	/**
	 * Diese Methode erzeugt die Titelseite f�r das PDF-Dokument mit der
	 * Bezeichnung des Verfahrens.
	 * 
	 * @param bezVerfahren
	 *            Bezeichnung des profilierten Verfahrens
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void erzeugeTitelSeite() throws DocumentException,
			MalformedURLException, IOException {
		// Symbol, falls vorhanden
		String pathImage = "./logo_doku.png";
		try {
			// Image image =
			// Image.getInstance(PdfCreatorImpl.class.getResource(pathImage));
			Image image = Image.getInstance(pathImage);
			image.setAlignment(Element.ALIGN_RIGHT);
			docPdf.add(image);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Fehler beim Einbinden der Grafik '" + pathImage
						+ "'.", e);
			}
		}
		docPdf.add(getEmptyLineText());

		// Restliche Texte werden im Event-Handler geschrieben

		// Seitenumbruch
		docPdf.newPage();
	}

	private String getCompositorStr(XSComplexType type) {
		XSParticle currParticle = null;
		if (type.asComplexType().getExplicitContent() != null) {
			currParticle = type.asComplexType().getExplicitContent()
					.asParticle();
		} else if (type.asComplexType().getContentType() != null) {
			currParticle = type.asComplexType().getContentType().asParticle();
		}
		if (currParticle != null) {
			XSModelGroup modelGroup = currParticle.getTerm().asModelGroup();
			Compositor compositor = modelGroup.getCompositor();
			if (compositor
					.equals(com.sun.xml.xsom.XSModelGroup.Compositor.SEQUENCE)) {
				return "Sequence";
			} else if (compositor
					.equals(com.sun.xml.xsom.XSModelGroup.Compositor.CHOICE)) {
				return "Choice";
			}
		}
		return "";
	}

	private String getMinMaxStr(XSParticle currParticle) {
		// minOccurs
		int minOccursChild = currParticle.getMinOccurs().intValue();
		String strminOccurs = "minOccurs: " + minOccursChild;
		// maxOccurs
		int maxOccursChild = currParticle.getMaxOccurs().intValue();
		String strMaxOccurs = "maxOccurs: " + maxOccursChild;
		if (maxOccursChild == -1) {
			strMaxOccurs = "maxOccurs: unbounded";
		}
		return strminOccurs + ", " + strMaxOccurs;
	}

	private Document initPdfWriterAndDocument(String filePath,
			boolean addHandlerHeaderFooter) throws FileNotFoundException,
			DocumentException {
		chapterCounter = 0;
		currPageNumber = 1;
		Document document = new Document();
		document.setMargins(40, 40, 55, 55);
		PdfWriter pdfWriter = PdfWriter.getInstance(document,
				new FileOutputStream(filePath));
		if (addHandlerHeaderFooter) {
			// Eventhandler f�r die Fu�zeile
			HeaderFooter hf = new HeaderFooter();
			pdfWriter.setPageEvent(hf);
		}
		document.open();
		return document;
	}

	private Paragraph getEmptyLineText() {
		return getEmptyLinesText(fontSizeText, 1);
	}

	private Paragraph getEmptyLineTextHalf() {
		return getEmptyLinesText(fontSizeTextHalf, 1);
	}

	private Paragraph getEmptyLinesText(int fontSize, int countLines) {
		Paragraph pg = new Paragraph("\n", FontFactory.getFont(
				fontNameStandard, fontSize, Font.NORMAL));
		for (int i = 1; i < countLines; i++) {
			pg.add(new Paragraph("", FontFactory.getFont(fontNameStandard,
					fontSize, Font.NORMAL)));
		}
		return pg;
	}

	private Chunk getChunkChapter(String text) {
		return new Chunk(text, FontFactory.getFont(fontNameStandard,
				fontSizeChapter, Font.BOLD));
	}

	private Chunk getChunkElement(String text) {
		return new Chunk(text, FontFactory.getFont(fontNameStandard,
				fontSizeElement, Font.BOLD));
	}

	private Chunk getChunkText(String text) {
		return new Chunk(text, FontFactory.getFont(fontNameStandard,
				fontSizeText, Font.NORMAL));
	}

	private Chunk getChunkTextBold(String text) {
		return new Chunk(text, FontFactory.getFont(fontNameStandard,
				fontSizeText, Font.BOLD));
	}

	private Chunk getChunkTextBoldWithReference(String text, String reference) {
		Chunk chunkText = new Chunk(text, FontFactory.getFont(fontNameStandard,
				fontSizeText, Font.BOLD));
		// Referenz setzen
		chunkText.setLocalGoto(reference);
		// Unterstreichen
		chunkText.setUnderline(new BaseColor(0x00, 0x0f, 0xFF), 0.5f, 0.0f,
				-4f, 0.0f, PdfContentByte.LINE_CAP_BUTT);
		return chunkText;
	}

	private Chunk getChunkTextItalic(String text) {
		return new Chunk(text, FontFactory.getFont(fontNameStandard,
				fontSizeText, Font.ITALIC));
	}

	private Phrase getPhraseText(String text) {
		return new Phrase(text, FontFactory.getFont(fontNameStandard,
				fontSizeText, Font.NORMAL));
	}

	private Phrase getPhraseTextBold(String text) {
		return new Phrase(text, FontFactory.getFont(fontNameStandard,
				fontSizeText, Font.BOLD));
	}

	private Phrase getPhraseChapter(String text) {
		return new Phrase(text, FontFactory.getFont(fontNameStandard,
				fontSizeChapter, Font.BOLD));
	}

	private Phrase getPhraseChapterItalic(String text) {
		return new Phrase(text, FontFactory.getFont(fontNameStandard,
				fontSizeChapter, Font.BOLDITALIC));
	}

	private Phrase getPhraseHeaderFooter(String text) {
		return new Phrase(text, FontFactory.getFont(fontNameStandard,
				fontSizeFooter, Font.NORMAL));
	}

	private Paragraph getParagraphChapter(String text) {
		return new Paragraph(text, FontFactory.getFont(fontNameStandard,
				fontSizeChapter, Font.BOLD));
	}

	private Paragraph getParagraphTextBold(String text) {
		return new Paragraph(text, FontFactory.getFont(fontNameStandard,
				fontSizeText, Font.BOLD));
	}

	private Paragraph getParagraphTextItalic(String text) {
		return new Paragraph(text, FontFactory.getFont(fontNameStandard,
				fontSizeText, Font.ITALIC));
	}

	private String getNameWithPrefix(XSDeclaration decl) {
		return getNsPref(decl.getTargetNamespace()) + ":" + decl.getName();
	}

	private String getReferenceForElement(XSElementDecl element) {
		return element.hashCode() + "_" + getNameWithPrefix(element);
	}

	private String getNsPref(String nsUrl) {
		return configurator.getPropertyNamespace(nsUrl);
	}

	class HeaderFooter extends PdfPageEventHelper {

		String currChapterTitle = "";

		/**
		 * Initialize one of the headers, based on the chapter title; reset the
		 * page number.
		 * 
		 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onChapter(com.itextpdf.text.pdf.PdfWriter,
		 *      com.itextpdf.text.Document, float, com.itextpdf.text.Paragraph)
		 */
		@Override
		public void onChapter(PdfWriter writer, Document document,
				float paragraphPosition, Paragraph title) {
			currChapterTitle = title.getContent();
		}

		/**
		 * Adds the header and the footer.
		 * 
		 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter,
		 *      com.itextpdf.text.Document)
		 */
		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			float posTopLine = document.top() + 10;
			float posTopText = document.top() + 15;
			float posBottomLine = document.bottomMargin() - 10;
			float posBottomText = document.bottomMargin() - 22;
			float posLeft = document.leftMargin();
			float posRight = document.getPageSize().getRight()
					- document.rightMargin();

			if (writer.getPageNumber() == 1) {
				// Titelseite

				PdfContentByte contentByte = writer.getDirectContent();

				// Titel des Verfahrens (ungef�hr in der Mitte der Seite)
				float posTitleX = document.getPageSize().getWidth() / 2;
				float posTitleY = document.getPageSize().getHeight() / 2;

				Phrase phraseTitle = getPhraseChapter("Schnittstellenspezifikation");
				ColumnText.showTextAligned(contentByte, Element.ALIGN_CENTER,
						phraseTitle, posTitleX, posTitleY + 30, 0);
				phraseTitle = getPhraseChapterItalic(bezVerfahren);
				ColumnText.showTextAligned(contentByte, Element.ALIGN_CENTER,
						phraseTitle, posTitleX, posTitleY, 0);

				// Datumszeile 'erstellt am' am Ende der Seite
				DateFormat dateFormat = new SimpleDateFormat(
						"dd.MM.yyyy HH:mm:ss");
				Phrase phraseDatum = getPhraseText("erstellt am "
						+ dateFormat.format(new Date()));
				ColumnText.showTextAligned(contentByte, Element.ALIGN_RIGHT,
						phraseDatum, posRight, document.bottomMargin(), 0);

			} else if (writer.getPageNumber() > 1) {
				// Kopf- und Fu�zeile erst ab der zweiten Seite ...

				PdfContentByte contentByte = writer.getDirectContent();

				// Kopfzeile, Linie
				contentByte.moveTo(posLeft, posTopLine);
				contentByte.lineTo(posRight, posTopLine);
				contentByte.stroke();

				// Kopfzeile, Dateiname
				File currFile = new File(dateiname);
				Phrase phraseDateiname = getPhraseHeaderFooter(currFile
						.getName());
				ColumnText.showTextAligned(contentByte, Element.ALIGN_LEFT,
						phraseDateiname, posLeft, posTopText, 0);

				// Kopfzeile, Erstellungsdatum
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				Phrase phraseDatum = getPhraseHeaderFooter(dateFormat
						.format(new Date()));
				ColumnText.showTextAligned(contentByte, Element.ALIGN_RIGHT,
						phraseDatum, posRight, posTopText, 0);

				// Fu�zeile, Linie
				contentByte.moveTo(posLeft, posBottomLine);
				contentByte.lineTo(posRight, posBottomLine);
				contentByte.stroke();

				// Fu�zeile, aktuelles Kapitel
				if (currChapterTitle.length() > 0) {
					Phrase phraseFooterChapterTitle = getPhraseHeaderFooter(currChapterTitle);
					ColumnText
							.showTextAligned(contentByte, Element.ALIGN_LEFT,
									phraseFooterChapterTitle, posLeft,
									posBottomText, 0);
				}

				// Fu�zeile, Seitenzahl
				Phrase phraseFooter = getPhraseHeaderFooter(""
						+ writer.getPageNumber());
				ColumnText.showTextAligned(contentByte, Element.ALIGN_RIGHT,
						phraseFooter, posRight, posBottomText, 0);
			}

			// Seitenzahl hochz�hlen
			PdfCreatorImpl.this.currPageNumber++;
		}
	}

}
