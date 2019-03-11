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
package it.iccu.sbn.ejb.vo.gestionestampe;

import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;

import java.sql.Date;



public class MovimentoPerStampaServCorrVO extends MovimentoVO {

	private static final long serialVersionUID = -2012067517033752714L;

	//correnti
	private String nominativo;
	private String professione;
	private String titoloStudio;
	private String titolo;
	//storico
	private String descrTipoServ;
	private String noteUtente;
	private String dataMassima;
	private String copyright;
	private String modErog;
	private String statoRichiesta;
	private String supporto;
	private String risposta;
	private String modPagamento;
	private String servIllAltern;
	private String codErogAltern;
	private String codBiblDest;
	private String noteBibliotecario;
	private String numVolumi;
	private String numPezzi;
	private String noteBibl;
	private String autore;
	private String editore;
	private String annoEdizione;
	private String luogoEdizione;
	private String numSolleciti;
	private Date dataUltSoll;
	private String numVolumeMon;
	private String num_rinnovi;
	private String descrStatoMov;
	private String prenotazione;
	private String codBibTipoServ;
	private String descrBibTipoServ;



	public MovimentoPerStampaServCorrVO(){
		super();
	}


	public String getNominativo() {
		return nominativo;
	}


	public void setNominativo(String nominativo) {
		this.nominativo = nominativo;
	}


	public String getProfessione() {
		return professione;
	}


	public void setProfessione(String professione) {
		this.professione = professione;
	}


	public String getTitoloStudio() {
		return titoloStudio;
	}


	public void setTitoloStudio(String titoloStudio) {
		this.titoloStudio = titoloStudio;
	}


	public String getTitolo() {
		return titolo;
	}


	public void setTitolo(String titolo) {
		this.titolo = trimAndSet(titolo);
	}


	public String getDescrTipoServ() {
		return descrTipoServ;
	}


	public void setDescrTipoServ(String descrTipoServ) {
		this.descrTipoServ = descrTipoServ;
	}

	public String getNoteUtente() {
		return noteUtente;
	}


	public void setNoteUtente(String noteUtente) {
		this.noteUtente = noteUtente;
	}


	public String getDataMassima() {
		return dataMassima;
	}


	public void setDataMassima(String dataMassima) {
		this.dataMassima = dataMassima;
	}

	public String getCopyright() {
		return copyright;
	}


	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}


	public String getModErog() {
		return modErog;
	}


	public void setModErog(String modErog) {
		this.modErog = modErog;
	}


	public String getStatoRichiesta() {
		return statoRichiesta;
	}


	public void setStatoRichiesta(String statoRichiesta) {
		this.statoRichiesta = statoRichiesta;
	}


	public String getSupporto() {
		return supporto;
	}


	public void setSupporto(String supporto) {
		this.supporto = supporto;
	}


	public String getRisposta() {
		return risposta;
	}


	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}


	public String getModPagamento() {
		return modPagamento;
	}


	public void setModPagamento(String modPagamento) {
		this.modPagamento = modPagamento;
	}


	public String getServIllAltern() {
		return servIllAltern;
	}


	public void setServIllAltern(String servIllAltern) {
		this.servIllAltern = servIllAltern;
	}


	public String getCodErogAltern() {
		return codErogAltern;
	}


	public void setCodErogAltern(String codErogAltern) {
		this.codErogAltern = codErogAltern;
	}


	public String getCodBiblDest() {
		return codBiblDest;
	}


	public void setCodBiblDest(String codBiblDest) {
		this.codBiblDest = codBiblDest;
	}


	public String getNoteBibliotecario() {
		return noteBibliotecario;
	}


	public void setNoteBibliotecario(String noteBibliotecario) {
		this.noteBibliotecario = noteBibliotecario;
	}


	public String getNumVolumi() {
		return numVolumi;
	}


	public void setNumVolumi(String numVolumi) {
		this.numVolumi = numVolumi;
	}


	public String getNumPezzi() {
		return numPezzi;
	}


	public void setNumPezzi(String numPezzi) {
		this.numPezzi = numPezzi;
	}


	public String getNoteBibl() {
		return noteBibl;
	}


	public void setNoteBibl(String noteBibl) {
		this.noteBibl = noteBibl;
	}


	public String getEditore() {
		return editore;
	}


	public void setEditore(String editore) {
		this.editore = trimAndSet(editore);
	}


	public String getNumSolleciti() {
		return numSolleciti;
	}


	public void setNumSolleciti(String numSolleciti) {
		this.numSolleciti = numSolleciti;
	}


	public String getAnnoEdizione() {
		return annoEdizione;
	}


	public void setAnnoEdizione(String annoEdizione) {
		this.annoEdizione = annoEdizione;
	}


	public String getLuogoEdizione() {
		return luogoEdizione;
	}


	public void setLuogoEdizione(String luogoEdizione) {
		this.luogoEdizione = luogoEdizione;
	}


	public String getNumVolumeMon() {
		return numVolumeMon;
	}


	public void setNumVolumeMon(String numVolumeMon) {
		this.numVolumeMon = numVolumeMon;
	}


	public String getNum_rinnovi() {
		return num_rinnovi;
	}


	public void setNum_rinnovi(String numRinnovi) {
		num_rinnovi = numRinnovi;
	}


	public String getDescrStatoMov() {
		return descrStatoMov;
	}


	public void setDescrStatoMov(String descrStatoMov) {
		this.descrStatoMov = descrStatoMov;
	}


	public String getAutore() {
		return autore;
	}


	public void setAutore(String autore) {
		this.autore = autore;
	}


	public String getPrenotazione() {
		return prenotazione;
	}


	public void setPrenotazione(String prenotazione) {
		this.prenotazione = prenotazione;
	}


	public Date getDataUltSoll() {
		return dataUltSoll;
	}


	public void setDataUltSoll(Date dataUltSoll) {
		this.dataUltSoll = dataUltSoll;
	}


	public String getCodBibTipoServ() {
		return codBibTipoServ;
	}


	public void setCodBibTipoServ(String codBibTipoServ) {
		this.codBibTipoServ = codBibTipoServ;
	}


	public String getDescrBibTipoServ() {
		return descrBibTipoServ;
	}


	public void setDescrBibTipoServ(String descrBibTipoServ) {
		this.descrBibTipoServ = descrBibTipoServ;
	}

}
