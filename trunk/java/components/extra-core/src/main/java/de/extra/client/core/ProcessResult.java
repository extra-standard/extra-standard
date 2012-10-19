package de.extra.client.core;

import de.extrastandard.api.model.content.IResponseData;

/**
 * 
 * Result transmission of a transport. Result can contain several single
 * responses.
 * 
 * @author DPRS
 * @version $Id$
 */
public class ProcessResult {

	private final IResponseData responseData;

	private final Exception exception;

	public ProcessResult(final IResponseData responseData, final Exception exception) {
		this.responseData = responseData;
		this.exception = exception;
	}

	public ProcessResult(final IResponseData responseData) {
		this(responseData, null);
	}

	public ProcessResult(final Exception exception) {
		this(null, exception);
	}

	/**
	 * @return the responseDatas
	 */
	public IResponseData getResponseData() {
		return responseData;
	}

	/**
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ProcessResult [");
		if (responseData != null) {
			builder.append("responseData=");
			builder.append(responseData);
			builder.append(", ");
		}
		if (exception != null) {
			builder.append("exception=");
			builder.append(exception);
		}
		builder.append("]");
		return builder.toString();
	}

}
