/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.iccu.sbn.util.cipher;

import it.iccu.sbn.util.Base64Util;
import it.iccu.sbn.util.Constants;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

public class PasswordEncrypter {

	private static Logger log = Logger.getLogger(PasswordEncrypter.class);

	private Cipher ecipher;
	private Cipher dcipher;

	public PasswordEncrypter(String passPhrase) {

		// 8-bytes Salt
		byte[] salt = {
			(byte) 0xA8, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
			(byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03
		};

		// Iteration count
		int iterationCount = 19;

		try {

			passPhrase = utf8PasswordToBase64(passPhrase);
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

			ecipher = Cipher.getInstance(key.getAlgorithm());
			dcipher = Cipher.getInstance(key.getAlgorithm());

			// Prepare the parameters to the cipthers
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

		} catch (Exception e) {
			log.error("", e);
		}

	}

	private String utf8PasswordToBase64(String passPhrase) {

		try {
			byte[] bytes = passPhrase.getBytes("UTF8");
			//il charset UTF8 utilizza un singolo byte per i caratteri base ASCII(0x00-0x7F)
			//se la lunghezza é differente devo ridurre la pwd a una stringa base64
			if (bytes.length != passPhrase.length()) {
				String encode = Base64Util.encode(bytes);
				log.warn("riduzione base64 password non ASCII: '" + passPhrase + "' --> '" + encode + "'");
				return encode;
			}

		} catch (Exception e) {
			log.error("", e);
		}

		return passPhrase;
	}

	public String encrypt(String str) {
		try {
			// Encode the string into bytes using utf-8
			byte[] utf8 = str.getBytes("UTF8");

			// Encrypt
			byte[] enc = ecipher.doFinal(utf8);

			// Encode bytes to base64 to get a string
			return Base64Util.encode(enc);

		} catch (BadPaddingException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	public String decrypt(String str) {

		try {

			// Decode base64 to get bytes
			byte[] dec = Base64Util.decode(str);

			// Decrypt
			byte[] utf8 = dcipher.doFinal(dec);

			// Decode using utf-8
			return new String(utf8, "UTF8");

		} catch (BadPaddingException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (UnsupportedEncodingException e) {
		}

		return null;
	}

	public static void testUsingPassPhrase(String password) {

		// Create encrypter/decrypter class
		PasswordEncrypter pe = new PasswordEncrypter(password);

		// Encrypt the string
		String encrypted = pe.encrypt(password);

		// Decrypt the string
		String decrypted = pe.decrypt(encrypted);

		// Print out values
		System.out.println("Generazione password bibliotecario");
		System.out.println("\tPassword scelta     : " + password);
		System.out.println("\tPassword criptata   : " + encrypted);
		System.out.println("\tPassword decriptata : " + decrypted);
		System.out.println();
	}

	public static String randomPassword() {
		return RandomStringUtils.randomAlphanumeric(Constants.Servizi.Utenti.MAX_PASSWORD_LENGTH);
	}

	/**
	 * Sole entry point to the class and application used for testing the String
	 * Encrypter class.
	 *
	 * @param args
	 *            Array of String arguments.
	 */
	public static void main(String[] args) {
		testUsingPassPhrase("12345678");
	}

}
