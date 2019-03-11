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
package it.iccu.sbn.web.actionforms.servizi.occupazioni;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.RicercaOccupazioneVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class RicercaOccupazioniForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 2168108524044208972L;

	private boolean sessione = false;

	private RicercaOccupazioneVO anaOccup = new RicercaOccupazioneVO();

	private List<ComboCodDescVO> lstProfessioni;


	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public RicercaOccupazioneVO getAnaOccup() {
		return anaOccup;
	}

	public void setAnaOccup(RicercaOccupazioneVO anaOccup) {
		this.anaOccup = anaOccup;
	}

	public List<ComboCodDescVO> getLstProfessioni() {
		return lstProfessioni;
	}

	public void setLstProfessioni(List<ComboCodDescVO> lstProfessioni) {
		this.lstProfessioni = lstProfessioni;
	}

}
