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
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AreaDatiDettaglioOggettiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaBaseForm;

import java.util.ArrayList;
import java.util.List;

public class GestioneSoggettoForm extends SemanticaBaseForm {


	private static final long serialVersionUID = -322041714863375332L;
	private RicercaComuneVO ricercaComune = new RicercaComuneVO();
	private DettaglioSoggettoVO dettSogGenVO  = new DettaglioSoggettoVO();
	private AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO = new AreaDatiDettaglioOggettiVO();
	private TreeElementViewSoggetti treeElementViewSoggetti = new TreeElementViewSoggetti();
	private boolean abilita = true;

	private List listaStatoControllo;
	private List titoliBiblio;
	private List listaTipoSoggetto;
	private List listaSoggettari;
	private List<TB_CODICI> listaEdizioni;

	private String cid;
	private String newCid;
	private String oldCid;
	private String cidFusione;
	private String testoCidFusione;
	private String xidConferma;
	private String testo;
	private String oldTesto;
	private String note;
	private String dataInserimento;
	private String sogInserimento;
	private String dataModifica;
	private String sogModifica;
	private String statoControllo;
	private String tipoSoggetto;
	private int numTitoliPolo;
	private int numTitoliBiblio;

	private boolean enableNumPolo = false;
	private boolean enableNumBiblio = false;
	private boolean enableTrascina = false;
	private boolean enableOk = true;
	private boolean enableFondi = false;
	private boolean enableElimina = false;
	private boolean enableConferma = false;
	private boolean enableCrea = false;
	private boolean flagNuovoEFondi = false;
	private boolean flagCancellaCondiviso = false;
	private boolean treeDaLista = false;

	private String codice;
	private String descrizione;
	private String action;
	private List ricerca;
	private boolean sessione = false;
	private AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici;

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

	public List getListaStatoControllo() {
		return listaStatoControllo;
	}

	public void setListaStatoControllo(List listaStatoControllo) {
		this.listaStatoControllo = listaStatoControllo;
	}

	public List getListaTipoSoggetto() {
		return listaTipoSoggetto;
	}

	public void setListaTipoSoggetto(List listaTipoSoggetto) {
		this.listaTipoSoggetto = listaTipoSoggetto;
	}

	public List getRicerca() {
		return ricerca;
	}

	public void setRicerca(List ricerca) {
		this.ricerca = ricerca;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public boolean isAbilita() {
		return abilita;
	}

	public void setAbilita(boolean abilita) {
		this.abilita = abilita;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public RicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public void setRicercaComune(RicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public List getListaSoggettari() {
		return listaSoggettari;
	}

	public void setListaSoggettari(List listaSoggettari) {
		this.listaSoggettari = listaSoggettari;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isEnableNumBiblio() {
		return enableNumBiblio;
	}

	public void setEnableNumBiblio(boolean enableNumBiblio) {
		this.enableNumBiblio = enableNumBiblio;
	}

	public boolean isEnableNumPolo() {
		return enableNumPolo;
	}

	public void setEnableNumPolo(boolean enableNumPolo) {
		this.enableNumPolo = enableNumPolo;
	}

	public boolean isEnableTrascina() {
		return enableTrascina;
	}

	public void setEnableTrascina(boolean enableTrascina) {
		this.enableTrascina = enableTrascina;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public DettaglioSoggettoVO getDettSogGenVO() {
		return dettSogGenVO;
	}

	public void setDettSogGenVO(DettaglioSoggettoVO dettSogGenVO) {
		this.dettSogGenVO = dettSogGenVO;
	}

	public AreaDatiDettaglioOggettiVO getAreaDatiDettaglioOggettiVO() {
		return areaDatiDettaglioOggettiVO;
	}

	public void setAreaDatiDettaglioOggettiVO(
			AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO) {
		this.areaDatiDettaglioOggettiVO = areaDatiDettaglioOggettiVO;
	}

	public TreeElementViewSoggetti getTreeElementViewSoggetti() {
		return treeElementViewSoggetti;
	}

	public void setTreeElementViewSoggetti(
			TreeElementViewSoggetti treeElementViewSoggetti) {
		this.treeElementViewSoggetti = treeElementViewSoggetti;
	}

	public String getStatoControllo() {
		return statoControllo;
	}

	public void setStatoControllo(String statoControllo) {
		this.statoControllo = statoControllo;
	}

	public String getTipoSoggetto() {
		return tipoSoggetto;
	}

	public void setTipoSoggetto(String tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
	}

	public boolean isEnableFondi() {
		return enableFondi;
	}

	public void setEnableFondi(boolean enableFondi) {
		this.enableFondi = enableFondi;
	}

	public String getCidFusione() {
		return cidFusione;
	}

	public void setCidFusione(String cidFusione) {
		this.cidFusione = cidFusione;
	}

	public boolean isTreeDaLista() {
		return treeDaLista;
	}

	public void setTreeDaLista(boolean treeDaLista) {
		this.treeDaLista = treeDaLista;
	}

	public boolean isEnableElimina() {
		return enableElimina;
	}

	public void setEnableElimina(boolean enableElimina) {
		this.enableElimina = enableElimina;
	}

	public String getXidConferma() {
		return xidConferma;
	}

	public void setXidConferma(String xidConferma) {
		this.xidConferma = xidConferma;
	}

	public boolean isEnableConferma() {
		return enableConferma;
	}

	public void setEnableConferma(boolean enableConferma) {
		this.enableConferma = enableConferma;
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

	public List getTitoliBiblio() {
		return titoliBiblio;
	}

	public void setTitoliBiblio(List titoliBiblio) {
		this.titoliBiblio = titoliBiblio;
	}

	public String getTestoCidFusione() {
		return testoCidFusione;
	}

	public void setTestoCidFusione(String testoCidFusione) {
		this.testoCidFusione = testoCidFusione;
	}

	public boolean isEnableOk() {
		return enableOk;
	}

	public void setEnableOk(boolean enableOk) {
		this.enableOk = enableOk;
	}

	public void setDatiBibliografici(AreaDatiPassaggioInterrogazioneTitoloReturnVO titoliBiblio2) {
		this.datiBibliografici = titoliBiblio2;
	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getDatiBibliografici() {
		return datiBibliografici;
	}

	public boolean isFlagNuovoEFondi() {
		return flagNuovoEFondi;
	}

	public void setFlagNuovoEFondi(boolean flagNuovoEFondi) {
		this.flagNuovoEFondi = flagNuovoEFondi;
	}

	public boolean isFlagCancellaCondiviso() {
		return flagCancellaCondiviso;
	}

	public void setFlagCancellaCondiviso(boolean flagCancellaCondiviso) {
		this.flagCancellaCondiviso = flagCancellaCondiviso;
	}

	public boolean isEnableCrea() {
		return enableCrea;
	}

	public void setEnableCrea(boolean enableCrea) {
		this.enableCrea = enableCrea;
	}

	public String getNewCid() {
		return newCid;
	}

	public void setNewCid(String newCid) {
		this.newCid = newCid;
	}

	public String getOldCid() {
		return oldCid;
	}

	public void setOldCid(String oldCid) {
		this.oldCid = oldCid;
	}

	public String getOldTesto() {
		return oldTesto;
	}

	public void setOldTesto(String oldTesto) {
		this.oldTesto = oldTesto;
	}

	public List<TB_CODICI> getListaEdizioni() {
		return listaEdizioni;
	}

	public void setListaEdizioni(List<TB_CODICI> listaEdizioni) {
		this.listaEdizioni = listaEdizioni;
	}

}
