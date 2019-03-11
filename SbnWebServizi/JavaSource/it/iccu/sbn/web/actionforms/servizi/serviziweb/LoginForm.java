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
import it.iccu.sbn.ejb.vo.documentofisico.RichiestaOpacVO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


public class LoginForm extends ActionForm {

	private static final long serialVersionUID = 5587111227897580212L;

	private boolean initialized;

	private String userName;
	private String password;
	// campi necessari per l'inserimento della
	// richiesta provenienza OPAC

	// fine campi inserimento richiesta
	private List<BibliotecaVO> listaBiblio;

	private RichiestaOpacVO richiesta = new RichiestaOpacVO();

	private String welcome;

	private boolean iscrizione;
	private boolean richiestaILL;
	private BibliotecaVO bib = new BibliotecaVO();

	private String sip2_ticket;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<BibliotecaVO> getListaBiblio() {

		return listaBiblio;
	}

	public void setListaBiblio(List<BibliotecaVO> listaBiblio) {
		this.listaBiblio = listaBiblio;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		//
		ActionErrors errors = new ActionErrors();
		//
		String bottone;
		//
		bottone = request.getParameter("paramLogin");
		//
		if (bottone != null) {
			if (bottone.equals("Autoregistrazione")) {

			}
			if (bottone.equals("Recupero Password")) {
				if (getUserName().length() <= 0) {
					errors.add("User Name", new ActionMessage(
							"campo.obbligatorio", "Utente"));
				}
			}
			if (bottone.equals("login") || bottone.equals("Cambio Password")) {
				if (this.userName != null && this.password != null) {
					if (getUserName().length() <= 0) {
						errors.add("User Name", new ActionMessage(
								"campo.obbligatorio", "Utente"));
					}
					if (getPassword().length() <= 0) {
						errors.add("password", new ActionMessage(
								"campo.obbligatorio", "Password"));
					}
				}
			}
		}
		return errors;
	}

	public String getWelcome() {
		return welcome;
	}

	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public void setIscrizione(boolean autoReg) {
		this.iscrizione = autoReg;
	}

	public boolean isRichiestaILL() {
		return richiestaILL;
	}

	public void setRichiestaILL(boolean richiestaILL) {
		this.richiestaILL = richiestaILL;
	}

	public BibliotecaVO getBib() {
		return bib;
	}

	public void setBib(BibliotecaVO bib) {
		this.bib = bib;
	}

	public boolean isIscrizione() {
		return iscrizione;
	}

	public String getSip2_ticket() {
		return sip2_ticket;
	}

	public void setSip2_ticket(String sip2_ticket) {
		this.sip2_ticket = sip2_ticket;
	}

	public RichiestaOpacVO getRichiesta() {
		return richiesta;
	}

	public void setRichiesta(RichiestaOpacVO richiesta) {
		this.richiesta = richiesta;
	}

}
