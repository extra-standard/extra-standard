package de.drv.dsrv.extra.marshaller;

import java.util.Map;

public class NamespacePrefixMapper extends
		com.sun.xml.bind.marshaller.NamespacePrefixMapper {

	private Map<String, String> mappings;

	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion,
			boolean requirePrefix) {

		if (mappings.containsKey(namespaceUri)) {
			return mappings.get(namespaceUri);
		} else {
			return suggestion;
		}
	}

	public void setMappings(Map<String, String> mappings) {
		this.mappings = mappings;
	}
}
