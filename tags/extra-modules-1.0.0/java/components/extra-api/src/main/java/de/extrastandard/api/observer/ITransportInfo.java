package de.extrastandard.api.observer;

import java.util.Calendar;

/**
 * Representiert Informationen einer Übertragung.
 * 
 * @author Leonid Potap
 * @version $Id$
 */
public interface ITransportInfo {

	/**
	 * Eindeutige TransportId
	 * 
	 * @return
	 * 
	 */
	String getHeaderId();

	/**
	 * Zeitstempel der Übertragung, gewonnen aus Header
	 * 
	 * @return
	 */
	Calendar getTime();

	/**
	 * Procecedure aus Header
	 * 
	 * @return
	 */
	String getProcedure();

	/**
	 * Application aus Header
	 * 
	 * @return
	 */
	String getApplication();

}
