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
package it.iccu.sbn.ejb.vo.servizi.sale;

import it.iccu.sbn.web.constant.ConstantDefault;

public class RicercaSalaVO extends SalaVO {

	private static final long serialVersionUID = 4307440174299597421L;

	private String ordinamento;
	private int elementiPerBlocco = Integer.parseInt(ConstantDefault.ELEMENTI_BLOCCHI.getDefault());
	private boolean conPostiLiberi;

	public String getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}

	public int getElementiPerBlocco() {
		return elementiPerBlocco;
	}

	public void setElementiPerBlocco(int elementiPerBlocco) {
		this.elementiPerBlocco = elementiPerBlocco;
	}

	public boolean isConPostiLiberi() {
		return conPostiLiberi;
	}

	public void setConPostiLiberi(boolean conPostiLiberi) {
		this.conPostiLiberi = conPostiLiberi;
	}

}
