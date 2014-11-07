package de.drv.dsrv.spoc.extra.v1_3;

import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.LinkedList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3._2000._09.xmldsig.CanonicalizationMethodType;
import org.w3._2001._04.xmlenc.AgreementMethodType;
import org.w3c.dom.Node;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.AnyPlugInContainerType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.logging.EventType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.messages.AbstractArgumentType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.plugins.AbstractAlgorithmType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.request.TransportRequestType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.response.TransportResponseType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ExtraErrorType;

/**
 * Bietet Methoden zum De- und Serialisieren von Extra-Objekten an. Als
 * Parameter und Rueckgabewerte werden die aus dem Schema generierten
 * JAXB-Klassen verwendet.
 */
public class ExtraJaxbMarshaller {

	private static final String MARSHALLER_PROPERTY_NAMESPACEPREFIXMAPPER = "com.sun.xml.bind.namespacePrefixMapper";
	private static final String JAXB_EXCEPTION = "Unerwartetes Element \"%s\". Das erwartete Element ist \"%s\".";
	private final ExtraNamespacePrefixMapper extraNamespacePrefixMapper;

	public ExtraJaxbMarshaller() {
		this.extraNamespacePrefixMapper = new ExtraNamespacePrefixMapper();
	}

	/**
	 * Serialisiert das uebergebene {@link ExtraErrorType} Objekt in einen
	 * String und gibt diesen zurueck.
	 * 
	 * @param extraError
	 *            zu serialisierendes Error-Objekt; darf nicht <code>null</code>
	 *            sein
	 * @return String-Repraesentation von {@link ExtraErrorType}
	 * @throws JAXBException
	 *             wenn beim Marshalling eine Exception geworfen wird
	 * @throws IllegalArgumentException
	 *             wenn <code>extraError</code> den Wert <code>null</code> hat
	 */
	public String marshalExtraError(final ExtraErrorType extraError) throws JAXBException {

		// Parameter-Validierung
		checkNotNullableValue(extraError, "extraError");

		// Wandle JAXB-Datenobjekt in eine Zeichenreihe um
		final Marshaller marshaller = createMarshaller();
		final StringWriter responseStringWriter = new StringWriter();
		marshaller.marshal(extraError, new StreamResult(responseStringWriter));
		return responseStringWriter.toString();
	}

	/**
	 * Serialisiert das uebergebene {@link ExtraErrorType} Objekt in das
	 * uebergebene <code>result</code>. <br/>
	 * Falls in <code>result</code> bereits Daten vorhanden sind, kann eine
	 * {@link RuntimeException} - abhaengig vom konkreten <code>Result</code>
	 * -Typ - geworfen werden.
	 * 
	 * @param extraError
	 *            zu serialisierendes Error-Objekt; darf nicht <code>null</code>
	 *            sein
	 * @param result
	 *            Ziel des Marshallings; darf nicht <code>null</code> sein
	 * 
	 * @throws JAXBException
	 *             wenn beim Marshalling eine Exception geworfen wird
	 * @throws IllegalArgumentException
	 *             wenn <code>extraError</code> oder <code>result</code> den
	 *             Wert <code>null</code> haben
	 */
	public void marshalExtraError(final ExtraErrorType extraError, final Result result) throws JAXBException {

		// Pruefe Werte, die nicht null sein duerfen
		checkNotNullableValue(extraError, "extraError");
		checkNotNullableValue(result, "result");

		final Marshaller marshaller = createMarshaller();
		marshaller.marshal(extraError, result);
	}

	/**
	 * Serialisiert das uebergebene {@link ExtraErrorType} Objekt in den
	 * uebergebenen <code>node</code>.
	 * 
	 * @param extraError
	 *            zu serialisierendes Error-Objekt; darf nicht <code>null</code>
	 *            sein
	 * @param node
	 *            Ziel des Marshallings; darf nicht <code>null</code> sein
	 * @throws JAXBException
	 *             wenn beim Marshalling eine Exception geworfen wird
	 * @throws org.w3c.dom.DOMException
	 *             wenn beim Marshalling in das <code>node</code> Objekt die
	 *             DOM-Struktur verletzt wird, z.B. falls schon Daten vorhanden
	 *             sind
	 * @throws IllegalArgumentException
	 *             wenn <code>extraError</code> oder <code>node</code> den Wert
	 *             <code>null</code> haben
	 */
	public void marshalExtraError(final ExtraErrorType extraError, final Node node) throws JAXBException {

		// Pruefe Werte, die nicht null sein duerfen
		checkNotNullableValue(extraError, "extraError");
		checkNotNullableValue(node, "node");

		final Marshaller marshaller = createMarshaller();
		marshaller.marshal(extraError, node);
	}

