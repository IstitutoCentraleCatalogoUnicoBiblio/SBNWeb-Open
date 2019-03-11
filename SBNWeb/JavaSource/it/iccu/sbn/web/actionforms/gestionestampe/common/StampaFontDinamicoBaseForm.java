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
package it.iccu.sbn.web.actionforms.gestionestampe.common;

import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;

import java.util.ArrayList;
import java.util.List;

public class StampaFontDinamicoBaseForm extends StampaForm {


	private static final long serialVersionUID = 1488646795170444444L;
	protected List listaFont;
	protected List listaPunti;

	public StampaFontDinamicoBaseForm() throws Exception{
		super();
		inizializza();
	}

	protected void inizializza() throws Exception{
		super.inizializza();
		listaFont=loadListaFont();
		listaPunti=loadListaPunti();
	}

	protected List loadListaPunti() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("09","9");
		lista.add(elem);
		elem = new StrutturaCombo("10","10");
		lista.add(elem);
		elem = new StrutturaCombo("11","11");
		lista.add(elem);
		elem = new StrutturaCombo("12","12");
		lista.add(elem);
		elem = new StrutturaCombo("13","13");
		lista.add(elem);
		elem = new StrutturaCombo("14","14");
		lista.add(elem);
		return lista;
	}

	protected List loadListaFont() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("Arial","Arial");
		lista.add(elem);
		elem = new StrutturaCombo("Times New Roman","Times");
		lista.add(elem);
		elem = new StrutturaCombo("Courier New","Courier");
		lista.add(elem);
		return lista;
	}

	public List getListaFont() {
		return listaFont;
	}

	public void setListaFont(List listaFont) {
		this.listaFont = listaFont;
	}

	public List getListaPunti() {
		return listaPunti;
	}

	public void setListaPunti(List listaPunti) {
		this.listaPunti = listaPunti;
	}
}
