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
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.AreaDatiDettaglioOggettiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaBaseForm;

import java.util.List;

public class ImportaSoggettoForm extends SemanticaBaseForm {


	private static final long serialVersionUID = 8625919782838334001L;

	private RicercaComuneVO ricercaComune = new RicercaComuneVO();

	private TreeElementViewSoggetti treeElementViewSoggetti = new TreeElementViewSoggetti();
	private DettaglioSoggettoVO dettSogGenVO = new DettaglioSoggettoVO();
	private AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO = new AreaDatiDettaglioOggettiVO();
	private AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();
	private CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune = new CatalogazioneSemanticaComuneVO();
	private AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici;
	private List titoliBiblio;
	private String cidTrascinaDa;
	private String testoTrascinaDa;
	private boolean abilita = true;
	private boolean modificato = false;
	private List listaStatoControllo;
	private List listaTipoSoggetto;
	private List listaSoggettari;
	private String cid;
	private String testo;
	private String note;
	private FolderType folder;
	private String T005;
	private String dataInserimento;
	private String sogInserimento;
	private String dataModifica;
	private String sogModifica;
	private String codStatoControllo;
	private String codTipoSoggetto;
	private String codice;
	private String action;
	private List ricerca;
	private boolean treeDaLista = false;
	private RicercaSoggettoListaVO output;
	private CatSemSoggettoVO outputIndice;
	private boolean sessione = false;
	private boolean enableNumPolo = false;
	private boolean enableNumBiblio = false;
	private boolean enableTit = false;
	private String descrSogg;
	private boolean esiste = false;

	private List<TB_CODICI> listaEdizioni;

	public boolean isEsiste() {
		return esiste;
	}

	public void setEsiste(boolean esiste) {
		this.esiste = esiste;
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
		if (this.testo != null) {
			if (!this.testo.equals(testo))
				this.setModificato(true);
		}
		this.testo = testo;
	}

	public boolean isModificato() {
		return modificato;
	}

	public void setModificato(boolean modificato) {
		this.modificato = modificato;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		if (this.note != null) {
			if (!this.note.equals(note))
				this.setModificato(true);
		}
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

	public AreaDatiDettaglioOggettiVO getAreaDatiDettaglioOggettiVO() {
		return areaDatiDettaglioOggettiVO;
	}

	public void setAreaDatiDettaglioOggettiVO(
			AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO) {
		this.areaDatiDettaglioOggettiVO = areaDatiDettaglioOggettiVO;
	}

	public DettaglioSoggettoVO getDettSogGenVO() {
		return dettSogGenVO;
	}

	public void setDettSogGenVO(DettaglioSoggettoVO dettSogGenVO) {
		this.dettSogGenVO = dettSogGenVO;
	}

	public TreeElementViewSoggetti getTreeElementViewSoggetti() {
		return treeElementViewSoggetti;
	}

	public void setTreeElementViewSoggetti(
			TreeElementViewSoggetti treeElementViewSoggetti) {
		this.treeElementViewSoggetti = treeElementViewSoggetti;
	}

	public RicercaSoggettoListaVO getOutput() {
		return output;
	}

	public void setOutput(RicercaSoggettoListaVO output) {
		this.output = output;
	}

	public boolean isTreeDaLista() {
		return treeDaLista;
	}

	public void setTreeDaLista(boolean treeDaLista) {
		this.treeDaLista = treeDaLista;
	}

	public void setDescrizioneSoggettario(String string) {
		this.descrSogg = string;

	}

	public String getDescrizioneSoggettario() {
		return this.descrSogg;
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

	public CatalogazioneSemanticaComuneVO getCatalogazioneSemanticaComune() {
		return catalogazioneSemanticaComune;
	}

	public void setCatalogazioneSemanticaComune(
			CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune) {
		this.catalogazioneSemanticaComune = catalogazioneSemanticaComune;
	}

	public String getDescrSogg() {
		return descrSogg;
	}

	public void setDescrSogg(String descrSogg) {
		this.descrSogg = descrSogg;
	}

	public FolderType getFolder() {
		return folder;
	}

	public void setFolder(FolderType type) {
		this.folder = type;
	}

	public boolean isEnableTit() {
		return enableTit;
	}

	public void setEnableTit(boolean enableTit) {
		this.enableTit = enableTit;
	}

	public AreaDatiPassBiblioSemanticaVO getAreaDatiPassBiblioSemanticaVO() {
		return areaDatiPassBiblioSemanticaVO;
	}

	public void setAreaDatiPassBiblioSemanticaVO(
			AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO) {
		this.areaDatiPassBiblioSemanticaVO = areaDatiPassBiblioSemanticaVO;
	}

	public String getT005() {
		return T005;
	}

	public void setT005(String t005) {
		T005 = t005;
	}

	public CatSemSoggettoVO getOutputIndice() {
		return outputIndice;
	}

	public void setOutputIndice(CatSemSoggettoVO outputIndice) {
		this.outputIndice = outputIndice;
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

}
