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
package it.iccu.sbn.web.actionforms.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.web.integration.actionforms.AbstractBibliotecaForm;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.upload.FormFile;

public class InterrogazioneListeDiConfrontoForm extends AbstractBibliotecaForm {


	private static final long serialVersionUID = 7503210205690383178L;


	public String provenienza;


	public String ritornoDaEsterna;

	// Canali di ricerca lista da lavorare
	private List listaNomeLista;
	public String nomeListaSelez;

	private List listaDataLista;
	public String dataListaSelez;
	public String dataListaSelezPerCanc;

	private List listaStatoLavorRecord;
	public String statoLavorRecordSelez;


	//	 Inizio Gestione oggetti per carcamento bid per catalogazione ciclica in Indice
	public String presenzaLoadFile = "NO";
	private FormFile uploadImmagine;
    private List listaBidDaFile;
    private boolean caricaConFusioneAutomatica;

    public String elencoBiblioMetropolitane = "";
	private List<BibliotecaVO> filtroLocBib = new ArrayList<BibliotecaVO>();


	public InterrogazioneListeDiConfrontoForm() {
		super();
		listaNomeLista = new ArrayList();
		listaDataLista = new ArrayList();
		listaStatoLavorRecord = new ArrayList();
	}



	public String getProvenienza() {
		return provenienza;
	}
	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}
	public String getRitornoDaEsterna() {
		return ritornoDaEsterna;
	}
	public void setRitornoDaEsterna(String ritornoDaEsterna) {
		this.ritornoDaEsterna = ritornoDaEsterna;
	}
	public List getListaBidDaFile() {
		return listaBidDaFile;
	}
	public void setListaBidDaFile(List listaBidDaFile) {
		this.listaBidDaFile = listaBidDaFile;
	}
	public FormFile getUploadImmagine() {
		return uploadImmagine;
	}
	public void setUploadImmagine(FormFile uploadImmagine) {
		this.uploadImmagine = uploadImmagine;
	}
	public String getPresenzaLoadFile() {
		return presenzaLoadFile;
	}
	public void setPresenzaLoadFile(String presenzaLoadFile) {
		this.presenzaLoadFile = presenzaLoadFile;
	}
	public String getElencoBiblioMetropolitane() {
		return elencoBiblioMetropolitane;
	}
	public void setElencoBiblioMetropolitane(String elencoBiblioMetropolitane) {
		this.elencoBiblioMetropolitane = elencoBiblioMetropolitane;
	}
	public List<BibliotecaVO> getFiltroLocBib() {
		return filtroLocBib;
	}
	public void setFiltroLocBib(List<BibliotecaVO> filtroLocBib) {
		this.filtroLocBib = filtroLocBib;
	}
	public String getNomeListaSelez() {
		return nomeListaSelez;
	}
	public void setNomeListaSelez(String nomeListaSelez) {
		this.nomeListaSelez = nomeListaSelez;
	}
	public String getDataListaSelez() {
		return dataListaSelez;
	}
	public void setDataListaSelez(String dataListaSelez) {
		this.dataListaSelez = dataListaSelez;
	}
	public String getStatoLavorRecordSelez() {
		return statoLavorRecordSelez;
	}
	public void setStatoLavorRecordSelez(String statoLavorRecordSelez) {
		this.statoLavorRecordSelez = statoLavorRecordSelez;
	}

	public List getListaNomeLista() {
		return listaNomeLista;
	}
	public void setListaNomeLista(List listaNomeLista) {
		this.listaNomeLista = listaNomeLista;
	}
	public List addListaNomeLista(ComboCodDescVO newLine) {
		listaNomeLista.add(newLine);
		return listaNomeLista;
	}

	public void svuotaListaDataLista() {
		listaDataLista.removeAll(listaDataLista);
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


	public List getListaStatoLavorRecord() {
		return listaStatoLavorRecord;
	}
	public void setListaStatoLavorRecord(List listaStatoLavorRecord) {
		this.listaStatoLavorRecord = listaStatoLavorRecord;
	}
	public List addListaStatoLavorRecord(String newLine) {
		listaStatoLavorRecord.add(newLine);
		return listaStatoLavorRecord;
	}



	public boolean isCaricaConFusioneAutomatica() {
		return caricaConFusioneAutomatica;
	}



	public void setCaricaConFusioneAutomatica(boolean caricaConFusioneAutomatica) {
		this.caricaConFusioneAutomatica = caricaConFusioneAutomatica;
	}



	public String getDataListaSelezPerCanc() {
		return dataListaSelezPerCanc;
	}



	public void setDataListaSelezPerCanc(String dataListaSelezPerCanc) {
		this.dataListaSelezPerCanc = dataListaSelezPerCanc;
	}





}
