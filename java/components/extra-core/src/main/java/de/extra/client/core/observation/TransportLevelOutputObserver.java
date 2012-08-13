package de.extra.client.core.observation;

/**
 * Beobachter von Ereignissen der eXTRa Transportschicht in einem Client.
 * 
 * <p>Vom konkreten Ziel und Protokoll für die Nachrichten wird abstrahiert.</p>
 *
 * <p>Die erwartete Abfolge der Ereignisse ist:<code>
 *    requestCreated requestDone [responseUnmarshalled responseDone]
 * </code></p>
 * 
 * <p>Ob Response Ereignisse auftreten, hängt vom im Request spezifizierten erwarteten Antwortverhalten ab.
 * Die Response-Ereignisse <i>müssen</i> auftreten, wenn der Request eine synchrone Response angefordert hat.</p>
 * 
 * TODO: Acknowledge, Error  (extra Kompendium kap. 4.3.2)
 * TODO: Benachrichtigung über Log-Element im XMLTransport (Kap. 4.3.4)
 */
public interface TransportLevelOutputObserver {
	
	// Idee: Listen solcher Beobachter in OutputPlugins der Transportebene zur Konfigurationszeit injizieren

	/**
	 * Der Request ist als Objekt im Speicher vollständig aufgebaut.
	 * 
	 * @param requestHeader  fertig gestellter Header
	 */
	public void requestFilled(de.drv.dsrv.extrastandard.namespace.request.TransportHeader requestHeader);
		
	/**
	 * Der Request ist auf den Weg an sein Ziel an den Empfänger gebracht worden.
	 * 
	 * @param destination möglichst eindeutige, menschenlesbare Kurzbezeichnung des Ziels (hier zum Beispiel
	 *     im physikalischen Empfänger); kann (zum Beispiel, wenn er ins Dateisystem geschrieben wurde), 
	 *     muss aber nicht spezifisch pro Message sein
	 * @param size  Größe in Bytes der Transport-Message
	 */
	public void requestOutputDone(String destination, long size);
	
	/**
	 * Der Client hat eine Response empfangen als Objekt im Speicher vollständig aufgebaut.
	 * 
	 * @param status  status der Requestverarbeitung <i>auf der Transportebene</i>
	 * @param responseHeader  fertig gestellter Header
	 */
	public void responseFilled(int status, de.drv.dsrv.extrastandard.namespace.response.TransportHeader responseHeader);
	
	/**
	 * Der Client hat die Response weitergegeben an das Ziel innerhalb des Senders (des Requests).
	 * 
	 * @param destination  möglichst eindeutige, menschenlesbare Kurzbezeichnung des Ziels (hier meist zum logischen Sender)
	 *     kann (zum Beispiel, wenn sie ins Dateisystem geschrieben wurde), muss aber nicht spezifisch pro Message sein
	 * @param size  Größe in Bytes der Transport-Message
	 */
	public void responseDone(String destination, long size);
}
