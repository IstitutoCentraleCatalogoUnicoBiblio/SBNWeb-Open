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


import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppDocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class EsameSugAcqForm extends ActionForm {

	private static final long serialVersionUID = 5237755223662570576L;

	private StrutturaCombo StatoSug;
	private ListaSuppDocumentoVO sugAcq = new ListaSuppDocumentoVO();
	//Campi dettaglio Lista listaSugAcq
	private List listaSugAcq;
	private List listaStatoSuggerimento;
	//Campi intervallo suggerimento di acquisto
	private String dal;
	private String al;
	//Campi testata listaStatoSugAcq
	private String utenteCon;
	private String biblioSel;
	private String ambiente;

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
	//
	public void setListaSugAcq(List listaSugAcq) {
		this.listaSugAcq = listaSugAcq;
	}
	public List getListaSugAcq() {
		return listaSugAcq;
	}
	//
	public List getListaStatoSuggerimento() {
		return listaStatoSuggerimento;
	}

	public void setListaStatoSuggerimento(List listaStatoSuggerimento) {
		this.listaStatoSuggerimento = listaStatoSuggerimento;
	}

	//
	public void setDal(String dal) {
		this.dal = dal;
	}
	public String getDal() {
		return dal;
	}
	public void setAl(String al) {
		this.al = al;
	}
	public String getAl() {
		return al;
	}
	public void setSugAcq(ListaSuppDocumentoVO sugAcq) {
		this.sugAcq = sugAcq;
	}
	public ListaSuppDocumentoVO getSugAcq() {
		return sugAcq;
	}

	public void setStatoSug(StrutturaCombo statoSug) {
		StatoSug = statoSug;
	}
	public StrutturaCombo getStatoSug() {
		return StatoSug;
	}
	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}
	public String getAmbiente() {
		return ambiente;
	}


}
