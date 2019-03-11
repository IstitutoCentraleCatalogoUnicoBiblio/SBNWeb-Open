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
package it.iccu.sbn.web.actions.servizi.materieinteresse;

import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaMateriaVO;
import it.iccu.sbn.web.actionforms.servizi.materieinteresse.RicercaMaterieInteresseForm;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class RicercaMaterieInteresseAction extends ServiziBaseAction {
	private static Log log = LogFactory.getLog(RicercaMaterieInteresseAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("servizi.bottone.cerca", "cerca");
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
		RicercaMaterieInteresseForm currentForm = (RicercaMaterieInteresseForm) form;
		Navigation navi = Navigation.getInstance(request);

		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
				RicercaMateriaVO ricerca = currentForm.getDatiRicerca();
				ricerca.setCodBib(navi.getUtente().getCodBib());
				ricerca.setCodPolo(navi.getUtente().getCodPolo());
				ricerca.setTicket(navi.getUserTicket());
				ricerca.setNumeroElementiBlocco(10);
				this.loadTipiOrdinamento(request, form);
				//almaviva5_20120308 #4885
				ricerca.setOrdinamento("1");
			}
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
		}

		return mapping.getInputForward();
	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaMaterieInteresseForm currentForm = (RicercaMaterieInteresseForm) form;
		try {
			if (Navigation.getInstance(request).isFromBar())	return mapping.getInputForward();

			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
			}
			this.checkForm(request);

			request.setAttribute(ServiziDelegate.PARAMETRI_RICERCA, currentForm.getDatiRicerca());

			return mapping.findForward("ok");
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

	private void loadTipiOrdinamento(HttpServletRequest request, ActionForm form) throws Exception {
		RicercaMaterieInteresseForm datiricerca = (RicercaMaterieInteresseForm) form;

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		List listaCodice = delegate.loadCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_MATERIE);

		datiricerca.setLstTipiOrdinamento(listaCodice);
	}
}
