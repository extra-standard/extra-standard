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
package de.extra.xtt.gui.model;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import de.extra.xtt.util.schema.SchemaElement;
import de.extra.xtt.util.tools.Configurator;

/**
 * TreeNode zur Verwendung im ProfilingTreeModel; das Knoten-Objekt
 * repräsentiert ein Schema-Element und besitzt Eigenschaften wie minOccurs,
 * maxOccurs usw.
 * 
 * @author Beier
 */
public class ProfilingTreeNode implements TreeNode {

	private final SchemaElement schemaElement;
	private final boolean belongsToChoice;
	private final boolean belongsToSequence;
	private final boolean belongsToWildcard;
	private final boolean hasReferenzExtern;
	private final boolean isLocalElement;
	private final int minOccursDefault;
	private final int maxOccursDefault;

	private ProfilingTreeNode parent;
	private Vector<ProfilingTreeNode> children;
	private boolean isChecked;
	private int minOccursUser;
	private int maxOccursUser;

	/**
	 * Konstruktor mit allen relevanten Eigenschaften des anzulegenden
	 * Knoten-Objekts.
	 * 
	 * @param schemaElement
	 *            SchemaElement, f�r das das Knoten-Objekt erzeugt wird
	 * @param minOccurs
	 *            minOccurs-Angabe aus dem Schema
	 * @param maxOccurs
	 *            maxOccurs-Angabe aus dem Schema
	 * @param belongsToChoice
	 *            Gibt an, ob der Knoten Element einer choice-Gruppe ist
	 * @param belongsToSequqence
	 *            Gibt an, ob der Knoten Element einer sequence-Gruppe ist
	 * @param belongsToWildcard
	 *            Gibt an, ob der Knoten Element einer wildcard ist
	 * @param checkAll
	 *            Gibt an, ob die Knoten standardm��ig selektiert sind
	 * @param hasRefrenzExtern
	 *            Gibt an, ob der Knoten eine Referenz auf einen Knoten aus
	 *            einem anderen TreeModel besitzt
	 * @param isLocalElement
	 *            Gibt an, ob das Element im Schema lokal definiert ist
	 * @param parent
	 *            Vaterknoten des zu erstellenden Knotens
	 */
	public ProfilingTreeNode(SchemaElement schemaElement, int minOccurs,
			int maxOccurs, boolean belongsToChoice, boolean belongsToSequqence,
			boolean belongsToWildcard, boolean checkAll,
			boolean hasRefrenzExtern, boolean isLocalElement,
			ProfilingTreeNode parent) {
		this.schemaElement = schemaElement;
		this.minOccursDefault = minOccurs;
		this.maxOccursDefault = maxOccurs;
		this.minOccursUser = minOccurs;
		this.maxOccursUser = maxOccurs;
		this.belongsToChoice = belongsToChoice;
		this.belongsToSequence = belongsToSequqence;
		this.belongsToWildcard = belongsToWildcard;
		this.hasReferenzExtern = hasRefrenzExtern;
		this.isLocalElement = isLocalElement;
		this.parent = parent;
		if (!isOptional()) {
			this.isChecked = true;
		}
		if (checkAll && isOptional()) {
			this.isChecked = true;
		}
	}

