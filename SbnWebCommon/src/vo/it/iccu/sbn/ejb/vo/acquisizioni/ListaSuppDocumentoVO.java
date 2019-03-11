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
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;

import java.util.List;

public class ListaSuppDocumentoVO extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = 4633441364740433900L;
	private int elemXBlocchi;
	private String ticket;
	private String codPolo;
	private String codBibl;
	private String codDocumento;
	private String tipoDocLet="S";
	private String statoSuggerimentoDocumento;
	private StrutturaTerna utente; //campo1 codbibl di appartenza utente + campo2 prg utente, campo 3 nominativo
	private StrutturaCombo titolo;
	private String dataSuggerimentoDocDa;
	private String dataSuggerimentoDocA;
	private String editore;
	private String luogoEdizione;
	private StrutturaCombo paese;
	private StrutturaCombo lingua;
	private String annoEdizione;
	private String chiamante;
	private List<DocumentoVO> selezioniChiamato;
	private String ordinamento;
	private boolean flag_canc=false;
	private boolean modalitaSif=true;
	private List<Integer> idDocList;


	public ListaSuppDocumentoVO (){

	}
	public ListaSuppDocumentoVO (String codP, String codB, String codDoc, String statoSuggDoc, StrutturaTerna ute, StrutturaCombo tit, String dataDa, String dataA,  String edi, String luogo, StrutturaCombo pae, StrutturaCombo lin, String annoEdi,   String chiama, String ordina ) throws Exception {
		//if (ese == null) {
		//	throw new Exception("Esercizio non valido");
		//}
		this.codPolo=codP;
		this.codBibl=codB;
		this.codDocumento=codDoc;
		this.statoSuggerimentoDocumento=statoSuggDoc;
		this.utente=ute;
		this.titolo=tit;
		this.dataSuggerimentoDocDa=dataDa;
		this.dataSuggerimentoDocA=dataA;
		this.editore=edi;
		this.luogoEdizione=luogo;
		this.paese=pae;
		this.lingua=lin;
		this.annoEdizione=annoEdi;
		this.chiamante = chiama;
		this.ordinamento = ordina;
	}
	public String getAnnoEdizione() {
		return annoEdizione;
	}
	public void setAnnoEdizione(String annoEdizione) {
		this.annoEdizione = annoEdizione;
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
	public String getCodDocumento() {
		return codDocumento;
	}
	public void setCodDocumento(String codDocumento) {
		this.codDocumento = codDocumento;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public String getDataSuggerimentoDocA() {
		return dataSuggerimentoDocA;
	}
	public void setDataSuggerimentoDocA(String dataSuggerimentoDocA) {
		this.dataSuggerimentoDocA = dataSuggerimentoDocA;
	}
	public String getDataSuggerimentoDocDa() {
		return dataSuggerimentoDocDa;
	}
	public void setDataSuggerimentoDocDa(String dataSuggerimentoDocDa) {
		this.dataSuggerimentoDocDa = dataSuggerimentoDocDa;
	}
	public String getEditore() {
		return editore;
	}
	public void setEditore(String editore) {
		this.editore = editore;
	}

	public String getLuogoEdizione() {
		return luogoEdizione;
	}
	public void setLuogoEdizione(String luogoEdizione) {
		this.luogoEdizione = luogoEdizione;
	}
	public String getOrdinamento() {
		return ordinamento;
	}
	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}

	public List<DocumentoVO> getSelezioniChiamato() {
		return selezioniChiamato;
	}
	public void setSelezioniChiamato(List<DocumentoVO> selezioniChiamato) {
		this.selezioniChiamato = selezioniChiamato;
	}
	public String getStatoSuggerimentoDocumento() {
		return statoSuggerimentoDocumento;
	}
	public void setStatoSuggerimentoDocumento(String statoSuggerimentoDocumento) {
		this.statoSuggerimentoDocumento = statoSuggerimentoDocumento;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getTipoDocLet() {
		return tipoDocLet;
	}
	public void setTipoDocLet(String tipoDocLet) {
		this.tipoDocLet = tipoDocLet;
	}
	public StrutturaCombo getTitolo() {
		return titolo;
	}
	public void setTitolo(StrutturaCombo titolo) {
		this.titolo = titolo;
	}
	public StrutturaTerna getUtente() {
		return utente;
	}
	public void setUtente(StrutturaTerna utente) {
		this.utente = utente;
	}
	public StrutturaCombo getLingua() {
		return lingua;
	}
	public void setLingua(StrutturaCombo lingua) {
		this.lingua = lingua;
	}
	public StrutturaCombo getPaese() {
		return paese;
	}
	public void setPaese(StrutturaCombo paese) {
		this.paese = paese;
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
	public List<Integer> getIdDocList() {
		return idDocList;
	}
	public void setIdDocList(List<Integer> idDocList) {
		this.idDocList = idDocList;
	}

}
