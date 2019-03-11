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

import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;

import java.sql.Timestamp;

public class DocumentoVO extends BaseVO {

	private static final long serialVersionUID = -3332297678394696113L;

	private String ticket;
	private Integer progressivo = 0;
	private String utenteCod;
	private int IDDoc;
	private String codPolo;
	private String codBibl;
	private String codDocumento;
	private String tipoDocLet = "S";
	private String statoSuggerimentoDocumento;
	private String denoStatoSuggerimento;
	private StrutturaTerna utente; // campo1 codbibl di appartenza utente +
									// campo2 prg utente, campo 3 nominativo
	private StrutturaCombo titolo;
	private String naturaBid;
	private String dataIns;
	private String dataAgg;
	private String primoAutore;
	private String editore;
	private String luogoEdizione;
	private StrutturaCombo paese;
	private StrutturaCombo lingua;
	private String annoEdizione;
	private String noteDocumento;
	private String msgPerLettore;
	private String tipoVariazione;
	private boolean flag_canc = false;
	private Timestamp dataUpd;

	public DocumentoVO() {
		super();
	};

	public DocumentoVO(String codP, String codB, String codDoc,
			String statoSuggDoc, StrutturaTerna ute, StrutturaCombo tit,
			String dataI, String dataA, String aut, String edi, String luogo,
			StrutturaCombo pae, StrutturaCombo lin, String annoEdi,
			String noteDoc, String msgXLet) throws Exception {

		this.codPolo = codP;
		this.codBibl = codB;
		this.codDocumento = codDoc;
		this.statoSuggerimentoDocumento = statoSuggDoc;
		this.utente = ute;
		this.titolo = tit;
		this.dataIns = dataI;
		this.dataAgg = dataA;
		this.primoAutore = aut;
		this.editore = edi;
		this.luogoEdizione = luogo;
		this.paese = pae;
		this.lingua = lin;
		this.annoEdizione = annoEdi;
		this.noteDocumento = noteDoc;
		this.msgPerLettore = msgXLet;

	}

	public String getChiave() {
		String chiave = getCodPolo() + "|" + getCodBibl() + "|"
				+ getTipoDocLet() + "|" + getCodDocumento();
		chiave = chiave.trim();
		return chiave;
	}

	public String getAnnoEdizione() {
		return annoEdizione;
	}

	public void setAnnoEdizione(String annoEdizione) {
		this.annoEdizione = trimAndSet(annoEdizione);
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
		this.codDocumento = trimAndSet(codDocumento);
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getDataAgg() {
		return dataAgg;
	}

	public void setDataAgg(String dataAgg) {
		this.dataAgg = dataAgg;
	}

	public String getDataIns() {
		return dataIns;
	}

	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}

	public String getEditore() {
		return editore;
	}

	public void setEditore(String editore) {
		this.editore = trimAndSet(editore);
	}

	public String getLuogoEdizione() {
		return luogoEdizione;
	}

	public void setLuogoEdizione(String luogoEdizione) {
		this.luogoEdizione = trimAndSet(luogoEdizione);
	}

	public String getMsgPerLettore() {
		return msgPerLettore;
	}

	public void setMsgPerLettore(String msgPerLettore) {
		this.msgPerLettore = trimAndSet(msgPerLettore);
	}

	public String getNoteDocumento() {
		return noteDocumento;
	}

	public void setNoteDocumento(String noteDocumento) {
		this.noteDocumento = trimAndSet(noteDocumento);
	}

	public String getPrimoAutore() {
		return primoAutore;
	}

	public void setPrimoAutore(String primoAutore) {
		this.primoAutore = trimAndSet(primoAutore);
	}

	public String getStatoSuggerimentoDocumento() {
		return statoSuggerimentoDocumento;
	}

	public void setStatoSuggerimentoDocumento(String statoSuggerimentoDocumento) {
		this.statoSuggerimentoDocumento = trimAndSet(statoSuggerimentoDocumento);
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

	public String getTipoDocLet() {
		return tipoDocLet;
	}

	public void setTipoDocLet(String tipoDocLet) {
		this.tipoDocLet = tipoDocLet;
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

	public Integer getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(Integer progressivo) {
		this.progressivo = progressivo;
	}

	public String getUtenteCod() {
		return utenteCod;
	}

	public void setUtenteCod(String utenteCod) {
		this.utenteCod = utenteCod;
	}

	public boolean isFlag_canc() {
		return flag_canc;
	}

	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}

	public int getIDDoc() {
		return IDDoc;
	}

	public void setIDDoc(int doc) {
		IDDoc = doc;
	}

	public String getNaturaBid() {
		return naturaBid;
	}

	public void setNaturaBid(String naturaBid) {
		this.naturaBid = naturaBid;
	}

	public Timestamp getDataUpd() {
		return dataUpd;
	}

	public void setDataUpd(Timestamp dataUpd) {
		this.dataUpd = dataUpd;
	}

	public String getDenoStatoSuggerimento() {
		return denoStatoSuggerimento;
	}

	public void setDenoStatoSuggerimento(String denoStatoSuggerimento) {
		this.denoStatoSuggerimento = denoStatoSuggerimento;
	}

}
