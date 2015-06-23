package de.extra.extraClientLight.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import de.extra.extraClientLight.IextraClient;
import de.extra.extraClientLight.model.DataObjektBean;
import de.extra.extraClientLight.model.RequestExtraBean;
import de.extra.extraClientLight.util.SendWebService;

public class ExtraClientImplTest {

	RequestExtraBean requestBean;

	@Mock
	SendWebService sendWebService;

	@Before
	public void setUp() throws Exception {

		requestBean = new RequestExtraBean();

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

	}

	// Erfolgreich

	@Test
	public void testExtraClientImplValid() {

		IextraClient extraClient = new ExtraClientImpl();

		Assert.assertNotNull(extraClient.sendExtra(requestBean));

	}

	// Bean mit leerem Absender

	@Test
	public void testExtraClientImplInvalidAbsLeer() {

		IextraClient extraClient = new ExtraClientImpl();

		RequestExtraBean testBean = requestBean;
		testBean.setAbsender("");

		Assert.assertNotNull(extraClient.sendExtra(testBean));

	}

}
