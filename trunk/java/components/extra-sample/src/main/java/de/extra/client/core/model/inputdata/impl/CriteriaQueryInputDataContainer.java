package de.extra.client.core.model.inputdata.impl;

import java.util.ArrayList;
import java.util.List;

import de.extrastandard.api.model.content.ICriteriaQueryInputData;
import de.extrastandard.api.model.content.ICriteriaQueryInputDataContainer;
import de.extrastandard.api.model.content.ISingleInputData;
import de.extrastandard.api.model.content.QueryArgumentType;

public class CriteriaQueryInputDataContainer extends InputDataContainer
		implements ICriteriaQueryInputDataContainer {

	private final List<ICriteriaQueryInputData> iDbSingleQueryInputDataList = new ArrayList<ICriteriaQueryInputData>();

	public CriteriaQueryInputDataContainer() {
		super();
	}

	public CriteriaQueryInputDataContainer(String aArgument,
			QueryArgumentType aQueryArgumentType, String aProcedureName, String aSubquery) {
		super();
		addDBSingleQueryInputData(aArgument, aQueryArgumentType, aProcedureName, aSubquery);
	}

	private void addDBSingleQueryInputData(String aArgument,
			QueryArgumentType aQueryArgumentType, String aProcedureName, String aSubquery) {
		final ICriteriaQueryInputData singleDBQueryInputData = new CriteriaQueryInputData(
				aArgument, aQueryArgumentType, aProcedureName, aSubquery);
		iDbSingleQueryInputDataList.add(singleDBQueryInputData);
	}

	@Override
	public boolean isContentEmpty() {
		return iDbSingleQueryInputDataList.isEmpty();
	}

	@Override
	public int getContentSize() {
		return iDbSingleQueryInputDataList.size();
	}

	@Override
	public List<ISingleInputData> getContent() {
		final List<ISingleInputData> content = new ArrayList<ISingleInputData>(
				iDbSingleQueryInputDataList);
		return content;
	}

}
