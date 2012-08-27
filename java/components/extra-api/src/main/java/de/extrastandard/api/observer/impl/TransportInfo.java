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
package de.extrastandard.api.observer.impl;

import java.util.Calendar;

import de.extrastandard.api.observer.ITransportInfo;

/**
 * @author Leonid Potap
 * @version $Id$
 */
public class TransportInfo implements ITransportInfo {

	private String headerId;

	private Calendar time;

	private String procedure;

	private String application;

	/**
	 * Default Full Constructor
	 * 
	 * @param headerId
	 * @param time
	 * @param procedure
	 * @param application
	 */
	public TransportInfo(String headerId, Calendar time, String procedure,
			String application) {
		super();
		this.headerId = headerId;
		this.time = time;
		this.procedure = procedure;
		this.application = application;
	}

	/**
	 * Leere Constructor
	 */
	public TransportInfo() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extrastandard.api.observer.ITransportInfo#getHeaderId()
	 */
	@Override
	public String getHeaderId() {
		return headerId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extrastandard.api.observer.ITransportInfo#getTime()
	 */
	@Override
	public Calendar getTime() {
		return time;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extrastandard.api.observer.ITransportInfo#getProcedure()
	 */
	@Override
	public String getProcedure() {
		return procedure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.extrastandard.api.observer.ITransportInfo#getApplication()
	 */
	@Override
	public String getApplication() {
		return application;
	}

	/**
	 * @param headerId
	 *            the headerId to set
	 */
	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(Calendar time) {
		this.time = time;
	}

	/**
	 * @param procedure
	 *            the procedure to set
	 */
	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	/**
	 * @param application
	 *            the application to set
	 */
	public void setApplication(String application) {
		this.application = application;
	}

}