	/**
	 * Pr�ft, ob die Kind-Knoten des aktuellen Knotens dem �bergebenenen
	 * Schema-Element entsprechen und gibt ggf. diesen Knd-Knoten zur�ck.
	 * 
	 * @param schemaElement
	 *            SchemaElement, auf das die Kind-Knoten gepr�ft werden
	 * @return TreeNode, der dem SchemaElement entpricht
	 */
	public ProfilingTreeNode getChildWithSameSchemaElement(
			SchemaElement schemaElement) {
		for (Enumeration<ProfilingTreeNode> e = this.children(); e
				.hasMoreElements();) {
			ProfilingTreeNode currNode = e.nextElement();
			if (schemaElement.equals(currNode.getSchemaElement())) {
				return currNode;
			}
		}
		return null;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public Enumeration<ProfilingTreeNode> children() {
		if (children == null) {
			Enumeration<ProfilingTreeNode> childs = new Enumeration<ProfilingTreeNode>() {
				@Override
				public boolean hasMoreElements() {
					return false;
				}

				@Override
				public ProfilingTreeNode nextElement() {
					throw new NoSuchElementException("Keine Elemente vorhanden");
				}
			};
			return childs;
		} else {
			return children.elements();
		}
	}

	/**
	 * F�gt eine Liste von TreeNodes als Kind-Elemente diesem Knoten hinzu
	 * 
	 * @param children
	 */
	public void addChildren(Vector<ProfilingTreeNode> children) {
		this.children = children;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public ProfilingTreeNode getChildAt(int childIndex) {
		if (children == null) {
			throw new ArrayIndexOutOfBoundsException("Knoten hat keine Kinder");
		}
		return children.elementAt(childIndex);
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public int getChildCount() {
		if (children == null) {
			return 0;
		} else {
			return children.size();
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public int getIndex(TreeNode aChild) {
		if (aChild == null) {
			throw new IllegalArgumentException("argument is null");
		}

		if (!isNodeChild(aChild)) {
			return -1;
		}
		return children.indexOf(aChild); // linear search
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public ProfilingTreeNode getParent() {
		return parent;
	}

	/**
	 * Gibt die minOccurs-Eigenschaft zur�ck, die im Schema f�r dieses Element
	 * spezifiziert ist
	 * 
	 * @return minOccurs-Eigenschaft, die im Schema f�r dieses Element
	 *         spezifiziert ist
	 */
	public int getMinOccursDefault() {
		return minOccursDefault;
	}

	/**
	 * Gibt die minOccurs-Eigenschaft zur�ck, die vom Benutzer angepasst werden
	 * kann
	 * 
	 * @return minOccurs-Eigenschaft, die vom Benutzer angepasst werden kann
	 */
	public int getMinOccursUser() {
		return minOccursUser;
	}

	/**
	 * Gibt die maxOccurs-Eigenschaft zur�ck, die im Schema f�r dieses Element
	 * spezifiziert ist
	 * 
	 * @return maxOccurs-Eigenschaft, die im Schema f�r dieses Element
	 *         spezifiziert ist
	 */
	public int getMaxOccursDefault() {
		return maxOccursDefault;
	}

	/**
	 * Gibt die maxOccurs-Eigenschaft zur�ck, die vom Benutzer angepasst werden
	 * kann
	 * 
	 * @return maxOccurs-Eigenschaft, die vom Benutzer angepasst werden kann
	 */
	public int getMaxOccursUser() {
		return maxOccursUser;
	}

	/**
	 * Setzt die parent-Eigenschaft des aktuellen Knotens auf das �bergebene
	 * Knoten-Objekt
	 * 
	 * @param parent
	 *            Knoten-Objekt
	 * 
	 */
	public void setParent(ProfilingTreeNode parent) {
		this.parent = parent;
	}

	/**
	 * Entfernt den angegebenen Kind-Knoten, falls vorhanden
	 * 
	 * @param nodeChild
	 *            Kind-Knoten, der entfernt werden soll
	 */
	public void removeChild(ProfilingTreeNode nodeChild) {
		if (nodeChild != null) {
			this.children.remove(nodeChild);
			nodeChild.setParent(null);
		}
	}

	/**
	 * Liefert die SchemaElement-Eigenschaft dieses Knotens zur�ck
	 * 
	 * @return SchemaElement-Eigenschaft des Knotens
	 */
	public SchemaElement getSchemaElement() {
		return schemaElement;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public boolean isLeaf() {
		return (getChildCount() == 0);
	}

	/**
	 * Gibt an, ob es sich bei dem Knoten um das Wurzelelement der
	 * referenzierten Elemente handelt
	 * 
	 * @param configurator
	 *            Konfiguratorobjekt mit Zugriff auf Properties und
	 *            Einstellungen
	 * 
	 * @return true, falls Wurzelelement der referenzierten Elemente
	 */
	public boolean isRootReferencedElements(Configurator configurator) {
		return schemaElement
				.getNameWithPrefix()
				.equals(configurator
						.getResString("XSDCREATORCTRL_TEXT_MEHRFACH_REF_ELEMENTE"));
	}

	/**
	 * Erzeugt einen Info-String f�r den aktuellen Knoten mit Angaben zur
	 * Gruppenzugeh�rigkeit (sequence / choice), minOccurs und maxOccurs
	 * 
	 * @return Info-Text
	 */
	public String getInfoEigenschaften() {
		String infoText = "";
		if (belongsToChoice) {
			infoText += "choice";
		} else if (belongsToSequence) {
			infoText += "sequence";
		}
		if (belongsToWildcard) {
			infoText += " (any)";
		}
		if (minOccursDefault >= 0) {
			infoText += " / minOccurs=" + minOccursDefault;
		}
		if (maxOccursUser >= 0) {
			if (maxOccursUser == Integer.MAX_VALUE) {
				infoText += " / maxOccurs=unbounded";
			} else {
				infoText += " / maxOccurs=" + maxOccursUser;
			}
		}
		return infoText;
	}

	/**
	 * Gibt an, ab der aktuelle Knoten laut Schemadefinition optional ist.
	 * 
	 * @return <code>true</code>, falls der Knoten optional ist, sonst
	 *         <code>false</code>
	 */
	public boolean isOptional() {
		return (!belongsToWildcard && ((minOccursDefault == 0) || belongsToChoice));
	}

	/**
	 * Gibt an, ob der aktuelle Knoten Teil einer Wildcard ('any') ist
	 * 
	 * @return <code>true</code>, falls Knoten Unterelement von 'any', sonst
	 *         <code>false</code>
	 */
	public boolean belongsToWildcard() {
		return belongsToWildcard;
	}

	/**
	 * Gibt an, ob der Wert f�r minOccurs ge�ndert werden darf
	 * 
	 * @return <code>true</code>, falls �nderung m�glich, sonst
	 *         <code>false</code>
	 */
	public boolean isMinOccursChangeable() {
		return (!belongsToWildcard && (belongsToChoice || belongsToSequence));
	}

	/**
	 * Gibt an, ob der Wert f�r maxOccurs ge�ndert werden darf
	 * 
	 * @return <code>true</code>, falls �nderung m�glich, sonst
	 *         <code>false</code>
	 */
	public boolean isMaxOccursChangeable() {
		return (!belongsToWildcard && (belongsToChoice || belongsToSequence));
	}

	/**
	 * Gibt an, ob der Knoten selektiert ist
	 * 
	 * @return <code>true</code>, falls selektiert, sonst <code>false</code>
	 */
	public boolean isChecked() {
		return isChecked;
	}

	/**
	 * Setzt den Selektionsstatus des Knotens
	 * 
	 * @param isChecked
	 *            Gibt an, ob der Knoten selektiert werden soll oder nicht
	 */
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	/**
	 * Setzt den Wert f�r die minOccurs-Eigenschaft, die der Bneutzer �ndern
	 * darf
	 * 
	 * @param value
	 *            neuer Wert f�r die minOccurs-Eigenschaft, die vom Benutzer
	 *            �nderbar ist
	 */
	public void setMinOccurUser(int value) {
		this.minOccursUser = value;
	}

	/**
	 * Setzt den Wert f�r die maxOccurs-Eigenschaft, die der Bneutzer �ndern
	 * darf
	 * 
	 * @param value
	 *            neuer Wert f�r die maxOccurs-Eigenschaft, die vom Benutzer
	 *            �nderbar ist
	 */
	public void setMaxOccurUser(int value) {
		this.maxOccursUser = value;
	}

	/**
	 * Gibt an, ob der Knoten eine Refrenz auf einen Knoten au�erhalb dieses
	 * TreeModels besitzt.
	 * 
	 * @return <code>true</code>, falls extrene Referenz vorhanden ist, sonst
	 *         <code>false</code>
	 */
	public boolean hasReferenzExtern() {
		return hasReferenzExtern;
	}

	/**
	 * Gibt an, ob das Element dieses Knotens im Schema lokal definiert wird
	 * 
	 * @return <code>true</code>: Element wird lokal definiert;
	 *         <code>false</code>: Referenz
	 */
	public boolean isLocalElement() {
		return isLocalElement;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public String toString() {
		return schemaElement.getName();
	}

	/**
	 * Gibt an, ob der �bergebene Knoten ein Kind-Objekt des aktuellen Knotens
	 * ist
	 * 
	 * @param aNode
	 *            Knotenobjekt, das mit den Kindknoten verglichen wird
	 * @return <code>true</code>, falls das angegebene Knoten-Objekt Kindknoten
	 *         vom aktuellen Knoten ist; sonst <code>false</code>
	 */
	private boolean isNodeChild(TreeNode aNode) {
		boolean retval;

		if (aNode == null) {
			retval = false;
		} else {
			if (getChildCount() == 0) {
				retval = false;
			} else {
				retval = (aNode.getParent() == this);
			}
		}
		return retval;
	}

}
