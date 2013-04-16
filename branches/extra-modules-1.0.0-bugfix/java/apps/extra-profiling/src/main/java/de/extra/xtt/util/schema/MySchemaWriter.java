/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 * 
 * You can obtain a copy of the license at
 * https://jwsdp.dev.java.net/CDDLv1.0.html
 * See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://jwsdp.dev.java.net/CDDLv1.0.html  If applicable,
 * add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your
 * own identifying information: Portions Copyright [yyyy]
 * [name of copyright owner]
 */
package de.extra.xtt.util.schema;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSAttGroupDecl;
import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSIdentityConstraint;
import com.sun.xml.xsom.XSListSimpleType;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSModelGroupDecl;
import com.sun.xml.xsom.XSNotation;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSRestrictionSimpleType;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.XSUnionSimpleType;
import com.sun.xml.xsom.XSWildcard;
import com.sun.xml.xsom.XSWildcard.Any;
import com.sun.xml.xsom.XSWildcard.Other;
import com.sun.xml.xsom.XSWildcard.Union;
import com.sun.xml.xsom.XSXPath;
import com.sun.xml.xsom.impl.Const;
import com.sun.xml.xsom.visitor.XSSimpleTypeVisitor;
import com.sun.xml.xsom.visitor.XSTermVisitor;
import com.sun.xml.xsom.visitor.XSVisitor;
import com.sun.xml.xsom.visitor.XSWildcardFunction;

import de.extra.xtt.util.tools.Configurator;
import de.extra.xtt.util.tools.XsdXmlHelper;

/**
 * Generates approximated XML Schema representation from a schema component.
 * This is not intended to be a fully-fledged round-trippable schema writer.
 * 
 * <h2>Usage of this class</h2>
 * <ol>
 * <li>Create a new instance with whatever Writer you'd like to send the output
 * to.
 * <li>Call one of the overloaded dump methods. You can repeat this process as
 * many times as you want.
 * </ol>
 * 
 * @author Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 * @author Kirill Grouchnikov (kirillcool@yahoo.com)
 * @author Beier (Anpassungen)
 */
public class MySchemaWriter implements XSVisitor, XSSimpleTypeVisitor {

	private final Configurator configurator;
	private final String nsPrefixBasisSchema;
	private final String nsUrlBasisSchema;
	private final int basisIndent;

	// Writer-Objekt für jeden Namespace
	private final Map<String, Writer> nsWriter;
	private final Map<String, ByteArrayOutputStream> nsOutputStream;

	// Liste mit den verwendeten Namespaces
	private final Map<String, List<String>> nsUsedNamespaces;
	private final Map<String, Integer> nsIndent;
	private int currIndentChange;
	private String currNsPrefixMain;
	private String currAnmerkung;

	private Node currNodeProfElement;

	private final List<ContXsType> xsTypesInserted;
	private List<ContXsType> xsTypesToInsert;

	private List<XSElementDecl> xsElementsReferenced = new LinkedList<XSElementDecl>();

	/**
	 * Konstruktor mit Initialisierungsparametern
	 * 
	 * @param nsPrefixBasisSchema
	 *            Namespace-Pr�fix vom Basisschema, meistens "xs"
	 * @param nsUrlBasisSchema
	 *            Namespace-URL vom Basisschema, meistens
	 *            "http://www.w3.org/2001/XMLSchema"
	 * @param configurator
	 *            Configurator mit Zugriff auf Properties und andere
	 *            Eigenschaften
	 * @param basisIndent
	 *            Einzug
	 */
	public MySchemaWriter(String nsPrefixBasisSchema, String nsUrlBasisSchema,
			Configurator configurator, int basisIndent) {
		this.nsPrefixBasisSchema = nsPrefixBasisSchema;
		this.nsUrlBasisSchema = nsUrlBasisSchema;
		this.configurator = configurator;
		this.xsTypesInserted = new LinkedList<ContXsType>();
		this.xsTypesToInsert = new LinkedList<ContXsType>();
		this.basisIndent = basisIndent;
		this.xsElementsReferenced = new LinkedList<XSElementDecl>();
		this.nsIndent = new HashMap<String, Integer>();
		this.nsWriter = new HashMap<String, Writer>();
		this.nsOutputStream = new HashMap<String, ByteArrayOutputStream>();
		this.nsUsedNamespaces = new HashMap<String, List<String>>();
		this.currIndentChange = 0;
	}

	/**
	 * Liefert die Liste aller referenziertes Elemente
	 * 
	 * @return Liste aller referenzierter Elemente
	 */
	public List<XSElementDecl> getXsElementsReferenced() {
		return xsElementsReferenced;
	}

	/**
	 * Setzt die Liste aller referenziertes Elemente
	 * 
	 * @param xsElementsReferenced
	 *            Liste aller referenzierter Elemente
	 */
	public void setXsElementsReferenced(List<XSElementDecl> xsElementsReferenced) {
		this.xsElementsReferenced = xsElementsReferenced;
	}

	/**
	 * Liefert alle Writer-Objekte zu den einzelnen Namespaces
	 * 
	 * @return Liste aller Writer-Objekte, Key ist der jeweilige
	 *         Namespace-Pr�fix
	 */
	public Map<String, Writer> getNsWriter() {
		return nsWriter;
	}

	/**
	 * Liefert die Liste aller OutputStream-Objekte zu den einzelnen Namespaces
	 * 
	 * @return Liste aller OutputStream-Objekte, Key ist der jeweilige
	 *         Namespace-Pr�fix
	 */
	public Map<String, ByteArrayOutputStream> getNsOutputStream() {
		return nsOutputStream;
	}

	/**
	 * Liefert die Liste aller benutzter Namespaces
	 * 
	 * @return Liste aller benutzter Namespaces, Key ist Namespace-Pr�fix
	 */
	public Map<String, List<String>> getNsUsedNamespaces() {
		return nsUsedNamespaces;
	}

