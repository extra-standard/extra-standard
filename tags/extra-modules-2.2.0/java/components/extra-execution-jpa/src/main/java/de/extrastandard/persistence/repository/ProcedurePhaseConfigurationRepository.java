package de.extrastandard.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.extrastandard.api.model.execution.IProcedureType;
import de.extrastandard.persistence.model.ProcedurePhaseConfiguration;

@Repository("procedurePhaseConfigurationRepository")
public interface ProcedurePhaseConfigurationRepository extends
		JpaRepository<ProcedurePhaseConfiguration, Long> {

	@Query("FROM ProcedurePhaseConfiguration WHERE phase = :phase and procedureType = :procedureType")
	ProcedurePhaseConfiguration findByPhaseAndProcedureType(
			@Param("phase") String phase,
			@Param("procedureType") IProcedureType procedureType);
}