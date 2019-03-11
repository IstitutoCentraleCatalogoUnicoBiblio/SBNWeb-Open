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
package it.iccu.sbn.web.actionforms.servizi.spectitolostudio;

import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.RicercaTitoloStudioVO;
//import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.SpecTitoloStudioVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class RicercaSpecTitoloStudioForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -3784079784553865823L;
	private boolean sessione = false;
	private RicercaTitoloStudioVO datiRicerca = new RicercaTitoloStudioVO();
	private List listaTdS;




	public RicercaTitoloStudioVO getDatiRicerca() {
		return datiRicerca;
	}
	public void setDatiRicerca(RicercaTitoloStudioVO datiRicerca) {
		this.datiRicerca = datiRicerca;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public List getListaTdS() {
		return listaTdS;
	}
	public void setListaTdS(List listaTdS) {
		this.listaTdS = listaTdS;
	}

}
