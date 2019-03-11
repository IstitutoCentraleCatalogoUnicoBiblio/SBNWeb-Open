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
package it.iccu.sbn.ejb.vo.servizi.spectitolostudio;

import it.iccu.sbn.ejb.vo.common.RicercaBaseVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;

import java.util.List;

public class RicercaTitoloStudioVO extends RicercaBaseVO<Object> {

	private static final long serialVersionUID = 3735654108240575490L;

	private int id_specificita_titoli_studio;
	private List<ComboCodDescVO> lstTipiOrdinamento;
	private String titoloStudio;
	private String desTitoloStudio;
	private String codSpecialita;
	private String desSpecialita;

	public String getCodSpecialita() {
		return codSpecialita;
	}

	public void setCodSpecialita(String codSpecialita) {
		this.codSpecialita = codSpecialita;
	}

	public String getDesSpecialita() {
		return desSpecialita;
	}

	public void setDesSpecialita(String desSpecialita) {
		this.desSpecialita = desSpecialita;
	}

	public List<ComboCodDescVO> getLstTipiOrdinamento() {
		return lstTipiOrdinamento;
	}

	public void setLstTipiOrdinamento(List<ComboCodDescVO> lstTipiOrdinamento) {
		this.lstTipiOrdinamento = lstTipiOrdinamento;
	}

	public String getTitoloStudio() {
		return titoloStudio;
	}

	public void setTitoloStudio(String titoloStudio) {
		int pos = titoloStudio.indexOf('\t');
		if (pos == -1) {
			this.titoloStudio = titoloStudio;
			this.desTitoloStudio = "";
		} else {
			this.titoloStudio = titoloStudio.substring(0, pos);
			this.desTitoloStudio = titoloStudio.substring(pos + 1);
		}
	}

	public int getId_specificita_titoli_studio() {
		return id_specificita_titoli_studio;
	}

	public void setId_specificita_titoli_studio(int id_specificita_titoli_studio) {
		this.id_specificita_titoli_studio = id_specificita_titoli_studio;
	}

	public String getDesTitoloStudio() {
		return desTitoloStudio;
	}

	public void setDesTitoloStudio(String desTitoloStudio) {
		this.desTitoloStudio = desTitoloStudio;
	}

}
