/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.drv.dsrv.spoc.commons.monitor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import javax.sql.DataSource;

/**
 * Implementiert die Methoden fuer den Zugriff auf die Datenbanktabelle DFUEMAIN
 * zur Speicherung von Monitoreintraegen.
 */
public class DfueMainDaoImpl implements DfueMainDao {

	private static final String ABSENDER_LOGISCH_SPALTENNAME = "ABSP"; // NOPMD

	private static final String EMPFAENGER_LOGISCH_SPALTENNAME = "EMPP"; // NOPMD

	private static final String PROZESS_ID_SPALTENNAME = "PID"; // NOPMD

	private static final String UEBERTRAGUNGSART_SPALTENNAME = "UEART"; // NOPMD

	private static final String STARTZEIT_SPALTENNAME = "STDATI"; // NOPMD

	private static final String ZWISCHENZEIT_SPALTENNAME = "ZWDATI"; // NOPMD

	private static final String ENDEZEIT_SPALTENNAME = "EDATI"; // NOPMD

	private final transient String insertSqlTeil1;

	private final transient String insertSqlTeil2;

	private final transient String updateStatusSql;

	private final transient String updateStatusEndezeitSql; // NOPMD

	private final transient String selectSql;

	private final transient DataSource dataSource;

	/**
	 * Konstruktor.
	 * 
	 * @param dataSource
	 *            die Datenquelle zur Ermittlung der Datenbankverbindungen
	 * @param schemaName
	 *            der Name des Datenbankschemas
	 */
	public DfueMainDaoImpl(final DataSource dataSource, final String schemaName) {
		super();
		this.dataSource = dataSource;
		this.insertSqlTeil1 = "INSERT INTO " + schemaName
				+ ".DFUEMAIN (VF, UM, ABSL, EMPL, AETYP, TNR, SNR, ONR, ST)";
		this.insertSqlTeil2 = " VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		this.updateStatusSql = "UPDATE " + schemaName
				+ ".DFUEMAIN SET ST = ? WHERE  \"ID#\" = ?";
		this.updateStatusEndezeitSql = "UPDATE " + schemaName
				+ ".DFUEMAIN SET ST = ?, EDATI = ? WHERE  \"ID#\" = ?";
		this.selectSql = "SELECT \"ID#\", VF, UM, ABSL, ABSP, EMPL, EMPP, AETYP, TNR, SNR, ONR, PID, ST, STDATI, ZWDATI, EDATI, UEART FROM "
				+ schemaName + ".DFUEMAIN WHERE \"ID#\" = ?";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long insert(final DfueMain dfueMain) throws DfueMainDaoException {
		long generatedId = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// Ermittle Datenbankverbindung
			connection = this.dataSource.getConnection();

			// Ermittle Insert-Anweisung
			final String insertSql = getInsertSql(dfueMain);

			// Bereite Prepared Statement vor
			preparedStatement = connection.prepareStatement(insertSql,
					Statement.RETURN_GENERATED_KEYS);

			// Setze verpflichtende Parameter
			int parameterIndex = 1;
			preparedStatement.setString(parameterIndex++,
					dfueMain.getVerfahren());
			preparedStatement.setString(parameterIndex++,
					dfueMain.getUmgebung());
			preparedStatement.setString(parameterIndex++,
					dfueMain.getAbsenderLogisch());
			preparedStatement.setString(parameterIndex++,
					dfueMain.getEmpfaengerLogisch());
			preparedStatement.setString(parameterIndex++,
					dfueMain.getAbsenderEmpfaengerTyp());
			preparedStatement.setString(parameterIndex++,
					dfueMain.getTransfernummer());
			preparedStatement.setString(parameterIndex++,
					dfueMain.getSendungsnummer());
			preparedStatement.setString(parameterIndex++,
					dfueMain.getOrdnungsnummer());
			preparedStatement.setString(parameterIndex++, dfueMain.getStatus());

			// Setze optionale Parameter
			parameterIndex = speichereParameterFuerOptionalenWert(
					dfueMain.getAbsenderPhysikalisch(), preparedStatement,
					parameterIndex);
			parameterIndex = speichereParameterFuerOptionalenWert(
					dfueMain.getEmpfaengerPhysikalisch(), preparedStatement,
					parameterIndex);
			parameterIndex = speichereParameterFuerOptionalenWert(
					dfueMain.getProzessId(), preparedStatement, parameterIndex);
			parameterIndex = speichereParameterFuerOptionalenWert(
					dfueMain.getStartzeit(), preparedStatement, parameterIndex);
			parameterIndex = speichereParameterFuerOptionalenWert(
					dfueMain.getZwischenzeit(), preparedStatement,
					parameterIndex);
			parameterIndex = speichereParameterFuerOptionalenWert(
					dfueMain.getEndezeit(), preparedStatement, parameterIndex);
			parameterIndex = speichereParameterFuerOptionalenWert(
					dfueMain.getUebertragungsart(), preparedStatement,
					parameterIndex);

			// Fuehre Insert durch
			final int rowsInserted = preparedStatement.executeUpdate();

			// Pruefe, ob genau eine Zeile eingefuegt wurde
			if (rowsInserted != 1) {
				throw new DfueMainDaoException(
						"Fehler beim Einfuegen des Eintrags '" + dfueMain
								+ "'. Es wurden '" + rowsInserted
								+ "' Zeilen eingefuegt.");
			}

			// Ermittle generierten Primaerschluessel
			resultSet = preparedStatement.getGeneratedKeys();

			// Pruefe, ob ein Primaerschluessel generiert wurde
			if (resultSet.next()) {
				// Speichere Wert des Primaerschluessels
				generatedId = resultSet.getLong(1);
				dfueMain.setId(generatedId);
			}
		} catch (final SQLException e) {
			throw new DfueMainDaoException(
					"Fehler beim Einfuegen des Eintrags '" + dfueMain + "'",
					e);
		} finally {
			// Schliesse Ressourcen
			close(connection, preparedStatement, resultSet);
		}

		return generatedId;
	}

