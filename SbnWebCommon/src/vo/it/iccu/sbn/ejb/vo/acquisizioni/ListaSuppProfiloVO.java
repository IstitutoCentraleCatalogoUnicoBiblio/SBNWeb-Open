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
package it.iccu.sbn.ejb.vo.acquisizioni;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;

import java.util.List;

public class ListaSuppProfiloVO extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = 721067300981717094L;
	private int elemXBlocchi;
	private String ticket;
	private String codPolo;
	private String codBibl;
	private StrutturaCombo profilo;
	private StrutturaCombo sezione;
	private StrutturaCombo fornitore;
	private String chiamante;
	private List<StrutturaProfiloVO> selezioniChiamato;
	private String ordinamento;
	private boolean flag_canc=false;
	private boolean modalitaSif=true;



	public ListaSuppProfiloVO (){};
	public ListaSuppProfiloVO (String polo, String bibl, StrutturaCombo prof, StrutturaCombo sez, StrutturaCombo lin, StrutturaCombo pae,  String chiama, String ordina ) throws Exception {
		if (prof == null) {
			throw new Exception("Profilo non valido");
		}
		this.codPolo = polo;
		this.codBibl = bibl;
		this.profilo = prof;
		this.sezione = sez;
		this.chiamante = chiama;
		this.ordinamento = ordina;

	}
	public String getChiamante() {
		return chiamante;
	}
	public void setChiamante(String chiamante) {
		this.chiamante = chiamante;
	}
	public String getCodBibl() {
		return codBibl;
	}
	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public String getOrdinamento() {
		return ordinamento;
	}
	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}
	public StrutturaCombo getProfilo() {
		return profilo;
	}
	public void setProfilo(StrutturaCombo profilo) {
		this.profilo = profilo;
	}
	public StrutturaCombo getSezione() {
		return sezione;
	}
	public void setSezione(StrutturaCombo sezione) {
		this.sezione = sezione;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public List<StrutturaProfiloVO> getSelezioniChiamato() {
		return selezioniChiamato;
	}
	public void setSelezioniChiamato(List<StrutturaProfiloVO> selezioniChiamato) {
		this.selezioniChiamato = selezioniChiamato;
	}
	public int getElemXBlocchi() {
		return elemXBlocchi;
	}
	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}
	public StrutturaCombo getFornitore() {
		return fornitore;
	}
	public void setFornitore(StrutturaCombo fornitore) {
		this.fornitore = fornitore;
	}
	public boolean isModalitaSif() {
		return modalitaSif;
	}
	public void setModalitaSif(boolean modalitaSif) {
		this.modalitaSif = modalitaSif;
	}





}
