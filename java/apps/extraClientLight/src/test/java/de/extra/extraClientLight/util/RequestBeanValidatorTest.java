package de.extra.extraClientLight.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.extra.extraClientLight.model.DataObjektBean;
import de.extra.extraClientLight.model.RequestExtraBean;

public class RequestBeanValidatorTest {

	RequestExtraBean requestBean;

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

	// Validieren der URL

	@Test
	public void testUrlIsValid() {

		Assert.assertTrue(RequestBeanValidator
				.checkUrl("http://localhost:8088/mockExtraBinding"));

	}

	@Test
	public void testUrlIsInvalid() {

		Assert.assertFalse(RequestBeanValidator.checkUrl("soap://golmagem"));

	}

	@Test
	public void testUrlIsNull() {

		Assert.assertFalse(RequestBeanValidator.checkUrl(null));

	}

	// Validieren Empfaenger und Absender

	@Test
	public void testAdressIsValid() {

		Assert.assertTrue(RequestBeanValidator.checkAdress("12345678"));
	}

	@Test
	public void testAdressInvalidEmpty() {

		Assert.assertFalse(RequestBeanValidator.checkAdress(""));

	}

	@Test
	public void testAdressInvalidNull() {

		Assert.assertFalse(RequestBeanValidator.checkAdress(null));

	}

	// Gesamttest

	@Test
	public void testValidationValid() {

		Assert.assertTrue(RequestBeanValidator.validateRequestBean(requestBean));
	}

	@Test
	public void testValidationInvalidUrlNull() {

		RequestExtraBean localBean = requestBean;

		localBean.setUrl(null);
		Assert.assertFalse(RequestBeanValidator.validateRequestBean(localBean));

	}

	// Test Verfahren

	@Test
	public void testValidationInvalidVerfahrenShort() {

		RequestExtraBean localBean = requestBean;

		localBean.setVerfahren("");
		Assert.assertFalse(RequestBeanValidator.validateRequestBean(localBean));

	}

	@Test
	public void testValidationInvalidVerfahrenNull() {

		RequestExtraBean localBean = requestBean;

		localBean.setVerfahren(null);
		Assert.assertFalse(RequestBeanValidator.validateRequestBean(localBean));

	}

	// Test Fachdienst

	@Test
	public void testValidationInvalidFachdienstNull() {

		RequestExtraBean localBean = requestBean;

		localBean.setFachdienst(null);
		Assert.assertFalse(RequestBeanValidator.validateRequestBean(localBean));

	}

	@Test
	public void testValidationInvalidFachdienst() {

		RequestExtraBean localBean = requestBean;

		localBean.setFachdienst("fischbroetchen");
		Assert.assertFalse(RequestBeanValidator.validateRequestBean(localBean));

	}

	// Test Profile

	@Test
	public void testValidationInvalidProfileNull() {

		RequestExtraBean localBean = requestBean;

		localBean.setProfile(null);
		Assert.assertFalse(RequestBeanValidator.validateRequestBean(localBean));

	}

	// Test Absender

	@Test
	public void testValidationInvalidAbsenderNull() {

		RequestExtraBean localBean = requestBean;

		localBean.setAbsender(null);
		Assert.assertFalse(RequestBeanValidator.validateRequestBean(localBean));

	}
	
	@Test
	public void testValidationInvalidAbsenderShort() {

		RequestExtraBean localBean = requestBean;

		localBean.setAbsender("");
		Assert.assertFalse(RequestBeanValidator.validateRequestBean(localBean));

	}

	// Test Empfänger
	@Test
	public void testValidationInvalidEmpfaengerNull() {

		RequestExtraBean localBean = requestBean;

		localBean.setEmpfaenger(null);
		Assert.assertFalse(RequestBeanValidator.validateRequestBean(localBean));

	}
	
	@Test
	public void testValidationInvalidEmpfaengerShort() {

		RequestExtraBean localBean = requestBean;

		localBean.setEmpfaenger("");
		Assert.assertFalse(RequestBeanValidator.validateRequestBean(localBean));

	}

}