	/**
	 * Serialisiert das uebergebene {@link TransportRequestType} Objekt (mittels
	 * JAXB-Marshalling) in den uebergebenen <code>outputStream</code>.
	 * 
	 * @param extraRequest
	 *            zu serialisierendes Request-Objekt; darf nicht
	 *            <code>null</code> sein
	 * @param outputStream
	 *            Ziel des Marshallings; darf nicht <code>null</code> sein
	 * @throws JAXBException
	 *             wenn beim Marshalling eine Exception geworfen wird
	 * @throws IllegalArgumentException
	 *             wenn <code>extraRequest</code> oder <code>outputStream</code>
	 *             den Wert <code>null</code> haben
	 */
	public void marshalTransportRequest(final TransportRequestType extraRequest, final OutputStream outputStream)
			throws JAXBException {

		// Pruefe Werte, die nicht null sein duerfen
		checkNotNullableValue(extraRequest, "extraRequest");
		checkNotNullableValue(outputStream, "outputStream");

		final Marshaller marshaller = createMarshaller();
		marshaller.marshal(extraRequest, outputStream);
	}

	/**
	 * Serialisiert das uebergebene {@link TransportResponseType} Objekt in
	 * einen String und gibt diesen zurueck.
	 * 
	 * @param extraResponse
	 *            zu serialisierendes Objekt; darf nicht <code>null</code> sein
	 * @return String-Repraesentation von {@link TransportResponseType}
	 * @throws JAXBException
	 *             wenn beim Marshalling eine Exception geworfen wird
	 * @throws IllegalArgumentException
	 *             wenn <code>extraResponse</code> den Wert <code>null</code>
	 *             hat
	 */
	public String marshalTransportResponse(final TransportResponseType extraResponse) throws JAXBException {

		// Parameter-Validierung
		checkNotNullableValue(extraResponse, "extraResponse");

		// Wandle JAXB-Datenobjekt in eine Zeichenreihe um
		final Marshaller marshaller = createMarshaller();
		final StringWriter responseStringWriter = new StringWriter();
		marshaller.marshal(extraResponse, new StreamResult(responseStringWriter));
		return responseStringWriter.toString();
	}

	/**
	 * Serialisiert das uebergebene {@link TransportResponseType} Objekt
	 * (mittels JAXB-Marshalling) in das uebergebene <code>result</code>. <br/>
	 * Falls in <code>result</code> bereits Daten vorhanden sind, kann eine
	 * {@link RuntimeException} - abhaengig vom konkreten <code>Result</code>
	 * -Typ - geworfen werden.
	 * 
	 * @param extraResponse
	 *            zu serialisierendes Response-Objekt; darf nicht
	 *            <code>null</code> sein
	 * @param result
	 *            Ziel des Marshallings; darf nicht <code>null</code> sein
	 * @throws JAXBException
	 *             wenn beim Marshalling eine Exception geworfen wird
	 * @throws IllegalArgumentException
	 *             wenn <code>extraResponse</code> oder <code>result</code> den
	 *             Wert <code>null</code> haben
	 */
	public void marshalTransportResponse(final TransportResponseType extraResponse, final Result result)
			throws JAXBException {

		// Pruefe Werte, die nicht null sein duerfen
		checkNotNullableValue(extraResponse, "extraResponse");
		checkNotNullableValue(result, "result");

		final Marshaller marshaller = createMarshaller();
		marshaller.marshal(extraResponse, result);
	}

