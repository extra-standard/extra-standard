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
