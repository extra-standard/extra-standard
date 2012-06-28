package de.drv.dsrv.extra.marshaller;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class ExtraJaxb2Marshaller extends Jaxb2Marshaller {

	private NamespacePrefixMapper namespacePrefixMapper;

	@Override
	protected void initJaxbMarshaller(Marshaller marshaller)
			throws JAXBException {
		super.initJaxbMarshaller(marshaller);

		if (namespacePrefixMapper != null) {
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
					namespacePrefixMapper);
		}
	}

	public void setNamespacePrefixMapper(
			NamespacePrefixMapper namespacePrefixMapper) {
		this.namespacePrefixMapper = namespacePrefixMapper;
	}
}
