/**
 * 
 */
package de.extra.client.plugins.dataplugin;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import de.extra.client.core.model.inputdata.impl.DBQueryMaxResponseIdInputData;
import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.model.execution.IExecutionPersistence;
import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.api.plugin.IDataPlugin;

/**
 * @author r52gma
 * @since 1.0.0-M2
 * 
 */
public class DBMaxQueryDataPlugin implements IDataPlugin {

	@Inject
	@Named("executionPersistenceJpa")
	IExecutionPersistence executionPersistence;

	@Value("${core.execution.phase}")
	private String executionPhase;

	@Value("${core.execution.procedure}")
	private String executionProcedure;

	private static final Logger logger = LoggerFactory
			.getLogger(DBQueryDataPlugin.class);

	DBQueryMaxResponseIdInputData dbQueryMaxResponseIdInputData = null;

	/* (non-Javadoc)
	 * @see de.extrastandard.api.plugin.IDataPlugin#getData()
	 */
	@Override
	public IInputDataContainer getData() {
		if (dbQueryMaxResponseIdInputData == null) {
			synchronized (dbQueryMaxResponseIdInputData) {
				hasMoreData();
			}

		}

		return dbQueryMaxResponseIdInputData;
	}

	/* (non-Javadoc)
	 * @see de.extrastandard.api.plugin.IDataPlugin#hasMoreData()
	 */
	@Override
	public boolean hasMoreData() {
		final PhaseQualifier phaseQualifier = PhaseQualifier
				.resolveByName(executionPhase);
		Long maxResponseId = executionPersistence.maxResponseIdForExecution(
				executionProcedure, phaseQualifier);
		dbQueryMaxResponseIdInputData = new DBQueryMaxResponseIdInputData(
				maxResponseId);

		logger.info("For Procedury and Phase {} MaxResponseId: {}",
				executionProcedure + "->" + executionPhase,
				String.valueOf(maxResponseId));

		return true;
	}

	/* (non-Javadoc)
	 * @see de.extrastandard.api.plugin.IDataPlugin#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}

}
