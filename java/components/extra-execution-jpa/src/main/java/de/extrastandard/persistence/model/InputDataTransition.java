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

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;

import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.IInputDataTransition;
import de.extrastandard.api.model.execution.IStatus;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.persistence.repository.InputDataTransitionRepository;
import de.extrastandard.persistence.repository.StatusRepository;

/**
 * JPA Implementierung von {@link IInputDataTransition}.
 * 
 * @author Thorsten Vogel
 * @version $Id: InputDataTransition.java 508 2012-09-04 09:35:41Z
 *          thorstenvogel@gmail.com $
 */
@Configurable(preConstruction = true)
@Entity
@Table(name = "INPUT_DATA_TRANSITION")
public class InputDataTransition extends AbstractEntity implements IInputDataTransition {

	@Transient
	@Inject
	@Named("statusRepository")
	private transient StatusRepository statusRepository;

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "input_data_transition_entity_seq_gen")
	@SequenceGenerator(name = "input_data_transition_entity_seq_gen", sequenceName = "seq_input_data_transition_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "current_status_id")
	private Status currentStatus;

	@ManyToOne
	@JoinColumn(name = "prev_status_id")
	private Status previousStatus;

	@Column(name = "transition_date")
	private Date transitionDate;

	@Column(name = "duration")
	private Long duration;

	@ManyToOne
	@JoinColumn(name = "input_data_id")
	private InputData inputData;

	@Transient
	@Inject
	@Named("inputDataTransitionRepository")
	private transient InputDataTransitionRepository repository;

	public InputDataTransition() {
	}

	/**
	 * Erstellt eine neue Transition mit dem PreviousStatus = null und mit dem
	 * CurrentStatus (Initial)
	 * 
	 * @param inputData
	 * @param persistentStatus
	 */
	public InputDataTransition(final InputData inputData) {
		Assert.notNull(inputData, "InputData must be specified");
		final Status currentStatus = statusRepository.findByName(PersistentStatus.INITIAL.toString());
		this.currentStatus = currentStatus;
		this.transitionDate = new Date();
		this.inputData = inputData;
		repository.save(this);
	}

	/**
	 * Erstellt eine neue Transition mit den angegebenen Daten
	 * 
	 * @param inputData
	 * @param iPreviousStatus
	 * @param iCurrentStatus
	 * @param lastTransitionDate
	 */
	public InputDataTransition(final InputData inputData, final IStatus iPreviousStatus, final IStatus iCurrentStatus,
			final Date lastTransitionDate) {
		Assert.notNull(iPreviousStatus, "CurrentStatus must be specified");
		Assert.notNull(iCurrentStatus, "PreviousStatus must be specified");
		Assert.notNull(lastTransitionDate, "LastTransitionDate must be specified");
		final Status previousStatus = statusRepository.findByName(iPreviousStatus.getName());
		this.setPreviousStatus(previousStatus);
		final Status currentStatus = statusRepository.findByName(iCurrentStatus.getName());
		this.setCurrentStatus(currentStatus);
		final Date transitionDate = new Date();
		this.setTransitionDate(transitionDate);
		final long duration = transitionDate.getTime() - lastTransitionDate.getTime();
		this.setDuration(duration);
		this.inputData = inputData;
		repository.save(this);
	}

	/**
	 * @see de.extrastandard.api.model.execution.PersistentEntity#saveOrUpdate()
	 */
	@Override
	public void saveOrUpdate() {
		repository.save(this);
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputDataTransition#getCurrentStatus()
	 */
	@Override
	public IStatus getCurrentStatus() {
		return currentStatus;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputDataTransition#getPreviousStatus()
	 */
	@Override
	public IStatus getPreviousStatus() {
		return previousStatus;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputDataTransition#getTransitionDate()
	 */
	@Override
	public Date getTransitionDate() {
		return transitionDate;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputDataTransition#getDuration()
	 */
	@Override
	public Long getDuration() {
		return duration;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IInputDataTransition#getInputData()
	 */
	@Override
	public IInputData getInputData() {
		return inputData;
	}

	public void setCurrentStatus(final Status currentStatus) {
		this.currentStatus = currentStatus;
	}

	public void setPreviousStatus(final Status previousStatus) {
		this.previousStatus = previousStatus;
	}

	public void setTransitionDate(final Date transitionDate) {
		this.transitionDate = transitionDate;
	}

	public void setDuration(final Long duration) {
		this.duration = duration;
	}

	public void setInputData(final InputData inputData) {
		this.inputData = inputData;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("InputDataTransition [transitionDate=");
		builder.append(transitionDate);
		builder.append(", currentStatus=");
		builder.append(currentStatus);
		builder.append(", previousStatus=");
		builder.append(previousStatus);
		builder.append(", duration=");
		builder.append(duration);
		builder.append(", inputData=");
		builder.append(inputData);
		builder.append("]");
		return builder.toString();
	}

}
