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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

import de.extra.xtt.gui.model.ProfilingTreeNode;

/**
 * Renderer-Objekt für die Darstellung eines ProfilingTreeNodes mit Checkbox,
 * evtl. externer Referenz und Kennezichnung von Pflichtfeldern
 * 
 * @author Beier
 */
public class CheckBoxNodeRenderer implements TreeCellRenderer {

	private final JCheckBox leafRendererCheckBox = new JCheckBox();

	private final Color textForeground, textBackground;

	/**
	 * Gibt an, ob die Checkbox selektiert ist.
	 * 
	 * @return <code>true</code>, falls selektiert; andernfalls
	 *         <code>false</code>
	 */
	protected boolean isSelected() {
		return leafRendererCheckBox.isSelected();
	}

	/**
	 * Standard-Konstruktor
	 */
	public CheckBoxNodeRenderer() {
		Font fontValue;
		fontValue = UIManager.getFont("Tree.font");
		if (fontValue != null) {
			leafRendererCheckBox.setFont(fontValue);
		}
		Boolean booleanValue = (Boolean) UIManager
				.get("Tree.drawsFocusBorderAroundIcon");
		leafRendererCheckBox.setFocusPainted((booleanValue != null)
				&& (booleanValue.booleanValue()));

		textForeground = UIManager.getColor("Tree.textForeground");
		textBackground = UIManager.getColor("Tree.textBackground");
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		if (selected) {
			leafRendererCheckBox.setBackground(new Color(238, 238, 238));
		} else {
			leafRendererCheckBox.setBackground(textBackground);
		}

		if ((value != null) && (value instanceof ProfilingTreeNode)) {
			ProfilingTreeNode node = (ProfilingTreeNode) value;

			String nodeText = node.toString();
			if (node.isMinOccursChangeable()
					&& (node.getMinOccursDefault() == 0)
					&& (node.getMinOccursUser() >= 1)) {
				nodeText += "*";
			} else {
				nodeText += "  ";
			}

			// externe Referenz
			if (node.hasReferenzExtern()) {
				nodeText += " -->";
				leafRendererCheckBox.setForeground(new Color(128, 0, 0));
			} else {
				leafRendererCheckBox.setForeground(textForeground);
			}

			leafRendererCheckBox.setText(nodeText);

			leafRendererCheckBox.setSelected(node.isChecked());
			if (node.isOptional()) {
				leafRendererCheckBox.setEnabled(true);
			} else {
				// falls Knoten nicht optional dann inaktiv
				leafRendererCheckBox.setEnabled(false);
			}
		}
		return leafRendererCheckBox;
	}

}