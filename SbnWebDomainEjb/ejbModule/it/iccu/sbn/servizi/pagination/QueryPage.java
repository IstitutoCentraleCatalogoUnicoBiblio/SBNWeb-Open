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
package it.iccu.sbn.servizi.pagination;

import java.io.Serializable;
import java.util.List;

public interface QueryPage extends Serializable {

	public enum State {
		OK,
		NOT_FOUND,
		ERROR;
	}

	public String getIdLista();

	public int getLastRow();

	public List<Serializable> getLista();

	public int getNumBlocco();

	public int getRowCount();

	public int getStartOffset();

	public int getTotRighe();

	public int getTotBlocchi();

	public int getMaxRighe();

	public State getState();
}
