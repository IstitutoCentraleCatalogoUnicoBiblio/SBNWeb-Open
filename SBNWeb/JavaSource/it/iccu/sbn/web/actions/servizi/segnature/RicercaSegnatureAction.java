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
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.segnature.RangeSegnatureVO;
import it.iccu.sbn.web.actionforms.servizi.segnature.RicercaSegnatureForm;
import it.iccu.sbn.web.actions.servizi.util.FileConstants;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;

import java.rmi.RemoteException;
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

public class RicercaSegnatureAction extends SegnatureAction {

	private static Logger log = Logger.getLogger(RicercaSegnatureAction.class);

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("servizi.bottone.cerca", "cerca");
		map.put("servizi.bottone.nuovo", "nuovo");

		return map;
	}

	private void checkForm(HttpServletRequest request,
			RicercaSegnatureForm datiricerca) throws Exception {
		boolean error = false;
		error = ValidazioneDati.strIsNull(datiricerca.getBiblioteca());
		if (error) {
			throw new Exception();
		}
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// setto il token per le transazioni successive
		RicercaSegnatureForm currentForm = (RicercaSegnatureForm) form;
		Navigation navi = Navigation.getInstance(request);
		if (navi.isFromBar())
			return mapping.getInputForward();

		try {
			if (!isTokenValid(request))
				saveToken(request);

			if (!currentForm.isSessione()) {
				log.debug("RicercaSegnatureAction::unspecified");
				currentForm.setSessione(true);
				loadDefault(currentForm, request);
				this.loadTipiOrdinamento(currentForm, request);

				RangeSegnatureVO rs = currentForm.getDettRicercaSegn();
				rs.setCodBiblioteca(navi.getUtente().getCodBib());
				rs.setCodPolo(navi.getUtente().getCodPolo());
				rs.setElemPerBlocchi(ConstantDefault.ELEMENTI_BLOCCHI.getDefaultAsNumber());
				//almaviva5_20111014 #4675 ordinamento per segnatura asc
				rs.setTipoOrdinamento("1");

			}


			return mapping.getInputForward();

		} catch (Exception e) {
			resetToken(request);
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

	public ActionForward cerca(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaSegnatureForm currentForm = (RicercaSegnatureForm) form;
		try {
			if (Navigation.getInstance(request).isFromBar())
				return mapping.getInputForward();

			if (!isTokenValid(request))
				saveToken(request);


			if (!currentForm.isSessione()) {
				currentForm.setSessione(true);
			}

			this.checkForm(request, currentForm);

			RangeSegnatureVO segnatura = (RangeSegnatureVO) currentForm
					.getDettRicercaSegn().clone();
			if (segnatura.getSegnInizio() != null
					&& !segnatura.getSegnInizio().trim().equals("")) {
				segnatura.setSegnFine(segnatura.getSegnInizio());

				this.normalizzaSegnatura(segnatura);
			}

			request.setAttribute("PathForm", mapping.getPath());
			request.setAttribute(ServiziDelegate.DETTAGLIO_SEGNATURA, segnatura);

			if (caricaSegnature(request, form) )
				return mapping.findForward("ok");

			return mapping.getInputForward();

		} catch (Exception e) {
			log.error("", e);
			this.setErroreGenerico(request, e);
			return mapping.getInputForward();
		}
	}

	private boolean caricaSegnature(HttpServletRequest request,
			ActionForm form) throws RemoteException {

		RicercaSegnatureForm currentForm = (RicercaSegnatureForm) form;
		Navigation navi = Navigation.getInstance(request);

		if (request.getAttribute(ServiziDelegate.DETTAGLIO_SEGNATURA)!=null)
		{
			DescrittoreBloccoVO blocco1 = ServiziDelegate.getInstance(request).caricaSegnature(navi.getUtente()
					.getCodPolo(), navi.getUtente().getCodBib(),
					(RangeSegnatureVO) request.getAttribute(ServiziDelegate.DETTAGLIO_SEGNATURA), navi.getUserTicket());

			if (blocco1 != null && blocco1.getTotRighe() > 0) {
				request.setAttribute(ServiziDelegate.LISTA_SEGNATURE, blocco1);
				return true;
			}

		}

		ActionMessages errors = new ActionMessages();
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.servizi.ListaVuota"));
		currentForm.setNonTrovato(true);
		this.saveErrors(request, errors);
		return false;
	}


	public ActionForward nuovo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RicercaSegnatureForm currentForm = (RicercaSegnatureForm) form;

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		if (!isTokenValid(request)) {
			saveToken(request);
		}

		if (!currentForm.isSessione()) {
			currentForm.setSessione(true);
		}

		currentForm.getDettRicercaSegn().setNewSegnatura(FileConstants.NEW);
		currentForm.setNonTrovato(false);
		request.setAttribute(ServiziDelegate.DETTAGLIO_SEGNATURA, currentForm.getDettRicercaSegn().clone());

		return mapping.findForward("nuovo");
	}

	private void loadDefault(RicercaSegnatureForm currentForm, HttpServletRequest request) throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		currentForm.setLstFruizioni(delegate.loadCodiciCategoriaDiFruizione());
		currentForm.setLstIndisp(delegate.loadCodiciNonDisponibilita());
	}

	private void loadTipiOrdinamento(RicercaSegnatureForm currentForm, HttpServletRequest request)
			throws Exception {
		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		List<ComboCodDescVO> listaOrd;
		try {
			listaOrd = delegate.loadCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_SEGNATURE);
			if (ValidazioneDati.size(listaOrd) > 1)
				listaOrd = CaricamentoCombo.cutFirst(listaOrd);
		} catch (Exception e) {
			listaOrd = ValidazioneDati.emptyList();
		}

		currentForm.getDettRicercaSegn().setLstTipiOrdinamento(listaOrd);
	}
}
