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
package it.iccu.sbn.web.actionforms.gestionesemantica.thesauro;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.CoppiaTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiLegameTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaCercaType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaComuneVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class RicercaThesauroForm extends ActionForm {


	private static final long serialVersionUID = -1334626090822454934L;
	private String action;
	private AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();
	private CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune = new CatalogazioneSemanticaComuneVO();
	private CoppiaTerminiVO datiLegame = new DatiLegameTerminiVO();

	private boolean enableCrea = false;
	private boolean enableIndice = false;

	private FolderType folder;
	private List listaOrdinamentoDescrittore;
	private List listaOrdinamentoTermini;
	private List listaRicercaTipo;
	// Combo Thesauro
	private List listaThesauri;
	private ModalitaCercaType modalita = ModalitaCercaType.CERCA;

	private List ricerca;

	private ThRicercaComuneVO ricercaComune = new ThRicercaComuneVO();

	private boolean initialized = false;

	private String termineThesauro;
	private ParametriThesauro parametri = new ParametriThesauro();

	public String getAction() {
		return action;
	}

	public AreaDatiPassBiblioSemanticaVO getAreaDatiPassBiblioSemanticaVO() {
		return areaDatiPassBiblioSemanticaVO;
	}

	public CatalogazioneSemanticaComuneVO getCatalogazioneSemanticaComune() {
		return catalogazioneSemanticaComune;
	}

	public FolderType getFolder() {
		return folder;
	}

	public List getListaOrdinamentoDescrittore() {
		return listaOrdinamentoDescrittore;
	}

	public List getListaOrdinamentoTermini() {
		return listaOrdinamentoTermini;
	}

	public List getListaRicercaTipo() {
		return listaRicercaTipo;
	}

	public List getListaThesauri() {
		return listaThesauri;
	}

	public ModalitaCercaType getModalita() {
		return modalita;
	}

	public List getRicerca() {
		return ricerca;
	}

	public ThRicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public String getTermineThesauro() {
		return termineThesauro;
	}

	public boolean isEnableCrea() {
		return enableCrea;
	}

	public boolean isEnableIndice() {
		return enableIndice;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setAreaDatiPassBiblioSemanticaVO(
			AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO) {
		this.areaDatiPassBiblioSemanticaVO = areaDatiPassBiblioSemanticaVO;
	}

	public void setCatalogazioneSemanticaComune(
			CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune) {
		this.catalogazioneSemanticaComune = catalogazioneSemanticaComune;
	}

	public void setEnableCrea(boolean enableCrea) {
		this.enableCrea = enableCrea;
	}

	public void setEnableIndice(boolean enableIndice) {
		this.enableIndice = enableIndice;
	}

	public void setFolder(FolderType folder) {
		this.folder = folder;
	}

	public void setListaOrdinamentoDescrittore(
			List listaOrdinamentoDescrittore) {
		this.listaOrdinamentoDescrittore = listaOrdinamentoDescrittore;
	}

	public void setListaOrdinamentoTermini(List listaOrdinamentoTermini) {
		this.listaOrdinamentoTermini = listaOrdinamentoTermini;

	}

	public void setListaRicercaTipo(List listaRicercaTipo) {
		this.listaRicercaTipo = listaRicercaTipo;
	}

	public void setListaThesauri(List listaThesauri) {
		this.listaThesauri = listaThesauri;
	}

	public void setModalita(ModalitaCercaType modalita) {
		this.modalita = modalita;
	}

	public void setRicerca(List ricerca) {
		this.ricerca = ricerca;
	}

	public void setRicercaComune(ThRicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
	}

	public void setTermineThesauro(String termineThesauro) {
		this.termineThesauro = termineThesauro;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		/*
		 * try { this.ricercaComune.valida(); } catch (ValidationException e) {
		 * e.printStackTrace(); switch (e.getError()) { case 0://
		 * ValidationException.erroreSoggetto: errors.add("generico", new
		 * ActionMessage( "errors.gestioneSemantica.soggettoerr")); break; case
		 * 1:// ValidationException.erroreDescrittore: errors.add("generico",
		 * new ActionMessage( "errors.gestioneSemantica.descrittoreerr"));
		 * break; case 2:// ValidationException.erroreValidazione:
		 * errors.add("generico", new ActionMessage(
		 * "errors.gestioneSemantica.validazione")); break; case 3://
		 * ValidationException.erroreDiagnostico: errors.add("generico", new
		 * ActionMessage( "errors.gestioneSemantica.descrittori")); break; case
		 * 4:// ValidationException.erroreNoDigitazione: errors.add("generico",
		 * new ActionMessage( "errors.gestioneSemantica.noselezione")); } }
		 */
		return errors;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public void setParametri(ParametriThesauro parametri) {
		this.parametri = parametri;
	}

	public ParametriThesauro getParametri() {
		return parametri;
	}

	public CoppiaTerminiVO getDatiLegame() {
		return datiLegame;
	}

	public void setDatiLegame(CoppiaTerminiVO datiLegame) {
		this.datiLegame = datiLegame;
	}

}
