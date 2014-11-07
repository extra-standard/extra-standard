package de.drv.dsrv.spoc.extra.v1_3;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorType;

/**
 * Die Exception hat ein {@link ExtraErrorType} als Instanzvariable, die immer
 * gesetzt sein muss. Deswegen gibt es auch nur einen Konstruktor mit diesem
 * Parameter. Verwendet wird die Exception, wenn statt einem
 * TransportResponseType ein {@link ExtraErrorType} zurueckgegeben werden soll.
 * Die Unterscheidung findet also in einem <code>try-catch</code>-Block statt.
 */
public class ExtraErrorResponseException extends Exception {

	private static final long serialVersionUID = 906192905275346978L;
	private final ExtraErrorType extraErrorType;

	/**
	 * Konstruktor.
	 * 
	 * @param extraErrorType
	 *            der in der Response zurueckgegebene ExtraError
	 */
	public ExtraErrorResponseException(final ExtraErrorType extraErrorType) {
		this.extraErrorType = extraErrorType;
	}

	/**
	 * Gibt den in der Response zurueckgegebenen ExtraError zurueck.
	 * 
	 * @return der in der Response zurueckgegebene ExtraError
	 */
	public ExtraErrorType getExtraErrorType() {
		return this.extraErrorType;
	}
}
