package de.extrastandard.api.model.content;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public interface ISingleContentInputData extends ISingleInputData {

	/**
	 * Liefert Daten Verschlüsselung, Transformation usw Beschreibung.
	 * 
	 * @return
	 */
	List<IInputDataPluginDescription> getPlugins();

	/**
	 * @return inputData as Stream
	 */
	InputStream getInputDataAsStream();

	/**
	 * Default Encoding UTF-8
	 * 
	 * @return
	 */
	String getInputDataAsString();

	/**
	 * Reads the contents of InputData into a String.
	 * 
	 * @param encoding
	 *            the encoding to use
	 * @return
	 */
	String getInputDataAsString(Charset encoding);

	/**
	 * Reads the contents of InputData into a byte array.
	 * 
	 * @return
	 */
	byte[] getInputDataAsByteArray();

	/**
	 * @return HashCode returns if available. If there is no hashCode returns
	 *         null
	 */
	String getHashCode();

	/**
	 * @return InputIdentifier
	 */
	String getInputIdentifier();

}
