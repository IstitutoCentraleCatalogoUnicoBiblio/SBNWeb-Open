// $Id: CIDR4.java,v 1.1 2010/01/07 15:06:57 almaviva5 Exp $
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
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a class for representing IPv4 CIDR notation networks.
 *
 * @author David Blacka (original)
 * @version $Revision: 1.1 $
 */
public class CIDR4 extends CIDR {
	private int mPrefixInt;

	protected CIDR4(Inet4Address prefix, int prefix_length) {
		mPrefixLength = prefix_length;

		// first we calculate the integer form of the address.
		mPrefixInt = ARegUtils.ipv4AddressToInt(prefix);

		// then we mask and store the (possibly) corrected form of the
		// prefix.
		int mask = ipv4PrefixLengthToMask(prefix_length);

		mPrefixInt &= mask;
		mPrefix = ARegUtils.intToIPv4Address(mPrefixInt);
	}

	public InetAddress getEndAddress() {
		return ARegUtils.intToIPv4Address(getEndAddressInt());
	}

	public int getEndAddressInt() {
		return mPrefixInt + ipv4PrefixLengthToLength(mPrefixLength) - 1;
	}

	/**
	 * Given an IPv4 prefix length, return the block length. I.e., a prefix
	 * length of 24 will return 256.
	 */
	private static int ipv4PrefixLengthToLength(int prefix_length) {
		return 1 << (32 - prefix_length);
	}

	/**
	 * Given a prefix length, return a netmask. I.e, a prefix length of 24 will
	 * return 0xFFFFFF00.
	 */
	private static int ipv4PrefixLengthToMask(int prefix_length) {
		return ~((1 << (32 - prefix_length)) - 1);
	}

	/**
	 * For a given block length, find the largest prefix length that, when
	 * converted back to a length is >= the orginal length. That is, the largest
	 * prefix length that does not create a block larger than length.
	 */
	private static int largestIPv4Prefix(int length) {
		// Note, this is an iterative approach. The equivalent math
		// approach is: 32 - (int) (log2(length). Or, since we don't have
		// a native log-base-two, 32 - (int) (log(length)/log(2)). A
		// simple benchmark shows the iterative approach to be about 14
		// times faster.

		for (int i = 1; i <= 32; i++) {
			if ((length & 0x80000000) != 0)
				return i;
			length <<= 1;
		}
		return 32;
	}

	/**
	 * Given a start and end address, return a list of one or more CIDR objects
	 * that are the equivalent.
	 */
	protected static List<CIDR> calcIPv4CIDRs(InetAddress startaddr,
			InetAddress endaddr) {
		int start = ARegUtils.ipv4AddressToInt(startaddr);
		int end = ARegUtils.ipv4AddressToInt(endaddr);

		int block_length = end - start;

		int prefix_length = largestIPv4Prefix(block_length + 1);

		List<CIDR> res = new ArrayList<CIDR>();

		while (block_length > 0) {
			int mask = ipv4PrefixLengthToMask(prefix_length);
			// check to see if our current network length is valid
			if ((start & mask) != start) {
				// if not, shrink the network length.
				prefix_length++;
				continue;
			}
			// Otherwise, we have a valid CIDR block, so add it to the list
			CIDR4 cidr = new CIDR4((Inet4Address) ARegUtils
					.intToIPv4Address(start), prefix_length);
			res.add(cidr);

			// and setup for the next round.
			int current_length = ipv4PrefixLengthToLength(prefix_length);
			start += current_length;
			block_length -= current_length;
			prefix_length = largestIPv4Prefix(block_length + 1);
		}

		return res;
	}
}
