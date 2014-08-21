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
