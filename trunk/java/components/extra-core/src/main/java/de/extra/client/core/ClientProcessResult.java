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
package de.extra.client.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import de.extra.client.core.util.IExtraReturnCodeAnalyser;
import de.extrastandard.api.model.content.IInputDataContainer;
import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.model.content.ISingleInputData;
import de.extrastandard.api.model.content.ISingleResponseData;

/**
 * Beinhaltet Ergebnisse eines Laufs.
 * 
 * @author Leonid Potap
 * @version $Id$
 */
@Scope("prototype")
@Named("clientProcessResult")
public class ClientProcessResult {

    // TODO Systemunabhängigen Formatter
    private static final String NEW_LINE = "\r\n";

    @Inject
    @Named("extraReturnCodeAnalyser")
    private IExtraReturnCodeAnalyser returnCodeAnalyser;

    private final List<ProcessResult> responses = new ArrayList<ProcessResult>();

    public void addResult(final IInputDataContainer dataContainer,
	    final IResponseData responseData) {
	responses.add(new ProcessResult(responseData, dataContainer));
    }

    public void addException(final Exception exception) {
	responses.add(new ProcessResult(exception));
    }

    /**
     * Liefert true, wenn die Verarbeitung aller Dateien ohne Fehler
     * abgeschlossen wurde.
     * 
     * @return
     */
    public boolean isSuccessful() {
	boolean hasErrors = false;
	for (final ProcessResult result : responses) {
	    final IResponseData responseData = result.getResponseData();
	    if (responseData != null && responseData.getResponses() != null) {
		for (final ISingleResponseData iResponseData : responseData
			.getResponses()) {
		    if (!returnCodeAnalyser
			    .isReturnCodeSuccessful(iResponseData
				    .getReturnCode())) {
			hasErrors = true;
			// TODO refactor
			break;
		    }
		}
	    } else if (result.getException() != null) {
		// TODO possibly recoverable exceptions?
		return false;
	    }
	}
	return !hasErrors;
    }

    public boolean hasExceptions() {

	for (final ProcessResult result : responses) {
	    if (result.getException() != null) {
		return true;
	    }
	}
	return false;
    }

    private boolean isSuccessful(final ISingleResponseData iResponseData) {
	return returnCodeAnalyser.isReturnCodeSuccessful(iResponseData
		.getReturnCode());
    }

    public String exceptionsToString() {
	final StringBuilder stringBuilder = new StringBuilder();
	for (final ProcessResult result : responses) {
	    if (result.getException() != null) {
		stringBuilder.append(" Exception Message: ").append(
			result.getException().getMessage());
		stringBuilder.append(" For Results: ").append(
			result.getResponseData());

	    }
	}
	return stringBuilder.toString();
    }

    /**
     * @return Aufbereitete Liste mit ReturnCodes zu jeder empfangener Nachricht
     */
    public String printResults() {
	final StringBuilder successResultAsString = new StringBuilder();
	final StringBuilder failedResultAsString = new StringBuilder();
	int processedResultsCount = 0;
	int succesfulResultsCount = 0;
	int failedResultsCount = 0;

	for (final ProcessResult result : responses) {
	    if (result.getException() != null) {
		failedResultsCount++;
		failedResultAsString.append(" Exception: ").append(
			result.getException().getMessage());
	    } else {
		final IResponseData responseData = result.getResponseData();
		final IInputDataContainer dataContainer = result
			.getDataContainer();
		final List<ISingleInputData> inputdataContent = dataContainer
			.getContent();
		for (final ISingleInputData singleInputData : inputdataContent) {
		    processedResultsCount++;
		    final String requestId = singleInputData.getRequestId();
		    final ISingleResponseData singleResponseData = responseData
			    .getResponse(requestId);

		    final StringBuilder singleDataResult = new StringBuilder();
		    singleDataResult.append(" InputData Type : ")
			    .append(singleInputData.getInputDataType())
			    .append(" Identifier : ")
			    .append(singleInputData.getInputIdentifier());
		    singleDataResult.append(" Request : ").append(
			    singleResponseData.getRequestId());
		    singleDataResult.append(" Received Response: ").append(
			    singleResponseData.getResponseId());
		    singleDataResult.append(" With ReturnCode: ").append(
			    singleResponseData.getReturnCode());
		    singleDataResult.append(" and ReturnText: ").append(
			    singleResponseData.getReturnText());
		    singleDataResult.append(NEW_LINE);
		    if (isSuccessful(singleResponseData)) {
			succesfulResultsCount++;
			successResultAsString.append(singleDataResult);
		    } else {
			failedResultsCount++;
			failedResultAsString.append(singleDataResult);
		    }
		}
	    }
	}
	final StringBuilder resultAsString = new StringBuilder(NEW_LINE);
	resultAsString.append("Anzahl verarbeitetn Saetze : ").append(
		processedResultsCount);
	resultAsString.append(NEW_LINE);
	resultAsString.append("Davon erfolgreich : ").append(
		succesfulResultsCount);
	resultAsString.append(NEW_LINE);
	resultAsString.append("Davon fehlerhaft : ").append(failedResultsCount);
	resultAsString.append(NEW_LINE);
	resultAsString.append("Erfolgreich verarbeite Datensätze: ").append(
		NEW_LINE);
	resultAsString.append(successResultAsString);
	if (failedResultsCount != 0) {
	    resultAsString.append("Fehlerhafte Datensätze: ").append(NEW_LINE);
	    resultAsString.append(failedResultAsString);
	}
	return resultAsString.toString();
    }

    /**
     * Fügt das singleProcessResult dem ProcessResult hinzu
     * 
     * @param singleProcessResult
     */
    public void addResult(final ClientProcessResult singleProcessResult) {
	final List<ProcessResult> singleProcessResultResponses = singleProcessResult
		.getResponses();
	this.responses.addAll(singleProcessResultResponses);

    }

    /**
     * @return the responseMap
     */
    public List<ProcessResult> getResponses() {
	return Collections.unmodifiableList(responses);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	final StringBuilder builder = new StringBuilder();
	builder.append("ClientProcessResult [");
	if (responses != null) {
	    builder.append("responseMap=");
	    builder.append(responses);
	}
	builder.append("]");
	return builder.toString();
    }

}
