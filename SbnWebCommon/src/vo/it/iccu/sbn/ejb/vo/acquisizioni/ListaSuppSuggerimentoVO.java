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

public class ListaSuppSuggerimentoVO extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = 1697912953621607040L;
	private int elemXBlocchi;
	private String ticket;
	private String codPolo;
	private String codBibl;
	private String codiceSuggerimento;
	private String statoSuggerimento;
	private String dataSuggerimentoDa;
	private String dataSuggerimentoA;
	private StrutturaCombo titolo;
	private StrutturaCombo bibliotecario;
	private StrutturaCombo sezione;
	private String chiamante;
	private List<SuggerimentoVO> selezioniChiamato;
	private String ordinamento;
	private boolean flag_canc=false;
	private boolean modalitaSif=true;
	private List<Integer> idSugList;




	public ListaSuppSuggerimentoVO (){};
	public ListaSuppSuggerimentoVO (String polo, String bibl, String codSugg, String statoSugg,String dataSuggDa,String dataSuggA, StrutturaCombo titSugg,StrutturaCombo biblSugg, StrutturaCombo sezSugg,  String chiama, String ordina ) throws Exception {
/*		if (prof == null) {
			throw new Exception("Profilo non valido");
		}
*/		this.codPolo = polo;
		this.codBibl = bibl;
		this.codiceSuggerimento=codSugg;
		this.statoSuggerimento=statoSugg;
		this.dataSuggerimentoDa=dataSuggDa;
		this.dataSuggerimentoA=dataSuggA;
		this.titolo=titSugg;
		this.bibliotecario=biblSugg;
		this.sezione=sezSugg;
		this.chiamante = chiama;
		this.ordinamento = ordina;

	}
	public StrutturaCombo getBibliotecario() {
		return bibliotecario;
	}
	public void setBibliotecario(StrutturaCombo bibliotecario) {
		this.bibliotecario = bibliotecario;
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
	public String getCodiceSuggerimento() {
		return codiceSuggerimento;
	}
	public void setCodiceSuggerimento(String codiceSuggerimento) {
		this.codiceSuggerimento = codiceSuggerimento;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public String getDataSuggerimentoA() {
		return dataSuggerimentoA;
	}
	public void setDataSuggerimentoA(String dataSuggerimentoA) {
		this.dataSuggerimentoA = dataSuggerimentoA;
	}
	public String getDataSuggerimentoDa() {
		return dataSuggerimentoDa;
	}
	public void setDataSuggerimentoDa(String dataSuggerimentoDa) {
		this.dataSuggerimentoDa = dataSuggerimentoDa;
	}
	public String getOrdinamento() {
		return ordinamento;
	}
	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}
	public List<SuggerimentoVO> getSelezioniChiamato() {
		return selezioniChiamato;
	}
	public void setSelezioniChiamato(List<SuggerimentoVO> selezioniChiamato) {
		this.selezioniChiamato = selezioniChiamato;
	}
	public StrutturaCombo getSezione() {
		return sezione;
	}
	public void setSezione(StrutturaCombo sezione) {
		this.sezione = sezione;
	}
	public String getStatoSuggerimento() {
		return statoSuggerimento;
	}
	public void setStatoSuggerimento(String statoSuggerimento) {
		this.statoSuggerimento = statoSuggerimento;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public StrutturaCombo getTitolo() {
		return titolo;
	}
	public void setTitolo(StrutturaCombo titolo) {
		this.titolo = titolo;
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
	public boolean isModalitaSif() {
		return modalitaSif;
	}
	public void setModalitaSif(boolean modalitaSif) {
		this.modalitaSif = modalitaSif;
	}
	public List<Integer> getIdSugList() {
		return idSugList;
	}
	public void setIdSugList(List<Integer> idSugList) {
		this.idSugList = idSugList;
	}





}
