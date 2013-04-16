package de.extra.client.core.responce.impl;

import java.util.ArrayList;
import java.util.Collection;

import de.extrastandard.api.exception.ExceptionCode;
import de.extrastandard.api.exception.ExtraConfigRuntimeException;
import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.model.content.ISingleResponseData;

/**
 * Wenn eine Nachricht mit mehreren Requests einen Response mit nur einer
 * Acknoledge bekommt, ist diese Klasse für das Processing zu nutzen
 * 
 * @author Leonid Potap
 * @version $Id$
 */
public class SingleResponseDataForMultipleRequest implements IResponseData {

	ISingleResponseData singleResponseData;

	/**
	 * @param singleResponseData
	 */
	public SingleResponseDataForMultipleRequest(
			final ISingleResponseData singleResponseData) {
		super();
		this.singleResponseData = singleResponseData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extrastandard.api.model.content.IResponseData#getReponses()
	 */
	@Override
	public Collection<ISingleResponseData> getResponses() {
		final Collection<ISingleResponseData> responses = new ArrayList<ISingleResponseData>();
		responses.add(singleResponseData);
		return responses;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.extrastandard.api.model.content.IResponseData#getResponse(java.lang
	 * .String)
	 */
	@Override
	public Collection<ISingleResponseData> getResponse(final String reguestId) {
		ArrayList<ISingleResponseData> responseList = new ArrayList<ISingleResponseData>();
		responseList.add(singleResponseData);
		return responseList;
	}

	/**
	 * Fügt einen neuen Datensatz zu den Responses
	 * 
	 * @param singleResponseData
	 */
	@Override
	public void addSingleResponse(final ISingleResponseData singleResponseData) {
		throw new ExtraConfigRuntimeException(
				ExceptionCode.EXTRA_ILLEGAL_ACCESS_EXCEPTION,
				"SingleResponseDataForMultipleRequest.addSingleResponse method is not supported");
	}

	@Override
	public Boolean isSuccessful() {
		if (getResponses() != null) {
			for (final ISingleResponseData iResponseData : getResponses()) {
				if (!iResponseData.isSuccessful()) {
					return false;
				}
			}			
		}
		return true;
	}

	/**
	 * Keine Warnung moeglich!
	 */
	@Override
	public Boolean isWarning() {
		return false;
	}

	@Override
	public void setWarning(Boolean warning) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SingleResponseDataForMultipleRequest [");
		if (singleResponseData != null) {
			builder.append("singleResponseData=");
			builder.append(singleResponseData);
		}
		builder.append("]");
		return builder.toString();
	}

}
