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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Configurable;

import de.extrastandard.api.model.execution.IMandator;
import de.extrastandard.persistence.repository.MandatorRepository;

/**
 * JPA Implementierung von {@link IMandator}.
 *
 * @author Thorsten Vogel
 * @version $Id$
 */
@Configurable(preConstruction=true)
@Entity
@Table(name="MANDATOR")
public class Mandator extends AbstractEntity implements IMandator {

	private static final long serialVersionUID = 1L;

	@Column(name="name")
	private String name;

	@Transient
	@Inject
	@Named("mandatorRepository")
	private transient MandatorRepository repository;

	/**
	 * @see de.extrastandard.api.model.execution.PersistentEntity#saveOrUpdate()
	 */
	@Override
	public void saveOrUpdate() {
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

}
