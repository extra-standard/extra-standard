package de.drv.dsrv.spoc.extra.v1_3;

import java.io.Serializable;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.Base64CharSequenceType;

/**
 * Erweitert {@link Base64CharSequenceType}, damit das JAXB-Objekt serialisiert
 * und folglich auch verschickt werden kann. Die Daten koennen in
 * <code>valueAsArray</code> abgelegt werden. Damit kann der DataHandler auf
 * <code>null</code> gesetzt werden (<code>setValue</code> der Elternklasse).
 */
public class Base64CharSequenceArrayType extends Base64CharSequenceType implements Serializable {

	private static final long serialVersionUID = 1L;

	private byte[] valueAsArray;

	/**
	 * Gibt die Nutzdaten als Byte-Array zurueck.
	 * 
	 * @return Nutzdaten
	 */
	public byte[] getValueAsArray() {
		return valueAsArray;
	}

	/**
	 * Setzt die Nutzdaten.
	 * 
	 * @param valueAsArray
	 */
	public void setValueAsArray(final byte[] valueAsArray) {
		this.valueAsArray = valueAsArray;
	}
}
