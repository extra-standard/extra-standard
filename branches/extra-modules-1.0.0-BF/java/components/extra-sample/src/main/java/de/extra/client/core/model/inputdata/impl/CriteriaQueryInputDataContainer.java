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

	public CriteriaQueryInputDataContainer(final String aArgument,
			final QueryArgumentType aQueryArgumentType,
			final String aProcedureName, final String aSubquery) {
		super();
		addDBSingleQueryInputData(aArgument, aQueryArgumentType,
				aProcedureName, aSubquery);
	}

	private void addDBSingleQueryInputData(final String aArgument,
			final QueryArgumentType aQueryArgumentType,
			final String aProcedureName, final String aSubquery) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CriteriaQueryInputDataContainer [");
		if (iDbSingleQueryInputDataList != null) {
			builder.append("iDbSingleQueryInputDataList=");
			builder.append(iDbSingleQueryInputDataList);
		}
		builder.append("]");
		return builder.toString();
	}

}
