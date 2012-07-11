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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import de.extra.xtt.gui.model.ProfilingTreeModel;
import de.extra.xtt.gui.model.ProfilingTreeNode;
import de.extra.xtt.util.XsdCreatorCtrl;
import de.extra.xtt.util.XsdCreatorCtrlException;
import de.extra.xtt.util.XsdCreatorCtrlImpl;
import de.extra.xtt.util.tools.Configurator;
import de.extra.xtt.util.tools.ConfiguratorException;

/**
 * Diese Klasse stellt das Hauptfenster der Anwendung dar.
 * 
 * @author Beier
 */
public class XsdCreator extends JFrame implements TreeSelectionListener {

	private static Logger logger = Logger.getLogger(XsdCreator.class);
	private final XsdCreatorCtrl xsdCreatorCtrl;
	private final Configurator configurator;

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel jPanelLeft = null;
	private JPanel jPanelMain = null;
	private JPanel jPanelMainHeader = null;
	private JLabel jLabelSchemaLab;
	private JTextPane fAnmerkAllgemein = null;
	private JButton jButtonNewForRequest = null;
	private JLabel jLabelNewKonfig = null;
	private JButton jButtonNewForRespone = null;
	private JButton jButtonSaveKonfig = null;
	private JButton jButtonLoadKonfig = null;
	private JButton jButtonCreateSchema = null;
	private JLabel jLabelKonfigLab;
	private JButton jButtonSaveAsKonfig = null;
	private JLabel jLabelSchemaTxt;
	private JLabel jLabelKonfigTxt;
	private JTextField fBezVerfahren;
	private JLabel lblBezVerfahren;
	private JPanel panelTree;
	private JScrollPane scrollPaneAdditional;
	private JScrollPane scrollPaneMain;
	private JTree treeMain;
	private JTree treeAdditional;

	private About about = null;
	private HelpDialog helpDialog = null;
	private JTextField fBezKurzVerfahren;
	private JLabel jLabelSelectedElement;
	private JLabel jLabelElementEigenschaften;

