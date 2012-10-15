package de.extra.client.core.responce.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.model.content.ISingleResponseData;

public class ResponseData implements IResponseData {

	private final Map<String, ISingleResponseData> responseDatenMap = new HashMap<String, ISingleResponseData>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extrastandard.api.model.content.IResponseData#getReponses()
	 */
	@Override
	public Collection<ISingleResponseData> getResponses() {
		return responseDatenMap.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.extrastandard.api.model.content.IResponseData#getResponse(java.lang
	 * .String)
	 */
	@Override
	public ISingleResponseData getResponse(final String reguestId) {
		return responseDatenMap.get(reguestId);
	}

	/**
	 * Fügt einen neuen Datensatz zu den Responses
	 * 
	 * @param singleResponseData
	 */
	@Override
	public void addSingleResponse(final ISingleResponseData singleResponseData) {
		responseDatenMap.put(singleResponseData.getRequestId(), singleResponseData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ResponseData [");
		final Collection<ISingleResponseData> responses = getResponses();
		if (responses != null && !responses.isEmpty()) {
			for (final ISingleResponseData singleResponseData : responses) {
				builder.append(" SingleResponseData: ").append(singleResponseData);
			}
		}
		builder.append("]");
		return builder.toString();
	}

}
