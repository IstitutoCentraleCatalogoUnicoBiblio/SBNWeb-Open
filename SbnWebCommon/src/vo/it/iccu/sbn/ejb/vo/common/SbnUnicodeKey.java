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
package it.iccu.sbn.ejb.vo.common;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.nio.ReadOnlyBufferException;

public class SbnUnicodeKey extends SerializableVO {

	private static final long serialVersionUID = -4331267974099514137L;

	private String key;
	private String category;
	private String descrizione;
	private String ordKey;
	private boolean _locked = false;

	private static final byte[] toByteArray(String hexStr) {

		byte bArray[] = new byte[hexStr.length() / 2];
		for (int i = 0; i < bArray.length; i++) {
			bArray[i] = (byte) Integer.parseInt(hexStr.substring(
					2 * i, 2 * i + 2), 16);
		}
		return bArray;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public String getKey() {
		return key;
	}

	public String getOrdKey() {
		return ordKey;
	}

	public void setDescrizione(String descrizione) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.descrizione = descrizione;
	}

	public void setKey(String key) {
		if (_locked)
			throw new ReadOnlyBufferException();
		try {
			this.key = new String(toByteArray(key), "UTF-16");
		} catch (Exception e) {
			e.printStackTrace();
			this.key = " ";
		}
	}

	public int getUChar() {
		return key.codePointAt(0);
	}

	public void setOrdKey(String ordKey) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.ordKey = ordKey;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		if (_locked)
			throw new ReadOnlyBufferException();
		this.category = category;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[key: '");
		buffer.append(key);
		buffer.append("' category: '");
		buffer.append(category);
		buffer.append("' descr: '");
		buffer.append(descrizione);
		buffer.append("' ordKey: '");
		buffer.append(ordKey);
		buffer.append("']");
		return buffer.toString();
	}

	public boolean equals(char c) {
		return (key.charAt(0) == c);
	}

	public SbnUnicodeKey lock() {
		this._locked = true;
		return this;
	}

}
