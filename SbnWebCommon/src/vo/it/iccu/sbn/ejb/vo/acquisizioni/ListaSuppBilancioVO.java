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

import java.util.List;
import java.util.Locale;

public class ListaSuppBilancioVO  extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = -6122620667863864337L;
	private int elemXBlocchi;
	private String ticket;
	private String codPolo;
	private String codBibl;
	private String esercizio;
	private String capitolo;
	private String impegno;
	private String chiamante;
	private List<BilancioVO> selezioniChiamato;
	private String ordinamento;
	private boolean flag_canc=false;
	private boolean ricercaEsamina=false; // per effettuare i calcoli solo in caso di esamina
	Locale loc=Locale.getDefault();
	private boolean modalitaSif=true;



	public ListaSuppBilancioVO (){};
	public ListaSuppBilancioVO (String codP, String codB, String ese, String cap , String imp, String chiama, String ordina  ) throws Exception {
		this.codPolo = codP;
		this.codBibl = codB;
		this.esercizio = ese;
		this.capitolo = cap;
		this.impegno = imp;
		this.chiamante = chiama;
		this.ordinamento = ordina;

	}
	public String getCapitolo() {
		return capitolo;
	}
	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
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
	public String getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(String esercizio) {
		this.esercizio = esercizio;
	}
	public String getImpegno() {
		return impegno;
	}
	public void setImpegno(String impegno) {
		this.impegno = impegno;
	}
	public List<BilancioVO> getSelezioniChiamato() {
		return selezioniChiamato;
	}
	public void setSelezioniChiamato(List<BilancioVO> selezioniChiamato) {
		this.selezioniChiamato = selezioniChiamato;
	}
	public String getOrdinamento() {
		return ordinamento;
	}
	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}
	public int getElemXBlocchi() {
		return elemXBlocchi;
	}
	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}
	public Locale getLoc() {
		return loc;
	}
	public void setLoc(Locale loc) {
		this.loc = loc;
	}
	public boolean isRicercaEsamina() {
		return ricercaEsamina;
	}
	public void setRicercaEsamina(boolean ricercaEsamina) {
		this.ricercaEsamina = ricercaEsamina;
	}
	public boolean isModalitaSif() {
		return modalitaSif;
	}
	public void setModalitaSif(boolean modalitaSif) {
		this.modalitaSif = modalitaSif;
	}

}
