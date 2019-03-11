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

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class ListaMovimentiOpacForm extends ActionForm {

	private static final long serialVersionUID = 4035668725498445734L;

	// LstCodiciServizio campi per l'inseromento della richiesta
	private String cod_biblio;
	private String titolo;
	private String autore;
	private String anno;
	private List<ServizioBibliotecaVO> lstCodiciServizio;
	private List<ServizioBibliotecaVO> lstCodiciServizioDoc;// Modifica del
															// 22/03/2010
															// almaviva
	// Dati documento sbn
	private String codBibInv;
	private String codSerieInv;
	private String codInvenInv;
	private String utenteCon;
	private String biblioSel;
	private String codUtente;
	private String ambiente;
	private String annoPeriodico;

	private InfoDocumentoVO infoDocumentoVO = new InfoDocumentoVO();
	// conferma pre operazioni
	private boolean flg = false;

	private boolean flgServDisDoc = false;

	private boolean conferma = false;
	private String servizioScelto;
	private String codservizioScelto;
	// getServizioScelto lstCodiciServizio codServ servizioScelto descrTipoServ
	// campo per indicare se ricerca è per Inv Seg Ute
	private String tipoRicerca;

	public String getServizioScelto() {
		return servizioScelto;
	}

	public void setServizioScelto(String servizioScelto) {
		this.servizioScelto = servizioScelto;
	}

	// Dati documento non sbn
	private String codSegnatura;
	private String editore;
	private String luogoEdizione;
	private String tipoDoc;

	public String getCodSegnatura() {
		return codSegnatura;
	}

	public void setCodSegnatura(String codSegnatura) {
		this.codSegnatura = codSegnatura;
	}

	public String getCodDocLet() {
		return codDocLet;
	}

	public void setCodDocLet(String codDocLet) {
		this.codDocLet = codDocLet;
	}

	public String getProgrEsempDocLet() {
		return progrEsempDocLet;
	}

	public void setProgrEsempDocLet(String progrEsempDocLet) {
		this.progrEsempDocLet = progrEsempDocLet;
	}

	private String codDocLet;
	private String progrEsempDocLet;

	private String tipoDocLet;

	public String getCodBibInv() {
		return codBibInv;
	}

	public void setCodBibInv(String codBibInv) {
		this.codBibInv = codBibInv;
	}

	public String getCodSerieInv() {
		return codSerieInv;
	}

	public void setCodSerieInv(String codSerieInv) {
		this.codSerieInv = codSerieInv != null ? codSerieInv.toUpperCase()
				: null;
	}

	public String getCodInvenInv() {
		return codInvenInv;
	}

	public void setCodInvenInv(String codInvenInv) {
		this.codInvenInv = codInvenInv;
	}

	public void setCod_biblio(String cod_biblio) {
		this.cod_biblio = cod_biblio;
	}

	public String getCod_biblio() {
		return cod_biblio;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public String getAutore() {
		return autore;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getAnno() {
		return anno;
	}

	public void setUtenteCon(String utenteCon) {
		this.utenteCon = utenteCon;
	}

	public String getUtenteCon() {
		return utenteCon;
	}

	public void setBiblioSel(String biblioSel) {
		this.biblioSel = biblioSel;
	}

	public String getBiblioSel() {
		return biblioSel;
	}

	public void setLstCodiciServizio(
			List<ServizioBibliotecaVO> lstCodiciServizio) {
		this.lstCodiciServizio = lstCodiciServizio;
	}

	public List<ServizioBibliotecaVO> getLstCodiciServizio() {
		return lstCodiciServizio;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setCodUtente(String codUtente) {
		this.codUtente = ValidazioneDati.trimOrEmpty(codUtente);
	}

	public String getCodUtente() {
		return codUtente;
	}

	public InfoDocumentoVO getInfoDocumentoVO() {
		return infoDocumentoVO;
	}

	public void setInfoDocumentoVO(InfoDocumentoVO infoDocumentoVO) {
		this.infoDocumentoVO = infoDocumentoVO;
	}

	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}

	public String getTipoRicerca() {
		return tipoRicerca;
	}

	public void setCodservizioScelto(String codservizioScelto) {
		this.codservizioScelto = codservizioScelto;
	}

	public String getCodservizioScelto() {
		return codservizioScelto;
	}

	public void setEditore(String editore) {
		this.editore = editore;
	}

	public String getEditore() {
		return editore;
	}

	public void setLuogoEdizione(String luogoEdizione) {
		this.luogoEdizione = luogoEdizione;
	}

	public String getLuogoEdizione() {
		return luogoEdizione;
	}

	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	public String getTipoDoc() {
		return tipoDoc;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setFlg(boolean flg) {
		this.flg = flg;
	}

	public boolean isFlg() {
		return flg;
	}

	public void setLstCodiciServizioDoc(
			List<ServizioBibliotecaVO> lstCodiciServizioDoc) {
		this.lstCodiciServizioDoc = lstCodiciServizioDoc;
	}

	public List<ServizioBibliotecaVO> getLstCodiciServizioDoc() {
		return lstCodiciServizioDoc;
	}

	public void setFlgServDisDoc(boolean flgServDisDoc) {
		this.flgServDisDoc = flgServDisDoc;
	}

	public boolean isFlgServDisDoc() {
		return flgServDisDoc;
	}

	public String getAnnoPeriodico() {
		return annoPeriodico;
	}

	public void setAnnoPeriodico(String annoPeriodico) {
		this.annoPeriodico = annoPeriodico;
	}

	public void setTipoDocLet(String tipoDocLet) {
		this.tipoDocLet = tipoDocLet;
	}

	public String getTipoDocLet() {
		return tipoDocLet;
	}

}
