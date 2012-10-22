package de.extra.client.core;

import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.model.content.IResponseData;

/**
 * 
 * Result transmission of a transport. Result can contain several single
 * responses.
 * 
 * @author DPRS
 * @version $Id: ProcessResult.java 841 2012-10-19 15:32:38Z
 *          potap.rentenservice@gmail.com $
 */
public class ProcessResult {

    /**
     * ResponseData for the given IInputData
     */
    private final IResponseData responseData;

    /**
     * InputData
     */
    private final IInputDataContainer dataContainer;

    /**
     * Eception
     */
    private final Exception exception;

    public ProcessResult(final IResponseData responseData,
	    final IInputDataContainer dataContainer, final Exception exception) {
	this.responseData = responseData;
	this.dataContainer = dataContainer;
	this.exception = exception;
    }

    public ProcessResult(final IResponseData responseData,
	    final IInputDataContainer dataContainer) {
	this(responseData, dataContainer, null);
    }

    public ProcessResult(final Exception exception) {
	this(null, null, exception);
    }

    /**
     * @return the dataContainer
     */
    public IInputDataContainer getDataContainer() {
	return dataContainer;
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
