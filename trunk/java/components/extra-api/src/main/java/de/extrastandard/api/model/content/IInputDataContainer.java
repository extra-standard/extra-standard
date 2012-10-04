package de.extrastandard.api.model.content;

import java.util.List;

import de.extrastandard.api.util.IImplementor;

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
public interface IInputDataContainer extends IImplementor {

	/**
	 * Liefert Daten Verschlüsselung, Transformation usw Beschreibung.
	 * 
	 * @return
	 */
	List<IInputDataPluginDescription> getPlugins();

	/**
	 * Liefert eine Identifizierung der InputDaten In der Phase 1 DataRequestId
	 * In derPhase 2 responseId
	 * 
	 * @return
	 */
	String getInputIdentification();

	/**
	 * Liefert RequestId, für die eindeutige Identifizierung in der XmlMessage
	 * 
	 * @return
	 */
	String getRequestId();

	/**
	 * @param requestId
	 *            Setzt RequestId für die eindeutige Identifizierung in der
	 *            XmlMesage
	 */
	public void setRequestId(final String requestId);
}