	/**
	 * Serialisiert das uebergebene {@link TransportResponseType} Objekt
	 * (mittels JAXB-Marshalling) in den uebergebenen <code>node</code>.
	 * 
	 * @param extraResponse
	 *            zu serialisierendes Response-Objekt; darf nicht
	 *            <code>null</code> sein
	 * @param node
	 *            Ziel des Marshallings; darf nicht <code>null</code> sein
	 * @throws JAXBException
	 *             wenn beim Marshalling eine Exception geworfen wird
	 * @throws org.w3c.dom.DOMException
	 *             wenn beim Marshalling in das <code>node</code> Objekt die
	 *             DOM-Struktur verletzt wird, z.B. falls schon Daten vorhanden
	 *             sind
	 * @throws IllegalArgumentException
	 *             wenn <code>extraResponse</code> oder <code>node</code> den
	 *             Wert <code>null</code> haben
	 */
	public void marshalTransportResponse(final TransportResponseType extraResponse, final Node node)
			throws JAXBException {

		// Pruefe Werte, die nicht null sein duerfen
		checkNotNullableValue(extraResponse, "extraResponse");
		checkNotNullableValue(node, "node");

		final Marshaller marshaller = createMarshaller();
		marshaller.marshal(extraResponse, node);
	}

	/**
	 * Erzeugt aus dem uebergebenen String ein {@link TransportRequestType}
	 * Objekt.
	 * 
	 * @param transportRequest
	 *            String-Repraesentation des eXTra-Requests
	 * @return JAXB-Repraesentation des eXTra-Requests
	 * @throws JAXBException
	 *             wenn beim Unmarshalling eine Exception geworfen wird oder das
	 *             uebergebene XML kein {@link TransportRequestType} ist.
	 * @throws IllegalArgumentException
	 *             wenn <code>transportRequest</code> den Wert <code>null</code>
	 *             hat
	 */
	public TransportRequestType unmarshalTransportRequest(final String transportRequest) throws JAXBException {

		// Parameter-Validierung
		checkNotNullableValue(transportRequest, "transportRequest");

		final Unmarshaller unmarshaller = createUnmarshaller();

		@SuppressWarnings("unchecked")
		final JAXBElement<Object> request = (JAXBElement<Object>) unmarshaller.unmarshal(new StreamSource(
				new StringReader(transportRequest)));

		if (!(request.getValue() instanceof TransportRequestType)) {
			throw new JAXBException(String.format(JAXB_EXCEPTION, request.getValue().getClass().getSimpleName(),
					"TransportRequestType"));
		}

		return (TransportRequestType) request.getValue();

	}

	/**
	 * Erzeugt aus dem uebergebenen XML ein {@link TransportRequestType} Objekt.
	 * 
	 * @param source
	 *            XML-Quelle eines eXTra-Requests; darf nicht <code>null</code>
	 *            sein
	 * @return JAXB-Repraesentation des eXTra-Requests
	 * @throws JAXBException
	 *             wenn beim Unmarshalling eine Exception geworfen wird oder das
	 *             uebergebene XML kein {@link TransportRequestType} ist.
	 * @throws IllegalArgumentException
	 *             wenn <code>source</code> den Wert <code>null</code> hat
	 */
	public TransportRequestType unmarshalTransportRequest(final Source source) throws JAXBException {

		// Pruefe Werte, die nicht null sein duerfen
		checkNotNullableValue(source, "source");

		final Unmarshaller unmarshaller = createUnmarshaller();

		@SuppressWarnings("unchecked")
		final JAXBElement<Object> request = (JAXBElement<Object>) unmarshaller.unmarshal(source);

		if (!(request.getValue() instanceof TransportRequestType)) {
			throw new JAXBException(String.format(JAXB_EXCEPTION, request.getValue().getClass().getSimpleName(),
					"TransportRequestType"));
		}

		return (TransportRequestType) request.getValue();
	}

