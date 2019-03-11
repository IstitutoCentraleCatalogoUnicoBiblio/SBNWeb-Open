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
import java.util.List;


public class GaraVO extends SerializableVO {
	/**
	 *
	 */
	private static final long serialVersionUID = -3315226603108936885L;
	private String ticket;
	private String chiave;
	private Integer progressivo=0;
	private String utente;
	private String codPolo;
	private String codBibl;
	private StrutturaCombo bid;
	private String naturaBid;
	private String codRicOfferta;
	private String dataRicOfferta;
	private String statoRicOfferta;
	private String desStatoRicOfferta;
	private double prezzoIndGara;
	private String prezzoIndGaraStr;
	private int numCopieRicAcq;
	private String noteOrdine;
	private String tipoVariazione;
	private List<PartecipantiGaraVO> dettaglioPartecipantiGara;
	private boolean flag_canc=false;
	private Timestamp dataUpd;



	public GaraVO (){};
	public GaraVO (String codP, String codB, StrutturaCombo idtitolo, String codRich, String dataRich,String statoRich, double prezGara, int copieRich, String noteOrd ) throws Exception {
		//if (annoF == null) {
		//	throw new Exception("Anno fattura non valido");
		//}
		this.codPolo = codP;
		this.codBibl = codB;
		this.bid = idtitolo;
		this.codRicOfferta = codRich;
		this.dataRicOfferta = dataRich;
		this.statoRicOfferta = statoRich;
		this.prezzoIndGara = prezGara;
		this.numCopieRicAcq = copieRich;
		this.noteOrdine = noteOrd;

	}


	public String getChiave() {
		String chiave=getCodBibl()+ "|" +  getCodRicOfferta() ;
		return chiave;
	}

	public void setChiave() {
		String chiave=getCodBibl()+ "|" + getCodRicOfferta();
		this.chiave = chiave;
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






	public StrutturaCombo getBid() {
		return bid;
	}






	public void setBid(StrutturaCombo bid) {
		this.bid = bid;
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




	public String getNoteOrdine() {
		return noteOrdine;
	}






	public void setNoteOrdine(String noteOrdine) {
		this.noteOrdine = noteOrdine;
	}






	public int getNumCopieRicAcq() {
		return numCopieRicAcq;
	}






	public void setNumCopieRicAcq(int numCopieRicAcq) {
		this.numCopieRicAcq = numCopieRicAcq;
	}






	public double getPrezzoIndGara() {
		return prezzoIndGara;
	}





	public void setPrezzoIndGara(double prezzoIndGara) {
		this.prezzoIndGara = prezzoIndGara;
	}




	public String getStatoRicOfferta() {
		return statoRicOfferta;
	}






	public void setStatoRicOfferta(String statoRicOfferta) {
		this.statoRicOfferta = statoRicOfferta;
	}
	public List<PartecipantiGaraVO> getDettaglioPartecipantiGara() {
		return dettaglioPartecipantiGara;
	}
	public void setDettaglioPartecipantiGara(
			List<PartecipantiGaraVO> dettaglioPartecipantiGara) {
		this.dettaglioPartecipantiGara = dettaglioPartecipantiGara;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getTipoVariazione() {
		return tipoVariazione;
	}
	public void setTipoVariazione(String tipoVariazione) {
		this.tipoVariazione = tipoVariazione;
	}
	public Integer getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(Integer progressivo) {
		this.progressivo = progressivo;
	}
	public String getUtente() {
		return utente;
	}
	public void setUtente(String utente) {
		this.utente = utente;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}
	public String getPrezzoIndGaraStr() {
		return prezzoIndGaraStr;
	}
	public void setPrezzoIndGaraStr(String prezzoIndGaraStr) {
		this.prezzoIndGaraStr = prezzoIndGaraStr;
	}
	public String getNaturaBid() {
		return naturaBid;
	}
	public void setNaturaBid(String naturaBid) {
		this.naturaBid = naturaBid;
	}
	public String getDesStatoRicOfferta() {
		return desStatoRicOfferta;
	}
	public void setDesStatoRicOfferta(String desStatoRicOfferta) {
		this.desStatoRicOfferta = desStatoRicOfferta;
	}
	public Timestamp getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Timestamp dataUpd) {
		this.dataUpd = dataUpd;
	}



}
