package de.extrastandard.api.model;

import java.util.List;
import java.util.Map;

public interface IExtraProfileConfiguration {

	String getContentType();

	boolean isMessageLayer();

	boolean isPackageLayer();

	/**
	 * @return
	 */
	String getRootElement();

	/**
	 * @return the elementsHierarchyMap
	 */
	Map<String, List<String>> getElementsHierarchyMap();

	/**
	 * Liefert alle Kinderelemente der Knote. Wenn Knote keine kinderelemente
	 * hat, wird ein leeren List zurückgeliefert
	 * 
	 * @param parentElement2
	 * @return
	 */
	List<String> getChildElements(String parentElement);

	String getFieldName(String parentElement, String childElement);

}