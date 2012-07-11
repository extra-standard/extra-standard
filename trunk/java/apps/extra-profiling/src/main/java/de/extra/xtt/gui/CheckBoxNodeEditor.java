package de.extra.xtt.gui;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import de.extra.xtt.gui.model.ProfilingTreeNode;

/**
 * CellEditor-Klasse für die Selektion der Checkbox eines Knotens im Tree für die Schema-Elemente
 * 
 * @author Beier
 * 
 */
public class CheckBoxNodeEditor extends AbstractCellEditor implements TreeCellEditor {

	private static final long serialVersionUID = 1L;

	private CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
	private JTree tree;

	/**
	 * Konstruktor, mit dem der aktuelle Tree gespeichert wird
	 * 
	 * @param tree
	 *            Tree-Objekt, in dem die Checkbox angezeigt wird
	 */
	public CheckBoxNodeEditor(JTree tree) {
		this.tree = tree;
	}

	/**
	 * {@inheritdoc}
	 */
	public Object getCellEditorValue() {
		return renderer.isSelected();
	}

	/**
	 * {@inheritdoc}
	 */
	public boolean isCellEditable(EventObject event) {
		boolean returnValue = false;
		if (event instanceof MouseEvent) {
			MouseEvent mouseEvent = (MouseEvent) event;
			TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
			// Prüfen, ob Click weit genug links (auf Checkbox) ist
			boolean clickIsOnCheckBox = mouseEvent.getX() < (tree.getPathBounds(path).x + 18);
			if ((path != null) && clickIsOnCheckBox) {
				Object node = path.getLastPathComponent();
				if ((node != null) && (node instanceof ProfilingTreeNode)) {
					ProfilingTreeNode treeNode = (ProfilingTreeNode) node;
					returnValue = treeNode.isOptional();
				}
			}
		}
		return returnValue;
	}

	/**
	 * {@inheritdoc}
	 */
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row) {

		Component editor = renderer.getTreeCellRendererComponent(tree, value, true, expanded, leaf, row, true);

		// editor always selected / focused
		ItemListener itemListener = new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				if (stopCellEditing()) {
					fireEditingStopped();
				}
			}
		};
		((JCheckBox) editor).addItemListener(itemListener);
		return editor;
	}
}