	/**
	 * Erstellt die Insert-Anweisung gemaess der im uebergebenen Datenobjekt
	 * gespeicherten Werte (die nicht gesetzten optionalen Werte sollen beim
	 * Insert durch die Datenbank belegt werden)
	 * 
	 * @param dfueMain
	 *            das Datenobjekt aufgrund dessen gespeicherten Werte die
	 *            Insert-Anweisung erstellt werden soll
	 * @return die Insert-Anweisung gemaess der im uebergebenen Datenobjekt
	 *         gespeicherten Werte
	 */
	private String getInsertSql(final DfueMain dfueMain) {
		// Erstelle optionale Teile der Insert-Anweisung
		final StringBuilder insertSpalten = new StringBuilder();
		final StringBuilder insertWerte = new StringBuilder();
		erweitereInsertAnweisungFuerOptionalenWert(
				dfueMain.getAbsenderPhysikalisch(), insertSpalten, insertWerte,
				ABSENDER_LOGISCH_SPALTENNAME);
		erweitereInsertAnweisungFuerOptionalenWert(
				dfueMain.getEmpfaengerPhysikalisch(), insertSpalten,
				insertWerte, EMPFAENGER_LOGISCH_SPALTENNAME);
		erweitereInsertAnweisungFuerOptionalenWert(dfueMain.getProzessId(),
				insertSpalten, insertWerte, PROZESS_ID_SPALTENNAME);
		erweitereInsertAnweisungFuerOptionalenWert(dfueMain.getStartzeit(),
				insertSpalten, insertWerte, STARTZEIT_SPALTENNAME);
		erweitereInsertAnweisungFuerOptionalenWert(dfueMain.getZwischenzeit(),
				insertSpalten, insertWerte, ZWISCHENZEIT_SPALTENNAME);
		erweitereInsertAnweisungFuerOptionalenWert(dfueMain.getEndezeit(),
				insertSpalten, insertWerte, ENDEZEIT_SPALTENNAME);
		erweitereInsertAnweisungFuerOptionalenWert(
				dfueMain.getUebertragungsart(), insertSpalten, insertWerte,
				UEBERTRAGUNGSART_SPALTENNAME);

		// Erstelle Insert-Anweisung
		final StringBuilder insertSql = new StringBuilder();
		insertSql.append(this.insertSqlTeil1.replaceFirst("\\)",
				insertSpalten.toString()));
		insertSql.append(")");
		insertSql.append(this.insertSqlTeil2.replaceFirst("\\)",
				insertWerte.toString()));
		insertSql.append(")");
		return insertSql.toString();
	}

	/**
	 * Erweitert die einzelnen Teile der Anweisung abhaengig von der Belegung
	 * eines optionalen Wertes. <br />
	 * Dazu wird zunaechst geprueft, ob der uebergebene Wert gueltig (ungleich
	 * <code>null</code> ist). Ist dies der Fall so wird der zugehoerige
	 * Spaltenname und ein zugehoeriger Parameter-Marker an der Insert-Anweisung
	 * ergaenzt.
	 * 
	 * @param wert
	 *            der optional belegte Wert
	 * @param insertSpalten
	 *            der Teil der Insert-Anweisung, welcher die Spaltennamen
	 *            enthaelt
	 * @param insertWerte
	 *            der Teil der Insert-Anweisung, welcher die Parameter-Marker
	 *            enthaelt
	 * @param spaltenname
	 *            der Name der Datenbankspalte in welchem der optionale Wert
	 *            gespeichert wird
	 */
	private void erweitereInsertAnweisungFuerOptionalenWert(final Object wert,
			final StringBuilder insertSpalten, final StringBuilder insertWerte,
			final String spaltenname) {
		// Pruefe, ob Wert gueltig ist
		if (wert != null) {
			// Erweitere Spaltennamen
			insertSpalten.append(", ");
			insertSpalten.append(spaltenname);

			// Erweitere Werte
			insertWerte.append(", ?");
		}
	}