	private void println(String strToWrite, String targetNamespacePrefix) {
		try {
			// Pr�fen, ob es f�r diesen Namespace schon die entsprechenden
			// Objekte gibt
			if (!nsWriter.containsKey(targetNamespacePrefix)) {
				nsOutputStream.put(targetNamespacePrefix,
						new ByteArrayOutputStream());
				nsWriter.put(targetNamespacePrefix, new OutputStreamWriter(
						nsOutputStream.get(targetNamespacePrefix), "UTF8"));
				nsUsedNamespaces.put(targetNamespacePrefix,
						new LinkedList<String>());
				nsIndent.put(targetNamespacePrefix, basisIndent);
			}
			// Anpassung Einzug
			nsIndent.put(targetNamespacePrefix,
					nsIndent.get(targetNamespacePrefix) + currIndentChange);

			// evtl. Einzug schreiben
			for (int i = 0; i < nsIndent.get(targetNamespacePrefix); i++) {
				nsWriter.get(targetNamespacePrefix).write("  ");
			}

			// eigentlichen Text schreiben
			nsWriter.get(targetNamespacePrefix).write(strToWrite);
			nsWriter.get(targetNamespacePrefix).write('\n');
			// flush stream to make the output appear immediately
			nsWriter.get(targetNamespacePrefix).flush();
			// Einzug zur�cksetzen
			currIndentChange = 0;
		} catch (IOException e) {
			// ignore IOException.
		}
	}

	private String getMainNs() {
		if ((nsPrefixBasisSchema != null) && (nsPrefixBasisSchema.length() > 0)) {
			return nsPrefixBasisSchema + ":";
		} else {
			return "";
		}
	}

	private String getCurrNsPrefix(String currTargetNamespaceUrl,
			String nsPrefixMain) {
		String nsPrefix = "";
		if (!nsPrefixMain.equals(getNsPref(currTargetNamespaceUrl))) {
			nsPrefix = getNsPref(currTargetNamespaceUrl);
			// evtl. zu den used-Namespaces hinzuf�gen
			if (!nsUsedNamespaces.containsKey(nsPrefixMain)) {
				nsUsedNamespaces.put(nsPrefixMain, new LinkedList<String>());
			}
			if (!nsUsedNamespaces.get(nsPrefixMain).contains(nsPrefix)) {
				nsUsedNamespaces.get(nsPrefixMain).add(nsPrefix);
			}
			nsPrefix += ":";
		}
		return nsPrefix;
	}

	private String getNsPref(String nsUrl) {
		return configurator.getPropertyNamespace(nsUrl);
	}

