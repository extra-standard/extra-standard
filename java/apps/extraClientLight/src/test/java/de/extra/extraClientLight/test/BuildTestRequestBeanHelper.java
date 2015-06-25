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

package de.extra.extraClientLight.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import de.extra.extraClientLight.model.DataObjektBean;
import de.extra.extraClientLight.model.RequestExtraBean;

public class BuildTestRequestBeanHelper {

	public static RequestExtraBean buildRequestInlineBean(){
		
		RequestExtraBean requestBean = new RequestExtraBean();

		requestBean.setUrl("http://localhost:8088/mockExtraBinding");

		requestBean.setMtom(false);
		requestBean.setAbsender("test-Sender");
		requestBean.setEmpfaenger("66667777");
		requestBean.setFachschluessel("47-test-11");
		requestBean
				.setFachdienst("http://www.extra-standard.de/datatypes/DummyData");
		requestBean
				.setVerfahren("http://www.extra-standard.de/procedures/Dummy");
		requestBean.setSynchron(false);

		requestBean
				.setProfile("http://www.extra-standard.de/profile/dummy/1.3");

		DataObjektBean dataObjekt = new DataObjektBean();

		InputStream is = new ByteArrayInputStream(new String(
				"<test>äsgeks</test>").getBytes());

		dataObjekt.setData(is);
		dataObjekt.setQuery(false);
		requestBean.setDataObjekt(dataObjekt);
		
		return requestBean;
		
	}
	
public static RequestExtraBean buildRequestMtomBean(){
		
		RequestExtraBean requestBean = new RequestExtraBean();

		requestBean.setUrl("http://localhost:8088/mockExtraBinding");

		requestBean.setMtom(true);
		requestBean.setAbsender("test-Sender");
		requestBean.setEmpfaenger("66667777");
		requestBean.setFachschluessel("47-test-11");
		requestBean
				.setFachdienst("http://www.extra-standard.de/datatypes/DummyData");
		requestBean
				.setVerfahren("http://www.extra-standard.de/procedures/Dummy");
		requestBean.setSynchron(false);

		requestBean
				.setProfile("http://www.extra-standard.de/profile/dummy/1.3");

		DataObjektBean dataObjekt = new DataObjektBean();

		InputStream is = new ByteArrayInputStream(new String(
				"<test>äsgeks</test>").getBytes());

		dataObjekt.setData(is);
		dataObjekt.setQuery(false);
		requestBean.setDataObjekt(dataObjekt);
		
		return requestBean;
		
	}

public static RequestExtraBean buildQueryBean(){
	
	RequestExtraBean requestBean = new RequestExtraBean();

	// requestBean.setUrl("https://www.eservicet-drv.de/SPoC/ExtraService");
	requestBean.setUrl("https://www.eservicet-drv.de/SPoC/execute");
	requestBean.setMtom(false);
	requestBean.setAbsender("test-Sender");
	requestBean.setEmpfaenger("66667777");
	requestBean.setFachschluessel("47-test-11");
	requestBean
			.setFachdienst("http://www.extra-standard.de/datatypes/DummyData");
	requestBean
			.setVerfahren("http://www.extra-standard.de/procedures/DummyZ");
	requestBean.setSynchron(false);
	requestBean.setMtom(false);
	requestBean
			.setProfile("http://www.extra-standard.de/profile/dummy/1.3");

	DataObjektBean dataObjekt = new DataObjektBean();

	dataObjekt.setQuery(true);
	dataObjekt
			.setQueryDataType("http://www.extra-standard/query/RequestId");
	dataObjekt
			.setQueryProcedure("http://www.extra-standard/procedures/Dummy");
	dataObjekt.setQueryId("1234");
	requestBean.setDataObjekt(dataObjekt);
	
	return requestBean;
	
}
	
}
