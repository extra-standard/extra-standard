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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * Eine provisorische Lösung, um die Testdaten in die HSQLDB on StartUP
 * hinzufügen
 * 
 * @author Leonid Potap
 * @version $Id$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-persistence-jpa.xml", "/spring-ittest-hsqldb-propertyplaceholder.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Named("initDataSetup")
public class InitDataSetup {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitDataSetup.class);

	@Inject
	@Named("persistenceTestSetup")
	private PersistenceTestSetup persistenceTestSetup;

	@Test
	@Transactional
	public void setupInitialDatenTest() {
		LOGGER.info("setupInitialDaten");
		// persistenceTestSetup.setupInitialDaten();
		// persistenceTestSetup.setupProcedureSendFeths();
		persistenceTestSetup.setUpTestDatenForProcedureSendFetchPhase2();
		persistenceTestSetup.setUpTestDatenForProcedureSendFetchPhase3();
	}

}
