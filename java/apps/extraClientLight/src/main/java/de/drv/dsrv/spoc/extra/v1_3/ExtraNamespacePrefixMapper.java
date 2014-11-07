package de.drv.dsrv.spoc.extra.v1_3;

import java.util.HashMap;
import java.util.Map;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * Erweitert den {@link com.sun.xml.bind.marshaller.NamespacePrefixMapper} um
 * die Namespace-Praefixe des eXTra-Schemas in der Version 1.3. <br />
 * Es wird ein Standard-Mapping angeboten, das aber auch mit der Methode
 * <code>setMapping</code> angepasst werden kann.
 */
public final class ExtraNamespacePrefixMapper extends NamespacePrefixMapper {

	private transient Map<String, String> mapping;

	/**
	 * Konstruktor.
	 */
	public ExtraNamespacePrefixMapper() {
		setDefaultMapping();
	}

	/**
	 * Ermittelt das Praefix fuer die uebergebene <code>namespaceUri</code>.
	 * Falls dieser Namespace nicht im Mapping definiert ist, wird als Praefix
	 * <code>suggestion</code> zurueckgegeben.
	 * 
	 * @param namespaceUri
	 *            Namespace, fuer den das Praefix ermittelt werden soll
	 * @param suggestion
	 *            wird als Praefix verwendet, falls <code>namespaceUri</code>
	 *            nicht im Mapping definiert ist
	 * @param requirePrefix
	 *            wird in dieser Implementierung nicht verwendet
	 */
	@Override
	public String getPreferredPrefix(final String namespaceUri, final String suggestion, final boolean requirePrefix) {
		String prefix = suggestion;

		if (this.mapping.containsKey(namespaceUri)) {
			prefix = this.mapping.get(namespaceUri);
		}

		return prefix;
	}

	/**
	 * Speichert das uebergebene Mapping.<br/>
	 * Damit wird das Standard-Mapping ueberschrieben.
	 * 
	 * @param mapping
	 *            Mapping fuer die Namespace-Praefixe
	 */
	public void setMapping(final Map<String, String> mapping) {
		this.mapping = mapping;
	}

	private void setDefaultMapping() {
		this.mapping = new HashMap<String, String>();
		this.mapping.put("http://www.extra-standard.de/namespace/request/1", "xreq");
		this.mapping.put("http://www.extra-standard.de/namespace/response/1", "xres");
		this.mapping.put("http://www.extra-standard.de/namespace/service/1", "xsrv");
		this.mapping.put("http://www.extra-standard.de/namespace/components/1", "xcpt");
		this.mapping.put("http://www.extra-standard.de/namespace/logging/1", "xlog");
		this.mapping.put("http://www.extra-standard.de/namespace/plugins/1", "xplg");
		this.mapping.put("http://www.extra-standard.de/namespace/message/1", "xmsg");
		this.mapping.put("http://www.w3.org/2001/04/xmlenc#", "xenc");
		this.mapping.put("http://www.w3.org/2000/09/xmldsig#", "ds");
	}
}