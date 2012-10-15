package de.extrastandard.api.model.execution;

public interface IProcedureType {

	/**
	 * @see de.extrastandard.api.model.execution.IProcedure#getName()
	 */
	public abstract String getName();

	/**
	 * @return the startPhase of the Procedure
	 */
	public String getStartPhase();

	/**
	 * @param phase
	 * @return true, if phase configured as a start phase of this ProcedureType
	 */
	public boolean isProcedureStartPhase(String phase);

}