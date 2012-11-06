
package de.extra.client.core.model.inputdata.impl;

import java.util.ArrayList;
import java.util.List;

import de.extrastandard.api.model.content.IDbSingleQueryInputData;
import de.extrastandard.api.model.content.IDbSingleQueryInputDataContainer;
import de.extrastandard.api.model.content.ISingleInputData;
import de.extrastandard.api.model.content.QueryArgumentType;

public class DBSingleQueryInputDataContainer extends InputDataContainer implements
		IDbSingleQueryInputDataContainer {

	private final List<IDbSingleQueryInputData> iDbSingleQueryInputDataList = new ArrayList<IDbSingleQueryInputData>();

	public DBSingleQueryInputDataContainer() {
		super();
	}

	public DBSingleQueryInputDataContainer(String aArgument,
			QueryArgumentType aQueryArgumentType) {
		super();
		addDBSingleQueryInputData(aArgument, aQueryArgumentType);
	}

	public void addDBSingleQueryInputData(String aArgument,
			QueryArgumentType aQueryArgumentType) {
		final IDbSingleQueryInputData singleDBQueryInputData = new DBSingleQueryInputData(
				aArgument, aQueryArgumentType);
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
