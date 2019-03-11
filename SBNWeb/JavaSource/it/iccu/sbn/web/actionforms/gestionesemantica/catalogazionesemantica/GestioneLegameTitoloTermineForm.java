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
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.ClassiCollegateTermineVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DettaglioTermineThesauroVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaLegameTitoloTermineType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.RicercaThesauroListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaComuneVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaBaseForm;

import java.util.List;

public class GestioneLegameTitoloTermineForm extends SemanticaBaseForm {


	private static final long serialVersionUID = -755732056483687628L;
	private ParametriThesauro parametri;
	private ThRicercaComuneVO ricercaComune = new ThRicercaComuneVO();
	private AreaDatiPassBiblioSemanticaVO datiBibliografici;
	private CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune = new CatalogazioneSemanticaComuneVO();
	private TreeElementViewSoggetti treeElementViewSoggetti = new TreeElementViewSoggetti();

	private List titoliBiblio;
	private FolderType folder;
	private boolean abilita = true;
	private boolean polo;
	private boolean indice;
	private List listaThesauri;
	private String did;
	private String testo;
	private String note;
	private String dataInserimento;
	private String sogInserimento;
	private String dataModifica;
	private String sogModifica;
	private int numNotiziePolo;
	private int numNotizieBiblio;
	private String codice;
	private String descrizione;
	private String action;
	private List ricerca;
	private String T005;
	private boolean enableSalvaThe = true;
	private boolean enableOk = false;
	private boolean enableTit = false;
	private boolean enableConferma = false;
	private String[] listaPulsanti;

	private boolean initialized = false;

	private RicercaThesauroListaVO outputLista;

	private List listaLivelloAutorita;
	private ModalitaLegameTitoloTermineType modalita;
	private DettaglioTermineThesauroVO dettaglio = new DettaglioTermineThesauroVO();
	private String notaLegame;

	private ClassiCollegateTermineVO classi = new ClassiCollegateTermineVO();
	private Integer selected;

	public String getNotaLegame() {
		return notaLegame;
	}

	public void setNotaLegame(String notaLegame) {
		this.notaLegame = notaLegame;
	}

	public List getRicerca() {
		return ricerca;
	}

	public void setRicerca(List ricerca) {
		this.ricerca = ricerca;
	}

	public boolean isAbilita() {
		return abilita;
	}

	public void setAbilita(boolean abilita) {
		this.abilita = abilita;
	}

	public ThRicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public void setRicercaComune(ThRicercaComuneVO ricercaComune) {
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

	public Integer getSelected() {
		return selected;
	}

	public void setSelected(Integer selected) {
		this.selected = selected;
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

	public String getT005() {
		return T005;
	}

	public void setT005(String t005) {
		T005 = t005;
	}

	public FolderType getFolder() {
		return folder;
	}

	public void setFolder(FolderType folder2) {
		this.folder = folder2;
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

	public AreaDatiPassBiblioSemanticaVO getDatiBibliografici() {
		return datiBibliografici;
	}

	public void setDatiBibliografici(
			AreaDatiPassBiblioSemanticaVO datiBibliografici) {
		this.datiBibliografici = datiBibliografici;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public boolean isEnableSalvaThe() {
		return enableSalvaThe;
	}

	public void setEnableSalvaThe(boolean enableSalvaThe) {
		this.enableSalvaThe = enableSalvaThe;
	}

	public List getListaThesauri() {
		return listaThesauri;
	}

	public void setListaThesauri(List listaThesauri) {
		this.listaThesauri = listaThesauri;
	}

	public RicercaThesauroListaVO getOutputLista() {
		return outputLista;
	}

	public void setOutputLista(RicercaThesauroListaVO outputLista) {
		this.outputLista = outputLista;
	}

	public List getTitoliBiblio() {
		return titoliBiblio;
	}

	public void setTitoliBiblio(List titoliBiblio) {
		this.titoliBiblio = titoliBiblio;
	}

	public void setListaLivelloAutorita(List loadComboStato) {
		this.listaLivelloAutorita = loadComboStato;
	}

	public List getListaLivelloAutorita() {
		return listaLivelloAutorita;
	}

	public ParametriThesauro getParametri() {
		return parametri;
	}

	public void setParametri(ParametriThesauro parametri) {
		this.parametri = parametri;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public void setModalita(ModalitaLegameTitoloTermineType modalita) {
		this.modalita = modalita;
	}

	public ModalitaLegameTitoloTermineType getModalita() {
		return modalita;
	}

	public void setDettaglio(DettaglioTermineThesauroVO dettaglio) {
		this.dettaglio = dettaglio;
	}

	public DettaglioTermineThesauroVO getDettaglio() {
		return dettaglio;
	}

	public boolean isEnableConferma() {
		return enableConferma;
	}

	public void setEnableConferma(boolean enableConferma) {
		this.enableConferma = enableConferma;
	}

	public String[] getListaPulsanti() {
		return listaPulsanti;
	}

	public void setListaPulsanti(String[] listaPulsanti) {
		this.listaPulsanti = listaPulsanti;
	}

	public ClassiCollegateTermineVO getClassi() {
		return classi;
	}

	public void setClassi(ClassiCollegateTermineVO classi) {
		this.classi = classi;
	}

}
