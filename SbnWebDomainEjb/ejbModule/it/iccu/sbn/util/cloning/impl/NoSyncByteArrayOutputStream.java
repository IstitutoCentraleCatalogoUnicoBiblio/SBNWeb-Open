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
package it.iccu.sbn.util.cloning.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class NoSyncByteArrayOutputStream extends OutputStream {

	protected byte buf[];
	protected int count;

	public NoSyncByteArrayOutputStream() {
		this(32);
	}

	public NoSyncByteArrayOutputStream(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Negative initial size: " + size);
		}
		buf = new byte[size];
	}

	public void write(int b) {
		int newcount = count + 1;
		if (newcount > buf.length) {
			byte newbuf[] = new byte[Math.max(buf.length << 1, newcount)];
			System.arraycopy(buf, 0, newbuf, 0, count);
			buf = newbuf;
		}
		buf[count] = (byte) b;
		count = newcount;
	}

	public void write(byte b[], int off, int len) {
		if ((off < 0) || (off > b.length) || (len < 0)
				|| ((off + len) > b.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		int newcount = count + len;
		if (newcount > buf.length) {
			byte newbuf[] = new byte[Math.max(buf.length << 1, newcount)];
			System.arraycopy(buf, 0, newbuf, 0, count);
			buf = newbuf;
		}
		System.arraycopy(b, off, buf, count, len);
		count = newcount;
	}

	public void writeTo(OutputStream out) throws IOException {
		out.write(buf, 0, count);
	}

	public void reset() {
		count = 0;
	}

	public byte toByteArray()[] {
		return buf;
	}

	public int size() {
		return count;
	}

	public String toString() {
		return new String(buf, 0, count);
	}

	public String toString(String enc) throws UnsupportedEncodingException {
		return new String(buf, 0, count, enc);
	}
}
