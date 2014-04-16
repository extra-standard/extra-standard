package de.drv.dsrv.spoc.commons.mtomdata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.sql.DataSource;

/**
 * Implementiert die Methoden fuer den Zugriff auf die Datenbanktabelle
 * MTOM_DATA.
 */
public class MtomDataDaoImpl implements MtomDataDao {

	private static final String TABLE_NAME = "MTOM_DATA";

	private final transient String schemaName;
	private final transient DataSource dataSource;

	/**
	 * Konstruktor.
	 * 
	 * @param dataSource
	 *            die Datenquelle zur Ermittlung der Datenbankverbindungen
	 * @param schemaName
	 *            der Name des Datenbankschemas
	 */
	public MtomDataDaoImpl(final DataSource dataSource, final String schemaName) {
		this.dataSource = dataSource;
		this.schemaName = schemaName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int insert(final MtomData mtomData) throws MtomDataDaoException {
		int lastInsertedId = -1;

		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		final String insertSql = "INSERT INTO "
				+ schemaName
				+ "."
				+ TABLE_NAME
				+ "(OID, NUTZDATEN, ZEITPUNKT, EXTRA_PROCEDURE, EXTRA_DATATYPE, EXTRA_PROFILE) VALUES (?, ?, ?, ?, ?, ?)";

		try {
			con = dataSource.getConnection();
			preparedStatement = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

			int idx = 0;
			preparedStatement.setInt(++idx, mtomData.getOid());
			preparedStatement.setBinaryStream(++idx, mtomData.getNutzdaten());
			mtomData.setZeitpunkt(new Timestamp(System.currentTimeMillis()));
			preparedStatement.setTimestamp(++idx, (Timestamp) mtomData.getZeitpunkt());
			preparedStatement.setString(++idx, mtomData.getExtraProcedure());
			preparedStatement.setString(++idx, mtomData.getExtraDatatype());
			preparedStatement.setString(++idx, mtomData.getExtraProfile());

			preparedStatement.execute();

			// Lese die generierte ID aus.
			generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				lastInsertedId = generatedKeys.getInt(1);
				mtomData.setId(lastInsertedId);
			}

			preparedStatement.close();
		} catch (final SQLException e) {
			throw new MtomDataDaoException("Fehler beim Einfuegen des Eintrags '" + mtomData + "'", e);
		} finally {
			close(con, preparedStatement, generatedKeys, mtomData);
		}

		return lastInsertedId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MtomData get(final int id) throws MtomDataDaoException {
		MtomData mtomData = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;

		try {
			final String selectSql = "SELECT OID, NUTZDATEN, ZEITPUNKT, EXTRA_PROCEDURE, EXTRA_DATATYPE, EXTRA_PROFILE FROM "
					+ schemaName + "." + TABLE_NAME + " WHERE \"ID\" = ?";

			// Bereite Prepared Statement vor
			con = dataSource.getConnection();
			preparedStatement = con.prepareStatement(selectSql);

			// Setze Parameter
			int idx = 0;
			preparedStatement.setInt(++idx, id);

			// Fuehre Select durch
			final ResultSet resultSet = preparedStatement.executeQuery();

			// Pruefe, ob ein Primaerschluessel
			// generiert wurde
			if (resultSet.next()) {
				// Speichere ermittelte Werte
				idx = 0;
				mtomData = new MtomData();
				mtomData.setId(id);
				mtomData.setOid(resultSet.getInt(++idx));
				mtomData.setNutzdaten(resultSet.getBinaryStream(++idx));
				mtomData.setZeitpunkt(resultSet.getTimestamp(++idx));
				mtomData.setExtraProcedure(trim(resultSet.getString(++idx)));
				mtomData.setExtraDatatype(trim(resultSet.getString(++idx)));
				mtomData.setExtraProfile(trim(resultSet.getString(++idx)));
			}

			// Gebe Daten zurueck
			return mtomData;
		} catch (final SQLException e) {
			e.printStackTrace();
			throw new MtomDataDaoException("Fehler beim Ermitteln der Daten fuer ID '" + id, e);
		} /*
		 * finally { // Schliesse Ressourcen // close(con, preparedStatement,
		 * null, null); /* Wenn Connections geschlossen wird kommt
		 * Fehlermeldung:
		 * 
		 * Caused by: org.apache.axiom.om.OMException: java.io.IOException:
		 * [jcc][10120][11936][4.13.80] Ungültige Operation: Lob ist
		 * geschlossen. ERRORCODE=-4470, SQLSTATE=null
		 * 
		 * Treiberversion: IBM DB2 JDBC Universal Driver Architecture 3.63.75
		 * 
		 * }
		 */
	}

	/**
	 * Prueft und schliesst die uebergebenen Datenbankressourcen.
	 * 
	 * @param connection
	 *            die zu schliessende Datenbankverbindung
	 * @param preparedStatement
	 *            das zu schliessende Prepared Statement
	 * @param resultSet
	 *            das zu schliessende Result Set
	 * @param mtomData
	 *            der zu schliessende InputStream des BLOBs (im MtomData-Objekt)
	 * @throws MtomDataDaoException
	 *             falls beim Schliessen der Datenbankressourcen ein technischer
	 *             Fehler auftritt
	 */
	private void close(final Connection connection, final PreparedStatement preparedStatement,
			final ResultSet resultSet, final MtomData mtomData) throws MtomDataDaoException {

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

			// Schliesse InputStream BLOB
			if (mtomData != null && mtomData.getNutzdaten() != null) {
				mtomData.getNutzdaten().close();
			}
		} catch (final SQLException e) {
			throw new MtomDataDaoException("Fehler beim Schliessen der Ressourcen", e);
		} catch (final IOException e) {
			throw new MtomDataDaoException("Fehler beim Schliessen der Ressourcen", e);
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
