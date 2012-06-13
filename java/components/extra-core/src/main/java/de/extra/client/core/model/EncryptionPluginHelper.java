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
package de.extra.client.core.model;

import java.math.BigInteger;

/**
 * Helper erstellt aus Bean Objekt vom Typ AbstractPlugInType zum Einbau in den
 * Request
 */
public class EncryptionPluginHelper extends TransformHelperBase {

	@Override
	public AbstractTransformType getTransformElement(
			PlugindatenBean pluginDatenBean) {

		EncryptionPluginBean encBean = (EncryptionPluginBean) pluginDatenBean;

		EncryptionType encryption = new EncryptionType();
		EncryptionAlgorithmType encAlgo = new EncryptionAlgorithmType();
		SpecificationType encSpec = new SpecificationType();
		DataType inputData = new DataType();
		DataType outputData = new DataType();

		encryption.setOrder(BigInteger.valueOf(encBean.getOrder()));

		encAlgo.setId(encBean.getEncAlgoId());
		encAlgo.setName(encBean.getEncAlgoName());

		encSpec.setName(encBean.getEncSpecName());
		encSpec.setUrl(encBean.getEncSpecUrl());
		encSpec.setVersion(encBean.getEncSpecVers());

		inputData.setBytes(BigInteger.valueOf(encBean.getEncInput()));
		outputData.setBytes(BigInteger.valueOf(encBean.getEncOutput()));

		encryption.setInputData(inputData);
		encryption.setOutputData(outputData);

		encAlgo.setSpecification(encSpec);

		encryption.setAlgorithm(encAlgo);

		return encryption;
	}
}
