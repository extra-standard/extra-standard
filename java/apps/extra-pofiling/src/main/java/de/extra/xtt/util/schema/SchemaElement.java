package de.extra.xtt.util.schema;

/**
 * Objekt zum Bündeln der Informationen zu einem Schema-Element: Name des Elements, Namespace-Präfix und Namespace-URL
 * 
 * @author Beier
 * 
 */
public class SchemaElement implements Comparable<SchemaElement> {

	private final String name;
	private final String nsUrl;
	private final String nsPrefix;

	/**
	 * Konstruktor mit Initialisierungsparametern
	 * 
	 * @param name
	 *            Name des Schema-Elements ohne Namespace-Präfix
	 * @param nsUrl
	 *            Namespace-URL des Schema-Elements
	 * @param nsPrefix
	 *            Namespace-Präfix des Schema-Elements
	 */
	public SchemaElement(String name, String nsUrl, String nsPrefix) {
		super();
		this.name = name;
		this.nsUrl = nsUrl;
		this.nsPrefix = nsPrefix;
	}

	/**
	 * Gibt den Namen des Schema-Elements zurück (ohne Namespace-Präfix)
	 * 
	 * @return Name des Schema-Elements
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gibt die Namespace-URL des Schema-Elements zurück
	 * 
	 * @return Namespace-URL
	 */
	public String getNsUrl() {
		return nsUrl;
	}

	/**
	 * Gibt den Namespace-Präfix des Schema-Elements zurück
	 * 
	 * @return Namespace-Präfix
	 */
	public String getNsPrefix() {
		return nsPrefix;
	}

	/**
	 * Gibt den Namen des Schema-Elements zusammen mit dem Namespace-Präfix zurück
	 * 
	 * @return Name inkl. Namspace-Präfix
	 */
	public String getNameWithPrefix() {
		String nameWithPrefix = name;
		if (nsPrefix.length() > 0) {
			nameWithPrefix = nsPrefix + ":" + name;
		}
		return nameWithPrefix;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public boolean equals(Object objToProof) {
		if (!(objToProof instanceof SchemaElement)) {
			return false;
		} else {
			SchemaElement seToProof = (SchemaElement) objToProof;
			boolean nameEqual = ((this.name == null) && (seToProof.getName() == null))
					|| this.name.equals(seToProof.getName());
			boolean urlEqual = ((this.nsUrl == null) && (seToProof.getNsUrl() == null))
					|| this.nsUrl.equals(seToProof.getNsUrl());
			boolean prefixEqual = ((this.nsPrefix == null) && (seToProof.getNsPrefix() == null))
					|| this.nsPrefix.equals(seToProof.getNsPrefix());
			return (nameEqual && urlEqual && prefixEqual);
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + name.hashCode();
		result = 31 * result + nsUrl.hashCode();
		result = 31 * result + nsPrefix.hashCode();
		return result;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public int compareTo(SchemaElement o) {
		return this.name.compareTo(o.getName());
	}
}
