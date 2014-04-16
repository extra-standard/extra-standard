package de.drv.dsrv.spoc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import de.drv.dsrv.spoc.dto.SPoCConfigDTO;

public class SpocConfigDAOImpl implements SpocConfigDAO {

	private final JdbcTemplate jdbcTemplate;

	private final String selectSql;

	public SpocConfigDAOImpl(final DataSource dataSource, final String schemaName) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.selectSql = "SELECT EXTRA_PROCEDURE, EXTRA_DATATYPE, EXTRA_PROFILE, EXTRA_VERSION, STARTURL FROM "
				+ schemaName
				+ ".CONFIG WHERE EXTRA_PROCEDURE = ? AND EXTRA_DATATYPE = ? AND EXTRA_PROFILE = ? AND EXTRA_VERSION = ?";
	}

	@Override
	public SPoCConfigDTO selectConfig(final String prozedur, final String datentyp, final String profil,
			final String version) {

		final Object[] arguments = new Object[] { prozedur, datentyp, profil, version };
		final int[] argumentTypen = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };

		try {
			return this.jdbcTemplate.queryForObject(this.selectSql, arguments, argumentTypen, new MyRowMapper());
		} catch (final EmptyResultDataAccessException ex) {
			return null;
		}

	}

	private static final class MyRowMapper implements RowMapper<SPoCConfigDTO> {

		@Override
		public SPoCConfigDTO mapRow(final ResultSet resultSet, final int rowNum) throws SQLException {
			return new SPoCConfigDTO(resultSet.getString("EXTRA_PROCEDURE"), resultSet.getString("EXTRA_DATATYPE"),
					resultSet.getString("EXTRA_PROFILE"), resultSet.getString("EXTRA_VERSION"),
					resultSet.getString("STARTURL"));
		}
	}
}
