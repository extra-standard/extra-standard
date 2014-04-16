package de.drv.dsrv.spoc.dao;

import de.drv.dsrv.spoc.dto.SPoCConfigDTO;

public interface SpocConfigDAO {

	SPoCConfigDTO selectConfig(final String prozedur, final String datentyp, final String profil,
			final String version);

}