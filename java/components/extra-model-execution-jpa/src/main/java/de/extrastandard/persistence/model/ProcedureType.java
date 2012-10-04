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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.extrastandard.api.model.execution.IProcedure;
import de.extrastandard.api.model.execution.IProcedureType;
import de.extrastandard.api.model.execution.IStatus;
import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.persistence.repository.ProcedurePhaseConfigurationRepository;
import de.extrastandard.persistence.repository.ProcedureTypeRepository;
import de.extrastandard.persistence.repository.StatusRepository;

/**
 * JPA Implementierung von {@link IProcedure}.
 * 
 * @author Leonid Potap
 * @version $Id$
 */
@Configurable(preConstruction = true)
@Entity
@Table(name = "PROCEDURE_TYPE")
public class ProcedureType extends AbstractEntity implements IProcedureType {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "procedure_type_entity_seq_gen")
	@SequenceGenerator(name = "procedure_type_entity_seq_gen", sequenceName = "seq_procedure_type_id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "start_phase")
	private String startPhase;

	@ManyToOne
	@JoinColumn(name = "end_status_id")
	private Status endStatus;

	@Transient
	@Inject
	@Named("procedureTypeRepository")
	private transient ProcedureTypeRepository repository;

	@Transient
	@Inject
	@Named("procedurePhaseConfigurationRepository")
	private transient ProcedurePhaseConfigurationRepository procedurePhaseConfigurationRepository;

	@Transient
	@Inject
	@Named("statusRepository")
	private transient StatusRepository statusRepository;

	/**
	 * Default Empty Constructor
	 */
	public ProcedureType() {
		super();
	}

	/**
	 * @param mandator
	 * @param name
	 */
	public ProcedureType(final String name, final IStatus endStatus, final String startPhase) {
		super();
		this.name = name;
		this.startPhase = startPhase;
		this.endStatus = statusRepository.findByName(endStatus.getName());
		repository.save(this);
	}

	@Override
	public Long getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extrastandard.persistence.model.IProcedureType#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return the startPhase
	 */
	@Override
	public String getStartPhase() {
		return startPhase;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.extrastandard.persistence.model.IProcedureType#getPhaseEndStatus(de
	 * .extrastandard.api.model.execution.PhaseQualifier)
	 */
	@Override
	public IStatus getPhaseEndStatus(final PhaseQualifier phase) {
		Assert.notNull(phase, "PhaseQualifier must be specified");
		final ProcedurePhaseConfiguration configuration = procedurePhaseConfigurationRepository
				.findByPhaseAndProcedureType(phase.toString(), this);
		final Status phaseEndStatus = configuration.getPhaseEndStatus();
		return phaseEndStatus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.extrastandard.persistence.model.IProcedureType#isProcedureEndStatus
	 * (de.extrastandard.api.model.execution.IStatus)
	 */
	@Override
	public boolean isProcedureEndStatus(final IStatus status) {
		Assert.notNull(status, "Status must be specified");
		return this.endStatus.getName().equals(status.getName());
	}

	@Override
	public boolean isProcedureStartPhase(final String phase) {
		Assert.notNull(phase, "Phase must be specified");
		final boolean isStartPhaseOfProcedure = phase.equalsIgnoreCase(this.startPhase);
		return isStartPhaseOfProcedure;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IProcedureType#getPhaseStartStatus(de.extrastandard.api.model.execution.PhaseQualifier)
	 */
	@Override
	public IStatus getPhaseStartStatus(final PhaseQualifier phase) {
		Assert.notNull(phase, "Phase must be specified");
		final ProcedurePhaseConfiguration findByPhaseAndProcedureType = procedurePhaseConfigurationRepository
				.findByPhaseAndProcedureType(phase.getName(), this);
		final Status phaseStartStatus = findByPhaseAndProcedureType.getPhaseStartStatus();
		return phaseStartStatus;

	}

	/**
	 * @see de.extrastandard.api.model.execution.PersistentEntity#saveOrUpdate()
	 */
	@Override
	@Transactional
	public void saveOrUpdate() {
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
		builder.append("ProcedureType [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		if (endStatus != null) {
			builder.append("endStatus=");
			builder.append(endStatus);
		}
		builder.append("]");
		return builder.toString();
	}

}
