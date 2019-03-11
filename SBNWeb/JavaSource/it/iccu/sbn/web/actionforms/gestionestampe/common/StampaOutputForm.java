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
package it.iccu.sbn.web.actionforms.gestionestampe.common;

import it.iccu.sbn.ejb.vo.gestionestampe.ComboCodDescVO;

import java.util.ArrayList;
import java.util.List;

public class StampaOutputForm extends StampaForm {

	private static final long serialVersionUID = -7979009598174139730L;
	private String tipoModello;
//	private List TipiModelli;
	private List<ComboCodDescVO> elencoModelli = new ArrayList<ComboCodDescVO>();
	private List elencoUtenti = new ArrayList();
	private String tipoFormato;
	private String modello;
	private String cognome;
	private String nome;
	private String dataNascita;
	private String codiceFiscale;
	private String email;
	private String autorizzazione;
	private String professione;
	private String nazCitta;
	private String titStudio;
	private String provResid;


	public String getAutorizzazione() {
		return autorizzazione;
	}
	public void setAutorizzazione(String autorizzazione) {
		this.autorizzazione = autorizzazione;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}


	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNazCitta() {
		return nazCitta;
	}
	public void setNazCitta(String nazCitta) {
		this.nazCitta = nazCitta;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getProfessione() {
		return professione;
	}
	public void setProfessione(String professione) {
		this.professione = professione;
	}
	public String getProvResid() {
		return provResid;
	}
	public void setProvResid(String provResid) {
		this.provResid = provResid;
	}
	public String getTitStudio() {
		return titStudio;
	}
	public void setTitStudio(String titStudio) {
		this.titStudio = titStudio;
	}
	public String getTipoFormato() {
		return tipoFormato;
	}
	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}
	public List getElencoModelli() {
		return elencoModelli;
	}
	public void setElencoModelli(List tipiModelli) {
		elencoModelli = tipiModelli;
	}
	public String getTipoModello() {
		return tipoModello;
	}
	public void setTipoModello(String tipoModello) {
		this.tipoModello = tipoModello;
	}
	public List getElencoUtenti() {
		return elencoUtenti;
	}
	public void setElencoUtenti(List elencoUtenti) {
		this.elencoUtenti = elencoUtenti;
	}
	public String getModello() {
		return modello;
	}
	public void setModello(String modello) {
		this.modello = modello;
	}
}
