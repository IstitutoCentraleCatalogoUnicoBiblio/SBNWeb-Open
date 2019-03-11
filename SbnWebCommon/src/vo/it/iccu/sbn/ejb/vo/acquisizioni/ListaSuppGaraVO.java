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

public class ListaSuppGaraVO extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = 2001124181427119791L;
	private String ticket;
	private String codPolo;
	private String codBibl;
	private StrutturaCombo bid;
	private String codRicOfferta;
	private String dataRicOfferta;
	private String statoRicOfferta;
	private String chiamante;
	private List<GaraVO> selezioniChiamato;
	private String ordinamento;
	private int elemXBlocchi;
	private boolean flag_canc=false;
	private boolean modalitaSif=true;


	//costruttore
	public ListaSuppGaraVO (){};
	public ListaSuppGaraVO (String codP, String codB, StrutturaCombo idtitolo, String codRich, String dataRich,String statoRich, String chiama, String ordina )
	throws Exception {

		this.codPolo = codP;
		this.codBibl = codB;
		this.bid = idtitolo;
		this.codRicOfferta = codRich;
		this.dataRicOfferta = dataRich;
		this.statoRicOfferta = statoRich;
		this.chiamante = chiama;
		this.ordinamento = ordina;
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
	public String getCodRicOfferta() {
		return codRicOfferta;
	}
	public void setCodRicOfferta(String codRicOfferta) {
		this.codRicOfferta = codRicOfferta;
	}
	public String getDataRicOfferta() {
		return dataRicOfferta;
	}
	public void setDataRicOfferta(String dataRicOfferta) {
		this.dataRicOfferta = dataRicOfferta;
	}
	public String getOrdinamento() {
		return ordinamento;
	}
	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}
	public List<GaraVO> getSelezioniChiamato() {
		return selezioniChiamato;
	}
	public void setSelezioniChiamato(List<GaraVO> selezioniChiamato) {
		this.selezioniChiamato = selezioniChiamato;
	}
	public String getStatoRicOfferta() {
		return statoRicOfferta;
	}
	public void setStatoRicOfferta(String statoRicOfferta) {
		this.statoRicOfferta = statoRicOfferta;
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
