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
 * CellEditor-Klasse für die Selektion der Checkbox eines Knotens im Tree für
 * die Schema-Elemente
 * 
 * @author Beier
 */
public class CheckBoxNodeEditor extends AbstractCellEditor implements
		TreeCellEditor {

	private static final long serialVersionUID = 1L;

	private final CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
	private final JTree tree;

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
	@Override
	public Object getCellEditorValue() {
		return renderer.isSelected();
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public boolean isCellEditable(EventObject event) {
		boolean returnValue = false;
		if (event instanceof MouseEvent) {
			MouseEvent mouseEvent = (MouseEvent) event;
			TreePath path = tree.getPathForLocation(mouseEvent.getX(),
					mouseEvent.getY());
			// Pr�fen, ob Click weit genug links (auf Checkbox) ist
			boolean clickIsOnCheckBox = mouseEvent.getX() < (tree
					.getPathBounds(path).x + 18);
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
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row) {

		Component editor = renderer.getTreeCellRendererComponent(tree, value,
				true, expanded, leaf, row, true);

		// editor always selected / focused
		ItemListener itemListener = new ItemListener() {
			@Override
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