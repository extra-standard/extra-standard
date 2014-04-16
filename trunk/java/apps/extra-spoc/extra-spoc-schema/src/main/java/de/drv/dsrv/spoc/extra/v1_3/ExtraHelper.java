package de.drv.dsrv.spoc.extra.v1_3;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.AnyXMLType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.ClassifiableIDType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.DataType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.FlagCodeType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.FlagType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.ReportType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.TextType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.request.TransportRequestBodyType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.request.TransportRequestType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.response.TransportResponseBodyType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.response.TransportResponseType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorReasonType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorType;

/**
 * Bietet Methoden rund um das eXTra-Schema in der Version 1.3 an. <br />
 * Als Parameter und Rueckgabewerte werden die aus dem Schema generierten
 * JAXB-Klassen verwendet.
 */
public final class ExtraHelper {

	private static final String VERSION = "1.0";
	private static final String ERROR_WEIGHT = "http://www.extra-standard.de/weight/ERROR";
	private static final String MTOM_DATA_ID = "MTOMDataID";

	private ExtraHelper() {
	}

	/**
	 * Mit den uebergebenen Parametern wird ein eXTra-Error-Objekt erstellt.
	 * 
	 * @param reason
	 *            Fehlerursache; darf nicht <code>null</code> sein
	 * @param errorCode
	 *            Fehlercode; darf nicht <code>null</code> sein
	 * @param errorText
	 *            Fehlertext; darf nicht <code>null</code> sein
	 * @return eXTra-Error-Objekt
	 * @throws DatatypeConfigurationException
	 *             wenn beim Setzen des Datums im Zusammenhang mit der
	 *             DatatypeFactory ein Fehler auftritt; fuer Details siehe
	 *             JavaDoc der Ausnahme
	 * @throws IllegalArgumentException
	 *             wenn <code>reason</code>, <code>errorCode</code> oder
	 *             <code>errorText</code> den Wert <code>null</code> haben
	 */
	public static ExtraErrorType generateError(final ExtraErrorReasonType reason, final String errorCode,
			final String errorText) throws DatatypeConfigurationException {
		return generateError(reason, null, null, errorCode, errorText);
	}

