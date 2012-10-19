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

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Configurable;

import de.extrastandard.api.model.execution.IInputData;
import de.extrastandard.api.model.execution.IPhaseConnection;
import de.extrastandard.api.model.execution.IStatus;
import de.extrastandard.api.model.execution.PersistentStatus;
import de.extrastandard.persistence.repository.InputDataRepository;
import de.extrastandard.persistence.repository.PhaseConnectionRepository;
import de.extrastandard.persistence.repository.StatusRepository;

/**
 * JPA Implementierung von {@link IPhasenDataConnection}.
 * 
 * @author Leonid Potap
 * @version $Id$
 */
/**
 * @author Leonid Potap
 * @version $Id$
 */
@Configurable(preConstruction = true)
@Entity
@Table(name = "PHASE_CONNECTION")
public class PhaseConnection extends AbstractEntity implements IPhaseConnection {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phase_connection_entity_seq_gen")
	@SequenceGenerator(name = "phase_connection_entity_seq_gen", sequenceName = "seq_phase_connection_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quelle_data_id")
	private InputData quelleInputData;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "target_data_id")
	private InputData targetInputData;

	@Column(name = "next_phase_qualifier", nullable = false)
	private String nextPhasequalifier;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "status_id")
	private Status status;

	@Transient
	@Inject
	@Named("phaseConnectionRepository")
	private transient PhaseConnectionRepository repository;

	@Transient
	@Inject
	@Named("inputDataRepository")
	private transient InputDataRepository inputDataRepository;

	@Inject
	@Named("statusRepository")
	@Transient
	private transient StatusRepository statusRepository;

	/**
	 * Default
	 */
	public PhaseConnection() {
	}

	public PhaseConnection(final InputData quelleInputData,
			final String nextPhasenQualifier) {
		super();
		this.quelleInputData = quelleInputData;
		this.nextPhasequalifier = nextPhasenQualifier;
		final Status persistentStatusInitial = statusRepository
				.findOne(PersistentStatus.INITIAL.getId());
		this.status = persistentStatusInitial;
		repository.save(this);
		quelleInputData.setNextPhaseConnection(this);
	}

	@Override
	public Long getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.extrastandard.persistence.model.IPhaseConnection#getQuelleInputData()
	 */
	@Override
	public IInputData getQuelleInputData() {
		return quelleInputData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.extrastandard.persistence.model.IPhaseConnection#getTargetInputData()
	 */
	@Override
	public IInputData getTargetInputData() {
		return targetInputData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.extrastandard.persistence.model.IPhaseConnection#getNextPhasequalifier
	 * ()
	 */
	@Override
	public String getNextPhasequalifier() {
		return nextPhasequalifier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extrastandard.persistence.model.IPhaseConnection#getStatus()
	 */
	@Override
	public IStatus getStatus() {
		return status;
	}

	/**
	 * @param targetInputData
	 *            the targetInputData to set
	 */
	@Override
	public void setTargetInputData(final IInputData iTargetInputData) {
		final InputData targetInputData = inputDataRepository
				.findOne(iTargetInputData.getId());
		this.targetInputData = targetInputData;
		repository.save(this);
	}

	/**
	 * Markiert PhaseConnction as abgearbeitet. Status is DONE
	 */
	public void success() {
		final Status statusDone = statusRepository
				.findOne(PersistentStatus.DONE.getId());
		this.status = statusDone;
		repository.save(this);
	}

	public void setFailed() {
		final Status statusFailed = statusRepository
				.findOne(PersistentStatus.FAIL.getId());
		this.status = statusFailed;
		repository.save(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("PhaseConnection [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (quelleInputData != null) {
			builder.append("quelleInputData=");
			builder.append(quelleInputData);
			builder.append(", ");
		}
		if (targetInputData != null) {
			builder.append("targetInputData=");
			builder.append(targetInputData);
			builder.append(", ");
		}
		if (nextPhasequalifier != null) {
			builder.append("nextPhasequalifier=");
			builder.append(nextPhasequalifier);
			builder.append(", ");
		}
		if (status != null) {
			builder.append("status=");
			builder.append(status);
		}
		builder.append("]");
		return builder.toString();
	}

}
