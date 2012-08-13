package de.extra.client.core.observation;

/**
 * Beobachter der Verarbeitung von Eingangsdaten.
 * 
 * <p>Der Beobachter wird über wichtige Ereignisse in der Verarbeitung von Eingangsdaten (Data) informiert.
 * Von der konkreten Quelle der Eingangsdaten (zum Beispiel Dateisystemverzeichnis) wird abstrahiert.
 * Es wird lediglich angenommen, dass es einen Verarbeitunsbeginn und -ende gibt, zwischen denen
 * die Verarbeitung von (beliebig vielen, auch null) "Dateneinheiten" liegen kann.</p>
 * 
 * <p>Über den Zusammenhang zwischen Dateneinheiten und Requests der Transportebene wird nichts angenommen,
 * sie kann n-m sein, verzögert oder während der Verarbeitung erfolgen.</p>
 * 
 * <p>Die generell erwartete Abfolge von Ereignissen ist:<code>
 * beginOfDataProcessing (beginOfUnitOfData endOfUnitOfData)* endOfDataProcessing
 * </code>
 * </p>
 */
public interface DataObserver {
	
	 // Idee: Listen solcher Beobachter in DataPlugins zur Konfigurationszeit injizieren

	 /**
	 * Beginn eines Verarbeitungsprozesses.
	 * 
	 * @param origin möglichts eindeutige und menschenlesbare Kurzbezeichnung der Datenquelle (zum Beispiel Pfad in einem Dateisystem)
	 */
	public void beginOfDataProcessing(String origin);
	
	/**
	 * Beginn des Empfangs einer Einheit Daten.
	 * 
	 * @param unitName  möglichts eindeutige und menschenlesbare Kurzbezeichnung der Dateneinheit (zum Beispiel Dateiname)
	 */
	public void beginOfUnitOfData(String unitName);
	
	/**
	 * Ende des Empfangs einer Einheit Daten.
	 * 
	 * @param unitName  möglichts eindeutige und menschenlesbare Kurzbezeichnung der Dateneinheit (zum Beispiel Dateiname),
	 *    <i>muss</i> gleich dem zu Beginn verwendeten sein
	 */
	public void endOfUnitOfData(String unitName, long size);
	
	/**
	 * Ende eines Verarbeitungsprozesses.
	 * 
	 * @param origin möglichts eindeutige und menschenlesbare Kurzbezeichnung der Datenquelle (zum Beispiel Pfad in einem Dateisystem),
	 *    <i>muss</i> gleich dem zu Beginn verwendeten sein
	 */
	public void endOfDataProcessing(String origin);
}
