package de.extra.xtt.util;

import java.io.File;
import java.util.Map;

import org.w3c.dom.Document;

import de.extra.xtt.gui.model.ProfilingTreeModel;

/**
 * Controller-Klasse für die Anwendung XSD-Creator. Stellt alle notwendigen Funktionen für die Oberfläche breit.
 * 
 * @author Beier
 * 
 */
public interface XsdCreatorCtrl {

	/**
	 * Erzeugt das TreeModel für das Request-Schema
	 * 
	 * @return TreeModel mit allen Elementen des Request-Schemas
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausführen der Methode ein beliebiger Fehler auftritt
	 */
	public abstract ProfilingTreeModel createTreeModelForRequest() throws XsdCreatorCtrlException;

	/**
	 * Erzeugt das TreeModel für das Response-Schema
	 * 
	 * @return TreeModel mit alle Elementen des Response-Schemas
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausführen der Methode ein beliebiger Fehler auftritt
	 */
	public abstract ProfilingTreeModel createTreeModelForResponse() throws XsdCreatorCtrlException;

	/**
	 * Erzeugt das TreeModel für die aktuell geladene Konfiguration
	 * 
	 * @return TreeModel für das aktuell geladene Schema
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausführen der Methode ein beliebiger Fehler auftritt
	 */
	public abstract ProfilingTreeModel createTreeModelForCurrentConfig() throws XsdCreatorCtrlException;

	/**
	 * Lädt die Profilkonfiguration aus der übergebenen Datei inkl. passendem Schema
	 * 
	 * @param fileConfig
	 *            Datei mit einer XML-Profilkonfiguration
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausführen der Methode ein beliebiger Fehler auftritt
	 */
	public abstract void setXmlFileConfig(File fileConfig) throws XsdCreatorCtrlException;

	/**
	 * Erzeugt eine PDF-Dokumentation für das aktuelle profilierte Schema
	 * 
	 * @param pathFile
	 *            Pfad für die zu erstellende PDF-Datei
	 * @param filePathSchema
	 *            Pfad der Schemadatei
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausführen der Methode ein beliebiger Fehler auftritt
	 */
	public abstract void createPdfDoku(String pathFile, String filePathSchema) throws XsdCreatorCtrlException;

	/**
	 * Liefert den aktuellen TargetNamespace aus der Profilkonfiguration
	 * 
	 * @return TargetNamespace
	 */
	public abstract String getTargetNamespace();

	/**
	 * Gibt an, ob aktuell eine Profilkonfiguration geladen ist
	 * 
	 * @return <code>true</code>, wenn Profilkonfiguration geladen; sonst <code>false</code>
	 */
	public abstract boolean isDocXmlLoaded();

	/**
	 * Für die übergebenen TreeModels wird eine Profilkonfiguration inkl. TargetNamespace und und Kurzbezeichnung für
	 * das Verfahren erzeugt.
	 * 
	 * @param treeModelMain
	 *            TreeModel mit den Hauptelementen
	 * @param treeModelAdd
	 *            TreeModel mit dne referenzierten Elementen
	 * @param targetNamespace
	 *            TargetNamespace für das profilierte Schema
	 * @param bezVerfahrenKurz
	 *            Kurzbezeichnung des Verfahrens; wird für die Dateinamen verwendet
	 * @param bezVerfahren
	 *            Bbezeichnung des Verfahrens
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausführen der Methode ein beliebiger Fehler auftritt
	 */
	public abstract void createXmlProf(ProfilingTreeModel treeModelMain, ProfilingTreeModel treeModelAdd,
			String targetNamespace, String bezVerfahrenKurz, String bezVerfahren) throws XsdCreatorCtrlException;

	/**
	 * Erzeugt das profilierte Schema für die aktuelle Profilkonfiguration; Ergebnis können ein oder mehrere
	 * Schemadateien sein.
	 * 
	 * @return Map mit dem Namespace-Präfix als Key und dem dazugehörigen Schemadokument als Value
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausführen der Methode ein beliebiger Fehler auftritt
	 */
	public abstract Map<String, Document> createSchemaProf() throws XsdCreatorCtrlException;

	/**
	 * Liefert den Pfad zur Datei des Quellschemas für die aktuelle Konfiguration.
	 * 
	 * @return Dateipfad vom Quellschema
	 */
	public abstract String getPathQuellSchema();

	/**
	 * Liefert den Pfad zur Datei der aktuell geladenen Profilkonfiguration.
	 * 
	 * @return Pfad der Profilkonfiguration
	 */
	public abstract String getPathXmlConfig();

	/**
	 * Speichert die aktuelle Profilkonfiguration.
	 * 
	 * @param path
	 *            Pfad und Name für die neue Datei
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausführen der Methode ein beliebiger Fehler auftritt
	 */
	public abstract void saveXmlConfig(String path) throws XsdCreatorCtrlException;

	/**
	 * Speichert die übergebenen Schema-Dateien im angegebenen Verzeichnis. Die Dateinamen werden automatisch aus dem
	 * Namespace-Präfix und der Kurzbezeichnung des Verfahrens aus der Konfiguration gebildet.
	 * 
	 * @param path
	 *            Verzeichnis, in dem die Schemadateien gespeichert werden
	 * @param docXsd
	 *            Map mit dem Namespace-Präfix als Key und dem dazugehörigen Schemadokument als Value
	 * @throws XsdCreatorCtrlException
	 *             Ausnahme wird erzeugt, falls beim Ausführen der Methode ein beliebiger Fehler auftritt
	 */
	public abstract void saveXsdSchema(String path, Map<String, Document> docXsd) throws XsdCreatorCtrlException;

	/**
	 * Liefert die Kurzbezeichnung des Verfahrens aus der aktuellen Profilkonfiguration.
	 * 
	 * @return Kurzbezeichnung des Verfahrens
	 */
	public abstract String getBezeichnungKurzVerfahren();

	/**
	 * Liefert die Bezeichnung des Verfahrens aus der aktuellen Profilkonfiguration.
	 * 
	 * @return Bezeichnung des Verfahrens
	 */
	public abstract String getBezeichnungVerfahren();

	/**
	 * Prüft den übergebenen String, ob es ein gültiger Dateiname ist (nur Buchstaben, Ziffern und '_')
	 * 
	 * @param bezKurzVerfahren
	 *            Zu prüfender String
	 * @return <code>true</code>, falls Bezeichnung gültig ist, sonst <code>false</code>
	 */
	public abstract boolean validateBezeichnungKurzVerfahren(String bezKurzVerfahren);

}