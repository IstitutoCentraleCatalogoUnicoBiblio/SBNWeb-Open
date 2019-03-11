// $Id: CIDR.java,v 1.1 2010/01/07 15:06:57 almaviva5 Exp $
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

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * This is a base class for representing a CIDR notation networks.
 *
 * @author David Blacka (original)
 * @version $Revision: 1.1 $
 */
public abstract class CIDR {
	protected InetAddress mPrefix;
	protected int mPrefixLength;

	/** This essentially should never be used. */
	protected CIDR() {
	}

	public static CIDR generateCIDR(InetAddress prefix, int prefix_length)
			throws UnknownHostException {
		if (prefix_length < 0)
			throw new UnknownHostException("Invalid prefix length used: "
					+ prefix_length);

		if (prefix instanceof Inet4Address) {
			if (prefix_length > 32)
				throw new UnknownHostException("Invalid prefix length used: "
						+ prefix_length);

			return new CIDR4((Inet4Address) prefix, prefix_length);
		}

		// otherwise, it must be IPv6.
		if (prefix_length > 128)
			throw new UnknownHostException("Invalid prefix length used: "
					+ prefix_length);
		return new CIDR6((Inet6Address) prefix, prefix_length);
	}

	public static CIDR generateCIDR(String cidr) throws UnknownHostException {
		int p = cidr.indexOf("/");
		if (p < 0) {
			throw new UnknownHostException("Invalid CIDR notation used: "
					+ cidr);
		}

		String addr = cidr.substring(0, p);
		String prefix_len_str = cidr.substring(p + 1);

		InetAddress prefix = ARegUtils.addressStringToInet(addr);
		int prefix_length = ARegUtils.parseInt(prefix_len_str, -1);

		if (prefix_length < 0)
			throw new UnknownHostException("Invalid prefix length used: "
					+ prefix_len_str);

		return generateCIDR(prefix, prefix_length);
	}

	public static List<CIDR> generateCIDRs(InetAddress startAddress,
			InetAddress endAddress) {
		if (!startAddress.getClass().equals(endAddress.getClass())) {
			throw new IllegalArgumentException(
					"startAddress and endAddress must both be the same kind of "
							+ "address (IPv4 vs. IPv6).");
		}

		if (startAddress instanceof Inet4Address) {
			return CIDR4.calcIPv4CIDRs(startAddress, endAddress);
		}
		return CIDR6.calcIPv6CIDRs(startAddress, endAddress);
	}

	/** @return the prefix of the CIDR block. */
	public InetAddress getPrefix() {
		return mPrefix;
	}

	/** @return the prefix length. */
	public int getPrefixLength() {
		return mPrefixLength;
	}

	/** @return the textual CIDR notation. */
	public String toString() {
		return mPrefix.getHostAddress() + "/" + mPrefixLength;
	}

	/** @return the end address of this block. */
	public abstract InetAddress getEndAddress();

}
