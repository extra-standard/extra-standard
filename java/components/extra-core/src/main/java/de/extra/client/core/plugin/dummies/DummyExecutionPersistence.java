package de.extra.client.core.plugin.dummies;

import java.util.List;

import javax.inject.Named;

import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.IExecutionPersistence;
import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.PhaseQualifier;

@Named("dummyExecutionPersistence")
public class DummyExecutionPersistence implements IExecutionPersistence {

	@Override
	public IExecution startExecution(final String procedureName, final String parameters,
			final PhaseQualifier phaseQualifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IInputData findInputDataByRequestId(final String requestId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IInputData> findInputDataForExecution(final String executionProcedure,
			final PhaseQualifier phaseQualifier) {
		// TODO Auto-generated method stub
		return null;
	}

}
