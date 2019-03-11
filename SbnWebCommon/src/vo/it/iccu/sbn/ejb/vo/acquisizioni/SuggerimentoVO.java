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

public class SuggerimentoVO extends SerializableVO {

	private static final long serialVersionUID = -7077967849417628508L;

	private String ticket;
	private Integer progressivo=0;
	private int IDSez;
	private String utente;
	private String codPolo;
	private String codBibl;
	private String cod_bib_cs;
	private String codiceSuggerimento;
	private String statoSuggerimento;
	private String denoStatoSuggerimento;
	private String dataSuggerimento;
	private StrutturaCombo titolo;
	private String naturaBid;
	private StrutturaCombo bibliotecario;
	private String nominativoBibliotecario;
	private StrutturaCombo sezione;
	private String noteSuggerimento;
	private String noteFornitore;
	private String noteBibliotecario;
	private String tipoVariazione;
	private boolean flag_canc=false;
	private Timestamp dataUpd;



	public SuggerimentoVO (){};
	public SuggerimentoVO (String codP, String codB, String codSugg, String statoSugg,String dataSugg, StrutturaCombo titSugg,StrutturaCombo biblSugg, StrutturaCombo sezSugg, String noteSugg,String noteForn, String noteBibl ) throws Exception {
		//if (ese == null) {
		//	throw new Exception("Esercizio non valido");
		//}
		this.codPolo=codP;
		this.codBibl=codB;
		this.codiceSuggerimento=codSugg;
		this.statoSuggerimento=statoSugg;
		this.dataSuggerimento=dataSugg;
		this.titolo=titSugg;
		this.bibliotecario=biblSugg;
		this.sezione=sezSugg;
		this.noteSuggerimento=noteSugg;
		this.noteFornitore=noteForn;
		this.noteBibliotecario=noteBibl;

	}

	public String getChiave() {
		String chiave=getCodBibl()+ "|" + getCodiceSuggerimento() ;
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


	public String getCodiceSuggerimento() {
		return codiceSuggerimento;
	}

	public void setCodiceSuggerimento(String codiceSuggerimento) {
		this.codiceSuggerimento = codiceSuggerimento;
	}

	public String getDataSuggerimento() {
		return dataSuggerimento;
	}

	public void setDataSuggerimento(String dataSuggerimento) {
		this.dataSuggerimento = dataSuggerimento;
	}

	public String getNoteBibliotecario() {
		return noteBibliotecario;
	}

	public void setNoteBibliotecario(String noteBibliotecario) {
		this.noteBibliotecario = trimOrEmpty(noteBibliotecario);
	}

	public String getNoteFornitore() {
		return noteFornitore;
	}

	public void setNoteFornitore(String noteFornitore) {
		this.noteFornitore = trimOrEmpty(noteFornitore);
	}

	public String getNoteSuggerimento() {
		return noteSuggerimento;
	}

	public void setNoteSuggerimento(String noteSuggerimento) {
		this.noteSuggerimento = trimOrEmpty(noteSuggerimento);
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

	public StrutturaCombo getTitolo() {
		return titolo;
	}

	public void setTitolo(StrutturaCombo titolo) {
		this.titolo = titolo;
	}
	public String getCod_bib_cs() {
		return cod_bib_cs;
	}
	public void setCod_bib_cs(String cod_bib_cs) {
		this.cod_bib_cs = cod_bib_cs;
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
	public int getIDSez() {
		return IDSez;
	}
	public void setIDSez(int sez) {
		IDSez = sez;
	}
	public String getNaturaBid() {
		return naturaBid;
	}
	public void setNaturaBid(String naturaBid) {
		this.naturaBid = naturaBid;
	}
	public StrutturaCombo getBibliotecario() {
		return bibliotecario;
	}
	public void setBibliotecario(StrutturaCombo bibliotecario) {
		this.bibliotecario = bibliotecario;
	}
	public String getNominativoBibliotecario() {
		return nominativoBibliotecario;
	}
	public void setNominativoBibliotecario(String nominativoBibliotecario) {
		this.nominativoBibliotecario = nominativoBibliotecario;
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