	/**
	 * This method initializes jPanelMain
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelLeft() {
		if (jPanelLeft == null) {
			jLabelNewKonfig = new JLabel();
			jLabelNewKonfig.setFont(new Font("Tahoma", Font.BOLD, 11));
			jLabelNewKonfig.setText(configurator
					.getResString("XSDCREATOR_LABEL_NEUE_PROFILKONFIGURATION"));
			jLabelNewKonfig.setLocation(new Point(15, 8));
			jLabelNewKonfig.setSize(new Dimension(202, 16));
			jPanelLeft = new JPanel();
			jPanelLeft.setMinimumSize(new Dimension(100, 200));
			jPanelLeft.setLayout(null);
			jPanelLeft.setPreferredSize(new Dimension(250, 0));
			jPanelLeft.setBackground(new Color(238, 238, 238));
			jPanelLeft.add(getJButtonNewForRequest(), null);
			jPanelLeft.add(jLabelNewKonfig, null);
			jPanelLeft.add(getJButtonNewForRespone(), null);
			jPanelLeft.add(getJButtonSaveKonfig(), null);
			jPanelLeft.add(getJButtonLoadKonfig(), null);
			jPanelLeft.add(getJButtonCreateSchema(), null);
			jPanelLeft.add(getJButtonSaveAsKonfig(), null);

			JLabel labelTextBtnSchema = new JLabel();
			labelTextBtnSchema.setHorizontalAlignment(SwingConstants.CENTER);
			labelTextBtnSchema.setVerticalAlignment(SwingConstants.TOP);
			labelTextBtnSchema.setText((String) null);
			labelTextBtnSchema.setSize(new Dimension(124, 16));
			labelTextBtnSchema.setLocation(new Point(3, 28));
			labelTextBtnSchema.setFont(new Font("Tahoma", Font.PLAIN, 11));
			labelTextBtnSchema.setBounds(20, 238, 210, 16);
			labelTextBtnSchema.setText(configurator
					.getResString("XSDCREATOR_LABEL_TEXTBTNSCHEMA"));
			jPanelLeft.add(labelTextBtnSchema);
		}
		return jPanelLeft;
	}

	/**
	 * This method initializes jPanelTree
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelMain() {
		if (jPanelMain == null) {
			jPanelMain = new JPanel();
			jPanelMain.setBorder(new EmptyBorder(0, 0, 0, 5));
			jPanelMain.setLayout(new BorderLayout());
			jPanelMain.add(getJPanelMainHeader(), BorderLayout.NORTH);

			JSplitPane jPanelMainMain = new JSplitPane();
			jPanelMainMain.setResizeWeight(1.0);
			jPanelMainMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jPanelMain.add(jPanelMainMain, BorderLayout.CENTER);

			JPanel panelMainBottom = new JPanel();
			panelMainBottom.setBorder(new EmptyBorder(0, 0, 0, 0));
			panelMainBottom.setMinimumSize(new Dimension(10, 170));
			jPanelMainMain.setRightComponent(panelMainBottom);
			panelMainBottom.setLayout(new BorderLayout(0, 0));
			panelMainBottom.add(getPanel_3(), BorderLayout.CENTER);

			JPanel panelMainTree = new JPanel();
			panelMainTree.setBounds(new Rectangle(0, 0, 0, 200));
			panelMainTree.setSize(new Dimension(0, 200));
			panelMainTree.setPreferredSize(new Dimension(10, 300));
			jPanelMainMain.setLeftComponent(panelMainTree);
			panelMainTree.setLayout(new BorderLayout(0, 0));
			panelMainTree.add(getPanelTree(), BorderLayout.CENTER);
			jPanelMainMain.setDividerLocation(244);
		}
		return jPanelMain;
	}

	/**
	 * This method initializes jPanelTreeHeader
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelMainHeader() {
		if (jPanelMainHeader == null) {
			jPanelMainHeader = new JPanel();
			jPanelMainHeader.setBackground(new Color(245, 245, 245));
			jPanelMainHeader.setPreferredSize(new Dimension(0, 104));
			jPanelMainHeader
					.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			GridBagLayout gbl_jPanelMainHeader = new GridBagLayout();
			gbl_jPanelMainHeader.columnWidths = new int[] { 0, 0 };
			gbl_jPanelMainHeader.rowHeights = new int[] { 0, 0, 0, 0 };
			gbl_jPanelMainHeader.columnWeights = new double[] { 0.0, 1.0 };
			gbl_jPanelMainHeader.rowWeights = new double[] { 0.25, 0.25, 0.25,
					0.25 };
			jPanelMainHeader.setLayout(gbl_jPanelMainHeader);
			jLabelSchemaLab = new JLabel();
			GridBagConstraints gbc_jLabelSchemaLab = new GridBagConstraints();
			gbc_jLabelSchemaLab.anchor = GridBagConstraints.WEST;
			gbc_jLabelSchemaLab.insets = new Insets(0, 0, 0, 5);
			gbc_jLabelSchemaLab.gridx = 0;
			gbc_jLabelSchemaLab.gridy = 0;
			jPanelMainHeader.add(jLabelSchemaLab, gbc_jLabelSchemaLab);
			jLabelSchemaLab.setFont(new Font("Tahoma", Font.BOLD, 11));
			jLabelSchemaLab.setText(configurator
					.getResString("XSDCREATOR_LABEL_SCHEMDATEI"));

			jLabelSchemaTxt = new JLabel();
			GridBagConstraints gbc_jLabelSchemaTxt = new GridBagConstraints();
			gbc_jLabelSchemaTxt.fill = GridBagConstraints.HORIZONTAL;
			gbc_jLabelSchemaTxt.anchor = GridBagConstraints.WEST;
			gbc_jLabelSchemaTxt.gridx = 1;
			gbc_jLabelSchemaTxt.gridy = 0;
			jPanelMainHeader.add(jLabelSchemaTxt, gbc_jLabelSchemaTxt);
			jLabelKonfigLab = new JLabel();
			GridBagConstraints gbc_jLabelKonfigLab = new GridBagConstraints();
			gbc_jLabelKonfigLab.anchor = GridBagConstraints.WEST;
			gbc_jLabelKonfigLab.insets = new Insets(0, 0, 0, 5);
			gbc_jLabelKonfigLab.gridx = 0;
			gbc_jLabelKonfigLab.gridy = 1;
			jPanelMainHeader.add(jLabelKonfigLab, gbc_jLabelKonfigLab);
			jLabelKonfigLab.setFont(new Font("Tahoma", Font.BOLD, 11));
			jLabelKonfigLab.setText(configurator
					.getResString("XSDCREATOR_LABEL_KONFIGURATIONSDATEI"));

			jLabelKonfigTxt = new JLabel();
			GridBagConstraints gbc_jLabelKonfigTxt = new GridBagConstraints();
			gbc_jLabelKonfigTxt.anchor = GridBagConstraints.WEST;
			gbc_jLabelKonfigTxt.fill = GridBagConstraints.HORIZONTAL;
			gbc_jLabelKonfigTxt.gridx = 1;
			gbc_jLabelKonfigTxt.gridy = 1;
			jPanelMainHeader.add(jLabelKonfigTxt, gbc_jLabelKonfigTxt);
			GridBagConstraints gbc_lblBezVerfahren = new GridBagConstraints();
			gbc_lblBezVerfahren.anchor = GridBagConstraints.WEST;
			gbc_lblBezVerfahren.insets = new Insets(0, 0, 0, 5);
			gbc_lblBezVerfahren.gridx = 0;
			gbc_lblBezVerfahren.gridy = 2;
			jPanelMainHeader.add(getLblBezVerfahren(), gbc_lblBezVerfahren);
			GridBagConstraints gbc_fBezVerfahren = new GridBagConstraints();
			gbc_fBezVerfahren.anchor = GridBagConstraints.WEST;
			gbc_fBezVerfahren.fill = GridBagConstraints.HORIZONTAL;
			gbc_fBezVerfahren.gridx = 1;
			gbc_fBezVerfahren.gridy = 2;
			jPanelMainHeader.add(getFBezVerfahren(), gbc_fBezVerfahren);

			JLabel lblBezKurzVerfahren = new JLabel();
			GridBagConstraints gbc_lblBezKurzVerfahren = new GridBagConstraints();
			gbc_lblBezKurzVerfahren.anchor = GridBagConstraints.WEST;
			gbc_lblBezKurzVerfahren.insets = new Insets(0, 0, 0, 5);
			gbc_lblBezKurzVerfahren.gridx = 0;
			gbc_lblBezKurzVerfahren.gridy = 3;
			jPanelMainHeader.add(lblBezKurzVerfahren, gbc_lblBezKurzVerfahren);
			lblBezKurzVerfahren.setText(configurator
					.getResString("XSDCREATOR_LABEL_BEZEICHNUNGKURZVERFAHREN"));
			lblBezKurzVerfahren.setFont(new Font("Tahoma", Font.BOLD, 11));

			fBezKurzVerfahren = new JTextField();
			fBezKurzVerfahren.setPreferredSize(new Dimension(6, 22));
			fBezKurzVerfahren.setMargin(new Insets(0, 2, 0, 2));
			GridBagConstraints gbc_fBezKurzVerfahren = new GridBagConstraints();
			gbc_fBezKurzVerfahren.fill = GridBagConstraints.HORIZONTAL;
			gbc_fBezKurzVerfahren.gridx = 1;
			gbc_fBezKurzVerfahren.gridy = 3;
			jPanelMainHeader.add(fBezKurzVerfahren, gbc_fBezKurzVerfahren);
			fBezKurzVerfahren.setColumns(10);
		}
		return jPanelMainHeader;
	}

	private ProfilingTreeNode currNode;
	private JButton btnOkAnmerkungenAllg;
	private JButton btnOkAnmerkungenVerwendung;
	private JPanel panel_3;
	private JScrollPane scrollPane_1;
	private JTextPane fAnmerkVerwendung;
	private JSpinner spinnerMinOccurs;
	private JSpinner spinnerMaxOccurs;
	private JButton btnMax;

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		currNode = (ProfilingTreeNode) ((JTree) e.getSource())
				.getLastSelectedPathComponent();
		setValuesCurrentSelectedNode();
	}

	private void setValuesCurrentSelectedNode() {
		if (currNode == null) {
			// Kein Knoten ausgew�hlt
			// Text zum selektierten Element
			jLabelSelectedElement.setText("");
			jLabelElementEigenschaften.setText("");
			// min-/maxOccurs-Angaben
			spinnerMaxOccurs.setEnabled(false);
			spinnerMinOccurs.setEnabled(false);
			btnMax.setEnabled(false);
			// Anmerkungen, allgemein
			fAnmerkAllgemein.setText("");
			fAnmerkAllgemein.setEnabled(false);
			btnOkAnmerkungenAllg.setEnabled(false);
			// Anmerkungen, zur Verwendung
			fAnmerkVerwendung.setText("");
			fAnmerkVerwendung.setEnabled(false);
			btnOkAnmerkungenVerwendung.setEnabled(false);
		} else {

			// Text zum selektierten Element
			jLabelSelectedElement.setText(currNode.getSchemaElement()
					.getNameWithPrefix());
			jLabelElementEigenschaften.setText(currNode.getInfoEigenschaften());

			// min-/maxOccurs-Angaben
			spinnerMinOccurs.setEnabled(currNode.isMinOccursChangeable());
			spinnerMaxOccurs.setEnabled(currNode.isMaxOccursChangeable());
			btnMax.setEnabled(currNode.isMaxOccursChangeable());
			if (currNode.isMinOccursChangeable()) {
				// aktuellen Wert und Beschr�nkung setzen
				spinnerMinOccurs.setModel(new SpinnerNumberModel(currNode
						.getMinOccursUser(), currNode.getMinOccursDefault(),
						currNode.getMaxOccursDefault(), 1));
			} else {
				spinnerMinOccurs.setValue(0);
			}
			if (currNode.isMaxOccursChangeable()) {
				// aktuellen Wert setzen
				spinnerMaxOccurs.setModel(new SpinnerNumberModel(currNode
						.getMaxOccursUser(), currNode.getMinOccursDefault(),
						currNode.getMaxOccursDefault(), 1));
			} else {
				spinnerMaxOccurs.setValue(0);
			}

			// Anmerkungen, allgemein
			fAnmerkAllgemein.setText(configurator
					.getAnmerkungAllgemein(currNode));
			fAnmerkAllgemein.setCaretPosition(0);
			fAnmerkAllgemein.setEnabled(true);
			btnOkAnmerkungenAllg.setEnabled(true);

			// Anmerkungen, zur Verwendung
			if ((currNode.getParent() == null)
					|| currNode.getParent().isRootReferencedElements(
							configurator) || currNode.belongsToWildcard()
					|| currNode.isLocalElement()) {
				// - kein Vaterknoten
				// - Vaterknoten ist Wurzel der mehrfach referenzierten Elemente
				// - aktuelles Element ist Teil einer any-Sequenz
				// - aktuelles Element wird im Schema lokal definiert (es reicht
				// eine Anmerkung)
				// => keine Verwendungsanmerkung
				fAnmerkVerwendung
						.setText(configurator
								.getResString("XSDCREATOR_TEXTPANE_ANMERKUNGENVERWENDUNG"));
				fAnmerkVerwendung.setEnabled(false);
				btnOkAnmerkungenVerwendung.setEnabled(false);
			} else {
				fAnmerkVerwendung.setText(configurator
						.getAnmerkungVerwendung(currNode));
				fAnmerkAllgemein.setCaretPosition(0);
				fAnmerkVerwendung.setEnabled(true);
				btnOkAnmerkungenVerwendung.setEnabled(true);
			}
		}
	}

	/**
	 * This method initializes jButtonNewForRequest
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonNewForRequest() {
		if (jButtonNewForRequest == null) {
			jButtonNewForRequest = new JButton();
			jButtonNewForRequest.setText(configurator
					.getResString("XSDCREATOR_BTN_RQUEST"));
			jButtonNewForRequest.setSize(new Dimension(100, 26));
			jButtonNewForRequest.setLocation(new Point(15, 35));
			jButtonNewForRequest
					.addActionListener(new java.awt.event.ActionListener() {
						@Override
						public void actionPerformed(java.awt.event.ActionEvent e) {
							XsdCreator.this.setCursor(new Cursor(
									Cursor.WAIT_CURSOR));
							int n = JOptionPane.YES_OPTION;
							if ((treeMain != null)
									&& (treeMain.getModel() != null)) {
								n = JOptionPane
										.showConfirmDialog(
												jContentPane,
												configurator
														.getResString("XSDCREATOR_DLG_ANPASSUNGEN_VERWERFEN"),
												configurator
														.getResString("XSDCREATOR_DLG_NEUE_PROFILIERUNG"),
												JOptionPane.YES_NO_OPTION);
							}
							if (n == JOptionPane.YES_OPTION) {
								try {
									setCurrFileKonfigToNull(xsdCreatorCtrl
											.createTreeModelForRequest());
								} catch (XsdCreatorCtrlException e1) {
									logger.error(
											"Fehler beim Anzeigen des Request-Schemas:",
											e1);
									JOptionPane
											.showMessageDialog(
													null,
													configurator
															.getResString("XSDCREATOR_DLG_FEHLER_TREEMODEL_REQUEST"),
													configurator
															.getResString("XSDCREATOR_DLG_NEUE_PROFILIERUNG"),
													JOptionPane.ERROR_MESSAGE);
								}
							}
							XsdCreator.this.setCursor(new Cursor(
									Cursor.DEFAULT_CURSOR));
						}
					});
		}
		return jButtonNewForRequest;
	}

	private void setCurrFileKonfigAndRefreshView(File currFileKonfig)
			throws XsdCreatorCtrlException {
		xsdCreatorCtrl.setXmlFileConfig(currFileKonfig);
		if (currFileKonfig != null) {
			// Neuen Schema-Baum erzeugen und anzeigen
			ProfilingTreeModel treeModelMain = xsdCreatorCtrl
					.createTreeModelForCurrentConfig();
			scrollPaneMain.setViewportView(getTreeMain(treeModelMain));
			if (treeModelMain.getTreeModelRefElements() != null) {
				// Baum mit referenzierten Elementen anzeigen
				scrollPaneAdditional
						.setViewportView(getTreeAdditional(treeModelMain
								.getTreeModelRefElements()));
			} else {
				scrollPaneAdditional.setViewportView(null);
			}
		}
		refreshTextAndButtons();
		// Aktuellen Knoten auf NULL setzen
		currNode = null;
		setValuesCurrentSelectedNode();
	}

	private void setCurrFileKonfigToNull(ProfilingTreeModel treeModel)
			throws XsdCreatorCtrlException {
		xsdCreatorCtrl.setXmlFileConfig(null);
		// Neuen Schema-Baum anzeigen
		scrollPaneMain.setViewportView(getTreeMain(treeModel));
		if (treeModel.getTreeModelRefElements() != null) {
			// Baum mit referenzierten Elementen anzeigen
			scrollPaneAdditional.setViewportView(getTreeAdditional(treeModel
					.getTreeModelRefElements()));
		} else {
			scrollPaneAdditional.setViewportView(null);
		}
		refreshTextAndButtons();
		// Aktuellen Knoten auf NULL setzen
		currNode = null;
		setValuesCurrentSelectedNode();
	}

	private void refreshTextAndButtons() {
		jLabelSchemaTxt.setText(xsdCreatorCtrl.getPathQuellSchema());
		jLabelKonfigTxt.setText(xsdCreatorCtrl.getPathXmlConfig());
		fBezVerfahren.setText(xsdCreatorCtrl.getBezeichnungVerfahren());
		fBezKurzVerfahren.setText(xsdCreatorCtrl.getBezeichnungKurzVerfahren());
		if (treeMain.getModel() != null) {
			jButtonSaveAsKonfig.setEnabled(true);
			jButtonSaveKonfig.setEnabled(xsdCreatorCtrl.isDocXmlLoaded());
			jButtonCreateSchema.setEnabled(xsdCreatorCtrl.isDocXmlLoaded());
		} else {
			// leerer Baum
			jButtonSaveKonfig.setEnabled(false);
			jButtonCreateSchema.setEnabled(false);
			jButtonSaveAsKonfig.setEnabled(false);
		}
	}

	/**
	 * This method initializes jButtonNewForRespone
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonNewForRespone() {
		if (jButtonNewForRespone == null) {
			jButtonNewForRespone = new JButton();
			jButtonNewForRespone.setMnemonic(KeyEvent.VK_UNDEFINED);
			jButtonNewForRespone.setLocation(new Point(135, 35));
			jButtonNewForRespone.setSize(new Dimension(100, 26));
			jButtonNewForRespone.setText(configurator
					.getResString("XSDCREATOR_BTN_RESPONSE"));
			jButtonNewForRespone
					.addActionListener(new java.awt.event.ActionListener() {
						@Override
						public void actionPerformed(java.awt.event.ActionEvent e) {
							XsdCreator.this.setCursor(new Cursor(
									Cursor.WAIT_CURSOR));
							int n = JOptionPane.YES_OPTION;
							if ((treeMain != null)
									&& (treeMain.getModel() != null)) {
								n = JOptionPane
										.showConfirmDialog(
												jContentPane,
												configurator
														.getResString("XSDCREATOR_DLG_ANPASSUNGEN_VERWERFEN"),
												configurator
														.getResString("XSDCREATOR_DLG_NEUE_PROFILIERUNG"),
												JOptionPane.YES_NO_OPTION);
							}
							if (n == JOptionPane.YES_OPTION) {
								try {
									setCurrFileKonfigToNull(xsdCreatorCtrl
											.createTreeModelForResponse());
								} catch (XsdCreatorCtrlException e1) {
									logger.error(
											"Fehler beim Anzeigen des Reponse-Schemas:",
											e1);
									JOptionPane
											.showMessageDialog(
													null,
													configurator
															.getResString("XSDCREATOR_DLG_FEHLER_TREEMODEL_RESPONSE"),
													configurator
															.getResString("XSDCREATOR_DLG_NEUE_PROFILIERUNG"),
													JOptionPane.ERROR_MESSAGE);
								}
							}
							XsdCreator.this.setCursor(new Cursor(
									Cursor.DEFAULT_CURSOR));
						}
					});
		}
		return jButtonNewForRespone;
	}

	/**
	 * This method initializes jButtonSaveKonfig
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonSaveKonfig() {
		if (jButtonSaveKonfig == null) {
			jButtonSaveKonfig = new JButton();
			jButtonSaveKonfig
					.addActionListener(new java.awt.event.ActionListener() {
						@Override
						public void actionPerformed(java.awt.event.ActionEvent e) {
							XsdCreator.this.setCursor(new Cursor(
									Cursor.WAIT_CURSOR));
							saveKonfig(false);
							XsdCreator.this.setCursor(new Cursor(
									Cursor.DEFAULT_CURSOR));
						}
					});
			jButtonSaveKonfig.setText(configurator
					.getResString("XSDCREATOR_BTN_SPEICHERN_KONFIG"));
			jButtonSaveKonfig.setSize(new Dimension(220, 26));
			jButtonSaveKonfig.setLocation(new Point(15, 126));
			jButtonSaveKonfig.setEnabled(false);
			jButtonSaveKonfig.setMnemonic(KeyEvent.VK_UNDEFINED);
		}
		return jButtonSaveKonfig;
	}

	/**
	 * This method initializes jButtonLoadKonfig
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonLoadKonfig() {
		if (jButtonLoadKonfig == null) {
			jButtonLoadKonfig = new JButton();
			jButtonLoadKonfig.setText(configurator
					.getResString("XSDCREATOR_BTN_KONFIGURATION_LADEN"));
			jButtonLoadKonfig.setSize(new Dimension(220, 26));
			jButtonLoadKonfig.setLocation(new Point(15, 90));
			jButtonLoadKonfig
					.addActionListener(new java.awt.event.ActionListener() {
						@Override
						public void actionPerformed(java.awt.event.ActionEvent e) {
							XsdCreator.this.setCursor(new Cursor(
									Cursor.WAIT_CURSOR));
							int n = JOptionPane.YES_OPTION;
							if ((treeMain != null)
									&& (treeMain.getModel() != null)) {
								n = JOptionPane
										.showConfirmDialog(
												jContentPane,
												configurator
														.getResString("XSDCREATOR_DLG_ANPASSUNGEN_VERWERFEN"),
												configurator
														.getResString("XSDCREATOR_DLG_KONFIGURATION_LADEN"),
												JOptionPane.YES_NO_OPTION);
							}
							if (n == JOptionPane.YES_OPTION) {
								try {
									JFileChooser fc = new JFileChooser();
									javax.swing.filechooser.FileFilter xmlFileFilter = new XmlXsdFileFilter(
											XmlXsdFileFilter.SUFFIX.XML);
									fc.setFileFilter(xmlFileFilter);
									fc.setAcceptAllFileFilterUsed(false);
									int returnVal = fc.showOpenDialog(null);
									if (returnVal == JFileChooser.APPROVE_OPTION) {
										File currFile = fc.getSelectedFile();
										setCurrFileKonfigAndRefreshView(currFile);
									}
								} catch (XsdCreatorCtrlException ex) {
									logger.error(
											"Fehler beim Laden der Profil-Konfiguration:",
											ex);
									JOptionPane
											.showMessageDialog(
													null,
													configurator
															.getResString("XSDCREATOR_DLG_FEHLER_LADEN_KONFIG"),
													configurator
															.getResString("XSDCREATOR_DLG_KONFIGURATION_LADEN"),
													JOptionPane.ERROR_MESSAGE);
								}
							}
							XsdCreator.this.setCursor(new Cursor(
									Cursor.DEFAULT_CURSOR));
						}
					});
		}
		return jButtonLoadKonfig;
	}

	/**
	 * This method initializes jButtonCreateSchema
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonCreateSchema() {
		if (jButtonCreateSchema == null) {
			jButtonCreateSchema = new JButton();
			jButtonCreateSchema
					.addActionListener(new java.awt.event.ActionListener() {
						@Override
						public void actionPerformed(java.awt.event.ActionEvent e) {
							XsdCreator.this.setCursor(new Cursor(
									Cursor.WAIT_CURSOR));
							int dlgErzeugeDoku = JOptionPane.NO_OPTION;
							String pathMainSchema = "";
							String selectedPath = "";
							// Schemadatei erzeugen und speichern
							try {
								Map<String, Document> docXsd = xsdCreatorCtrl
										.createSchemaProf();
								JFileChooser fc = new JFileChooser();
								fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
								int returnVal = fc.showSaveDialog(null);
								if (returnVal == JFileChooser.APPROVE_OPTION) {
									selectedPath = fc.getSelectedFile()
											.getPath();
									// Pr�fen, ob bereits eine Datei vorhanden
									// ist
									boolean dateiVorhanden = false;
									for (Map.Entry<String, Document> currEntry : docXsd
											.entrySet()) {
										String currFilePath = selectedPath
												+ "\\"
												+ configurator
														.getDateinameFuerSchema(
																xsdCreatorCtrl
																		.getBezeichnungKurzVerfahren(),
																currEntry
																		.getKey());
										File file = new File(currFilePath);
										if (currEntry.getKey().equals("xreq")
												|| currEntry.getKey().equals(
														"xres")) {
											pathMainSchema = currFilePath;
										}
										if (file.exists()) {
											dateiVorhanden = true;
										}
									}
									int n = JOptionPane.YES_OPTION;
									if (dateiVorhanden) {
										// Abfrage, wenn Datei bereits existiert
										n = JOptionPane.showConfirmDialog(
												jContentPane,
												configurator
														.getResString("XSDCREATOR_DLG_DATEI_BEREITS_VORHANDEN_FRAGE"),
												configurator
														.getResString("XSDCREATOR_DLG_DATEI_BEREITS_VORHANDEN"),
												JOptionPane.YES_NO_OPTION);
									}
									if (n == JOptionPane.YES_OPTION) {
										// Schema-Datei speichern
										xsdCreatorCtrl.saveXsdSchema(
												selectedPath, docXsd);
										// Frage, ob Dokumentation derstellt
										// werden soll
										dlgErzeugeDoku = JOptionPane
												.showConfirmDialog(
														jContentPane,
														configurator
																.getResString("XSDCREATOR_DLG_PDFDOKU_ERZEUGEN_FRAGE"),
														configurator
																.getResString("XSDCREATOR_DLG_PDFDOKU_ERZEUGEN"),
														JOptionPane.YES_NO_OPTION);
									}
								}
							} catch (XsdCreatorCtrlException e1) {
								logger.error(
										"Fehler beim Erstellen/Speichern des profilierten Schemas:",
										e1);
								JOptionPane.showMessageDialog(
										null,
										configurator
												.getResString("XSDCREATOR_DLG_FEHLER_NEUES_SCHEMA"),
										configurator
												.getResString("XSDCREATOR_DLG_NEUES_SCHEMA"),
										JOptionPane.ERROR_MESSAGE);
							}

							if (dlgErzeugeDoku == JOptionPane.YES_OPTION) {
								// PDF-Dokumentation erzeugen
								String currFilePath = selectedPath
										+ "\\"
										+ configurator
												.getDateinameFuerDoku(xsdCreatorCtrl
														.getBezeichnungKurzVerfahren());
								try {
									xsdCreatorCtrl.createPdfDoku(currFilePath,
											pathMainSchema);
								} catch (XsdCreatorCtrlException e1) {
									logger.error(
											"Fehler beim Erstellen der PDF-Dokumentation f�r das profilierte Schema:",
											e1);
									JOptionPane
											.showMessageDialog(
													null,
													configurator
															.getResString("XSDCREATOR_DLG_FEHLER_PDFDOKU_ERZEUGEN"),
													configurator
															.getResString("XSDCREATOR_DLG_PDFDOKU_ERZEUGEN"),
													JOptionPane.ERROR_MESSAGE);
								}
							}

							XsdCreator.this.setCursor(new Cursor(
									Cursor.DEFAULT_CURSOR));
						}
					});
			jButtonCreateSchema.setText(configurator
					.getResString("XSDCREATOR_BTN_NEUES_SCHEMA"));
			jButtonCreateSchema.setLocation(new Point(15, 208));
			jButtonCreateSchema.setSize(new Dimension(220, 26));
			jButtonCreateSchema.setEnabled(false);
			jButtonCreateSchema.setMnemonic(KeyEvent.VK_UNDEFINED);
		}
		return jButtonCreateSchema;
	}

	/**
	 * This method initializes jButtonSaveAsKonfig
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonSaveAsKonfig() {
		if (jButtonSaveAsKonfig == null) {
			jButtonSaveAsKonfig = new JButton();
			jButtonSaveAsKonfig
					.addActionListener(new java.awt.event.ActionListener() {
						@Override
						public void actionPerformed(java.awt.event.ActionEvent e) {
							XsdCreator.this.setCursor(new Cursor(
									Cursor.WAIT_CURSOR));
							saveKonfig(true);
							XsdCreator.this.setCursor(new Cursor(
									Cursor.DEFAULT_CURSOR));
						}
					});
			jButtonSaveAsKonfig.setText(configurator
					.getResString("XSDCREATOR_BTN_SPEICHERN_UNTER_KONFIG"));
			jButtonSaveAsKonfig.setLocation(new Point(15, 162));
			jButtonSaveAsKonfig.setSize(new Dimension(220, 26));
			jButtonSaveAsKonfig.setMnemonic(KeyEvent.VK_UNDEFINED);
			jButtonSaveAsKonfig.setEnabled(false);
		}
		return jButtonSaveAsKonfig;
	}

	private ProfilingTreeModel getProfilingTreeModelRef() {
		if ((treeAdditional != null) && (treeAdditional.getModel() != null)) {
			return (ProfilingTreeModel) treeAdditional.getModel();
		} else {
			return null;
		}
	}

	private static Configurator initConfigurator() throws ConfiguratorException {
		Properties propUser = Configurator
				.loadPropertiesFromFile(Configurator.PATH_PROPERTIES_USER);
		Properties propSystem = Configurator
				.loadPropertiesFromResource(Configurator.PATH_PROPERTIES_SYSTEM);
		Properties propNamespace = Configurator
				.loadPropertiesFromResource(Configurator.PATH_PROPERTIES_NAMESPACE);
		Properties propVersion = Configurator
				.loadPropertiesFromResource(Configurator.PATH_PROPERTIES_VERSION);
		Properties propAnmerkungen = Configurator
				.loadPropertiesFromFile(Configurator.PATH_PROPERTIES_ANMERKUNGEN);
		ResourceBundle resStrings = Configurator
				.loadResourceBundle(Configurator.NAME_RESBUNDLE_STRINGS);
		Configurator config = new Configurator(propUser, propSystem,
				propNamespace, propVersion, propAnmerkungen, resStrings,
				Configurator.PATH_TAILORING_SCHEMA);
		return config;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		try {
			// Configurator initalisieren
			Configurator configurator = initConfigurator();

			// Control-Klasse instanziieren
			XsdCreatorCtrl xsdCreatorCtrl = new XsdCreatorCtrlImpl(configurator);

			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				logger.error("Fehler beim Setzen von Look&Feel:", e);
			}

			XsdCreator thisClass = new XsdCreator(xsdCreatorCtrl, configurator);
			thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			thisClass.setLocationRelativeTo(null);
			thisClass.setVisible(true);

		} catch (ConfiguratorException configEx) {
			logger.error(configEx);
		}
	}

	/**
	 * This is the default constructor
	 */
	public XsdCreator() {
		this.xsdCreatorCtrl = null;
		this.configurator = null;
		initialize();
	}

