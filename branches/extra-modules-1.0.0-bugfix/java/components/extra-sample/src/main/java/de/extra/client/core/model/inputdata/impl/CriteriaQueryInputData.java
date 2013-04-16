package de.extra.client.core.model.inputdata.impl;

import de.extrastandard.api.model.content.ICriteriaQueryInputData;
import de.extrastandard.api.model.content.QueryArgumentType;

public class CriteriaQueryInputData implements ICriteriaQueryInputData {

	private static final String INPUT_DATA_TYPE = "DB_SINGLE_QUERY_INPUTDATA";

	private final String argument;
	private final QueryArgumentType queryArgumentType;

	private String requestId;
	private final String inputIdentifier;

	private final String procedureName;

	private final String subquery;

	public CriteriaQueryInputData(final String aArgument,
			final QueryArgumentType queryArgumentType,
			final String aProcedureName, final String subquery) {
		super();
		this.argument = aArgument;
		this.queryArgumentType = queryArgumentType;
		this.inputIdentifier = queryArgumentType.getType() + ": " + argument;
		this.procedureName = aProcedureName;
		this.subquery = subquery;
	}

	@Override
	public void setRequestId(final String aRequestId) {
		requestId = aRequestId;
	}

	@Override
	public String getRequestId() {
		return requestId;
	}

	@Override
	public String getInputIdentifier() {
		return inputIdentifier;
	}

	@Override
	public String getInputDataType() {
		return INPUT_DATA_TYPE;
	}

	@Override
	public String getArgument() {
		return argument;
	}

	@Override
	public QueryArgumentType getQueryArgumentType() {
		return queryArgumentType;
	}

	@Override
	public String getProcedureName() {
		return procedureName;
	}

	@Override
	public String getSubquery() {
		return subquery;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CriteriaQueryInputData [");
		if (argument != null) {
			builder.append("argument=");
			builder.append(argument);
			builder.append(", ");
		}
		if (queryArgumentType != null) {
			builder.append("queryArgumentType=");
			builder.append(queryArgumentType);
			builder.append(", ");
		}
		if (requestId != null) {
			builder.append("requestId=");
			builder.append(requestId);
			builder.append(", ");
		}
		if (procedureName != null) {
			builder.append("procedureName=");
			builder.append(procedureName);
			builder.append(", ");
		}
		if (subquery != null) {
			builder.append("subquery=");
			builder.append(subquery);
		}
		builder.append("]");
		return builder.toString();
	}

}
