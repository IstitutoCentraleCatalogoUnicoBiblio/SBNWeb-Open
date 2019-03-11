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

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class ServiziILLForm extends ActionForm {

	private static final long serialVersionUID = 2436765098670844285L;

	private DocumentoNonSbnVO doc = new DocumentoNonSbnVO();
	private BibliotecaVO bib = new BibliotecaVO();
	private String segnatura;
	private String autore;
	private String titolo;
	private List listaTipoDoc;
	private List listabibDest;
	private String luogoEdizione;
	private String editore;
	private String annoEdi;
	private String biblioDest;

	public BibliotecaVO getBib() {
		return bib;
	}

	public void setBib(BibliotecaVO bib) {
		this.bib = bib;
	}

	public List getListabibDest() {
		return listabibDest;
	}

	public void setListabibDest(List listabibDest) {
		this.listabibDest = listabibDest;
	}

	public String getBiblioDest() {
		return biblioDest;
	}

	public void setBiblioDest(String biblioDest) {
		this.biblioDest = biblioDest;
	}

	public DocumentoNonSbnVO getTipoD() {
		return doc;
	}

	public void setTipoD(DocumentoNonSbnVO doc) {
		this.doc = doc;
	}

	public String getAnnoEdi() {
		return annoEdi;
	}

	public void setAnnoEdi(String annoEdi) {
		this.annoEdi = annoEdi;
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

	/*
	 * public ActionErrors validate(ActionMapping mapping, HttpServletRequest
	 * request) { ActionErrors errors = new ActionErrors();
	 *
	 * if(this.titolo != null) { if (getTitolo().length() <= 0) {
	 * errors.add("Titolo", new ActionMessage( "campo.obbligatorio", "Titolo"));
	 * }
	 *
	 * } return errors; }
	 */
}
