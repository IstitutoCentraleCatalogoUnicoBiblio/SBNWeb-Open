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
package it.iccu.sbn.web.actions.servizi.segnature;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.servizi.segnature.RangeSegnatureVO;
import it.iccu.sbn.web.actionforms.servizi.segnature.ListaSegnatureForm;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.ArrayList;
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

public class ListaSegnatureAction extends SegnatureAction {

	private static Logger log = Logger.getLogger(ListaSegnatureAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		//map.put("servizi.bottone.esamina", "ok");
		map.put("servizi.bottone.esamina", "Esamina");
		map.put("servizi.bottone.si", "si");
		map.put("servizi.bottone.no", "no");
		map.put("servizi.bottone.annulla", "annulla");
		map.put("servizi.bottone.nuovo", "nuovo");
		map.put("servizi.bottone.cancella", "cancella");
		map.put("button.blocco", "blocco");
		map.put("servizi.bottone.deselTutti", "deselTutti");
		map.put("servizi.bottone.selTutti",   "selTutti");
		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSegnatureForm currentForm = (ListaSegnatureForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione()) {

				currentForm.setSessione(true);
				currentForm.setConferma(false);
				currentForm.setAnaSegn((RangeSegnatureVO) request.getAttribute(ServiziDelegate.DETTAGLIO_SEGNATURA));
				log.debug("ListaSegnatureAction::unspecified");


				DescrittoreBloccoVO blocco1 = ServiziDelegate.getInstance(request).caricaSegnature(navi.getUtente()
					.getCodPolo(), navi.getUtente().getCodBib(),
					currentForm.getAnaSegn(), navi.getUserTicket());

				if (DescrittoreBloccoVO.isFilled(blocco1) ) {
					// abilito i tasti per il blocco se necessario
					currentForm.setAbilitaBlocchi((blocco1.getTotBlocchi() > 1));
					// memorizzo le informazioni per la gestione blocchi
					currentForm.setIdLista(blocco1.getIdLista());
					currentForm.setTotRighe(blocco1.getTotRighe());
					currentForm.setTotBlocchi(blocco1.getTotBlocchi());
					currentForm.setBloccoSelezionato(blocco1.getNumBlocco());
					currentForm.setMaxRighe(blocco1.getMaxRighe());
					currentForm.setListaSegnature(blocco1.getLista());
				}
				if (blocco1.getTotRighe() == 1)
					currentForm.setCodSelSegnSing("0");

				// gestione automatismo check su unico elemento lista
				if (ValidazioneDati.size(currentForm.getListaSegnature()) == 1)
					currentForm.setCodSelSegn(new Integer[] { ((RangeSegnatureVO)currentForm.getListaSegnature().get(0)).getId() });
			}

			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return this.backForward(request, true);
		}
	}

