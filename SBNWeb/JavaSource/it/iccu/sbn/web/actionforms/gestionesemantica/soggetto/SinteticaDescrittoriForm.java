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

import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class SinteticaDescrittoriForm extends AbstractSinteticaSoggettiForm {


	private static final long serialVersionUID = 6620189288318104633L;
	private RicercaComuneVO ricercaComune = new RicercaComuneVO();
	private AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();
	private CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune = new CatalogazioneSemanticaComuneVO();
	private String bid;
	private String testoBid;
	private FolderType folder;
	private List listaSoggettari;
	private List listaSintetica;
	private String action;
	private boolean enableTit = false;
	private boolean enableSelTutti = true;
	private boolean enableDeselTutti = true;
	private boolean enableStampa = true;
	private boolean enableEsamina = true;
	private String elemBlocco;
	private String blocchiTotali;
	private String elementi;
	private String[] codSoggetto;
	private String codSoggettoRadio;
	private String codice;
	private boolean sessione = false;
	private String livContr;
	private String dataInserimento;
	private String dataVariazione;
	private SBNMarc sbnMarcRispostaSave;
	private Integer linkProgressivo;
	private RicercaSoggettoListaVO outputDescrittori;
	private RicercaSoggettoListaVO outputDescrittoriSog;
	private RicercaSoggettoListaVO outputDescrittoriPar;

	private TreeElementViewSoggetti treeElementViewSoggetti = new TreeElementViewSoggetti();
	private boolean abilita = true;
	private boolean enableIndice = false;
	private boolean enableCreaListaPolo = true;
	private boolean enableCercaIndice = false;


	public RicercaSoggettoListaVO getOutputDescrittoriSog() {
		return outputDescrittoriSog;
	}

	public void setOutputDescrittoriSog(RicercaSoggettoListaVO outputDescrittoriSog) {
		this.outputDescrittoriSog = outputDescrittoriSog;
	}

	public RicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public void setRicercaComune(RicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		return errors;
	}

	public String getBlocchiTotali() {
		return blocchiTotali;
	}

	public void setBlocchiTotali(String blocchiTotali) {
		this.blocchiTotali = blocchiTotali;
	}

	public String getElemBlocco() {
		return elemBlocco;
	}

	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}

	public String getElementi() {
		return elementi;
	}

	public void setElementi(String elementi) {
		this.elementi = elementi;
	}

	public RicercaSoggettoListaVO getOutputDescrittori() {
		return outputDescrittori;
	}

	public void setOutputDescrittori(RicercaSoggettoListaVO outputDescrittori) {
		this.outputDescrittori = outputDescrittori;
	}

	public String[] getCodSoggetto() {
		return codSoggetto;
	}

	public void setCodSoggetto(String[] codSoggetto) {
		this.codSoggetto = codSoggetto;
	}

	public boolean isAbilita() {
		return abilita;
	}

	public void setAbilita(boolean abilita) {
		this.abilita = abilita;
	}

	public boolean isEnableCercaIndice() {
		return enableCercaIndice;
	}

	public void setEnableCercaIndice(boolean enableCercaIndice) {
		this.enableCercaIndice = enableCercaIndice;
	}

	public boolean isEnableCreaListaPolo() {
		return enableCreaListaPolo;
	}

	public void setEnableCreaListaPolo(boolean enableCreaListaPolo) {
		this.enableCreaListaPolo = enableCreaListaPolo;
	}

	public boolean isEnableIndice() {
		return enableIndice;
	}

	public void setEnableIndice(boolean enableIndice) {
		this.enableIndice = enableIndice;
	}

	public List getListaSintetica() {
		return listaSintetica;
	}

	public void setListaSintetica(List listaSintetica) {
		this.listaSintetica = listaSintetica;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getCodSoggettoRadio() {
		return codSoggettoRadio;
	}

	public void setCodSoggettoRadio(String codSoggettoRadio) {
		this.codSoggettoRadio = codSoggettoRadio;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List getListaSoggettari() {
		return listaSoggettari;
	}

	public void setListaSoggettari(List listaSoggettari) {
		this.listaSoggettari = listaSoggettari;
	}

	public TreeElementViewSoggetti getTreeElementViewSoggetti() {
		return treeElementViewSoggetti;
	}

	public void setTreeElementViewSoggetti(
			TreeElementViewSoggetti treeElementViewSoggetti) {
		this.treeElementViewSoggetti = treeElementViewSoggetti;
	}

	public String getLivContr() {
		return livContr;
	}

	public void setLivContr(String livContr) {
		this.livContr = livContr;
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

	public SBNMarc getSbnMarcRispostaSave() {
		return sbnMarcRispostaSave;
	}

	public void setSbnMarcRispostaSave(SBNMarc sbnMarcRispostaSave) {
		this.sbnMarcRispostaSave = sbnMarcRispostaSave;
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

	public AreaDatiPassBiblioSemanticaVO getAreaDatiPassBiblioSemanticaVO() {
		return areaDatiPassBiblioSemanticaVO;
	}

	public void setAreaDatiPassBiblioSemanticaVO(
			AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO) {
		this.areaDatiPassBiblioSemanticaVO = areaDatiPassBiblioSemanticaVO;
	}

	public boolean isEnableDeselTutti() {
		return enableDeselTutti;
	}

	public void setEnableDeselTutti(boolean enableDeselTutti) {
		this.enableDeselTutti = enableDeselTutti;
	}

	public boolean isEnableSelTutti() {
		return enableSelTutti;
	}

	public void setEnableSelTutti(boolean enableSelTutti) {
		this.enableSelTutti = enableSelTutti;
	}

	public boolean isEnableStampa() {
		return enableStampa;
	}

	public void setEnableStampa(boolean enableStampa) {
		this.enableStampa = enableStampa;
	}

	public boolean isEnableEsamina() {
		return enableEsamina;
	}

	public void setEnableEsamina(boolean enableEsamina) {
		this.enableEsamina = enableEsamina;
	}

	public RicercaSoggettoListaVO getOutputDescrittoriPar() {
		return outputDescrittoriPar;
	}

	public void setOutputDescrittoriPar(RicercaSoggettoListaVO outputDescrittoriPar) {
		this.outputDescrittoriPar = outputDescrittoriPar;
	}

	public Integer getLinkProgressivo() {
		// TODO Auto-generated method stub
		return this.linkProgressivo;
	}

	public void setLinkProgressivo(Integer linkProgressivo) {
		this.linkProgressivo = linkProgressivo;
	}
}
