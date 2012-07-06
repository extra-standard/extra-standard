package de.extra.xtt.util.tools;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Document;

/**
 * Klasse zum Auflösen von Namespace-URL bzw. -Präfix für ein DOM-Dokument
 * 
 * @author Beier
 *
 */
public class UniversalNamespaceResolver implements NamespaceContext {

    private final Document sourceDocument;

    /**
     * Das Quell-Dokument wird für die Suche nach Namespaces
     * gespeichert.
     * 
     * @param document
     *            Quell-Dokument
     */
    public UniversalNamespaceResolver(Document document) {
        sourceDocument = document;
    }

    /**
     * Die Namespace-Suche wird an das Quell-Dokument delegiert.
     * 
     * @param prefix
     *            zu suchender Namespace-Präfix
     * @return uri
     */
    public String getNamespaceURI(String prefix) {
        if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
            return sourceDocument.lookupNamespaceURI(null);
        } else {
            return sourceDocument.lookupNamespaceURI(prefix);
        }
    }

    /**
     * {@inheritdoc}
     */
    public String getPrefix(String namespaceURI) {
        return sourceDocument.lookupPrefix(namespaceURI);
    }

	/**
	 * {@inheritdoc}
	 */
	public Iterator<String> getPrefixes(String namespaceURI) {
        // nicht implementiert
        return null;
    }

}
