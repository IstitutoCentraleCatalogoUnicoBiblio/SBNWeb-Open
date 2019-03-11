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

public class ListaSuppOffertaFornitoreVO extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = -689738627097200882L;
	private int elemXBlocchi;
	private String ticket;
	private String codPolo;
	private String codBibl;
	private String identificativoOfferta;
	private StrutturaCombo fornitore;
	private String statoOfferta;
	private StrutturaCombo paese;
	private StrutturaCombo lingua;
	private StrutturaCombo bid;
	private StrutturaCombo chiaveTitoloIsbd;
	private StrutturaCombo autore;
	private StrutturaCombo classificazione;
	private String chiamante;
	private List<OffertaFornitoreVO> selezioniChiamato;
	private String ordinamento;
	private boolean flag_canc=false;
	private boolean modalitaSif=true;


	public ListaSuppOffertaFornitoreVO (){

	}
	public ListaSuppOffertaFornitoreVO (String codP, String codB,  String idOff,  StrutturaCombo forn, String statoOff, StrutturaCombo pae , StrutturaCombo ling,  StrutturaCombo bidOff, StrutturaCombo KTitIsdb,StrutturaCombo aut,StrutturaCombo classif, String chiama, String ordina ) throws Exception {
		//if (ese == null) {
		//	throw new Exception("Esercizio non valido");
		//}
/*		this.codPolo=codP;
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
*/
		this.codPolo=codP;
		this.codBibl=codB;
		this.identificativoOfferta=idOff;
		this.fornitore=forn;
		this.statoOfferta=statoOff;
		this.paese=pae;
		this.lingua=ling;
		this.bid=bidOff;
		this.chiaveTitoloIsbd=KTitIsdb;
		this.autore=aut;
		this.classificazione=classif;
		this.chiamante = chiama;
		this.ordinamento = ordina;
	}
	public StrutturaCombo getAutore() {
		return autore;
	}
	public void setAutore(StrutturaCombo autore) {
		this.autore = autore;
	}
	public StrutturaCombo getBid() {
		return bid;
	}
	public void setBid(StrutturaCombo bid) {
		this.bid = bid;
	}
	public String getChiamante() {
		return chiamante;
	}
	public void setChiamante(String chiamante) {
		this.chiamante = chiamante;
	}
	public StrutturaCombo getChiaveTitoloIsbd() {
		return chiaveTitoloIsbd;
	}
	public void setChiaveTitoloIsbd(StrutturaCombo chiaveTitoloIsbd) {
		this.chiaveTitoloIsbd = chiaveTitoloIsbd;
	}
	public StrutturaCombo getClassificazione() {
		return classificazione;
	}
	public void setClassificazione(StrutturaCombo classificazione) {
		this.classificazione = classificazione;
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
	public StrutturaCombo getFornitore() {
		return fornitore;
	}
	public void setFornitore(StrutturaCombo fornitore) {
		this.fornitore = fornitore;
	}
	public String getIdentificativoOfferta() {
		return identificativoOfferta;
	}
	public void setIdentificativoOfferta(String identificativoOfferta) {
		this.identificativoOfferta = identificativoOfferta;
	}
	public StrutturaCombo getLingua() {
		return lingua;
	}
	public void setLingua(StrutturaCombo lingua) {
		this.lingua = lingua;
	}
	public String getOrdinamento() {
		return ordinamento;
	}
	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}
	public StrutturaCombo getPaese() {
		return paese;
	}
	public void setPaese(StrutturaCombo paese) {
		this.paese = paese;
	}
	public List<OffertaFornitoreVO> getSelezioniChiamato() {
		return selezioniChiamato;
	}
	public void setSelezioniChiamato(List<OffertaFornitoreVO> selezioniChiamato) {
		this.selezioniChiamato = selezioniChiamato;
	}
	public String getStatoOfferta() {
		return statoOfferta;
	}
	public void setStatoOfferta(String statoOfferta) {
		this.statoOfferta = statoOfferta;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
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
}
