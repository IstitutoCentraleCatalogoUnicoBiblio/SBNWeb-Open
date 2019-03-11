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
package it.iccu.sbn.web.actions.elaborazioniDifferite;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElementoSinteticaElabDiffVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.RichiestaElaborazioniDifferiteVO;
import it.iccu.sbn.web.actionforms.elaborazioniDifferite.SinteticaElaborazioniDifferiteForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.integration.bd.BatchDelegate;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class SinteticaElaborazioniDifferiteAction extends
		SinteticaLookupDispatchAction {

	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.blocco", "blocco");
		map.put("button.esamina", "esamina");
		map.put("button.indietro", "indietro");
		map.put("button.aggiorna", "aggiorna");

		map.put("button.selAllTitoli", "tutti");
		map.put("button.deSelAllTitoli", "nessuno");

		return map;
	}

	public ActionForward esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaElaborazioniDifferiteForm currentForm = (SinteticaElaborazioniDifferiteForm) form;

		if (!ValidazioneDati.isFilled(currentForm.getRichiesteSelez()) ) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.esaminaNoSelezione"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		preparaRichiestaDiDettaglio(currentForm, request);
		return mapping.findForward("esamina");
	}

	public ActionForward indietro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();
		return navi.goBack(true);
	}

	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages errors = new ActionMessages();
		SinteticaElaborazioniDifferiteForm currentForm = (SinteticaElaborazioniDifferiteForm) form;

		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar())
				return mapping.getInputForward();

			if (!currentForm.isSessione())
				currentForm.setSessione(true);

			if (request.getSession().getAttribute(Constants.CURRENT_MENU)
					.equals("menu.elaborazionidifferite.statoRichieste")
					&& currentForm.getCodiceBibl() == null
					&& navi.getActionCaller() == null)
				// si proviene dal menu
				// pulizia integrale delle variabili di sessione
				Pulisci.PulisciVar(request);

			String biblio = navi.getUtente().getCodBib();
			currentForm.setCodiceBibl(biblio);

			DescrittoreBloccoVO blocco1 = (DescrittoreBloccoVO) request
					.getAttribute(BatchDelegate.SINTETICA_RICHIESTE);

			RichiestaElaborazioniDifferiteVO ricerca =
				(RichiestaElaborazioniDifferiteVO) request.getAttribute(BatchDelegate.PARAMETRI_RICERCA);
			currentForm.setRicerca(ricerca);

			List<ElementoSinteticaElabDiffVO> lista;

			if (blocco1 != null && blocco1.getTotRighe() > 0) {
				currentForm.setIdLista(blocco1.getIdLista());
				currentForm.setTotRighe(blocco1.getTotRighe());
				currentForm.setTotBlocchi(blocco1.getTotBlocchi());
				currentForm.setNumBlocco(blocco1.getNumBlocco());
				currentForm.setMaxRighe(blocco1.getMaxRighe());

				if (blocco1.getTotRighe() == 1) {
					ElementoSinteticaElabDiffVO first = (ElementoSinteticaElabDiffVO) blocco1.getLista().get(0);
					currentForm.setRichiesteSelez(new String[]{first.getIdRichiesta()} );
				}

			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.elabdiff.nonTrovato"));
				this.saveErrors(request, errors);
				return navi.goBack(true);
			}

			lista = blocco1.getLista();
			currentForm.setListaRichieste(lista);

			return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.elabdiff.erroreNonGestito"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

	}

	private void preparaRichiestaDiDettaglio(
			SinteticaElaborazioniDifferiteForm form, HttpServletRequest request) {
		try {
			List<ElementoSinteticaElabDiffVO> elencoDettaglio = new ArrayList<ElementoSinteticaElabDiffVO>();
			List<ElementoSinteticaElabDiffVO> listaRichiesteOrdinata = form.getListaRichieste();

			for (ElementoSinteticaElabDiffVO richiesta : listaRichiesteOrdinata) {
				for (String idRichiesta : form.getRichiesteSelez())
					if (idRichiesta.equals(richiesta.getIdRichiesta())) {
						elencoDettaglio.add(richiesta);
						break;
					}
			}

			request.setAttribute(BatchDelegate.DETTAGLIO_BATCH,
				elencoDettaglio);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaElaborazioniDifferiteForm currentForm = (SinteticaElaborazioniDifferiteForm) form;

		int numBlocco = currentForm.getNumBlocco();
		String idLista = currentForm.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco > 1 && idLista != null) {
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();
			DescrittoreBloccoVO bloccoVO = factory.getElaborazioniDifferite()
					.nextBlocco(ticket, idLista, numBlocco);
			if (bloccoVO != null) {
				List<ElementoSinteticaElabDiffVO> lista = currentForm
						.getListaRichieste();
				lista.addAll(bloccoVO.getLista());
				Collections.sort(lista,	ElementoSinteticaElabDiffVO.ORDINA_PER_PROGRESSIVO);
			}
		}
		return mapping.getInputForward();
	}

	public ActionForward aggiorna(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SinteticaElaborazioniDifferiteForm currentForm = (SinteticaElaborazioniDifferiteForm) form;
		RichiestaElaborazioniDifferiteVO richiesta = currentForm.getRicerca();
		BatchDelegate delegate = BatchDelegate.getDelegate(request);
		DescrittoreBloccoVO blocco1 = delegate.cercaRichieste(richiesta);
		if (blocco1 == null)
			return mapping.getInputForward();

		if (blocco1 != null && blocco1.getTotRighe() > 0) {
			currentForm.setIdLista(blocco1.getIdLista());
			currentForm.setTotRighe(blocco1.getTotRighe());
			currentForm.setTotBlocchi(blocco1.getTotBlocchi());
			currentForm.setNumBlocco(blocco1.getNumBlocco());
			currentForm.setMaxRighe(blocco1.getMaxRighe());
			currentForm.setListaRichieste(blocco1.getLista());
			Navigation.getInstance(request).getCache().getCurrentElement().setInfoBlocchi(null);
		}

		return mapping.getInputForward();
	}

	public ActionForward tutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaElaborazioniDifferiteForm currentForm = (SinteticaElaborazioniDifferiteForm) form;
		List<ElementoSinteticaElabDiffVO> richieste = currentForm.getListaRichieste();
		int size = ValidazioneDati.size(richieste);
		if (size > 0) {
			String[] selected = new String[size];
			for (int idx = 0; idx < size; idx++)
				selected[idx] = richieste.get(idx).getIdRichiesta();

			currentForm.setRichiesteSelez(selected);
		}

		return mapping.getInputForward();
	}

	public ActionForward nessuno(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SinteticaElaborazioniDifferiteForm currentForm = (SinteticaElaborazioniDifferiteForm) form;
		currentForm.setRichiesteSelez(null);

		return mapping.getInputForward();
	}


}
