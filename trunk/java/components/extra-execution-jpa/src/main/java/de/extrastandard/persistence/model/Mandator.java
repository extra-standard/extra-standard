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
import javax.persistence.UniqueConstraint;

import org.springframework.beans.factory.annotation.Configurable;

import de.extrastandard.api.model.execution.IMandator;
import de.extrastandard.persistence.repository.MandatorRepository;

/**
 * JPA Implementierung von {@link IMandator}.
 * 
 * @author Thorsten Vogel
 * @version $Id: Mandator.java 508 2012-09-04 09:35:41Z thorstenvogel@gmail.com
 *          $
 */
@Configurable(preConstruction = true)
@Entity
@Table(name = "MANDATOR", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Mandator extends AbstractEntity implements IMandator {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mandator_entity_seq_gen")
	@SequenceGenerator(name = "mandator_entity_seq_gen", sequenceName = "seq_mandator_id")
	private Long id;

	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@Transient
	@Inject
	@Named("mandatorRepository")
	private transient MandatorRepository repository;

	public Mandator() {
		super();
	}

	public Mandator(final String name) {
		super();
		this.name = name;
		repository.save(this);
	}

	/**
	 * @see de.extrastandard.api.model.execution.IMandator#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public Long getId() {
		return id;
	}

}
