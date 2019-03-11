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
package it.iccu.sbn.web.actionforms.gestionesemantica.classificazione;

import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClasseListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AreaDatiDettaglioOggettiVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaBaseForm;

import java.util.ArrayList;
import java.util.List;

public class EsaminaClasseForm extends SemanticaBaseForm {


	private static final long serialVersionUID = -7643974046650660048L;
	private RicercaClassiVO ricercaClasse = new RicercaClassiVO();
	private DettaglioClasseVO dettClaGen = new DettaglioClasseVO();
	private AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO = new AreaDatiDettaglioOggettiVO();
	private AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();
	private CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune = new CatalogazioneSemanticaComuneVO();
	private AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici;
	private FolderType folder;
	private boolean abilita = true;
	private boolean enableEsamina = false;
	private List titoliBiblio;
	private String notazioneTrascinaDa;
	private String testoTrascinaDa;
	private String simbolo;

	private boolean enableTit = false;
	private boolean enableImporta = false;
	private String StatoControllo;

	private String sistemaClassificazione;
	private String edizioneDewey;
	private List listaSistemiClassificazione;
	private List<ComboCodDescVO> listaEdizioni;
	private List listaStatoControllo;
	private String identificativoClasse;
	private String descrizione;
	private String ulterioreTermine;
	private String dataInserimento;
	private String sogInserimento;
	private String dataModifica;
	private String sogModifica;
	private int numTitoliPolo;

	private int numTitoliBiblio;
	private int numTitoliIndice;
	private String action;
	private List ricerca;

	private boolean enableNumPolo = false;
	private boolean enableNumBiblio = false;
	private boolean enableNumIndice = false;
	private boolean enableGestione = false;
	private boolean enableOk = false;
	private boolean enableIndice = false;
	private boolean sessione = false;

	private RicercaClasseListaVO output;

	private List<ComboCodDescVO> comboGestioneEsamina = new ArrayList<ComboCodDescVO>();
	private String idFunzioneEsamina;

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

	public RicercaClasseListaVO getOutput() {
		return output;
	}

	public void setOutput(RicercaClasseListaVO output) {
		this.output = output;
	}

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

	public AreaDatiDettaglioOggettiVO getAreaDatiDettaglioOggettiVO() {
		return areaDatiDettaglioOggettiVO;
	}

	public void setAreaDatiDettaglioOggettiVO(
			AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO) {
		this.areaDatiDettaglioOggettiVO = areaDatiDettaglioOggettiVO;
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

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getEdizioneDewey() {
		return edizioneDewey;
	}

	public void setEdizioneDewey(String edizioneDewey) {
		this.edizioneDewey = edizioneDewey;
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

	public boolean isEnableNumBiblio() {
		return enableNumBiblio;
	}

	public void setEnableNumBiblio(boolean enableNumBiblio) {
		this.enableNumBiblio = enableNumBiblio;
	}

	public boolean isEnableNumIndice() {
		return enableNumIndice;
	}

	public void setEnableNumIndice(boolean enableNumIndice) {
		this.enableNumIndice = enableNumIndice;
	}

	public boolean isEnableNumPolo() {
		return enableNumPolo;
	}

	public void setEnableNumPolo(boolean enableNumPolo) {
		this.enableNumPolo = enableNumPolo;
	}

	public String getIdentificativoClasse() {
		return identificativoClasse;
	}

	public void setIdentificativoClasse(String identificativoClasse) {
		this.identificativoClasse = identificativoClasse;
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

	public void setListaSistemiClassificazione(
			List listaSistemiClassificazione) {
		this.listaSistemiClassificazione = listaSistemiClassificazione;
	}

	public List getListaStatoControllo() {
		return listaStatoControllo;
	}

	public void setListaStatoControllo(List listaStatoControllo) {
		this.listaStatoControllo = listaStatoControllo;
	}

	public int getNumTitoliBiblio() {
		return numTitoliBiblio;
	}

	public void setNumTitoliBiblio(int numTitoliBiblio) {
		this.numTitoliBiblio = numTitoliBiblio;
	}

	public int getNumTitoliIndice() {
		return numTitoliIndice;
	}

	public void setNumTitoliIndice(int numTitoliIndice) {
		this.numTitoliIndice = numTitoliIndice;
	}

	public int getNumTitoliPolo() {
		return numTitoliPolo;
	}

	public void setNumTitoliPolo(int numTitoliPolo) {
		this.numTitoliPolo = numTitoliPolo;
	}

	public List getRicerca() {
		return ricerca;
	}

	public void setRicerca(List ricerca) {
		this.ricerca = ricerca;
	}

	public RicercaClassiVO getRicercaClasse() {
		return ricercaClasse;
	}

	public void setRicercaClasse(RicercaClassiVO ricercaClasse) {
		this.ricercaClasse = ricercaClasse;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getSistemaClassificazione() {
		return sistemaClassificazione;
	}

	public void setSistemaClassificazione(String sistemaClassificazione) {
		this.sistemaClassificazione = sistemaClassificazione;
	}

	public String getSogInserimento() {
		return sogInserimento;
	}

	public void setSogInserimento(String sogInserimento) {
		this.sogInserimento = sogInserimento;
	}

	public String getSogModifica() {
		return sogModifica;
	}

	public void setSogModifica(String sogModifica) {
		this.sogModifica = sogModifica;
	}

	public String getStatoControllo() {
		return StatoControllo;
	}

	public void setStatoControllo(String statoControllo) {
		StatoControllo = statoControllo;
	}

	public String getUlterioreTermine() {
		return ulterioreTermine;
	}

	public void setUlterioreTermine(String ulterioreTermine) {
		this.ulterioreTermine = ulterioreTermine;
	}

	public DettaglioClasseVO getDettClaGen() {
		return dettClaGen;
	}

	public void setDettClaGen(DettaglioClasseVO dettClaGen) {
		this.dettClaGen = dettClaGen;
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

	public FolderType getFolder() {
		return folder;
	}

	public void setFolder(FolderType folder2) {
		this.folder = folder2;
	}

	public boolean isEnableImporta() {
		return enableImporta;
	}

	public void setEnableImporta(boolean enableImporta) {
		this.enableImporta = enableImporta;
	}

	public boolean isEnableTit() {
		return enableTit;
	}

	public void setEnableTit(boolean enableTit) {
		this.enableTit = enableTit;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	public String getNotazioneTrascinaDa() {
		return notazioneTrascinaDa;
	}

	public void setNotazioneTrascinaDa(String notazioneTrascinaDa) {
		this.notazioneTrascinaDa = notazioneTrascinaDa;
	}

	public String getTestoTrascinaDa() {
		return testoTrascinaDa;
	}

	public void setTestoTrascinaDa(String testoTrascinaDa) {
		this.testoTrascinaDa = testoTrascinaDa;
	}

	public List getTitoliBiblio() {
		return titoliBiblio;
	}

	public void setTitoliBiblio(List titoliBiblio) {
		this.titoliBiblio = titoliBiblio;
	}

	public boolean isEnableIndice() {
		return enableIndice;
	}

	public void setEnableIndice(boolean enableIndice) {
		this.enableIndice = enableIndice;
	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getDatiBibliografici() {
		return datiBibliografici;
	}

	public void setDatiBibliografici(
			AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici) {
		this.datiBibliografici = datiBibliografici;
	}

	public boolean isEnableOk() {
		return enableOk;
	}

	public void setEnableOk(boolean enableOk) {
		this.enableOk = enableOk;
	}

}
