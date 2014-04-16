package de.drv.dsrv.spoc.web.manager.impl;

import java.io.IOException;

import javax.activation.DataHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.drv.dsrv.spoc.commons.mtomdata.MtomData;
import de.drv.dsrv.spoc.commons.mtomdata.MtomDataDao;
import de.drv.dsrv.spoc.extra.v1_3.ExtraHelper;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.Base64CharSequenceType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.components.RequestDetailsType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.request.TransportRequestType;
import de.drv.dsrv.spoc.extra.v1_3.jaxb.response.TransportResponseType;
import de.drv.dsrv.spoc.web.manager.SpocNutzdatenManager;
import de.drv.dsrv.spoc.web.manager.SpocNutzdatenManagerException;
import de.drv.dsrv.spoc.web.util.InputStreamDataSource;

/**
 * Implementiert Methoden zum Speichern und Lesen der Nutzdaten, die als Anhang
 * im eXTra-Request oder eXTra-Response transportiert werden.
 */
public class SpocNutzdatenManagerImpl implements SpocNutzdatenManager {

	private static final Log LOG = LogFactory.getLog(SpocNutzdatenManagerImpl.class);

	private final MtomDataDao mtomDataDao;

	private final int oidInsert;

	/**
	 * Konstruktor.
	 * 
	 * @param mtomDataDao
	 *            DAO-Klasse zum Zugriff auf die Datenbank
	 * @param oidInsert
	 *            Wert fuer die Spalte OID beim Einfuegen in die Tabelle
	 *            MTOM_DATA
	 */
	public SpocNutzdatenManagerImpl(final MtomDataDao mtomDataDao, final int oidInsert) {
		this.mtomDataDao = mtomDataDao;
		this.oidInsert = oidInsert;
	}

	@Override
	public void processRequestNutzdaten(final TransportRequestType transportRequestType)
			throws SpocNutzdatenManagerException {

		try {

			// Pruefe, ob Nutzdaten vorhanden sind
			if ((transportRequestType != null) && (transportRequestType.getTransportBody() != null)
					&& (transportRequestType.getTransportBody().getData() != null)
					&& (transportRequestType.getTransportBody().getData().getBase64CharSequence() != null)
					&& (transportRequestType.getTransportBody().getData().getBase64CharSequence().getValue() != null)) {

				// Erstellt ein MtomData-Objekt mit den Nutzdaten
				final MtomData mtomData = extraxtMtomDataFromRequest(transportRequestType);

				if (LOG.isDebugEnabled()) {
					LOG.debug("Speichere Nutzdaten in Datenbank f\u00fcr die Parameter profile="
							+ mtomData.getExtraProfile() + ", procedure=" + mtomData.getExtraProcedure()
							+ ", dataType=" + mtomData.getExtraDatatype() + ".");
				}

				// Speichere die Nutzdaten in Datenbank
				final int mtomDataId = this.mtomDataDao.insert(mtomData);

				if (LOG.isInfoEnabled()) {
					LOG.info("Nutzdaten mit der ID " + mtomDataId + " in Datenbank gespeichert.");
				}

				// Entferne Base64CharSequence
				transportRequestType.getTransportBody().getData().setBase64CharSequence(null);

				// Fuege AnyXML mit <MTOMDataID>12345</MTOMDataID> hinzu
				ExtraHelper.setMtomDataIdToRequest(transportRequestType, mtomDataId);
			}
		} catch (final Exception e) {
			throw new SpocNutzdatenManagerException("Fehler beim Verarbeiten der eXTra-Request-Nutzdaten.", e);
		}
	}

	@Override
	public void processResponseNutzdaten(final TransportResponseType transportResponseType)
			throws SpocNutzdatenManagerException {

		try {
			// Lese evtl. vorhandene MTOM-Data-ID aus der eXTra-Response
			final Integer mtomDataId = ExtraHelper.getMtomDataIdFromResponse(transportResponseType);

			if (mtomDataId != null) {

				if (LOG.isInfoEnabled()) {
					LOG.info("Lese Nutzdaten mit der ID " + mtomDataId + " aus der Datenbank.");
				}
				// Lade Nutzdaten aus der Datenbank
				final MtomData mtomData = this.mtomDataDao.get(mtomDataId);
				if ((mtomData != null) && (mtomData.getNutzdaten() != null)) {
					// Entferne AnyXML
					transportResponseType.getTransportBody().getData().setAnyXML(null);
					// Erstelle neue Base64CharSequence mit DataHandler
					final Base64CharSequenceType base64CharSequenceType = new Base64CharSequenceType();
					base64CharSequenceType
							.setValue(new DataHandler(new InputStreamDataSource(mtomData.getNutzdaten())));
					transportResponseType.getTransportBody().getData().setBase64CharSequence(base64CharSequenceType);
				} else {
					throw new SpocNutzdatenManagerException(
							"Fehler beim Verarbeiten der eXTra-Response-Nutzdaten; kein Nutzdaten-Eintrag mit der ID "
									+ mtomDataId + " vorhanden.");
				}
			}
		} catch (final Exception e) {
			if (e instanceof SpocNutzdatenManagerException) {
				throw (SpocNutzdatenManagerException) e;
			} else {
				throw new SpocNutzdatenManagerException("Fehler beim Verarbeiten der eXTra-Response-Nutzdaten.", e);
			}
		}
	}

	private MtomData extraxtMtomDataFromRequest(final TransportRequestType transportRequestType) throws IOException {

		final MtomData mtomData = new MtomData();

		// OID - Wert aus Einstellungen
		mtomData.setOid(this.oidInsert);

		if (transportRequestType != null) {
			// Profile
			mtomData.setExtraProfile(transportRequestType.getProfile());
			if ((transportRequestType.getTransportHeader() != null)
					&& (transportRequestType.getTransportHeader().getRequestDetails() != null)) {
				final RequestDetailsType requestDetails = transportRequestType.getTransportHeader().getRequestDetails();
				// Procedure
				mtomData.setExtraProcedure(requestDetails.getProcedure());
				// DataType
				mtomData.setExtraDatatype(requestDetails.getDataType());
			}

			if ((transportRequestType.getTransportBody() != null)
					&& (transportRequestType.getTransportBody().getData() != null)
					&& (transportRequestType.getTransportBody().getData().getBase64CharSequence() != null)
					&& (transportRequestType.getTransportBody().getData().getBase64CharSequence().getValue() != null)) {
				// Nutzdaten als InputStream
				mtomData.setNutzdaten(transportRequestType.getTransportBody().getData().getBase64CharSequence()
						.getValue().getInputStream());
			}
		}
		return mtomData;
	}
}
