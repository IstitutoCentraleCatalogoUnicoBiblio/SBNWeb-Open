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
package it.iccu.sbn.ejb.vo.periodici.esame;

import it.iccu.sbn.ejb.vo.BaseVO;

import java.lang.reflect.Field;

public abstract class SerieBaseVO extends BaseVO {

	private static final long serialVersionUID = 2126981888858746951L;

	private transient SerieBaseVO parent;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + uniqueId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SerieBaseVO other = (SerieBaseVO) obj;
		if (uniqueId != other.uniqueId)
			return false;
		return true;
	}

	/**
	 * metodo di ricerca ricorsivo sul campo {@code uniqueId} utilizzando <b>reflection</b>.
	 * @param id codice univoco dell'oggetto
	 * @return oggetto identificato con {@code uniqueId}, altrimenti {@code null}
	 */

	public SerieBaseVO search(int id) {
		if (this.uniqueId == id)
			return this;

		try {
			Field[] fields = this.getClass().getDeclaredFields();
			for (Field f : fields) {
				boolean isInstance = SerieBaseVO.class.isAssignableFrom(f.getType());
				if (isInstance) {
					f.setAccessible(true);
					SerieBaseVO sb = (SerieBaseVO) f.get(this);
					if (sb != null) {
						SerieBaseVO found = sb.search(id);
						if (found != null)
							return found;
					}
				}
			}

		} catch (Exception e) {
			return null;
		}

		return null;
	}

	public SerieBaseVO getParent() {
		return parent;
	}

	public void setParent(SerieBaseVO parent) {
		this.parent = parent;
	}

}
