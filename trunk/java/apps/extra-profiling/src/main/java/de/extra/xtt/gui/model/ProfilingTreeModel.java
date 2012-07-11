package de.extra.xtt.gui.model;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import de.extra.xtt.util.schema.SchemaElement;
import de.extra.xtt.util.tools.XsdXmlHelper;

/**
 * TreeModel für die Abbildung einer Profilkonfiguration für die Profilierung eines eXTra-Schemas. Die einzelnen Knoten stellen die verfügbaren Schema-Elemente dar und haben Eigenschaften wie
 * minOccurs und maxOccurs. das Model besitzt außerdem die Funktion, die bereits initialisierten Knoten einer neuen Profilkonfiguration anzupassen.
 * 
 * @author Beier
 * 
 */
public class ProfilingTreeModel implements TreeModel {

	private static final List<TreeModelListener> listeners = new LinkedList<TreeModelListener>();
	private final TreeNode root;
	private final ProfilingTreeModel treeModelRefElements;
	
	private List<ProfilingTreeNode> checkedNodes = new LinkedList<ProfilingTreeNode>();
	private List<ProfilingTreeNode> uncheckedNodes = new LinkedList<ProfilingTreeNode>();

	/**
	 * Konstruktor, der neben dem Wurzelknoten auch das Model für die referenzierten Elemente enthält
	 * 
	 * @param nodeRoot
	 *            Wurzel-Knoten
	 * @param treeModelRefElements
	 *            TreeModel mit den referenzierten Elementen
	 */
	public ProfilingTreeModel(ProfilingTreeNode nodeRoot, ProfilingTreeModel treeModelRefElements) {
		this.root = nodeRoot;
		this.treeModelRefElements = treeModelRefElements;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public Object getChild(Object parent, int index) {
		return ((TreeNode) parent).getChildAt(index);
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public int getChildCount(Object parent) {
		return ((TreeNode) parent).getChildCount();
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (parent == null || child == null) {
			return -1;
		}
		return ((TreeNode) parent).getIndex((TreeNode) child);
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public Object getRoot() {
		return root;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public boolean isLeaf(Object node) {
		return ((TreeNode) node).isLeaf();
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listeners.remove(l);
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void addTreeModelListener(TreeModelListener l) {
		listeners.add(l);
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		ProfilingTreeNode currNode = (ProfilingTreeNode) path.getLastPathComponent();
		currNode.setChecked((Boolean) newValue);
		if (currNode.isChecked()) {
			checkedNodes.add(currNode);
			uncheckedNodes.remove(currNode);
		} else {
			checkedNodes.remove(currNode);
			uncheckedNodes.add(currNode);
		}
		fireModelChangedEvent(path);
	}

	/**
	 * 
	 * Gibt das TreeModel mit den referenzierten Elementen zurück.
	 * 
	 * @return TreeModel mit den referenzierten Elementen
	 */
	public ProfilingTreeModel getTreeModelRefElements() {
		return treeModelRefElements;
	}

	/**
	 * Die angegebene Profilkonfiguration wird auf die bestehenden Knoten angewendet; z.B. wird der Selektionsstatus der einzelnen Elemente angepasst.
	 * 
	 * @param docXmlProfil
	 *            Profilkonfiguration als XML-DOM-Dokument
	 * @throws XPathExpressionException
	 */
	public void applyXmlConfig(Document docXmlProfil) throws XPathExpressionException {
		// Haupt-Elemente
		applyXmlConfigToNode(docXmlProfil, (ProfilingTreeNode) root);
		// referenzierte Elemente
		if (treeModelRefElements != null) {
			applyXmlConfigToNode(docXmlProfil, (ProfilingTreeNode) treeModelRefElements.getRoot());
		}
	}

	/**
	 * Die angegebene Profilkonfiguration wird auf die Kind-Knoten des übergebenen Knotens angewendet.
	 * 
	 * @param docXmlProfil
	 *            Profilkonfiguration als XML-DOM-Dokument
	 * @param nodeParent
	 *            Knoten, dessen Kind-Elemente geprüft werden
	 * @throws XPathExpressionException
	 */
	private void applyXmlConfigToNode(Document docXmlProfil, ProfilingTreeNode nodeParent)
			throws XPathExpressionException {
		if (nodeParent.getChildCount() > 0) {
			SchemaElement parentSchemaElement = nodeParent.getSchemaElement();
			// Falls Kind-Elemente vorhanden sind, wird für diese geprüft, ob sie in der XML-Konfiguration angegeben sind
			for (Enumeration<ProfilingTreeNode> e = nodeParent.children(); e.hasMoreElements();) {
				ProfilingTreeNode currNode = e.nextElement();
				SchemaElement currSchemaElement = currNode.getSchemaElement();
				String xPathStr = "//element[name/text()='" + parentSchemaElement.getNameWithPrefix()
						+ "']/kind[text()='" + currSchemaElement.getNameWithPrefix() + "']";
				NodeList nl = XsdXmlHelper.xpathSuche(xPathStr, docXmlProfil);
				if ((nl != null) && (nl.getLength() > 0)) {
					currNode.setChecked(true);
					// prüfen, ob Angaben zu min- oder maxOccurs
					NodeList listNodes = XsdXmlHelper.xpathSuche("attribute::minOccurs", nl.item(0));
					if (listNodes.getLength() > 0) {
						String minOccStr = listNodes.item(0).getNodeValue();
						currNode.setMinOccurUser(Integer.parseInt(minOccStr));
					}
					listNodes = XsdXmlHelper.xpathSuche("attribute::maxOccurs", nl.item(0));
					if (listNodes.getLength() > 0) {
						String maxOccStr = listNodes.item(0).getNodeValue();
						currNode.setMaxOccurUser(Integer.parseInt(maxOccStr));
					}
				}
				// Rekursiver Aufruf für das jeweilige Kind-Element
				applyXmlConfigToNode(docXmlProfil, currNode);
			}
		}
	}

	private void fireModelChangedEvent(TreePath path) {
		for (TreeModelListener l : listeners) {
			l.treeNodesChanged(new TreeModelEvent(this, path));
		}
	}

}
