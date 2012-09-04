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
package de.extrastandard.persistence.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import de.extrastandard.api.model.execution.IExecution;
import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.IProcedure;
import de.extrastandard.api.model.execution.IStatus;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.persistence.repository.ExecutionRepository;
import de.extrastandard.persistence.repository.StatusRepository;

/**
 * JPA Implementierung von {@link IExecution}.
 *
 * @author Thorsten Vogel
 * @version $Id$
 */
@Configurable(preConstruction=true)
@Entity
@Table(name = "EXECUTION")
public class Execution extends AbstractEntity implements IExecution {

	private static final long serialVersionUID = 1L;

	@Column(name = "start_time")
	private Date startTime;

	@Column(name = "end_time")
	private Date endTime;

	@Column(name = "parameters")
	private String parameters;

	@ManyToOne
	@JoinColumn(name = "procedure_id")
	private Procedure procedure;

	@ManyToOne
	@JoinColumn(name = "status_id")
	private Status status;

	@OneToMany(fetch = FetchType.LAZY)
	private Set<InputData> inputDataSet = new HashSet<InputData>();

	@Inject
	@Named("executionRepository")
	@Transient
	private transient ExecutionRepository repository;

	@Inject
	@Named("statusRepository")
	@Transient
	private transient StatusRepository statusRepository;

	/**
	 *
	 */
	public Execution() {
	}

	/**
	 * Erzeugt eine neue Execution, die sogleich persistiert wird.
	 *
	 * @param parameters Parameter, mit denen die Execution gestartet wurde.
	 */
	public Execution(final String parameters) {
		this.parameters = parameters;
		this.startTime = new Date();
		this.status = statusRepository.findByName(PersistentStatus.INITIAL.name());
		saveOrUpdate();
	}

	/**
	 * @see de.extrastandard.api.model.execution.IExecution#endExecution(de.extrastandard.api.model.execution.IStatus)
	 */
	@Override
	@Transactional
	public void endExecution(final IStatus status) {
		this.endTime = new Date();
		saveOrUpdate();
	}

	/**
	 * @see de.extrastandard.api.model.execution.IExecution#startInputData(de.extrastandard.persistence.api.IInputType,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public IInputData startInputData(final String inputIdentifier,
			final String hashCode) {
		InputData inputData = new InputData(inputIdentifier, hashCode, PhaseQualifier.PHASE1.name());
		this.inputDataSet.add(inputData);
		saveOrUpdate();
		return inputData;
	}

	/**
	 * @see de.extrastandard.api.model.execution.PersistentEntity#saveOrUpdate()
	 */
	@Override
	public void saveOrUpdate() {
		repository.save(this);
	}

	/**
	 * @see de.extrastandard.api.model.execution.IExecution#getParameters()
	 */
	@Override
	public String getParameters() {
		return parameters;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IExecution#getStartTime()
	 */
	@Override
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IExecution#getProcedure()
	 */
	@Override
	public IProcedure getProcedure() {
		return procedure;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IExecution#getStatus()
	 */
	@Override
	public IStatus getStatus() {
		return status;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IExecution#getEndTime()
	 */
	@Override
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IExecution#getInputDataSet()
	 */
//	@Override
//	public Set<InputData> getInputDataSet() {
//		return inputDataSet;
//	}

	public void setStartTime(final Date startTime) {
		this.startTime = startTime;
	}

	public void setParameters(final String parameters) {
		this.parameters = parameters;
	}

	public void setProcedure(final Procedure procedure) {
		this.procedure = procedure;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}

	public void setEndTime(final Date endTime) {
		this.endTime = endTime;
	}

	public void setInputDataSet(final Set<InputData> inputDataSet) {
		this.inputDataSet = inputDataSet;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Execution [parameters=");
		builder.append(parameters);
		builder.append(", procedure=");
		builder.append(procedure);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}

}
