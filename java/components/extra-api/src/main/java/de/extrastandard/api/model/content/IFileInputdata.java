package de.extrastandard.api.model.content;

import java.io.InputStream;

public interface IFileInputdata extends IInputDataContainer {

	/**
	 * @return the inputData as Stream
	 */
	public abstract InputStream getInputData();

	/**
	 * @return the hashCode
	 */
	public abstract String getHashCode();

	/**
	 * @return the fileName
	 */
	public abstract String getFileName();

}