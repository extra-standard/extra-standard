/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.extra.client.plugins.dataplugin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import de.extra.client.core.model.inputdata.impl.DBMultiQueryInputData;
import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.model.execution.IExecutionPersistence;
import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.api.plugin.IDataPlugin;

/**
 * Depending on the transit phase and the open Scenarion evaluates data from the
 * database, and provides information for the further processing.
 * 
 * For example, for scenario SendFetch for Phase2, all input data is selected
 * whose status "RESULT_EXPECTED", for the Phase3 all input data with the status
 * "RESULT_PROCESSED"
 * 
 * @author Leonid Potap
 * @version $Id$
 */
@Named("dbQueryDataPlugin")
public class DBQueryDataPlugin implements IDataPlugin {

	@Inject
	@Named("executionPersistenceJpa")
	IExecutionPersistence executionPersistence;

	@Value("${core.execution.phase}")
	private String executionPhase;

	@Value("${core.execution.procedure}")
	private String executionProcedure;

	@Value("${plugins.dataplugin.dbQueryDataPlugin.inputDataLimit}")
	private Integer inputDataLimit;

	private static final Logger logger = LoggerFactory
			.getLogger(DBQueryDataPlugin.class);

	private List<IInputData> inputDataList;

	@Override
	public IInputDataContainer getData() {
		if (inputDataList == null) {
			synchronized (inputDataList) {
				hasMoreData();
			}

		}
		final DBMultiQueryInputData dbQueryinputData = new DBMultiQueryInputData();
		final List<IInputDataContainer> senderDataBeanList = new ArrayList<IInputDataContainer>();
		senderDataBeanList.add(dbQueryinputData);

		for (final IInputData inputData : inputDataList) {
			dbQueryinputData.addSingleDBQueryInputData(inputData.getId(),
					String.valueOf(inputData.getRequestId()),
					inputData.getResponseId());
		}
		logger.info("For Procedury and Phase {} found {} Records.",
				executionProcedure + "->" + executionPhase,
				inputDataList.size());

		return dbQueryinputData;
	}

	@Override
	public synchronized boolean hasMoreData() {
		final PhaseQualifier phaseQualifier = PhaseQualifier
				.resolveByName(executionPhase);
		inputDataList = executionPersistence.findInputDataForExecution(
				executionProcedure, phaseQualifier, inputDataLimit);
		return !inputDataList.isEmpty();
	}

	@Override
	public boolean isEmpty() {
		final PhaseQualifier phaseQualifier = PhaseQualifier
				.resolveByName(executionPhase);
		final Long countInputData = executionPersistence
				.countInputDataForExecution(executionProcedure, phaseQualifier);
		return (countInputData == 0);
	}

	/**
	 * @param executionPhase
	 *            the executionPhase to set
	 */
	public void setExecutionPhase(final String executionPhase) {
		this.executionPhase = executionPhase;
	}
}
