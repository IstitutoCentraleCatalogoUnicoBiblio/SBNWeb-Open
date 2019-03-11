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
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.CoppiaTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.DatiLegameTerminiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ParametriThesauro.ModalitaCercaType;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.RicercaThesauroListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.thesauro.ricerca.ThRicercaComuneVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class ListaThesauriForm extends AbstractSinteticaThesauriForm {


	private static final long serialVersionUID = -8159772465304612026L;
	private boolean abilita = true;
	private String action;
	private AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();
	private String blocchiTotali;
	private CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune = new CatalogazioneSemanticaComuneVO();
	private String cidRoot;
	private String codSelezionato;
	private String dataInserimento;
	private String dataVariazione;
	private AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici;
	private String did = "";
	private String[] didMultiSelez;
	private String elemBlocco;
	private String elementi;
	private boolean enableCarica = true;
	private boolean enableDeselTutti = true;
	private boolean enableEsamina = true;
	private boolean enableOk = false;
	private boolean enableOkTit = false;
	private boolean enableSelTutti = true;
	private boolean enableStampa = true;
	private boolean enableTit = false;
	private FolderType folder;
	private Integer linkProgressivo;
	private List listaSintetica;
	private List listaThesauri;
	private String livContr;

	private ModalitaCercaType modalita;
	private int offset;

	// tabella iterate
	// contiene oggetti di tipo it.iccu.sbn.ejb.vo.gestionesemantica.thesauro
	private RicercaThesauroListaVO output;
	private RicercaThesauroListaVO outputDescrittori;
	private ParametriThesauro parametri;
	private String paramId;
	private String progr;
	private ThRicercaComuneVO ricercaComune = new ThRicercaComuneVO();

	private boolean sessione = false;
	private String tipoThesauro;
	private boolean treeDaLista = false;
	private TreeElementViewSoggetti treeElementViewSoggetti;
	private CoppiaTerminiVO datiLegame = new DatiLegameTerminiVO();

	private List<ComboCodDescVO> comboGestioneEsamina;
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

	public String getAction() {
		return action;
	}

	public AreaDatiPassBiblioSemanticaVO getAreaDatiPassBiblioSemanticaVO() {
		return areaDatiPassBiblioSemanticaVO;
	}

	public String getBlocchiTotali() {
		return blocchiTotali;
	}

	public CatalogazioneSemanticaComuneVO getCatalogazioneSemanticaComune() {
		return catalogazioneSemanticaComune;
	}

	public String getCidRoot() {
		return cidRoot;
	}

	public String getCodSelezionato() {
		return codSelezionato;
	}

	public String getDataInserimento() {
		return dataInserimento;
	}

	public String getDataVariazione() {
		return dataVariazione;
	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getDatiBibliografici() {
		return datiBibliografici;
	}

	public String getDid() {
		return did;
	}

	public String[] getDidMultiSelez() {
		return didMultiSelez;
	}

	public String getElemBlocco() {
		return elemBlocco;
	}

	public String getElementi() {
		return elementi;
	}

	public FolderType getFolder() {
		return folder;
	}

	public Integer getLinkProgressivo() {
		return linkProgressivo;
	}

	public List getListaSintetica() {
		return listaSintetica;
	}

	public List getListaThesaurari() {
		return listaThesauri;
	}

	public List getListaThesauri() {
		return listaThesauri;
	}

	public String getLivContr() {
		return livContr;
	}

	public ModalitaCercaType getModalita() {
		return modalita;
	}

	public int getOffset() {
		return offset;
	}

	public RicercaThesauroListaVO getOutput() {
		return output;
	}

	public RicercaThesauroListaVO getOutputDescrittori() {
		return outputDescrittori;
	}

	public ParametriThesauro getParametri() {
		return parametri;
	}

	public String getParamId() {
		return paramId;
	}

	public String getProgr() {
		return progr;
	}

	public ThRicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public String getTipoThesauro() {
		return tipoThesauro;
	}

	public TreeElementViewSoggetti getTreeElementViewSoggetti() {
		return treeElementViewSoggetti;
	}

	public boolean isAbilita() {
		return abilita;
	}

	public boolean isEnableCarica() {
		return enableCarica;
	}

	public boolean isEnableDeselTutti() {
		return enableDeselTutti;
	}

	public boolean isEnableEsamina() {
		return enableEsamina;
	}

	public boolean isEnableOk() {
		return enableOk;
	}

	public boolean isEnableOkTit() {
		return enableOkTit;
	}

	public boolean isEnableSelTutti() {
		return enableSelTutti;
	}

	public boolean isEnableStampa() {
		return enableStampa;
	}

	public boolean isEnableTit() {
		return enableTit;
	}

	public boolean isSessione() {
		return sessione;
	}

	public boolean isTreeDaLista() {
		return treeDaLista;
	}

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {

	}

	public void setAbilita(boolean abilita) {
		this.abilita = abilita;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setAreaDatiPassBiblioSemanticaVO(
			AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO) {
		this.areaDatiPassBiblioSemanticaVO = areaDatiPassBiblioSemanticaVO;
	}

	public void setBlocchiTotali(String blocchiTotali) {
		this.blocchiTotali = blocchiTotali;
	}

	public void setCatalogazioneSemanticaComune(
			CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune) {
		this.catalogazioneSemanticaComune = catalogazioneSemanticaComune;
	}

	public void setCidRoot(String cidRoot) {
		this.cidRoot = cidRoot;
	}

	public void setCodSelezionato(String codSelezionato) {
		this.codSelezionato = codSelezionato;
	}

	public void setDataInserimento(String dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public void setDataVariazione(String dataVariazione) {
		this.dataVariazione = dataVariazione;
	}

	public void setDatiBibliografici(
			AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici) {
		this.datiBibliografici = datiBibliografici;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public void setDidMultiSelez(String[] didMultiSelez) {
		this.didMultiSelez = didMultiSelez;
	}

	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}

	public void setElementi(String elementi) {
		this.elementi = elementi;
	}

	public void setEnableCarica(boolean enableCarica) {
		this.enableCarica = enableCarica;
	}

	public void setEnableDeselTutti(boolean enableDeselTutti) {
		this.enableDeselTutti = enableDeselTutti;
	}

	public void setEnableEsamina(boolean enableEsamina) {
		this.enableEsamina = enableEsamina;
	}

	public void setEnableOk(boolean enableOk) {
		this.enableOk = enableOk;
	}

	public void setEnableOkTit(boolean enableOkTit) {
		this.enableOkTit = enableOkTit;
	}

	public void setEnableSelTutti(boolean enableSelTutti) {
		this.enableSelTutti = enableSelTutti;
	}

	public void setEnableStampa(boolean enableStampa) {
		this.enableStampa = enableStampa;
	}

	public void setEnableTit(boolean enableTit) {
		this.enableTit = enableTit;
	}

	public void setFolder(FolderType folder2) {
		this.folder = folder2;
	}

	public void setLinkProgressivo(Integer linkProgressivo) {
		this.linkProgressivo = linkProgressivo;
	}

	public void setListaSintetica(List listaSintetica) {
		this.listaSintetica = listaSintetica;
	}

	public void setListaThesauri(List listaThesauri) {
		this.listaThesauri = listaThesauri;
	}

	public void setLivContr(String livContr) {
		this.livContr = livContr;
	}

	public void setModalita(ModalitaCercaType modalita) {
		this.modalita = modalita;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setOutput(RicercaThesauroListaVO output) {
		this.output = output;
	}

	public void setOutputDescrittori(RicercaThesauroListaVO outputDescrittori) {
		this.outputDescrittori = outputDescrittori;
	}

	public void setParametri(ParametriThesauro parametri) {
		this.parametri = parametri;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public void setProgr(String progr) {
		this.progr = progr;
	}

	public void setRicercaComune(ThRicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public void setTipoThesauro(String tipoThesauro) {
		this.tipoThesauro = tipoThesauro;
	}

	public void setTreeDaLista(boolean treeDaLista) {
		this.treeDaLista = treeDaLista;
	}

	public void setTreeElementViewSoggetti(
			TreeElementViewSoggetti treeElementViewSoggetti) {
		this.treeElementViewSoggetti = treeElementViewSoggetti;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		return errors;
	}

	public void setDatiLegame(CoppiaTerminiVO datiLegame) {
		this.datiLegame = datiLegame;
	}

	public CoppiaTerminiVO getDatiLegame() {
		return datiLegame;
	}

}
