package de.extrastandard.api.model.content;

import java.io.InputStream;
import java.util.List;

/**
 * <pre>
 * Beinhaltet die InputDaten für das Extra Klient.
 * In der Phase 1 sind die 
 * 	InputData
 *  plugins
 *  DataRequestId
 *  relevant 
 *  In der Phase 2 
 *  requestID
 *  TODO ? Refactor
 * </pre>
 * 
 * @author DPRS
 * @version $Id$
 */
public interface IInputDataContainer {

	/**
	 * Identifizierung der InputDaten in der phase 2
	 * 
	 * @return
	 */
	String getRequestId();

	/**
	 * Liefert Daten Verschlüsselung, Transformation usw Beschreibung.
	 * 
	 * @return
	 */
	List<IInputDataPluginDescription> getPlugins();

	/**
	 * Identifizierung der InputDaten in der phase 1
	 * 
	 * @return
	 */
	String getDataRequestId();

	/**
	 * InputData als Stream
	 * 
	 * @return the inputData
	 */
	InputStream getInputData();

	/**
	 * Liefert eine Identifizierung der InputDaten In der Phase 1 DataRequestId
	 * In derPhase 2 requestID
	 * 
	 * @return
	 */
	String getInputIdentification();

}