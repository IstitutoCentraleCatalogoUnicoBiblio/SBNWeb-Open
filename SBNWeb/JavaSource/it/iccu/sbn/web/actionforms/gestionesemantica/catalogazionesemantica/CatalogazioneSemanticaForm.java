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
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatSemSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.DettaglioClasseVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.RicercaClassiVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.soggetto.AbstractSinteticaSoggettiForm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class CatalogazioneSemanticaForm extends AbstractSinteticaSoggettiForm {


	private static final long serialVersionUID = -7402878557547455494L;
	private FolderType folder = FolderType.FOLDER_SOGGETTI;
	private FolderType folderClassi = FolderType.FOLDER_CLASSI;

	private RicercaComuneVO ricercaComune = new RicercaComuneVO();
	private RicercaClassiVO ricercaClasse = new RicercaClassiVO();

	private DettaglioClasseVO dettClaGen = new DettaglioClasseVO();

	private AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();

	private String action;
	private boolean enable;
	private boolean sessione = false;
	private boolean abilita = true;

	private List listaSoggettari;
	private List listaSistemiClassificazione;
	private List<ComboCodDescVO> listaEdizioni;
	private List listaStatoControllo;

	private String statoControllo;
	private Object servletContext;
	private String codice;
	private String descrizione;
	private String testoCid;
	private String livAut;
	private String cid;
	private String notaAlLegame;
	private boolean enableIndice = false;
	private boolean enableRecupera = false;
	private boolean enableModifica = false;
	private boolean enableElimina = false;
	private boolean enableSoloEsamina = false;
	private boolean enableNulla = false;
	private boolean enableOk = false;
	private CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune = new CatalogazioneSemanticaComuneVO();
	private CatSemSoggettoVO output;
	private CatSemClassificazioneVO outputClassi;

	private List<ComboCodDescVO> comboGestioneLegame = new ArrayList<ComboCodDescVO>();
	private String idFunzioneLegame;


	public CatSemClassificazioneVO getOutputClassi() {
		return outputClassi;
	}

	public void setOutputClassi(CatSemClassificazioneVO outputClassi) {
		this.outputClassi = outputClassi;
	}

	public CatSemSoggettoVO getOutput() {
		return output;
	}

	public void setOutput(CatSemSoggettoVO output) {
		this.output = output;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public CatalogazioneSemanticaComuneVO getCatalogazioneSemanticaComune() {
		return catalogazioneSemanticaComune;
	}

	public void setCatalogazioneSemanticaComune(
			CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune) {
		this.catalogazioneSemanticaComune = catalogazioneSemanticaComune;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public List getListaSoggettari() {
		return listaSoggettari;
	}

	public void setListaSoggettari(List listaSoggettari) {
		this.listaSoggettari = listaSoggettari;
	}

	public Object getServletContext() {
		return servletContext;
	}

	public void setServletContext(Object servletContext) {
		this.servletContext = servletContext;
	}

	public boolean isAbilita() {
		return abilita;
	}

	public void setAbilita(boolean abilita) {
		this.abilita = abilita;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public boolean isEnableIndice() {
		return enableIndice;
	}

	public void setEnableIndice(boolean enableIndice) {
		this.enableIndice = enableIndice;
	}

	public boolean isEnableRecupera() {
		return enableRecupera;
	}

	public void setEnableRecupera(boolean enableRecupera) {
		this.enableRecupera = enableRecupera;
	}

	public boolean isEnableElimina() {
		return enableElimina;
	}

	public void setEnableElimina(boolean enableElimina) {
		this.enableElimina = enableElimina;
	}

	public boolean isEnableModifica() {
		return enableModifica;
	}

	public void setEnableModifica(boolean enableModifica) {
		this.enableModifica = enableModifica;
	}

	public AreaDatiPassBiblioSemanticaVO getAreaDatiPassBiblioSemanticaVO() {
		return areaDatiPassBiblioSemanticaVO;
	}

	public void setAreaDatiPassBiblioSemanticaVO(
			AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO) {
		this.areaDatiPassBiblioSemanticaVO = areaDatiPassBiblioSemanticaVO;
	}

	public boolean isEnableSoloEsamina() {
		return enableSoloEsamina;
	}

	public void setEnableSoloEsamina(boolean enableSoloEsamina) {
		this.enableSoloEsamina = enableSoloEsamina;
	}

	public RicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public void setRicercaComune(RicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
	}


	public FolderType getFolder() {
		return folder;
	}


	public void setFolder(FolderType folder) {
		this.folder = folder;
		this.catalogazioneSemanticaComune.setFolder(folder);
	}

	public FolderType getFolderClassi() {
		return folderClassi;
	}

	public void setFolderClassi(FolderType folderClassi) {
		this.folderClassi = folderClassi;
		this.catalogazioneSemanticaComune.setFolder(folderClassi);
	}

	public String getTestoCid() {
		return testoCid;
	}

	public void setTestoCid(String testoCid) {
		this.testoCid = testoCid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getNotaAlLegame() {
		return notaAlLegame;
	}

	public void setNotaAlLegame(String notaAlLegame) {
		this.notaAlLegame = notaAlLegame;
	}

	public String getLivAut() {
		return livAut;
	}

	public void setLivAut(String livAut) {
		this.livAut = livAut;
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

	public void setListaSistemiClassificazione(List listaSistemiClassificazione) {
		this.listaSistemiClassificazione = listaSistemiClassificazione;
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

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		return;
	}

	public List getListaStatoControllo() {
		return listaStatoControllo;
	}

	public void setListaStatoControllo(List listaStatoControllo) {
		this.listaStatoControllo = listaStatoControllo;
	}

	public String getStatoControllo() {
		return statoControllo;
	}

	public void setStatoControllo(String statoControllo) {
		this.statoControllo = statoControllo;
	}

	public boolean isEnableOk() {
		return enableOk;
	}

	public void setEnableOk(boolean enableOk) {
		this.enableOk = enableOk;
	}

	public boolean isEnableNulla() {
		return enableNulla;
	}

	public void setEnableNulla(boolean enableNulla) {
		this.enableNulla = enableNulla;
	}

	public List<ComboCodDescVO> getComboGestioneLegame() {
		return comboGestioneLegame;
	}

	public void setComboGestioneLegame(List<ComboCodDescVO> comboGestioneLegame) {
		this.comboGestioneLegame = comboGestioneLegame;
	}

	public String getIdFunzioneLegame() {
		return idFunzioneLegame;
	}

	public void setIdFunzioneLegame(String idFunzioneLegame) {
		this.idFunzioneLegame = idFunzioneLegame;
	}




}
