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

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.FolderType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.web.actionforms.gestionesemantica.SemanticaBaseForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class RicercaSoggettoForm extends SemanticaBaseForm {


	private static final long serialVersionUID = -6790968248955206456L;
	private RicercaComuneVO ricercaComune = new RicercaComuneVO();
	private AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();
	private CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune = new CatalogazioneSemanticaComuneVO();
	private AreaDatiPassaggioInterrogazioneTitoloReturnVO datiBibliografici;
	private List<ComboCodDescVO> listaSoggettari;
	private List listaStatoControllo;
	private List listaOrdinamentoSoggetto;
	private List listaOrdinamentoDescrittore;
	private List listaRicercaTipo;
	private List titoliBiblio;
	private List listaRicercaPerUnDescrittore;
	private String bid;
	private String testoBid;
	private FolderType folder;
	private boolean enableIndice = false;
	private String appoggioCodiceSoggettario;

	private boolean enableCrea = false;
	private String action;
	private String cidTrascinaDa;
	private String testoTrascinaDa;
	private List ricerca;
	private boolean sessione = false;

	private List<TB_CODICI> listaEdizioni;

	public RicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public void setRicercaComune(RicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		try {
			this.ricercaComune.validate();
		} catch (ValidationException e) {
			e.printStackTrace();
			switch (e.getError()) {
			case 0:// ValidationException.erroreSoggetto:
				errors.add("generico", new ActionMessage(
						"errors.gestioneSemantica.soggettoerr"));
				break;
			case 1:// ValidationException.erroreDescrittore:
				errors.add("generico", new ActionMessage(
						"errors.gestioneSemantica.descrittoreerr"));
				break;
			case 2:// ValidationException.erroreValidazione:
				errors.add("generico", new ActionMessage(
						"errors.gestioneSemantica.validazione"));
				break;
			case 3:// ValidationException.erroreDiagnostico:
				errors.add("generico", new ActionMessage(
						"errors.gestioneSemantica.descrittori"));
				break;
			case 4:// ValidationException.erroreNoDigitazione:
				errors.add("generico", new ActionMessage(
						"errors.gestioneSemantica.noselezione"));
				break;
			case 5:// ValidationException.erroreNoDigitazione:
				errors.add("generico", new ActionMessage(
						"errors.gestioneSemantica.noCanali"));
				break;
			case 6:// ValidationException.erroreNoDigitazione:
				errors.add("generico", new ActionMessage(
						"errors.gestioneSemantica.noSoggettario"));
				break;

			}
		}
		return errors;
	}

	public List getRicerca() {
		return ricerca;
	}

	public void setRicerca(List ricerca) {
		this.ricerca = ricerca;
	}

	public boolean isEnableCrea() {
		return enableCrea;
	}

	public void setEnableCrea(boolean enableCrea) {
		this.enableCrea = enableCrea;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public boolean isEnableIndice() {
		return enableIndice;
	}

	public void setEnableIndice(boolean enableIndice) {
		this.enableIndice = enableIndice;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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

	public List getListaOrdinamentoDescrittore() {
		return listaOrdinamentoDescrittore;
	}

	public void setListaOrdinamentoDescrittore(
			List listaOrdinamentoDescrittore) {
		this.listaOrdinamentoDescrittore = listaOrdinamentoDescrittore;
	}

	public List getListaOrdinamentoSoggetto() {
		return listaOrdinamentoSoggetto;
	}

	public void setListaOrdinamentoSoggetto(List listaOrdinamentoSoggetto) {
		this.listaOrdinamentoSoggetto = listaOrdinamentoSoggetto;
	}

	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		return;
	}

	public List getListaRicercaTipo() {
		return listaRicercaTipo;
	}

	public void setListaRicercaTipo(List listaRicercaTipo) {
		this.listaRicercaTipo = listaRicercaTipo;
	}

	public List getListaRicercaPerUnDescrittore() {
		return listaRicercaPerUnDescrittore;
	}

	public void setListaRicercaPerUnDescrittore(
			List listaRicercaPerUnDescrittore) {
		this.listaRicercaPerUnDescrittore = listaRicercaPerUnDescrittore;
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

	public String getAppoggioCodiceSoggettario() {
		return appoggioCodiceSoggettario;
	}

	public void setAppoggioCodiceSoggettario(String appoggioCodiceSoggettario) {
		this.appoggioCodiceSoggettario = appoggioCodiceSoggettario;
	}

	public List getListaStatoControllo() {
		return listaStatoControllo;
	}

	public void setListaStatoControllo(List listaStatoControllo) {
		this.listaStatoControllo = listaStatoControllo;
	}

	public List<ComboCodDescVO> getListaSoggettari() {
		return listaSoggettari;
	}

	public void setListaSoggettari(List<ComboCodDescVO> listaSoggettari) {
		this.listaSoggettari = listaSoggettari;
	}

	public List<TB_CODICI> getListaEdizioni() {
		return listaEdizioni;
	}

	public void setListaEdizioni(List<TB_CODICI> listaEdizioni) {
		this.listaEdizioni = listaEdizioni;
	}

}
