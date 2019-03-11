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
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElementoSinteticaElabDiffVO;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.web.actionforms.elaborazioniDifferite.DettaglioElaborazioniDifferiteForm;
import it.iccu.sbn.web.actions.acquisizioni.util.Pulisci;
import it.iccu.sbn.web.integration.bd.BatchDelegate;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

public class DettaglioElaborazioniDifferiteAction extends LookupDispatchAction {

	private static String[] BOTTONIERA = new String[] {
		"button.elimina", "button.indietro"
	};
	private static String[] BOTTONIERA_CONFERMA = new String[] { "button.si",
		"button.no", "button.indietro" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("button.elimina", "elimina");
		map.put("button.indietro", "indietro");
		map.put("button.si", "si");
		map.put("button.no", "no");

		map.put("button.selAllTitoli", "tutti");
		map.put("button.deSelAllTitoli", "nessuno");

		return map;
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioElaborazioniDifferiteForm currentForm = (DettaglioElaborazioniDifferiteForm) form;

		try {
			BatchDelegate delegate = BatchDelegate.getDelegate(request);
			List<ElementoSinteticaElabDiffVO> listaRichieste = currentForm.getListaRichieste();
			String[] richieste = currentForm.getRichiesteSelez();
			if (!delegate.eliminaRichiesteElaborazioniDifferite(richieste, null) )
				return mapping.getInputForward();

			for (String id : richieste) {
				Iterator<ElementoSinteticaElabDiffVO> iterator = listaRichieste.iterator();
				while (iterator.hasNext())
					if (iterator.next().getIdRichiesta().equals(id)) {
						iterator.remove();
						break;
					}
			}
			if (listaRichieste.size() > 0)
				return mapping.getInputForward();
			else
				return Navigation.getInstance(request).goToBookmark(BatchDelegate.RICERCA_BATCH_BOOKMARK, true);

		} catch (Exception e) {
			log.error(e);
			return mapping.getInputForward();
		} finally {

			currentForm.setConferma(false);
			currentForm.setPulsanti(BOTTONIERA);
		}

	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DettaglioElaborazioniDifferiteForm currentForm = (DettaglioElaborazioniDifferiteForm) form;
		currentForm.setConferma(false);
		currentForm.setPulsanti(BOTTONIERA);
		return mapping.getInputForward();
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

		DettaglioElaborazioniDifferiteForm currentForm = (DettaglioElaborazioniDifferiteForm) form;

		try {
			Navigation navi = Navigation.getInstance(request);
			if (navi.isFromBar())
				return mapping.getInputForward();

			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
				currentForm.setPulsanti(BOTTONIERA);
				currentForm.setRichiesteSelez(null);
				String biblio = navi.getUtente().getCodBib();
				currentForm.setCodiceBibl(biblio);
				List<ElementoSinteticaElabDiffVO> listaRichieste = (List<ElementoSinteticaElabDiffVO>) request.getAttribute(BatchDelegate.DETTAGLIO_BATCH);
				currentForm.setListaRichieste(listaRichieste);
				if (ValidazioneDati.isFilled(listaRichieste) && listaRichieste.size() == 1 )
					currentForm.setRichiesteSelez(new String[] { listaRichieste.get(0).getIdRichiesta() });
			}

			if (request.getSession().getAttribute(Constants.CURRENT_MENU)
					.equals("menu.elaborazionidifferite.statoRichieste")
					&& currentForm.getCodiceBibl() == null
					&& navi.getActionCaller() == null)
				// si proviene dal menu
				// pulizia integrale delle variabili di sessione
				Pulisci.PulisciVar(request);

			return mapping.getInputForward();
		}
		// altri tipi di errore
		catch (Exception e) {

			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.elabdiff.erroreNonGestito"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

	}

	public ActionForward elimina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		DettaglioElaborazioniDifferiteForm currentForm = (DettaglioElaborazioniDifferiteForm) form;
		String[] richiesteSelez = currentForm.getRichiesteSelez();
		if (!ValidazioneDati.isFilled(richiesteSelez)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.esaminaNoSelezione"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		String ids = "#";
		Iterator<String> iterator = Arrays.asList(richiesteSelez).iterator();
		for (;;) {
			ids += iterator.next();
			if (iterator.hasNext())
				ids += ", #";
			else break;
		}

		String codBib = Navigation.getInstance(request).getUtente().getCodBib();

		for (String idBatch : richiesteSelez ) {

			ElementoSinteticaElabDiffVO sintVO = getElementoSelezionato(idBatch, form);
			if (sintVO == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.esaminaNoSelezione"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			//check batch in esecuzione
			if (ValidazioneDati.equals(sintVO.getStato(), ConstantsJMS.STATO_EXEC) ) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.erroreCancellaBatchExec"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}

			//check batch altre biblioteche
			if (!ValidazioneDati.equals(sintVO.getBiblioteca(), codBib)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.erroreCancellaAltraBib"));
				this.saveErrors(request, errors);
				return mapping.getInputForward();
			}
		}

		currentForm.setConferma(true);
		currentForm.setPulsanti(BOTTONIERA_CONFERMA);
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.confermaCancellaBatch", ids));
		this.saveErrors(request, errors);
		return mapping.getInputForward();
	}

	private ElementoSinteticaElabDiffVO getElementoSelezionato(String idBatch,
			ActionForm form) {
		DettaglioElaborazioniDifferiteForm currentForm = (DettaglioElaborazioniDifferiteForm) form;
		List<ElementoSinteticaElabDiffVO> listaRichieste = currentForm.getListaRichieste();
		if (!ValidazioneDati.isFilled(listaRichieste))
			return null;

		for (ElementoSinteticaElabDiffVO e : listaRichieste)
			if (e.getIdRichiesta().equals(idBatch))
				return e;

		return null;
	}

	public ActionForward tutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DettaglioElaborazioniDifferiteForm currentForm = (DettaglioElaborazioniDifferiteForm) form;
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

		DettaglioElaborazioniDifferiteForm currentForm = (DettaglioElaborazioniDifferiteForm) form;
		currentForm.setRichiesteSelez(null);

		return mapping.getInputForward();
	}

}
