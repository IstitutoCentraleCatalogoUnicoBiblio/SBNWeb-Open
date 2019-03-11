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
package it.iccu.sbn.web.actionforms.servizi.serviziweb;

import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class EsameRichiesteForm extends ActionForm {

	private static final long serialVersionUID = 3096568821448066377L;

	private MovimentoListaVO detMov = new MovimentoListaVO();
	private MovimentoListaVO movimentoSalvato = new MovimentoListaVO();
	/**
	 * Dettagli del servizio associato al movimento in esame
	 */
	private ServizioBibliotecaVO servizioMovimento;
	//
	// Campi dettaglio Lista
	private List<MovimentoVO> listaRichieste = new ArrayList<MovimentoVO>();

	//
	private boolean testProroga;
	//
	private String utenteCon;
	private String biblioSel;
	private String ambiente;
	//
	private boolean flgInCorso = true;
	private boolean flgPrenotazioni = false;
	private boolean flgRespinte = false;
	private boolean flgEvase = false;
	//
	private Date dataProrogaMassima = null;
	//
	private short numeroRinnovo = 0;

	private boolean initialized;
	private String[] folders;
	private Integer currentFolder = 0;
	private MovimentoRicercaVO filtro = new MovimentoRicercaVO();

	//
	public void setUtenteCon(String utenteCon) {
		this.utenteCon = utenteCon;
	}

	public String getUtenteCon() {
		return utenteCon;
	}

	//
	public void setBiblioSel(String biblioSel) {
		this.biblioSel = biblioSel;
	}

	public String getBiblioSel() {
		return biblioSel;
	}

	public void setDetMov(MovimentoListaVO detMov) {
		this.detMov = detMov;
	}

	public MovimentoListaVO getDetMov() {
		return detMov;
	}

	public void setServizioMovimento(ServizioBibliotecaVO servizioMovimento) {
		this.servizioMovimento = servizioMovimento;
	}

	public ServizioBibliotecaVO getServizioMovimento() {
		return servizioMovimento;
	}

	public void setMovimentoSalvato(MovimentoListaVO movimentoSalvato) {
		this.movimentoSalvato = movimentoSalvato;
	}

	public MovimentoListaVO getMovimentoSalvato() {
		return movimentoSalvato;
	}

	public Date getDataProrogaMassima() {
		return dataProrogaMassima;
	}

	public void setDataProrogaMassima(Date dataProrogaMassima) {
		this.dataProrogaMassima = dataProrogaMassima;
	}

	//
	public short getNumeroRinnovo() {
		return numeroRinnovo;
	}

	public void setNumeroRinnovo(short numeroRinnovo) {
		this.numeroRinnovo = numeroRinnovo;
	}

	public void setTestProroga(boolean testProroga) {
		this.testProroga = testProroga;
	}

	public boolean isTestProroga() {
		return testProroga;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setFlgInCorso(boolean flgInCorso) {
		this.flgInCorso = flgInCorso;
	}

	public boolean isFlgInCorso() {
		return flgInCorso;
	}

	public void setFlgPrenotazioni(boolean flgPrenotazioni) {
		this.flgPrenotazioni = flgPrenotazioni;
	}

	public boolean isFlgPrenotazioni() {
		return flgPrenotazioni;
	}

	public void setFlgRespinte(boolean flgRespinte) {
		this.flgRespinte = flgRespinte;
	}

	public boolean isFlgRespinte() {
		return flgRespinte;
	}

	public void setFlgEvase(boolean flgEvase) {
		this.flgEvase = flgEvase;
	}

	public boolean isFlgEvase() {
		return flgEvase;
	}

	public List<MovimentoVO> getListaRichieste() {
		return listaRichieste;
	}

	public void setListaRichieste(List<MovimentoVO> listaRichieste) {
		this.listaRichieste = listaRichieste;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
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

	public MovimentoRicercaVO getFiltro() {
		return filtro;
	}

	public void setFiltro(MovimentoRicercaVO filtro) {
		this.filtro = filtro;
	}

}
