package de.extrastandard.api.model.execution;

public interface IProcedureType {

	/**
	 * @see de.extrastandard.api.model.execution.IProcedure#getName()
	 */
	public abstract String getName();

	/**
	 * @param phase
	 * @return liefert end Status dieser Phase
	 */
	public abstract IStatus getPhaseEndStatus(PhaseQualifier phase);

	/**
	 * @param status
	 * @return true, wenn {@link Status} der letzte in diesem Scenario ist
	 */
	public abstract boolean isProcedureEndStatus(IStatus status);

}