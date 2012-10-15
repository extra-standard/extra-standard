package de.extrastandard.api.model.content;

import java.io.File;
import java.util.List;

public interface ISingleStreamInputData {

	/**
	 * Liefert Daten Verschlüsselung, Transformation usw Beschreibung.
	 * 
	 * @return
	 */
	List<IInputDataPluginDescription> getPlugins();

	/**
	 * @return the inputData as Stream
	 */
	File getInputData();

}
