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
package it.iccu.sbn.web.actions.servizi.utenti;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.ServizioVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actionforms.servizi.utenti.ListaServiziForm;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ListaServiziAction extends SinteticaLookupDispatchAction {

	private static Logger log = Logger.getLogger(ListaServiziAction.class);

	//private ListaServiziForm listaServiziForm;

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.scegli", "scegli");
		map.put("servizi.bottone.annulla", "annulla");
		map.put("button.blocco", "blocco");
		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaServiziForm currentForm = (ListaServiziForm) form;
		if (Navigation.getInstance(request).isFromBar() )
			return mapping.getInputForward();

		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione()) {
			log.debug("init()");
			currentForm.setSessione(true);
			//la prima volta che entro immetto i parametri nella form
			currentForm.setCodBibUte((String) request
					.getAttribute("CodiceBibUtente"));
			currentForm.setCodUte((String) request
					.getAttribute("CodiceUtente"));
			currentForm.setCodPolo((String) request
					.getAttribute("CodicePolo"));
			currentForm.setCodBib((String) request
					.getAttribute("CodiceBibServizio"));
			DescrittoreBloccoVO blocco1 = ((DescrittoreBloccoVO) request
					.getAttribute(ServiziDelegate.LISTA_ANAGRAFICA_SERVIZI_BIBLIOTECA));
			// abilito i tasti per il blocco se necessario
			// memorizzo le informazioni per la gestione blocchi
			currentForm.setIdLista(blocco1.getIdLista());
			currentForm.setTotRighe(blocco1.getTotRighe());
			currentForm.setTotBlocchi(blocco1.getTotBlocchi());
			currentForm.setBloccoSelezionato(blocco1.getNumBlocco());
			currentForm.setAbilitaBlocchi(blocco1.getTotBlocchi() > 1);
			currentForm.setElencoScegliServizio(blocco1.getLista());
			// SALVO LA LISTA PASSATA DEI SERVIZI GIA' ASSOCIATI
			currentForm.setServSelezionati((List) request
					.getAttribute("ServAssociati"));
		}
		// chiamo mappa per visualizzare la lista filtrata

		if (ValidazioneDati.isFilled(currentForm.getElencoScegliServizio()) )
		{
			for (int index = 0; index <currentForm.getElencoScegliServizio().size(); index++) {
				ServizioVO srv = (ServizioVO) currentForm.getElencoScegliServizio().get(index);
				srv.setDescrizioneTipoServizio(CodiciProvider.cercaDescrizioneCodice(srv.getCodice(),
						CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));
			}
		}
		return mapping.getInputForward();
	}

	public ActionForward scegli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaServiziForm listaServiziForm = (ListaServiziForm) form;
		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!listaServiziForm.isSessione())
				listaServiziForm.setSessione(true);

			resetToken(request);
			this.lasciaSoloSelezionati(listaServiziForm.getElencoScegliServizio());
			boolean rit = true;
			rit=this.validaSelezione(listaServiziForm);
			if (rit) {
				this.aggiornaListaServizi(listaServiziForm.getElencoScegliServizio(),
						listaServiziForm.getServSelezionati());
				request.setAttribute("ServSelezionati", listaServiziForm.getServSelezionati());
				request.setAttribute("VengoDa", "ListaServizi");
				return mapping.findForward("ok");
			} else {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(
					"errors.servizi.associazioneImpossibileTipoServizioDuplicato"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		} catch (Exception e) {
			return mapping.getInputForward();
		}
	}

	private boolean validaSelezione(ActionForm form) {
		ListaServiziForm listaServiziForm = (ListaServiziForm) form;
		Set<String> test = new HashSet<String>();
		int index = 0;
		List elencoScegliServizio = listaServiziForm.getElencoScegliServizio();
		// SE GIA' PRESENTE SELEZIONATO + DI UN SEVRVIZIO CON UGUALE TIPO SERVIZIO
		while (index < elencoScegliServizio.size()) {
			ServizioVO servizio = (ServizioVO) elencoScegliServizio.get(index);
			if (test.contains(servizio.getCodice())) return false;
			test.add(servizio.getCodice());
			index++;
		}
		return true;
	}

	private void lasciaSoloSelezionati(List elencoScegliServizio) {
		if (elencoScegliServizio == null)
			return;
		int index = 0;
		while (index < elencoScegliServizio.size()) {
			ServizioVO serBib = (ServizioVO) elencoScegliServizio.get(index);
			if (serBib.getCancella() == null
					|| !serBib.getCancella().equals("C")) {
				elencoScegliServizio.remove(serBib);
				index--;
			}
			index++;
		}
	}

	private void aggiornaListaServizi(List elencoScegliServizio,
			List serviziUtente) {
		if (elencoScegliServizio == null && elencoScegliServizio.size() == 0)
			return;
		int index = 0;
		while (index < elencoScegliServizio.size()) {
			ServizioVO serBib = (ServizioVO) elencoScegliServizio.get(index);
			serBib.setCancella("");
			serBib.setStato(ServizioVO.NEW);
			for (int index2 = 0; index2 < serviziUtente.size(); index2++) {
				ServizioVO serUte = (ServizioVO) serviziUtente.get(index2);
				// se sono lo stesso servizio ed era cancellato lo ripristino
				// togliendolo dalla lista dei passati e inserendolo con i dati
				// modificati dalla lista dei selezionati in quella dei passati
				// per tornare e ricaricare i servizi utente
//				if (serUte.getCodice().equals(serBib.getCodice())) {
				if (serUte.getIdServizio() == serBib.getIdServizio()) {
					if (serUte.getStato() == ServizioVO.DELDELMOD) {
						serBib.setStato(ServizioVO.MOD);
					}
					if (serUte.getStato() == ServizioVO.DELDELOLD) {
						serBib.setStato(ServizioVO.OLD);
					}
					if (serUte.getStato() == ServizioVO.DELELI) {
						serBib.setStato(ServizioVO.NEW);
					}
					serviziUtente.remove(serUte);
					break;
				}
			}
			index++;
			serviziUtente.add(serBib);
		}
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaServiziForm listaServiziForm = (ListaServiziForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		if (!listaServiziForm.isSessione())
			listaServiziForm.setSessione(true);

		resetToken(request);
		listaServiziForm.getElencoScegliServizio().clear();
		// RESTITUISCO I SELEZIONATI PASSATI CON REQUEST
		request.setAttribute("ServSelezionati", listaServiziForm.getServSelezionati());
		request.setAttribute("VengoDa", "ListaServizi");
		return mapping.findForward("annulla");
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaServiziForm listaServiziForm = (ListaServiziForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		if (!listaServiziForm.isSessione())
			listaServiziForm.setSessione(true);

		int numBlocco = listaServiziForm.getBloccoSelezionato();
		String idLista = listaServiziForm.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco > 1 && idLista != null) {
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			DescrittoreBloccoVO bloccoVO = delegate.caricaBlocco(ticket, idLista, numBlocco);
			if (bloccoVO != null) {
				listaServiziForm.getElencoScegliServizio().addAll(bloccoVO.getLista());
				if (bloccoVO.getNumBlocco() < bloccoVO.getTotBlocchi())
					listaServiziForm.setBloccoSelezionato(bloccoVO.getNumBlocco() + 1);

				Collections.sort(listaServiziForm.getElencoScegliServizio(), BaseVO.ORDINAMENTO_PER_PROGRESSIVO);
			}
		}
		return mapping.getInputForward();
	}

}
