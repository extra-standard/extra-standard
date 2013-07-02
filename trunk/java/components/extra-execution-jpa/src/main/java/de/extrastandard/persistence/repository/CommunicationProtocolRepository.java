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

import de.extrastandard.api.model.execution.ICommunicationProtocol;
import de.extrastandard.api.model.execution.IProcedure;
import de.extrastandard.persistence.model.CommunicationProtocol;
import de.extrastandard.persistence.model.Status;

/**
 * Repository für CommunicationProtocol.
 * 
 * @author Thorsten Vogel
 * @version $Id: CommunicationProtocolRepository.java 508 2012-09-04 09:35:41Z
 *          thorstenvogel@gmail.com $
 */
@Repository("communicationProtocolRepository")
public interface CommunicationProtocolRepository extends
		JpaRepository<CommunicationProtocol, Long> {

	@Query("FROM CommunicationProtocol WHERE requestId = :requestId")
	CommunicationProtocol findByRequestId(@Param("requestId") String requestId);

	@Query("FROM CommunicationProtocol communicationProtocol WHERE responseId = :responseId and communicationProtocol.execution.procedure =:procedure"
			+ " and communicationProtocol.execution.phase = :phaseQualifier ")
	CommunicationProtocol findByResponseIdAndPhaseAndProcedure(
			@Param("responseId") String responseId,
			@Param("procedure") IProcedure procedure,
			@Param("phaseQualifier") String phaseQualifier);

	@Query("select communicationProtocol FROM CommunicationProtocol communicationProtocol "
			+ " WHERE communicationProtocol.nextPhaseConnection.nextPhasequalifier = :phaseQualifier "
			+ " and communicationProtocol.execution.procedure = :procedure "
			+ " and communicationProtocol.nextPhaseConnection.status = :status")
	List<ICommunicationProtocol> findByProcedureAndPhaseQualifierAndStatus(
			@Param("procedure") IProcedure procedure,
			@Param("phaseQualifier") String phaseQualifier,
			@Param("status") Status status, Pageable pageRequest);

	@Query("select communicationProtocol FROM CommunicationProtocol communicationProtocol "
			+ " WHERE communicationProtocol.nextPhaseConnection.nextPhasequalifier = :phaseQualifier "
			+ " and communicationProtocol.execution.procedure = :procedure "
			+ " and communicationProtocol.nextPhaseConnection.status = :status"
			+ " and communicationProtocol.status = :comProtStatus")
	List<ICommunicationProtocol> findByProcedureAndPhaseQualifierAndStatusAndComProtStatus(
			@Param("procedure") IProcedure procedure,
			@Param("phaseQualifier") String phaseQualifier,
			@Param("status") Status status,
			@Param("comProtStatus") Status comProtStatus, Pageable pageRequest);

	@Query("select count(*) FROM CommunicationProtocol communicationProtocol "
			+ " WHERE communicationProtocol.nextPhaseConnection.nextPhasequalifier = :phaseQualifier "
			+ " and communicationProtocol.execution.procedure = :procedure "
			+ " and communicationProtocol.nextPhaseConnection.status = :status")
	Long count(@Param("procedure") IProcedure procedure,
			@Param("phaseQualifier") String phaseQualifier,
			@Param("status") Status status);

	/**
	 * Für die übergebene Procedure und Phase wird die maximale Response-ID
	 * eines CommunicationProtocol Elements ermittelt.
	 * 
	 * @param procedure
	 * @param phaseQualifier
	 * @return
	 */
	@Query("select max(cast (inp.responseId as int)) from CommunicationProtocol inp "
			+ " WHERE inp.execution.procedure =:procedure "
			+ " and inp.execution.phase = :phaseQualifier ")
	Integer maxResponseIdForProcedureAndPhase(
			@Param("procedure") IProcedure procedure,
			@Param("phaseQualifier") String phaseQualifier);

	/**
	 * Für die übergebene Procedure, Phase und Subquery wird die maximale
	 * Response-ID eines CommunicationProtocol Elements ermittelt. Wird z.B.
	 * benoetigt um fuer ein Land (= Subquery) die zuletzt erhaltene
	 * Server-ResponseID abzufragen.
	 * 
	 * @param procedure
	 * @param phaseQualifier
	 * @param subquery
	 * @return
	 */
	@Query("select max(cast (inp.responseId as int)) from CommunicationProtocol inp "
			+ " WHERE inp.execution.procedure =:procedure "
			+ " and inp.execution.phase = :phaseQualifier "
			+ " and inp.subquery = :subquery ")
	Integer maxResponseIdForProcedureAndPhaseAndSubquery(
			@Param("procedure") IProcedure procedure,
			@Param("phaseQualifier") String phaseQualifier,
			@Param("subquery") String subquery);

	/**
	 * Für die übergebene Procedure und Phase wird die maximale Response-ID auf
	 * Bais eines String (funktioniert nur für ein festdefinierter String
	 * Format) eines CommunicationProtocol Elements ermittelt.
	 * 
	 * @param procedure
	 * @param phaseQualifier
	 * @return
	 */
	@Query("select max(inp.responseId) from CommunicationProtocol inp "
			+ " WHERE inp.execution.procedure =:procedure "
			+ " and inp.execution.phase = :phaseQualifier ")
	String maxStringResponseIdForProcedureAndPhase(
			@Param("procedure") IProcedure procedure,
			@Param("phaseQualifier") String phaseQualifier);

	/**
	 * Für die übergebene Procedure, Phase und Subquery wird die maximale
	 * Response-ID auf Bais eines String (funktioniert nur für ein
	 * festdefinierter String Format) eines CommunicationProtocol Elements
	 * ermittelt. Wird z.B. benoetigt um fuer ein Land (= Subquery) die zuletzt
	 * erhaltene Server-ResponseID abzufragen.
	 * 
	 * @param procedure
	 * @param phaseQualifier
	 * @param subquery
	 * @return
	 */
	@Query("select max(inp.responseId) from CommunicationProtocol inp "
			+ " WHERE inp.execution.procedure =:procedure "
			+ " and inp.execution.phase = :phaseQualifier "
			+ " and inp.subquery = :subquery ")
	String maxStringResponseIdForProcedureAndPhaseAndSubquery(
			@Param("procedure") IProcedure procedure,
			@Param("phaseQualifier") String phaseQualifier,
			@Param("subquery") String subquery);

	/**
	 * Sucht für einen OutputIdentifier das zugeordnete CommunicationProtocol.
	 * Diese Methode wird verwendet, wenn eine externe Anwendung eine
	 * OutputDatei (= OutputIdentifier) bestätigen möchte.
	 * 
	 * @param outputIdentifier
	 * @return
	 */
	@Query("FROM CommunicationProtocol WHERE outputIdentifier = :outputIdentifier")
	CommunicationProtocol findByOutputIdentifier(
			@Param("outputIdentifier") String outputIdentifier);

}
