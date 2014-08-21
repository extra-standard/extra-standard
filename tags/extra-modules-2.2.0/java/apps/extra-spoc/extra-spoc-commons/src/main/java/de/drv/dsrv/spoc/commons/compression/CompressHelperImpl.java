/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.drv.dsrv.spoc.commons.compression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

/**
 * Implementiert die Methoden zum Komprimieren und Dekomprimieren von Daten. Die
 * gueltigen Algorithmen sind in der Klasse {@link CompressionAlgorithmCode}
 * definiert.
 */
public class CompressHelperImpl implements CompressHelper {

	private static final int BUFFER_LENGTH_DECOMPRESS = 2048;

	@Override
	public byte[] compressData(final CompressionAlgorithmCode compressionAlgorithmCode,
			final byte[] dataUncompressed)
			throws CompressException {

		// Sicherstellen, dass compressionAlgorithmCode != null
		checkAlgorithmCodeNotNull(compressionAlgorithmCode);

		// Pruefen, ob Daten nicht null sind
		if (dataUncompressed == null) {
			return dataUncompressed;
		}

		// Pruefe, ob das Kennzeichen fuer die Dekomprimierung gueltig ist
		if (CompressionAlgorithmCode.ZIP.equals(compressionAlgorithmCode)
				|| CompressionAlgorithmCode.GZIP
						.equals(compressionAlgorithmCode)) {
			try {
				return compressDataWithCompressor(dataUncompressed);
			} catch (final Exception e) {
				throw new CompressException(e);
			}
		} else {
			// Gebe unkomprimierte Daten zurueck
			return dataUncompressed;
		}
	}

	@Override
	public byte[] decompressData(final CompressionAlgorithmCode compressionAlgorithmCode,
			final byte[] dataCompressed)
			throws CompressException {

		// Sicherstellen, dass compressionAlgorithmCode != null
		checkAlgorithmCodeNotNull(compressionAlgorithmCode);

		// Pruefen, ob Daten nicht null sind
		if (dataCompressed == null) {
			return dataCompressed;
		}

		// Pruefe, ob das Kennzeichen fuer die Dekomprimierung gueltig ist
		if (CompressionAlgorithmCode.ZIP.equals(compressionAlgorithmCode)
				|| CompressionAlgorithmCode.GZIP
						.equals(compressionAlgorithmCode)) {
			try {

				ByteArrayInputStream dataCompressedStream = new ByteArrayInputStream(
						dataCompressed);

				// Ermittle Daten fuer ZIP-Archiver
				byte[] dataDecompressed = decompressDataWithArchiver(dataCompressedStream);

				// Pruefe, ob Daten aus Archiv gueltig sind
				if (dataDecompressed != null) {
					// Gebe dekomprimierte Daten zurueck
					return dataDecompressed;
				} else {

					dataCompressedStream = new ByteArrayInputStream(
							dataCompressed);

					// Ermittle Daten fuer GZIP-Compressor
					dataDecompressed = decompressDataWithCompressor(dataCompressedStream);

					// Pruefe, ob Daten gueltig sind
					if (dataDecompressed != null) {
						// Gebe dekomprimierte Daten zurueck
						return dataDecompressed;
					} else {
						// Keine dekomprimierten Daten vorhanden -> Werfe
						// Exception
						throw new CompressException(new Exception());
					}
				}

			} catch (final EOFException e) {
				throw new CompressException(e);
			} catch (final IOException e) {
				throw new CompressException(e);
			} catch (final Exception e) {
				throw new CompressException(e);
			}
		} else {
			// Gebe komprimierte Daten zurueck
			return dataCompressed;
		}
	}

	@Override
	public boolean isValidCode(final CompressionAlgorithmCode compressionAlgorithmCode) {

		// Pruefe, ob das Kennzeichen fuer die Dekomprimierung gueltig ist
		return (CompressionAlgorithmCode.NONE.equals(compressionAlgorithmCode)
				|| CompressionAlgorithmCode.GZIP
						.equals(compressionAlgorithmCode) || CompressionAlgorithmCode.ZIP
				.equals(compressionAlgorithmCode));
	}

