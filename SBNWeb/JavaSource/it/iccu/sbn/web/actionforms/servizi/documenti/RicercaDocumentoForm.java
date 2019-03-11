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
package it.iccu.sbn.web.actionforms.servizi.documenti;

import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaCercaType;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.sif.SIFListaDocumentiNonSbnVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.List;

public class RicercaDocumentoForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = -3318926037685080181L;
	private boolean initialized = false;
	private String biblioteca;
	private DocumentoNonSbnRicercaVO filtro = new DocumentoNonSbnRicercaVO();
	private ParametriServizi parametri = new ParametriServizi();
	private List<ComboCodDescVO> listaTipoOrdinamento;
	private List<ComboCodDescVO> listaTipoFonte;
	private List<ComboCodDescVO> listaTipoDocumento;
	private String[] listaPulsanti;
	private ModalitaCercaType modalitaCerca;
	private SIFListaDocumentiNonSbnVO attivazioneSIF;

	private List<TB_CODICI> listaNature;
	private List<TB_CODICI> listaTipoNumeroStd;
	private List<TB_CODICI> listaTipoRecord;

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public DocumentoNonSbnRicercaVO getFiltro() {
		return filtro;
	}

	public void setFiltro(DocumentoNonSbnRicercaVO filtro) {
		this.filtro = filtro;
	}

	public ParametriServizi getParametri() {
		return parametri;
	}

	public void setParametri(ParametriServizi parametri) {
		this.parametri = parametri;
	}

	public List<ComboCodDescVO> getListaTipoOrdinamento() {
		return listaTipoOrdinamento;
	}

	public void setListaTipoOrdinamento(List<ComboCodDescVO> listaTipoOrdinamento) {
		this.listaTipoOrdinamento = listaTipoOrdinamento;
	}

	public List<ComboCodDescVO> getListaTipoFonte() {
		return listaTipoFonte;
	}

	public void setListaTipoFonte(List<ComboCodDescVO> listaTipoFonte) {
		this.listaTipoFonte = listaTipoFonte;
	}

	public String getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}

	public List<ComboCodDescVO> getListaTipoDocumento() {
		return listaTipoDocumento;
	}

	public void setListaTipoDocumento(List<ComboCodDescVO> listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento;
	}

	public String[] getListaPulsanti() {
		return listaPulsanti;
	}

	public void setListaPulsanti(String[] listaPulsanti) {
		this.listaPulsanti = listaPulsanti;
	}

	public void setModalitaCerca(ModalitaCercaType modalita) {
		this.modalitaCerca = modalita;
	}

	public ModalitaCercaType getModalitaCerca() {
		return modalitaCerca;
	}

	public void setAttivazioneSIF(SIFListaDocumentiNonSbnVO sif) {
		this.attivazioneSIF = sif;
	}

	public SIFListaDocumentiNonSbnVO getAttivazioneSIF() {
		return attivazioneSIF;
	}

	public List<TB_CODICI> getListaNature() {
		return listaNature;
	}

	public void setListaNature(List<TB_CODICI> listaNature) {
		this.listaNature = listaNature;
	}

	public List<TB_CODICI> getListaTipoNumeroStd() {
		return listaTipoNumeroStd;
	}

	public void setListaTipoNumeroStd(List<TB_CODICI> listaTipoNumeroStd) {
		this.listaTipoNumeroStd = listaTipoNumeroStd;
	}

	public List<TB_CODICI> getListaTipoRecord() {
		return listaTipoRecord;
	}

	public void setListaTipoRecord(List<TB_CODICI> listaTipoRecord) {
		this.listaTipoRecord = listaTipoRecord;
	}

}
