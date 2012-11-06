
package de.extra.client.core.model.inputdata.impl;

import java.util.ArrayList;
import java.util.List;

import de.extrastandard.api.model.content.IDbMaxResponseIdQueryInputData;
import de.extrastandard.api.model.content.ISingleInputData;

public class DBQueryMaxResponseIdInputData extends InputDataContainer implements
		IDbMaxResponseIdQueryInputData {

	Long maxResponseId = null;

	public DBQueryMaxResponseIdInputData() {
		super();
	}

	public DBQueryMaxResponseIdInputData(Long maxResponseId) {
		super();
		this.maxResponseId = maxResponseId;
	}

	public Long getMaxResponseId() {
		return maxResponseId;
	}

	@Override
	public boolean isContentEmpty() {
		return maxResponseId == null;
	}

	@Override
	public int getContentSize() {
		return (maxResponseId == null) ? 0 : 1;
	}

	@Override
	public List<ISingleInputData> getContent() {
		// TODO !? getContent() benoetigt?!
		final InDBQueryInputData singleDBQueryInputData = new InDBQueryInputData(
				maxResponseId, String.valueOf(maxResponseId),
				String.valueOf(maxResponseId));

		List<ISingleInputData> ret = new ArrayList<ISingleInputData>();
		ret.add(singleDBQueryInputData);
		return ret;
	}

}
