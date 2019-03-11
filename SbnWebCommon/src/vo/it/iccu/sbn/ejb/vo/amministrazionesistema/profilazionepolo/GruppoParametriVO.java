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
package it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo;

import it.iccu.sbn.ejb.vo.BaseVO;

import java.util.ArrayList;
import java.util.List;

public class GruppoParametriVO extends BaseVO {

	private static final long serialVersionUID = 4826626321582511036L;

	private List<ParametroVO> elencoParametri = new ArrayList<ParametroVO>();
	private String codice;
	private String descrizione;
	private String indice;
	private String acceso;
	private boolean selezioneCheck;
	private int sequenzaOrdinamento;

	private String cd_tabella;

	public int getSequenzaOrdinamento() {
		return sequenzaOrdinamento;
	}

	public void setSequenzaOrdinamento(int sequenzaOrdinamento) {
		this.sequenzaOrdinamento = sequenzaOrdinamento;
	}

	public List<ParametroVO> getElencoParametri() {
		return elencoParametri;
	}

	public void setElencoParametri(List<ParametroVO> elencoParametri) {
		this.elencoParametri = elencoParametri;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getIndice() {
		return indice;
	}

	public void setIndice(String indice) {
		this.indice = indice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getAcceso() {
		return acceso;
	}

	public void setAcceso(String acceso) {
		this.acceso = acceso;
	}

	public boolean isSelezioneCheck() {
		return selezioneCheck;
	}

	public void setSelezioneCheck(boolean selezioneCheck) {
		this.selezioneCheck = selezioneCheck;
	}

	@Override
	public int getRepeatableId() {
		return (codice + cd_tabella).hashCode();
	}

	public String getCd_tabella() {
		return cd_tabella;
	}

	public void setCd_tabella(String cd_tabella) {
		this.cd_tabella = trimAndSet(cd_tabella);
	}

}
