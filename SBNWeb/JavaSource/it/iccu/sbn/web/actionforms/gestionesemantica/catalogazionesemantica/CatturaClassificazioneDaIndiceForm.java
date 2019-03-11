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

import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClasseListaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.classificazione.AbstractSinteticaClassiForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

public class CatturaClassificazioneDaIndiceForm extends AbstractSinteticaClassiForm {


	private static final long serialVersionUID = 725626961637711508L;

	private RicercaClassiVO ricercaClasse = new RicercaClassiVO();

	private RicercaComuneVO ricercaComune = new RicercaComuneVO();

	private DettaglioClasseVO dettClaGen = new DettaglioClasseVO();

	AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();

	private CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune = new CatalogazioneSemanticaComuneVO();

	private String bid;

	private String testoBid;

	private int numTitoliPolo;

	private boolean esiste = false;

	private String T005;

	private boolean modificato = false;

	private String descrizione;

	private String ulterioreTermine;

	private String codStatoControllo;

	private FolderType folder;

	private List titoliBiblio;

	private List listaSintetica;

	private List listaStatoControllo;

	private List listaSistemiClassificazione;

	private List<ComboCodDescVO> listaEdizioni;

	private boolean sessione = false;

	// Text
	private String elemBlocco;

	// Text
	private String blocchiTotali;

	// Text
	private String elementi;

	// radio button
	private String[] codClasse;

	private String codSelezionato;

	private int offset;

	private String action;

	private String notazioneTrascinaDa;

	private String testoTrascinaDa;

	private String paramId;

	private String livContr;

	private String dataInserimento;

	private String dataVariazione;

	private SBNMarc sbnMarcRispostaSave;

	// tabella iterate
	// contiene oggetti di tipo
	// it.iccu.sbn.ejb.vo.gestionesemantica.classificazioni
	private RicercaClasseListaVO output;

	private CatSemClassificazioneVO outputLista;

	private boolean abilita = true;

	private boolean enableIndice = false;

	private boolean enableCreaListaPolo = true;

	private boolean enableCercaIndice = true;

	private boolean enableTit = false;

	private boolean enableOk = false;

	private boolean enableOkTit = false;

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

	public String getTestoTrascinaDa() {
		return testoTrascinaDa;
	}

	public void setTestoTrascinaDa(String testoTrascinaDa) {
		this.testoTrascinaDa = testoTrascinaDa;
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

	public String[] getCodClasse() {
		return codClasse;
	}

	public void setCodClasse(String[] codClasse) {
		this.codClasse = codClasse;
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

	public String getNotazioneTrascinaDa() {
		return notazioneTrascinaDa;
	}

	public void setNotazioneTrascinaDa(String notazioneTrascinaDa) {
		this.notazioneTrascinaDa = notazioneTrascinaDa;
	}

	public RicercaClasseListaVO getOutput() {
		return output;
	}

	public void setOutput(RicercaClasseListaVO output) {
		this.output = output;
	}

	public RicercaClassiVO getRicercaClasse() {
		return ricercaClasse;
	}

	public void setRicercaClasse(RicercaClassiVO ricercaClasse) {
		this.ricercaClasse = ricercaClasse;
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

	public List getTitoliBiblio() {
		return titoliBiblio;
	}

	public void setTitoliBiblio(List titoliBiblio) {
		this.titoliBiblio = titoliBiblio;
	}

	public int getNumTitoliPolo() {
		return numTitoliPolo;
	}

	public void setNumTitoliPolo(int numTitoliPolo) {
		this.numTitoliPolo = numTitoliPolo;
	}

	public boolean isEsiste() {
		return esiste;
	}

	public void setEsiste(boolean esiste) {
		this.esiste = esiste;
	}

	public String getT005() {
		return T005;
	}

	public void setT005(String t005) {
		T005 = t005;
	}

	public boolean isModificato() {
		return modificato;
	}

	public void setModificato(boolean modificato) {
		this.modificato = modificato;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getUlterioreTermine() {
		return ulterioreTermine;
	}

	public void setUlterioreTermine(String ulterioreTermine) {
		this.ulterioreTermine = ulterioreTermine;
	}

	public String getCodStatoControllo() {
		return codStatoControllo;
	}

	public void setCodStatoControllo(String codStatoControllo) {
		this.codStatoControllo = codStatoControllo;
	}

	public CatSemClassificazioneVO getOutputLista() {
		return outputLista;
	}

	public void setOutputLista(CatSemClassificazioneVO outputLista) {
		this.outputLista = outputLista;
	}

	public RicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public void setRicercaComune(RicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
	}

	public List getListaStatoControllo() {
		return listaStatoControllo;
	}

	public void setListaStatoControllo(List listaStatoControllo) {
		this.listaStatoControllo = listaStatoControllo;
	}

}
