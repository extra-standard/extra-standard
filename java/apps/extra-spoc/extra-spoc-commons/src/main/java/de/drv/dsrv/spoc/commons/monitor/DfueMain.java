package de.drv.dsrv.spoc.commons.monitor;

import java.io.Serializable;
import java.util.Date;

/**
 * Datenklasse, welche saemtliche Informationen zu einem Monitoreintrag in der
 * Datenbanktabelle <code>DFUEMAIN</code> beinhaltet.
 */
public class DfueMain implements Serializable { // NOPMD // SUPPRESS CHECKSTYLE MethodCount
	private static final long serialVersionUID = 1689071151553484551L;

	private long id; // NOPMD

	private String verfahren;

	private String umgebung;

	private String absenderLogisch;

	private String absenderPhysikalisch; // NOPMD

	private String empfaengerLogisch;

	private String empfaengerPhysikalisch; // NOPMD

	private String absenderEmpfaengerTyp; // NOPMD

	private String transfernummer;

	private String sendungsnummer;

	private String ordnungsnummer;

	private String status;

	private String prozessId;

	private Date startzeit;

	private Date zwischenzeit;

	private Date endezeit;

	private String uebertragungsart;

	/**
	 * Gibt die eindeutige ID des Monitoreintrags zurueck.
	 * 
	 * @return die eindeutige ID des Monitoreintrags
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Speichert die eindeutige ID des Monitoreintrags.
	 * 
	 * @param id
	 *            die zu speichernde eindeutige ID des Monitoreintrags
	 */
	public void setId(final long id) { // NOPMD
		this.id = id;
	}

	/**
	 * Gibt das Verfahren zurueck.
	 * 
	 * @return das Verfahren
	 */
	public String getVerfahren() {
		return this.verfahren;
	}

	/**
	 * Speichert das Verfahren.
	 * 
	 * @param verfahren
	 *            das Verfahren
	 */
	public void setVerfahren(final String verfahren) {
		this.verfahren = verfahren;
	}

	/**
	 * Gibt die Umgebung zurueck.
	 * 
	 * @return die Umgebung
	 */
	public String getUmgebung() {
		return this.umgebung;
	}

	/**
	 * Speichert die Umgebung.
	 * 
	 * @param umgebung
	 *            die Umgebung
	 */
	public void setUmgebung(final String umgebung) {
		this.umgebung = umgebung;
	}

	/**
	 * Gibt den logischen Absender zurueck.
	 * 
	 * @return der logische Absender
	 */
	public String getAbsenderLogisch() {
		return this.absenderLogisch;
	}

	/**
	 * Setzt den logischen Absender.
	 * 
	 * @param absenderLogisch
	 *            der logische Absender
	 */
	public void setAbsenderLogisch(final String absenderLogisch) {
		this.absenderLogisch = absenderLogisch;
	}

	/**
	 * Gibt den physikalischen Absender zurueck.
	 * 
	 * @return der physikalische Absender
	 */
	public String getAbsenderPhysikalisch() {
		return this.absenderPhysikalisch;
	}

	/**
	 * Setzt den physikalischen Absender.
	 * 
	 * @param absenderPhysikalisch
	 *            der physikalische Absender
	 */
	public void setAbsenderPhysikalisch(final String absenderPhysikalisch) { // NOPMD
		this.absenderPhysikalisch = absenderPhysikalisch;
	}

	/**
	 * Gibt den logischen Empfaenger zurueck.
	 * 
	 * @return der logische Empfaenger
	 */
	public String getEmpfaengerLogisch() {
		return this.empfaengerLogisch;
	}

	/**
	 * Setzt den logischen Empfaenger Absender.
	 * 
	 * @param empfaengerLogisch
	 *            der logische Empfaenger
	 */
	public void setEmpfaengerLogisch(final String empfaengerLogisch) {
		this.empfaengerLogisch = empfaengerLogisch;
	}

	/**
	 * Gibt den physikalischen Empfaenger zurueck.
	 * 
	 * @return der physikalische Empfaenger
	 */
	public String getEmpfaengerPhysikalisch() {
		return this.empfaengerPhysikalisch;
	}

	/**
	 * Setzt den physikalischen Empfaenger.
	 * 
	 * @param empfaengerPhysikalisch
	 *            der physikalische Empfaenger
	 */
	public void setEmpfaengerPhysikalisch(final String empfaengerPhysikalisch) { // NOPMD
		this.empfaengerPhysikalisch = empfaengerPhysikalisch;
	}

	/**
	 * Gibt den Typ des Absenders/Empfaengers, welcher angibt in welcher Form
	 * die Informationen zum Absender/Empfaenger (beispielsweise als
	 * Betriebsnummer) hinterlegt sind, zurueck.
	 * 
	 * @return der Typ des Absenders/Empfaengers
	 */
	public String getAbsenderEmpfaengerTyp() {
		return this.absenderEmpfaengerTyp;
	}

	/**
	 * Setzt den Typ des Absenders/Empfaengers, welcher angibt in welcher Form
	 * die Informationen zum Absender/Empfaenger (beispielsweise als
	 * Betriebsnummer) hinterlegt sind.
	 * 
	 * @param absenderEmpfaengerTyp
	 *            der Typ des Absenders/Empfaengers
	 */
	public void setAbsenderEmpfaengerTyp(final String absenderEmpfaengerTyp) { // NOPMD
		this.absenderEmpfaengerTyp = absenderEmpfaengerTyp;
	}

