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

/**
 * Definiert die Methoden zum Komprimieren und Dekomprimieren von Daten. Die
 * gueltigen Algorithmen sind in der Klasse {@link CompressionAlgorithmCode}
 * definiert.
 */
public interface CompressHelper {

	/**
	 * Komprimiert die uebergebenen Daten mit dem angegebenen Algorithmus.
	 * 
	 * @param compressionAlgorithmCode
	 *            Code des Komprimierungsalgorithmus; darf nicht
	 *            <code>null</code> sein
	 * @param dataUncompressed
	 *            die unkomprimierten Daten
	 * 
	 * @return die komprimierten Daten
	 * @throws CompressException
	 *             wenn beim Komprimierungsvorgang ein technischer Fehler
	 *             auftritt
	 * @throws IllegalArgumentException
	 *             wenn <code>compressionAlgorithmCode</code> den Wert
	 *             <code>null</code> hat
	 */
	byte[] compressData(CompressionAlgorithmCode compressionAlgorithmCode,
			byte[] dataUncompressed)
			throws CompressException;

	/**
	 * Dekomprimiert die uebergebenen Daten mit dem angegebenen Algorithmus.
	 * 
	 * @param compressionAlgorithmCode
	 *            Code des Komprimierungsalgorithmus; darf nicht
	 *            <code>null</code> sein
	 * @param dataCompressed
	 *            die komprimierten Daten
	 * 
	 * @return die dekomrpimierten Daten
	 * @throws CompressException
	 *             wenn beim Dekomprimierungsvorgang ein technischer Fehler
	 *             auftritt
	 * @throws IllegalArgumentException
	 *             wenn <code>compressionAlgorithmCode</code> den Wert
	 *             <code>null</code> hat
	 */
	byte[] decompressData(CompressionAlgorithmCode compressionAlgorithmCode,
			byte[] dataCompressed)
			throws CompressException;

	/**
	 * Prueft die Gueltigkeit des uebergebenen Algorithmus-Codes.
	 * 
	 * @param compressionAlgorithmCode
	 *            Code des Algorithmus
	 * @return <code>true</code>, falls Algorithmus-Code gueltig ist, sonst
	 *         <code>false</code>
	 */
	boolean isValidCode(CompressionAlgorithmCode compressionAlgorithmCode);

}
