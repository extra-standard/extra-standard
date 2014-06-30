package de.extra.client.core.builder;

import de.drv.dsrv.extrastandard.namespace.request.RequestTransport;
import de.extrastandard.api.model.content.IExtraProfileConfiguration;
import de.extrastandard.api.model.content.IInputDataContainer;

/**
 * 
 * @author Leonid Potap
 * @version $Id$
 */
public interface IRequestTransportBuilder {

	/**
	 * @param senderData
	 * @param config
	 * @return einen Leeren RequestTransport Element mit Version und Profile
	 *         Informationen
	 */
	RequestTransport buildRequestTransport(IInputDataContainer senderData,
			IExtraProfileConfiguration config);

	/**
	 * @param config
	 * @return einen Leeren RequestTransport Element mit Version und Profile
	 *         Informationen
	 */
	RequestTransport buildRequestTransport(IExtraProfileConfiguration config);

	/**
	 * @return xmlType
	 */
	String getXmlType();

}