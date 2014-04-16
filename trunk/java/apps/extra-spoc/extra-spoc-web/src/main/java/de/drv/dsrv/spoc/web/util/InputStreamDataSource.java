package de.drv.dsrv.spoc.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

/**
 * Implementiert eine DataSource auf Basis eines InpuStreams.
 */
public class InputStreamDataSource implements DataSource {

	private final InputStream inputStream;

	/**
	 * Konstruktor.
	 * 
	 * @param inputStream
	 *            InputStream-Objekt
	 */
	public InputStreamDataSource(final InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return this.inputStream;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public String getContentType() {
		return "application/octet-stream";
	}

	@Override
	public String getName() {
		return "InputStreamDataSource";
	}
}