	/**
	 * Initialisiert das Controller-Objekt und das Konfigurator-Objekt
	 * 
	 * @param xsdCreatorCtrl
	 *            Klasse mit allen notwendigen Funktionen f�r die Oberfl�che
	 * @param configurator
	 *            Konfiguratorobjekt mit Zugriff auf Properties und
	 *            Einstellungen
	 */
	public XsdCreator(XsdCreatorCtrl xsdCreatorCtrl, Configurator configurator) {
		this.xsdCreatorCtrl = xsdCreatorCtrl;
		this.configurator = configurator;
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						XsdCreator.class
								.getResource("/resource/de/drv/dsrv/xtt/gui/icons/app.png")));
		this.setContentPane(getJContentPane());
		this.setTitle("XSD-Creator");
		this.setMinimumSize(new Dimension(800, 600));
		this.setBounds(new Rectangle(0, 0, 700, 400));
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanelLeft(), BorderLayout.WEST);
			jContentPane.add(getJPanelMain(), BorderLayout.CENTER);

			JMenuBar menuBar = new JMenuBar();
			jContentPane.add(menuBar, BorderLayout.NORTH);

			JMenu mnDatei = new JMenu(
					configurator.getResString("XSDCREATOR_MENU_DATEI"));
			menuBar.add(mnDatei);

			JMenuItem mntmBeenden = new JMenuItem(
					configurator.getResString("XSDCREATOR_MENU_BEENDEN"));
			mntmBeenden.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int n = JOptionPane.YES_OPTION;
					if ((treeMain != null) && (treeMain.getModel() != null)) {
						n = JOptionPane.showConfirmDialog(
								jContentPane,
								configurator
										.getResString("XSDCREATOR_DLG_ANPASSUNGEN_VERWERFEN_BEENDEN"),
								configurator
										.getResString("XSDCREATOR_DLG_BEENDEN"),
								JOptionPane.YES_NO_OPTION);
					}
					if (n == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				}
			});
			mnDatei.add(mntmBeenden);

			JMenu mnHilfe = new JMenu(
					configurator.getResString("XSDCREATOR_MENU_HILFE"));
			menuBar.add(mnHilfe);

			JMenuItem mntmHilfe = new JMenuItem(
					configurator.getResString("XSDCREATOR_MENU_HILFE"));
			mntmHilfe.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (helpDialog == null) {
						helpDialog = new HelpDialog(XsdCreator.this,
								configurator);
						helpDialog.setAlwaysOnTop(false);
					}
					helpDialog.setVisible(true);
				}
			});
			mnHilfe.add(mntmHilfe);

			JMenuItem mntmber = new JMenuItem(
					configurator.getResString("XSDCREATOR_MENU_UEBER"));
			mntmber.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (about == null) {
						about = new About(XsdCreator.this, configurator);
						about.setAlwaysOnTop(true);
					}
					about.setVisible(true);
				}
			});

			JSeparator separator = new JSeparator();
			mnHilfe.add(separator);
			mnHilfe.add(mntmber);
		}
		return jContentPane;
	}

	private JTextField getFBezVerfahren() {
		if (fBezVerfahren == null) {
			fBezVerfahren = new JTextField();
			fBezVerfahren.setPreferredSize(new Dimension(6, 22));
			fBezVerfahren.setMargin(new Insets(0, 2, 0, 2));
			fBezVerfahren.setColumns(10);
		}
		return fBezVerfahren;
	}

	private JLabel getLblBezVerfahren() {
		if (lblBezVerfahren == null) {
			lblBezVerfahren = new JLabel();
			lblBezVerfahren.setText(configurator
					.getResString("XSDCREATOR_LABEL_BEZEICHNUNGVERFAHREN"));
			lblBezVerfahren.setFont(new Font("Tahoma", Font.BOLD, 11));
		}
		return lblBezVerfahren;
	}

	private JPanel getPanelTree() {
		if (panelTree == null) {
			panelTree = new JPanel();
			GridBagLayout gbl_panelTree = new GridBagLayout();
			gbl_panelTree.columnWidths = new int[] { 0, 0, 0 };
			gbl_panelTree.rowHeights = new int[] { 0, 0 };
			gbl_panelTree.columnWeights = new double[] { 1.0, 1.0,
					Double.MIN_VALUE };
			gbl_panelTree.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
			panelTree.setLayout(gbl_panelTree);
			GridBagConstraints gbc_scrollPaneMain = new GridBagConstraints();
			gbc_scrollPaneMain.insets = new Insets(0, 0, 0, 5);
			gbc_scrollPaneMain.fill = GridBagConstraints.BOTH;
			gbc_scrollPaneMain.gridx = 0;
			gbc_scrollPaneMain.gridy = 0;
			panelTree.add(getScrollPaneMain(), gbc_scrollPaneMain);
			GridBagConstraints gbc_scrollPaneAdditional = new GridBagConstraints();
			gbc_scrollPaneAdditional.fill = GridBagConstraints.BOTH;
			gbc_scrollPaneAdditional.gridx = 1;
			gbc_scrollPaneAdditional.gridy = 0;
			panelTree.add(getScrollPaneAdditional(), gbc_scrollPaneAdditional);
		}
		return panelTree;
	}

	private JScrollPane getScrollPaneAdditional() {
		if (scrollPaneAdditional == null) {
			scrollPaneAdditional = new JScrollPane();
			scrollPaneAdditional.setBackground(Color.WHITE);
		}
		return scrollPaneAdditional;
	}

	private JScrollPane getScrollPaneMain() {
		if (scrollPaneMain == null) {
			scrollPaneMain = new JScrollPane();
		}
		return scrollPaneMain;
	}

	private JTree getTreeMain(ProfilingTreeModel treeModel) {
		if (treeMain == null) {
			treeMain = new JTree();
			treeMain.setToggleClickCount(2);
			treeMain.setBackground(Color.white);
			treeMain.setShowsRootHandles(true);
			treeMain.addTreeSelectionListener(this);
			treeMain.getSelectionModel().setSelectionMode(
					TreeSelectionModel.SINGLE_TREE_SELECTION);
			treeMain.setModel(treeModel);
			treeMain.setCellRenderer(new CheckBoxNodeRenderer());
			treeMain.setCellEditor(new CheckBoxNodeEditor(treeMain));
			treeMain.setEditable(true);
		}
		treeMain.setModel(treeModel);

		// Baum expandieren?
		boolean expand = false;
		try {
			expand = Boolean
					.parseBoolean(configurator
							.getPropertyUser(Configurator.PropBezeichnungUser.BAUM_HAUPTELEMENTE_EXPANDIERT));
		} catch (Exception e) {

		}
		if (expand) {
			// Alle Knoten expandieren
			for (int i = 0; i < treeMain.getRowCount(); i++) {
				treeMain.expandRow(i);
			}
		}
		return treeMain;
	}

	private JTree getTreeAdditional(ProfilingTreeModel treeModel) {
		if (treeAdditional == null) {
			treeAdditional = new JTree();
			treeAdditional.setToggleClickCount(2);
			treeAdditional.setBackground(Color.white);
			treeAdditional.setShowsRootHandles(false);
			treeAdditional.addTreeSelectionListener(this);
			treeAdditional.getSelectionModel().setSelectionMode(
					TreeSelectionModel.SINGLE_TREE_SELECTION);
			treeAdditional.setModel(treeModel);
			treeAdditional.setCellRenderer(new CheckBoxNodeRenderer());
			treeAdditional
					.setCellEditor(new CheckBoxNodeEditor(treeAdditional));
			treeAdditional.setEditable(true);
		}
		treeAdditional.setModel(treeModel);

		// Baum expandieren?
		boolean expand = false;
		try {
			expand = Boolean
					.parseBoolean(configurator
							.getPropertyUser(Configurator.PropBezeichnungUser.BAUM_REFRENZIERTE_ELEMENTE_EXPANDIERT));
		} catch (Exception e) {

		}
		if (expand) {
			// Alle Knoten expandieren
			for (int i = 0; i < treeAdditional.getRowCount(); i++) {
				treeAdditional.expandRow(i);
			}
		}
		return treeAdditional;
	}

	private JButton getBtnOkAnmerkungenVerwendung() {
		if (btnOkAnmerkungenVerwendung == null) {
			btnOkAnmerkungenVerwendung = new JButton("");
			btnOkAnmerkungenVerwendung.setPreferredSize(new Dimension(40, 40));
			btnOkAnmerkungenVerwendung.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Anmerkungen zur Verwendung speichern
					if ((currNode != null)
							&& (currNode.getParent() != null)
							&& !currNode.getParent().isRootReferencedElements(
									configurator)) {
						configurator.setAnmerkungVerwendung(currNode
								.getSchemaElement(), currNode.getParent()
								.getSchemaElement(), fAnmerkVerwendung
								.getText());
						fAnmerkVerwendung.setCaretPosition(0);
					}
				}
			});
			btnOkAnmerkungenVerwendung
					.setIcon(new ImageIcon(
							XsdCreator.class
									.getResource("/resource/de/drv/dsrv/xtt/gui/icons/accept.png")));
			btnOkAnmerkungenVerwendung.setEnabled(false);
		}
		return btnOkAnmerkungenVerwendung;
	}

	private JPanel getPanel_3() {
		if (panel_3 == null) {
			panel_3 = new JPanel();
			panel_3.setMinimumSize(new Dimension(0, 0));
			panel_3.setPreferredSize(new Dimension(0, 0));
			panel_3.setBackground(new Color(245, 245, 245));
			GridBagLayout gbl_panel_3 = new GridBagLayout();
			gbl_panel_3.columnWidths = new int[] { 0, 0, 0 };
			gbl_panel_3.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
			gbl_panel_3.columnWeights = new double[] { 0.0, 1.0, 0.0 };
			gbl_panel_3.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.5,
					0.5 };
			panel_3.setLayout(gbl_panel_3);

			JLabel lblSelektiertesElement = new JLabel();
			lblSelektiertesElement.setMinimumSize(new Dimension(150, 20));
			lblSelektiertesElement.setVerticalAlignment(SwingConstants.TOP);
			GridBagConstraints gbc_lblSelektiertesElement = new GridBagConstraints();
			gbc_lblSelektiertesElement.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblSelektiertesElement.insets = new Insets(5, 3, 5, 5);
			gbc_lblSelektiertesElement.gridx = 0;
			gbc_lblSelektiertesElement.gridy = 0;
			panel_3.add(lblSelektiertesElement, gbc_lblSelektiertesElement);
			lblSelektiertesElement.setText(configurator
					.getResString("XSDCREATOR_LABEL_ELEMENT_SELEKTIERT"));
			lblSelektiertesElement.setFont(new Font("Tahoma", Font.BOLD, 11));

			jLabelSelectedElement = new JLabel();
			jLabelSelectedElement.setVerticalAlignment(SwingConstants.TOP);
			GridBagConstraints gbc_jLabelSelectedElement = new GridBagConstraints();
			gbc_jLabelSelectedElement.anchor = GridBagConstraints.NORTHWEST;
			gbc_jLabelSelectedElement.insets = new Insets(5, 0, 5, 5);
			gbc_jLabelSelectedElement.gridx = 1;
			gbc_jLabelSelectedElement.gridy = 0;
			panel_3.add(jLabelSelectedElement, gbc_jLabelSelectedElement);

			JLabel lblEigenschaften = new JLabel();
			lblEigenschaften.setMinimumSize(new Dimension(150, 20));
			GridBagConstraints gbc_lblEigenschaften = new GridBagConstraints();
			gbc_lblEigenschaften.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblEigenschaften.insets = new Insets(0, 3, 5, 5);
			gbc_lblEigenschaften.gridx = 0;
			gbc_lblEigenschaften.gridy = 1;
			panel_3.add(lblEigenschaften, gbc_lblEigenschaften);
			lblEigenschaften.setText(configurator
					.getResString("XSDCREATOR_LABEL_ELEMENT_EIGENSCHAFTEN"));
			lblEigenschaften.setFont(new Font("Tahoma", Font.BOLD, 11));

			jLabelElementEigenschaften = new JLabel();
			jLabelElementEigenschaften.setVerticalAlignment(SwingConstants.TOP);
			GridBagConstraints gbc_jLabelElementEigenschaften = new GridBagConstraints();
			gbc_jLabelElementEigenschaften.anchor = GridBagConstraints.NORTHWEST;
			gbc_jLabelElementEigenschaften.insets = new Insets(0, 0, 5, 5);
			gbc_jLabelElementEigenschaften.gridx = 1;
			gbc_jLabelElementEigenschaften.gridy = 1;
			panel_3.add(jLabelElementEigenschaften,
					gbc_jLabelElementEigenschaften);

			JLabel lblMinOccurs = new JLabel();
			lblMinOccurs.setMinimumSize(new Dimension(150, 20));
			lblMinOccurs.setVerticalAlignment(SwingConstants.TOP);
			GridBagConstraints gbc_lblMinOccurs = new GridBagConstraints();
			gbc_lblMinOccurs.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblMinOccurs.insets = new Insets(0, 3, 5, 5);
			gbc_lblMinOccurs.gridx = 0;
			gbc_lblMinOccurs.gridy = 2;
			panel_3.add(lblMinOccurs, gbc_lblMinOccurs);
			lblMinOccurs.setText(configurator
					.getResString("XSDCREATOR_LABEL_MINOCCURS"));
			lblMinOccurs.setFont(new Font("Tahoma", Font.BOLD, 11));
			GridBagConstraints gbc_spinnerMinOccurs = new GridBagConstraints();
			gbc_spinnerMinOccurs.anchor = GridBagConstraints.NORTHWEST;
			gbc_spinnerMinOccurs.insets = new Insets(0, 0, 5, 5);
			gbc_spinnerMinOccurs.gridx = 1;
			gbc_spinnerMinOccurs.gridy = 2;
			panel_3.add(getSpinnerMinOccurs(), gbc_spinnerMinOccurs);

			JLabel lblMaxOccurs = new JLabel();
			lblMaxOccurs.setText(configurator
					.getResString("XSDCREATOR_LABEL_MAXOCCURS"));
			lblMaxOccurs.setMinimumSize(new Dimension(150, 20));
			lblMaxOccurs.setVerticalAlignment(SwingConstants.TOP);
			GridBagConstraints gbc_lblMaxOccurs = new GridBagConstraints();
			gbc_lblMaxOccurs.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblMaxOccurs.insets = new Insets(0, 3, 5, 5);
			gbc_lblMaxOccurs.gridx = 0;
			gbc_lblMaxOccurs.gridy = 3;
			panel_3.add(lblMaxOccurs, gbc_lblMaxOccurs);
			lblMaxOccurs.setFont(new Font("Tahoma", Font.BOLD, 11));

			JPanel panel = new JPanel();
			panel.setBackground(new Color(245, 245, 245));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTHWEST;
			gbc_panel.insets = new Insets(0, 0, 5, 5);
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 1;
			gbc_panel.gridy = 3;
			panel_3.add(panel, gbc_panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[] { 100, 37, 0 };
			gbl_panel.rowHeights = new int[] { 21, 0 };
			gbl_panel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
			gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			panel.setLayout(gbl_panel);

			spinnerMaxOccurs = new JSpinner();
			spinnerMaxOccurs.setPreferredSize(new Dimension(100, 20));
			spinnerMaxOccurs.setEnabled(false);
			GridBagConstraints gbc_spinnerMaxOccurs = new GridBagConstraints();
			gbc_spinnerMaxOccurs.anchor = GridBagConstraints.NORTHWEST;
			gbc_spinnerMaxOccurs.insets = new Insets(0, 0, 0, 5);
			gbc_spinnerMaxOccurs.gridx = 0;
			gbc_spinnerMaxOccurs.gridy = 0;
			panel.add(spinnerMaxOccurs, gbc_spinnerMaxOccurs);

			btnMax = new JButton("max");
			btnMax.setPreferredSize(new Dimension(40, 21));
			btnMax.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if ((currNode != null) && spinnerMaxOccurs.isEnabled()) {
						spinnerMaxOccurs.setValue(currNode
								.getMaxOccursDefault());
					}
				}
			});
			btnMax.setMargin(new Insets(0, 5, 0, 5));
			GridBagConstraints gbc_btnMax = new GridBagConstraints();
			gbc_btnMax.anchor = GridBagConstraints.NORTHWEST;
			gbc_btnMax.gridx = 1;
			gbc_btnMax.gridy = 0;
			panel.add(btnMax, gbc_btnMax);
			fAnmerkAllgemein = new JTextPane();
			fAnmerkAllgemein.setPreferredSize(new Dimension(0, 0));
			fAnmerkAllgemein.setEnabled(false);
			fAnmerkAllgemein.setMinimumSize(new Dimension(0, 0));
			fAnmerkAllgemein.setBackground(new Color(255, 255, 255));

			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setPreferredSize(new Dimension(0, 0));
			scrollPane.setMinimumSize(new Dimension(0, 0));
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
			gbc_scrollPane.weightx = 1.0;
			gbc_scrollPane.weighty = 1.0;
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 1;
			gbc_scrollPane.gridy = 4;
			panel_3.add(scrollPane, gbc_scrollPane);
			scrollPane.setViewportView(fAnmerkAllgemein);

			JLabel lblAnmerkAllg = new JLabel();
			lblAnmerkAllg.setVerticalAlignment(SwingConstants.TOP);
			lblAnmerkAllg.setPreferredSize(new Dimension(150, 30));
			GridBagConstraints gbc_lblAnmerkAllg = new GridBagConstraints();
			gbc_lblAnmerkAllg.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblAnmerkAllg.insets = new Insets(0, 3, 0, 5);
			gbc_lblAnmerkAllg.gridx = 0;
			gbc_lblAnmerkAllg.gridy = 4;
			panel_3.add(lblAnmerkAllg, gbc_lblAnmerkAllg);
			lblAnmerkAllg.setText(configurator
					.getResString("XSDCREATOR_LABEL_ANMERKUNGENALLGEMEIN"));
			lblAnmerkAllg.setFont(new Font("Tahoma", Font.BOLD, 11));

			btnOkAnmerkungenAllg = new JButton("");
			btnOkAnmerkungenAllg.setPreferredSize(new Dimension(40, 40));
			GridBagConstraints gbc_btnOkAnmerkungenAllg = new GridBagConstraints();
			gbc_btnOkAnmerkungenAllg.anchor = GridBagConstraints.NORTHEAST;
			gbc_btnOkAnmerkungenAllg.gridx = 2;
			gbc_btnOkAnmerkungenAllg.gridy = 4;
			panel_3.add(btnOkAnmerkungenAllg, gbc_btnOkAnmerkungenAllg);
			btnOkAnmerkungenAllg
					.setIcon(new ImageIcon(
							XsdCreator.class
									.getResource("/resource/de/drv/dsrv/xtt/gui/icons/accept.png")));
			btnOkAnmerkungenAllg.setEnabled(false);
			btnOkAnmerkungenAllg.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Anmerkungen allgemein speichern
					if (currNode != null) {
						configurator.setAnmerkungAllgemein(
								currNode.getSchemaElement(),
								fAnmerkAllgemein.getText());
						fAnmerkAllgemein.setCaretPosition(0);
					}
				}
			});
			GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
			gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
			gbc_scrollPane_1.weightx = 1.0;
			gbc_scrollPane_1.weighty = 1.0;
			gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
			gbc_scrollPane_1.gridx = 1;
			gbc_scrollPane_1.gridy = 5;
			panel_3.add(getScrollPane_1_1(), gbc_scrollPane_1);

			JLabel lblAnmerkVerwendung = new JLabel();
			lblAnmerkVerwendung.setVerticalAlignment(SwingConstants.TOP);
			lblAnmerkVerwendung.setPreferredSize(new Dimension(150, 30));
			GridBagConstraints gbc_lblAnmerkVerwendung = new GridBagConstraints();
			gbc_lblAnmerkVerwendung.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblAnmerkVerwendung.insets = new Insets(0, 3, 0, 5);
			gbc_lblAnmerkVerwendung.gridx = 0;
			gbc_lblAnmerkVerwendung.gridy = 5;
			panel_3.add(lblAnmerkVerwendung, gbc_lblAnmerkVerwendung);
			lblAnmerkVerwendung.setText(configurator
					.getResString("XSDCREATOR_LABEL_ANMERKUNGENVERWENDUNG"));
			lblAnmerkVerwendung.setFont(new Font("Tahoma", Font.BOLD, 11));
			GridBagConstraints gbc_btnOkAnmerkungenVerwendung = new GridBagConstraints();
			gbc_btnOkAnmerkungenVerwendung.anchor = GridBagConstraints.NORTHEAST;
			gbc_btnOkAnmerkungenVerwendung.gridx = 2;
			gbc_btnOkAnmerkungenVerwendung.gridy = 5;
			panel_3.add(getBtnOkAnmerkungenVerwendung(),
					gbc_btnOkAnmerkungenVerwendung);
		}
		return panel_3;
	}

	private JScrollPane getScrollPane_1_1() {
		if (scrollPane_1 == null) {
			scrollPane_1 = new JScrollPane();
			scrollPane_1.setPreferredSize(new Dimension(0, 0));
			scrollPane_1.setMinimumSize(new Dimension(0, 0));
			scrollPane_1.setViewportView(getFAnmerkVerwendung());
		}
		return scrollPane_1;
	}

	private JTextPane getFAnmerkVerwendung() {
		if (fAnmerkVerwendung == null) {
			fAnmerkVerwendung = new JTextPane();
			fAnmerkVerwendung.setPreferredSize(new Dimension(0, 0));
			fAnmerkVerwendung.setMinimumSize(new Dimension(0, 0));
			fAnmerkVerwendung.setEnabled(false);
			fAnmerkVerwendung.setBackground(Color.WHITE);
		}
		return fAnmerkVerwendung;
	}

	private JSpinner getSpinnerMinOccurs() {
		if (spinnerMinOccurs == null) {
			spinnerMinOccurs = new JSpinner();
			spinnerMinOccurs.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if (spinnerMinOccurs.isEnabled()) {
						// Wert �bernehmen
						int currMinValue = Integer.parseInt(spinnerMinOccurs
								.getValue().toString());
						int currMaxValue = Integer.parseInt(spinnerMaxOccurs
								.getValue().toString());
						currNode.setMinOccurUser(currMinValue);
						// Minimum f. maxOccurs aktualieren
						// aktuellen Wert setzen
						spinnerMaxOccurs.setModel(new SpinnerNumberModel(
								currMaxValue, currMinValue, currNode
										.getMaxOccursDefault(), 1));
						// Tree aktualisieren (evtl. wird * gesetzt)
						panelTree.setVisible(false);
						panelTree.setVisible(true);
					}
				}
			});
			spinnerMinOccurs.setPreferredSize(new Dimension(100, 20));
			spinnerMinOccurs.setModel(new SpinnerNumberModel(new Integer(0),
					new Integer(0), null, new Integer(1)));
			spinnerMinOccurs.setEnabled(false);
		}
		return spinnerMinOccurs;
	}

	private void saveKonfig(boolean saveAs) {
		try {
			if (xsdCreatorCtrl
					.validateBezeichnungKurzVerfahren(fBezKurzVerfahren
							.getText())) {
				// XML-Konfiguration erstellen
				xsdCreatorCtrl.createXmlProf(
						(ProfilingTreeModel) treeMain.getModel(),
						getProfilingTreeModelRef(),
						xsdCreatorCtrl.getTargetNamespace(),
						fBezKurzVerfahren.getText(), fBezVerfahren.getText());
				if (saveAs) {
					JFileChooser fc = new JFileChooser();
					javax.swing.filechooser.FileFilter xmlFileFilter = new XmlXsdFileFilter(
							XmlXsdFileFilter.SUFFIX.XML);
					fc.setFileFilter(xmlFileFilter);
					fc.setAcceptAllFileFilterUsed(false);
					fc.setSelectedFile(new File(xsdCreatorCtrl
							.getBezeichnungKurzVerfahren() + ".xml"));
					int returnVal = fc.showSaveDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File currFile = fc.getSelectedFile();
						int n = JOptionPane.YES_OPTION;
						if (currFile.exists()) {
							// Abfrage, wenn Datei bereits existiert
							n = JOptionPane
									.showConfirmDialog(
											jContentPane,
											configurator
													.getResString("XSDCREATOR_DLG_DATEI_BEREITS_VORHANDEN_FRAGE"),
											configurator
													.getResString("XSDCREATOR_DLG_DATEI_BEREITS_VORHANDEN"),
											JOptionPane.YES_NO_OPTION);
						}
						if (n == JOptionPane.YES_OPTION) {
							// XML-Datei speichern
							xsdCreatorCtrl.saveXmlConfig(currFile.getPath());
							refreshTextAndButtons();
						}
					}
				} else {
					// XML-Datei speichern
					xsdCreatorCtrl.saveXmlConfig(xsdCreatorCtrl
							.getPathXmlConfig());
				}
			} else {
				JOptionPane
						.showMessageDialog(
								null,
								configurator
										.getResString("XSDCREATOR_DLG_BEZEICHNUNG_VERFAHREN_FEHLER"),
								configurator
										.getResString("XSDCREATOR_DLG_SPEICHERN_KONFIG"),
								JOptionPane.ERROR_MESSAGE);
			}
		} catch (XsdCreatorCtrlException e1) {
			logger.error(
					"Fehler beim Erstellen/Speichern der Profil-Konfiguration:",
					e1);
			JOptionPane
					.showMessageDialog(
							null,
							configurator
									.getResString("XSDCREATOR_DLG_FEHLER_SPEICHERN_PROFILKONFIGURATION"),
							configurator
									.getResString("XSDCREATOR_DLG_SPEICHERN_KONFIG"),
							JOptionPane.ERROR_MESSAGE);
		}
	}
}
