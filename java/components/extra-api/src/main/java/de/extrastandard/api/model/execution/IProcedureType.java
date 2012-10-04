package de.extrastandard.api.model.execution;

public interface IProcedureType {

	/**
	 * @see de.extrastandard.api.model.execution.IProcedure#getName()
	 */
	public abstract String getName();

	/**
	 * @param phase
	 * @return provides end status of this phase in the scenario
	 */
	public abstract IStatus getPhaseEndStatus(PhaseQualifier phase);

	/**
	 * @param status
	 * @return true, if @ link Status} , the last in this scenario is
	 */
	public abstract boolean isProcedureEndStatus(IStatus status);

	/**
	 * @return the startPhase of the Procedure
	 */
	public String getStartPhase();

	/**
	 * @param phase
	 * @return true, if phase configured as a start phase of this ProcedureType
	 */
	public boolean isProcedureStartPhase(String phase);

	/**
	 * provides start status of this phase in the scenario
	 * 
	 * @param phase
	 */
	public IStatus getPhaseStartStatus(PhaseQualifier phase);
}