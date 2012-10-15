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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.extrastandard.api.model.execution.IProcedure;
import de.extrastandard.api.model.execution.IProcedureType;
import de.extrastandard.persistence.repository.ProcedurePhaseConfigurationRepository;
import de.extrastandard.persistence.repository.ProcedureTypeRepository;

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

	@Column(name = "end_phase")
	private String endPhase;

	@Transient
	@Inject
	@Named("procedureTypeRepository")
	private transient ProcedureTypeRepository repository;

	@Transient
	@Inject
	@Named("procedurePhaseConfigurationRepository")
	private transient ProcedurePhaseConfigurationRepository procedurePhaseConfigurationRepository;

	/**
	 * Default Empty Constructor
	 */
	public ProcedureType() {
		super();
	}

	/**
	 * @param name
	 * @param mandator
	 */
	public ProcedureType(final String name, final String startPhase, final String endPhase) {
		super();
		this.name = name;
		this.startPhase = startPhase;
		this.endPhase = endPhase;
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

	@Override
	public boolean isProcedureStartPhase(final String phase) {
		Assert.notNull(phase, "Phase must be specified");
		final boolean isStartPhaseOfProcedure = phase.equalsIgnoreCase(this.startPhase);
		return isStartPhaseOfProcedure;
	}

	public boolean isProcedureEndPhase(final String phase) {
		Assert.notNull(phase, "Phase must be specified");
		final boolean isEndPhaseOfProcedure = phase.equalsIgnoreCase(this.endPhase);
		return isEndPhaseOfProcedure;
	}

	public String getNextPhase(final String phase) {
		Assert.notNull(phase, "Phase must be specified");
		final ProcedurePhaseConfiguration procedurePhaseConfiguration = procedurePhaseConfigurationRepository
				.findByPhaseAndProcedureType(phase, this);
		final ProcedurePhaseConfiguration nextPhaseConfiguration = procedurePhaseConfiguration
				.getNextPhaseConfiguration();
		String nextPhase = null;
		if (nextPhaseConfiguration != null) {
			nextPhase = nextPhaseConfiguration.getPhase();
		}
		return nextPhase;
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
		if (endPhase != null) {
			builder.append("endPhase=");
			builder.append(endPhase);
		}
		builder.append("]");
		return builder.toString();
	}

}
