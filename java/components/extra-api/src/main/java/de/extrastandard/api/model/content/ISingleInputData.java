package de.extrastandard.api.model.content;

/**
 * Marker Interface für die inputDaten
 * 
 * @author Leonid Potap
 * @version $Id$
 */
public interface ISingleInputData {

	/**
	 * Set RequestId for InputData
	 * 
	 * @param requestId
	 */
	void setRequestId(String requestId);

	/**
	 * @return
	 */
	String getRequestId();

}
