package de.extra.client.core.model.inputdata.impl;

import de.extrastandard.api.model.content.ICriteriaQueryInputData;
import de.extrastandard.api.model.content.QueryArgumentType;

public class CriteriaQueryInputData implements ICriteriaQueryInputData {

	private static final String INPUT_DATA_TYPE = "DB_SINGLE_QUERY_INPUTDATA";

	private String argument;
	private QueryArgumentType queryArgumentType;

	private String requestId;
	private String inputIdentifier;

	private String procedureName;

	private String subquery;

	public CriteriaQueryInputData(String aArgument,
			QueryArgumentType queryArgumentType, String aProcedureName, String subquery) {
		super();
		this.argument = aArgument;
		this.queryArgumentType = queryArgumentType;
		this.inputIdentifier = queryArgumentType.getType() + ": " + argument;
		this.procedureName = aProcedureName;
		this.subquery = subquery;
	}

	@Override
	public void setRequestId(String aRequestId) {
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

}
