package de.extra.xtt.util.schema;

import com.sun.xml.xsom.XSType;

/**
 * 
 * Diese Klasse ist ein Container f�r einen Schema-Typ und den dazugeh�rigen Namen des Schema-Elements, von dem dieser
 * Typ verwendet wird.
 * 
 * @author Beier
 * 
 */
public class ContXsType {

	private final XSType type;
	private final String elementNameWithPrefix;

	/**
	 * Konstruktor zur Belegung des Typs und Elementsnamens
	 * 
	 * @param type
	 *            XML-Schematyp
	 * @param elementNameWithPrefix
	 *            Name inkl. Pr�fix des Elements, das diesen Typ verwendet
	 */
	public ContXsType(XSType type, String elementNameWithPrefix) {
		this.type = type;
		this.elementNameWithPrefix = elementNameWithPrefix;
	}

	/**
	 * Gibt den Schematyp zur�ck
	 * 
	 * @return XML-Schematyp
	 */
	public XSType getType() {
		return type;
	}

	/**
	 * Gibt den Namen des Elements inkl. Pr�fix zur�ck
	 * 
	 * @return Name des Elements inkl. Pr�fix
	 */
	public String getElementNameWithPrefix() {
		return elementNameWithPrefix;
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof ContXsType) {
			return type.equals(((ContXsType) o).getType());
		} else {
			return super.equals(o);
		}
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}
	
	

}
