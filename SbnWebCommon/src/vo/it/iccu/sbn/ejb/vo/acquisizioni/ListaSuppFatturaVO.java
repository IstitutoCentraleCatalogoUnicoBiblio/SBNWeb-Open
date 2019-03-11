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

public class ListaSuppFatturaVO  extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = -4035333700110109111L;
	private int elemXBlocchi;
	private String ticket;
	private String codPolo;
	private String codBibl;
	private String annoFattura;
	private String progrFattura;
	private String dataFatturaDa;
	private String dataFatturaA;
	private String numFattura;
	private String dataFattura;
	private String dataRegFattura;
	private String statoFattura;
	private String tipoFattura;
	private double importoFattura; // valore inventariale
	private StrutturaCombo fornitore;
	private StrutturaTerna ordine;
	private StrutturaTerna bilancio;
	private String chiamante;
	private List<FatturaVO> selezioniChiamato;
	private String ordinamento;
	private boolean flag_canc=false;
	private String utente;
	private boolean ricercaOrd=true; // se l'ordine è filtro di ricerca; altrimenti in gestionefatturadadocfisico è solo un valore da passare
	private boolean modalitaSif=true;
	private int IDFatt;
	private int IDFattNC; // x ricerca delle NC con righe legate all'ID della fattura
	private String provenienza="";


	public ListaSuppFatturaVO (){};
	public ListaSuppFatturaVO (String codP, String codB, String annoF, String prgF , String dataDa, String dataA ,  String numF, String dataF, String dataRegF, String  staF,String tipF , StrutturaCombo fornFatt,StrutturaTerna ordFatt, StrutturaTerna bilFatt, String chiama,  String ordina) throws Exception {
		this.codPolo = codP;
		this.codBibl = codB;
		this.annoFattura = annoF;
		this.progrFattura = prgF;
		this.dataFatturaDa=dataDa;
		this.dataFatturaA=dataA;
		this.numFattura = numF;
		this.dataFattura = dataF;
		this.dataRegFattura = dataRegF;
		this.statoFattura = staF;
		this.tipoFattura = tipF;
		this.fornitore = fornFatt;
		this.ordine = ordFatt;
		this.bilancio = bilFatt;

		this.chiamante = chiama;
		this.ordinamento = ordina;
	}


	public String getAnnoFattura() {
		return annoFattura;
	}
	public void setAnnoFattura(String annoFattura) {
		this.annoFattura = annoFattura;
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
	public String getDataFattura() {
		return dataFattura;
	}
	public void setDataFattura(String dataFattura) {
		this.dataFattura = dataFattura;
	}
	public String getDataFatturaA() {
		return dataFatturaA;
	}
	public void setDataFatturaA(String dataFatturaA) {
		this.dataFatturaA = dataFatturaA;
	}
	public String getDataFatturaDa() {
		return dataFatturaDa;
	}
	public void setDataFatturaDa(String dataFatturaDa) {
		this.dataFatturaDa = dataFatturaDa;
	}
	public String getDataRegFattura() {
		return dataRegFattura;
	}
	public void setDataRegFattura(String dataRegFattura) {
		this.dataRegFattura = dataRegFattura;
	}
	public int getElemXBlocchi() {
		return elemXBlocchi;
	}
	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}
	public StrutturaCombo getFornitore() {
		return fornitore;
	}
	public void setFornitore(StrutturaCombo fornitore) {
		this.fornitore = fornitore;
	}
	public String getNumFattura() {
		return numFattura;
	}
	public void setNumFattura(String numFattura) {
		this.numFattura = numFattura;
	}
	public String getOrdinamento() {
		return ordinamento;
	}
	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}
	public StrutturaTerna getOrdine() {
		return ordine;
	}
	public void setOrdine(StrutturaTerna ordine) {
		this.ordine = ordine;
	}
	public String getProgrFattura() {
		return progrFattura;
	}
	public void setProgrFattura(String progrFattura) {
		this.progrFattura = progrFattura;
	}
	public List<FatturaVO> getSelezioniChiamato() {
		return selezioniChiamato;
	}
	public void setSelezioniChiamato(List<FatturaVO> selezioniChiamato) {
		this.selezioniChiamato = selezioniChiamato;
	}
	public String getStatoFattura() {
		return statoFattura;
	}
	public void setStatoFattura(String statoFattura) {
		this.statoFattura = statoFattura;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getTipoFattura() {
		return tipoFattura;
	}
	public void setTipoFattura(String tipoFattura) {
		this.tipoFattura = tipoFattura;
	}
	public StrutturaTerna getBilancio() {
		return bilancio;
	}
	public void setBilancio(StrutturaTerna bilancio) {
		this.bilancio = bilancio;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}
	public double getImportoFattura() {
		return importoFattura;
	}
	public void setImportoFattura(double importoFattura) {
		this.importoFattura = importoFattura;
	}
	public String getUtente() {
		return utente;
	}
	public void setUtente(String utente) {
		this.utente = utente;
	}
	public boolean isRicercaOrd() {
		return ricercaOrd;
	}
	public void setRicercaOrd(boolean ricercaOrd) {
		this.ricercaOrd = ricercaOrd;
	}
	public boolean isModalitaSif() {
		return modalitaSif;
	}
	public void setModalitaSif(boolean modalitaSif) {
		this.modalitaSif = modalitaSif;
	}
	public int getIDFatt() {
		return IDFatt;
	}
	public void setIDFatt(int fatt) {
		IDFatt = fatt;
	}
	public int getIDFattNC() {
		return IDFattNC;
	}
	public void setIDFattNC(int fattNC) {
		IDFattNC = fattNC;
	}
	public String getProvenienza() {
		return provenienza;
	}
	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

}
