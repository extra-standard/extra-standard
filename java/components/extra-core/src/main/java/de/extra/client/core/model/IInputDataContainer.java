package de.extra.client.core.model;

import java.io.InputStream;
import java.util.List;

public interface IInputDataContainer {

	String getRequestId();

	List<IInputDataPluginDescription> getPlugins();

	String getDataRequestId();

	/**
	 * @return the inputData
	 */
	InputStream getInputData();

}