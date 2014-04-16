package de.drv.dsrv.spoc.web.webservice;

/**
 * Wird geworfen, wenn SPoC nicht identifizieren kann, an welches Fachverfahren
 * ein Request weiter geleitet werden soll.
 */
public class UnidentifiedFachverfahrenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String profile;
	private final String version;
	private final String procedure;
	private final String dataType;

	/**
	 * @param profile
	 *            Wert des profile-Attributs im Transport-Element
	 * @param version
	 *            Wert des version-Attributs im Transport-Element
	 * @param procedure
	 *            Wert des Procedure-Elements im TransportHeader
	 * @param dataType
	 *            Wert des DataType-Elements im TransportHeader
	 */
	public UnidentifiedFachverfahrenException(final String profile, final String version, final String procedure,
			final String dataType) {
		super();
		this.profile = profile;
		this.version = version;
		this.procedure = procedure;
		this.dataType = dataType;
	}

	@Override
	public String getMessage() {
		return "Kein Fachverfahren f\u00fcr Profile: " + this.profile + ", Version: " + this.version + ", Procedure: "
				+ this.procedure + ", DataType: " + this.dataType + " vorhanden.";
	}

	/**
	 * @return Wert des profile-Attributs im Transport-Element
	 */
	public String getProfile() {
		return this.profile;
	}

	/**
	 * @return Wert des version-Attributs im Transport-Element
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * @return Wert des Procedure-Elements im TransportHeader
	 */
	public String getProcedure() {
		return this.procedure;
	}

	/**
	 * @return Wert des DataType-Elements im TransportHeader
	 */
	public String getDataType() {
		return this.dataType;
	}
}
