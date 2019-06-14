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
package it.iccu.sbn.web.exception;

import static java.lang.Math.max;

import java.util.Random;

public class RandomIdGenerator {

	private static final int BUFFER_SIZE = 16;
	private static final char[] hexCode = "0123456789abcdef".toCharArray();
	
	static ThreadLocal<Random> RG = new ThreadLocal<Random>() {
		protected Random initialValue() {
			return new Random();
		}
	};

	private static String printHexBinary(byte[] data) {
		StringBuilder buf = new StringBuilder(data.length * 2);
		for (byte b : data) {
			buf.append(hexCode[(b >> 4) & 0xF]);
			buf.append(hexCode[(b & 0xF)]);
		}
		return buf.toString();
	}

	public static String getId() {
		return getId(BUFFER_SIZE);
	}

	public static String getId(int size) {
		byte[] randomBytes = new byte[max(BUFFER_SIZE, size)];
		RG.get().nextBytes(randomBytes);

		return printHexBinary(randomBytes);
	}

}