/*	public ActionForward ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSegnatureForm currentForm = (ListaSegnatureForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		if (currentForm.getCodSelSegnSing() != null
				&& currentForm.getCodSelSegnSing().length() > 0) {
			resetToken(request);

			request.setAttribute(ServiziDelegate.DETTAGLIO_SEGNATURA, this.passaSelezionato(
					currentForm.getListaSegnature(),
					currentForm.getCodSelSegnSing()).clone());

			return mapping.findForward("esamina");
		} else {
			// messaggio di errore.
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			saveToken(request);
			return mapping.getInputForward();
		}
	}*/

	public ActionForward Esamina(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListaSegnatureForm currentForm = (ListaSegnatureForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		if (currentForm.getCodSelSegn() != null
				&& currentForm.getCodSelSegn().length > 0) {
			resetToken(request);
			// carico il vo con i dati selezioonati
			currentForm.setSelectedOccup(currentForm.getCodSelSegn() );
			//currentForm.setAnaOccup(this.passaSelezionato(currentForm.getListaOccupazioni(), currentForm.getCodSelOccup()[0]));
			//currentForm.getAnaOccup().setnsetNewSpecialita(OccupazioneVO.OLD);

			List<RangeSegnatureVO> arrSegnSel = new ArrayList<RangeSegnatureVO>();
			for (int t = 0; t < currentForm.getCodSelSegn().length; t++) {
				RangeSegnatureVO  eleSegn=this.passaSelezionato(currentForm
						.getListaSegnature(),  currentForm.getSelectedOccup()[t]);
				arrSegnSel.add(eleSegn);
			}

			request.setAttribute("SegnSel", arrSegnSel);
			return mapping.findForward("esamina");
			// }
		} else {
			// messaggio di errore.
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			// nessun codice selezionato
			saveToken(request);
			return mapping.getInputForward();
		}
	}

	private RangeSegnatureVO passaSelezionato(List<RangeSegnatureVO> listaSegn,	Integer codSelSegn) {
		for (int i = 0; i < listaSegn.size(); i++) {
			RangeSegnatureVO currentVo = listaSegn
					.get(i);
			if (currentVo.getId() == codSelSegn) {
				return currentVo;
			}
		}
		return null;
	}

	public ActionForward deselTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSegnatureForm currentForm = (ListaSegnatureForm) form;
		try {
			currentForm.setCodSelSegn(null);
			return mapping.getInputForward();
		}
		catch (Exception ex) {
			return mapping.findForward("annulla");
		}
	}


	public ActionForward selTutti(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSegnatureForm currentForm = (ListaSegnatureForm) form;

		int numSegn = 0;
		try {
			List<RangeSegnatureVO> segnature = currentForm.getListaSegnature();
			numSegn = segnature.size();
			if (numSegn > 0) {
				Integer[] codSegn = new Integer[numSegn];
				for (int i = 0; i < numSegn; i++)
					codSegn[i] = segnature.get(i).getId();

				currentForm.setCodSelSegn(codSegn);
			}
		} catch (Exception ex) {
			return mapping.findForward("annulla");
		}

		return mapping.getInputForward();
	}



	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSegnatureForm currentForm = (ListaSegnatureForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		resetToken(request);
		RangeSegnatureVO newSegn = new RangeSegnatureVO(0, 0, "", "", "", "", "",	"");
		newSegn.setCodBiblioteca(currentForm.getAnaSegn().getCodBiblioteca());
		newSegn.setCodPolo(currentForm.getAnaSegn().getCodPolo());
		newSegn.setNewSegnatura(true);
		newSegn.setSegnInizio(currentForm.getAnaSegn().getSegnInizio());
		newSegn.setCodFruizione(currentForm.getAnaSegn().getCodFruizione());
		newSegn.setCodIndisp(currentForm.getAnaSegn().getCodIndisp());
		request.setAttribute(ServiziDelegate.DETTAGLIO_SEGNATURA, newSegn);
		return mapping.findForward("nuovo");
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSegnatureForm currentForm = (ListaSegnatureForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		currentForm.setConferma(false);
		// return mapping.findForward("annulla");
		return this.backForward(request, false);
	}

	public ActionForward cancella(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSegnatureForm currentForm = (ListaSegnatureForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		if (currentForm.getCodSelSegn() != null
				&& currentForm.getCodSelSegn().length > 0) {
			currentForm.setConferma(true);
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.confermaOperazione"));
			this.saveErrors(request, errors);
			this.saveMessages(request, ConfermaDati.preparaConferma(this,
					mapping, request));
			saveToken(request);
			currentForm.setConferma(true);

			currentForm.setCodSelSegnConferma(new Integer[currentForm
					.getCodSelSegn().length]);
			for (int i = 0; i < currentForm.getCodSelSegn().length; i++) {
				currentForm.getCodSelSegnConferma()[i] = currentForm
						.getCodSelSegn()[i];
			}

			return mapping.getInputForward();
		} else {
			// messaggio di errore.
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.codiceNessunaSelezione"));
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}
	}

	public ActionForward si(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSegnatureForm currentForm = (ListaSegnatureForm) form;
		try {
			if (!isTokenValid(request)) {
				saveToken(request);
			}
			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
			}

			// codice selezionati x cui effettuo operazione di cancellazione
			// this.cancmultipla(request,
			// getArrayIdSegnature(currentForm.getListaSegnature(),
			// currentForm.getCodSelSegnConferma()));
			this.cancmultipla(request, currentForm.getCodSelSegnConferma());

			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.servizi.codiceCancellazioneEffettuata"));
			this.saveErrors(request, errors);
			this.resetToken(request);
			currentForm.setConferma(false);
			return Navigation.getInstance(request).goBack(true);//this.unspecified(mapping, form, request, response);
		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

	private void cancmultipla(HttpServletRequest request, Integer[] codSelSegn)
			throws Exception {
		if (codSelSegn.length == 0)
			return;

		ServiziDelegate.getInstance(request)
				.cancellaSegnature(codSelSegn, Navigation.getInstance(request)
						.getUtente().getFirmaUtente());
	}

	public ActionForward no(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSegnatureForm currentForm = (ListaSegnatureForm) form;
		if (!isTokenValid(request)) {
			saveToken(request);
		}
		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}
		currentForm = (ListaSegnatureForm) form;
		// Viene settato il token per le transazioni successive
		this.saveToken(request);
		currentForm.setConferma(false);
		return mapping.getInputForward();
	}

	public ActionForward blocco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ListaSegnatureForm currentForm = (ListaSegnatureForm) form;
		if (!isTokenValid(request))
			saveToken(request);

		if (!currentForm.isSessione())
			currentForm.setSessione(true);

		int numBlocco = currentForm.getBloccoSelezionato();
		String idLista = currentForm.getIdLista();
		String ticket = Navigation.getInstance(request).getUserTicket();
		if (numBlocco > 1 && idLista != null) {
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			DescrittoreBloccoVO bloccoVO = delegate.caricaBlocco(ticket, idLista, numBlocco);
			if (bloccoVO != null) {
				currentForm.getListaSegnature().addAll(bloccoVO.getLista());
				// ho caricato tutte le righe sulla form
				if (currentForm.getListaSegnature().size() == bloccoVO.getTotRighe())
					currentForm.setAbilitaBlocchi(false);
			}
		}
		return mapping.getInputForward();
	}

}
