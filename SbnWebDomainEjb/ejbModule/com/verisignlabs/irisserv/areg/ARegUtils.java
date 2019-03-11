// $Id: ARegUtils.java,v 1.1 2010/01/07 15:06:57 almaviva5 Exp $
//
// Copyright (C) 2002 Verisign, Inc.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
// USA

package com.verisignlabs.irisserv.areg;

import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.codec.binary.Hex;

/**
 * This class contains a number of basic utility routines used throughout this
 * provider. The main class of routines are various methods of converting IP
 * addresses to and from various encodings.
 *
 * The encodings are native, numeric, hex, and textual. Native is the
 * java.net.InetAddress (or one of its subclasses). IPv4 addresses use int as
 * their numeric encoding. IPv6 addresses use BigInteger. Hex is a zero-fill
 * bare hex string, and textual is the normal, human readable form of the
 * address.
 *
 * @author David Blacka (original)
 * @version $Revision: 1.1 $
 */
public class ARegUtils {

	/**
	 * Convert an IPv4 or IPv6 textual representation into an InetAddress.
	 */
	public static InetAddress addressStringToInet(String addr)
			throws UnknownHostException {
		// Ideally, if 'addr' is a text form of an IP address (v4 or
		// v6), this should not result in a DNS query, and just convert
		// the textual form.
		return InetAddress.getByName(addr);
	}

	/**
	 * Convert a bare hex notation address (e.g. "0a83fce3") into an IPv4 or
	 * IPv6 InetAddress.
	 *
	 * @param hex
	 *            the hex string. This must be strictly either 8 or 32
	 *            characters long, consisting only of valid hex digits (0-9,
	 *            a-f).
	 * @throws UnknownHostException
	 *             if the hex string is not a valid IP address (not valid hex or
	 *             the wrong length).
	 */
	public static InetAddress hexToAddress(String hex)
			throws UnknownHostException {
		if (hex.length() != 8 && hex.length() != 32)
			throw new UnknownHostException("hex must be either 4 or 16 bytes");

		byte[] a = null;
		try {
			a = Hex.decodeHex(hex.toCharArray());
		} catch (Exception e) {
			// presumably we got here because some non-hex character was
			// encountered.
			throw new UnknownHostException(e.getMessage());
		}

		return InetAddress.getByAddress(a);
	}

	/**
	 * Encode an IPv4 or IPv6 address into a bare, zero-filled hex string.
	 */
	public static String addressToHex(InetAddress addr) {
		byte[] a = addr.getAddress();
		char[] c = Hex.encodeHex(a);

		return new String(c);
	}

	/** Convert an integer into an (IPv4) InetAddress. */
	public static InetAddress intToIPv4Address(int addr) {
		byte[] a = new byte[4];

		a[0] = (byte) ((addr >> 24) & 0xFF);
		a[1] = (byte) ((addr >> 16) & 0xFF);
		a[2] = (byte) ((addr >> 8) & 0xFF);
		a[3] = (byte) ((addr & 0xFF));

		InetAddress res = null;
		try {
			// the documentation for this method claims it will only throw
			// an exception if the length of the byte array is incorrect.
			// So, this should never through the exception.
			res = InetAddress.getByAddress(a);
		} catch (UnknownHostException e) {
		}
		return res;
	}

	/**
	 * Given an IPv4 address (really, an Inet4Address), convert it into an
	 * integer.
	 *
	 * @throws IllegalArgumentException
	 *             if the address is really an IPv6 address.
	 */
	public static int ipv4AddressToInt(InetAddress addr) {
		if (addr instanceof Inet6Address)
			throw new IllegalArgumentException(
					"IPv6 address used in IPv4 context");

		byte[] a = addr.getAddress();

		int res = ((a[0] & 0xFF) << 24) | ((a[1] & 0xFF) << 16)
				| ((a[2] & 0xFF) << 8) | (a[3] & 0xFF);

		return res;
	}

	/**
	 * Given the textual form of an IPv4 address, convert it into an integer.
	 *
	 * @throws UnknownHostException
	 *             if addr is an invalid address format.
	 */
	public static int ipv4AddressToInt(String addr) throws UnknownHostException {
		return ipv4AddressToInt(addressStringToInet(addr));
	}

	/**
	 * Given an IPv6 address, convert it into a BigInteger.
	 *
	 * @throws IllegalArgumentException
	 *             if the address is not an IPv6 address.
	 */
	public static BigInteger ipv6AddressToBigInteger(InetAddress addr) {
		if (!(addr instanceof Inet6Address))
			throw new IllegalArgumentException(
					"IPv4 address used in IPv6 context.");

		byte[] a = addr.getAddress();

		if (a[0] == -1) {
			return new BigInteger(1, a);
		}

		return new BigInteger(a);
	}

	/**
	 * Given a textual IPv6 address, convert it into BigInteger.
	 *
	 * @throws UnknownHostException
	 *             if addr is an invalid IPv6 address.
	 */
	public static BigInteger ipv6AddressToBigInteger(String addr)
			throws UnknownHostException {
		return ipv6AddressToBigInteger(addressStringToInet(addr));
	}

	/**
	 * Convert a big integer into an IPv6 address.
	 *
	 * @throws UnknownHostException
	 *             if the big integer is too large, and thus an invalid IPv6
	 *             address.
	 */
	public static InetAddress bigIntToIPv6Address(BigInteger addr)
			throws UnknownHostException {
		byte[] a = new byte[16];
		byte[] b = addr.toByteArray();

		if (b.length > 16 && !(b.length == 17 && b[0] == 0)) {
			throw new UnknownHostException("invalid IPv6 address (too big)");
		}

		if (b.length == 16) {
			return InetAddress.getByAddress(b);
		}

		// handle the case where the IPv6 address starts with "FF".
		if (b.length == 17) {
			System.arraycopy(b, 1, a, 0, 16);
		} else {
			// copy the address into a 16 byte array, zero-filled.
			int p = 16 - b.length;
			for (int i = 0; i < b.length; i++) {
				a[p + i] = b[i];
			}
		}
		return InetAddress.getByAddress(a);
	}

	/**
	 * @param intstr
	 *            a string containing an integer.
	 * @param def
	 *            the default if the string does not contain a valid integer.
	 */
	public static int parseInt(String intstr, int def) {
		Integer res;

		if (intstr == null)
			return def;

		try {
			res = Integer.decode(intstr);
		} catch (Exception e) {
			res = new Integer(def);
		}

		return res.intValue();
	}
}
