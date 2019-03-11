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

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaComuneVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaBaseForm;

import java.util.ArrayList;
import java.util.List;

public class AnaliticaThesauroForm extends SemanticaBaseForm {


	private static final long serialVersionUID = -9169445658134622586L;
	private ThRicercaComuneVO ricercaComune;
	private ParametriThesauro parametri;
	private List listaThesauri;
	private List listaLivelloAutorita;

	private String dataInserimento;
	private String dataVariazione;

	private String xidConferma;

	private boolean initialized = false;
	private boolean abilita = true;
	private boolean enableOk = false;
	private boolean enableCrea = true;
	private boolean enableEsamina = true;
	private boolean enableGestione = true;
	private boolean enableInserisci = true;
	private boolean enableConferma = false;
	private boolean enableStampa = true;
	private boolean enableElimina = true;
	private boolean enableScegli = false;
	private boolean enableModifica = true;

	private int numTitoliBiblio;
	private int numTitoliPolo;

	private String nodoSelezionato;
	private String checkSelezionato;
	private String[] listaDidSelez = null;

	private String action;
	private TreeElementViewSoggetti reticolo;

	private String livelloAutorita;
	private String codThesauro;
	private int posizioneCorrente;

	private String idFunzione;
	private List<ComboCodDescVO> comboGestione = new ArrayList<ComboCodDescVO>();
	private List<ComboCodDescVO> comboGestioneEsamina = new ArrayList<ComboCodDescVO>();
	private String idFunzioneEsamina;
	private String[] listaPulsanti;


	public List<ComboCodDescVO> getComboGestioneEsamina() {
		return comboGestioneEsamina;
	}

	public void setComboGestioneEsamina(List<ComboCodDescVO> comboGestioneEsamina) {
		this.comboGestioneEsamina = comboGestioneEsamina;
	}

	public String getIdFunzioneEsamina() {
		return idFunzioneEsamina;
	}

	public void setIdFunzioneEsamina(String idFunzioneEsamina) {
		this.idFunzioneEsamina = idFunzioneEsamina;
	}

	public ThRicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public void setRicercaComune(ThRicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
	}

	public ParametriThesauro getParametri() {
		return parametri;
	}

	public void setParametri(ParametriThesauro parametri) {
		this.parametri = parametri;
	}

	public List getListaThesauri() {
		return listaThesauri;
	}

	public void setListaThesauri(List listaThesauri) {
		this.listaThesauri = listaThesauri;
	}

	public List getListaLivelloAutorita() {
		return listaLivelloAutorita;
	}

	public void setListaLivelloAutorita(List listaLivelloAutorita) {
		this.listaLivelloAutorita = listaLivelloAutorita;
	}

	public String getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(String dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public String getDataVariazione() {
		return dataVariazione;
	}

	public void setDataVariazione(String dataVariazione) {
		this.dataVariazione = dataVariazione;
	}

	public String getXidConferma() {
		return xidConferma;
	}

	public void setXidConferma(String xidConferma) {
		this.xidConferma = xidConferma;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public boolean isAbilita() {
		return abilita;
	}

	public void setAbilita(boolean abilita) {
		this.abilita = abilita;
	}

	public boolean isEnableEsamina() {
		return enableEsamina;
	}

	public void setEnableEsamina(boolean enableEsamina) {
		this.enableEsamina = enableEsamina;
	}

	public boolean isEnableGestione() {
		return enableGestione;
	}

	public void setEnableGestione(boolean enableGestione) {
		this.enableGestione = enableGestione;
	}

	public boolean isEnableInserisci() {
		return enableInserisci;
	}

	public void setEnableInserisci(boolean enableInserisci) {
		this.enableInserisci = enableInserisci;
	}

	public boolean isEnableConferma() {
		return enableConferma;
	}

	public void setEnableConferma(boolean enableConferma) {
		this.enableConferma = enableConferma;
	}

	public boolean isEnableStampa() {
		return enableStampa;
	}

	public void setEnableStampa(boolean enableStampa) {
		this.enableStampa = enableStampa;
	}

	public boolean isEnableElimina() {
		return enableElimina;
	}

	public void setEnableElimina(boolean enableElimina) {
		this.enableElimina = enableElimina;
	}

	public boolean isEnableScegli() {
		return enableScegli;
	}

	public void setEnableScegli(boolean enableScegli) {
		this.enableScegli = enableScegli;
	}

	public boolean isEnableModifica() {
		return enableModifica;
	}

	public void setEnableModifica(boolean enableModifica) {
		this.enableModifica = enableModifica;
	}

	public int getNumTitoliBiblio() {
		return numTitoliBiblio;
	}

	public void setNumTitoliBiblio(int numTitoliBiblio) {
		this.numTitoliBiblio = numTitoliBiblio;
	}

	public int getNumTitoliPolo() {
		return numTitoliPolo;
	}

	public void setNumTitoliPolo(int numTitoliPolo) {
		this.numTitoliPolo = numTitoliPolo;
	}

	public String getNodoSelezionato() {
		return nodoSelezionato;
	}

	public void setNodoSelezionato(String nodoSelezionato) {
		this.nodoSelezionato = nodoSelezionato;
	}

	public String getCheckSelezionato() {
		return checkSelezionato;
	}

	public void setCheckSelezionato(String checkSelezionato) {
		this.checkSelezionato = checkSelezionato;
	}

	public String[] getListaDidSelez() {
		return listaDidSelez;
	}

	public void setListaDidSelez(String[] listaDidSelez) {
		this.listaDidSelez = listaDidSelez;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public TreeElementViewSoggetti getReticolo() {
		return reticolo;
	}

	public void setReticolo(TreeElementViewSoggetti reticolo) {
		this.reticolo = reticolo;
	}

	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	public void setLivelloAutorita(String livelloAutorita) {
		this.livelloAutorita = livelloAutorita;
	}

	public int getPosizioneCorrente() {
		return posizioneCorrente;
	}

	public void setPosizioneCorrente(int posizioneCorrente) {
		this.posizioneCorrente = posizioneCorrente;
	}

	public boolean isEnableMultiAnalitica() {
		// return enableMultiAnalitica;
		return (listaDidSelez != null && listaDidSelez.length > 0);
	}

	public boolean isEnableCrea() {
		return enableCrea;
	}

	public void setEnableCrea(boolean enableCrea) {
		this.enableCrea = enableCrea;
	}

	public boolean isEnableOk() {
		return enableOk;
	}

	public void setEnableOk(boolean enableOk) {
		this.enableOk = enableOk;
	}

	public String getCodThesauro() {
		return codThesauro;
	}

	public void setCodThesauro(String codThesauro) {
		this.codThesauro = codThesauro;
	}

	public String getIdFunzione() {
		return idFunzione;
	}

	public void setIdFunzione(String idFunzione) {
		this.idFunzione = idFunzione;
	}

	public List<ComboCodDescVO> getComboGestione() {
		return comboGestione;
	}

	public void setComboGestione(List<ComboCodDescVO> comboGestione) {
		this.comboGestione = comboGestione;
	}

	public void setListaPulsanti(String[] listaPulsanti) {
		this.listaPulsanti = listaPulsanti;
	}

	public String[] getListaPulsanti() {
		return listaPulsanti;
	}

}
