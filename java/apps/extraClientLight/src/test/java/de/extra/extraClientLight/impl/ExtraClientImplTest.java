package de.extra.extraClientLight.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.extra.extraClientLight.IextraClient;
import de.extra.extraClientLight.model.RequestExtraBean;
import de.extra.extraClientLight.model.ResponseExtraBean;
import de.extra.extraClientLight.test.BuildTestRequestBeanHelper;
import de.extra.extraClientLight.util.SendWebService;
import de.extra.extraClientLight.util.SendWebServiceFactory;

public class ExtraClientImplTest {

	RequestExtraBean requestBean;
	
	@Mock
	SendWebServiceFactory webServiceFactory;

	@Mock
	SendWebService sendWebServiceMock;
	
	@InjectMocks
	ExtraClientImpl client;

	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);

		when(webServiceFactory.getWebSendWebSerice()).thenReturn(sendWebServiceMock);
		
		requestBean = BuildTestRequestBeanHelper.buildRequestInlineBean();
	}

	// Erfolgreich

	@Test
	public void testExtraClientImplValid() {

		ResponseExtraBean repsonse =client.sendExtra(requestBean);

		assertNotNull(repsonse);

	}

	// Bean mit leerem Absender

	@Test
	public void testExtraClientImplInvalidAbsLeer() {

	//	IextraClient extraClient = new ExtraClientImpl();

		RequestExtraBean testBean = requestBean;
		testBean.setAbsender("");

		assertNotNull(client.sendExtra(testBean));

	}

	// Senden mit MTOM

	@Test
	public void testExtraClientImplSendMtom() {

	//	IextraClient extraClient = new ExtraClientImpl();

		RequestExtraBean testBean = requestBean;
		testBean.setMtom(true);

		assertNotNull(client.sendExtra(testBean));

	}

}