	/**
	 * Mit den uebergebenen Parametern wird ein eXTra-Error-Objekt erstellt.
	 * 
	 * @param reason
	 *            Fehlerursache; darf nicht <code>null</code> sein
	 * @param requestId
	 *            ID des Requests; kann <code>null</code> sein
	 * @param responseId
	 *            ID der Response; kann <code>null</code> sein
	 * @param errorCode
	 *            Fehlercode; darf nicht <code>null</code> sein
	 * @param errorText
	 *            Fehlertext; darf nicht <code>null</code> sein
	 * @return eXTra-Error-Objekt
	 * @throws DatatypeConfigurationException
	 *             wenn beim Setzen des Datums im Zusammenhang mit der
	 *             DatatypeFactory ein Fehler auftritt; fuer Details siehe
	 *             JavaDoc der Ausnahme
	 * @throws IllegalArgumentException
	 *             wenn <code>reason</code>, <code>errorCode</code> oder
	 *             <code>errorText</code> den Wert <code>null</code> haben
	 */
	public static ExtraErrorType generateError(final ExtraErrorReasonType reason, final ClassifiableIDType requestId,
			final ClassifiableIDType responseId, final String errorCode, final String errorText)
			throws DatatypeConfigurationException {

		// Pruefe Werte, die nicht null sein duerfen
		checkNotNullableValue(reason, "reason");
		checkNotNullableValue(errorCode, "errorCode");
		checkNotNullableValue(errorText, "errorText");

		final ExtraErrorType extraError = new ExtraErrorType();

		// Error-Attribut
		// Nur dieser Wert zugelassen
		extraError.setVersion(VERSION);

		// Reason
		extraError.setReason(reason);

		// Request-ID
		extraError.setRequestID(requestId);

		// Response-ID
		extraError.setResponseID(responseId);

		// Report mit Flag
		final ReportType report = new ReportType();
		report.setHighestWeight(ERROR_WEIGHT);
		final FlagType flag = new FlagType();
		flag.setWeight(ERROR_WEIGHT);
		final FlagCodeType flagCode = new FlagCodeType();
		flagCode.setValue(errorCode);
		flag.setCode(flagCode);
		final TextType flagText = new TextType();
		flagText.setValue(errorText);
		flag.setText(flagText);
		report.getFlag().add(flag);
		extraError.setReport(report);

		// Aktueller Zeitstempel
		extraError.setTimeStamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));

		return extraError;
	}

	/**
	 * Setzt {@code <MTOMDataID>_id_< /MTOMDataID>} in das {@link AnyXMLType}
	 * -Element von {@link DataType} des {@link TransportRequestBodyType}s. Das
	 * {@link AnyXMLType}-Element wird dabei ueberschrieben. Sollte
	 * {@link DataType} oder {@link TransportRequestBodyType} nicht gesetzt
	 * sein, werden diese erstellt. transportRequestType darf nicht
	 * <code>null</code> sein.
	 * 
	 * @param transportRequestType
	 *            - das Objekt in das die MTOMDataID gesetzt wird, darf nicht
	 *            <code>null</code> sein.
	 * @param mtomDataId
	 *            - Die ID des Attachements in der Datenbank.
	 * @throws ParserConfigurationException
	 *             wenn beim Erstellen des {@link AnyXMLType}-Elements ein
	 *             Fehler auftritt.; fuer Details siehe JavaDoc der Ausnahme
	 * @throws IllegalArgumentException
	 *             wenn transportRequestType <code>null</code> ist.
	 */
	public static void setMtomDataIdToRequest(final TransportRequestType transportRequestType, final int mtomDataId)
			throws ParserConfigurationException {

		checkNotNullableValue(transportRequestType, "transportRequestType");

		final AnyXMLType anyXMLType = getMtomDataIdAnyXml(mtomDataId);

		TransportRequestBodyType body = transportRequestType.getTransportBody();
		if (body == null) {
			body = new TransportRequestBodyType();
			transportRequestType.setTransportBody(body);
		}

		DataType data = body.getData();
		if (data == null) {
			data = new DataType();
			body.setData(data);
		}

		data.setAnyXML(anyXMLType);
	}

	/**
	 * Setzt {@code <MTOMDataID>_id_< /MTOMDataID>} in das {@link AnyXMLType}
	 * -Element von {@link DataType} des {@link TransportResponseBodyType}s. Das
	 * {@link AnyXMLType}-Element wird dabei ueberschrieben. Sollte
	 * {@link DataType} oder {@link TransportResponseBodyType} nicht gesetzt
	 * sein, werden diese erstellt. transportResponseType darf nicht
	 * <code>null</code> sein.
	 * 
	 * @param transportResponseType
	 *            - das Objekt in das die MTOMDataID gesetzt wird, darf nicht
	 *            <code>null</code> sein.
	 * @param mtomDataId
	 *            - Die ID des Attachements in der Datenbank.
	 * @throws ParserConfigurationException
	 *             wenn beim Erstellen des {@link AnyXMLType}-Elements ein
	 *             Fehler auftritt.; fuer Details siehe JavaDoc der Ausnahme
	 * @throws IllegalArgumentException
	 *             wenn transportResponseType <code>null</code> ist.
	 */
	public static void setMtomDataIdToResponse(final TransportResponseType transportResponseType, final int mtomDataId)
			throws ParserConfigurationException {

		checkNotNullableValue(transportResponseType, "transportResponseType");

		if (transportResponseType == null) {
			throw new IllegalArgumentException("TransportResponseType darf nicht null sein.");
		}

		final AnyXMLType anyXMLType = getMtomDataIdAnyXml(mtomDataId);

		TransportResponseBodyType body = transportResponseType.getTransportBody();
		if (body == null) {
			body = new TransportResponseBodyType();
			transportResponseType.setTransportBody(body);
		}

		DataType data = body.getData();
		if (data == null) {
			data = new DataType();
			body.setData(data);
		}

		data.setAnyXML(anyXMLType);
	}

	/**
	 * Liest die MTOMDataID des {@link TransportRequestType} aus und gibt diese
	 * zurueck. Der Rueckgabewert ist <code>null</code>, falls das Element nicht
	 * vorhanden ist, oder z.B. mehrere Elemente auf Ebene der MTOMDataID sind.
	 * transportRequestType darf nicht <code>null</code> sein.
	 * 
	 * @param transportRequestType
	 *            Das Objekt, in dem die MTOMDataID gesucht wird, darf nicht
	 *            <code>null</code> sein.
	 * @return Die ID des Attachements aus der Datenbank, wenn diese angeben
	 *         ist. <code>null</code> sonst.
	 * @throws IllegalArgumentException
	 *             Wenn transportRequestType <code>null</code> ist.
	 */
	public static Integer getMtomDataIdFromRequest(final TransportRequestType transportRequestType) {

		checkNotNullableValue(transportRequestType, "transportRequestType");

		Integer mtomDataId = null;

		// Pruefe, ob genau ein Any-Element vorhanden ist
		if ((transportRequestType.getTransportBody() != null)
				&& (transportRequestType.getTransportBody().getData() != null)
				&& (transportRequestType.getTransportBody().getData().getAnyXML() != null)
				&& (transportRequestType.getTransportBody().getData().getAnyXML().getAny() != null)
				&& (transportRequestType.getTransportBody().getData().getAnyXML().getAny().size() == 1)) {

			final org.w3c.dom.Element element = transportRequestType.getTransportBody().getData().getAnyXML().getAny()
					.get(0);

			if (MTOM_DATA_ID.equals(element.getNodeName())) {
				mtomDataId = Integer.parseInt(element.getTextContent());
			}
		}

		return mtomDataId;
	}

	/**
	 * Liest die MTOMDataID des {@link TransportResponseType} aus und gibt diese
	 * zurueck. Der Rueckgabewert ist <code>null</code>, falls das Element nicht
	 * vorhanden ist, oder z.B. mehrere Elemente auf Ebene der MTOMDataID sind.
	 * transportResponseType darf nicht <code>null</code> sein.
	 * 
	 * @param transportResponseType
	 *            Das Objekt, in dem die MTOMDataID gesucht wird, darf nicht
	 *            <code>null</code> sein.
	 * @return Die ID des Attachements aus der Datenbank, wenn diese angeben
	 *         ist. <code>null</code> sonst.
	 * @throws IllegalArgumentException
	 *             Wenn transportResponseType <code>null</code> ist.
	 */
	public static Integer getMtomDataIdFromResponse(final TransportResponseType transportResponseType) {

		checkNotNullableValue(transportResponseType, "transportResponseType");

		Integer mtomDataId = null;

		// Pruefe, ob genau ein Any-Element vorhanden ist
		if ((transportResponseType.getTransportBody() != null)
				&& (transportResponseType.getTransportBody().getData() != null)
				&& (transportResponseType.getTransportBody().getData().getAnyXML() != null)
				&& (transportResponseType.getTransportBody().getData().getAnyXML().getAny() != null)
				&& (transportResponseType.getTransportBody().getData().getAnyXML().getAny().size() == 1)) {

			final org.w3c.dom.Element element = transportResponseType.getTransportBody().getData().getAnyXML().getAny()
					.get(0);

			if (MTOM_DATA_ID.equals(element.getNodeName())) {
				mtomDataId = Integer.parseInt(element.getTextContent());
			}
		}

		return mtomDataId;
	}

	private static void checkNotNullableValue(final Object notNullableValue, final String parameterName)
			throws IllegalArgumentException {
		if (notNullableValue == null) {
			throw new IllegalArgumentException("Parameter " + parameterName + " darf nicht null sein.");
		}
	}

	/**
	 * Erstellt ein {@link AnyXMLType} fuer das Einfuegen der MTOMDataID in den
	 * TransportBody.
	 * 
	 * @param mtomDataId
	 * @return
	 * @throws ParserConfigurationException
	 */
	private static AnyXMLType getMtomDataIdAnyXml(final int mtomDataId) throws ParserConfigurationException {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder builder = factory.newDocumentBuilder();
		final Document document = builder.newDocument();

		final Element elMtomDataId = document.createElement(MTOM_DATA_ID);
		elMtomDataId.setTextContent("" + mtomDataId);

		final AnyXMLType anyXMLType = new AnyXMLType();
		anyXMLType.getAny().add(elMtomDataId);

		return anyXMLType;
	}
}
