package de.extra.client.core.model.inputdata.impl;

import de.extrastandard.api.model.content.IDbSingleQueryInputData;
import de.extrastandard.api.model.content.QueryArgumentType;

public class DBSingleQueryInputData implements IDbSingleQueryInputData {

	private static final String INPUT_DATA_TYPE = "DB_SINGLE_QUERY_INPUTDATA";

	private String argument;
	private QueryArgumentType queryArgumentType;

	private String requestId;
	private String inputIdentifier;

	private String procedureName;

	public DBSingleQueryInputData(String aArgument,
			QueryArgumentType queryArgumentType, String aProcedureName) {
		super();
		this.argument = aArgument;
		this.queryArgumentType = queryArgumentType;
		// TODO MAXRESP (06.11.12)
		inputIdentifier = queryArgumentType.getType() + ": " + argument;
		procedureName = aProcedureName;
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

}
