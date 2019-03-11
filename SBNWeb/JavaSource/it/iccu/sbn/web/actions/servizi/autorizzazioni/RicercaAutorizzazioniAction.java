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
package it.iccu.sbn.web.actions.servizi.autorizzazioni;

import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actionforms.servizi.autorizzazioni.RicercaAutorizzazioniForm;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class RicercaAutorizzazioniAction extends AutorizzazioniBaseAction {

	private static Logger log = Logger.getLogger(RicercaAutorizzazioniAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("servizi.bottone.cerca", "cerca");
		map.put("servizi.bottone.nuovo", "nuovo");
		map.put("servizi.bottone.annulla", "annulla");
		return map;
	}

	private void checkForm(HttpServletRequest request) throws Exception {
		//ActionMessages errors = new ActionMessages();
		log.info("checkForm()");
		boolean error = false;
		if (error) {
			throw new Exception();
		}
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaAutorizzazioniForm currentForm = (RicercaAutorizzazioniForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
				currentForm.getAutRicerca().setCodPolo(navi.getUtente().getCodPolo());
				currentForm.getAutRicerca().setCodBib(navi.getUtente().getCodBib());
				currentForm.getAutRicerca().setTicket(navi.getUserTicket());
			}
			this.loadTipiOrdinamento(form);
			currentForm.setNonTrovato(false);
			currentForm.getAutRicerca().setNumeroElementiBlocco(10);
			currentForm.getAutRicerca().setOrdinamento("1");

			return mapping.getInputForward();
		} catch (Exception ex) {
			return mapping.findForward("annulla");
		}
	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaAutorizzazioniForm currentForm = (RicercaAutorizzazioniForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
			}
			this.checkForm(request);
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			DescrittoreBloccoVO blocco1 = delegate.caricaListaAutorizzazioni(
					currentForm.getAutRicerca());
			if (blocco1.getTotRighe() > 0) {
				request.setAttribute(ServiziDelegate.LISTA_AUTORIZZAZIONI,
						blocco1);
				request.setAttribute(
						ServiziDelegate.PARAMETRI_RICERCA_AUTORIZZAZIONI,
						currentForm.getAutRicerca());

				request.setAttribute("PathForm", mapping.getPath());
				return mapping.findForward("ok");
			} else {
				currentForm.setNonTrovato(true);
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.servizi.ListaVuota"));
				this.saveErrors(request, errors);
				resetToken(request);
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getMessage()));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaAutorizzazioniForm currentForm = (RicercaAutorizzazioniForm) form;

		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		currentForm.getAutRicerca().setNuovaAut(AutorizzazioneVO.NEW);

		this.resetToken(request);

		//request.setAttribute("RicercaAut", this.currentForm.getAutRicerca());
		request.setAttribute(ServiziDelegate.PARAMETRI_RICERCA_AUTORIZZAZIONI,currentForm.getAutRicerca());
		return mapping.findForward("nuovo");
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaAutorizzazioniForm currentForm = (RicercaAutorizzazioniForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		currentForm.clear();
		return mapping.getInputForward();
	}

	private void loadTipiOrdinamento(ActionForm form) throws Exception {
		RicercaAutorizzazioniForm currentForm = (RicercaAutorizzazioniForm) form;
		List<TB_CODICI> listaCodice = CodiciProvider.getCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_AUTORIZZAZIONI);
		CaricamentoCombo carCombo = new CaricamentoCombo();
		currentForm.setLstTipiOrdinamento(carCombo.loadComboCodiciDesc(listaCodice));

	}
}
