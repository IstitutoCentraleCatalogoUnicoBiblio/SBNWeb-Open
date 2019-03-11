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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.ArrayList;
import java.util.List;

public class ListaDescrittoriSoggettoVO extends SerializableVO {

	private static final long serialVersionUID = 1994060342680641279L;

	private int countSoggettiCollegatiTuttiDescrittori;

	private List<CountDescrittoreVO> listaDescrittori;

	public int getCountSoggettiCollegatiTuttiDescrittori() {
		return countSoggettiCollegatiTuttiDescrittori;
	}

	public void setCountSoggettiCollegatiTuttiDescrittori(
			int countSoggettiCollegatiTuttiDescrittori) {
		this.countSoggettiCollegatiTuttiDescrittori = countSoggettiCollegatiTuttiDescrittori;
	}

	public List<CountDescrittoreVO> getListaDescrittori() {
		return listaDescrittori;
	}

	public void setListaDescrittori(
			List<CountDescrittoreVO> listaDescrittori) {
		this.listaDescrittori = listaDescrittori;

	}

	public CountDescrittoreVO getCountListaDescrittori(int posizione)
			throws Exception {
		if (this.listaDescrittori != null
				&& this.listaDescrittori.size() > posizione) {
			return this.listaDescrittori.get(posizione);
		}
		throw new Exception();
	}

	public List<String> getSoggettiCollegatiTuttiDescrittori() {
		List<String> ret = new ArrayList<String>();
		if (this.listaDescrittori != null) {
			for (int index = 0; index < this.listaDescrittori.size(); index++)

				ret.add(this.listaDescrittori.get(index).getDescrittore());

		}

		return ret;
	}

}