	/**
	 * Gibt die Transfernummer zurueck.
	 * 
	 * @return die Transfernummer
	 */
	public String getTransfernummer() {
		return this.transfernummer;
	}

	/**
	 * Setzt die Transfernummer.
	 * 
	 * @param transfernummer
	 *            die Transfernummer
	 */
	public void setTransfernummer(final String transfernummer) {
		this.transfernummer = transfernummer;
	}

	/**
	 * Gibt die Sendungsnummer zurueck.
	 * 
	 * @return die Sendungsnummer
	 */
	public String getSendungsnummer() {
		return this.sendungsnummer;
	}

	/**
	 * Setzt die Sendungsnummer.
	 * 
	 * @param sendungsnummer
	 *            die Sendungsnummer
	 */
	public void setSendungsnummer(final String sendungsnummer) {
		this.sendungsnummer = sendungsnummer;
	}

	/**
	 * Gibt die Ordnungsnummer zurueck.
	 * 
	 * @return die Ordnungsnummer
	 */
	public String getOrdnungsnummer() {
		return this.ordnungsnummer;
	}

	/**
	 * Setzt die Ordnungsnummer.
	 * 
	 * @param ordnungsnummer
	 *            die Ordnungsnummer
	 */
	public void setOrdnungsnummer(final String ordnungsnummer) {
		this.ordnungsnummer = ordnungsnummer;
	}

	/**
	 * Gibt die Prozess-ID zurueck.
	 * 
	 * @return die Prozess-ID
	 */
	public String getProzessId() {
		return this.prozessId;
	}

	/**
	 * Setzt die Prozess-ID.
	 * 
	 * @param prozessId
	 *            Prozess-ID
	 */
	public void setProzessId(final String prozessId) {
		this.prozessId = prozessId;
	}

	/**
	 * Gibt den Status zurueck.
	 * 
	 * @return der Status
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * Setzt den Status.
	 * 
	 * @param status
	 *            der Status
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/**
	 * Gibt die Startzeit zurueck.
	 * 
	 * @return die Startzeit
	 */
	public Date getStartzeit() {
		return this.startzeit;
	}

	/**
	 * Setzt die Startzeit.
	 * 
	 * @param startzeit
	 *            die Startzeit
	 */
	public void setStartzeit(final Date startzeit) {
		this.startzeit = startzeit;
	}

	/**
	 * Gibt die Zwischenzeit (Zeitpunkt der letzten Aktualisierung) zurueck.
	 * 
	 * @return die Zwischenzeit (Zeitpunkt der letzten Aktualisierung)
	 */
	public Date getZwischenzeit() {
		return this.zwischenzeit;
	}

	/**
	 * Setzt die Zwischenzeit (Zeitpunkt der letzten Aktualisierung).
	 * 
	 * @param zwischenzeit
	 *            die Zwischenzeit (Zeitpunkt der letzten Aktualisierung)
	 */
	public void setZwischenzeit(final Date zwischenzeit) {
		this.zwischenzeit = zwischenzeit;
	}

	/**
	 * Gibt die Endezeit zurueck.
	 * 
	 * @return die Endezeit
	 */
	public Date getEndezeit() {
		return this.endezeit;
	}

	/**
	 * Setzt die Endezeit.
	 * 
	 * @param endezeit
	 *            die Endezeit
	 */
	public void setEndezeit(final Date endezeit) {
		this.endezeit = endezeit;
	}

	/**
	 * Gibt die Uebertragungsart zurueck.
	 * 
	 * @return die Uebertragungsart
	 */
	public String getUebertragungsart() {
		return this.uebertragungsart;
	}

	/**
	 * Setzt die Uebertragungsart.
	 * 
	 * @param uebertragungsart
	 *            die Uebertragungsart
	 */
	public void setUebertragungsart(final String uebertragungsart) {
		this.uebertragungsart = uebertragungsart;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder("Verfahren: ");
		builder.append(this.verfahren);
		builder.append("\nUmgebung: ");
		builder.append(this.umgebung);
		builder.append("\nLogischer Absender: ");
		builder.append(this.absenderLogisch);
		builder.append("\nPhysikalischer Absender: ");
		builder.append(this.absenderPhysikalisch);
		builder.append("\nLogischer Empfaenger: ");
		builder.append(this.empfaengerLogisch);
		builder.append("\nPhysikalischer Empfaenger: ");
		builder.append(this.empfaengerPhysikalisch);
		builder.append("\nAbsender Typ: ");
		builder.append(this.absenderEmpfaengerTyp);
		builder.append("\nTransfernummer: ");
		builder.append(this.transfernummer);
		builder.append("\nSendungsnummer: ");
		builder.append(this.sendungsnummer);
		builder.append("\nOrdnungsnummer: ");
		builder.append(this.ordnungsnummer);
		builder.append("\nProzess-ID: ");
		builder.append(this.prozessId);
		builder.append("\nStatus: ");
		builder.append(this.status);
		builder.append("\nStartzeit: ");
		builder.append(this.startzeit);
		builder.append("\nZwischenzeit: ");
		builder.append(this.zwischenzeit);
		builder.append("\nEndezeit: ");
		builder.append(this.endezeit);
		builder.append("\nUebertragungsart: ");
		builder.append(this.uebertragungsart);
		return builder.toString();
	}
}