	/**
	 * Speichert die Belegung eines optionalen Wertes als Parameter im
	 * uebergebenen Prepared Statement. <br />
	 * Dazu wird zunaechst geprueft, ob der uebergebene Wert gueltig (ungleich
	 * <code>null</code> ist). Ist dies der Fall so wird der Wert abhaengig vom
	 * Typ als Parameter mit dem angegebenen Index im uebergebenen Prepared
	 * Statement.
	 * 
	 * @param wert
	 *            der optional belegte Wert
	 * @param preparedStatement
	 *            das Prepared Statement, in welchem der uebergebene Wert belegt
	 *            werden soll
	 * @param parameterIndex
	 *            der Parameter-Index unter welchem der uebergebene Wert im
	 *            Prepared Statement gesetzt werden soll
	 * @return der Index, welcher zum Setzen des naechsten Parameters innerhalb
	 *         des Prepared Statement verwendet werden soll. Wenn der
	 *         uebergebene Wert <code>null</code> ist, dann wird der uebergebene
	 *         Parameter-Index zurueckgegeben. Andernfalls wird der uebergebene
	 *         Parameter-Index um eins erhoeht und zurueckgegeben
	 * @throws SQLException
	 *             falls beim Setzen des Wertes im Prepared Statement ein
	 *             technischer Fehler auftritt
	 */
	private int speichereParameterFuerOptionalenWert(final Object wert,
			final PreparedStatement preparedStatement, final int parameterIndex)
			throws SQLException {
		int parameterIndexNeu = parameterIndex;

		// Pruefe, ob Wert gueltig ist
		if (wert != null) {
			// Pruefe und speichere Zeichenreihe
			if (wert instanceof String) {
				preparedStatement.setString(parameterIndexNeu++, (String) wert);
			} else {
				// Pruefe und speichere Datum
				if (wert instanceof Date) {
					preparedStatement.setTimestamp(parameterIndexNeu++,
							new Timestamp(((Date) wert).getTime()));
				}
			}
		}

		// Gebe den naechsten zu verwendenden Parameter-Index zurueck
		return parameterIndexNeu;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateStatus(final String status, final long id) // NOPMD
			throws DfueMainDaoException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// Ermittle Datenbankverbindung
			connection = this.dataSource.getConnection();

			// Bereite Prepared Statement vor
			preparedStatement = connection
					.prepareStatement(this.updateStatusSql);

			// Setze Parameter
			int parameterIndex = 1;
			preparedStatement.setString(parameterIndex++, status);
			preparedStatement.setLong(parameterIndex++, id);

			// Fuehre Update durch
			final int rowsUpdated = preparedStatement.executeUpdate();

			// Pruefe, ob genau eine Zeile aktualisiert wurde
			if (rowsUpdated != 1) {
				throw new DfueMainDaoException(
						"Fehler bei Aktualisierung des Status fuer ID '"
								+ id + "' auf '" + status + "'. Es wurden '"
								+ rowsUpdated + "' Zeilen aktualisiert");
			}
		} catch (final SQLException e) {
			throw new DfueMainDaoException(
					"Fehler bei Aktualisierung des Status fuer ID '" + id
							+ "' auf '" + status + "'", e);
		} finally {
			// Schliesse Ressourcen
			close(connection, preparedStatement, null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateStatusEndezeit(final String status, final Date endezeit,
			final long id) // NOPMD
			throws DfueMainDaoException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// Ermittle Datenbankverbindung
			connection = this.dataSource.getConnection();

			// Bereite Prepared Statement vor
			preparedStatement = connection
					.prepareStatement(this.updateStatusEndezeitSql);
			// Setze Parameter
			int parameterIndex = 1;
			preparedStatement.setString(parameterIndex++, status);
			preparedStatement.setTimestamp(parameterIndex++, new Timestamp(
					endezeit.getTime()));
			preparedStatement.setLong(parameterIndex++, id);

			// Fuehre Update durch
			final int rowsUpdated = preparedStatement.executeUpdate();

			// Pruefe, ob genau eine Zeile aktualisiert wurde
			if (rowsUpdated != 1) {
				throw new DfueMainDaoException(
						"Fehler bei Aktualisierung des Status und der Endezeit fuer ID '"
								+ id + "' auf '" + status + "'. Es wurden '"
								+ rowsUpdated + "' Zeilen aktualisiert");
			}
		} catch (final SQLException e) {
			throw new DfueMainDaoException(
					"Fehler bei Aktualisierung des Status und der Endezeit fuer ID '"
							+ id + "' auf '" + status + "'", e);
		} finally {
			// Schliesse Ressourcen
			close(connection, preparedStatement, null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DfueMain get(final long id) // NOPMD
			throws DfueMainDaoException {
		DfueMain dfueMain = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// Ermittle Datenbankverbindung
			connection = this.dataSource.getConnection();

			// Bereite Prepared Statement vor
			preparedStatement = connection.prepareStatement(this.selectSql);

			// Setze Parameter
			int parameterIndex = 1;
			preparedStatement.setLong(parameterIndex++, id);

			// Fuehre Select durch
			final ResultSet resultSet = preparedStatement.executeQuery();

			// Pruefe, ob ein Primaerschluessel
			// generiert wurde
			if (resultSet.next()) {
				// Speichere ermittelte Werte
				parameterIndex = 1;
				dfueMain = new DfueMain();
				dfueMain.setId(resultSet.getLong(parameterIndex++));
				dfueMain.setVerfahren(trim(resultSet
						.getString(parameterIndex++)));
				dfueMain.setUmgebung(trim(resultSet.getString(parameterIndex++)));
				dfueMain.setAbsenderLogisch(trim(resultSet
						.getString(parameterIndex++)));
				dfueMain.setAbsenderPhysikalisch(trim(resultSet
						.getString(parameterIndex++)));
				dfueMain.setEmpfaengerLogisch(trim(resultSet
						.getString(parameterIndex++)));
				dfueMain.setEmpfaengerPhysikalisch(trim(resultSet
						.getString(parameterIndex++)));
				dfueMain.setAbsenderEmpfaengerTyp(trim(resultSet
						.getString(parameterIndex++)));
				dfueMain.setTransfernummer(trim(resultSet
						.getString(parameterIndex++)));
				dfueMain.setSendungsnummer(trim(resultSet
						.getString(parameterIndex++)));
				dfueMain.setOrdnungsnummer(trim(resultSet
						.getString(parameterIndex++)));
				dfueMain.setProzessId(trim(resultSet
						.getString(parameterIndex++)));
				dfueMain.setStatus(trim(resultSet.getString(parameterIndex++)));
				dfueMain.setStartzeit(resultSet.getTimestamp(parameterIndex++));
				dfueMain.setZwischenzeit(resultSet
						.getTimestamp(parameterIndex++));
				dfueMain.setEndezeit(resultSet.getTimestamp(parameterIndex++));
				dfueMain.setUebertragungsart(trim(resultSet
						.getString(parameterIndex++)));
			}

			// Gebe Daten zurueck
			return dfueMain;
		} catch (final SQLException e) {
			throw new DfueMainDaoException(
					"Fehler beim Ermitteln der Daten fuer ID '" + id, e);
		} finally {
			// Schliesse Ressourcen
			close(connection, preparedStatement, null);
		}
	}

	/**
	 * Prueft und schliesst die uebergebenen Datenbankressourcen.
	 * 
	 * @param connection
	 *            die zu schliessende Datenbankverbindung
	 * @param preparedStatement
	 *            das zu schliessende Prepared Statement
	 * @param resultSet
	 *            das zu schliessende Result Sert
	 * @throws DfueMainDaoException
	 *             falls beim Schliessen der Datenbankressourcen ein technischer
	 *             Fehler auftritt
	 */
	private void close(final Connection connection,
			final PreparedStatement preparedStatement, final ResultSet resultSet)
			throws DfueMainDaoException {

		try {
			// Schliesse Result Set
			if (resultSet != null) {
				resultSet.close();
			}

			// Schliesse Prepared Statement
			if (preparedStatement != null) {
				preparedStatement.close();
			}

			// Schliesse Datenbankverbindung
			if (connection != null) {
				connection.close();
			}
		} catch (final SQLException e) {
			throw new DfueMainDaoException(
					"Fehler beim Schliessen der Ressourcen", e);
		}
	}

	/**
	 * Entfernt die fuehrenden und endenden Leerzeichen der uebergebenen
	 * Zeichenreihe, falls diese gueltig (ungleich <code>null</code>) ist.
	 * 
	 * @param wert
	 *            die Zeichenreihe von welcher die fuehrenden und endenden
	 *            Leerzeichen entfernt werden sollen
	 * @return die von fuehrenden und endenden Leerzeichen bereinigte
	 *         Zeichenreihe, falls diese gueltig (ungleich <code>null</code> );
	 *         andernfalls <code>null</code>
	 */
	private String trim(final String wert) {
		return wert == null ? wert : wert.trim();
	}
}
