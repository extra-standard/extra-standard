package de.extra.xtt.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.log4j.Logger;

import de.extra.xtt.util.tools.Configurator;

/**
 * Diese Klasse implementiert den Hilfe-Dialog für die Anwendung.
 * Hauptbestandteil ist eine html-Seite, die den Hilfetext enthält.
 * 
 * @author Beier
 *
 */
public class HelpDialog extends JDialog  implements HyperlinkListener {

	private static Logger logger = Logger.getLogger(HelpDialog.class);
	private static final long serialVersionUID = 1L;
	
	private Configurator configurator ;
	private JPanel jContentPane = null;
	private JEditorPane editorPane;

	/**
	 * Konstruktor zur Initialisierung des Hilfe-Dialogs
	 * 
	 * @param owner
	 *            Frame, das Besitzer dieses Dialogs ist
	 * @param configurator
	 *            Configurator-Objekt mit dem Zugriff auf die Einstellungen
	 */
	public HelpDialog(Frame owner, Configurator configurator) {
		super(owner);
		this.configurator = configurator;
		setTitle("Hilfe zu XSD-Creator");
		initialize();
	}

	private void initialize() {
		this.setSize(700, 400);
		this.setContentPane(getJContentPane());
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setBorder(new CompoundBorder());
			jContentPane.setLayout(new BorderLayout());

			JPanel panel = new JPanel();
			panel.setMinimumSize(new Dimension(10, 50));
			panel.setSize(new Dimension(0, 50));
			jContentPane.add(panel, BorderLayout.NORTH);
			panel.setLayout(new BorderLayout(0, 0));

			JLabel label = new JLabel("");
			label.setIcon(new ImageIcon(HelpDialog.class
					.getResource("/resource/de/drv/dsrv/xtt/gui/img/awv_extra_small.png")));
			panel.add(label, BorderLayout.EAST);

			JLabel lblHilfeZuXsdcreator = new JLabel(configurator.getResString("HELPDIALOG_TITEL"));
			lblHilfeZuXsdcreator.setBorder(new EmptyBorder(0, 10, 0, 0));
			lblHilfeZuXsdcreator.setFont(new Font("Tahoma", Font.BOLD, 18));
			panel.add(lblHilfeZuXsdcreator, BorderLayout.WEST);

			JPanel panel_1 = new JPanel();
			jContentPane.add(panel_1, BorderLayout.SOUTH);
			panel_1.setLayout(new BorderLayout(0, 0));

			JButton btnSchlieen = new JButton(configurator.getResString("HELPDIALOG_BTN_BEENDEN"));
			btnSchlieen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
			btnSchlieen.setHorizontalAlignment(SwingConstants.RIGHT);
			panel_1.add(btnSchlieen, BorderLayout.EAST);

			JScrollPane scrollPane = new JScrollPane();
			jContentPane.add(scrollPane, BorderLayout.CENTER);

			editorPane = new JEditorPane();
			try {
				java.net.URL helpURL = HelpDialog.class.getResource("/resource/de/drv/dsrv/xtt/gui/html/Help.html");
				if (helpURL != null) {
					editorPane.setPage(helpURL);
					editorPane.addHyperlinkListener(this);
				}
			} catch (IOException e) {
				logger.error("Fehler beim Anzeigen der Hilfe-HTML-Seite: ", e);

			}
			editorPane.setEditable(false);
			scrollPane.setViewportView(editorPane);
		}
		return jContentPane;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				editorPane.setPage(e.getURL());
			} catch (IOException ex) {
				// Ignoriere Exception
				;
			}
		}
		
	}

}
