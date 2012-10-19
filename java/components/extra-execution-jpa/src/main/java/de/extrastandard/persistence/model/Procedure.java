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

import de.extrastandard.api.model.execution.IMandator;
import de.extrastandard.api.model.execution.IProcedure;
import de.extrastandard.persistence.repository.ProcedureRepository;

/**
 * JPA Implementierung von {@link IProcedure}.
 * 
 * @author Thorsten Vogel
 * @version $Id: Procedure.java 508 2012-09-04 09:35:41Z thorstenvogel@gmail.com
 *          $
 */
@Configurable(preConstruction = true)
@Entity
@Table(name = "PROCEDURE")
public class Procedure extends AbstractEntity implements IProcedure {

	private static final long serialVersionUID = 1L;

	// private

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "procedure_entity_seq_gen")
	@SequenceGenerator(name = "procedure_entity_seq_gen", sequenceName = "seq_procedure_id")
	private Long id;

	@ManyToOne()
	@JoinColumn(name = "mandator_id")
	private Mandator mandator;

	@ManyToOne()
	@JoinColumn(name = "procedure_type_id")
	private ProcedureType procedureType;

	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@Column(name = "short_key", unique = true, nullable = false, length = 30)
	private String shortKey;

	@Transient
	@Inject
	@Named("procedureRepository")
	private transient ProcedureRepository repository;

	/**
	 * Default Empty Constructor
	 */
	public Procedure() {
		super();
	}

	/**
	 * @param mandator
	 * @param name
	 */
	public Procedure(final Mandator mandator, final ProcedureType procedureType, final String name,
			final String shortKey) {
		super();
		this.mandator = mandator;
		this.procedureType = procedureType;
		this.name = name;
		this.shortKey = shortKey;
		repository.save(this);
	}

	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IProcedure#getIMandator()
	 */
	@Override
	public IMandator getMandator() {
		return mandator;
	}

	/**
	 * @see de.extrastandard.api.model.execution.IProcedure#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	public void setMandator(final IMandator mandator) {
		this.mandator = (Mandator) mandator;
	}

	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the shortKey
	 */
	@Override
	public String getShortKey() {
		return shortKey;
	}

	/**
	 * @param shortKey
	 *            the shortKey to set
	 */
	public void setShortKey(final String shortKey) {
		this.shortKey = shortKey;
	}

	/**
	 * @return the procedureType
	 */
	@Override
	public ProcedureType getProcedureType() {
		return procedureType;
	}

	@Override
	public boolean isProcedureEndPhase(final String phase) {
		Assert.notNull(phase, "Phase must be specified");
		return procedureType.isProcedureEndPhase(phase);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Procedure [mandator=");
		builder.append(mandator);
		builder.append(", name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * @param phase
	 * @return nextPhase of the procedure, or null
	 */
	public String getNextPhase(final String phase) {
		return procedureType.getNextPhase(phase);
	}

}
