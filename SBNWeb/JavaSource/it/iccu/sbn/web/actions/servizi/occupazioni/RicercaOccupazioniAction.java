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
package it.iccu.sbn.web.actions.servizi.occupazioni;

import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.web.actionforms.servizi.occupazioni.RicercaOccupazioniForm;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;



public class RicercaOccupazioniAction extends ServiziBaseAction {

	private static Logger log = Logger.getLogger(RicercaOccupazioniAction.class);


	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.cerca", "Ok");

		return map;
	}

	private void checkForm(HttpServletRequest request) throws Exception {
		boolean error = false;
		if (error) {
			throw new Exception();
		}
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// setto il token per le transazioni successive
		RicercaOccupazioniForm datiricerca = (RicercaOccupazioniForm) form;
		if (Navigation.getInstance(request).isFromBar())	return mapping.getInputForward();

		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!datiricerca.isSessione()) {
				datiricerca.setSessione(true);
				Navigation navi = Navigation.getInstance(request);
				datiricerca.getAnaOccup().setCodBib(navi.getUtente().getCodBib());
				datiricerca.getAnaOccup().setCodPolo(navi.getUtente().getCodPolo());
				datiricerca.getAnaOccup().setTicket(navi.getUserTicket());
				datiricerca.getAnaOccup().setNumeroElementiBlocco(10);
				datiricerca.getAnaOccup().setOrdinamento("3");
			}
			this.loadTipiOrdinamento(datiricerca, request);
			datiricerca.setLstProfessioni(this.loadProfessioni(request));

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
		}

		return mapping.getInputForward();
	}

	public ActionForward Ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaOccupazioniForm datiricerca = (RicercaOccupazioniForm) form;
		try {
			if (Navigation.getInstance(request).isFromBar())	return mapping.getInputForward();

			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!datiricerca.isSessione()) {
				datiricerca.setSessione(true);
			}
			this.checkForm(request);

			request.setAttribute(ServiziDelegate.PARAMETRI_RICERCA, datiricerca.getAnaOccup());
			return mapping.findForward("ok");
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	private void loadTipiOrdinamento(RicercaOccupazioniForm datiricerca, HttpServletRequest request) throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		List lista = delegate.loadCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_OCCUPAZIONI);
		datiricerca.getAnaOccup().setLstTipiOrdinamento(lista);
	}

}
