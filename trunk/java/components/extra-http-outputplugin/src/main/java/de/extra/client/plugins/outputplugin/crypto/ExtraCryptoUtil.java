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
package de.extra.client.plugins.outputplugin.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ExtraCryptoUtil {

	private static final String SYM_KEY_STR = "F5aybqjqq7Mq8iGi+5kjigW1zfYW4QxCkAoAjVCeuNk=";

	private static final String CHARSET = "UTF-8";

	// Advanced Encryption Standard as specified by NIST in a draft FIPS
	private static final String ALGORITHM = "AES";

	// Set of operations to generate output "algorithm/mode/padding" or set to
	// default cipher operations using "algorithm"
	private static final String TRANSFORMATION = "AES";

	// /**
	// * Generates a new random secrect key with AES/256bit
	// */
	// private static void generateRandomSecretKey() {
	// try {
	// javax.crypto.KeyGenerator kg = javax.crypto.KeyGenerator
	// .getInstance(ALGORITHM);
	// kg.init(256);
	//
	// System.out.println(new BASE64Encoder().encode(kg.generateKey()
	// .getEncoded()));
	//
	// } catch (java.security.NoSuchAlgorithmException e) {
	// e.printStackTrace(System.err);
	// }
	// }

	/** Encrypts the specified string, using the current secret key. */
	public static String encrypt(String sValue) {
		return encrypt(sValue, null);
	}

	/** Decrypts the specified encrypted string, using the current secret key. */
	public static String decrypt(String sName) {
		return decrypt(sName, null);
	}

	/**
	 * Generates a new SecretKeySpec using the encoded string. Uses the
	 * specified algorithm to generate the key.
	 */
	private static SecretKeySpec decodeKey(String encrpKey) throws Exception {
		SecretKeySpec skeySpec = null;
		BASE64Decoder base64Decoder = new BASE64Decoder();

		byte[] raw = base64Decoder.decodeBuffer(encrpKey);
		skeySpec = new SecretKeySpec(raw, ALGORITHM);

		return skeySpec;
	}

	/** Encrypts the specified string, using the specified secret key. */
	private static String encrypt(String sValue, String secretKey) {
		if (secretKey == null) {
			secretKey = SYM_KEY_STR;
		}

		if (sValue == null || sValue.equals("")) {
			return "";
		}

		String textEncode = null;
		Cipher encryptCipher = null;

		try {
			SecretKeySpec skeySpec = decodeKey(secretKey);
			encryptCipher = Cipher.getInstance(TRANSFORMATION);
			encryptCipher.init(Cipher.ENCRYPT_MODE, skeySpec);

			byte[] plainText = sValue.trim().getBytes(CHARSET);

			// do the actual encryption
			byte[] cipherText = encryptCipher.doFinal(plainText);

			BASE64Encoder base64Encoder = new BASE64Encoder();
			// Changed to encode() to avoid <cr> on end of string
			// textEncode = base64Encoder.encodeBuffer(cipherText);
			textEncode = base64Encoder.encode(cipherText);

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

		return textEncode;
	}

	/** Decrypts the specified encrypted string, using the specified secret key. */
	private static String decrypt(String sName, String secretKey) {
		if (secretKey == null) {
			secretKey = SYM_KEY_STR;
		}

		if (sName == null || sName.equals("")) {
			return "";
		}

		String sText = "";
		try {
			SecretKeySpec skeySpec = decodeKey(secretKey);
			Cipher decryptCipher = Cipher.getInstance(TRANSFORMATION);
			decryptCipher.init(Cipher.DECRYPT_MODE, skeySpec);

			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] encpArr = base64Decoder.decodeBuffer(sName.trim());

			byte[] plainText = decryptCipher.doFinal(encpArr);

			sText = new String(plainText, CHARSET);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

		return sText;
	}
}