	/**
	 * Erzeugt aus dem uebergebenen XML ein {@link TransportResponseType}
	 * Objekt. Sollte es sich dabei um {@link ExtraErrorType} handeln wird
	 * stattdessen die Exception {@link ExtraErrorResponseException} ausgeloest,
	 * die den {@link ExtraErrorType} enthaelt.
	 * 
	 * @param source
	 *            XML-Quelle einer eXTra-Response; darf nicht <code>null</code>
	 *            sein
	 * @return JAXB-Repraesentation der eXTra-Response
	 * @throws JAXBException
	 *             wenn beim Unmarshalling eine Exception geworfen wird oder das
	 *             uebergebene XML kein {@link TransportResponseType} oder
	 *             {@link ExtraErrorType} ist.
	 * @throws IllegalArgumentException
	 *             wenn <code>source</code> den Wert <code>null</code> hat
	 * @throws ExtraErrorResponseException
	 *             wenn in <code>source</code> nicht ein
	 *             {@link TransportResponseType} sondern ein
	 *             {@link ExtraErrorType} ist. Der {@link ExtraErrorType} wird
	 *             mit der Exception uebergeben.
	 */
	public TransportResponseType unmarshalTransportResponse(final Source source) throws JAXBException,
			ExtraErrorResponseException {

		// Pruefe Werte, die nicht null sein duerfen
		checkNotNullableValue(source, "source");

		final Unmarshaller unmarshaller = createUnmarshaller();

		@SuppressWarnings("unchecked")
		final JAXBElement<Object> jaxbElement = (JAXBElement<Object>) unmarshaller.unmarshal(source);

		if (jaxbElement.getValue() instanceof ExtraErrorType) {
			throw new ExtraErrorResponseException((ExtraErrorType) jaxbElement.getValue());
		} else if (!(jaxbElement.getValue() instanceof TransportResponseType)) {
			throw new JAXBException(String.format(JAXB_EXCEPTION, jaxbElement.getValue().getClass().getSimpleName(),
					"TransportResponseType"));
		}

		return (TransportResponseType) jaxbElement.getValue();
	}

	/**
	 * Erstellt einen Marshaller mit dem aus den JAXB-Objekten z.B. eine
	 * XML-Datei erzeugt werden kann. Der JAXB-Kontext besteht aus den
	 * <code>de.drv.dsrv.spoc.extra.v1_3.jaxb.*</code>-Packages.
	 * 
	 * @return {@link Marshaller}
	 * @throws JAXBException
	 *             wenn beim Erstellen des Marshallers eine Exception geworfen
	 *             wird
	 */
	public Marshaller createMarshaller() throws JAXBException {
		final Marshaller marshaller = getJaxbContext().createMarshaller();
		marshaller.setProperty(MARSHALLER_PROPERTY_NAMESPACEPREFIXMAPPER, extraNamespacePrefixMapper);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
		return marshaller;
	}

	/**
	 * Erstellt einen Unmarshaller mit dem aus einer XML-Datei JAXB-Objekte
	 * erzeugt werden koennen. Der JAXB-Kontext besteht aus den
	 * <code>de.drv.dsrv.spoc.extra.v1_3.jaxb.*</code>-Packages.
	 * 
	 * @return {@link Unmarshaller}
	 * @throws JAXBException
	 *             wenn beim Erstellen des Marshallers eine Exception geworfen
	 *             wird
	 */
	public Unmarshaller createUnmarshaller() throws JAXBException {
		return getJaxbContext().createUnmarshaller();
	}

	private void checkNotNullableValue(final Object notNullableValue, final String parameterName)
			throws IllegalArgumentException {
		if (notNullableValue == null) {
			throw new IllegalArgumentException("Parameter " + parameterName + " darf nicht null sein.");
		}
	}

	/**
	 * Erstellt einen {@link JAXBContext}, der alle JAXB-Packages kennt.
	 * 
	 * @return {@link JAXBContext}
	 * @throws JAXBException
	 */
	private JAXBContext getJaxbContext() throws JAXBException {
		final LinkedList<String> packages = new LinkedList<String>();

		packages.add(AnyPlugInContainerType.class.getPackage().getName()); // components
		packages.add(EventType.class.getPackage().getName()); // logging
		packages.add(AbstractArgumentType.class.getPackage().getName()); // messages
		packages.add(AbstractAlgorithmType.class.getPackage().getName()); // plugins
		packages.add(TransportRequestType.class.getPackage().getName()); // request
		packages.add(TransportResponseType.class.getPackage().getName()); // response
		packages.add(ExtraErrorType.class.getPackage().getName()); // service
		packages.add(CanonicalizationMethodType.class.getPackage().getName()); // xmldsig
		packages.add(AgreementMethodType.class.getPackage().getName()); // xmlend

		final StringBuilder contextPath = new StringBuilder();
		for (int i = 0; i < packages.size(); ++i) {
			contextPath.append(packages.get(i));

			if (i < packages.size()) {
				contextPath.append(":");
			}
		}

		return JAXBContext.newInstance(contextPath.toString());
	}
}
