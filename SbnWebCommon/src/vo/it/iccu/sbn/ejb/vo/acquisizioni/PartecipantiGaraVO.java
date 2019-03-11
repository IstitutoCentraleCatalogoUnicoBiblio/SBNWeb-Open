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

import java.sql.Timestamp;


public class PartecipantiGaraVO extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = 453929128160338248L;
	private String codPolo;
	private String codBibl;
	private StrutturaCombo fornitore;
	private String statoPartecipante;
	private String codRicOfferta;
	private String noteAlFornitore;
	private boolean esisteNota=false;
	private String dataInvioAlFornRicOfferta;
	private String codtipoInvio;
	private String msgRispDaFornAGara;
	private boolean esisteRisp=false;
	private boolean flag_canc=false;
	private Timestamp dataUpd;




	public PartecipantiGaraVO (){};
	public PartecipantiGaraVO (String codP, String codB, StrutturaCombo forn, String statoPart, String codRich, String noteForn, String dataInvioForn, String codInvio, String msgRispForn ) throws Exception {
		//if (annoF == null) {
		//	throw new Exception("Anno fattura non valido");
		//}
		this.codPolo = codP;
		this.codBibl = codB;
		this.fornitore = forn;
		this.statoPartecipante = statoPart;
		this.codRicOfferta = codRich;
		this.noteAlFornitore = noteForn;
		this.dataInvioAlFornRicOfferta = dataInvioForn;
		this.codtipoInvio = codInvio;
		this.msgRispDaFornAGara = msgRispForn;

	}


	public String getChiave() {
		String chiave=getCodBibl()+ "|" + getFornitore().getCodice()+ "|" +  getCodRicOfferta() ;
		chiave=chiave.trim();
		return chiave;
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

	public String getCodtipoInvio() {
		return codtipoInvio;
	}

	public void setCodtipoInvio(String codtipoInvio) {
		this.codtipoInvio = codtipoInvio;
	}

	public String getDataInvioAlFornRicOfferta() {
		return dataInvioAlFornRicOfferta;
	}

	public void setDataInvioAlFornRicOfferta(String dataInvioAlFornRicOfferta) {
		this.dataInvioAlFornRicOfferta = dataInvioAlFornRicOfferta;
	}

	public StrutturaCombo getFornitore() {
		return fornitore;
	}

	public void setFornitore(StrutturaCombo fornitore) {
		this.fornitore = fornitore;
	}

	public String getMsgRispDaFornAGara() {
		return msgRispDaFornAGara;
	}


	public void setMsgRispDaFornAGara(String msgRispDaFornAGara) {
		this.msgRispDaFornAGara = msgRispDaFornAGara;
	}


	public String getNoteAlFornitore() {
		return noteAlFornitore;
	}


	public void setNoteAlFornitore(String noteAlFornitore) {
		this.noteAlFornitore = noteAlFornitore;
	}


	public String getStatoPartecipante() {
		return statoPartecipante;
	}


	public void setStatoPartecipante(String statoPartecipante) {
		this.statoPartecipante = statoPartecipante;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}
	public boolean isEsisteNota() {
		return esisteNota;
	}
	public void setEsisteNota(boolean esisteNota) {
		this.esisteNota = esisteNota;
	}
	public boolean isEsisteRisp() {
		return esisteRisp;
	}
	public void setEsisteRisp(boolean esisteRisp) {
		this.esisteRisp = esisteRisp;
	}
	public Timestamp getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Timestamp dataUpd) {
		this.dataUpd = dataUpd;
	}



}
