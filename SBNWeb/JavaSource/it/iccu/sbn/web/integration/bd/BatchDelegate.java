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
package it.iccu.sbn.web.integration.bd;

import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.RichiestaElaborazioniDifferiteVO;
import it.iccu.sbn.web.integration.BusinessDelegateException;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class BatchDelegate {
	public static final String SINTETICA_RICHIESTE = "sint.richieste.batch";
	public static final String PARAMETRI_RICERCA = "param.ricerca.batch";
	public static final String DETTAGLIO_BATCH = "dettaglio.richiesta.batch";
	public static final String RICERCA_BATCH_BOOKMARK = "ricerca.batch.bookmark";

	private static Logger log = Logger.getLogger(BatchDelegate.class);

	private static BatchDelegate instance;
	private final FactoryEJBDelegate factory;
	private final UserVO utenteCollegato;
	private final HttpServletRequest request;

	public BatchDelegate(HttpServletRequest request, FactoryEJBDelegate factory,
			UserVO utenteCollegato) {
		this.request = request;
		this.factory = factory;
		this.utenteCollegato = utenteCollegato;
	}

	synchronized public static BatchDelegate getInstance()
			throws BusinessDelegateException {
		if (instance == null) {
			instance = new BatchDelegate(null, null, null);
		}
		return instance;
	}

	protected void saveErrors(HttpServletRequest request,
			ActionMessages errors, Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);

		if (errors == null || errors.isEmpty()) {
			request.removeAttribute("org.apache.struts.action.ERROR");
			return;
		} else {
			request.setAttribute("org.apache.struts.action.ERROR", errors);
			return;
		}
	}

	public static BatchDelegate getDelegate(HttpServletRequest request)
			throws Exception {
		return new BatchDelegate(request, FactoryEJBDelegate.getInstance(),
				Navigation.getInstance(request).getUtente());
	}

	public DescrittoreBloccoVO cercaRichieste(
			RichiestaElaborazioniDifferiteVO richiesta) {
		ActionMessages errors = new ActionMessages();
		try {
			DescrittoreBloccoVO blocco1 = factory.getElaborazioniDifferite()
					.getRichiesteElaborazioniDifferite(
							utenteCollegato.getTicket(), richiesta,
							richiesta.getNumeroElementiBlocco() );

			if (blocco1.getTotRighe() > 0)
				return blocco1;

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.elabdiff.nonTrovato"));
			saveErrors(request, errors, null);
			return null;

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors, e);
			log.error("", e);
			return null;
		}

	}

	public boolean eliminaRichiesteElaborazioniDifferite(String[] richieste, Boolean deleteOutputs) {
		ActionMessages errors = new ActionMessages();
		try {
			factory.getElaborazioniDifferite().eliminaRichiesteElaborazioniDifferite(utenteCollegato.getTicket(), richieste, deleteOutputs);
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.cancellaOK"));
			saveErrors(request, errors, null);
			return true;

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.gestioneSemantica.erroreSistema", e.getMessage()));
			this.saveErrors(request, errors, e);
			log.error("", e);
			return false;
		}


	}

}
