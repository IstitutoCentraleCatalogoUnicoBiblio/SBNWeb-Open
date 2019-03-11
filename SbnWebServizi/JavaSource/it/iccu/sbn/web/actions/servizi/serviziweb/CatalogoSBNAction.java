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
package it.iccu.sbn.web.actions.servizi.serviziweb;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.bd.serviziweb.LogoutDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class CatalogoSBNAction extends LookupDispatchAction {

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		return map;
	}
	//
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		HttpSession session = request.getSession();
		UtenteWeb utente = (UtenteWeb)session.getAttribute(Constants.UTENTE_WEB_KEY);
		//
		TB_CODICI codicePolo = CodiciProvider.cercaCodice(utente.getCodPolo(),
				CodiciType.CODICE_CONFIGURAZIONE_URL_OPAC,
				CodiciRicercaType.RICERCA_CODICE_SBN);
		TB_CODICI codiceBib = CodiciProvider.cercaCodice(utente.getCodBib(),
				CodiciType.CODICE_CONFIGURAZIONE_URL_OPAC,
				CodiciRicercaType.RICERCA_CODICE_SBN);
		if (codiceBib == null)	// non trovo la riga per la bib. leggo configurazione default di polo (*)
			codiceBib = codicePolo;

		String path = mapping.getInputForward().getPath();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			String type = request.getParameter(Constants.TIPO_CATALOGO);
			if (type.equals("SBN")) {
				path = ValidazioneDati.isFilled(codiceBib.getCd_flg2()) ? codiceBib.getCd_flg2() : codicePolo.getCd_flg2();

			} else {
				//non sbn
				path = ValidazioneDati.isFilled(codiceBib.getCd_flg4()) ? codiceBib.getCd_flg4() : codicePolo.getCd_flg4();
			}

			LogoutDelegate.logout(request);
			request.setAttribute(Constants.OPAC_URL, path);

			return mapping.findForward("catalogo");

		}
		catch (Exception ex) {
			//Errore nel caricamento del Catalogo SBN
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errore.servizi.lista.diritti.utente"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this, mapping, request));
			return (mapping.getInputForward());
		}
	}
}
