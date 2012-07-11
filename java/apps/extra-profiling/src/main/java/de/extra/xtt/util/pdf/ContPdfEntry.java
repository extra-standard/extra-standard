package de.extra.xtt.util.pdf;

/**
 * Diese Klasse ist ein Container für einen Eintrag im PDF-Dokument, der im Inhaltsverzeichnis inkl. Seitennummer und
 * Referenz erscheinen soll.
 * 
 * @author Beier
 * 
 */
public class ContPdfEntry {

	private final ContPdfEntry parentEntry;
	private final String bezeichnung;
	private final String destination;
	private int pageNumber;

	/**
	 * Konstruktor zu Initialisierung des Dateninhalts.
	 * 
	 * @param parentEntry
	 *            Übergeordneter Eintrag (optional)
	 * @param bezeichnung
	 *            Bezeichnung des Eintrags
	 * @param destination
	 *            Bezeichnung der Referenz
	 * @param pageNumber
	 *            Seitennummer des Eintrags
	 */
	public ContPdfEntry(ContPdfEntry parentEntry, String bezeichnung, String destination, int pageNumber) {
		this.parentEntry = parentEntry;
		this.bezeichnung = bezeichnung;
		this.destination = destination;
		this.pageNumber = pageNumber;
	}

	/**
	 * Gibt den übergeordneten Eintrag des aktuellen Eintrags zurück
	 * 
	 * @return übergeordneter Eintrag, falls vorhanden; sonst <code>null</code>
	 */
	public ContPdfEntry getParentEntry() {
		return parentEntry;
	}

	/**
	 * Gibt die Bezeichnung des Eintrags zurück
	 * 
	 * @return Bezeichnung
	 */
	public String getBezeichnung() {
		return bezeichnung;
	}

	/**
	 * Gibt diie Referenz des Eintrags zurück
	 * 
	 * @return Referenz im PDF-Dokument
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * Setzt die Seitenzahl für den Eintrag
	 * 
	 * @param pageNumber
	 *            Seitenzahl des Eintrags im PDF-Dokument
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * Gitb die Seitenzahl des Eintrags zurück
	 * 
	 * @return Seitenzahl im PDF-Dokument
	 */
	public int getPageNumber() {
		return pageNumber;
	}
}
