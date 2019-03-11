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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.ArrayList;
import java.util.List;


public class AreaDatiLocalizzazioniAuthorityMultiplaVO extends SerializableVO {

	private static final long serialVersionUID = 4169593348705950043L;

	private List<AreaDatiLocalizzazioniAuthorityVO> listaAreaLocalizVO = new ArrayList<AreaDatiLocalizzazioniAuthorityVO>();
	private String codErr;
	private String testoErr;

	public List<AreaDatiLocalizzazioniAuthorityVO> getListaAreaLocalizVO() {
		return listaAreaLocalizVO;
	}

	public void setListaAreaLocalizVO(
			List<AreaDatiLocalizzazioniAuthorityVO> listaAreaLocalizVO) {
		this.listaAreaLocalizVO = listaAreaLocalizVO;
	}

	public List<AreaDatiLocalizzazioniAuthorityVO> addListaAreaLocalizVO(AreaDatiLocalizzazioniAuthorityVO eleAreaLocalizVO) {
		listaAreaLocalizVO.add(eleAreaLocalizVO);
		return listaAreaLocalizVO;
	}

	public String getCodErr() {
		return codErr;
	}

	public void setCodErr(String codErr) {
		this.codErr = codErr;
	}

	public String getTestoErr() {
		return testoErr;
	}

	public void setTestoErr(String testoErr) {
		this.testoErr = testoErr;
	}


}
