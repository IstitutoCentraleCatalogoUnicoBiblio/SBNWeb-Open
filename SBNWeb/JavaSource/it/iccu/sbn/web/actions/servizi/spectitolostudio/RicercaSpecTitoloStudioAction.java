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
package it.iccu.sbn.web.actions.servizi.spectitolostudio;


import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.web.actionforms.servizi.spectitolostudio.RicercaSpecTitoloStudioForm;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

public class RicercaSpecTitoloStudioAction extends ServiziBaseAction {


	@Override
	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("servizi.bottone.cerca", "Ok");

		return map;
	}


	private void checkForm(HttpServletRequest request) throws Exception {
		ActionMessages errors = new ActionMessages();
		boolean error = false;
		if (error) {
			throw new Exception();
		}
	}


	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
										HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		Navigation navi = Navigation.getInstance(request);
		RicercaSpecTitoloStudioForm datiricerca = (RicercaSpecTitoloStudioForm) form;

		this.saveToken(request);

		if (navi.isFromBar())	return mapping.getInputForward();

		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}

			this.loadTipiOrdinamento(request, datiricerca);
			if (!datiricerca.isSessione()) {
				datiricerca.setSessione(true);
				datiricerca.getDatiRicerca().setCodBib(navi.getUtente().getCodBib());
				datiricerca.getDatiRicerca().setCodPolo(navi.getUtente().getCodPolo());
				datiricerca.getDatiRicerca().setTicket(navi.getUserTicket());
				datiricerca.getDatiRicerca().setNumeroElementiBlocco(10);
			}
			this.loadTitoliDiStudio(request, datiricerca);
			datiricerca.getDatiRicerca().setOrdinamento("4");
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
		RicercaSpecTitoloStudioForm datiricerca = (RicercaSpecTitoloStudioForm) form;
		try {
			if (Navigation.getInstance(request).isFromBar())	return mapping.getInputForward();

			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!datiricerca.isSessione()) {
				datiricerca.setSessione(true);
			}
			this.checkForm(request);

			request.setAttribute(ServiziDelegate.PARAMETRI_RICERCA, datiricerca.getDatiRicerca());
			return mapping.findForward("ok");
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}


	private void loadTitoliDiStudio(HttpServletRequest request, RicercaSpecTitoloStudioForm datiricerca)
	throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		List listaCodice = delegate.loadCodici(CodiciType.CODICE_TITOLO_STUDIO);

		datiricerca.setListaTdS(listaCodice);
	}


	private void loadTipiOrdinamento(HttpServletRequest request, RicercaSpecTitoloStudioForm datiricerca)
	throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		List listaCodice = delegate.loadCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_TITOLI_STUDIO);

		datiricerca.getDatiRicerca().setLstTipiOrdinamento(listaCodice);
	}


}
