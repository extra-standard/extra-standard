package de.extra.client.core.plugin.dummies;

import java.util.List;

import javax.inject.Named;

import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.IExecutionPersistence;
import de.extrastandard.api.model.execution.ICommunicationProtocol;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.api.model.execution.PhaseQualifier;

@Named("dummyExecutionPersistence")
public class DummyExecutionPersistence implements IExecutionPersistence {

	@Override
	public IExecution startExecution(final String procedureName,
			final String parameters, final PhaseQualifier phaseQualifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICommunicationProtocol findInputDataByRequestId(final String requestId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ICommunicationProtocol> findInputDataForExecution(
			final String executionProcedure, final PhaseQualifier phaseQualifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ICommunicationProtocol> findInputDataForExecution(
			final String executionProcedure,
			final PhaseQualifier phaseQualifier, final Integer inputDataLimit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long countInputDataForExecution(final String executionProcedure,
			final PhaseQualifier phaseQualifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String maxResponseIdForExecution(String procedureName,
			PhaseQualifier phaseQualifier, String subquery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean changeCommunicationProtocolStatusByOutputIdentifier(final String outputIdentifier, PersistentStatus persistentStatus) {
		// TODO Auto-generated method stub
		return true;
	}

}
