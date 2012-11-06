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
package de.extrastandard.persistence.model;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.persistence.repository.MandatorRepository;

/**
 * Initiale Datenbefüllung für das Fachverfahren Sterbedatenabgleich
 * 
 * @author Michael Werner
 * @version $Id$
 */
@Named("persistenceSterbedatenTestSetup")
public class PersistenceSterbedatenTestSetup {

	private static final Logger logger = LoggerFactory
			.getLogger(PersistenceSterbedatenTestSetup.class);

	private static final String MANDATOR_TEST = "DRV";

	private static final String PROCTYPE_STERBEDATENAUS1_SHORTKEY = "STERBEDATENAUS1";
	private static final String PROC_STERBEDATENAUS1_NAME = "Sterbedaten Ausland 1";
	private static final String PROC_STERBEDATENAUS1_KEY = "PROC_STERBEDATENAUS1";

	private static final String PROCTYPE_STERBEDATENAUS2 = "STERBEDATENAUS2";
	private static final String PROC_STERBEDATENAUS2_NAME = "Sterbedaten Ausland 2";
	private static final String PROC_STERBEDATENAUS2_KEY = "PROC_STERBEDATENAUS2";

	@Inject
	@Named("mandatorRepository")
	private transient MandatorRepository mandatorRepository;

	// @Inject
	// @Named("executionPersistenceJpa")
	// private ExecutionPersistenceJpa executionPersistence;

	@Transactional
	public void setupProcedureSterbedatenAus1() {
		final ProcedureType procedureSterbedaten1 = new ProcedureType(
				PROCTYPE_STERBEDATENAUS1_SHORTKEY);

		new ProcedurePhaseConfiguration(procedureSterbedaten1,
				PhaseQualifier.PHASE1);

		final Mandator mandatorTEST = mandatorRepository
				.findByName(MANDATOR_TEST);

		new Procedure(mandatorTEST, procedureSterbedaten1,
				PROC_STERBEDATENAUS1_NAME, PROC_STERBEDATENAUS1_KEY);

		logger.info("setupProcedureSterbedatenAus1 finished");
	}

	@Transactional
	public void setupProcedureSterbedatenAus2() {
		final ProcedureType procedureSterbedaten2 = new ProcedureType(
				PROCTYPE_STERBEDATENAUS2);

		final ProcedurePhaseConfiguration procedurePhaseConfigurationPhase2 = new ProcedurePhaseConfiguration(
				procedureSterbedaten2, PhaseQualifier.PHASE2);

		new ProcedurePhaseConfiguration(procedureSterbedaten2,
				PhaseQualifier.PHASE1, procedurePhaseConfigurationPhase2);

		final Mandator mandatorTEST = mandatorRepository
				.findByName(MANDATOR_TEST);

		new Procedure(mandatorTEST, procedureSterbedaten2,
				PROC_STERBEDATENAUS2_NAME, PROC_STERBEDATENAUS2_KEY);

		logger.info("setupProcedureSterbedatenAus2 finished");
	}

	// TODO Testdaten
}
