package de.drv.dsrv.spoc.web.webservice.spring;

import java.io.IOException;
import java.net.URI;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.Namespace;
import org.springframework.ws.server.endpoint.annotation.Namespaces;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.server.endpoint.annotation.XPathParam;

import de.drv.dsrv.spoc.web.service.FachverfahrenRequestService;
import de.drv.dsrv.spoc.web.service.SpocBetriebsnummerService;
import de.drv.dsrv.spoc.web.service.SpocRoutingService;
import de.drv.dsrv.spoc.web.webservice.UnidentifiedFachverfahrenException;

/**
 * Web Service Endpoint fuer alle von SPoC erhaltenen eXTra-Requests.
 */
@Endpoint
public class SpocEndpoint {

	private static final Log LOG = LogFactory.getLog(SpocEndpoint.class);

	private final SpocRoutingService spocRoutingService;
	private final FachverfahrenRequestService fachverfahrenRequestService;
	private final SpocBetriebsnummerService betriebsnummerService;

	public SpocEndpoint(final SpocRoutingService spocRoutingService,
			final FachverfahrenRequestService fachverfahrenRequestService,
			final SpocBetriebsnummerService betriebsnummerService) {
		this.spocRoutingService = spocRoutingService;
		this.fachverfahrenRequestService = fachverfahrenRequestService;
		this.betriebsnummerService = betriebsnummerService;
	}

	/**
	 * Von Spring-WS aufgerufene Methode zur Verarbeitung von eXTra-Requests.
	 * <p>
	 * Identifiziert das zugeh&oumlrige Fachverfahren und leitet den
	 * eXTra-Request dorthin weiter. Wenn aus dem Original-Request eine
	 * Betriebsnummer extrahiert werden kann, wird diese als Request-Parameter
	 * an das Fachverfahren weiter gegeben.
	 * 
	 * @param version
	 *            Wert des version-Attributs im Transport-Element
	 * @param profile
	 *            Wert des profile-Attributs im Transport-Element
	 * @param procedure
	 *            Wert des Procedure-Elements im TransportHeader
	 * @param dataType
	 *            Wert des DataType-Elements im TransportHeader
	 * @param payload
	 *            der eXTra-Request mit dem Transport-Element als Wurzel
	 * 
	 * @return die vom Fachverfahren erhaltene Antwort
	 * 
	 * @throws IOException
	 *             wenn w&auml;rend der Verarbeitung des Requests ein Fehler
	 *             auftritt
	 * @throws UnidentifiedFachverfahrenException
	 *             wenn f&uumlr den Request kein Fachverfahren identifiziert
	 *             werden kann
	 */
	@Namespaces({ @Namespace(prefix = "req", uri = "http://www.extra-standard.de/namespace/request/1"),
			@Namespace(prefix = "cmp", uri = "http://www.extra-standard.de/namespace/components/1") })
	@PayloadRoot(localPart = "Transport", namespace = "http://www.extra-standard.de/namespace/request/1")
	@ResponsePayload
	public Source handleExtraRequest(@XPathParam("/req:Transport/@version") final String version,
			@XPathParam("/req:Transport/@profile") final String profile,
			@XPathParam("/req:Transport/req:TransportHeader/cmp:RequestDetails/cmp:Procedure") final String procedure,
			@XPathParam("/req:Transport/req:TransportHeader/cmp:RequestDetails/cmp:DataType") final String dataType,
			@RequestPayload final StreamSource payload) throws IOException {

		// Ermittle Fachverfahren-URL
		URI fachverfahrenUrl = this.spocRoutingService.getFachverfahrenUrl(version, profile, procedure, dataType);

		if (fachverfahrenUrl == null) {

			throw new UnidentifiedFachverfahrenException(profile, version, procedure, dataType);

		} else {

			// Versucht, die Betriebsnummer aus dem Request zu lesen und sie als
			// Parameter an den weiter geleiteten Request anzuhaengen
			fachverfahrenUrl = this.betriebsnummerService.addBetriebsnummerFromRequestToUrl(fachverfahrenUrl);

			// Leitet den Request an das Fachverfahren weiter
			if (LOG.isInfoEnabled()) {
				LOG.info("Weiterleiten des Requests an Fachverfahren >" + fachverfahrenUrl + "<.");
			}
			final Source responseSource = this.fachverfahrenRequestService.executeFachverfahrenRequest(
					fachverfahrenUrl, payload.getInputStream());
			if (LOG.isInfoEnabled()) {
				LOG.info("Antwort erhalten von Fachverfahren >" + fachverfahrenUrl + "<.");
			}

			return responseSource;
		}
	}
}
