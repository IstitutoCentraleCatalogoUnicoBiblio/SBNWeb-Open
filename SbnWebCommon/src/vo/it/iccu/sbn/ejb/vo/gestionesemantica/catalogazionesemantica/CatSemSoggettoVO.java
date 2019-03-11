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
package it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica;

import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaSoggettoVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatSemSoggettoVO extends SBNMarcCommonVO {

	private static final long serialVersionUID = -767947942617772133L;

	private String codSoggettario;
	private String descSoggettario;
	private List<ElementoSinteticaSoggettoVO> listaSoggetti;
	private String codSelezionato;
	private Map<String, String> maxLivelloAutSogg = new HashMap<String, String>();

	public String getCodSelezionato() {
		return codSelezionato;
	}

	public void setCodSelezionato(String codSelezionato) {
		this.codSelezionato = codSelezionato;
	}

	public CatSemSoggettoVO() {
		super();
	}

	public CatSemSoggettoVO(String codSoggettario, String descSoggettario,
			List<ElementoSinteticaSoggettoVO> listaSoggetti ) {
		this.codSoggettario = codSoggettario;
		this.descSoggettario = descSoggettario;
		this.listaSoggetti = listaSoggetti;
	}

	public String getCodSoggettario() {
		return codSoggettario;
	}

	public void setCodSoggettario(String codSoggettario) {
		this.codSoggettario = codSoggettario;
	}

	public String getDescSoggettario() {
		return descSoggettario;
	}

	public void setDescSoggettario(String descSoggettario) {
		this.descSoggettario = descSoggettario;
	}

	public List<ElementoSinteticaSoggettoVO> getListaSoggetti() {
		return listaSoggetti;
	}

	public void setListaSoggetti(List<ElementoSinteticaSoggettoVO> listaSoggetti) {
		this.listaSoggetti = listaSoggetti;
	}

	public Map<String, String> getMaxLivelloAutSogg() {
		return maxLivelloAutSogg;
	}

	public void setMaxLivelloAutSogg(Map<String, String> maxLivelloAutSogg) {
		this.maxLivelloAutSogg = maxLivelloAutSogg;
	}

}
