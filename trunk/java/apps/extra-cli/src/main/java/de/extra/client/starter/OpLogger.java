package de.extra.client.starter;

import java.text.SimpleDateFormat;

import javax.inject.Named;

import org.apache.log4j.Logger;

import de.extra.client.core.observation.ITransportObserver;

/**
 * Betriebslogger.
 * 
 * Kapselt betriebliches Logging.
 */
@Named("transportObserver")
public class OpLogger implements ITransportObserver {

    /** Standard-Format für Zeitstempel */
	private final static String TIMESTAMP_FORMAT = "dd.MM.yyyy HH:mm:ss";	
	/** Formatter für das Standard-Zeitstempelformat */
	public final static SimpleDateFormat timestampFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);

	/** Logger für das betriebliche Log */
	public static Logger log = Logger.getLogger("operations");

	/** exit-status OK */
	public static int STATUS_OK = 0;
	/** exit-status Warnung */
	public static int STATUS_WARN = 16;
	/** exit-status betrieblicher bzw. technischer bzw. Transportfehler */
	public static int STATUS_OPERATIONAL_ERROR = 32;
	/** exit-status fachlicher bzw. logischer Fehler */
	public static int STATUS_LOGICAL_ERROR = 64;
	
	/** aggregierter exit-status */
	static int exitStatus = STATUS_OK;

	/**
	 * Aggregierter Exit-Status des Clients.
	 * 
	 * @return exit-status
	 */
	public int getExitStatus() {
		return exitStatus;
	}
	
	@Override
	public void requestDataReceived(String unitName, long size) {
		log.info("Request-Daten erhalten aus " + unitName + ", " + size + " Bytes.");
	}

	@Override
	public void requestFilled(de.drv.dsrv.extrastandard.namespace.request.TransportHeader requestHeader) {
		log.info("Request erstellt, " +
				"ID = " + requestHeader.getRequestDetails().getRequestID().getValue() + "," +
				"Zeitstempel = " + requestHeader.getRequestDetails().getTimeStamp() + "," +
				"Applikation = " + requestHeader.getRequestDetails().getApplication() + ", " +
				"Prozedur = " + requestHeader.getRequestDetails().getProcedure());
	}

	@Override
	public void requestForwarded(String destination, long size) {
		log.info("Request weitergeleitet an " + destination + ", " + size + " Bytes.");
	}

	@Override
	public void responseFilled(
			int status,
			de.drv.dsrv.extrastandard.namespace.response.TransportHeader responseHeader) {
		log.info("Response erhalten, " +
				"ID = " + responseHeader.getResponseDetails().getResponseID().getValue() + "," +
				"Request-ID = " + responseHeader.getRequestDetails().getRequestID().getValue() + "," +
				"Zeitstempel = " + responseHeader.getResponseDetails().getTimeStamp() + "," +
				"Applikation = " + responseHeader.getResponseDetails().getApplication());
		// TODO statt des betriebsspezifischen Status-Codes sollte hier etwas allgemeiners als Parameter kommen
		// TODO auch bei error oder acknowledge status aggregieren
		exitStatus |= status;
	}

	@Override
	public void responseDataForwarded(String destination, long size) {
		log.info("Response-Daten weitergeleitet an " + destination + ", " + size + " Bytes.");
	}
}
