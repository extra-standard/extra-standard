package de.extra.client.starter;

import java.text.SimpleDateFormat;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.extrastandard.api.observer.ITransportInfo;
import de.extrastandard.api.observer.ITransportObserver;

/**
 * Betriebslogger.
 *
 * Kapselt betriebliches Logging.
 */
@Named("transportObserver")
public class OpLogger implements ITransportObserver {

	/**
	 * Standard-Format für Zeitstempel
	 */
	private static final String TIMESTAMP_FORMAT = "dd.MM.yyyy HH:mm:ss";

	/**
	 * Formatter für das Standard-Zeitstempelformat
	 */
	public static final SimpleDateFormat timestampFormat = new SimpleDateFormat(
			TIMESTAMP_FORMAT);

	/** Logger für das betriebliche Log */
	public static final Logger log = LoggerFactory.getLogger("operations");

	/**
	 * @see de.extrastandard.api.observer.ITransportObserver#requestDataReceived(java.lang.String, long)
	 */
	@Override
	public void requestDataReceived(final String unitName, final long size) {
		log.info("Request-Daten erhalten aus " + unitName + ", " + size
				+ " Bytes.");
	}

	@Override
	public void requestFilled(final ITransportInfo transportInfo) {
		log.info("Request erstellt, " + "ID = " + transportInfo.getHeaderId()
				+ "," + "Zeitstempel = " + transportInfo.getTime() + ","
				+ "Applikation = " + transportInfo.getApplication() + ", "
				+ "Prozedur = " + transportInfo.getProcedure());
	}

	@Override
	public void requestForwarded(final String destination, final long size) {
		log.info("Request weitergeleitet an " + destination + ", " + size
				+ " Bytes.");
	}

	@Override
	public void responseFilled(final ITransportInfo transportInfo) {
		log.info("Response erhalten, " + "ID = " + transportInfo.getHeaderId()
				+ "," + "Request-ID = " + transportInfo.getHeaderId() + ","
				+ "Zeitstempel = " + transportInfo.getTime() + ","
				+ "Applikation = " + transportInfo.getApplication());
		// TODO statt des betriebsspezifischen Status-Codes sollte hier etwas
		// allgemeiners als Parameter kommen
		// TODO auch bei error oder acknowledge status aggregieren
	}

	@Override
	public void responseDataForwarded(final String destination, final long size) {
		// TODO: Summe Ausgabedateien und Anzahl Datensätze einsammeln und
		// loggen?
		// OpLogger.log.info("Ausgabedateien: " + "TODO");
		// OpLogger.log.info("Anzahl bereitgestellte Saetze : " + "TODO");
		log.info("Response-Daten weitergeleitet an " + destination + ", "
				+ size + " Bytes.");
	}
}
