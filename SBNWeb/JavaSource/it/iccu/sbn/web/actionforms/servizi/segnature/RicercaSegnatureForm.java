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
package it.iccu.sbn.web.actionforms.servizi.segnature;

import it.iccu.sbn.ejb.vo.servizi.segnature.RangeSegnatureVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class RicercaSegnatureForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -7964142932048657683L;

	private boolean sessione   = false;

	private boolean nonTrovato = false;

	private RangeSegnatureVO dettRicercaSegn = new RangeSegnatureVO(0, 0, "", "", "", "", "", "");

	private List lstFruizioni;

	private List lstIndisp;



	public List getLstFruizioni() {
		return lstFruizioni;
	}

	public void setLstFruizioni(List lstFruizioni) {
		this.lstFruizioni = lstFruizioni;
	}

	public List getLstIndisp() {
		return lstIndisp;
	}

	public void setLstIndisp(List lstIndisp) {
		this.lstIndisp = lstIndisp;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public boolean isNonTrovato() {
		return nonTrovato;
	}

	public void setNonTrovato(boolean nonTrovato) {
		this.nonTrovato = nonTrovato;
	}

	public RangeSegnatureVO getDettRicercaSegn() {
		return dettRicercaSegn;
	}

	public void setDettRicercaSegn(RangeSegnatureVO dettRicercaSegn) {
		this.dettRicercaSegn = dettRicercaSegn;
	}
}
