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
package it.iccu.sbn.ejb.vo.gestionesemantica.classificazione;

import it.iccu.sbn.ejb.vo.common.SBNMarcCommonVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemClassificazioneVO;

import java.util.List;


public class RicercaClasseListaVO extends SBNMarcCommonVO {

	private static final long serialVersionUID = -9179702032662731103L;
	//contiene una lista di oggetti di tipo SinteticaClasseVO.java
	private SistemaClassificazioneVO sistema;
	private EdDeweyVO edizione;
	private List<SinteticaClasseVO> risultati;


	public List<SinteticaClasseVO> getRisultati() {
		return risultati;
	}

	public void setRisultati(List<SinteticaClasseVO> risultati) {
		this.risultati = risultati;
	}

	public EdDeweyVO getEdizione() {
		return edizione;
	}

	public RicercaClasseListaVO(CatSemClassificazioneVO soggClassi) {
		super(soggClassi);
		this.risultati = soggClassi.getListaClassi();
	}

	public RicercaClasseListaVO() {
		super();
	}

	public void setEdizione(EdDeweyVO edizione) {
		this.edizione = edizione;
	}

	public SistemaClassificazioneVO getSistema() {
		return sistema;
	}

	public void setSistema(SistemaClassificazioneVO sistema) {
		this.sistema = sistema;
	}

}
