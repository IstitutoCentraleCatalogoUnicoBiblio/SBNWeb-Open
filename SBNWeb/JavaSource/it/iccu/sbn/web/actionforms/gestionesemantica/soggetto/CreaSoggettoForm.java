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
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaBaseForm;

import java.util.List;

public class CreaSoggettoForm extends SemanticaBaseForm {


	private static final long serialVersionUID = -7505343394005867246L;
	private RicercaComuneVO ricercaComune = new RicercaComuneVO();
	private AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();
	private CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune = new CatalogazioneSemanticaComuneVO();
	private TreeElementViewSoggetti treeElementViewSoggetti = new TreeElementViewSoggetti();
	private String cidTrascinaDa;
	private String testoTrascinaDa;
	private List titoliBiblio;
	private AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici;
	private String bid;
	private String testoBid;
	private FolderType folder;
	private boolean abilita = true;
	private boolean polo;
	private boolean indice;
	private List listaStatoControllo;
	private List listaTipoSoggetto;
	private List listaSoggettari;
	private String cid;
	private String livelloAutorita;
	private String categoriaSoggetto;
	private String testo;
	private String note;
	private String dataInserimento;
	private String sogInserimento;
	private String dataModifica;
	private String sogModifica;
	private String dataInserimentoA;
	private String sogInserimentoA;
	private String dataModificaA;
	private String sogModificaA;
	private String codStatoControllo;
	private String codTipoSoggetto;
	private int numNotiziePolo;
	private int numNotizieBiblio;
	private String codice;
	private String descrizione;
	private String codiceA;
	private String descrizioneA;
	private String action;
	private List ricerca;
	private String T005;
	private boolean enableSalvaSog = true;
	private boolean enableNumPolo = false;
	private boolean enableNumBiblio = false;
	private boolean enableOk = false;
	private boolean enableTit = false;
	private boolean sessione = false;
	private RicercaSoggettoListaVO outputLista;

	private List<TB_CODICI> listaEdizioni;
	private String edizione;

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

	public int getNumNotizieBiblio() {
		return numNotizieBiblio;
	}

	public void setNumNotizieBiblio(int numNotizieBiblio) {
		this.numNotizieBiblio = numNotizieBiblio;
	}

	public int getNumNotiziePolo() {
		return numNotiziePolo;
	}

	public void setNumNotiziePolo(int numNotiziePolo) {
		this.numNotiziePolo = numNotiziePolo;
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

	public String getCodStatoControllo() {
		return codStatoControllo;
	}

	public void setCodStatoControllo(String codStatoControllo) {
		this.codStatoControllo = codStatoControllo;
	}

	public String getCodTipoSoggetto() {
		return codTipoSoggetto;
	}

	public void setCodTipoSoggetto(String codTipoSoggetto) {
		this.codTipoSoggetto = codTipoSoggetto;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public boolean isEnableOk() {
		return enableOk;
	}

	public void setEnableOk(boolean enableOk) {
		this.enableOk = enableOk;
	}

	public boolean isEnableSalvaSog() {
		return enableSalvaSog;
	}

	public void setEnableSalvaSog(boolean enableSalvaSog) {
		this.enableSalvaSog = enableSalvaSog;
	}

	public RicercaSoggettoListaVO getOutputLista() {
		return outputLista;
	}

	public void setOutputLista(RicercaSoggettoListaVO outputLista) {
		this.outputLista = outputLista;
	}

	public boolean isIndice() {
		return indice;
	}

	public void setIndice(boolean indice) {
		this.indice = indice;
	}

	public boolean isPolo() {
		return polo;
	}

	public void setPolo(boolean polo) {
		this.polo = polo;
	}

	public TreeElementViewSoggetti getTreeElementViewSoggetti() {
		return treeElementViewSoggetti;
	}

	public void setTreeElementViewSoggetti(
			TreeElementViewSoggetti treeElementViewSoggetti) {
		this.treeElementViewSoggetti = treeElementViewSoggetti;
	}

	public String getCategoriaSoggetto() {
		return categoriaSoggetto;
	}

	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	public String getCodiceA() {
		return codiceA;
	}

	public void setCodiceA(String codiceA) {
		this.codiceA = codiceA;
	}

	public String getDataInserimentoA() {
		return dataInserimentoA;
	}

	public void setDataInserimentoA(String dataInserimentoA) {
		this.dataInserimentoA = dataInserimentoA;
	}

	public String getDataModificaA() {
		return dataModificaA;
	}

	public void setDataModificaA(String dataModificaA) {
		this.dataModificaA = dataModificaA;
	}

	public String getDescrizioneA() {
		return descrizioneA;
	}

	public void setDescrizioneA(String descrizioneA) {
		this.descrizioneA = descrizioneA;
	}

	public String getSogInserimentoA() {
		return sogInserimentoA;
	}

	public void setSogInserimentoA(String sogInserimentoA) {
		this.sogInserimentoA = sogInserimentoA;
	}

	public String getSogModificaA() {
		return sogModificaA;
	}

	public void setSogModificaA(String sogModificaA) {
		this.sogModificaA = sogModificaA;
	}

	public void setCategoriaSoggetto(String categoriaSoggetto) {
		this.categoriaSoggetto = categoriaSoggetto;
	}

	public void setLivelloAutorita(String livelloAutorita) {
		this.livelloAutorita = livelloAutorita;
	}

	public String getT005() {
		return T005;
	}

	public void setT005(String t005) {
		T005 = t005;
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

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public FolderType getFolder() {
		return folder;
	}

	public void setFolder(FolderType folder2) {
		this.folder = folder2;
	}

	public String getTestoBid() {
		return testoBid;
	}

	public void setTestoBid(String testoBid) {
		this.testoBid = testoBid;
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

	public boolean isEnableTit() {
		return enableTit;
	}

	public void setEnableTit(boolean enableTit) {
		this.enableTit = enableTit;
	}

	public String getCidTrascinaDa() {
		return cidTrascinaDa;
	}

	public void setCidTrascinaDa(String cidTrascinaDa) {
		this.cidTrascinaDa = cidTrascinaDa;
	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getDatiBibliografici() {
		return datiBibliografici;
	}

	public void setDatiBibliografici(
			AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici) {
		this.datiBibliografici = datiBibliografici;
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

	public List<TB_CODICI> getListaEdizioni() {
		return listaEdizioni;
	}

	public void setListaEdizioni(List<TB_CODICI> listaEdizioni) {
		this.listaEdizioni = listaEdizioni;
	}

	public String getEdizione() {
		return edizione;
	}

	public void setEdizione(String edizione) {
		this.edizione = edizione;
	}

}
