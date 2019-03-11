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

public class ListaSuppSezioneVO extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = -3971771081999126659L;
	private int elemXBlocchi;
	private String ticket;
	private String codPolo;
	private String codBibl;
	private int idSezione;
	private String codiceSezione;
	private String descrizioneSezione;
	private String chiamante;
	private List<SezioneVO> selezioniChiamato;
	private String esercizio; // per gestire l'esame spesa
	private String dataOrdineDa; // per gestire l'esame spesa
	private String dataOrdineA; // per gestire l'esame spesa
	private String ordinamento;
	private boolean flag_canc=false;
	Locale loc=Locale.getDefault();
	private boolean modalitaSif=true;
	private boolean chiusura=false;
	private boolean storia=false;

	public ListaSuppSezioneVO (){

	}
	public ListaSuppSezioneVO (String codP, String codB, String codSez, String desSez , String chiama, String ordina ) throws Exception {
		//if (ese == null) {
		//	throw new Exception("Esercizio non valido");
		//}
		this.codPolo=codP;
		this.codBibl=codB;
		this.codiceSezione=codSez;
		this.descrizioneSezione=desSez;
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
	public String getCodiceSezione() {
		return codiceSezione;
	}
	public void setCodiceSezione(String codiceSezione) {
		this.codiceSezione = codiceSezione;
	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public String getDescrizioneSezione() {
		return descrizioneSezione;
	}
	public void setDescrizioneSezione(String descrizioneSezione) {
		this.descrizioneSezione = descrizioneSezione;
	}
	public String getOrdinamento() {
		return ordinamento;
	}
	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}
	public List<SezioneVO> getSelezioniChiamato() {
		return selezioniChiamato;
	}
	public void setSelezioniChiamato(List<SezioneVO> selezioniChiamato) {
		this.selezioniChiamato = selezioniChiamato;
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
	public int getIdSezione() {
		return idSezione;
	}
	public void setIdSezione(int idSezione) {
		this.idSezione = idSezione;
	}
	public String getDataOrdineA() {
		return dataOrdineA;
	}
	public void setDataOrdineA(String dataOrdineA) {
		this.dataOrdineA = dataOrdineA;
	}
	public String getDataOrdineDa() {
		return dataOrdineDa;
	}
	public void setDataOrdineDa(String dataOrdineDa) {
		this.dataOrdineDa = dataOrdineDa;
	}
	public String getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(String esercizio) {
		this.esercizio = esercizio;
	}
	public Locale getLoc() {
		return loc;
	}
	public void setLoc(Locale loc) {
		this.loc = loc;
	}
	public boolean isModalitaSif() {
		return modalitaSif;
	}
	public void setModalitaSif(boolean modalitaSif) {
		this.modalitaSif = modalitaSif;
	}
	public boolean isChiusura() {
		return chiusura;
	}
	public void setChiusura(boolean chiusura) {
		this.chiusura = chiusura;
	}
	public boolean isStoria() {
		return storia;
	}
	public void setStoria(boolean storia) {
		this.storia = storia;
	}

}
