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
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaSoggettoListaVO;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class ListaSoggettiForm extends AbstractSinteticaSoggettiForm {


	private static final long serialVersionUID = -1545371444745406231L;
	private RicercaComuneVO ricercaComune = new RicercaComuneVO();
	private AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();
	private CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune = new CatalogazioneSemanticaComuneVO();
	private AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici;
	private String bid;
	private String testo;
	private String testoBid;
	private FolderType folder;
	private String progr;
	private List listaSintetica;
	private List titoliBiblio;
	private List listaSoggettari;
	private boolean sessione = false;
	private boolean treeDaLista = false;
	private boolean enableSelTutti = true;
	private boolean enableDeselTutti = true;
	private boolean enableCarica = true;
	private boolean enableEsamina = true;
	private boolean enableStampa = true;
	private String elemBlocco;
	private String blocchiTotali;
	private String elementi;
	private String[] codSoggetto;
	private String codSelezionato;
	private int offset;
	private String action;
	private String did;
	private String cidTrascinaDa;
	private String testoTrascinaDa;
	private String cidRoot;
	private String paramId;
	private TreeElementViewSoggetti rootAnalitica = new TreeElementViewSoggetti();
	private String livContr;
	private String tipoSoggetto;
	private String dataInserimento;
	private String dataVariazione;
	private SBNMarc sbnMarcRispostaSave;
	private RicercaSoggettoListaVO output;
	private RicercaSoggettoListaVO outputDescrittori;
	private RicercaSoggettoListaVO outputDescrittoriSog;
	private RicercaSoggettoListaVO outputlistaprima;

	private boolean abilita = true;
	private boolean enableIndice = false;
	private boolean enableCreaListaPolo = true;
	private boolean enableCercaIndice = true;
	private boolean enableTit = false;
	private boolean enableOk = false;
	private boolean enableOkTit = false;

	private Integer linkProgressivo;

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

	public RicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public void setRicercaComune(RicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
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

	public RicercaSoggettoListaVO getOutput() {
		return output;
	}

	public void setOutput(RicercaSoggettoListaVO output) {
		this.output = output;
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

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public boolean isEnableOk() {
		return enableOk;
	}

	public void setEnableOk(boolean enableOk) {
		this.enableOk = enableOk;
	}

	public String getCodSelezionato() {
		return codSelezionato;
	}

	public void setCodSelezionato(String codSelezionato) {
		this.codSelezionato = codSelezionato;
	}

	public String getCidTrascinaDa() {
		return cidTrascinaDa;
	}

	public void setCidTrascinaDa(String cidTrascinaDa) {
		this.cidTrascinaDa = cidTrascinaDa;
	}

	public String getTestoTrascinaDa() {
		return testoTrascinaDa;
	}

	public void setTestoTrascinaDa(String testoTrascinaDa) {
		this.testoTrascinaDa = testoTrascinaDa;
	}

	public List getListaSoggettari() {
		return listaSoggettari;
	}

	public void setListaSoggettari(List listaSoggettari) {
		this.listaSoggettari = listaSoggettari;
	}

	public String getCidRoot() {
		return cidRoot;
	}

	public void setCidRoot(String cidRoot) {
		this.cidRoot = cidRoot;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public SBNMarc getSbnMarcRispostaSave() {
		return sbnMarcRispostaSave;
	}

	public void setSbnMarcRispostaSave(SBNMarc sbnMarcRispostaSave) {
		this.sbnMarcRispostaSave = sbnMarcRispostaSave;
	}

	public TreeElementViewSoggetti getRootAnalitica() {
		return rootAnalitica;
	}

	public void setRootAnalitica(
			TreeElementViewSoggetti treeElementViewSoggetti) {
		this.rootAnalitica = treeElementViewSoggetti;
	}

	public String getLivContr() {
		return livContr;
	}

	public void setLivContr(String livContr) {
		this.livContr = livContr;
	}

	public String getTipoSoggetto() {
		return tipoSoggetto;
	}

	public void setTipoSoggetto(String tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
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

	public RicercaSoggettoListaVO getOutputDescrittori() {
		return outputDescrittori;
	}

	public void setOutputDescrittori(RicercaSoggettoListaVO outputDescrittori) {
		this.outputDescrittori = outputDescrittori;
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

	public boolean isEnableTit() {
		return enableTit;
	}

	public void setEnableTit(boolean enableTit) {
		this.enableTit = enableTit;
	}

	public boolean isEnableOkTit() {
		return enableOkTit;
	}

	public void setEnableOkTit(boolean enableOkTit) {
		this.enableOkTit = enableOkTit;
	}

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		return;
	}

	public boolean isTreeDaLista() {
		return treeDaLista;
	}

	public void setTreeDaLista(boolean treeDaLista) {
		this.treeDaLista = treeDaLista;
	}

	public boolean isEnableCarica() {
		return enableCarica;
	}

	public void setEnableCarica(boolean enableCarica) {
		this.enableCarica = enableCarica;
	}

	public boolean isEnableDeselTutti() {
		return enableDeselTutti;
	}

	public void setEnableDeselTutti(boolean enableDeselTutti) {
		this.enableDeselTutti = enableDeselTutti;
	}

	public boolean isEnableEsamina() {
		return enableEsamina;
	}

	public void setEnableEsamina(boolean enableEsamina) {
		this.enableEsamina = enableEsamina;
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

	public String getProgr() {
		return progr;
	}

	public void setProgr(String progr) {
		this.progr = progr;
	}

	public CatalogazioneSemanticaComuneVO getCatalogazioneSemanticaComune() {
		return catalogazioneSemanticaComune;
	}

	public void setCatalogazioneSemanticaComune(
			CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune) {
		this.catalogazioneSemanticaComune = catalogazioneSemanticaComune;
	}

	public AreaDatiPassBiblioSemanticaVO getAreaDatiPassBiblioSemanticaVO() {
		return areaDatiPassBiblioSemanticaVO;
	}

	public void setAreaDatiPassBiblioSemanticaVO(
			AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO) {
		this.areaDatiPassBiblioSemanticaVO = areaDatiPassBiblioSemanticaVO;
	}

	public List getTitoliBiblio() {
		return titoliBiblio;
	}

	public void setTitoliBiblio(List titoliBiblio) {
		this.titoliBiblio = titoliBiblio;
	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getDatiBibliografici() {
		return datiBibliografici;
	}

	public void setDatiBibliografici(
			AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici) {
		this.datiBibliografici = datiBibliografici;
	}

	public RicercaSoggettoListaVO getOutputDescrittoriSog() {
		return outputDescrittoriSog;
	}

	public void setOutputDescrittoriSog(RicercaSoggettoListaVO outputDescrittoriSog) {
		this.outputDescrittoriSog = outputDescrittoriSog;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public RicercaSoggettoListaVO getOutputlistaprima() {
		return outputlistaprima;
	}

	public void setOutputlistaprima(RicercaSoggettoListaVO outputlistaprima) {
		this.outputlistaprima = outputlistaprima;
	}

	public Integer getLinkProgressivo() {
		// TODO Auto-generated method stub
		return this.linkProgressivo;
	}

	public void setLinkProgressivo(Integer linkProgressivo) {
		this.linkProgressivo = linkProgressivo;
	}

}
