package de.drv.dsrv.spoc.commons.mtomdata;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Datenklasse, welche saemtliche Informationen zu einem Eintrag in der
 * Datenbanktabelle <code>MTOM_DATA</code> beinhaltet.
 */
public class MtomData implements Serializable {
	private static final long serialVersionUID = 8177131121268799350L;

	private int id;
	private int oid;
	private Date zeitpunkt;
	private InputStream nutzdaten;
	private String extraProcedure;
	private String extraDatatype;
	private String extraProfile;

	/**
	 * Gibt die eindeutige ID des MTOM-DATA Eintrages zurueck.
	 * 
	 * @return die eindeutige ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setzt die eindeutige ID des MTOM-DATA Eintrages.
	 * 
	 * @param id
	 *            zu speichernde ID.
	 */
	public void setId(final int id) {
		this.id = id;
	}

	/**
	 * Gibt die OID zurueck.
	 * 
	 * @return OID
	 */
	public int getOid() {
		return oid;
	}

	/**
	 * Setzt die OID.
	 * 
	 * @param oid
	 */
	public void setOid(final int oid) {
		this.oid = oid;
	}

	/**
	 * Gibt den Zeitpunkt zurueck, zu dem der Tabelleneintrag getaetigt wurde.
	 * 
	 * @return Zeitpunkt
	 */
	public Date getZeitpunkt() {
		return zeitpunkt;
	}

	/**
	 * Setzt den Zeitpunkt, zu dem der Tabelleneintrag getaetigt wurde.
	 * 
	 * @param zeitpunkt
	 */
	public void setZeitpunkt(final Date zeitpunkt) {
		this.zeitpunkt = zeitpunkt;
	}

	/**
	 * Gibt den {@link InputStream} der Nutzdaten zurueck.
	 * 
	 * @return Nutzdaten
	 */
	public InputStream getNutzdaten() {
		return nutzdaten;
	}

	/**
	 * Setzt den {@link InputStream} der Nutzdaten.
	 * 
	 * @param nutzdaten
	 */
	public void setNutzdaten(final InputStream nutzdaten) {
		this.nutzdaten = nutzdaten;
	}

	/**
	 * Gibt die eXTra-Procedure zurueck.
	 * 
	 * @return eXTra-Procedure
	 */
	public String getExtraProcedure() {
		return extraProcedure;
	}

	/**
	 * Setzt die eXTra-Procedure.
	 * 
	 * @param extraProcedure
	 */
	public void setExtraProcedure(final String extraProcedure) {
		this.extraProcedure = extraProcedure;
	}

	/**
	 * Gibt den eXTra-Datentype zurueck.
	 * 
	 * @return eXTra-Datentype
	 */
	public String getExtraDatatype() {
		return extraDatatype;
	}

	/**
	 * Setzt den eXTra-Datentype.
	 * 
	 * @param extraDatatype
	 */
	public void setExtraDatatype(final String extraDatatype) {
		this.extraDatatype = extraDatatype;
	}

	/**
	 * Gibt das eXTra-Profile zurueck.
	 * 
	 * @return eXTra-Profile
	 */
	public String getExtraProfile() {
		return extraProfile;
	}

	/**
	 * Setzt das eXTra-Profile.
	 * 
	 * @param extraProfile
	 */
	public void setExtraProfile(final String extraProfile) {
		this.extraProfile = extraProfile;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder("Id: ");
		builder.append(id);
		builder.append("\nOID: ");
		builder.append(oid);
		builder.append("\nZeitpunkt: ");
		builder.append(zeitpunkt);
		builder.append("\nExtra Procedure: ");
		builder.append(extraProcedure);
		builder.append("\nExtra Datatype: ");
		builder.append(extraDatatype);
		builder.append("\nExtra Profile: ");
		builder.append(extraProfile);
		return builder.toString();
	}
}
