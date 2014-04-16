package de.drv.dsrv.spoc.web.service;

/**
 * Exception f&uuml;r einen fehlerhafte Antwort des Fachverfahrens.
 */
public class FachverfahrenResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final int httpStatusCode;
	private final String reason;

	/**
	 * @param httpStatusCode der HTTP Status Code der Antwort des Fachverfahrens (z.B. 404 Not Found)
	 * @param reason der vom Fachverfahren mitgegebene Reason String
	 */
	public FachverfahrenResponseException(final int httpStatusCode, final String reason) {
		super();
		this.httpStatusCode = httpStatusCode;
		this.reason = reason;
	}

	/**
	 * @return der HTTP Status Code der Antwort des Fachverfahrens (z.B. 404 Not Found)
	 */
	public int getHttpStatusCode() {
		return this.httpStatusCode;
	}

	/**
	 * @return der vom Fachverfahren mitgegebene Reason String
	 */
	public String getReason() {
		return this.reason;
	}

	@Override
	public String toString() {
		return "Fehler bei der Antwort des Fachverfahrens mit HTTP Code >" + this.httpStatusCode
				+ "< und Reason String >" + this.reason + "<.";
	}
}
