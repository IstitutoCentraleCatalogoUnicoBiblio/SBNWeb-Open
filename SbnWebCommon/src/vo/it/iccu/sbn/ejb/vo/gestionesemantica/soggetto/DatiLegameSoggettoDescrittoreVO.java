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
package it.iccu.sbn.ejb.vo.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;

import java.util.List;


public class DatiLegameSoggettoDescrittoreVO extends SBNMarcCommonVO {

	/**
	 *
	 */
	private static final long serialVersionUID = 4036689290535381256L;
	private String codiceSoggettario;
	private String cid;
	private String cidLivelloAut;
	private List<LegameSogDesVO> legami;

	public String getCodiceSoggettario() {
		return codiceSoggettario;
	}

	public void setCodiceSoggettario(String codiceSoggettario) {
		this.codiceSoggettario = codiceSoggettario;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCidLivelloAut() {
		return cidLivelloAut;
	}

	public void setCidLivelloAut(String cidLivelloAut) {
		this.cidLivelloAut = cidLivelloAut;
	}

	public List<LegameSogDesVO> getLegami() {
		return legami;
	}

	public void setLegami(List<LegameSogDesVO> legami) {
		this.legami = legami;
	}



}
