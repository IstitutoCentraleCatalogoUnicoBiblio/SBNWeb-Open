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
package it.iccu.sbn.web.actionforms.amministrazionesistema.bibliotecario;

import it.iccu.sbn.ejb.vo.common.ComboVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class NuovoBibliotecarioForm extends ActionForm {


	private static final long serialVersionUID = -3957237299951748791L;
	private String nome = "";
	private String cognome = "";
	private String username = "";
	private String usernameBackup = "";
	private String ufficio = "";
	private String note = "";
	private String selezioneRuolo;

	private boolean nuovo;
	private boolean checkReset;
	private boolean scaduto;
	private boolean abilitatoNuovo;
	private boolean abilitatoProfilo;
	private boolean flagCentroSistema;

	private String dataVariazione;
	private String dataAccesso;
	private boolean salvato;
	private boolean conferma = false;
	private String provenienza;
	private List<ComboVO> elencoRuoli = new ArrayList<ComboVO>();
	private int id;
	private String selezioneBiblio;
	private List<ComboVO> elencoBiblio = new ArrayList<ComboVO>();

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsernameBackup() {
		return usernameBackup;
	}

	public void setUsernameBackup(String usernameBackup) {
		this.usernameBackup = usernameBackup;
	}

	public String getUfficio() {
		return ufficio;
	}

	public void setUfficio(String ufficio) {
		this.ufficio = ufficio;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getSelezioneRuolo() {
		return selezioneRuolo;
	}

	public void setSelezioneRuolo(String selezioneRuolo) {
		this.selezioneRuolo = selezioneRuolo;
	}

	public boolean isNuovo() {
		return nuovo;
	}

	public void setNuovo(boolean nuovo) {
		this.nuovo = nuovo;
	}

	public boolean isCheckReset() {
		return checkReset;
	}

	public void setCheckReset(boolean checkReset) {
		this.checkReset = checkReset;
	}

	public boolean isScaduto() {
		return scaduto;
	}

	public void setScaduto(boolean scaduto) {
		this.scaduto = scaduto;
	}

	public boolean isAbilitatoNuovo() {
		return abilitatoNuovo;
	}

	public void setAbilitatoNuovo(boolean abilitatoNuovo) {
		this.abilitatoNuovo = abilitatoNuovo;
	}

	public boolean isAbilitatoProfilo() {
		return abilitatoProfilo;
	}

	public void setAbilitatoProfilo(boolean abilitatoProfilo) {
		this.abilitatoProfilo = abilitatoProfilo;
	}

	public boolean isFlagCentroSistema() {
		return flagCentroSistema;
	}

	public void setFlagCentroSistema(boolean flagCentroSistema) {
		this.flagCentroSistema = flagCentroSistema;
	}

	public String getDataVariazione() {
		return dataVariazione;
	}

	public void setDataVariazione(String dataVariazione) {
		this.dataVariazione = dataVariazione;
	}

	public String getDataAccesso() {
		return dataAccesso;
	}

	public void setDataAccesso(String dataAccesso) {
		this.dataAccesso = dataAccesso;
	}

	public boolean isSalvato() {
		return salvato;
	}

	public void setSalvato(boolean salvato) {
		this.salvato = salvato;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public List<ComboVO> getElencoRuoli() {
		return elencoRuoli;
	}

	public void setElencoRuoli(List<ComboVO> elencoRuoli) {
		this.elencoRuoli = elencoRuoli;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSelezioneBiblio() {
		return selezioneBiblio;
	}

	public void setSelezioneBiblio(String selezioneBiblio) {
		this.selezioneBiblio = selezioneBiblio;
	}

	public List<ComboVO> getElencoBiblio() {
		return elencoBiblio;
	}

	public void setElencoBiblio(List<ComboVO> elencoBiblio) {
		this.elencoBiblio = elencoBiblio;
	}

}
