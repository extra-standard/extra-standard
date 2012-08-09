package de.extra.client.starter;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

public class ExtraClientConstants {

  private ExtraClientConstants () { }
  
    /** Standard-Format für Zeitstempel */
	private final static String TIMESTAMP_FORMAT = "dd.MM.yyyy HH:mm:ss";	
	/** Formatter für das Standard-Zeitstempelformat */
	public final static SimpleDateFormat timestampFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);

	/** Logger für das betriebliche Log */
	public static Logger opLogger = Logger.getLogger("operations");
}
