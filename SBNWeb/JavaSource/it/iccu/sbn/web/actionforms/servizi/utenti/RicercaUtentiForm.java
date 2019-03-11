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
package it.iccu.sbn.web.actionforms.servizi.utenti;

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class RicercaUtentiForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -5273466550445315406L;
	private boolean sessione = false;
    private String pathForm;
	private List lstTipiOrdinamento;
	private RicercaUtenteBibliotecaVO ricerca = new RicercaUtenteBibliotecaVO();
	private List tipoAutor;
	private List titoloStudio;
	private List specTitoloStudio;
	private List occupazioni;
	private List professione;
	private List materie;
	private List provinciaResidenza;
	private List nazCitta;
	private List atenei;
	private boolean nonTrovato=false;
	private String chgProf="";
	private String chgTit="";

	private boolean conferma=false;
	private String richiesta;

	private List tipoPersonalita;
	private List listaPersonaGiurid;

	private BibliotecaVO currentBib;
	private boolean SIF;



	public void clear() {
		this.ricerca.setCognome("");
		this.ricerca.setNome("");
		this.ricerca.setCodFiscale("");
		this.ricerca.setMail("");
		this.ricerca.setDataNascita("");
		this.ricerca.setProvResidenza("");
		this.ricerca.setNazCitta("");
		this.ricerca.setTipoAutorizzazione("");
		this.ricerca.setTitStudio("");
		this.ricerca.setProfessione("");
		this.conferma=false;
		this.richiesta="";
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		return errors;
	}

	public List getNazCitta() {
		return nazCitta;
	}

	public void setNazCitta(List nazCitta) {
		this.nazCitta = nazCitta;
	}

	public List getProfessione() {
		return professione;
	}

	public void setProfessione(List professione) {
		this.professione = professione;
	}

	public List getProvinciaResidenza() {
		return provinciaResidenza;
	}

	public void setProvinciaResidenza(List provinciaResidenza) {
		this.provinciaResidenza = provinciaResidenza;
	}

	public List getTipoAutor() {
		return tipoAutor;
	}

	public void setTipoAutor(List tipoAutor) {
		this.tipoAutor = tipoAutor;
	}

	public List getTitoloStudio() {
		return titoloStudio;
	}

	public void setTitoloStudio(List titoloStudio) {
		this.titoloStudio = titoloStudio;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public RicercaUtenteBibliotecaVO getRicerca() {
		return ricerca;
	}

	public void setRicerca(RicercaUtenteBibliotecaVO ricerca) {
		this.ricerca = ricerca;
	}

	public String getPathForm() {
		return pathForm;
	}

	public void setPathForm(String pathForm) {
		this.pathForm = pathForm;
	}

	public List getLstTipiOrdinamento() {
		return lstTipiOrdinamento;
	}

	public void setLstTipiOrdinamento(List lstTipiOrdinamento) {
		this.lstTipiOrdinamento = lstTipiOrdinamento;
	}

	public boolean isNonTrovato() {
		return nonTrovato;
	}

	public void setNonTrovato(boolean nonTrovato) {
		this.nonTrovato = nonTrovato;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public String getRichiesta() {
		return richiesta;
	}

	public void setRichiesta(String richiesta) {
		this.richiesta = richiesta;
	}

	public List getAtenei() {
		return atenei;
	}

	public void setAtenei(List atenei) {
		this.atenei = atenei;
	}

	public List getTipoPersonalita() {
		return tipoPersonalita;
	}

	public void setTipoPersonalita(List tipoPersonalita) {
		this.tipoPersonalita = tipoPersonalita;
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

	public List getMaterie() {
		return materie;
	}

	public void setMaterie(List materie) {
		this.materie = materie;
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

	public List getListaPersonaGiurid() {
		return listaPersonaGiurid;
	}

	public void setListaPersonaGiurid(List listaPersonaGiurid) {
		this.listaPersonaGiurid = listaPersonaGiurid;
	}

	public BibliotecaVO getCurrentBib() {
		return currentBib;
	}

	public void setCurrentBib(BibliotecaVO biblioteca) {
		this.currentBib = biblioteca;
	}

	public boolean isSIF() {
		return SIF;
	}

	public void setSIF(boolean sIF) {
		SIF = sIF;
	}

}
