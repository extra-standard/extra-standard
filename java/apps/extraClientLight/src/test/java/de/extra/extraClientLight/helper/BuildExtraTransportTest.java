package de.extra.extraClientLight.helper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import de.drv.dsrv.spoc.extra.v1_3.jaxb.request.TransportRequestBodyType;

public class BuildExtraTransportTest {

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

		Assert.assertEquals("Inhalt Body", "Teststring", new String(outByte));

	}

}
