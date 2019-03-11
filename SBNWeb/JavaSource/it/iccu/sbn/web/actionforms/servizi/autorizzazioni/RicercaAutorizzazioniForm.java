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
package it.iccu.sbn.web.actionforms.servizi.autorizzazioni;

import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.RicercaAutorizzazioneVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class RicercaAutorizzazioniForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -8685576338456456642L;
	private boolean sessione = false;
	private RicercaAutorizzazioneVO autRicerca = new RicercaAutorizzazioneVO();
	private List LstTipiOrdinamento;
	//Sarà impostato a true se la ricerca non trova elementi
	private boolean nonTrovato = false;




	public List getLstTipiOrdinamento() {
		return LstTipiOrdinamento;
	}

	public void setLstTipiOrdinamento(List lstTipiOrdinamento) {
		LstTipiOrdinamento = lstTipiOrdinamento;
	}

	public void clear()
	{
		this.autRicerca.setCodice("");
		this.autRicerca.setDescrizione("");
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		//super.reset(arg0, arg1);
		//this.autRicerca = new RicercaAutorizzazioneVO();
		//sessione = false;
	}

	public RicercaAutorizzazioneVO getAutRicerca() {
		return autRicerca;
	}

	public void setAutRicerca(RicercaAutorizzazioneVO autRicerca) {
		this.autRicerca = autRicerca;
	}

	public boolean isNonTrovato() {
		return nonTrovato;
	}

	public void setNonTrovato(boolean nonTrovato) {
		this.nonTrovato = nonTrovato;
	}

}
