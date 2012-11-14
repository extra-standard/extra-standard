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
package de.extrastandard.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.IProcedure;
import de.extrastandard.persistence.model.InputData;
import de.extrastandard.persistence.model.Status;

/**
 * Repository für InputData.
 * 
 * @author Thorsten Vogel
 * @version $Id: InputDataRepository.java 508 2012-09-04 09:35:41Z
 *          thorstenvogel@gmail.com $
 */
@Repository("inputDataRepository")
public interface InputDataRepository extends JpaRepository<InputData, Long> {

	@Query("FROM InputData WHERE requestId = :requestId")
	InputData findByRequestId(@Param("requestId") String requestId);

	@Query("select inputdata FROM InputData inputdata "
			+ " WHERE inputdata.nextPhaseConnection.nextPhasequalifier = :phaseQualifier "
			+ " and inputdata.execution.procedure = :procedure "
			+ " and inputdata.nextPhaseConnection.status = :status")
	List<IInputData> findByProcedureAndPhaseQualifierAndStatus(
			@Param("procedure") IProcedure procedure,
			@Param("phaseQualifier") String phaseQualifier,
			@Param("status") Status status, Pageable pageRequest);

	@Query("select count(*) FROM InputData inputdata "
			+ " WHERE inputdata.nextPhaseConnection.nextPhasequalifier = :phaseQualifier "
			+ " and inputdata.execution.procedure = :procedure "
			+ " and inputdata.nextPhaseConnection.status = :status")
	Long count(@Param("procedure") IProcedure procedure,
			@Param("phaseQualifier") String phaseQualifier,
			@Param("status") Status status);

//	select max (INPUT_DATA.RESPONSE_ID) from INPUT_DATA , PROCEDURE, EXECUTION
//	where INPUT_DATA.EXECUTION_ID = EXECUTION.ID
//	and EXECUTION.PROCEDURE_ID = PROCEDURE.ID
//	and EXECUTION.PHASE = 'SterbedatenabgleichDataFetchPhase1'
//	and PROCEDURE.NAME = 'SterbedatenabgleichDataFetch'
	
	// TODO Reicht das?? Status??
	// Oder Minimum mit Status Offen??
	
	// FIXME inp.ResponseId ist kein numerisches Feld!
	// Vorerst inp.Id statt inp.responseId genommen
	/**
	 * Für die übergebene Procedure und Phase wird die maximale Response-ID
	 * eines Input_Data Elements ermittelt.
	 * 
	 * @param procedure
	 * @param phaseQualifier
	 * @return
	 */
	@Query("select max(cast (inp.responseId as int)) from InputData inp "
			+ " WHERE inp.execution.procedure =:procedure "
			+ " and inp.execution.phase = :phaseQualifier ")
	Integer maxResponseIdForProcedureAndPhase(
			@Param("procedure") IProcedure procedure,
			@Param("phaseQualifier") String phaseQualifier);

}
