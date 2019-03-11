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

import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassBiblioSemanticaVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.catalogazionesemantica.CatalogazioneSemanticaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class VariaLegameSoggettoTitoloForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -5220766037122910368L;
	private AreaDatiPassBiblioSemanticaVO areaDatiPassBiblioSemanticaVO = new AreaDatiPassBiblioSemanticaVO();
	private RicercaComuneVO ricercaComune = new RicercaComuneVO();
	private String action;
	private boolean enable;
	private boolean sessione = false;
	private boolean creatoInIndice = false;
	private boolean abilita = true;
	private List listaSoggettari;
	private List listaStatoControllo;
	private List listaTipoSoggetto;
	private String statoControllo;
	private String tipoSoggetto;
	private String notaAlLegame;
	private String cid;
	private String testo;
	private String note;
	private Object servletContext;
	private String codice;
	private String descrizione;
	private String dataInserimento;
	private String dataModifica;
	private CatalogazioneSemanticaComuneVO catalogazioneSemanticaComune = new CatalogazioneSemanticaComuneVO();
	private DettaglioSoggettoVO dettaglio = new DettaglioSoggettoVO();

	private List<TB_CODICI> listaEdizioni;

	public DettaglioSoggettoVO getDettaglio() {
		return dettaglio;
	}

	public void setDettaglio(DettaglioSoggettoVO dettaglio) {
		this.dettaglio = dettaglio;
	}

	public boolean isAbilita() {
		return abilita;
	}

	public void setAbilita(boolean abilita) {
		this.abilita = abilita;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
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

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public List getListaSoggettari() {
		return listaSoggettari;
	}

	public void setListaSoggettari(List listaSoggettari) {
		this.listaSoggettari = listaSoggettari;
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

	public String getNotaAlLegame() {
		return notaAlLegame;
	}

	public void setNotaAlLegame(String notaAlLegame) {
		this.notaAlLegame = notaAlLegame;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Object getServletContext() {
		return servletContext;
	}

	public void setServletContext(Object servletContext) {
		this.servletContext = servletContext;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public RicercaComuneVO getRicercaComune() {
		return ricercaComune;
	}

	public void setRicercaComune(RicercaComuneVO ricercaComune) {
		this.ricercaComune = ricercaComune;
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

	public boolean isCreatoInIndice() {
		return creatoInIndice;
	}

	public void setCreatoInIndice(boolean creatoInIndice) {
		this.creatoInIndice = creatoInIndice;
	}

	public List<TB_CODICI> getListaEdizioni() {
		return listaEdizioni;
	}

	public void setListaEdizioni(List<TB_CODICI> listaEdizioni) {
		this.listaEdizioni = listaEdizioni;
	}



}
