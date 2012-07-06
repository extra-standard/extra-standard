package de.extra.xtt.gui;

import java.io.File;
import java.io.FileFilter;

/**
 * FileFilter für *.xml und *.xsd-Dateien
 * 
 * @author Beier
 * 
 */
public class XmlXsdFileFilter extends javax.swing.filechooser.FileFilter implements FileFilter {

	private final SUFFIX suffix;

	/**
	 * Konstruktor mit der Angabe der Endung für den Filter
	 * 
	 * @param suffix
	 *            Endung aus den vorgegebenen Typen
	 */
	public XmlXsdFileFilter(SUFFIX suffix) {
		this.suffix = suffix;
	}

	/**
	 * {@inheritdoc}
	 */
	public boolean accept(File f) {
		return (f.isDirectory() || f.getName().toLowerCase().endsWith(suffixStr(suffix)));
	}

	/**
	 * {@inheritdoc}
	 */
	public String getDescription() {
		String rvVal = "";
		switch (suffix) {
		case XML:
			rvVal = "XML-Dateien";
			break;

		case XSD:
			rvVal = "XSD-Dateien";
			break;
		}
		return rvVal;
	}

	/**
	 * Mögliche Dateitypen für den Filterprozess
	 * 
	 * @author Beier
	 * 
	 */
	public static enum SUFFIX {
		XML, XSD
	}

	private String suffixStr(SUFFIX suff) {
		String rvVal = "";
		switch (suff) {
		case XML:
			rvVal = ".xml";
			break;

		case XSD:
			rvVal = ".xsd";
			break;
		}

		return rvVal;
	}

}
