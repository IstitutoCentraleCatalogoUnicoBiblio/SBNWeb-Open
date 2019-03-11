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

import it.iccu.sbn.ejb.vo.servizi.serviziweb.ListaServiziVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class SceltaServiziForm extends ActionForm {

	private static final long serialVersionUID = 6567443799901264546L;

	private ListaServiziVO listaServizi;

	private String titolo;

	private String segnatura;

	private String utenteCon;

	private String biblioSel;

	private List listaServ;

	public ListaServiziVO getListaServizi() {
		return listaServizi;
	}

	public void setListaServizi(ListaServiziVO listaServizi) {
		this.listaServizi = listaServizi;
	}

	public String getSegnatura() {
		return segnatura;
	}

	public void setSegnatura(String segnatura) {
		this.segnatura = segnatura;
	}

	public List getListaServ() {
		return listaServ;
	}

	public void setListaServ(List listaServ) {
		this.listaServ = listaServ;
	}

	public String getBiblioSel() {
		return biblioSel;
	}

	public void setBiblioSel(String biblioSel) {
		this.biblioSel = biblioSel;
	}

	public String getUtenteCon() {
		return utenteCon;
	}

	public void setUtenteCon(String utenteCon) {
		this.utenteCon = utenteCon;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		if (this.segnatura != null) {
			if (getSegnatura().length() <= 0) {
				errors.add("Segnatura", new ActionMessage("campo.obbligatorio",
						"Segnatura"));
			}

		}
		return errors;
	}

}
