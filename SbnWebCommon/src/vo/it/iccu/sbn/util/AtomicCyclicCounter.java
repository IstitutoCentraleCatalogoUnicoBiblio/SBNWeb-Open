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

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCyclicCounter extends SerializableVO {

	private static final long serialVersionUID = -1498440727941508749L;

	private final AtomicInteger _id = new AtomicInteger(-1);

	public int getNextValue() {
		if (_id.compareAndSet(Integer.MAX_VALUE, 0) )
			return 0;

		return _id.incrementAndGet();
	}

	public int get() {
		return _id.get();
	}

	public void set(int newValue) {
		_id.set(newValue);
	}

}
