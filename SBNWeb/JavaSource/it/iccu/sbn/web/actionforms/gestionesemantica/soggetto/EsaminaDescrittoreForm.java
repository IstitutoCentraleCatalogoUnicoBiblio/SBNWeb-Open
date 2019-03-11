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
package it.iccu.sbn.web.actionforms.gestionesemantica.soggetto;

import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AreaDatiDettaglioOggettiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.SoggettarioVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaBaseForm;

import java.util.List;

public class EsaminaDescrittoreForm extends SemanticaBaseForm {


	private static final long serialVersionUID = -3151146744804407586L;
	private RicercaComuneVO ricercaComune = new RicercaComuneVO();
	private boolean abilita = true;
	private TreeElementViewSoggetti treeElementViewSoggetti = new TreeElementViewSoggetti();
	private DettaglioDescrittoreVO dettDesGenVO = new DettaglioDescrittoreVO();
	private AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO = new AreaDatiDettaglioOggettiVO();
	private RicercaSoggettoListaVO outputDescrittori;
	private List listaSoggettari;
	private List<SoggettarioVO> listaFormaNome;
	private List<ComboCodDescVO> listaLivelloAutorita;
	private String did;
	private String didPadre;
	private String cid;
	private String descrittore;
	private String note;
	private String dataInserimento;
	private String dataIns;
	private String sogInserimento;
	private String dataModifica;
	private String dataMod;
	private String sogModifica;
	private String progr;
	private int numSoggetti;
	private int numSoggettiIndice;
	private int numUtilizzati;
	private String codice;
	private String action;
	private String T005;
	private List ricerca;
	private boolean enableSoggetti = false;
	private boolean enableSoggettiIndice = false;
	private boolean enableUtilizzati = false;
	private boolean enableGestione = true;
	private boolean enableAnaSog = false;
	private boolean enableManuale = false;
	private boolean sessione = false;
	private boolean daControllare;
	private String livContr;
	private String tipoSoggetto;
	private String testoSoggetto;

	private List<TB_CODICI> listaEdizioni;
	private List<TB_CODICI> listaCategoriaTermine;

	public String getTestoSoggetto() {
		return testoSoggetto;
	}

	public void setTestoSoggetto(String testoSoggetto) {
		this.testoSoggetto = testoSoggetto;
	}

	public String getLivContr() {
		return livContr;
	}

