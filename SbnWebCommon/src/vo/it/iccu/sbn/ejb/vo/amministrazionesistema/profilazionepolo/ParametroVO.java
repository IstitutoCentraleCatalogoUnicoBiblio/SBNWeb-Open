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
import it.iccu.sbn.ejb.vo.common.ComboVO;

import java.util.ArrayList;
import java.util.List;

public class ParametroVO extends BaseVO {

	private static final long serialVersionUID = 1736912312680215346L;

	public enum ParametroType {
		LIVELLO_AUTORITA,
		ALTRO;
	}

	private String descrizione;
	private List<ComboVO> elencoScelte = new ArrayList<ComboVO>();
	private String index;
	private String selezione;
	private String tipo;
	private String congelato;
	private List<String> radioOptions = new ArrayList<String>();
	private List<CheckVO> elencoCheck = new ArrayList<CheckVO>();

	private ParametroType type = ParametroType.ALTRO;

	public List<String> getRadioOptions() {
		return radioOptions;
	}

	public void setRadioOptions(List<String> radioOptions) {
		this.radioOptions = radioOptions;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<ComboVO> getElencoScelte() {
		return elencoScelte;
	}

	public void setElencoScelte(List<ComboVO> elencoScelte) {
		this.elencoScelte = elencoScelte;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getSelezione() {
		return selezione;
	}

	public void setSelezione(String selezione) {
		this.selezione = selezione;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCongelato() {
		return congelato;
	}

	public void setCongelato(String congelato) {
		this.congelato = congelato;
	}

	public List<CheckVO> getElencoCheck() {
		return elencoCheck;
	}

	public void setElencoCheck(List<CheckVO> elencoCheck) {
		this.elencoCheck = elencoCheck;
	}

	@Override
	public int getRepeatableId() {
		return (tipo + descrizione).hashCode();
	}

	public ParametroType getType() {
		return type;
	}

	public void setType(ParametroType type) {
		this.type = type;
	}

}
