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
package it.finsiel.sbn.util;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

public class BinaryBlobType extends DataTypeCHAR {
	public int[] sqlTypes() {
		return new int[] { Types.BLOB };
	}

	public Class<byte[]> returnedClass() {
		return byte[].class;
	}

	public boolean equals(Object x, Object y) {
		return (x == y)
				|| (x != null && y != null && java.util.Arrays.equals(
						(byte[]) x, (byte[]) y));
	}

	public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
			throws HibernateException, SQLException {
		Blob blob = rs.getBlob(names[0]);
		if (blob == null)
			return null;

		return blob.getBytes(1, (int) blob.length());
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index)
			throws HibernateException, SQLException {
		st.setBlob(index, value != null ? Hibernate.createBlob((byte[]) value) : null);
	}

	public Object deepCopy(Object value) {
		if (value == null)
			return null;

		byte[] bytes = (byte[]) value;
		byte[] result = new byte[bytes.length];
		System.arraycopy(bytes, 0, result, 0, bytes.length);

		return result;
	}

	public boolean isMutable() {
		return true;
	}
}
