package de.drv.dsrv.spoc.web.webservice.jax;

import javax.xml.ws.WebFault;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorType;

/**
 * Repraesentiert einen eXTra-Fehler als Ausnahme eines Webservice-Aufrufs.
 */
@WebFault(name = "ExtraError", targetNamespace = "http://www.extra-standard.de/namespace/service/1")
public class ExtraError extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Java type that goes as soapenv:Fault detail element.
	 * 
	 */
	private final ExtraErrorType faultInfo;

	/**
	 * Konstruktor.
	 * 
	 * @param faulString
	 *            Fehlertext
	 * @param faultInfo
	 *            Fehlerinfo-Objekt
	 */
	public ExtraError(final String faulString, final ExtraErrorType faultInfo) {
		super(faulString);
		this.faultInfo = faultInfo;
	}

	public ExtraErrorType getFaultInfo() {
		return this.faultInfo;
	}
}
