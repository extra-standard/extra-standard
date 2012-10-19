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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.util.Assert;

import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.persistence.repository.ProcedureRepository;

/**
 * @author Leonid Potap
 * @version $Id$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-persistence-jpa.xml", "/spring-ittest-hsqldb-propertyplaceholder.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class ProcedureTypeIT {

	@Inject
	@Named("procedureRepository")
	private ProcedureRepository procedureRepository;

	@Test
	public void testIsProcedureEndPhase() {
		final Procedure procedureSendFetch = procedureRepository
				.findByName(PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME);
		Assert.notNull(procedureSendFetch, "Procedure not found: " + PersistenceTestSetup.PROCEDURE_DATA_MATCH_NAME);
		Assert.isTrue(procedureSendFetch.isProcedureEndPhase(PhaseQualifier.PHASE3.getName()), "Unexpected EndPhase.");
		Assert.isTrue(!procedureSendFetch.isProcedureEndPhase(PhaseQualifier.PHASE2.getName()), "Unexpected EndPhase.");
		Assert.isTrue(!procedureSendFetch.isProcedureEndPhase(PhaseQualifier.PHASE1.getName()), "Unexpected EndPhase.");
	}
}
