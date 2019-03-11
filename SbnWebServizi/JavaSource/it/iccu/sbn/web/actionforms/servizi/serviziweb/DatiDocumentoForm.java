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

import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class DatiDocumentoForm extends ActionForm {

	private static final long serialVersionUID = -835885603793671990L;

	private DocumentoNonSbnVO doc = new DocumentoNonSbnVO();
	private String segnatura;

	private int idDocLettore;
	private String autore;
	private String titolo;
	private String luogoEdizione;
	private String editore;
	private List listaTipoDoc;
	private String annoEdi;
	private Short numVolume;
	private String annataPeriodici;
	private String suggerimenti;

	private String utenteCon;
	private String biblioSel;
	private String ambiente;
	private boolean lettura;

	public String getBiblioSel() {
		return biblioSel;
	}

	public void setBiblioSel(String biblioSel) {
		this.biblioSel = biblioSel;
	}

	public DocumentoNonSbnVO getDoc() {
		return doc;
	}

	public void setDoc(DocumentoNonSbnVO doc) {
		this.doc = doc;
	}

	public String getUtenteCon() {
		return utenteCon;
	}

	public void setUtenteCon(String utenteCon) {
		this.utenteCon = utenteCon;
	}

	public String getAutore() {
		return autore;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public String getEditore() {
		return editore;
	}

	public void setEditore(String editore) {
		this.editore = editore;
	}

	public List getListaTipoDoc() {
		return listaTipoDoc;
	}

	public void setListaTipoDoc(List listaTipoDoc) {
		this.listaTipoDoc = listaTipoDoc;
	}

	public String getLuogoEdizione() {
		return luogoEdizione;
	}

	public void setLuogoEdizione(String luogoEdizione) {
		this.luogoEdizione = luogoEdizione;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getSegnatura() {
		return segnatura;
	}

	public void setSegnatura(String segnatura) {
		this.segnatura = segnatura;
	}

	public void setNumVolume(Short numVolume) {
		this.numVolume = numVolume;
	}

	public Short getNumVolume() {
		return numVolume;
	}

	public void setSuggerimenti(String suggerimenti) {
		this.suggerimenti = suggerimenti;
	}

	public String getSuggerimenti() {
		return suggerimenti;
	}

	public void setAnnoEdi(String annoEdi) {
		this.annoEdi = annoEdi;
	}

	public String getAnnoEdi() {
		return annoEdi;
	}

	public void setAnnataPeriodici(String annataPeriodici) {
		this.annataPeriodici = annataPeriodici;
	}

	public String getAnnataPeriodici() {
		return annataPeriodici;
	}

	public void setIdDocLettore(int idDocLettore) {
		this.idDocLettore = idDocLettore;
	}

	public int getIdDocLettore() {
		return idDocLettore;
	}

	public void setLettura(boolean lettura) {
		this.lettura = lettura;
	}

	public boolean isLettura() {
		return lettura;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getAmbiente() {
		return ambiente;
	}

}
