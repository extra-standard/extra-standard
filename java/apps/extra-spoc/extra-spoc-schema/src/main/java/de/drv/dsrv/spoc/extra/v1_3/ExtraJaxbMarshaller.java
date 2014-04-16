package de.drv.dsrv.spoc.extra.v1_3;

import java.io.OutputStream;
import java.util.LinkedList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

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
import de.drv.dsrv.spoc.extra.v1_3.jaxb.service.ObjectFactory;

/**
 * Bietet Methoden zum De- und Serialisieren von Extra-Objekten an. Als
 * Parameter und Rueckgabewerte werden die aus dem Schema generierten
 * JAXB-Klassen verwendet.
 */
public class ExtraJaxbMarshaller {

	private static final String MARSHALLER_PROPERTY_NAMESPACEPREFIXMAPPER = "com.sun.xml.internal.bind.namespacePrefixMapper";
	private static final String JAXB_EXCEPTION = "Unerwartetes Element \"%s\". Das erwartete Element ist \"%s\".";

	private final ExtraNamespacePrefixMapper extraNamespacePrefixMapper;

	/**
	 * @throws JAXBException
	 *             Kopiert aus
	 *             <code>javax.xml.bind.JAXBContext.newInstance(Class...)</code>
	 *             :<br>
	 *             if an error was encountered while creating the
	 *             <code>JAXBContext</code> such as
	 *             <ol>
	 *             <li>failure to locate either ObjectFactory.class or
	 *             jaxb.index in the packages</li>
	 *             <li>an ambiguity among global elements contained in the
	 *             contextPath</li>
	 *             <li>failure to locate a value for the context factory
	 *             provider property</li>
	 *             <li>mixing schema derived packages from different providers
	 *             on the same contextPath</li>
	 *             </ol>
	 */
	public ExtraJaxbMarshaller() throws JAXBException {
		this.extraNamespacePrefixMapper = new ExtraNamespacePrefixMapper();
	}

	/**
	 * Serialisiert das &uuml;bergebene {@link ExtraErrorType} Objekt in das
	 * &uuml;bergebene <code>result</code>. <br/>
	 * Falls in <code>result</code> bereits Daten vorhanden sind, kann eine
	 * {@link RuntimeException} - abh&auml;ngig vom konkreten
	 * <code>Result</code>-Typ - geworfen werden.
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

		final JAXBElement<ExtraErrorType> errorJaxb = new ObjectFactory().createExtraError(extraError);

		final Marshaller marshaller = createMarshaller();
		marshaller.marshal(errorJaxb, result);
	}

	/**
	 * Serialisiert das &uuml;bergebene {@link ExtraErrorType} Objekt in den
	 * &uuml;bergebenen <code>node</code>.
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

		final JAXBElement<ExtraErrorType> errorJaxb = new ObjectFactory().createExtraError(extraError);

		final Marshaller marshaller = createMarshaller();
		marshaller.marshal(errorJaxb, node);
	}

	/**
	 * Serialisiert das &uuml;bergebene {@link TransportResponseType} Objekt
	 * (mittels JAXB-Marshalling) in das &uuml;bergebene <code>result</code>. <br/>
	 * Falls in <code>result</code> bereits Daten vorhanden sind, kann eine
	 * {@link RuntimeException} - abh&auml;ngig vom konkreten
	 * <code>Result</code>-Typ - geworfen werden.
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

		final JAXBElement<TransportResponseType> responseJaxb = new de.drv.dsrv.spoc.extra.v1_3.jaxb.response.ObjectFactory()
				.createTransport(extraResponse);

		final Marshaller marshaller = createMarshaller();
		marshaller.marshal(responseJaxb, result);
	}

	/**
	 * Serialisiert das &uuml;bergebene {@link TransportResponseType} Objekt
	 * (mittels JAXB-Marshalling) in den &uuml;bergebenen <code>node</code>.
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

		final JAXBElement<TransportResponseType> responseJaxb = new de.drv.dsrv.spoc.extra.v1_3.jaxb.response.ObjectFactory()
				.createTransport(extraResponse);

		final Marshaller marshaller = createMarshaller();
		marshaller.marshal(responseJaxb, node);
	}

	/**
	 * Serialisiert das &uuml;bergebene {@link TransportRequestType} Objekt
	 * (mittels JAXB-Marshalling) in den &uuml;bergebenen
	 * <code>outputStream</code>.
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

		final JAXBElement<TransportRequestType> requestJaxb = new de.drv.dsrv.spoc.extra.v1_3.jaxb.request.ObjectFactory()
				.createTransport(extraRequest);

		final Marshaller marshaller = createMarshaller();

		marshaller.marshal(requestJaxb, outputStream);
	}

	/**
	 * Erzeugt aus dem &uuml;bergebenen XML ein {@link TransportResponseType}
	 * Objekt. Sollte es sich dabei um {@link ExtraErrorType} handeln wird
	 * stattdessen die Exception {@link ExtraErrorResponseException}
	 * ausgel&ouml;st, die den {@link ExtraErrorType} enth&auml;lt.
	 * 
	 * @param source
	 *            XML-Quelle einer eXTra-Response; darf nicht <code>null</code>
	 *            sein
	 * @return JAXB-Repr&auml;sentation der eXTra-Response
	 * @throws JAXBException
	 *             wenn beim Unmarshalling eine Exception geworfen wird oder das
	 *             &uuml;bergebene XML kein {@link TransportResponseType} oder
	 *             {@link ExtraErrorType} ist.
	 * @throws IllegalArgumentException
	 *             wenn <code>source</code> den Wert <code>null</code> hat
	 * @throws ExtraErrorResponseException
	 *             wenn in <code>source</code> nicht ein
	 *             {@link TransportResponseType} sondern ein
	 *             {@link ExtraErrorType} ist. Der {@link ExtraErrorType} wird
	 *             mit der Exception &uuml;bergeben.
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
	 * Erzeugt aus dem &uuml;bergebenen XML ein {@link TransportRequestType}
	 * Objekt.
	 * 
	 * @param source
	 *            XML-Quelle eines eXTra-Requests; darf nicht <code>null</code>
	 *            sein
	 * @return JAXB-Repr&auml;sentation des eXTra-Requests
	 * @throws JAXBException
	 *             wenn beim Unmarshalling eine Exception geworfen wird oder das
	 *             &uuml;bergebene XML kein {@link TransportRequestType} ist.
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

	private Marshaller createMarshaller() throws JAXBException {
		final Marshaller marshaller = getJaxbContext().createMarshaller();
		marshaller.setProperty(MARSHALLER_PROPERTY_NAMESPACEPREFIXMAPPER, extraNamespacePrefixMapper);
		return marshaller;
	}

	private Unmarshaller createUnmarshaller() throws JAXBException {
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
