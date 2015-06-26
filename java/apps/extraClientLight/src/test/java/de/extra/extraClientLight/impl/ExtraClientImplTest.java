package de.extra.extraClientLight.impl;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sun.mail.iap.Response;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.request.TransportRequestType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.response.TransportResponseType;
import de.extra.extraClientLight.exception.ExtraClientLightException;
import de.extra.extraClientLight.model.RequestExtraBean;
import de.extra.extraClientLight.model.ResponseExtraBean;
import de.extra.extraClientLight.test.BuildTestRequestBeanHelper;
import de.extra.extraClientLight.util.ISendWebService;
import de.extra.extraClientLight.util.SendWebServiceFactory;

public class ExtraClientImplTest {

	RequestExtraBean requestBean;

	@Mock
	SendWebServiceFactory webServiceFactory;

	@Mock
	ISendWebService sendWebServiceMock;

	@InjectMocks
	ExtraClientImpl client;

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);

		when(webServiceFactory.getWebSendWebSerice()).thenReturn(
				sendWebServiceMock);

		requestBean = BuildTestRequestBeanHelper.buildRequestInlineBean();
	}

	// Erfolgreich

	@Test
	public void testExtraClientImplValid() {
		try {

			// TODO Valides ExtraResponseObjekt erstellen

			when(
					sendWebServiceMock.sendRequest(
							any(TransportRequestType.class), anyString(),
							anyBoolean())).thenReturn(
					new TransportResponseType());
		} catch (ExtraClientLightException e) {

			e.printStackTrace();
		}
		ResponseExtraBean repsonse = client.sendExtra(requestBean);

		assertNotNull(repsonse);

	}

	// Exception

	@Test
	public void testExtraClientImplExtraException() {
		try {

			when(sendWebServiceMock.sendRequest(
					any(TransportRequestType.class), anyString(), anyBoolean())).thenThrow(new ExtraClientLightException());
		} catch (ExtraClientLightException e) {

			e.printStackTrace();
		}
		ResponseExtraBean response = client.sendExtra(requestBean);

		assertEquals(9, response.getReturnCode());

	}

	// Bean mit leerem Absender

	@Test
	public void testExtraClientImplInvalidAbsLeer() {

		// IextraClient extraClient = new ExtraClientImpl();

		RequestExtraBean testBean = requestBean;
		testBean.setAbsender("");

		assertNotNull(client.sendExtra(testBean));

	}

	// Senden mit MTOM

	@Test
	public void testExtraClientImplSendMtom() {

		// IextraClient extraClient = new ExtraClientImpl();

		RequestExtraBean testBean = requestBean;
		testBean.setMtom(true);

		assertNotNull(client.sendExtra(testBean));

	}

}
