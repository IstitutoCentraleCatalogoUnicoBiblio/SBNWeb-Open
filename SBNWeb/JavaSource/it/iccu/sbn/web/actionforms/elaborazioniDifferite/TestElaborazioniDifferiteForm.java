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
package it.iccu.sbn.web.actionforms.elaborazioniDifferite;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class TestElaborazioniDifferiteForm extends ActionForm {


	private static final long serialVersionUID = -4411574740734155971L;
	private boolean sessione;
	private String codiceBibl;

	String procedura;
	String codaAssegnata;
	String idRichiestaAssegnato;


	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String getCodiceBibl() {
		return codiceBibl;
	}
	public void setCodiceBibl(String codiceBibl) {
		this.codiceBibl = codiceBibl;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        return errors;
    }
	public String getCodaAssegnata() {
		return codaAssegnata;
	}
	public void setCodaAssegnata(String codaAssegnata) {
		this.codaAssegnata = codaAssegnata;
	}
	public String getIdRichiestaAssegnato() {
		return idRichiestaAssegnato;
	}
	public void setIdRichiestaAssegnato(String idRichiestaAssegnato) {
		this.idRichiestaAssegnato = idRichiestaAssegnato;
	}
	public String getProcedura() {
		return procedura;
	}
	public void setProcedura(String procedura) {
		this.procedura = procedura;
	}

}
