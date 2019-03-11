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
package it.iccu.sbn.ejb.vo.gestionesemantica.thesauro;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DatiFusioneTerminiVO extends CoppiaTerminiVO {

	private static final long serialVersionUID = 748499807067044976L;
	private boolean _cloneTitColl = true;
	private transient AreaDatiPassaggioInterrogazioneTitoloReturnVO titoliCollegati;
	private String[] spostaID;

	public String[] getSpostaID() {
		return spostaID;
	}

	public void setSpostaID(String[] spostaID) {
		this.spostaID = spostaID;
	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getTitoliCollegati() {
		return titoliCollegati;
	}

	public void setTitoliCollegati(AreaDatiPassaggioInterrogazioneTitoloReturnVO titoliCollegati) {
		this.titoliCollegati = titoliCollegati;
	}

	public Object customClone(boolean _cloneTitColl) {
		this._cloneTitColl = _cloneTitColl;
		Object clone = super.clone();
		this._cloneTitColl = true;

		return clone;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
		if (_cloneTitColl)
			s.writeObject(titoliCollegati);
	}

	private void readObject(ObjectInputStream s) throws IOException,
			ClassNotFoundException {
		s.defaultReadObject();
		if (_cloneTitColl)
			titoliCollegati = (AreaDatiPassaggioInterrogazioneTitoloReturnVO) s.readObject();
	}



}
