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
package it.iccu.sbn.web.integration.actionforms.servizi.sale;

import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.calendario.RicercaGrigliaCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.RicercaPrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.RicercaSalaVO;
import it.iccu.sbn.vo.custom.servizi.sale.Mediazione;
import it.iccu.sbn.web.integration.actionforms.servizi.ServiziBaseForm;

import java.util.ArrayList;
import java.util.List;

public class RicercaSaleForm extends ServiziBaseForm {


	private static final long serialVersionUID = 5425706712165740273L;
	private RicercaSalaVO ricerca = new RicercaSalaVO();
	private RicercaPrenotazionePostoVO ricercaPrenotazioni = new RicercaPrenotazionePostoVO();
	private RicercaGrigliaCalendarioVO grigliaCalendario = new RicercaGrigliaCalendarioVO();

	private List<TB_CODICI> listaTipoOrdinamento;

	private String[] folders;
	private Integer currentFolder = 0;

	private List<Mediazione> categorieMediazione = new ArrayList<Mediazione>();
	private Integer selectedCat;

	private List<PrenotazionePostoVO> prenotazioni = new ArrayList<PrenotazionePostoVO>();
	private Integer selectedPren;

	private Integer selectedMov;

	private String cd_cat_mediazione;
	private List<Mediazione> listaCatMediazione = new ArrayList<Mediazione>();

	public RicercaSalaVO getRicerca() {
		return ricerca;
	}

	public void setRicerca(RicercaSalaVO ricerca) {
		this.ricerca = ricerca;
	}

	public RicercaPrenotazionePostoVO getRicercaPrenotazioni() {
		return ricercaPrenotazioni;
	}

	public void setRicercaPrenotazioni(RicercaPrenotazionePostoVO ricercaPrenotazioni) {
		this.ricercaPrenotazioni = ricercaPrenotazioni;
	}

	public List<TB_CODICI> getListaTipoOrdinamento() {
		return listaTipoOrdinamento;
	}

	public void setListaTipoOrdinamento(List<TB_CODICI> listaTipoOrdinamento) {
		this.listaTipoOrdinamento = listaTipoOrdinamento;
	}

	public String[] getFolders() {
		return folders;
	}

	public void setFolders(String[] folders) {
		this.folders = folders;
	}

	public Integer getCurrentFolder() {
		return currentFolder;
	}

	public void setCurrentFolder(Integer currentFolder) {
		this.currentFolder = currentFolder;
	}

	public List<Mediazione> getCategorieMediazione() {
		return categorieMediazione;
	}

	public void setCategorieMediazione(List<Mediazione> categorieMediazione) {
		this.categorieMediazione = categorieMediazione;
	}

	public Integer getSelectedCat() {
		return selectedCat;
	}

	public void setSelectedCat(Integer selected) {
		this.selectedCat = selected;
	}

	public List<PrenotazionePostoVO> getPrenotazioni() {
		return prenotazioni;
	}

	public void setPrenotazioni(List<PrenotazionePostoVO> prenotazioni) {
		this.prenotazioni = prenotazioni;
	}

	public Integer getSelectedPren() {
		return selectedPren;
	}

	public void setSelectedPren(Integer selectedPren) {
		this.selectedPren = selectedPren;
	}

	public Integer getSelectedMov() {
		return selectedMov;
	}

	public void setSelectedMov(Integer selectecReq) {
		this.selectedMov = selectecReq;
	}

	public RicercaGrigliaCalendarioVO getGrigliaCalendario() {
		return grigliaCalendario;
	}

	public void setGrigliaCalendario(RicercaGrigliaCalendarioVO grigliaCalendario) {
		this.grigliaCalendario = grigliaCalendario;
	}

	public String getCd_cat_mediazione() {
		return cd_cat_mediazione;
	}

	public void setCd_cat_mediazione(String cd_cat_mediazione) {
		this.cd_cat_mediazione = cd_cat_mediazione;
	}

	public List<Mediazione> getListaCatMediazione() {
		return listaCatMediazione;
	}

	public void setListaCatMediazione(List<Mediazione> listaCatMediazione) {
		this.listaCatMediazione = listaCatMediazione;
	}

}
