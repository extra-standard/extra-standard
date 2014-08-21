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

/**
 * Eine provisorische Lösung, um die Testdaten in die HSQLDB on StartUP
 * einzufügen. Folgende Schritte sind für den Aufbau einer HSQL-DB notwendig:
 * <ul>
 * <li>In 'src/test/resources/extra-persistence-user.properties' diesen Eintrag
 * setzen:
 * 'plugins.execution.executionPersistenceJpa.hibernate.generateDdl=true'.</li>
 * <li>Für die lokale DB muss dieser Pfad aktiv sein:
 * plugins.execution.executionPersistenceJpa
 * .database.connect_url=jdbc:hsqldb:file
 * :target/test-classes/test-hsqldb/eXTra-persistence</li>
 * <li>Für die lokale DB muss dieser Pfad aktiv sein:</li>
 * 
 * <li>In der Methode 'setupInitialDaten()' den entsprechenden Bereich zur
 * Datenerzeugung auskommentieren.</li>
 * <li>'setupInitialDaten()' als Test ausführen und anschließend den Bereich
 * wieder einkommentieren</li>
 * </ul>
 * 
 * 
 * @author Leonid Potap
 * @version $Id$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-persistence-jpa.xml",
		"/spring-ittest.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Named("initDataSetup")
public class InitDataSetup {

	private static final Logger logger = LoggerFactory
			.getLogger(InitDataSetup.class);

	@Inject
	@Named("persistenceTestSetup")
	private PersistenceTestSetup persistenceTestSetup;

	@Inject
	@Named("persistenceSterbedatenTestSetup")
	private PersistenceSterbedatenTestSetup persistenceSterbedatenTestSetup;

	@Test
	public void setupInitialDaten() throws InterruptedException {
		logger.info("setupInitialDaten");
//		persistenceTestSetup.setupInitialDaten();
//		persistenceTestSetup.setupprocedureSendFetch();
//		persistenceTestSetup.setUpTestDatenForProcedureSendFetchPhase2();
//		persistenceTestSetup.setUpTestDatenForProcedureSendFetchPhase3();
//
//		// -- Sterbedaten --
//		persistenceSterbedatenTestSetup.setupInitialDaten();
//		persistenceSterbedatenTestSetup.setupProcedureSterbedatenAus();

		Thread.sleep(3000);
		logger.info("setupInitialDaten abgeschlossen");
		Thread.sleep(3000);
	}
}
