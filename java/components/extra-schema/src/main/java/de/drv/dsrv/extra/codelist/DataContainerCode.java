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
package de.drv.dsrv.extra.codelist;

/**
 * A list of extensible codes denoting a data container type. Base values: -
 * http://www.extra-standard.de/container/DATABASE -
 * http://www.extra-standard.de/container/FILE
 * 
 * @author Leonid Potap
 * @version $Id: DataContainerCode.java 1635 2013-06-19 06:44:17Z
 *          potap.rentenservice@gmail.com $
 */
public abstract class DataContainerCode {

	public static final String FILE = "http://www.extra-standard.de/container/FILE";

	public static final String DATABASE = "http://www.extra-standard.de/container/DATABASE";

}
