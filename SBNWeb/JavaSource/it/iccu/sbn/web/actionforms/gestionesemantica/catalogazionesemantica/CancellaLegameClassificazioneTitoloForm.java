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
package it.iccu.sbn.web.actionforms.gestionesemantica.catalogazionesemantica;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class CancellaLegameClassificazioneTitoloForm extends AbstractBibliotecaForm{

/**
	 * 
	 */
	private static final long serialVersionUID = -7296014272257431381L;

private DettaglioClasseVO dettClaGen = new DettaglioClasseVO();

	AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();

	private RicercaClassiVO ricercaClasse = new RicercaClassiVO();

	private boolean enableConferma = false;

	private String action;

	private boolean enable;

	private boolean sessione = false;

	private boolean abilita = true;

	private List listaStatoControllo;

	private List listaSistemiClassificazione;

	private List<ComboCodDescVO> listaEdizioni;

	private String statoControllo;

	private String folder;

	private String notaAlLegame;

	private String identificativoClasse;

	private String simbolo;

	private String descrizione;

	private String note;

	private Object servletContext;

	private String codice;

	private String ulterioreTermine;

	private String dataInserimento;

	private String dataModifica;

	private CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune = new CatalogazioneSemanticaComuneVO();

	public boolean isAbilita() {
		return abilita;
	}

	public void setAbilita(boolean abilita) {
		this.abilita = abilita;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}


	public AreaDatiPassBiblioSemanticaVO getAreaDatiPassBiblioSemanticaVO() {
		return areaDatiPassBiblioSemanticaVO;
	}

	public void setAreaDatiPassBiblioSemanticaVO(
			AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO) {
		this.areaDatiPassBiblioSemanticaVO = areaDatiPassBiblioSemanticaVO;
	}

	public CatalogazioneSemanticaComuneVO getCatalogazioneSemanticaComune() {
		return catalogazioneSemanticaComune;
	}

	public void setCatalogazioneSemanticaComune(
			CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune) {
		this.catalogazioneSemanticaComune = catalogazioneSemanticaComune;
	}


	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}



	public List getListaStatoControllo() {
		return listaStatoControllo;
	}

	public void setListaStatoControllo(List listaStatoControllo) {
		this.listaStatoControllo = listaStatoControllo;
	}



	public List<ComboCodDescVO> getListaEdizioni() {
		return listaEdizioni;
	}

	public void setListaEdizioni(List<ComboCodDescVO> list) {
		this.listaEdizioni = list;
	}

	public List getListaSistemiClassificazione() {
		return listaSistemiClassificazione;
	}

	public void setListaSistemiClassificazione(List listaSistemiClassificazione) {
		this.listaSistemiClassificazione = listaSistemiClassificazione;
	}

	public String getNotaAlLegame() {
		return notaAlLegame;
	}

	public void setNotaAlLegame(String notaAlLegame) {
		this.notaAlLegame = notaAlLegame;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Object getServletContext() {
		return servletContext;
	}

	public void setServletContext(Object servletContext) {
		this.servletContext = servletContext;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}


	public String getStatoControllo() {
		return statoControllo;
	}

	public void setStatoControllo(String statoControllo) {
		this.statoControllo = statoControllo;
	}



	public String getIdentificativoClasse() {
		return identificativoClasse;
	}

	public void setIdentificativoClasse(String identificativoClasse) {
		this.identificativoClasse = identificativoClasse;
	}

	public RicercaClassiVO getRicercaClasse() {
		return ricercaClasse;
	}

	public void setRicercaClasse(RicercaClassiVO ricercaClasse) {
		this.ricercaClasse = ricercaClasse;
	}

	public String getUlterioreTermine() {
		return ulterioreTermine;
	}

	public void setUlterioreTermine(String ulterioreTermine) {
		this.ulterioreTermine = ulterioreTermine;
	}

	public String getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(String dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public String getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(String dataModifica) {
		this.dataModifica = dataModifica;
	}

	public DettaglioClasseVO getDettClaGen() {
		return dettClaGen;
	}

	public void setDettClaGen(DettaglioClasseVO dettClaGen) {
		this.dettClaGen = dettClaGen;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(FolderType type) {
		this.folder = folder;
	}

	public boolean isEnableConferma() {
		return enableConferma;
	}

	public void setEnableConferma(boolean enableConferma) {
		this.enableConferma = enableConferma;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

}

