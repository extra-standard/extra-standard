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

import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.IInputDataTransition;
import de.extrastandard.api.model.execution.IStatus;
import de.extrastandard.persistence.repository.InputDataTransitionRepository;

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

	@Column(name = "qualifier")
	private String qualifier;

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
	 * @see de.extrastandard.api.model.execution.IInputDataTransition#getQualifier()
	 */
	@Override
	public String getQualifier() {
		return qualifier;
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

	public void setQualifier(final String qualifier) {
		this.qualifier = qualifier;
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
		builder.append(", qualifier=");
		builder.append(qualifier);
		builder.append(", inputData=");
		builder.append(inputData);
		builder.append("]");
		return builder.toString();
	}

}
