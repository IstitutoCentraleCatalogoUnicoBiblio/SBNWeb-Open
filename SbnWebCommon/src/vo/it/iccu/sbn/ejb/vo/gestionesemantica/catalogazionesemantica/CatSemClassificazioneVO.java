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
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SinteticaClasseVO;

import java.util.List;

public class CatSemClassificazioneVO extends SBNMarcCommonVO {

	private static final long serialVersionUID = 2872355268886031853L;
	private String codClassificazione;
	private String descClassificazione;
	private String codEdizione;
	private String descEdizione;
	private List<SinteticaClasseVO> listaClassi;
	private List<SinteticaClasseVO> listaClassiNew;
	private String codNotazioneSelezionato;

	public CatSemClassificazioneVO() {
		super();
	}

	public CatSemClassificazioneVO(String codClassificazione,
			String descClassificazione, List<SinteticaClasseVO> listaClassi) {
		this.codClassificazione = codClassificazione;
		this.descClassificazione = descClassificazione;
		this.listaClassi = listaClassi;
	}

	public String getCodClassificazione() {
		return codClassificazione;
	}

	public void setCodClassificazione(String codClassificazione) {
		this.codClassificazione = codClassificazione;
	}

	public String getCodNotazioneSelezionato() {
		return codNotazioneSelezionato;
	}

	public void setCodNotazioneSelezionato(String codNotazioneSelezionato) {
		this.codNotazioneSelezionato = codNotazioneSelezionato;
	}

	public String getDescClassificazione() {
		return descClassificazione;
	}

	public void setDescClassificazione(String descClassificazione) {
		this.descClassificazione = descClassificazione;
	}

	public List<SinteticaClasseVO> getListaClassi() {
		return listaClassi;
	}

	public void setListaClassi(List<SinteticaClasseVO> listaClassi) {
		this.listaClassi = listaClassi;
	}

	public String getCodEdizione() {
		return codEdizione;
	}

	public void setCodEdizione(String codEdizione) {
		this.codEdizione = trimOrFill(codEdizione, ' ', 2);
	}

	public String getDescEdizione() {
		return descEdizione;
	}

	public void setDescEdizione(String descEdizione) {
		this.descEdizione = descEdizione;
	}

	public void add(SinteticaClasseVO listaClassiNew) {
		// TODO Auto-generated method stub

	}

	public List<SinteticaClasseVO> getListaClassiNew() {
		return listaClassiNew;
	}

	public void setListaClassiNew(List<SinteticaClasseVO> listaClassiNew) {
		this.listaClassiNew = listaClassiNew;
	}



}
