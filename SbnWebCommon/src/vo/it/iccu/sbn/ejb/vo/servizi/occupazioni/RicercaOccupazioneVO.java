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
package it.iccu.sbn.ejb.vo.servizi.occupazioni;

import it.iccu.sbn.ejb.vo.common.RicercaBaseVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;

import java.util.List;

public class RicercaOccupazioneVO extends RicercaBaseVO<Object> {

	private static final long serialVersionUID = 5597215856125465858L;
	private int id_occupazioni;
	private String professione;
	private String desProfessione;
	private String codOccupazione;
	private String desOccupazione;
	private List<ComboCodDescVO> lstTipiOrdinamento;

	public String getCodOccupazione() {
		return codOccupazione;
	}

	public void setCodOccupazione(String codOccupazione) {
		this.codOccupazione = codOccupazione;
	}

	public String getDesOccupazione() {
		return desOccupazione;
	}

	public void setDesOccupazione(String desOccupazione) {
		this.desOccupazione = desOccupazione;
	}

	public List<ComboCodDescVO> getLstTipiOrdinamento() {
		return lstTipiOrdinamento;
	}

	public void setLstTipiOrdinamento(List<ComboCodDescVO> lstTipiOrdinamento) {
		this.lstTipiOrdinamento = lstTipiOrdinamento;
	}

	public String getProfessione() {
		return professione;
	}

	public void setProfessione(String professione) {
		int pos = professione.indexOf('\t');
		if (pos == -1) {
			this.professione = professione;
			this.desProfessione = "";
		} else {
			this.professione = professione.substring(0, pos);
			this.desProfessione = professione.substring(pos + 1);
		}
	}

	public int getId_occupazioni() {
		return id_occupazioni;
	}

	public void setId_occupazioni(int id_occupazioni) {
		this.id_occupazioni = id_occupazioni;
	}

	public String getDesProfessione() {
		return desProfessione;
	}

	public void setDesProfessione(String desProfessione) {
		this.desProfessione = desProfessione;
	}

}
