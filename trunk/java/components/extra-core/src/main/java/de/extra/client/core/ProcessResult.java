package de.extra.client.core;

import java.util.List;

import de.extrastandard.api.model.IResponseData;

/**
 * @author DPRS
 * @version $Id$
 */
public class ProcessResult {

	private final List<IResponseData> responseDatas;

	private final Exception exception;

	public ProcessResult(final List<IResponseData> responseDatas, final Exception exception) {
		this.responseDatas = responseDatas;
		this.exception = exception;
	}

	public ProcessResult(final List<IResponseData> responseDatas) {
		this(responseDatas, null);
	}

	public ProcessResult(final Exception exception) {
		this(null, exception);
	}

	/**
	 * @return the responseDatas
	 */
	public List<IResponseData> getResponseDatas() {
		return responseDatas;
	}

	/**
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}

}