	/**
	 * Bearbeitet alle im SchemaSet vorhandenen Schemata
	 * 
	 * @param s
	 *            SchemSet
	 */
	public void visit(XSSchemaSet s) {
		Iterator<XSSchema> itr = s.iterateSchema();
		while (itr.hasNext()) {
			schema(itr.next());
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void schema(XSSchema s) {

		// QUICK HACK: don't print the built-in components
		if (s.getTargetNamespace().equals(Const.schemaNamespace))
			return;

		currIndentChange = 0;

		println(MessageFormat.format("<" + getMainNs()
				+ "schema targetNamespace=\"{0}\">", s.getTargetNamespace()),
				getNsPref(s.getTargetNamespace()));
		currIndentChange++;

		Iterator<XSAttGroupDecl> itr1 = s.iterateAttGroupDecls();
		while (itr1.hasNext())
			attGroupDecl(itr1.next());

		Iterator<XSAttributeDecl> itr2 = s.iterateAttributeDecls();
		while (itr2.hasNext())
			attributeDecl(itr2.next());

		Iterator<XSComplexType> itr3 = s.iterateComplexTypes();
		while (itr3.hasNext())
			complexType(itr3.next());

		Iterator<XSElementDecl> itr4 = s.iterateElementDecls();
		while (itr4.hasNext())
			elementDecl(itr4.next());

		Iterator<XSModelGroupDecl> itr5 = s.iterateModelGroupDecls();
		while (itr5.hasNext())
			modelGroupDecl(itr5.next());

		Iterator<XSSimpleType> itr6 = s.iterateSimpleTypes();
		while (itr6.hasNext())
			simpleType(itr6.next());

		currIndentChange--;
		println("</" + getMainNs() + "schema>",
				getNsPref(s.getTargetNamespace()));
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void attGroupDecl(XSAttGroupDecl decl) {

		String nsPrefix = getNsPref(decl.getTargetNamespace());

		println(MessageFormat.format("<" + getMainNs()
				+ "attGroup name=\"{0}\">", decl.getName()), nsPrefix);
		currIndentChange++;

		@SuppressWarnings("unchecked")
		Iterator<XSAttGroupDecl> itr1 = (Iterator<XSAttGroupDecl>) decl
				.iterateAttGroups();
		while (itr1.hasNext())
			dumpRef(itr1.next(), nsPrefix);

		@SuppressWarnings("unchecked")
		Iterator<XSAttributeUse> itr2 = (Iterator<XSAttributeUse>) decl
				.iterateDeclaredAttributeUses();
		while (itr2.hasNext())
			attributeUse(itr2.next());

		currIndentChange--;
		println("</" + getMainNs() + "attGroup>", nsPrefix);
	}

	/**
	 * Schreibt die �bergebene AttributGruppe als XML mit Hilfe des
	 * entsprechenden Writer-Objekts.
	 * 
	 * @param decl
	 *            AttributGruppe, f�r die XML erzeugt werden soll
	 * @param nsPrefixMain
	 *            Namespace-Pr�fix, mit Hilfe dessen das entsprechende
	 *            Writer-Objekt bestimmt wird
	 */
	public void dumpRef(XSAttGroupDecl decl, String nsPrefixMain) {
		String nsPrefix = getCurrNsPrefix(decl.getTargetNamespace(),
				nsPrefixMain);
		println(MessageFormat.format("<" + getMainNs()
				+ "attGroup ref=\"{0}{1}\"/>", nsPrefix, decl.getName()),
				nsPrefixMain);
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void attributeUse(XSAttributeUse use) {
		attributeUse(use, currNsPrefixMain);
	}

	/**
	 * Schreibt das �bergebene Attribut als XML mit Hilfe des entsprechenden
	 * Writer-Objekts.
	 * 
	 * @param use
	 *            Attribut, f�r das XML erzeugt werden soll
	 * @param nsPrefixMain
	 *            Namespace-Pr�fix, mit Hilfe dessen das entsprechende
	 *            Writer-Objekt bestimmt wird
	 */
	public void attributeUse(XSAttributeUse use, String nsPrefixMain) {
		XSAttributeDecl decl = use.getDecl();

		String additionalAtts = "";

		if (use.isRequired())
			additionalAtts += " use=\"required\"";
		if (use.getFixedValue() != null
				&& use.getDecl().getFixedValue() == null)
			additionalAtts += " fixed=\"" + use.getFixedValue() + '\"';
		if (use.getDefaultValue() != null
				&& use.getDecl().getDefaultValue() == null)
			additionalAtts += " default=\"" + use.getDefaultValue() + '\"';

		if (decl.isLocal()) {
			// this is anonymous attribute use
			dump(decl, additionalAtts, nsPrefixMain);
		} else {
			// reference to a global one
			String nsPrefix = getCurrNsPrefix(decl.getTargetNamespace(),
					nsPrefixMain);
			println(MessageFormat.format("<" + getMainNs()
					+ "attribute ref=\"{0}{1}{2}\"/>", nsPrefix,
					decl.getName(), additionalAtts), nsPrefixMain);
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void attributeDecl(XSAttributeDecl decl) {
		attributeDecl(decl, currNsPrefixMain);
	}

	/**
	 * Schreibt f�r das �bergebene Element XML mit Hilfe des entsprechenden
	 * Writer-Objekts.
	 * 
	 * @param decl
	 *            Schema-Element, f�r das XML erzeugt werden soll
	 * @param nsPrefixMain
	 *            Namespace-Pr�fix, mit Hilfe dessen das entsprechende
	 *            Writer-Objekt bestimmt wird
	 */
	public void attributeDecl(XSAttributeDecl decl, String nsPrefixMain) {
		dump(decl, "", nsPrefixMain);
	}

	/**
	 * Schreibt f�r das �bergebene Element XML mit Hilfe des entsprechenden
	 * Writer-Objekts.
	 * 
	 * @param decl
	 *            Schema-Element, f�r das XML erzeugt werden soll
	 * @param additionalAtts
	 *            Zusatz-Attribute
	 * @param nsPrefixMain
	 *            Namespace-Pr�fix, mit Hilfe dessen das entsprechende
	 *            Writer-Objekt bestimmt wird
	 */
	private void dump(XSAttributeDecl decl, String additionalAtts,
			String nsPrefixMain) {
		XSSimpleType type = decl.getType();

		String nsPrefix = getCurrNsPrefix(type.getTargetNamespace(),
				nsPrefixMain);
		println(MessageFormat.format(
				"<" + getMainNs() + "attribute name=\"{0}\"{1}{2}{3}{4}{5}>",
				decl.getName(),
				additionalAtts,
				type.isLocal() ? "" : MessageFormat.format(" type=\"{0}{1}\"",
						nsPrefix, type.getName()),
				decl.getFixedValue() == null ? "" : " fixed=\""
						+ decl.getFixedValue() + '\"',
				decl.getDefaultValue() == null ? "" : " default=\""
						+ decl.getDefaultValue() + '\"', type.isLocal() ? ""
						: " /"), nsPrefixMain);

		if (type.isLocal()) {
			currIndentChange++;
			simpleType(type, nsPrefixMain, null);
			currIndentChange--;
			println("</" + getMainNs() + "attribute>", nsPrefixMain);
		} else {
			ContXsType currType = new ContXsType(type,
					getNsPref(decl.getTargetNamespace()) + ":" + decl.getName());
			if (!xsTypesToInsert.contains(currType)) {
				// Typ vormerken, um am Ende ausgegeben zu werden
				xsTypesToInsert.add(currType);
			}
		}
	}

	/**
	 * Schreibt XML f�r alle gemerkten, referenzierten Typen
	 * 
	 * @param profilXml
	 *            XMl-Dokument mit der Konfiguration der einzelnen Elemente
	 */
	public void printReferencedTypes(Document profilXml) {
		// Liste kopieren
		LinkedList<ContXsType> listToInsert = new LinkedList<ContXsType>();
		for (ContXsType currType : xsTypesToInsert) {
			listToInsert.add(currType);
		}

		// urspr�ngliche Liste leeren
		xsTypesToInsert = new LinkedList<ContXsType>();

		// aktuelle Liste abarbeiten; dabei k�nnen wieder Typen in die
		// urspr�ngliche Liste eingetragen werden
		for (ContXsType currType : listToInsert) {
			if (!xsTypesInserted.contains(currType)) {

				Node xmlNodeElement = null;
				String currElementName = currType.getElementNameWithPrefix();
				try {
					if ((currElementName != null)
							&& (currElementName.length() > 0)) {
						NodeList nl = XsdXmlHelper.xpathSuche(
								"//element[name/text()='" + currElementName
										+ "']", profilXml);
						if (nl.getLength() > 0) {
							xmlNodeElement = nl.item(0);
						}
					}
				} catch (XPathExpressionException e) {
				}

				if (currType.getType() instanceof XSComplexType) {
					complexType(currType.getType().asComplexType(), null,
							xmlNodeElement);
				} else if (currType.getType() instanceof XSSimpleType) {
					simpleType(currType.getType().asSimpleType(), null,
							xmlNodeElement);
				}
				xsTypesInserted.add(currType);
			}
		}

		// Evtl. noch weitere Typen vorhanden
		if (xsTypesToInsert.size() > 0) {
			printReferencedTypes(profilXml);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sun.xml.xsom.visitor.XSContentTypeVisitor#simpleType(com.sun.xml.
	 * xsom.XSSimpleType)
	 */
	@Override
	public void simpleType(XSSimpleType type) {
		simpleType(type, currNsPrefixMain, null);
	}

	/**
	 * Schreibt f�r das �bergebene Element XML mit Hilfe des entsprechenden
	 * Writer-Objekts.
	 * 
	 * @param type
	 *            Objekt, f�r das XML erzeugt werden soll
	 * @param nsPrefixMain
	 *            Namespace-Pr�fix, mit Hilfe dessen das entsprechende
	 *            Writer-Objekt bestimmt wird
	 * @param xmlNodeElement
	 *            Knoten-Objekt, das die Konfiguration f�r diesen Typ enth�lt
	 */
	public void simpleType(XSSimpleType type, String nsPrefixMain,
			Node xmlNodeElement) {
		if (nsPrefixMain == null) {
			// Selbst Hauptelement
			nsPrefixMain = getNsPref(type.getTargetNamespace());
		}
		this.currNodeProfElement = xmlNodeElement;

		String strElement = "";
		try {
			strElement = XsdXmlHelper
					.xpathSuche("./name/text()", xmlNodeElement).item(0)
					.getNodeValue();
		} catch (Exception e) {
		}
		ContXsType currType = new ContXsType(type, strElement);

		if (!type.getTargetNamespace().equals(nsUrlBasisSchema)
				&& !xsTypesInserted.contains(currType)) {

			println(MessageFormat.format("<" + getMainNs() + "simpleType{0}>",
					type.isLocal() ? "" : " name=\"" + type.getName() + '\"'),
					nsPrefixMain);
			currIndentChange++;

			this.currNsPrefixMain = nsPrefixMain;
			type.visit((XSSimpleTypeVisitor) this);

			currIndentChange--;
			println("</" + getMainNs() + "simpleType>", nsPrefixMain);

			xsTypesInserted.add(currType);

			// evtl. extension/restriction-Typ behandeln
			XSType currBaseType = type.getBaseType();
			if (currBaseType != null) {
				if (currBaseType instanceof XSComplexType) {
					complexType(currBaseType.asComplexType(), null, null);
				} else if (currBaseType instanceof XSSimpleType) {
					simpleType(currBaseType.asSimpleType(), null, null);
				}
				ContXsType currBaseTypeCont = new ContXsType(currBaseType, "");
				xsTypesInserted.add(currBaseTypeCont);
			}
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void listSimpleType(XSListSimpleType type) {
		listSimpleType(type, currNsPrefixMain);
	}

	/**
	 * Schreibt f�r das �bergebene Element XML mit Hilfe des entsprechenden
	 * Writer-Objekts.
	 * 
	 * @param type
	 *            Objekt, f�r das XML erzeugt werden soll
	 * @param nsPrefixMain
	 *            Namespace-Pr�fix, mit Hilfe dessen das entsprechende
	 *            Writer-Objekt bestimmt wird
	 */
	public void listSimpleType(XSListSimpleType type, String nsPrefixMain) {
		XSSimpleType itemType = type.getItemType();

		if (itemType.isLocal()) {
			println("<" + getMainNs() + "list>", nsPrefixMain);
			currIndentChange++;
			simpleType(itemType);
			currIndentChange--;
			println("</" + getMainNs() + "list>", nsPrefixMain);
		} else {
			// global type
			String nsPrefix = getCurrNsPrefix(itemType.getTargetNamespace(),
					nsPrefixMain);
			println(MessageFormat.format("<" + getMainNs()
					+ "list itemType=\"{0}{1}\" />", nsPrefix,
					itemType.getName()), nsPrefixMain);

			ContXsType currType = new ContXsType(itemType, "");
			if (!xsTypesToInsert.contains(currType)) {
				// Typ vormerken, um am Ende ausgegeben zu werden
				xsTypesToInsert.add(currType);
			}
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void unionSimpleType(XSUnionSimpleType type) {
		unionSimpleType(type, currNsPrefixMain);
	}

	/**
	 * Schreibt f�r das �bergebene Element XML mit Hilfe des entsprechenden
	 * Writer-Objekts.
	 * 
	 * @param type
	 *            Objekt, f�r das XML erzeugt werden soll
	 * @param nsPrefixMain
	 *            Namespace-Pr�fix, mit Hilfe dessen das entsprechende
	 *            Writer-Objekt bestimmt wird
	 */
	public void unionSimpleType(XSUnionSimpleType type, String nsPrefixMain) {
		final int len = type.getMemberSize();
		StringBuffer ref = new StringBuffer();

		for (int i = 0; i < len; i++) {
			XSSimpleType member = type.getMember(i);
			String nsPrefix = getCurrNsPrefix(member.getTargetNamespace(),
					nsPrefixMain);

			if (member.isGlobal())
				ref.append(MessageFormat.format(" {0}{1}", nsPrefix,
						member.getName()));
		}

		if (ref.length() == 0)
			println("<" + getMainNs() + "union>", nsPrefixMain);
		else
			println("<" + getMainNs() + "union memberTypes=\"" + ref + "\">",
					nsPrefixMain);
		currIndentChange++;

		for (int i = 0; i < len; i++) {
			XSSimpleType member = type.getMember(i);
			if (member.isLocal())
				simpleType(member);
		}
		currIndentChange--;
		println("</" + getMainNs() + "union>", nsPrefixMain);
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void restrictionSimpleType(XSRestrictionSimpleType type) {
		restrictionSimpleType(type, currNsPrefixMain);
	}

	/**
	 * Schreibt f�r das �bergebene Element XML mit Hilfe des entsprechenden
	 * Writer-Objekts.
	 * 
	 * @param type
	 *            Objekt, f�r das XML erzeugt werden soll
	 * @param nsPrefixMain
	 *            Namespace-Pr�fix, mit Hilfe dessen das entsprechende
	 *            Writer-Objekt bestimmt wird
	 */
	public void restrictionSimpleType(XSRestrictionSimpleType type,
			String nsPrefixMain) {

		if (type.getBaseType() == null) {
			// don't print anySimpleType
			if (!type.getName().equals("anySimpleType"))
				throw new InternalError();
			if (!Const.schemaNamespace.equals(type.getTargetNamespace()))
				throw new InternalError();
			return;
		}

		XSSimpleType baseType = type.getSimpleBaseType();

		String nsPrefix = getCurrNsPrefix(baseType.getTargetNamespace(),
				nsPrefixMain);

		println(MessageFormat.format(
				"<" + getMainNs() + "restriction{0}>",
				baseType.isLocal() ? "" : " base=\"" + nsPrefix
						+ baseType.getName() + '\"'), nsPrefixMain);
		currIndentChange++;

		if (baseType.isLocal())
			simpleType(baseType, nsPrefixMain, null);

		Iterator<XSFacet> itr = type.iterateDeclaredFacets();
		while (itr.hasNext())
			facet(itr.next(), nsPrefixMain);

		currIndentChange--;
		println("</" + getMainNs() + "restriction>", nsPrefixMain);
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void facet(XSFacet facet) {
		facet(facet, currNsPrefixMain);
	}

	/**
	 * Schreibt f�r das �bergebene Element XML mit Hilfe des entsprechenden
	 * Writer-Objekts.
	 * 
	 * @param facet
	 *            Objekt, f�r das XML erzeugt werden soll
	 * @param nsPrefixMain
	 *            Namespace-Pr�fix, mit Hilfe dessen das entsprechende
	 *            Writer-Objekt bestimmt wird
	 */
	public void facet(XSFacet facet, String nsPrefixMain) {
		println(MessageFormat.format("<{0} value=\"{1}\"/>", getMainNs()
				+ facet.getName(), facet.getValue()), nsPrefixMain);
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void notation(XSNotation notation) {
		notation(notation, currNsPrefixMain);
	}

	/**
	 * Schreibt f�r das �bergebene Element XML mit Hilfe des entsprechenden
	 * Writer-Objekts.
	 * 
	 * @param notation
	 *            Objekt, f�r das XML erzeugt werden soll
	 * @param nsPrefixMain
	 *            Namespace-Pr�fix, mit Hilfe dessen das entsprechende
	 *            Writer-Objekt bestimmt wird
	 */
	public void notation(XSNotation notation, String nsPrefixMain) {
		println(MessageFormat.format("<" + getMainNs()
				+ "notation name='\"0}\" public =\"{1}\" system=\"{2}\" />",
				notation.getName(), notation.getPublicId(),
				notation.getSystemId()), nsPrefixMain);
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void complexType(XSComplexType type) {
		complexType(type, currNsPrefixMain, currNodeProfElement);
	}

	/**
	 * Schreibt f�r das �bergebene Element XML mit Hilfe des entsprechenden
	 * Writer-Objekts.
	 * 
	 * @param type
	 *            Objekt, f�r das XML erzeugt werden soll
	 * @param nsPrefixMain
	 *            Namespace-Pr�fix, mit Hilfe dessen das entsprechende
	 *            Writer-Objekt bestimmt wird
	 * @param xmlNodeElement
	 *            Knoten-Objekt, das die Konfiguration f�r diesen Typ enth�lt
	 */
	public void complexType(XSComplexType type, String nsPrefixMain,
			Node xmlNodeElement) {
		String strElement = "";
		try {
			strElement = XsdXmlHelper
					.xpathSuche("./name/text()", xmlNodeElement).item(0)
					.getNodeValue();
		} catch (Exception e) {
		}
		ContXsType currType = new ContXsType(type, strElement);
		if (!type.getTargetNamespace().equals(nsUrlBasisSchema)
				&& !xsTypesInserted.contains(currType)) {

			this.currNodeProfElement = xmlNodeElement;
			if (nsPrefixMain == null) {
				nsPrefixMain = getNsPref(type.getTargetNamespace());
			}

			println(MessageFormat.format("<" + getMainNs()
					+ "complexType{0}{1}>", type.isLocal() ? "" : " name=\""
					+ type.getName() + '\"',
					type.isAbstract() ? " abstract=\"true\"" : ""),
					nsPrefixMain);
			currIndentChange++;

			if (type.getContentType().asSimpleType() != null) {
				// simple content
				println("<" + getMainNs() + "simpleContent>", nsPrefixMain);
				currIndentChange++;

				XSType baseType = type.getBaseType();
				String nsPrefix = getCurrNsPrefix(
						baseType.getTargetNamespace(), nsPrefixMain);

				if (type.getDerivationMethod() == XSType.RESTRICTION) {
					// restriction
					println(MessageFormat.format("<" + getMainNs()
							+ "restriction base=\"{0}{1}\">", nsPrefix,
							baseType.getName()), nsPrefixMain);
					currIndentChange++;

					dumpComplexTypeAttribute(type, nsPrefixMain);

					currIndentChange--;
					println("</" + getMainNs() + "restriction>", nsPrefixMain);
				} else {
					// extension
					println(MessageFormat.format("<" + getMainNs()
							+ "extension base=\"{0}{1}\">", nsPrefix,
							baseType.getName()), nsPrefixMain);

					// check if have redefine tag - Kirill
					if (type.isGlobal()
							&& type.getTargetNamespace().equals(
									baseType.getTargetNamespace())
							&& type.getName().equals(baseType.getName())) {
						currIndentChange++;
						println("<" + getMainNs() + "redefine>", nsPrefixMain);
						currIndentChange++;
						baseType.visit(this);
						currIndentChange--;
						println("</" + getMainNs() + "redefine>", nsPrefixMain);
						currIndentChange--;
					}

					currIndentChange++;

					dumpComplexTypeAttribute(type, nsPrefixMain);

					currIndentChange--;
					println("</" + getMainNs() + "extension>", nsPrefixMain);
				}

				currIndentChange--;
				println("</" + getMainNs() + "simpleContent>", nsPrefixMain);
			} else {
				// complex content
				println("<" + getMainNs() + "complexContent>", nsPrefixMain);
				currIndentChange++;

				XSComplexType baseType = type.getBaseType().asComplexType();
				String nsPrefix = getCurrNsPrefix(
						baseType.getTargetNamespace(), nsPrefixMain);

				if (type.getDerivationMethod() == XSType.RESTRICTION) {
					// restriction
					println(MessageFormat.format("<" + getMainNs()
							+ "restriction base=\"{0}{1}\">", nsPrefix,
							baseType.getName()), nsPrefixMain);
					currIndentChange++;

					this.currNsPrefixMain = nsPrefixMain;
					type.getContentType().visit(this);
					dumpComplexTypeAttribute(type, nsPrefixMain);

					currIndentChange--;
					println("</" + getMainNs() + "restriction>", nsPrefixMain);
				} else {
					// extension
					println(MessageFormat.format("<" + getMainNs()
							+ "extension base=\"{0}{1}\">", nsPrefix,
							baseType.getName()), nsPrefixMain);

					// check if have redefine - Kirill
					if (type.isGlobal()
							&& type.getTargetNamespace().equals(
									baseType.getTargetNamespace())
							&& type.getName().equals(baseType.getName())) {
						currIndentChange++;
						println("<" + getMainNs() + "redefine>", nsPrefixMain);
						currIndentChange++;
						baseType.visit(this);
						currIndentChange--;
						println("</" + getMainNs() + "redefine>", nsPrefixMain);
						currIndentChange--;
					}

					currIndentChange++;

					this.currNsPrefixMain = nsPrefixMain;
					type.getExplicitContent().visit(this);
					dumpComplexTypeAttribute(type, nsPrefixMain);

					currIndentChange--;
					println("</" + getMainNs() + "extension>", nsPrefixMain);
				}

				currIndentChange--;
				println("</" + getMainNs() + "complexContent>", nsPrefixMain);
			}

			currIndentChange--;
			println("</" + getMainNs() + "complexType>", nsPrefixMain);

			xsTypesInserted.add(currType);

			// evtl. extension/restriction-Typ behandeln
			XSType currBaseType = type.getBaseType();
			if (currBaseType != null) {
				if (currBaseType instanceof XSComplexType) {
					complexType(currBaseType.asComplexType(), null, null);
				} else if (currBaseType instanceof XSSimpleType) {
					simpleType(currBaseType.asSimpleType(), null, null);
				}
			}
		}
	}

	/**
	 * Schreibt f�r das �bergebene Element XML mit Hilfe des entsprechenden
	 * Writer-Objekts.
	 * 
	 * @param type
	 *            Objekt, f�r das XML erzeugt werden soll
	 * @param nsPrefixMain
	 *            Namespace-Pr�fix, mit Hilfe dessen das entsprechende
	 *            Writer-Objekt bestimmt wird
	 */
	private void dumpComplexTypeAttribute(XSComplexType type,
			String nsPrefixMain) {

		@SuppressWarnings("unchecked")
		Iterator<XSAttGroupDecl> itr1 = (Iterator<XSAttGroupDecl>) type
				.iterateAttGroups();
		while (itr1.hasNext())
			dumpRef(itr1.next(), nsPrefixMain);

		@SuppressWarnings("unchecked")
		Iterator<XSAttributeUse> itr2 = (Iterator<XSAttributeUse>) type
				.iterateDeclaredAttributeUses();
		while (itr2.hasNext())
			attributeUse(itr2.next(), nsPrefixMain);

		XSWildcard awc = type.getAttributeWildcard();
		if (awc != null)
			wildcard(getMainNs() + "anyAttribute", awc, "", nsPrefixMain);
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void elementDecl(XSElementDecl decl) {
		elementDecl(decl, "", currNsPrefixMain, currNodeProfElement, "");
	}

	/**
	 * Schreibt f�r das �bergebene Element XML mit Hilfe des entsprechenden
	 * Writer-Objekts.
	 * 
	 * @param decl
	 *            Objekt, f�r das XML erzeugt werden soll
	 * @param extraAtts
	 *            Zusatzangaben zum Objekt
	 * @param nsPrefixMain
	 *            Namespace-Pr�fix, mit Hilfe dessen das entsprechende
	 *            Writer-Objekt bestimmt wird
	 * @param nodeProfilElement
	 *            element-Node aus der Profilkonfiguration
	 * @param anmerkungVerwendung
	 *            Anmerkungstext zur Verwendung dieses Elements (wird bei der
	 *            Ausgabe von lokalen Elementen in Typen verwendet)
	 */
	public void elementDecl(XSElementDecl decl, String extraAtts,
			String nsPrefixMain, Node nodeProfilElement,
			String anmerkungVerwendung) {
		if (nsPrefixMain == null) {
			nsPrefixMain = getNsPref(decl.getTargetNamespace());
		}

		XSType type = decl.getType();

		// Anmerkung erzeugen, falls vorhanden
		String anmerkungGes = "";
		String currAnmAllg = "";
		String currAnmVerwendung = "";
		// allg. Anmerkung
		try {
			NodeList listNodes = XsdXmlHelper.xpathSuche("./anmerkung/text()",
					nodeProfilElement);
			if (listNodes.getLength() > 0) {
				String currElementAnmerkung = listNodes.item(0).getNodeValue();
				// Anmerkungsstring zusammenbauen
				if (currElementAnmerkung.length() > 0) {
					currAnmAllg = "<" + getMainNs() + "documentation>"
							+ currElementAnmerkung + "</" + getMainNs()
							+ "documentation>";
				}
			}
		} catch (Exception e) {
		}

		// Anmerkung Verwendung
		if ((anmerkungVerwendung != null) && (anmerkungVerwendung.length() > 0)) {
			currAnmVerwendung = "<" + getMainNs() + "documentation>"
					+ anmerkungVerwendung + "</" + getMainNs()
					+ "documentation>";
		}

		if ((currAnmAllg.length() > 0) || (currAnmVerwendung.length() > 0)) {
			anmerkungGes = "<" + getMainNs() + "annotation>" + currAnmAllg
					+ currAnmVerwendung + "</" + getMainNs() + "annotation>";
		}

		String nsPrefix = getCurrNsPrefix(type.getTargetNamespace(),
				nsPrefixMain);
		println(MessageFormat.format("<" + getMainNs()
				+ "element name=\"{0}\"{1}{2}>{3}{4}", decl.getName(),
				type.isLocal() ? "" : " type=\"" + nsPrefix + type.getName()
						+ '\"', extraAtts, anmerkungGes, type.isLocal() ? ""
						: "</" + getMainNs() + "element>"), nsPrefixMain);

		if (type.isLocal()) {
			currIndentChange++;

			this.currNsPrefixMain = nsPrefixMain;
			type.visit(this);

			currIndentChange--;
			println("</" + getMainNs() + "element>", nsPrefixMain);
		} else {
			ContXsType currType = new ContXsType(type,
					getNsPref(decl.getTargetNamespace()) + ":" + decl.getName());
			if (!xsTypesToInsert.contains(currType)) {
				// Typ vormerken, um am Ende ausgegeben zu werden
				xsTypesToInsert.add(currType);
			}
		}

	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void modelGroupDecl(XSModelGroupDecl decl) {
		modelGroupDecl(decl, currNsPrefixMain);
	}

	/**
	 * Schreibt f�r das �bergebene Element XML mit Hilfe des entsprechenden
	 * Writer-Objekts.
	 * 
	 * @param decl
	 *            Objekt, f�r das XML erzeugt werden soll
	 * @param nsPrefixMain
	 *            Namespace-Pr�fix, mit Hilfe dessen das entsprechende
	 *            Writer-Objekt bestimmt wird
	 */
	public void modelGroupDecl(XSModelGroupDecl decl, String nsPrefixMain) {
		println(MessageFormat.format("<" + getMainNs() + "group name=\"{0}\">",
				decl.getName()), nsPrefixMain);
		currIndentChange++;

		modelGroup(decl.getModelGroup(), "", nsPrefixMain);

		currIndentChange--;
		println("</" + getMainNs() + "group>", nsPrefixMain);
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void modelGroup(XSModelGroup group) {
		modelGroup(group, "", currNsPrefixMain);
	}

	/**
	 * Schreibt f�r das �bergebene Element XML mit Hilfe des entsprechenden
	 * Writer-Objekts.
	 * 
	 * @param group
	 *            Objekt, f�r das XML erzeugt werden soll
	 * @param extraAtts
	 *            Zusatzangaben zum Objekt
	 * @param nsPrefixMain
	 *            Namespace-Pr�fix, mit Hilfe dessen das entsprechende
	 *            Writer-Objekt bestimmt wird
	 */
	private void modelGroup(XSModelGroup group, String extraAtts,
			String nsPrefixMain) {
		println(MessageFormat.format("<" + getMainNs() + "{0}{1}>",
				group.getCompositor(), extraAtts), nsPrefixMain);
		currIndentChange++;

		final int len = group.getSize();
		for (int i = 0; i < len; i++) {
			XSParticle currParticle = group.getChild(i);
			XSElementDecl currPartElement = currParticle.getTerm()
					.asElementDecl();
			// Pr�fen, ob dieses Element in der Konfiguration vorkommt
			boolean isToPrint = true;
			Integer minOccUser = null;
			Integer maxOccUser = null;
			String anmerkung = "";
			if ((currNodeProfElement != null) && (currPartElement != null)) {
				isToPrint = false;

				NodeList nodesKindXml;
				try {
					nodesKindXml = XsdXmlHelper.xpathSuche("./kind/text()",
							currNodeProfElement);
					for (int j = 0; j < nodesKindXml.getLength(); j++) {
						// Name des XML-Knotens in der Profilkonfiguration
						String strXmlElementName = nodesKindXml.item(j)
								.getNodeValue();
						// Passendes SchemaElement erzeugen
						SchemaElement currSchemaElement = configurator
								.getSchemaElement(strXmlElementName);
						if (currPartElement.getTargetNamespace().equals(
								currSchemaElement.getNsUrl())
								&& currPartElement.getName().equals(
										currSchemaElement.getName())) {
							isToPrint = true;
							// Pr�fen, ob f�r minOccurs Benutzerangaben
							// vorhanden sind
							NodeList listNodes = XsdXmlHelper.xpathSuche(
									"attribute::minOccurs", nodesKindXml
											.item(j).getParentNode());
							if (listNodes.getLength() > 0) {
								minOccUser = Integer.parseInt(listNodes.item(0)
										.getNodeValue());
							}
							// Pr�fen, ob f�r maxOccurs Benutzerangaben
							// vorhanden sind
							listNodes = XsdXmlHelper.xpathSuche(
									"attribute::maxOccurs", nodesKindXml
											.item(j).getParentNode());
							if (listNodes.getLength() > 0) {
								maxOccUser = Integer.parseInt(listNodes.item(0)
										.getNodeValue());
							}
							// Pr�fen, ob Anmerkung vorhanden ist
							listNodes = XsdXmlHelper.xpathSuche(
									"attribute::anmerkung", nodesKindXml
											.item(j).getParentNode());
							if (listNodes.getLength() > 0) {
								anmerkung = listNodes.item(0).getNodeValue();
							}
							break;
						}
					}
				} catch (XPathExpressionException e) {
					isToPrint = true;
				}
			}
			if (isToPrint) {
				particle(currParticle, nsPrefixMain, minOccUser, maxOccUser,
						anmerkung);
			}
		}

		currIndentChange--;
		println(MessageFormat.format("</" + getMainNs() + "{0}>",
				group.getCompositor()), nsPrefixMain);
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void particle(XSParticle part) {
		particle(part, currNsPrefixMain, null, null, null);
	}

	/**
	 * Schreibt f�r das �bergebene Element XML mit Hilfe des entsprechenden
	 * Writer-Objekts.
	 * 
	 * @param part
	 *            Objekt, f�r das XML erzeugt werden soll
	 * @param nsPrefixMain
	 *            Namespace-Pr�fix, mit Hilfe dessen das entsprechende
	 *            Writer-Objekt bestimmt wird
	 * @param minOccUser
	 *            minOccurs-Benutzerangabe, optional
	 * @param maxOccUser
	 *            maxOccurs-Benutzerangabe, optional
	 * @param anmVerwendung
	 *            Anmerkungstext zur Verwendung dieses Elements
	 */
	public void particle(XSParticle part, String nsPrefixMain,
			Integer minOccUser, Integer maxOccUser, String anmVerwendung) {
		int i;

		StringBuffer buf = new StringBuffer();

		i = part.getMinOccurs().intValue();
		if (minOccUser != null) {
			i = minOccUser;
		}
		buf.append(" minOccurs=\"").append(i).append('\"');

		i = part.getMaxOccurs().intValue();
		if (maxOccUser != null) {
			i = maxOccUser;
		}
		if ((i == XSParticle.UNBOUNDED) || (i == Integer.MAX_VALUE)) {
			buf.append(" maxOccurs=\"unbounded\"");
		} else {
			buf.append(" maxOccurs=\"").append(i).append('\"');
		}

		final String extraAtts = buf.toString();

		this.currNsPrefixMain = nsPrefixMain;
		// Anmerkung zur Verwendung
		this.currAnmerkung = anmVerwendung;
		part.getTerm().visit(new XSTermVisitor() {

			@Override
			public void elementDecl(XSElementDecl decl) {
				if (decl.isLocal()) {
					// Passenden Element-Knoten aus der Profilkonfiguration
					// holen
					Node currElementNode = null;
					String strNameCurrElement = getNsPref(decl
							.getTargetNamespace()) + ":" + decl.getName();
					try {
						if (MySchemaWriter.this.currNodeProfElement != null) {
							currElementNode = XsdXmlHelper.xpathSuche(
									"//element[name/text()='"
											+ strNameCurrElement + "']",
									MySchemaWriter.this.currNodeProfElement)
									.item(0);
						}
					} catch (XPathExpressionException e) {
					}
					MySchemaWriter.this.elementDecl(decl, extraAtts,
							MySchemaWriter.this.currNsPrefixMain,
							currElementNode, MySchemaWriter.this.currAnmerkung);
				} else {
					// Anmerkungsstring zusammenbauen
					String strAnmerkung = "";
					if ((MySchemaWriter.this.currAnmerkung != null)
							&& (MySchemaWriter.this.currAnmerkung.length() > 0)) {
						strAnmerkung = "<" + getMainNs() + "annotation><"
								+ getMainNs() + "documentation>";
						strAnmerkung += MySchemaWriter.this.currAnmerkung;
						strAnmerkung += "</" + getMainNs() + "documentation></"
								+ getMainNs() + "annotation>";
					}

					// reference
					String nsPrefix = getCurrNsPrefix(
							decl.getTargetNamespace(),
							MySchemaWriter.this.currNsPrefixMain);
					println(MessageFormat.format("<" + getMainNs()
							+ "element ref=\"{0}{1}\"{2}>{3}</" + getMainNs()
							+ "element>", nsPrefix, decl.getName(), extraAtts,
							strAnmerkung), MySchemaWriter.this.currNsPrefixMain);

					// Referenziertes Element der Liste hinzuf�gen
					if (!decl.getTargetNamespace().equals(nsUrlBasisSchema)
							&& !xsElementsReferenced.contains(decl)) {
						xsElementsReferenced.add(decl);
					}
				}
			}

			@Override
			public void modelGroupDecl(XSModelGroupDecl decl) {
				// reference
				String nsPrefix = getCurrNsPrefix(decl.getTargetNamespace(),
						MySchemaWriter.this.currNsPrefixMain);
				println(MessageFormat.format("<" + getMainNs()
						+ "group ref=\"{0}{1}\"{2}/>", nsPrefix,
						decl.getName(), extraAtts),
						MySchemaWriter.this.currNsPrefixMain);
			}

			@Override
			public void modelGroup(XSModelGroup group) {
				MySchemaWriter.this.modelGroup(group, extraAtts,
						MySchemaWriter.this.currNsPrefixMain);
			}

			@Override
			public void wildcard(XSWildcard wc) {
				MySchemaWriter.this.wildcard(getMainNs() + "any", wc,
						extraAtts, MySchemaWriter.this.currNsPrefixMain);
			}
		});
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void wildcard(XSWildcard wc) {
		wildcard(wc, currNsPrefixMain);
	}

	/**
	 * Schreibt f�r das �bergebene Element XML mit Hilfe des entsprechenden
	 * Writer-Objekts.
	 * 
	 * @param wc
	 *            Objekt, f�r das XML erzeugt werden soll
	 * @param nsPrefixMain
	 *            Namespace-Pr�fix, mit Hilfe dessen das entsprechende
	 *            Writer-Objekt bestimmt wird
	 */
	public void wildcard(XSWildcard wc, String nsPrefixMain) {
		wildcard(getMainNs() + "any", wc, "", nsPrefixMain);
	}

	private void wildcard(String tagName, XSWildcard wc, String extraAtts,
			String nsPrefixMain) {
		final String proessContents;
		switch (wc.getMode()) {
		case XSWildcard.LAX:
			proessContents = " processContents='lax'";
			break;
		case XSWildcard.STRTICT:
			proessContents = "";
			break;
		case XSWildcard.SKIP:
			proessContents = " processContents='skip'";
			break;
		default:
			throw new AssertionError();
		}

		println(MessageFormat.format("<{0}{1}{2}{3}/>", tagName,
				proessContents, wc.apply(WILDCARD_NS), extraAtts), nsPrefixMain);

		String currNamespaceStr = wc.apply(WILDCARD_NS);
		if ((currNamespaceStr != null) && (currNamespaceStr.length() > 0)) {
			// Alle Elemente merken, die per Wildcard hier verwendet werden
			// d�rfen
			// Dazu alle Schemata durchlaufen und pr�fen, ob der Targetnamespace
			// von Wildcard akzeptiert wird
			for (XSSchema currSchema : wc.getRoot().getSchemas()) {
				if (wc.acceptsNamespace(currSchema.getTargetNamespace())) {
					Map<String, XSElementDecl> elementDecls = currSchema
							.getElementDecls();
					for (Map.Entry<String, XSElementDecl> currEntry : elementDecls
							.entrySet()) {
						if (!xsElementsReferenced
								.contains(currEntry.getValue())) {
							xsElementsReferenced.add(currEntry.getValue());
						}
					}
				}
			}
		}
	}

	/**
	 * Funktion, die den Namespace-String eines any-Elements zur�ckgibt
	 */
	public static final XSWildcardFunction<String> WILDCARD_NS = new XSWildcardFunction<String>() {
		@Override
		public String any(Any wc) {
			return ""; // default
		}

		@Override
		public String other(Other wc) {
			return " namespace='##other'";
		}

		@Override
		public String union(Union wc) {
			StringBuffer buf = new StringBuffer(" namespace='");
			boolean first = true;
			for (String s : wc.getNamespaces()) {
				if (first)
					first = false;
				else
					buf.append(' ');
				buf.append(s);
			}
			return buf.append('\'').toString();
		}
	};

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void annotation(XSAnnotation ann) {
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void identityConstraint(XSIdentityConstraint decl) {
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void xpath(XSXPath xp) {
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void empty(XSContentType t) {
	}
}