package de.extrastandard.api.model.content;

import java.util.Collection;

/**
 * Provides results back to a request / transmission.
 * 
 * @author Leonid Potap
 * @version $Id$
 */
public interface IResponseData {

	/**
	 * 
	 * @return all results contained in the response
	 */
	Collection<ISingleResponseData> getReponses();

	/**
	 * returns an {@link SingleResponseData} specified by reguestId
	 * 
	 * @param reguestId
	 * @return
	 */
	ISingleResponseData getResponse(String reguestId);

	/**
	 * Add a {@link SingleResponseData} to ResponseData
	 * 
	 * @param singleResponseData
	 */
	void addSingleResponse(ISingleResponseData singleResponseData);

}
