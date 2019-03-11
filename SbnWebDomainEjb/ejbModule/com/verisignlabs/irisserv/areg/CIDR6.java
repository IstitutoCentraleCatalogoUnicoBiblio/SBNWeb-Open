// $Id: CIDR6.java,v 1.1 2010/01/07 15:06:57 almaviva5 Exp $
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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is a class for representing IPv6 CIDR notation networks.
 *
 * @author David Blacka (original)
 * @version $Revision: 1.1 $
 */
public class CIDR6 extends CIDR {
	private BigInteger mPrefixBigInt;
	private Log log = LogFactory.getLog(this.getClass());

	protected CIDR6(Inet6Address prefix, int prefix_length) {
		mPrefixLength = prefix_length;

		// First we calculate the bigint form of the address.
		mPrefixBigInt = ARegUtils.ipv6AddressToBigInteger(prefix);

		// Then we mask and store the (possibly) corrected form of the
		// prefix.
		BigInteger mask = ipv6PrefixLengthToMask(prefix_length);

		try {
			mPrefixBigInt = mPrefixBigInt.and(mask);
			mPrefix = ARegUtils.bigIntToIPv6Address(mPrefixBigInt);
		} catch (UnknownHostException e) {
			// this should never happen.
		}
	}

	public InetAddress getEndAddress() {
		try {
			return ARegUtils.bigIntToIPv6Address(getEndAddressBigInt());
		} catch (UnknownHostException e) {
			log.error("invalid ip address calculated as an end address");
			return null;
		}
	}

	public BigInteger getEndAddressBigInt() {
		return mPrefixBigInt.add(ipv6PrefixLengthToLength(mPrefixLength))
				.subtract(BigInteger.ONE);
	}

	/**
	 * Given an IPv6 prefix length, return the block length. I.e., a prefix
	 * length of 96 will return 2**32.
	 */
	private static BigInteger ipv6PrefixLengthToLength(int prefix_length) {
		return BigInteger.ONE.shiftLeft(128 - prefix_length);
	}

	private static BigInteger ipv6PrefixLengthToMask(int prefix_length) {
		return BigInteger.ONE.shiftLeft(128 - prefix_length).subtract(
				BigInteger.ONE).not();
	}

	private static int largestIPv6Prefix(BigInteger length) {
		double l = length.doubleValue();
		return 128 - (int) (Math.log(l) / Math.log(2.0));
	}

	protected static List<CIDR> calcIPv6CIDRs(InetAddress startaddr,
			InetAddress endaddr) {
		// Get the starting and ending addresses as BigIntegers.
		BigInteger start = new BigInteger(startaddr.getAddress());
		BigInteger end = new BigInteger(endaddr.getAddress());

		// Calculate our block length
		BigInteger block_length = end.subtract(start);

		// and our largest prefix length that will fit.
		int prefix_length = largestIPv6Prefix(block_length.add(BigInteger.ONE));

		List<CIDR> res = new ArrayList<CIDR>();

		// while block_length > 0.
		while (block_length.compareTo(BigInteger.ZERO) > 0) {
			BigInteger mask = ipv6PrefixLengthToMask(prefix_length);

			// Check to see if the current prefix length is valid.
			if (start.and(mask).compareTo(start) != 0) {
				// if not, shrink the CIDR network length (i.e., lengthen the
				// prefix).
				prefix_length++;
				continue;
			}

			// otherwise, we have found a valid CIDR block, so add it to the
			// result set.
			Inet6Address prefix_addr = null;
			try {
				prefix_addr = (Inet6Address) ARegUtils
						.bigIntToIPv6Address(start);
			} catch (UnknownHostException e) {
			}

			CIDR6 cidr = new CIDR6(prefix_addr, prefix_length);
			res.add(cidr);

			// and setup for the next round.
			BigInteger current_length = ipv6PrefixLengthToLength(prefix_length);
			start = start.add(current_length);
			block_length = block_length.subtract(current_length);
			prefix_length = largestIPv6Prefix(block_length.add(BigInteger.ONE));
		}

		return res;
	}
}
