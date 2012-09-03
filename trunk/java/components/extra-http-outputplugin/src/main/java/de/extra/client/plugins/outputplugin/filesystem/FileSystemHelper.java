package de.extra.client.plugins.outputplugin.filesystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

import de.drv.dsrv.extrastandard.namespace.components.DataType;
import de.drv.dsrv.extrastandard.namespace.components.FlagType;
import de.drv.dsrv.extrastandard.namespace.components.ReportType;
import de.drv.dsrv.extrastandard.namespace.response.Package;
import de.extra.client.plugins.outputplugin.utils.OutputPluginHelper;
import de.extrastandard.api.model.content.IResponseData;
import de.extrastandard.api.plugin.IResponseProcessPlugin;

//@Named("fileSystemHelper")
public class FileSystemHelper implements IResponseProcessPlugin, Serializable {

	private static final long serialVersionUID = 1607616003288362662L;

	private static Logger logger = Logger.getLogger(FileSystemHelper.class);

	@Inject
	@Named("eXTrajaxb2Marshaller")
	private Unmarshaller unmarshaller;

	@Value("${plugins.responseplugin.fileSaverResponsePlugin.eingangOrdner}")
	private String eingangOrdner;

	@Value("${plugins.responseplugin.fileSaverResponsePlugin.reportOrdner}")
	private String reportOrdner;

	@Override
	public List<IResponseData> processResponse(final InputStream responseAsStream) {
		try {
			de.drv.dsrv.extrastandard.namespace.response.XMLTransport extraResponse;

			extraResponse = (de.drv.dsrv.extrastandard.namespace.response.XMLTransport) unmarshaller
					.unmarshal(new StreamSource(responseAsStream));

			pruefeVerzeichnis();

			final List<Package> packageList = extraResponse.getTransportBody().getPackage();
			if (!OutputPluginHelper.isBodyEmpty(extraResponse.getTransportBody())) {
				if (packageList == null || packageList.size() == 0) {
					final String responseId = extraResponse.getTransportHeader().getResponseDetails().getResponseID()
							.getValue();
					logger.debug("Keine Pakete vorhanden");
					final byte[] responseBody = extraResponse.getTransportBody().getData().getBase64CharSequence()
							.getValue();

					if (saveBodyToFilesystem(responseId, responseBody)) {
						logger.debug("Speicheren des Body auf Filesystem erfolgreich");
					}
				} else {
					for (final Iterator<Package> iter = packageList.iterator(); iter.hasNext();) {
						final Package extraPackage = iter.next();

						final String responseId = extraPackage.getPackageHeader().getResponseDetails().getResponseID()
								.getValue();
						DataType data = new DataType();
						data = extraPackage.getPackageBody().getData();
						byte[] packageBody = null;

						if (data.getBase64CharSequence() != null) {
							packageBody = data.getBase64CharSequence().getValue();

						} else {
							if (data.getCharSequence() != null) {
								packageBody = data.getCharSequence().getValue().getBytes();
							}
						}

						if (packageBody != null) {
							if (saveBodyToFilesystem(responseId, packageBody)) {
								if (logger.isDebugEnabled()) {
									logger.debug("Speichern für RespId " + responseId + " erfolgreich");
								}
							}
						} else {
							logger.error("PackageBody nicht gefüllt");

						}
					}
				}
			} else {

				final ReportType report = extraResponse.getTransportHeader().getResponseDetails().getReport();

				final String requestId = extraResponse.getTransportHeader().getRequestDetails().getRequestID()
						.getValue();
				final String responseId = extraResponse.getTransportHeader().getResponseDetails().getResponseID()
						.getValue();

				saveReportToFilesystem(report, responseId, requestId);

				logger.info("Body leer");
			}

			return new ArrayList<IResponseData>();
		} catch (final XmlMappingException xmlMappingException) {
			// TODO Exceptionhandling
			throw new IllegalStateException(xmlMappingException);
		} catch (final IOException ioException) {
			// TODO Auto-generated catch block
			throw new IllegalStateException(ioException);
		}
	}

	private boolean saveBodyToFilesystem(final String responseId, final byte[] responseBody) {
		final boolean erfolgreichGespeichert = false;

		final StringBuffer dateiName = new StringBuffer();

		dateiName.append(eingangOrdner);
		dateiName.append(baueDateiname());
		dateiName.append("-");
		dateiName.append(responseId);

		final File responseFile = new File(dateiName.toString());

		FileWriter fw = null;

		try {
			fw = new FileWriter(responseFile);

			fw.write(new String(responseBody));

		} catch (final IOException e) {
			logger.error("Fehler beim schreiben der Antwort", e);
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Dateiname: '" + dateiName + "'");
		}

		return erfolgreichGespeichert;
	}

	private boolean saveReportToFilesystem(final ReportType report, final String responseId, final String requestId) {
		final boolean erfolgreichGespeichert = false;

		final StringBuffer dateiName = new StringBuffer();

		dateiName.append(reportOrdner);
		dateiName.append(baueDateiname());
		dateiName.append(".rep");

		final File reportFile = new File(dateiName.toString());

		final List<FlagType> flagList = report.getFlag();
		FlagType flagItem = null;

		if (flagList.size() >= 1) {
			flagItem = flagList.get(0);
		}

		FileWriter fw = null;
		StringBuffer sb;

		try {
			fw = new FileWriter(reportFile);

			sb = new StringBuffer();
			sb.append("Die Meldung mit ID ");
			sb.append(requestId);
			sb.append(" erhielt folgende Antwort:");
			sb.append("\r\n");
			sb.append("ResponseID: ");
			sb.append(responseId);
			sb.append("\r\n");
			sb.append("ReturnCode:");
			sb.append(flagItem.getCode().getValue());
			sb.append("\r\n");
			sb.append(flagItem.getText().getValue());

			fw.write(sb.toString());
		} catch (final IOException e) {
			logger.error("Fehler beim Schreiben des Reports", e);
		} finally {
			try {
				fw.close();
			} catch (final IOException e) {
				logger.error("Fehler beim schließen das FileWriters");
			}
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Report: '" + dateiName + "'");
		}

		return erfolgreichGespeichert;
	}

	private String baueDateiname() {
		final Date now = Calendar.getInstance().getTime();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmm");
		sdf.format(now);

		return sdf.format(now);
	}

	private void pruefeVerzeichnis() {
		final File eingangsOrdner = new File(eingangOrdner);
		final File reportsOrdner = new File(reportOrdner);

		if (!eingangsOrdner.exists()) {
			logger.debug("Eingangsordner anlegen");

			eingangsOrdner.mkdir();
		}
		if (!reportsOrdner.exists()) {
			logger.debug("Reportordner anlegen");

			reportsOrdner.mkdir();
		}
	}
}
