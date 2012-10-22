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
package de.extrastandard.api.model.content;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public interface ISingleContentInputData extends ISingleInputData {

    /**
     * Liefert Daten Verschlüsselung, Transformation usw Beschreibung.
     * 
     * @return
     */
    List<IInputDataPluginDescription> getPlugins();

    /**
     * @return inputData as Stream
     */
    InputStream getInputDataAsStream();

    /**
     * Default Encoding UTF-8
     * 
     * @return
     */
    String getInputDataAsString();

    /**
     * Reads the contents of InputData into a String.
     * 
     * @param encoding
     *            the encoding to use
     * @return
     */
    String getInputDataAsString(Charset encoding);

    /**
     * Reads the contents of InputData into a byte array.
     * 
     * @return
     */
    byte[] getInputDataAsByteArray();

    /**
     * @return HashCode returns if available. If there is no hashCode returns
     *         null
     */
    String getHashCode();

}
