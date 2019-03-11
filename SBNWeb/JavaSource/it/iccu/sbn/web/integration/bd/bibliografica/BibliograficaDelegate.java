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
package it.iccu.sbn.web.integration.bd.bibliografica;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class BibliograficaDelegate {

	private static final String BIBLIOGRAFICA_DELEGATE = "req.gest.biblio.delegate";

	private static Logger log = Logger.getLogger(BibliograficaDelegate.class);

	private final UserVO utente;
	private final HttpServletRequest request;
	private final FactoryEJBDelegate factory;


	public static final BibliograficaDelegate getInstance(HttpServletRequest request) throws Exception {
		BibliograficaDelegate delegate = (BibliograficaDelegate) request.getAttribute(BIBLIOGRAFICA_DELEGATE);
		if (delegate == null) {
			delegate = new BibliograficaDelegate(request);
			request.setAttribute(BIBLIOGRAFICA_DELEGATE, delegate);
		}
		return delegate;
	}

	private BibliograficaDelegate(HttpServletRequest request) throws Exception {
		Navigation navi = Navigation.getInstance(request);
		this.request = request;
		factory = FactoryEJBDelegate.getInstance();
		utente = navi.getUtente();
		//utenteEjb = (Utente) request.getSession().getAttribute(Constants.UTENTE_BEAN);

	}

	public int countLocalizzazioniPendenti()  throws Exception {
		CommandInvokeVO command = new CommandInvokeVO(utente.getTicket(), CommandType.GB_LOCALIZZAZIONI_PENDENTI);
		try {
			CommandResultVO result = factory.getGestioneBibliografica().invoke(command);
			result.throwError();

			Number cnt = (Number) result.getResult();
			return cnt.intValue();

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		} catch (ValidationException e) {
			LinkableTagUtils.addError(request, e);
		}

		return 0;
	}

}
