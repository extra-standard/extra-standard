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

/**
 * Criteria-Querys der Form ("<xmsg:GT>10000</xmsg:GT>") werden durch dieses Interface abgebildet.
 * In dem Beispiel ist '10000' das Argument und 'GT' (Greater Then) der QueryArgumentType.
 * 
 * Querys dieser Form sollen realisiert werden:
 * 
 * <xmsg:Query>
 *     <xmsg:Argument property="http://www.extra-standard.de/property/ResponseID">
 *          <xmsg:GT>10000</xmsg:GT>
 *     </xmsg:Argument>
 *     <xmsg:Argument property="http://www.extra-standard.de/property/Procedure">
 *          <xmsg:EQ>STDAUSL</xmsg:EQ>
 *     </xmsg:Argument>
 * </xmsg:Query>
 * @author r52gma
 *
 */
public interface ICriteriaQueryInputData extends ISingleInputData {
	String getArgument();

	QueryArgumentType getQueryArgumentType();

	String getProcedureName();
}
