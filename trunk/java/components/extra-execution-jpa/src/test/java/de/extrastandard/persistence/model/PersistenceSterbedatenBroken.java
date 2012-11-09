package de.extrastandard.persistence.model;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Transient;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.extrastandard.api.model.execution.IExecutionPersistence;
import de.extrastandard.persistence.repository.InputDataRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-persistence-jpa.xml",
		"/spring-ittest.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class PersistenceSterbedatenBroken {
	@Inject
	@Named("persistenceSterbedatenTestSetup")
	private PersistenceSterbedatenTestSetup persistenceSterbedatenTestSetup;

	@Transient
	@Inject
	@Named("inputDataRepository")
	private transient InputDataRepository inputDataRepository;

	@Inject
	@Named("executionPersistenceJpa")
	private IExecutionPersistence executionPersistence;


	@Before
	public void before() throws Exception {
		persistenceSterbedatenTestSetup.setupProcedureSterbedatenAus1();
		persistenceSterbedatenTestSetup.setupProcedureSterbedatenAus2();
	}

}