	private void checkAlgorithmCodeNotNull(
			final CompressionAlgorithmCode compressionAlgorithmCode) {

		// Pruefe, ob Algorithmus-Code angegeben wurde
		if (compressionAlgorithmCode == null) {
			throw new IllegalArgumentException(
					"Parameter compressionAlgorithmCode darf nicht null sein.");
		}
	}

	/**
	 * Erzeugen eines ByteArrayOutputStreams und GZIPOutputStream. Diese werden
	 * mit den uebergebenen ByteArray gefuellt und mit GZIP komprimiert. Das
	 * Ergebnis wird zurueckgegeben.
	 * 
	 * @param dataToCompress
	 *            Byte Array mit den zu komprimierenden Daten
	 * @return komprimiertes ByteArray
	 * @throws Exception
	 *             Es koennen IOException und NullPointerException auftreten
	 */
	private byte[] compressDataWithCompressor(final byte[] dataToCompress)
			throws Exception {

		// Erstelle Datenobjekt zur Speicherung der dekomprimierten Daten
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();

		// Erstelle Ausgang-Strom im GZIP-Format
		final GZIPOutputStream gz = new GZIPOutputStream(bos);

		gz.write(dataToCompress);

		gz.close();
		bos.close();

		return bos.toByteArray();
	}

	/**
	 * Ermittelt die dekomprimierten Daten unter Verwendung eines Archivers.
	 * 
	 * @param dataCompressed
	 *            die komprimierten Daten
	 * @return die dekomprimierten Daten
	 * @throws IOException
	 *             wenn beim Lesen bzw. Schreiben der Daten ein technischer
	 *             Fehler auftritt
	 */
	private byte[] decompressDataWithArchiver(
			final ByteArrayInputStream dataCompressed) throws IOException {
		try {

			// Ermittle Inputstream zum Zugriff auf archivierte Daten
			final ZipInputStream zipInputStream = new ZipInputStream(
					dataCompressed);

			try {
				// Ermittle naechsten Eintrag in der Archivdatei
				final ZipEntry zipEntry = zipInputStream.getNextEntry();

				// Pruefe Eintrag
				if ((zipEntry != null) && !zipEntry.isDirectory()) {

					// Ermittle Daten
					int count;
					final byte buffer[] = new byte[BUFFER_LENGTH_DECOMPRESS];

					final ByteArrayOutputStream dataDecompressed = new ByteArrayOutputStream();

					while ((count = zipInputStream.read(buffer, 0,
							BUFFER_LENGTH_DECOMPRESS)) != -1) {
						dataDecompressed.write(buffer, 0, count);
					}
					dataDecompressed.flush();
					dataDecompressed.close();

					return dataDecompressed.toByteArray();

				} else {
					// Daten nicht entschluesselbar -> Gebe null zurueck
					return null;
				}
			} finally {
				// Schliesse Input Stream
				zipInputStream.close();
			}
		} catch (final ZipException e) {
			// Daten nicht entschluesselbar -> Gebe null zurueck
			return null;
		}
	}

	/**
	 * Ermittelt die dekomprimierten Daten unter Verwendung eines Compressors.
	 * 
	 * @param dataCompressed
	 *            die komprimierten Daten
	 * @return die dekomprimierten Daten
	 * @throws IOException
	 *             wenn beim Lesen bzw. Schreiben der Daten ein technischer
	 *             Fehler auftritt
	 */
	private byte[] decompressDataWithCompressor(
			final ByteArrayInputStream dataCompressed) throws IOException {

		// Ermittle Inputstream zum Zugriff auf komprimierten Datens
		final GZIPInputStream compressorInputStream = new GZIPInputStream(
				dataCompressed);

		// Erstelle Datenobjekt zur Speicherung der dekomprimierten Daten
		final ByteArrayOutputStream decompressedDaten = new ByteArrayOutputStream();

		try {

			// Lese dekomprimierte Daten schrittweise
			final byte[] buffer = new byte[BUFFER_LENGTH_DECOMPRESS];
			int n = 0;
			while (-1 != (n = compressorInputStream.read(buffer))) {
				decompressedDaten.write(buffer, 0, n);
			}

			// Gebe dekomprimierte Daten zurueck
			return decompressedDaten.toByteArray();
		} finally {
			// Schliesse Streams
			decompressedDaten.close();
			compressorInputStream.close();
		}
	}
}
