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
package it.iccu.sbn.ejb.vo;

import it.iccu.sbn.util.AtomicCyclicCounter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class UniqueIdentifiableVO extends SerializableVO implements Comparable<UniqueIdentifiableVO> {

	private static final long serialVersionUID = -8758089532111426995L;
	private static final AtomicCyclicCounter cnt = new AtomicCyclicCounter();

	public static final <T extends UniqueIdentifiableVO> T search(int uniqueId, Collection<T> target) {
		if (!isFilled(target))
			return null;

		for (T u : target)
			if (u.uniqueId == uniqueId)
				return u;

		return null;
	}

	public static final <T extends UniqueIdentifiableVO> List<T> search(Integer[] uniqueIds, Collection<T> target) {
		List<T> output = new ArrayList<T>();
		if (!isFilled(target))
			return null;

		for (T u : target)
			for (Integer id : uniqueIds)
				if (u.uniqueId == id)
					output.add(u);

		return output;
	}

	public static final <T extends UniqueIdentifiableVO> int indexOfUniqueId(int uniqueId, Collection<T> target) {
		if (!isFilled(target))
			return -1;

		int pos = -1;

		for (T u : target) {
			pos++;
			if (u.getUniqueId() == uniqueId)
				return pos;
		}

		return -1;
	}

	public static final <T extends UniqueIdentifiableVO> T searchRepeatableId(int repeatableId, Collection<T> target) {
		if (!isFilled(target))
			return null;

		for (T u : target)
			if (u.getRepeatableId() == repeatableId)
				return u;

		return null;
	}

	public static final <T extends UniqueIdentifiableVO> int indexOfRepeatableId(int repeatableId, Collection<T> target) {
		if (!isFilled(target))
			return -1;

		int pos = -1;

		for (T u : target) {
			pos++;
			if (u.getRepeatableId() == repeatableId)
				return pos;
		}

		return -1;
	}

	protected final int uniqueId;
	protected final Timestamp creationTime;

	{
		uniqueId = cnt.getNextValue();
		creationTime = new Timestamp(System.currentTimeMillis());
	}

	public final int getUniqueId() {
		return uniqueId;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public boolean older(UniqueIdentifiableVO other) {
		return creationTime.before(other.creationTime);
	}

	public boolean newer(UniqueIdentifiableVO other) {
		return creationTime.after(other.creationTime);
	}

	public int compareTo(UniqueIdentifiableVO o) {
		return uniqueId - o.uniqueId;
	}

	public int getRepeatableId() {
		return this.hashCode();
	}

}