	public void setLivContr(String livContr) {
		this.livContr = livContr;
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

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
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

	public String getDescrittore() {
		return descrittore;
	}

	public void setDescrittore(String descrittore) {
		this.descrittore = descrittore;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public boolean isEnableSoggetti() {
		return enableSoggetti;
	}

	public void setEnableSoggetti(boolean enableSoggetti) {
		this.enableSoggetti = enableSoggetti;
	}

	public boolean isEnableUtilizzati() {
		return enableUtilizzati;
	}

	public void setEnableUtilizzati(boolean enableUtilizzati) {
		this.enableUtilizzati = enableUtilizzati;
	}

	public List getListaSoggettari() {
		return listaSoggettari;
	}

	public void setListaSoggettari(List listaSoggettari) {
		this.listaSoggettari = listaSoggettari;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getNumSoggetti() {
		return numSoggetti;
	}

	public void setNumSoggetti(int numSoggetti) {
		this.numSoggetti = numSoggetti;
	}

	public int getNumUtilizzati() {
		return numUtilizzati;
	}

	public void setNumUtilizzati(int numUtilizzati) {
		this.numUtilizzati = numUtilizzati;
	}

	public List getRicerca() {
		return ricerca;
	}

	public void setRicerca(List ricerca) {
		this.ricerca = ricerca;
	}

	public RicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public void setRicercaComune(RicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
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

	public boolean isDaControllare() {
		return daControllare;
	}

	public void setDaControllare(boolean daControllare) {
		this.daControllare = daControllare;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getDidPadre() {
		return didPadre;
	}

	public void setDidPadre(String didPadre) {
		this.didPadre = didPadre;
	}

	public boolean isEnableSoggettiIndice() {
		return enableSoggettiIndice;
	}

	public void setEnableSoggettiIndice(boolean enableSoggettiIndice) {
		this.enableSoggettiIndice = enableSoggettiIndice;
	}

	public int getNumSoggettiIndice() {
		return numSoggettiIndice;
	}

	public void setNumSoggettiIndice(int numSoggettiIndice) {
		this.numSoggettiIndice = numSoggettiIndice;
	}

	public DettaglioDescrittoreVO getDettDesGenVO() {
		return dettDesGenVO;
	}

	public void setDettDesGenVO(DettaglioDescrittoreVO dettDesGenVO) {
		this.dettDesGenVO = dettDesGenVO;
	}

	public AreaDatiDettaglioOggettiVO getAreaDatiDettaglioOggettiVO() {
		return areaDatiDettaglioOggettiVO;
	}

	public void setAreaDatiDettaglioOggettiVO(
			AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO) {
		this.areaDatiDettaglioOggettiVO = areaDatiDettaglioOggettiVO;
	}

	public boolean isEnableGestione() {
		return enableGestione;
	}

	public void setEnableGestione(boolean enableGestione) {
		this.enableGestione = enableGestione;
	}

	public TreeElementViewSoggetti getTreeElementViewSoggetti() {
		return treeElementViewSoggetti;
	}

	public void setTreeElementViewSoggetti(
			TreeElementViewSoggetti treeElementViewSoggetti) {
		this.treeElementViewSoggetti = treeElementViewSoggetti;
	}

	public String getTipoSoggetto() {
		return tipoSoggetto;
	}

	public void setTipoSoggetto(String tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
	}

	public boolean isEnableAnaSog() {
		return enableAnaSog;
	}

	public void setEnableAnaSog(boolean enableAnaSog) {
		this.enableAnaSog = enableAnaSog;
	}

	public RicercaSoggettoListaVO getOutputDescrittori() {
		return outputDescrittori;
	}

	public void setOutputDescrittori(RicercaSoggettoListaVO outputDescrittori) {
		this.outputDescrittori = outputDescrittori;
	}

	public String getProgr() {
		return progr;
	}

	public void setProgr(String progr) {
		this.progr = progr;
	}

	public String getDataIns() {
		return dataIns;
	}

	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}

	public String getDataMod() {
		return dataMod;
	}

	public void setDataMod(String dataMod) {
		this.dataMod = dataMod;
	}

	public boolean isAutomatico() {
		return !enableManuale;
	}

	public boolean isEnableManuale() {
		return enableManuale;
	}

	public void setEnableManuale(boolean enableManuale) {
		this.enableManuale = enableManuale;
	}

	public String getT005() {
		return T005;
	}

	public void setT005(String t005) {
		T005 = t005;
	}

	public List<SoggettarioVO> getListaFormaNome() {
		return listaFormaNome;
	}

	public void setListaFormaNome(List<SoggettarioVO> listaFormaNome) {
		this.listaFormaNome = listaFormaNome;
	}

	public List<ComboCodDescVO> getListaLivelloAutorita() {
		return listaLivelloAutorita;
	}

	public void setListaLivelloAutorita(
			List<ComboCodDescVO> listaLivelloAutorita) {
		this.listaLivelloAutorita = listaLivelloAutorita;
	}

	public List<TB_CODICI> getListaEdizioni() {
		return listaEdizioni;
	}

	public void setListaEdizioni(List<TB_CODICI> listaEdizioni) {
		this.listaEdizioni = listaEdizioni;
	}

	public List<TB_CODICI> getListaCategoriaTermine() {
		return listaCategoriaTermine;
	}

	public void setListaCategoriaTermine(List<TB_CODICI> listaCategoriaTermine) {
		this.listaCategoriaTermine = listaCategoriaTermine;
	}

}
