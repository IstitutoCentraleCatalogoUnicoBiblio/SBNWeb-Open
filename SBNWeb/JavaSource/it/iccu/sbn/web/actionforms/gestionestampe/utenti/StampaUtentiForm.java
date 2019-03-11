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
package it.iccu.sbn.web.actionforms.gestionestampe.utenti;

import it.iccu.sbn.ejb.vo.gestionestampe.ComboCodDescVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class StampaUtentiForm extends ActionForm {//extends StampaForm

	private static final long serialVersionUID = -2171186702006519049L;
	private String tipoFormato;
	private String cognome;
	private String nome;
	private String dataNascita;
	private String codiceFiscale;
	private String email;
	private String tipoModello;
	private List<ComboCodDescVO> elencoModelli = new ArrayList<ComboCodDescVO>();
	private String tipoAutorizzazione;
	private List<ComboCodDescVO> elencoAutorizzazioni = new ArrayList<ComboCodDescVO>();
	private String tipoProfessione;
	private List<ComboCodDescVO> elencoProfessioni = new ArrayList<ComboCodDescVO>();
	private String tipoNazCitta;
	private List<ComboCodDescVO> elencoNazCitta = new ArrayList<ComboCodDescVO>();
	private String tipoTitStudio;
	private List<ComboCodDescVO> elencoTitStudio = new ArrayList<ComboCodDescVO>();
	private String tipoProvResid;
	private List<ComboCodDescVO> elencoProvResid = new ArrayList<ComboCodDescVO>();
	private String tipoRicerca;
	private String codUte;
	private String codiceAteneo;
	private String matricola;
	private String dataNascitaDa;
	private String dataNascitaA;
	private String dataFineAutDa;
	private String dataFineAutA;
	private List atenei;

	private List specTitoloStudio;
	private List occupazioni;
	private String chgProf="";
	private String chgTit="";
	private List materie;

	private String occupazione;
	private String materia;
	private String specificita;
	private boolean sessione = false;

	private List tipoPersonalita;
	private String tipoPersona;


//	private String autorizzazione;
//	private String professione;
//	private String nazCitta;
//	private String titStudio;
//	private String provResid;
//	indica il radio button per la ricerca (int=intero par=parola ini=inizio)

//	public String getAutorizzazione() {
//		return autorizzazione;
//	}
//	public void setAutorizzazione(String autorizzazione) {
//		this.autorizzazione = autorizzazione;
//	}
//	public String getNazCitta() {
//	return nazCitta;
//}
//public void setNazCitta(String nazCitta) {
//	this.nazCitta = nazCitta;
//}
//	public String getProfessione() {
//	return professione;
//}
//public void setProfessione(String professione) {
//	this.professione = professione;
//}
//public String getProvResid() {
//	return provResid;
//}
//public void setProvResid(String provResid) {
//	this.provResid = provResid;
//}
//public String getTitStudio() {
//	return titStudio;
//}
//public void setTitStudio(String titStudio) {
//	this.titStudio = titStudio;
//}
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
	public String getCognomeNome() {
		String composto="";

		if (cognome != null && !"".equals(cognome.trim()))
		{
			composto=composto + cognome;
		}
		if (nome != null && !"".equals(nome.trim()))
		{
			composto=composto + nome;
		}
		return composto;
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

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipoFormato() {
		return tipoFormato;
	}
	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}
	public String getTipoAutorizzazione() {
		return tipoAutorizzazione;
	}
	public void setTipoAutorizzazione(String tipoAutorizzazione) {
		this.tipoAutorizzazione = tipoAutorizzazione;
	}
	public List<ComboCodDescVO> getElencoAutorizzazioni() {
		return elencoAutorizzazioni;
	}
	public void setElencoAutorizzazioni(List<ComboCodDescVO> elencoAutorizzazioni) {
		this.elencoAutorizzazioni = elencoAutorizzazioni;
	}
	public String getTipoNazCitta() {
		return tipoNazCitta;
	}
	public void setTipoNazCitta(String tipoNazCitta) {
		this.tipoNazCitta = tipoNazCitta;
	}
	public List getElencoNazCitta() {
		return elencoNazCitta;
	}
	public void setElencoNazCitta(List<ComboCodDescVO> nazCitta) {
		this.elencoNazCitta = nazCitta;
	}
	public String getTipoModello() {
		return tipoModello;
	}
	public void setTipoModello(String tipoModello) {
		this.tipoModello = tipoModello;
	}
	public List getElencoModelli() {
		return elencoModelli;
	}
	public void setElencoModelli(List<ComboCodDescVO> elencoModelli) {
		this.elencoModelli = elencoModelli;
	}
	public String getTipoProfessione() {
		return tipoProfessione;
	}
	public void setTipoProfessione(String tipoProfessione) {
		this.tipoProfessione = tipoProfessione;
	}
	public List<ComboCodDescVO> getElencoProfessioni() {
		return elencoProfessioni;
	}
	public void setElencoProfessioni(List<ComboCodDescVO> elencoProfessioni) {
		this.elencoProfessioni = elencoProfessioni;
	}
	public String getTipoProvResid() {
		return tipoProvResid;
	}
	public void setTipoProvResid(String tipoProvResid) {
		this.tipoProvResid = tipoProvResid;
	}
	public List<ComboCodDescVO> getElencoProvResid() {
		return elencoProvResid;
	}
	public void setElencoProvResid(List<ComboCodDescVO> elencoProvResid) {
		this.elencoProvResid = elencoProvResid;
	}
	public String getTipoTitStudio() {
		return tipoTitStudio;
	}
	public void setTipoTitStudio(String tipoTitStudio) {
		this.tipoTitStudio = tipoTitStudio;
	}
	public List<ComboCodDescVO> getElencoTitStudio() {
		return elencoTitStudio;
	}
	public void setElencoTitStudio(List<ComboCodDescVO> elencoTitStudio) {
		this.elencoTitStudio = elencoTitStudio;
	}
	public String getTipoRicerca() {
		return tipoRicerca;
	}
	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}
	public List getAtenei() {
		return atenei;
	}
	public void setAtenei(List atenei) {
		this.atenei = atenei;
	}
	public String getCodiceAteneo() {
		return codiceAteneo;
	}
	public void setCodiceAteneo(String codiceAteneo) {
		this.codiceAteneo = codiceAteneo;
	}
	public String getCodUte() {
		return codUte;
	}
	public void setCodUte(String codUte) {
		this.codUte = codUte;
	}
	public String getDataFineAutA() {
		return dataFineAutA;
	}
	public void setDataFineAutA(String dataFineAutA) {
		this.dataFineAutA = dataFineAutA;
	}
	public String getDataFineAutDa() {
		return dataFineAutDa;
	}
	public void setDataFineAutDa(String dataFineAutDa) {
		this.dataFineAutDa = dataFineAutDa;
	}
	public String getDataNascitaA() {
		return dataNascitaA;
	}
	public void setDataNascitaA(String dataNascitaA) {
		this.dataNascitaA = dataNascitaA;
	}
	public String getDataNascitaDa() {
		return dataNascitaDa;
	}
	public void setDataNascitaDa(String dataNascitaDa) {
		this.dataNascitaDa = dataNascitaDa;
	}
	public String getMatricola() {
		return matricola;
	}
	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}
	public String getChgProf() {
		return chgProf;
	}
	public void setChgProf(String chgProf) {
		this.chgProf = chgProf;
	}
	public String getChgTit() {
		return chgTit;
	}
	public void setChgTit(String chgTit) {
		this.chgTit = chgTit;
	}
	public String getMateria() {
		return materia;
	}
	public void setMateria(String materia) {
		this.materia = materia;
	}
	public List getMaterie() {
		return materie;
	}
	public void setMaterie(List materie) {
		this.materie = materie;
	}
	public String getOccupazione() {
		return occupazione;
	}
	public void setOccupazione(String occupazione) {
		this.occupazione = occupazione;
	}
	public List getOccupazioni() {
		return occupazioni;
	}
	public void setOccupazioni(List occupazioni) {
		this.occupazioni = occupazioni;
	}
	public List getSpecTitoloStudio() {
		return specTitoloStudio;
	}
	public void setSpecTitoloStudio(List specTitoloStudio) {
		this.specTitoloStudio = specTitoloStudio;
	}
	public String getSpecificita() {
		return specificita;
	}
	public void setSpecificita(String specificita) {
		this.specificita = specificita;
	}
	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String getTipoPersona() {
		return tipoPersona;
	}
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	public List getTipoPersonalita() {
		return tipoPersonalita;
	}
	public void setTipoPersonalita(List tipoPersonalita) {
		this.tipoPersonalita = tipoPersonalita;
	}


}
