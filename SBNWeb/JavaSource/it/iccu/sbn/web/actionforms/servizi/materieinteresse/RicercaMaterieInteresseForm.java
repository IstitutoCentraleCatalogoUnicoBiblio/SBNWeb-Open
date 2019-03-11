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
package it.iccu.sbn.web.actionforms.servizi.materieinteresse;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaMateriaVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class RicercaMaterieInteresseForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -5263444173141657865L;
	private boolean sessione = false;
	private RicercaMateriaVO datiRicerca = new RicercaMateriaVO();

	private List<ComboCodDescVO> lstTipiOrdinamento;


	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public List<ComboCodDescVO> getLstTipiOrdinamento() {
		return lstTipiOrdinamento;
	}
	public void setLstTipiOrdinamento(List<ComboCodDescVO> lstTipiOrdinamento) {
		this.lstTipiOrdinamento = lstTipiOrdinamento;
	}


	public RicercaMateriaVO getDatiRicerca() {
		return datiRicerca;
	}
	public void setDatiRicerca(RicercaMateriaVO datiRicerca) {
		this.datiRicerca = datiRicerca;
	}

}





