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
package it.iccu.sbn.util;

import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;

/**
 * Utility class that encodes and decodes byte arrays in Base64 representation.
 * Uses underlying functionality in the java.util.prefs package of the Java API.
 * Note that this is slightly different from RFC 2045; ie, there are no CRLFs in
 * the encoded string. Should be thread safe. Requires Java 1.4 or better.
 */
public class Base64Util extends AbstractPreferences {

	private String store;
	private static Base64Util instance = new Base64Util();

	/** Hide the constructor; this is a singleton. */
	private Base64Util() {
		super(null, "");
	}

	/** Given a byte array, return its Base64 representation as a String. */
	public static final synchronized String encode(byte[] b) {
		instance.putByteArray(null, b);
		return instance.get(null, null);
	}

	/**
	 * Given a String containing a Base64 representation, return the
	 * corresponding byte array.
	 */
	public static final synchronized byte[] decode(String base64String) {
		instance.put(null, base64String);
		return instance.getByteArray(null, null);
	}

	public String get(String key, String def) {
		return store;
	}

	public void put(String key, String value) {
		store = value;
	}

	// Other methods required to implement the abstract class; these methods are
	// not used.
	protected AbstractPreferences childSpi(String name) {
		return null;
	}

	protected void putSpi(String key, String value) {
	}

	protected String getSpi(String key) {
		return null;
	}

	protected void removeSpi(String key) {
	}

	protected String[] keysSpi() throws BackingStoreException {
		return null;
	}

	protected String[] childrenNamesSpi() throws BackingStoreException {
		return null;
	}

	protected void syncSpi() throws BackingStoreException {
	}

	protected void removeNodeSpi() throws BackingStoreException {
	}

	protected void flushSpi() throws BackingStoreException {
	}

	/** Just used for simple unit testing. Remove as desired. */
	public static void main(String[] args) throws Exception {
		String s = "fante cavallo & re --- àccèntàtò";
		System.out.println("Start:" + s);
		String es = Base64Util.encode(s.getBytes("UTF8"));
		System.out.println("Encoded:" + es);
		System.out.println("Decoded:" + new String(Base64Util.decode(es), "UTF8"));

	}
}
