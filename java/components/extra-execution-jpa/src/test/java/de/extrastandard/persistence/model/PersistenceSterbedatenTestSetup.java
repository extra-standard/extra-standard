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

	static final String MANDATOR_TEST = "DRV";

	static final String PROCTYPE_STERBEDATENAUS = "STERBEDATENAUS";
	static final String PROC_STERBEDATENAUS_NAME = "Sterbedaten Ausland";
	static final String PROC_STERBEDATENAUS_KEY = "PROC_STERBEDATENAUS";

	@Inject
	@Named("mandatorRepository")
	private transient MandatorRepository mandatorRepository;

	@Transactional
	public void setupInitialDaten() {
		new Mandator(MANDATOR_TEST);

		logger.info("SetupInitialDaten finished");
	}

	@Transactional
	public void setupProcedureSterbedatenAus() {
		final ProcedureType procedureSterbedaten = new ProcedureType(
				PROCTYPE_STERBEDATENAUS);

		final ProcedurePhaseConfiguration procedurePhaseConfigurationPhase3 = new ProcedurePhaseConfiguration(
				procedureSterbedaten, PhaseQualifier.PHASE3);

		// Phase2 hat Phase3 als Nachfolger!
		new ProcedurePhaseConfiguration(
				procedureSterbedaten, PhaseQualifier.PHASE2,
				procedurePhaseConfigurationPhase3);

		// Phase 1 hat keinen Nachfolger!
		new ProcedurePhaseConfiguration(
				procedureSterbedaten,
				PhaseQualifier.PHASE1);

		final Mandator mandatorTEST = mandatorRepository
				.findByName(MANDATOR_TEST);

		new Procedure(mandatorTEST, procedureSterbedaten,
				PROC_STERBEDATENAUS_NAME, PROC_STERBEDATENAUS_KEY);

		logger.info("setupProcedureSterbedatenAusland finished");
	}
}
