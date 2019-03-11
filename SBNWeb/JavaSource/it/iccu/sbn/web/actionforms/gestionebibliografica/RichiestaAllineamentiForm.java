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
//	SBNWeb - Rifacimento ClientServer
//		FORM
//		almaviva2 - Inizio Codifica Agosto 2006
package it.iccu.sbn.web.actionforms.gestionebibliografica;

import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AllineaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AllineaVO.FileXMLTipoOperazione;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.upload.FormFile;


public class RichiestaAllineamentiForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 6169017337719881260L;

	private String statoAvanzamento;

	private String tipoProspettazione;



	private String tipoMatSelez;
	private String dataAllineaDa;
	private String dataAllineaA;
	private List listaTipoMat;
	private String idFileAllineamenti;

	// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
	// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
	// del flag di avvenuto allineamento.
	private String idFileLocaleAllineamenti;
	// tipo operazione su file xml
	private FileXMLTipoOperazione fileXmlTipoOperazione = FileXMLTipoOperazione.ALLINEA;


	public String getIdFileLocaleAllineamenti() {
		return idFileLocaleAllineamenti;
	}

	public void setIdFileLocaleAllineamenti(String idFileLocaleAllineamenti) {
		this.idFileLocaleAllineamenti = idFileLocaleAllineamenti;
	}
	// Inizio Gestione oggetti per cattura/fusione massiva
	private FormFile uploadImmagine;
	private FormFile uploadImmagine2;
    private List listaBidDaFile;

    // questi campi servono per la fusione da attivare dalle tabelle tb_import
    private List listaDataLista = new ArrayList();
	public String dataListaSelez;
	// Fine Gestione oggetti per cattura/fusione massiva

	AllineaVO areaDatiAllineamentoPoloIndiceVO = new AllineaVO();


	public String getStatoAvanzamento() {
		return statoAvanzamento;
	}

	public void setStatoAvanzamento(String statoAvanzamento) {
		this.statoAvanzamento = statoAvanzamento;
	}


	// Inizio Gestione oggetti per cattura massiva
	public FormFile getUploadImmagine() {
		return uploadImmagine;
	}

	public void setUploadImmagine(FormFile uploadImmagine) {
		this.uploadImmagine = uploadImmagine;
	}
	public List getListaBidDaFile() {
		return listaBidDaFile;
	}

	public void setListaBidDaFile(List listaBidDaFile) {
		this.listaBidDaFile = listaBidDaFile;
	}

	public List addListaBidDaFile(String identificativo) {
		listaBidDaFile.add(identificativo);
		return listaBidDaFile;
	}
	// Fine Gestione oggetti per cattura massiva

	public List getListaTipoMat() {
		return listaTipoMat;
	}

	public void setListaTipoMat(List listaTipoMat) {
		this.listaTipoMat = listaTipoMat;
	}

	public String getTipoMatSelez() {
		return tipoMatSelez;
	}

	public void setTipoMatSelez(String tipoMatSelez) {
		this.tipoMatSelez = tipoMatSelez;
	}

	public String getIdFileAllineamenti() {
		return idFileAllineamenti;
	}

	public void setIdFileAllineamenti(String idFileAllineamenti) {
		this.idFileAllineamenti = idFileAllineamenti;
	}

	public FileXMLTipoOperazione getFileXmlTipoOperazione() {
		return fileXmlTipoOperazione;
	}

	public void setFileXmlTipoOperazione(FileXMLTipoOperazione fileXmlTipoOperazione) {
		this.fileXmlTipoOperazione = fileXmlTipoOperazione;
	}

	public String getDataAllineaA() {
		return dataAllineaA;
	}

	public void setDataAllineaA(String dataAllineaA) {
		this.dataAllineaA = dataAllineaA;
	}

	public String getDataAllineaDa() {
		return dataAllineaDa;
	}

	public void setDataAllineaDa(String dataAllineaDa) {
		this.dataAllineaDa = dataAllineaDa;
	}

	public FormFile getUploadImmagine2() {
		return uploadImmagine2;
	}

	public void setUploadImmagine2(FormFile uploadImmagine2) {
		this.uploadImmagine2 = uploadImmagine2;
	}
	public String getTipoProspettazione() {
		return tipoProspettazione;
	}

	public void setTipoProspettazione(String tipoProspettazione) {
		this.tipoProspettazione = tipoProspettazione;
	}

	public AllineaVO getAreaDatiAllineamentoPoloIndiceVO() {
		return areaDatiAllineamentoPoloIndiceVO;
	}

	public void setAreaDatiAllineamentoPoloIndiceVO(
			AllineaVO areaDatiAllineamentoPoloIndiceVO) {
		this.areaDatiAllineamentoPoloIndiceVO = areaDatiAllineamentoPoloIndiceVO;
	}


	public String getDataListaSelez() {
		return dataListaSelez;
	}
	public void setDataListaSelez(String dataListaSelez) {
		this.dataListaSelez = dataListaSelez;
	}
	public List getListaDataLista() {
		return listaDataLista;
	}
	public void setListaDataLista(List listaDataLista) {
		this.listaDataLista = listaDataLista;
	}
	public List addListaDataLista(ComboCodDescVO newLine) {
		listaDataLista.add(newLine);
		return listaDataLista;
	}
}
