package de.extrastandard.api.model.content;

public interface ISingleQueryInputData extends ISingleInputData {

	/**
	 * @return the dbInputDataId
	 */
	public String getOriginRequestId();

	/**
	 * @return the serverResponceId
	 */
	public String getServerResponceId();

}
