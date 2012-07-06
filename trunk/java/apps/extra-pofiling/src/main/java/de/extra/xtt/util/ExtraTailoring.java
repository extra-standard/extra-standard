package de.extra.xtt.util;

import java.util.Map;

import org.w3c.dom.Document;

import com.sun.xml.xsom.XSSchemaSet;

import de.extra.xtt.gui.model.ProfilingTreeNode;
import de.extra.xtt.util.tools.Configurator;

/**
 * Schnittstelle für die Erzeugung eines verfahrensspezifischen Extra-Schemas
 * 
 * @author Mueller
 * 
 */
public interface ExtraTailoring {

	/**
	 * Methode zum erzeugen eines profilierten Extra-Schemas inkl. Hauptschema und verknüpfte Schemata
	 * 
	 * @param ssQuellSchema
	 *            Das vollständige Quellschema, z.B. eXTra Request 1.1
	 * @param pofilXml
	 *            Die Profilkonfiguration im XML-Format mit der Beschreibung der benötigten Element
	 * @param bezeichnungVerfahren
	 *            Kurzbezeichnung des zu profilierenden Verfahren (wird für die Dateinamen der Schemadateien verwendet)
	 * @return Die profilierten Schemadateien im DOM-Format
	 * @throws ExtraTailoringException
	 *             Falls ein Fehler auftritt, wird diese Ausnahme erzeugt
	 */
	public Map<String, Document> erzeugeProfiliertesExtraSchema(XSSchemaSet ssQuellSchema, Document pofilXml,
			String bezeichnungVerfahren) throws ExtraTailoringException;

	/**
	 * Methode zum Erzeugen einer Profil-Konfiguration
	 * 
	 * @param schemaType
	 *            Angabe, welches Schema profiliert werden soll (Basisschema)
	 * @param rootNode
	 *            Wurzelelement aus dem Haupt-Profilbaum, der die selektierten Knoten enthält
	 * @param rootNodeRef
	 *            Wurzelelement aus dem Profilbaum mit den referenzierten Elementen
	 * @param targetNamespace
	 *            TargetNamespace für das Schema; wird in der Konfiguration gespeichert
	 * @param bezVerfahrenKurz
	 *            Kurzbezeichnung des zu profilierenden Verfahrens; wird in der Konfiguration gespeichert
	 * @param bezVerfahren
	 *            Bezeichnung des zu profilierenden Verfahrens; wird in der Konfiguration gespeichert
	 * @return Profilkonfiguration im DOM-Format
	 * @throws ExtraTailoringException
	 *             Falls ein Fehler auftritt, wird diese Ausnahme erzeugt
	 */
	public Document erzeugeProfilkonfiguration(Configurator.SchemaType schemaType, ProfilingTreeNode rootNode,
			ProfilingTreeNode rootNodeRef, String targetNamespace, String bezVerfahrenKurz, String bezVerfahren)
			throws ExtraTailoringException;
}
