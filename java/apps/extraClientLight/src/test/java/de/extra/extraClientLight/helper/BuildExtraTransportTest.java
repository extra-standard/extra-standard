package de.extra.extraClientLight.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.request.TransportRequestBodyType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.request.TransportRequestType;
import de.extra.extraClientLight.model.RequestExtraBean;
import de.extra.extraClientLight.test.BuildTestRequestBeanHelper;

public class BuildExtraTransportTest {

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void testBuildBody() {

		InputStream in = new ByteArrayInputStream(
				new String("Teststring").getBytes());
		TransportRequestBodyType requestBody = null;
		try {
			requestBody = BuildExtraTransport.buildBody(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] outByte = null;
		try {
			outByte = IOUtils.toByteArray(requestBody.getData()
					.getBase64CharSequence().getValue().getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals("Inhalt Body", "Teststring", new String(outByte));

	}

	@Test
	public void testBuildBodyEmpty() {
		RequestExtraBean testBean = BuildTestRequestBeanHelper
				.buildRequestInlineBean();

		testBean.setDataObjekt(null);

		TransportRequestType request = BuildExtraTransport
				.buildTransportRequest(testBean);

		assertNull(request.getTransportBody().getData());

	}

	@Test
	public void testBuildBodyQuery() {

		RequestExtraBean testBean = BuildTestRequestBeanHelper.buildQueryBean();

		TransportRequestType req = BuildExtraTransport
				.buildTransportRequest(testBean);

		Assert.assertNotNull(req.getTransportBody().getData()
				.getElementSequence().getAny().get(0));

	}

}
