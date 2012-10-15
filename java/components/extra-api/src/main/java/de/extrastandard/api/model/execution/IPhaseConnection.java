package de.extrastandard.api.model.execution;

public interface IPhaseConnection {

	/**
	 * @see de.extrastandard.api.model.execution.PersistentEntity#saveOrUpdate()
	 */
	public abstract void saveOrUpdate();

	/**
	 * @return the quelleInputData
	 */
	public abstract IInputData getQuelleInputData();

	/**
	 * @return the targetInputData
	 */
	public abstract IInputData getTargetInputData();

	/**
	 * @return the nextPhasequalifier
	 */
	public abstract String getNextPhasequalifier();

	/**
	 * @return the status
	 */
	public abstract IStatus getStatus();

	/**
	 * @param targetInputData
	 */
	void setTargetInputData(IInputData targetInputData);

}