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
import org.springframework.util.Assert;

import de.extrastandard.api.model.execution.PhaseQualifier;
import de.extrastandard.persistence.repository.ProcedurePhaseConfigurationRepository;

/**
 * JPA Implementierung von {@link IProcedurePhaseConfiguration}. In dieser
 * Klasse werden die Statusübergänge für die unterschiedlieche Scenarien
 * festgehalten
 * 
 * @author Leonid Potap
 * @version $Id$
 */
@Configurable(preConstruction = true)
@Entity
@Table(name = "PROCEDURE_PHASE_CONFIGURATION")
public class ProcedurePhaseConfiguration extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "procedurephase_configuration_entity_seq_gen")
	@SequenceGenerator(name = "procedurephase_configuration_entity_seq_gen", sequenceName = " seq_procephase_config_id")
	private Long id;

	@ManyToOne()
	@JoinColumn(name = "procedure_type_id")
	private ProcedureType procedureType;

	@Column(name = "phase")
	private String phase;

	@ManyToOne()
	@JoinColumn(name = "next_phase_configuration_id")
	private ProcedurePhaseConfiguration nextPhaseConfiguration;

	@Transient
	@Inject
	@Named("procedurePhaseConfigurationRepository")
	private transient ProcedurePhaseConfigurationRepository repository;

	/**
	 * Default Constructor
	 */
	public ProcedurePhaseConfiguration() {
		super();
	}

	/**
	 * @param procedure
	 * @param phaseEndStatus
	 * @param phase
	 */
	public ProcedurePhaseConfiguration(final ProcedureType procedureType, final PhaseQualifier phaseQualifier) {
		this(procedureType, phaseQualifier, null);
	}

	/**
	 * @param procedureType
	 * @param phaseQualifier
	 * @param nextPhaseConfiguration
	 */
	public ProcedurePhaseConfiguration(final ProcedureType procedureType, final PhaseQualifier phaseQualifier,
			final ProcedurePhaseConfiguration nextPhaseConfiguration) {
		super();
		Assert.notNull(procedureType, "ProcedureType must be specified");
		Assert.notNull(phaseQualifier, "PhaseQualifier must be specified");
		this.procedureType = procedureType;
		this.phase = phaseQualifier.name();
		if (nextPhaseConfiguration != null) {
			this.nextPhaseConfiguration = nextPhaseConfiguration;
		}
		repository.save(this);
	}

	/**
	 * @return the id
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public void saveOrUpdate() {
		repository.save(this);

	}

	/**
	 * @return the phase
	 */
	public String getPhase() {
		return phase;
	}

	/**
	 * @param phase
	 *            the phase to set
	 */
	public void setPhase(final String phase) {
		this.phase = phase;
	}

	/**
	 * @return the procedureType
	 */
	public ProcedureType getProcedureType() {
		return procedureType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ProcedurePhaseConfiguration [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (procedureType != null) {
			builder.append("procedureType=");
			builder.append(procedureType);
			builder.append(", ");
		}
		if (phase != null) {
			builder.append("phase=");
			builder.append(phase);
		}
		if (nextPhaseConfiguration != null) {
			builder.append("nextPhaseConfiguration=");
			builder.append(nextPhaseConfiguration);
			builder.append(", ");
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * @return the nextPhaseConfiguration
	 */
	public ProcedurePhaseConfiguration getNextPhaseConfiguration() {
		return nextPhaseConfiguration;
	}

}
